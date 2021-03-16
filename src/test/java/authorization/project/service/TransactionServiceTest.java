package authorization.project.service;

import authorization.project.persistence.entity.TransactionEntity;
import authorization.project.persistence.repository.ITransactionRepository;
import authorization.project.constant.AccountError;
import authorization.project.constant.TransactionError;
import authorization.project.dto.domain.AccountDto;
import authorization.project.dto.domain.TransactionDto;
import authorization.project.exception.BusinessException;
import authorization.project.service.impl.TransactionService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

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
public class TransactionServiceTest {

    /**
     * Dependency loading
     */
    @Mock
    private ITransactionRepository transactionRepository;

    /**
     * Outsider class for integration testing
     */
    @InjectMocks
    private TransactionService transactionService;

    @Test
    public void contextLoads() {
        assertNotNull(transactionRepository);
        assertNotNull(transactionService);
    }


    /**
     * <b>When<b/> getAllTransactionsWithSameAmountAndMerchant method is called
     * <b>Then<b/> should return list of TransactionDto if found
     */
    @Test
    public void
    whenGetAllTransactionsWithSameAmountAndMerchantCalled_shouldReturnAListOfTransactionDtoIfFound() {
        Integer amount = 100;
        String merchant = "Burger King";
        List<TransactionEntity> transactionEntityList = new ArrayList<>();
        transactionEntityList.add(TransactionEntity.builder().amount(amount).merchantName(merchant).build());
        when(transactionRepository.findAllByAmountEqualsAndMerchantNameEquals(amount, merchant))
                .thenReturn(transactionEntityList);
        List<TransactionDto> transactionDtoList =
                transactionService.getAllTransactionsWithSameAmountAndMerchant(
                        TransactionDto.builder().merchant(merchant).amount(amount).build());
        assertFalse(transactionDtoList.isEmpty());
        assertEquals(transactionDtoList.size(), 1);
        assertEquals(transactionDtoList.get(0).getAmount(), transactionEntityList.get(0).getAmount());
        assertEquals(transactionDtoList.get(0).getMerchant(), transactionEntityList.get(0).getMerchantName());
    }

    /**
     * <b>When<b/> getAllTransactionsWithSameAmountAndMerchant method is called
     * <b>Then<b/> should return empty list of TransactionDto if not found
     */
    @Test
    public void
    whenGetAllTransactionsWithSameAmountAndMerchantCalled_shouldReturnAnEmptyListIfNotFound() {
        Integer amount = 100;
        String merchant = "Burger King";
        List<TransactionEntity> transactionEntityList = new ArrayList<>();
        transactionEntityList.add(TransactionEntity.builder().amount(amount).merchantName(merchant).build());
        when(transactionRepository.findAllByAmountEqualsAndMerchantNameEquals(amount, merchant))
                .thenReturn(new ArrayList<>());
        List<TransactionDto> transactionDtoList =
                transactionService.getAllTransactionsWithSameAmountAndMerchant(
                        TransactionDto.builder().merchant(merchant).amount(amount).build());
        assertTrue(transactionDtoList.isEmpty());
    }

    /**
     * <b>Given<b/> MINUTES_INTERVAL Equal to 2
     * <b>When<b/> getAllTransactionsFromLastMinutes method is called
     * <b>Then<b/> should return list of TransactionDto if found
     */
    @Test
    public void givenMinutesIntervalEqualTwo_whenGetAllTransactionsFromLastMinutesCalled_shouldReturnAListOfTransactionDtoIfFound() {
        Integer amount = 100;
        String merchant = "Burger King";
        OffsetDateTime endDate = OffsetDateTime.now();
        OffsetDateTime startDate = endDate.minusMinutes(2);
        List<TransactionEntity> transactionEntityList = new ArrayList<>();
        transactionEntityList.add(TransactionEntity.builder().amount(amount).merchantName(merchant).build());
        when(transactionRepository.findAllByDateLessThanEqualAndDateGreaterThanEqual(endDate, startDate))
                .thenReturn(transactionEntityList);
        List<TransactionDto> transactionDtoList = transactionService.getAllTransactionsFromLastMinutes(endDate);
        assertFalse(transactionDtoList.isEmpty());
        assertEquals(transactionDtoList.size(), 1);
        assertEquals(transactionDtoList.get(0).getAmount(), transactionEntityList.get(0).getAmount());
        assertEquals(transactionDtoList.get(0).getMerchant(), transactionEntityList.get(0).getMerchantName());
    }

