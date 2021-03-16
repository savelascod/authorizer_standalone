package authorization.project.delegate.contract;

import authorization.project.dto.domain.TransactionDto;
import authorization.project.exception.BusinessException;

import java.util.List;

/**
 * Contract that specifies operations to be performed in over transactions
 *
 * @version 1.0
 * @since 1.0
 */
public interface ITransactionDelegate {
    /**
     * Method that receives a transaction and perform all required operations
     *
     * @param transactionDto -> instance of {@link TransactionDto}
     * @return a List of {@link BusinessException} if are present.
     */
    List<BusinessException> performTransactionAuthorization(TransactionDto transactionDto);
}
