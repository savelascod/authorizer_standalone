package authorization.project.service.impl;

import authorization.project.constant.TransactionError;
import authorization.project.dto.domain.AccountDto;
import authorization.project.dto.domain.TransactionDto;
import authorization.project.persistence.entity.TransactionEntity;
import authorization.project.persistence.repository.ITransactionRepository;
import authorization.project.service.contract.ITransactionService;
import authorization.project.exception.BusinessException;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * Service implementation of {@link ITransactionService}
 *
 * @version 1.0
 * @since 1.0
 */
public class TransactionService implements ITransactionService {

    /**
     * Repository class to perform operations in TRANSACTION ENTITIES
     */
    private final ITransactionRepository transactionRepository;
    /**
     * Max number of operations that can be performed in MINUTES_INTERVAL
     */
    private int MAX_TRANSACTIONS_INTERVAL = 3;
    /**
     * Time interval in minutes
     */
    private int MINUTES_INTERVAL = 2;

    /**
     * Constructor with autowired dependencies
     */
    public TransactionService(ITransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<BusinessException> authorizeTransaction(
            TransactionDto transactionDto,
            Supplier<Integer> accountId,
            Supplier<List<BusinessException>> accountValidations,
            Supplier<Optional<AccountDto>> getAccountStatus) {
        List<BusinessException> businessErrors = accountValidations.get();
        Optional<AccountDto> accountDto = getAccountStatus.get();
        if (accountDto.isPresent() && transactionDto.getAmount() > accountDto.get().getAvailableLimit())
            businessErrors.add(new BusinessException(TransactionError.ERROR_2.getMessage()));
        if (getAllTransactionsFromLastMinutes(transactionDto.getTime()).size() >= MAX_TRANSACTIONS_INTERVAL)
            businessErrors.add(new BusinessException(TransactionError.ERROR_0.getMessage()));
        if (!getAllTransactionsWithSameAmountAndMerchant(transactionDto).isEmpty())
            businessErrors.add(new BusinessException(TransactionError.ERROR_1.getMessage()));
        if (businessErrors.isEmpty()) transactionRepository.save(
                TransactionEntity.builder()
                        .date(transactionDto.getTime())
                        .amount(transactionDto.getAmount())
                        .destinyAccountId(accountId.get())
                        .merchantName(transactionDto.getMerchant())
                        .build()
        );
        return businessErrors;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public List<TransactionDto> getAllTransactionsFromLastMinutes(OffsetDateTime endDateTime) {
        return transactionRepository
                .findAllByDateLessThanEqualAndDateGreaterThanEqual(endDateTime,
                        endDateTime.minusMinutes(MINUTES_INTERVAL))
                .stream().map(entity -> TransactionDto.builder()
                        .amount(entity.getAmount()).time(entity.getDate())
                        .merchant(entity.getMerchantName())
                        .build()).collect(Collectors.toList());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<TransactionDto> getAllTransactionsWithSameAmountAndMerchant(TransactionDto transactionDto) {
        return transactionRepository
                .findAllByAmountEqualsAndMerchantNameEquals(transactionDto.getAmount(), transactionDto.getMerchant())
                .stream().map(entity -> TransactionDto.builder()
                        .amount(entity.getAmount()).time(entity.getDate())
                        .merchant(entity.getMerchantName())
                        .build()).collect(Collectors.toList());
    }

}
