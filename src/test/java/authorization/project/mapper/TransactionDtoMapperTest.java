package authorization.project.mapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import authorization.project.dto.domain.TransactionDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Test cases form {@link TransactionDtoMapper}
 *
 * @version 1.0
 * @since 1.0
 */
@ExtendWith(MockitoExtension.class)
public class TransactionDtoMapperTest {

    /**
     * Class under test
     */
    @InjectMocks
    private TransactionDtoMapper transactionDtoMapper;

    /**
     * <b>Given<b/> parsed JSON Object of type TransactionAuthorizationRequestDto
     * <b>When<b/> objectToTransactionAuthorizationRequestDto method is called
     * <b>Then<b/> should return Optional of TransactionDto
     */
    @Test
    public void
    givenAParsedJsonObject_whenObjectToTransactionAuthorizationRequestDtoCalled_shouldReturnOptionalTransactionDto()
            throws JsonProcessingException {
        String rawData = "{\"transaction\": {\"merchant\": \"Burger King\", \"amount\": 22, \"time\":\"2019-02-13T13:00:00.000Z\"}}";
        ObjectMapper mapper = new ObjectMapper();
        Object data = mapper.readValue(rawData, Object.class);
        Optional<TransactionDto> transactionDto = transactionDtoMapper.objectToTransactionAuthorizationRequestDto(data);
        assertTrue(transactionDto.isPresent());
        assertTrue(transactionDto.get().getAmount().equals(22));
        assertTrue(transactionDto.get().getMerchant().equals("Burger King"));
    }
}
