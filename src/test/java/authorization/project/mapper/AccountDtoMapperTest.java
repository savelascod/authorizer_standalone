package authorization.project.mapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import authorization.project.dto.domain.AccountDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Test cases for {@link AccountDtoMapper}
 *
 * @version 1.0
 * @since 1.0
 */
@ExtendWith(MockitoExtension.class)
public class AccountDtoMapperTest {

    /**
     * Class Unser test
     */
    @InjectMocks
    private AccountDtoMapper accountDtoMapper;

    /**
     * <b>Given<b/> parsed JSON Object of type AccountCreationRequestDto
     * <b>When<b/> objectToAccountCreationRequestDto method is called
     * <b>Then<b/> should return Optional of AccountDto
     */
    @Test
    public void
    givenAParsedJsonObject_whenObjectToAccountCreationRequestDtoCalled_shouldReturnOptionalAccountDto()
            throws JsonProcessingException {
        String rawData = "{\"account\": {\"active-card\": true, \"available-limit\": 100}}";
        ObjectMapper mapper = new ObjectMapper();
        Object data = mapper.readValue(rawData, Object.class);
        Optional<AccountDto> accountDto = accountDtoMapper.objectToAccountCreationRequestDto(data);
        assertTrue(accountDto.isPresent());
        assertTrue(accountDto.get().getActiveCard().equals(true));
        assertTrue(accountDto.get().getAvailableLimit().equals(100));
    }
}
