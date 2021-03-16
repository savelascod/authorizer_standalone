package authorization.project.delegate.impl;

import authorization.project.constant.AccountError;
import authorization.project.delegate.contract.ITransactionDelegate;
import authorization.project.dto.domain.AccountDto;
import authorization.project.dto.domain.TransactionDto;
import authorization.project.exception.BusinessException;
import authorization.project.service.contract.IAccountService;
import authorization.project.service.contract.ITransactionService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

/**
 * Delegate instance of {@link ITransactionDelegate}, to call services and perform transaction's logic.
 *
 * @version 1.0
 * @since 1.0
 */
public class TransactionDelegate implements ITransactionDelegate {

    /**
     * instance of {@link ITransactionService}
     */
    private final ITransactionService transactionService;

    /**
     * instance of {@link IAccountService}
     */
    private final IAccountService accountService;

    /**
     * Constructor with autowired dependencies
     */
    public TransactionDelegate(
            ITransactionService transactionService,
            IAccountService accountService) {
        this.accountService = accountService;
        this.transactionService = transactionService;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<BusinessException> performTransactionAuthorization(TransactionDto transactionDto) {
        Supplier<List<BusinessException>> accountValidations = () -> {
            List<BusinessException> businessErrors = new ArrayList<>();
            if (!accountService.isAccountInitialized())
                businessErrors.add(new BusinessException(AccountError.ERROR_2.getMessage()));
            else if (!accountService.isCardActive())
                businessErrors.add(new BusinessException(AccountError.ERROR_1.getMessage()));
            return businessErrors;
        };
        Supplier<Integer> accountId = () -> accountService.getAccountId().get();
        Supplier<Optional<AccountDto>> accountDto = accountService::getAccountStatus;
        return transactionService.authorizeTransaction(transactionDto, accountId, accountValidations, accountDto);
    }

}
