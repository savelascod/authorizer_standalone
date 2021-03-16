package authorization.project.mapper;

import authorization.project.dto.request.AccountCreationRequestDto;
import authorization.project.dto.domain.AccountDto;

import java.util.Optional;

/**
 * Mapper class for {@link AccountDto}
 *
 * @version 1.0
 * @since 1.0
 */
public class AccountDtoMapper extends DefaultMapper {
    public Optional<AccountDto> objectToAccountCreationRequestDto(Object object) {
        return Optional.ofNullable(getObjectMapper().convertValue(object, AccountCreationRequestDto.class).getAccount());
    }
}
