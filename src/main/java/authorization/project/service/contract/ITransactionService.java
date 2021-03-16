package authorization.project.service.contract;

import authorization.project.dto.domain.AccountDto;
import authorization.project.dto.domain.TransactionDto;
import authorization.project.exception.BusinessException;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

/**
 * Contract that defines the operations to be performed in transactions
 *
 * @version 1.0
 * @since 1.0
 */
public interface ITransactionService {

    /**
     * Method that performs the business login needed to authorize a transaction
     *
     * @param transactionDto     instance of {@link TransactionDto}
     * @param accountId          function to return the accountId
     * @param accountValidations function to perform bussiness login validation on accounts
     * @param getAccountStatus   function to retrieve the current account status.
     * @return a list of {@link BusinessException} in case of business errors
     */
    List<BusinessException> authorizeTransaction(
            TransactionDto transactionDto,
            Supplier<Integer> accountId,
            Supplier<List<BusinessException>> accountValidations,
            Supplier<Optional<AccountDto>> getAccountStatus);

    /**
     * Method that retrieves all transactions from the last minutes given the endDate
     *
     * @param endDate -> Upper bound of search
     * @return a List of {@link TransactionDto} with transactions present in range.
     */
    List<TransactionDto> getAllTransactionsFromLastMinutes(OffsetDateTime endDate);

    /**
     * Method that retrieves all authorized transactions with the same Merchant name and amount
     *
     * @param transactionDto -> instance of {@link TransactionDto} with the information to be searched
     * @return a List of {@link TransactionDto} that comply with the condition
     */
    List<TransactionDto> getAllTransactionsWithSameAmountAndMerchant(TransactionDto transactionDto);
}
