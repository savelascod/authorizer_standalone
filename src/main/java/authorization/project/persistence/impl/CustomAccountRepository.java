package authorization.project.persistence.impl;

import authorization.project.persistence.entity.AccountEntity;
import authorization.project.persistence.repository.IAccountRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Custom implementation of {@link IAccountRepository}, with explicit Data Structures
 *
 * @version 1.0
 * @since 1.0
 */
public class CustomAccountRepository implements IAccountRepository {

    private List<AccountEntity> accounts = new ArrayList<>();

    private int idCount;

    @Override
    public Optional<AccountEntity> findFirstByIdIsNotNull() {
        Optional<AccountEntity> accountEntity = accounts.isEmpty() ?
                Optional.empty() : Optional.of(accounts.get(0));
        return accountEntity;
    }

    @Override
    public long count() {
        return accounts.size();
    }

    @Override
    public <S extends AccountEntity> S save(S entity) {
        entity.setId(++idCount);
        accounts.add(entity);
        return entity;
    }

}
