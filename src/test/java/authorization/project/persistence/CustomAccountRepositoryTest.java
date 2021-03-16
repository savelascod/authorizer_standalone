package authorization.project.persistence;

import authorization.project.persistence.entity.AccountEntity;
import authorization.project.persistence.impl.CustomAccountRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

/**
 * Test cases for {@link CustomAccountRepository}
 *
 * @version 1.0
 * @since 1.0
 */
@ExtendWith(MockitoExtension.class)
public class CustomAccountRepositoryTest {

    /**
     * Class Under test
     */
    private CustomAccountRepository customAccountRepository;

    @BeforeEach
    public void setUp() {
        customAccountRepository = new CustomAccountRepository();
    }

    /**
     * <b>When<b/> findFirstByIdIsNotNullCalled method is called
     * <b>Then<b/> should return Optional of AccountEntity
     */
    @Test
    public void
    whenFindFirstByIdIsNotNullCalled_thenShouldReturnOptionalOfAccountEntity() {
        Optional<AccountEntity> accountEntity = customAccountRepository.findFirstByIdIsNotNull();
        assertFalse(accountEntity.isPresent());
    }

    /**
     * <b>When<b/> findFirstByIdIsNotNullCalled method is called
     * <b>Then<b/> should return Optional of AccountEntity with value if exists
     */
    @Test
    public void
    whenFindFirstByIdIsNotNullCalled_thenShouldReturnOptionalOfAccountEntityWithObjectIfExists() {
        customAccountRepository.save(AccountEntity.builder().id(1).build());
        Optional<AccountEntity> accountEntity = customAccountRepository.findFirstByIdIsNotNull();
        assertEquals(accountEntity.get().getId(), 1);
    }

    /**
     * <b>When<b/> save method is called
     * <b>Then<b/> should insert and return AccountEntity
     */
    @Test
    public void
    whenSaveCalled_thenShouldInsertAndReturnAccountEntity() {
        AccountEntity accountEntity = customAccountRepository.save(AccountEntity.builder().id(1).build());
        Optional<AccountEntity> accountEntityOptional = customAccountRepository.findFirstByIdIsNotNull();
        assertEquals(accountEntityOptional.get().getId(), 1);
        assertEquals(accountEntity.getId(), accountEntityOptional.get().getId());
    }

    /**
     * <b>When<b/> count method is called
     * <b>Then<b/> should return total of all elements
     */
    @Test
    public void
    whenCountCalled_thenShouldReturnTotalElements() {
        customAccountRepository.save(AccountEntity.builder().id(1).build());
        Long total = customAccountRepository.count();
        assertEquals(total, 1);
    }
}
