package authorization.project.service;

import authorization.project.service.impl.AccountService;
import authorization.project.constant.AccountError;
import authorization.project.dto.domain.AccountDto;
import authorization.project.exception.BusinessException;
import authorization.project.persistence.entity.AccountEntity;
import authorization.project.persistence.repository.IAccountRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit and integration tests for {@link AccountService}
 *
 * @version 1.0
 * @since 1.0
 */
@ExtendWith(MockitoExtension.class)
public class AccountServiceTest {

    /**
     * Dependency loading
     */
    @Mock
    private IAccountRepository accountRepository;

    /**
     * Class Under Test
     */
    @InjectMocks
    private AccountService accountService;

    @Test
    public void contextLoads() {
        assertNotNull(accountService);
        assertNotNull(accountRepository);
    }

    /**
     * <b>Given<b/> an AccountDto object
     * <b>When<b/> createAccount method is called
     * <b>Then<b/> should create account if no Account have been previously initialized
     */
    @Test
    public void givenAnAccountDto_whenCreateAccountIsCalled_shouldCreateAccountIfNoAccountIsInitialized() {
        when(accountRepository.count()).thenReturn(Long.valueOf(0));
        AccountDto accountDto = AccountDto.builder().activeCard(true).availableLimit(100).build();
        List<BusinessException> businessExceptions = accountService.createAccount(accountDto);
        assertNotNull(businessExceptions);
        assertTrue(businessExceptions.isEmpty());
        verify(accountRepository, times(1)).save(AccountEntity.builder()
                .availableLimit(accountDto.getAvailableLimit())
                .activeCard(accountDto.getActiveCard()).build());
        verifyNoMoreInteractions(accountRepository);
    }

    /**
     * <b>When<b/> isAccountInitialized method is called
     * <b>Then<b/> should return true if there is an stored account
     */
    @Test
    public void whenIsAccountInitialized_shouldReturnTrueIfThereIsAnStoredAccount() {
        when(accountRepository.count()).thenReturn(Long.valueOf(1));
        assertTrue(accountService.isAccountInitialized());
        verifyNoMoreInteractions(accountRepository);
    }

    /**
     * <b>When<b/> isAccountInitialized method is called
     * <b>Then<b/> should return false if there is an stored account
     */
    @Test
    public void whenIsAccountInitialized_shouldReturnFalseIfThereIsAnStoredAccount() {
        when(accountRepository.count()).thenReturn(Long.valueOf(0));
        assertFalse(accountService.isAccountInitialized());
        verifyNoMoreInteractions(accountRepository);
    }

    /**
     * <b>Given<b/> an AccountDto object
     * <b>When<b/> createAccount method is called
     * <b>Then<b/> should not create account if no Account have been previously initialized
     */
    @Test
    public void givenAnAccountDto_whenCreateAccountIsCalled_shouldNotCreateAccountIfNoAccountIsInitialized() {
        when(accountRepository.count()).thenReturn(Long.valueOf(1));
        AccountDto accountDto = AccountDto.builder().activeCard(true).availableLimit(100).build();
        List<BusinessException> businessExceptions = accountService.createAccount(accountDto);
        assertNotNull(businessExceptions);
        assertTrue(!businessExceptions.isEmpty());
        businessExceptions.forEach(businessException ->
                assertTrue(businessException.getMessage().equals(AccountError.ERROR_0.getMessage())));
        verifyNoMoreInteractions(accountRepository);
    }

    /**
     * <b>When<b/> cetAccountStatus method is called
     * <b>Then<b/> should return an Optional of AccountDTO if there is an Account
     */
    @Test
    public void whenGetAccountStatusIsCalled_shouldReturnAnOptionalOfAccountDTOIfAccountExists() {
        AccountEntity accountEntity = AccountEntity.builder()
                .activeCard(true).availableLimit(200).id(1).build();
        when(accountRepository.findFirstByIdIsNotNull()).thenReturn(Optional.of(accountEntity));
        Optional<AccountDto> accountDtoOptional = accountService.getAccountStatus();
        assertTrue(accountDtoOptional.isPresent());
        assertTrue(accountDtoOptional.get().getActiveCard().equals(accountEntity.getActiveCard()));
        assertTrue(accountDtoOptional.get().getAvailableLimit().equals(accountEntity.getAvailableLimit()));
        verifyNoMoreInteractions(accountRepository);
    }

    /**
     * <b>When<b/> cetAccountStatus method is called
     * <b>Then<b/> should return an Optional Empty of AccountDTO if there is no an Account
     */
    @Test
    public void whenGetAccountStatusIsCalled_shouldReturnAnOptionalEmptyOfAccountDTOIfAccountNotExists() {
        when(accountRepository.findFirstByIdIsNotNull()).thenReturn(Optional.empty());
        Optional<AccountDto> accountDtoOptional = accountService.getAccountStatus();
        assertFalse(accountDtoOptional.isPresent());
        verifyNoMoreInteractions(accountRepository);
    }

    /**
     * <b>When<b/> isCardActive method is called
     * <b>Then<b/> should return True If Account has active card
     */
    @Test
    public void whenIsCardActiveCalled_shouldReturnTrueIfAccountIsPresentAndHasActiveCard() {
        when(accountRepository.findFirstByIdIsNotNull())
                .thenReturn(Optional.of(AccountEntity.builder()
                        .activeCard(true).availableLimit(200).id(1).build()));
        assertTrue(accountService.isCardActive());
        verifyNoMoreInteractions(accountRepository);
    }

    /**
     * <b>When<b/> isCardActive method is called
     * <b>Then<b/> should return False If Account is not present
     */
    @Test
    public void whenIsCardActiveCalled_shouldReturnFalseIfAccountIsNotPresent() {
        when(accountRepository.findFirstByIdIsNotNull())
                .thenReturn(Optional.empty());
        assertFalse(accountService.isCardActive());
        verifyNoMoreInteractions(accountRepository);
    }

    /**
     * <b>When<b/> isCardActive method is called
     * <b>Then<b/> should return False If Account is present but has no active card
     */
    @Test
    public void whenIsCardActiveCalled_shouldReturnFalseIfAccountIsPresentAndHasNotActiveCard() {
        when(accountRepository.findFirstByIdIsNotNull())
                .thenReturn(Optional.of(AccountEntity.builder()
                        .activeCard(false).availableLimit(200).id(1).build()));
        assertFalse(accountService.isCardActive());
        verifyNoMoreInteractions(accountRepository);
    }

    /**
     * <b>When<b/> getAccountId method is called
     * <b>Then<b/> should Optional of Integer with value if account exists
     */
    @Test
    public void whenGetAccountId_shouldReturnOptionalIntegerIfAccountExists() {
        when(accountRepository.findFirstByIdIsNotNull())
                .thenReturn(Optional.of(AccountEntity.builder()
                        .activeCard(false).availableLimit(200).id(1).build()));
        Optional<Integer> accountId = accountService.getAccountId();
        assertTrue(accountId.isPresent());
        assertTrue(accountId.get().equals(1));
        verifyNoMoreInteractions(accountRepository);
    }

    /**
     * <b>When<b/> getAccountId method is called
     * <b>Then<b/> should Optional of Empty with value if account not exists
     */
    @Test
    public void whenGetAccountId_shouldReturnOptionalEmptyIfAccountNotExists() {
        when(accountRepository.findFirstByIdIsNotNull())
                .thenReturn(Optional.empty());
        Optional<Integer> accountId = accountService.getAccountId();
        assertFalse(accountId.isPresent());
        verifyNoMoreInteractions(accountRepository);
    }

}