    /**
     * <b>Given<b/> MINUTES_INTERVAL Equal to 2
     * <b>When<b/> getAllTransactionsFromLastMinutes method is called
     * <b>Then<b/> should return empty list of TransactionDto if Not found
     */
    @Test
    public void givenMinutesIntervalEqualTwo_whenGetAllTransactionsFromLastMinutesCalled_shouldReturnAnEmptyListIfNotFound() {
        Integer amount = 100;
        String merchant = "Burger King";
        OffsetDateTime endDate = OffsetDateTime.now();
        OffsetDateTime startDate = endDate.minusMinutes(2);
        List<TransactionEntity> transactionEntityList = new ArrayList<>();
        transactionEntityList.add(TransactionEntity.builder().amount(amount).merchantName(merchant).build());
        when(transactionRepository.findAllByDateLessThanEqualAndDateGreaterThanEqual(endDate, startDate))
                .thenReturn(new ArrayList<>());
        List<TransactionDto> transactionDtoList = transactionService.getAllTransactionsFromLastMinutes(endDate);
        assertTrue(transactionDtoList.isEmpty());
    }

    /**
     * <b>Given<b/> MINUTES_INTERVAL Equal to 2 and MAX_TRANSACTIONS_INTERVAL equal to 3
     * <b>Then<b/> should authorize transaction if comply with all conditions
     */
    @Test
    public void givenMinIntervalTwoAndMaxTransactionsThree_shouldAuthorizeTransactionIfComplyAllConditions() {
        OffsetDateTime endDate = OffsetDateTime.now();
        OffsetDateTime startDate = endDate.minusMinutes(2);

        when(transactionRepository.findAllByDateLessThanEqualAndDateGreaterThanEqual(endDate, startDate))
                .thenReturn(new ArrayList<>());

        TransactionDto transactionDto = TransactionDto.builder().amount(20).time(endDate).merchant("Burger King").build();
        Supplier<Integer> accountId = () -> 0;
        Supplier<Optional<AccountDto>> getAccountStatus = () ->
                Optional.of(AccountDto.builder().activeCard(true).availableLimit(200).build());
        Supplier<List<BusinessException>> accountValidations = ArrayList::new;

        List<BusinessException> businessExceptions = transactionService
                .authorizeTransaction(transactionDto, accountId, accountValidations, getAccountStatus);
        assertTrue(businessExceptions.isEmpty());
        verify(transactionRepository, times(1)).save(any());
    }


    /**
     * <b>Given<b/> MINUTES_INTERVAL Equal to 2 and MAX_TRANSACTIONS_INTERVAL equal to 3
     * <b>When<b/> There exists Business errors in Account
     * <b>Then<b/> should not authorize
     */
    @Test
    public void givenMinIntervalTwoAndMaxTransactionsThree_whenAccountErrors_shouldNotAuthorizeTransaction() {
        OffsetDateTime endDate = OffsetDateTime.now();
        OffsetDateTime startDate = endDate.minusMinutes(2);

        List<BusinessException> accountErrors = new ArrayList<>();
        accountErrors.add(new BusinessException(AccountError.ERROR_1.getMessage()));
        accountErrors.add(new BusinessException(AccountError.ERROR_2.getMessage()));

        when(transactionRepository.findAllByDateLessThanEqualAndDateGreaterThanEqual(endDate, startDate))
                .thenReturn(new ArrayList<>());

        TransactionDto transactionDto = TransactionDto.builder().amount(20).time(endDate).merchant("Burger King").build();
        Supplier<Integer> accountId = () -> 0;
        Supplier<Optional<AccountDto>> getAccountStatus = () ->
                Optional.of(AccountDto.builder().activeCard(true).availableLimit(200).build());
        Supplier<List<BusinessException>> accountValidations = () -> accountErrors;

        List<BusinessException> businessExceptions = transactionService
                .authorizeTransaction(transactionDto, accountId, accountValidations, getAccountStatus);
        assertFalse(businessExceptions.isEmpty());
        assertEquals(businessExceptions, accountErrors);
        verify(transactionRepository, times(0)).save(any());
    }

    /**
     * <b>Given<b/> MINUTES_INTERVAL Equal to 2 and MAX_TRANSACTIONS_INTERVAL equal to 3
     * <b>When<b/> There exists amount error in Transaction
     * <b>Then<b/> should not authorize
     */
    @Test
    public void givenMinIntervalTwoAndMaxTransactionsThree_whenTransactionAmountError_shouldNotAuthorizeTransaction() {
        OffsetDateTime endDate = OffsetDateTime.now();
        OffsetDateTime startDate = endDate.minusMinutes(2);

        List<BusinessException> accountErrors = new ArrayList<>();

        when(transactionRepository.findAllByDateLessThanEqualAndDateGreaterThanEqual(endDate, startDate))
                .thenReturn(new ArrayList<>());

        TransactionDto transactionDto = TransactionDto.builder().amount(300).time(endDate).merchant("Burger King").build();
        Supplier<Integer> accountId = () -> 0;
        Supplier<Optional<AccountDto>> getAccountStatus = () ->
                Optional.of(AccountDto.builder().activeCard(true).availableLimit(200).build());
        Supplier<List<BusinessException>> accountValidations = () -> accountErrors;

        List<BusinessException> businessExceptions = transactionService
                .authorizeTransaction(transactionDto, accountId, accountValidations, getAccountStatus);
        assertFalse(businessExceptions.isEmpty());
        assertEquals(businessExceptions.get(0).getMessage(), TransactionError.ERROR_2.getMessage());
        verify(transactionRepository, times(0)).save(any());
    }

