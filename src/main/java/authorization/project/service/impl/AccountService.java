package authorization.project.service.impl;

import authorization.project.constant.AccountError;
import authorization.project.dto.domain.AccountDto;
import authorization.project.exception.BusinessException;
import authorization.project.persistence.entity.AccountEntity;
import authorization.project.persistence.repository.IAccountRepository;
import authorization.project.service.contract.IAccountService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Service implementation of {@link IAccountService}
 *
 * @version 1.0
 * @since 1.0
 */
public class AccountService implements IAccountService {

    /**
     * Repository class to perform operations in ACCOUNT ENTITIES
     */
    private final IAccountRepository accountRepository;

    /**
     * Constructor with autowired dependencies
     */
    public AccountService(IAccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isAccountInitialized() {
        return accountRepository.count() > 0;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<BusinessException> createAccount(AccountDto accountDto) {
        List<BusinessException> businessErrors = new ArrayList<>();
        if (isAccountInitialized()) {
            businessErrors.add(new BusinessException(AccountError.ERROR_0.getMessage()));
        } else {
            accountRepository.save(AccountEntity.builder()
                    .activeCard(accountDto.getActiveCard())
                    .availableLimit(accountDto.getAvailableLimit()).build());
        }
        return businessErrors;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<AccountDto> getAccountStatus() {
        Optional<AccountEntity> accountEntity = accountRepository.findFirstByIdIsNotNull();
        return accountEntity.map(entity ->
                AccountDto.builder().activeCard(entity.getActiveCard()).availableLimit(entity.getAvailableLimit()).build());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isCardActive() {
        Optional<AccountDto> accountDto = getAccountStatus();
        return accountDto.isPresent() && accountDto.get().getActiveCard();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<Integer> getAccountId() {
        return accountRepository.findFirstByIdIsNotNull().map(accountEntity ->
                Optional.of(accountEntity.getId())).orElse(Optional.empty());
    }

}
