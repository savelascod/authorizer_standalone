package authorization.project.persistence;

import authorization.project.persistence.entity.TransactionEntity;
import authorization.project.persistence.impl.CustomTransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.OffsetDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Test cases for {@link CustomTransactionRepository}
 *
 * @version 1.0
 * @since 1.0
 */
@ExtendWith(MockitoExtension.class)
public class CustomTransactionRepositoryTest {

    private CustomTransactionRepository customTransactionRepository;

    @BeforeEach
    public void setUp() {
        customTransactionRepository = new CustomTransactionRepository();
    }

    /**
     * <b>When<b/> findAllByDateLessThanEqualAndDateGreaterThanEqual method is called
     * <b>Then<b/> should return List with elements
     */
    @Test
    public void whenFindAllByDateLessThanEqualAndDateGreaterThanEqual_thenShouldReturnList() {
        OffsetDateTime dateTime = OffsetDateTime.now();
        customTransactionRepository.save(TransactionEntity.builder().date(dateTime).build());
        customTransactionRepository.save(TransactionEntity.builder().date(dateTime.minusMinutes(1)).build());
        customTransactionRepository.save(TransactionEntity.builder().date(dateTime.minusMinutes(2)).build());
        List<TransactionEntity> transactionEntityList =
                customTransactionRepository.findAllByDateLessThanEqualAndDateGreaterThanEqual(dateTime,
                        dateTime.minusMinutes(2));
        assertTrue(transactionEntityList.size() == 3);
    }

    /**
     * <b>When<b/> findAllByAmountEqualsAndMerchantNameEquals method is called
     * <b>Then<b/> should return List with elements
     */
    @Test
    public void whenFindAllByAmountEqualsAndMerchantNameEquals_thenShouldReturnList() {
        customTransactionRepository.save(TransactionEntity.builder()
                .amount(20).merchantName("Burger King").build());
        List<TransactionEntity> transactionEntityList =
                customTransactionRepository
                        .findAllByAmountEqualsAndMerchantNameEquals(20, "Burger King");
        assertTrue(transactionEntityList.size() == 1);
    }

    /**
     * <b>When<b/> save method is called
     * <b>Then<b/> should return a TransactionEntity
     */
    @Test
    public void whenSaveCalled_thenShouldReturnTransactionEntity() {
        TransactionEntity transactionEntity = customTransactionRepository.save(TransactionEntity.builder()
                .amount(20).merchantName("Burger King").build());
        assertTrue(transactionEntity.getAmount().equals(20));
        assertTrue(transactionEntity.getMerchantName().equals("Burger King"));
    }

}
