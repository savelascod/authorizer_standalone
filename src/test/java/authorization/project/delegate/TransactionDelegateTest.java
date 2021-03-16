package authorization.project.delegate;

import authorization.project.delegate.impl.TransactionDelegate;
import authorization.project.persistence.entity.AccountEntity;
import authorization.project.persistence.entity.TransactionEntity;
import authorization.project.persistence.repository.ITransactionRepository;
import authorization.project.service.impl.AccountService;
import authorization.project.constant.AccountError;
import authorization.project.dto.domain.TransactionDto;
import authorization.project.exception.BusinessException;
import authorization.project.persistence.repository.IAccountRepository;
import authorization.project.service.impl.TransactionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Unit and integration tests for {@link TransactionService}
 *
 * @version 1.0
 * @since 1.0
 */
@ExtendWith(MockitoExtension.class)
public class TransactionDelegateTest {

    /**
     * Mocks for transitive dependencies (2-level)
     */
    @Mock
    private IAccountRepository accountRepository;
    @Mock
    private ITransactionRepository transactionRepository;

    /**
     * Service Instances
     */
    @InjectMocks
    private TransactionService transactionService;
    @InjectMocks
    private AccountService accountService;

    /**
     * Class under test
     */
    private TransactionDelegate transactionDelegate;

    @BeforeEach
    public void setUp() {
        transactionDelegate = new TransactionDelegate(transactionService, accountService);
    }

    @Test
    public void contextLoads() {
        assertNotNull(accountRepository);
        assertNotNull(transactionRepository);
        assertNotNull(transactionService);
        assertNotNull(accountService);
        assertNotNull(transactionDelegate);
    }

    /**
     * <b>Given<b/> a TransactionDto object
     * <b>When<b/> performTransactionAuthorization method is called
     * <b>Then<b/> should authorize transaction if there is no error of any type
     */
    @Test
    public void
    givenATransactionDto_whenPerformTransactionAuthorizationCalled_thenShouldAuthorizeTransaction() {
        OffsetDateTime date = OffsetDateTime.now();
        Integer amount = 100;
        String merchant = "Burger King";
        TransactionDto transactionDto = TransactionDto.builder()
                .time(date)
                .amount(amount)
                .merchant(merchant)
                .build();
        when(accountRepository.count()).thenReturn(Long.valueOf(1));
        when(accountRepository.findFirstByIdIsNotNull()).thenReturn(Optional.of(
                AccountEntity.builder().activeCard(true).id(1).availableLimit(200).build()
        ));
        when(transactionRepository.findAllByDateLessThanEqualAndDateGreaterThanEqual(date, date.minusMinutes(2)))
                .thenReturn(new ArrayList<>());
        when(transactionRepository
                .findAllByAmountEqualsAndMerchantNameEquals(amount, merchant))
                .thenReturn(new ArrayList<>());
        List<BusinessException> businessErrors =
                transactionDelegate.performTransactionAuthorization(transactionDto);
        assertTrue(businessErrors.isEmpty());
        verify(transactionRepository, times(1)).save(any(TransactionEntity.class));
    }

    /**
     * <b>Given<b/> a TransactionDto object
     * <b>When<b/>  account is not initialized
     * <b>Then<b/> should not authorize transaction if there is no error of any type
     */
    @Test
    public void
    givenATransactionDto_whenAccountNotInit_thenShouldNotAuthorizeTransaction() {
        OffsetDateTime date = OffsetDateTime.now();
        Integer amount = 100;
        String merchant = "Burger King";
        TransactionDto transactionDto = TransactionDto.builder()
                .time(date)
                .amount(amount)
                .merchant(merchant)
                .build();
        when(accountRepository.count()).thenReturn(Long.valueOf(0));
        when(accountRepository.findFirstByIdIsNotNull()).thenReturn(Optional.of(
                AccountEntity.builder().activeCard(true).id(1).availableLimit(200).build()
        ));
        when(transactionRepository.findAllByDateLessThanEqualAndDateGreaterThanEqual(date, date.minusMinutes(2)))
                .thenReturn(new ArrayList<>());
        when(transactionRepository
                .findAllByAmountEqualsAndMerchantNameEquals(amount, merchant))
                .thenReturn(new ArrayList<>());
        List<BusinessException> businessErrors =
                transactionDelegate.performTransactionAuthorization(transactionDto);
        assertFalse(businessErrors.isEmpty());
        assertTrue(businessErrors.get(0).getMessage().equals(AccountError.ERROR_2.getMessage()));
        verify(transactionRepository, times(0)).save(any(TransactionEntity.class));
    }

    /**
     * <b>Given<b/> a TransactionDto object
     * <b>When<b/>  account has not Active cards
     * <b>Then<b/> should not authorize transaction if there is no error of any type
     */
    @Test
    public void
    givenATransactionDto_whenAccountHasNotActiveCards_thenShouldNotAuthorizeTransaction() {
        OffsetDateTime date = OffsetDateTime.now();
        Integer amount = 100;
        String merchant = "Burger King";
        TransactionDto transactionDto = TransactionDto.builder()
                .time(date)
                .amount(amount)
                .merchant(merchant)
                .build();
        when(accountRepository.count()).thenReturn(Long.valueOf(1));
        when(accountRepository.findFirstByIdIsNotNull()).thenReturn(Optional.of(
                AccountEntity.builder().activeCard(false).id(1).availableLimit(200).build()
        ));
        when(transactionRepository.findAllByDateLessThanEqualAndDateGreaterThanEqual(date, date.minusMinutes(2)))
                .thenReturn(new ArrayList<>());
        when(transactionRepository
                .findAllByAmountEqualsAndMerchantNameEquals(amount, merchant))
                .thenReturn(new ArrayList<>());
        List<BusinessException> businessErrors =
                transactionDelegate.performTransactionAuthorization(transactionDto);
        assertFalse(businessErrors.isEmpty());
        assertTrue(businessErrors.get(0).getMessage().equals(AccountError.ERROR_1.getMessage()));
        verify(transactionRepository, times(0)).save(any(TransactionEntity.class));
    }


}
