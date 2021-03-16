package authorization.project.service.contract;

import authorization.project.dto.domain.AccountDto;
import authorization.project.exception.BusinessException;

import java.util.List;
import java.util.Optional;

/**
 * Contract that defines the operations to be performed in accounts
 *
 * @version 1.0
 * @since 1.0
 */
public interface IAccountService {

    /**
     * Method that creates an Account
     *
     * @param accountDto -> instance of {@link AccountDto}
     * @return a list with business errors
     */
    List<BusinessException> createAccount(AccountDto accountDto);

    /**
     * Method that checks if there is already an account created
     *
     * @return true of false, whether an account was created or not.
     */
    boolean isAccountInitialized();

    /**
     * Method that return the current status of the stored account
     *
     * @return an Optional instance of {@link AccountDto} with the account information
     */
    Optional<AccountDto> getAccountStatus();

    /**
     * Determines if the current account has a card active
     *
     * @return true of false, whether an account has an active card or not.
     */
    boolean isCardActive();

    /**
     * Return the id of the first account stored in database
     */
    Optional<Integer> getAccountId();
}