    /**
     * <b>Given<b/> MINUTES_INTERVAL Equal to 2 and MAX_TRANSACTIONS_INTERVAL equal to 3
     * <b>When<b/> There exists more or equal than MAX_TRANSACTIONS_INTERVAL amount in MINUTES_INTERVAL
     * <b>Then<b/> should not authorize
     */
    @Test
    public void givenMinIntervalTwoAndMaxTransactionsThree_whenMaxTransactionInIntervalError_shouldNotAuthorizeTransaction() {
        OffsetDateTime endDate = OffsetDateTime.now();
        OffsetDateTime startDate = endDate.minusMinutes(2);

        List<BusinessException> accountErrors = new ArrayList<>();
        List<TransactionEntity> transactionsInInterval = new ArrayList<>();
        transactionsInInterval.add(TransactionEntity.builder().build());
        transactionsInInterval.add(TransactionEntity.builder().build());
        transactionsInInterval.add(TransactionEntity.builder().build());

        when(transactionRepository.findAllByDateLessThanEqualAndDateGreaterThanEqual(endDate, startDate))
                .thenReturn(transactionsInInterval);

        TransactionDto transactionDto = TransactionDto.builder().amount(200).time(endDate).merchant("Burger King").build();
        Supplier<Integer> accountId = () -> 0;
        Supplier<Optional<AccountDto>> getAccountStatus = () ->
                Optional.of(AccountDto.builder().activeCard(true).availableLimit(200).build());
        Supplier<List<BusinessException>> accountValidations = () -> accountErrors;

        List<BusinessException> businessExceptions = transactionService
                .authorizeTransaction(transactionDto, accountId, accountValidations, getAccountStatus);
        assertFalse(businessExceptions.isEmpty());
        assertEquals(businessExceptions.get(0).getMessage(), TransactionError.ERROR_0.getMessage());
        verify(transactionRepository, times(0)).save(any());
    }

    /**
     * <b>Given<b/> MINUTES_INTERVAL Equal to 2 and MAX_TRANSACTIONS_INTERVAL equal to 3
     * <b>When<b/> There exists more than one transaction with the same amount and merchant name
     * <b>Then<b/> should not authorize
     */
    @Test
    public void givenMinIntervalTwoAndMaxTransactionsThree_whenMaxTransactionSameError_shouldNotAuthorizeTransaction() {
        OffsetDateTime endDate = OffsetDateTime.now();
        OffsetDateTime startDate = endDate.minusMinutes(2);
        Integer amount = 100;
        String merchant = "Burger King";

        List<BusinessException> accountErrors = new ArrayList<>();
        List<TransactionEntity> transactionEntities = new ArrayList<>();

        when(transactionRepository.findAllByDateLessThanEqualAndDateGreaterThanEqual(endDate, startDate))
                .thenReturn(transactionEntities);
        transactionEntities.add(TransactionEntity.builder().destinyAccountId(0)
                .amount(amount).merchantName(merchant).date(OffsetDateTime.now()).build());
        when(transactionRepository.findAllByAmountEqualsAndMerchantNameEquals(amount, merchant)).thenReturn(transactionEntities);

        TransactionDto transactionDto = TransactionDto.builder().amount(amount).time(endDate).merchant(merchant).build();
        Supplier<Integer> accountId = () -> 0;
        Supplier<Optional<AccountDto>> getAccountStatus = () ->
                Optional.of(AccountDto.builder().activeCard(true).availableLimit(200).build());
        Supplier<List<BusinessException>> accountValidations = () -> accountErrors;

        List<BusinessException> businessExceptions = transactionService
                .authorizeTransaction(transactionDto, accountId, accountValidations, getAccountStatus);
        assertFalse(businessExceptions.isEmpty());
        assertEquals(businessExceptions.get(0).getMessage(), TransactionError.ERROR_1.getMessage());
        verify(transactionRepository, times(0)).save(any());
    }


}
