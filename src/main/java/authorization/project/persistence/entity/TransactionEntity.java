package authorization.project.persistence.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

/**
 * Entity class that represents a TRANSACTION
 *
 * @version 1.0
 * @since 1.0
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TransactionEntity {

    /**
     * Internal ID representation of an ACCOUNT object
     */
    private Integer id;

    /**
     * Integer that represents the DESTINY ACCOUNT'S ID
     */
    private Integer destinyAccountId;

    /**
     * String that represents the MERCHANT'S NAME who perform the transaction
     */
    private String merchantName;

    /**
     * OffsetDateTime that represents the TRANSACTION'S CREATION DATE
     */
    private OffsetDateTime date;

    /**
     * Integer that represents the TRANSACTION'S AMOUNT
     */
    private Integer amount;
}
