package authorization.project.persistence.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Entity class that represents an ACCOUNT
 *
 * @version 1.0
 * @since 1.0
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AccountEntity {

    /**
     * Internal ID representation of an ACCOUNT object
     */
    private Integer id;

    /**
     * Boolean that represents if an ACCOUNT has an ACTIVE CARD
     */
    private Boolean activeCard;

    /**
     * Integer that represents the AVAILABLE LIMIT of an ACCOUNT
     */
    private Integer availableLimit;
}
