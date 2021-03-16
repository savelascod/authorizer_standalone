package authorization.project.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.NoArgsConstructor;
import authorization.project.dto.domain.AccountDto;

/**
 * DTO Class that represents a request to create an ACCOUNT
 *
 * @version 1.0
 * @since 1.0
 */
@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class AccountCreationRequestDto {
    /**
     * instance of {@link AccountDto}
     */
    private AccountDto account;
}
