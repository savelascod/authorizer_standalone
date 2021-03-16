package authorization.project.persistence.repository;

import authorization.project.persistence.entity.AccountEntity;

import java.util.Optional;

/**
 * Repository contract to manage persistence on {@link AccountEntity} Entities
 *
 * @version 1.0
 * @since 1.0
 */
public interface IAccountRepository {
    Optional<AccountEntity> findFirstByIdIsNotNull();

    long count();

    <S extends AccountEntity> S save(S entity);
}
