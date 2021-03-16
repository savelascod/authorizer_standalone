package authorization.project.dto.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO Class that represents and Account
 *
 * @version 1.0
 * @since 1.0
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class AccountDto {
    /**
     * Boolean that represents if an ACCOUNT has an ACTIVE CARD
     */
    private Boolean activeCard;

    /**
     * Integer that represents the AVAILABLE LIMIT of an ACCOUNT
     */
    private Integer availableLimit;
}
