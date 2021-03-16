package authorization.project.persistence.impl;

import authorization.project.persistence.entity.TransactionEntity;
import authorization.project.persistence.repository.ITransactionRepository;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Custom implementation of {@link ITransactionRepository}, with explicit Data Structures
 *
 * @version 1.0
 * @since 1.0
 */
public class CustomTransactionRepository implements ITransactionRepository {

    private List<TransactionEntity> transactions = new ArrayList<>();

    private int idCount;

    @Override
    public List<TransactionEntity> findAllByDateLessThanEqualAndDateGreaterThanEqual
            (OffsetDateTime endDateTime, OffsetDateTime startDateTime) {
        return transactions.stream().filter(transactionEntity ->
                !(transactionEntity.getDate().isAfter(endDateTime) ||
                        transactionEntity.getDate().isBefore(startDateTime)))
                .collect(Collectors.toList());
    }

    @Override
    public List<TransactionEntity> findAllByAmountEqualsAndMerchantNameEquals
            (Integer amount, String merchantName) {
        return transactions.stream().filter(transactionEntity ->
                transactionEntity.getAmount().equals(amount) &&
                        transactionEntity.getMerchantName().equals(merchantName))
                .collect(Collectors.toList());
    }

    @Override
    public <S extends TransactionEntity> S save(S entity) {
        entity.setId(++idCount);
        transactions.add(entity);
        return entity;
    }
}
