package authorization.project.dto.response;

import authorization.project.dto.domain.AccountDto;
import authorization.project.dto.request.AccountCreationRequestDto;
import authorization.project.dto.request.TransactionAuthorizationRequestDto;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

/**
 * DTO Class that represents responses to
 * {@link AccountCreationRequestDto}
 * and {@link TransactionAuthorizationRequestDto}
 *
 * @version 1.0
 * @since 1.0
 */
@Builder
@Getter
@AllArgsConstructor
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class ResponseDto {

    /**
     * instance of {@link AccountDto}
     */
    private AccountDto account;

    /**
     * List of violations concerning the given request
     */
    private List<String> violations;
}
