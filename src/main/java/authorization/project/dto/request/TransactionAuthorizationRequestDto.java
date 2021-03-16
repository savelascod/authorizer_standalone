package authorization.project.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.NoArgsConstructor;
import authorization.project.dto.domain.TransactionDto;

/**
 * DTO Class that represents a request to create a TRANSACTION AUTHORIZATION
 *
 * @version 2.1
 * @since 2.1
 */
@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class TransactionAuthorizationRequestDto {
    /**
     * instance of {@link TransactionDto}
     */
    private TransactionDto transaction;
}
