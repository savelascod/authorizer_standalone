package authorization.project.mapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import authorization.project.dto.domain.AccountDto;
import authorization.project.dto.response.ResponseDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Test cases for {@link ResponseDtoMapper}
 *
 * @version 1.0
 * @since 1.0
 */
@ExtendWith(MockitoExtension.class)
public class ResponseDtoMapperTest {

    /**
     * Class Under test
     */
    @InjectMocks
    private ResponseDtoMapper responseDtoMapper;

    /**
     * <b>Given<b/> parsed a List of ResponseDto Objects
     * <b>When<b/> responseListDtoToString method is called
     * <b>Then<b/> should return Json String representation of Object
     */
    @Test
    public void givenAResponseDtoList_whenResponseListDtoToStringCalled_shouldReturnStringWIthJson()
            throws JsonProcessingException {
        List<ResponseDto> responses = new ArrayList<>();
        responses.add(ResponseDto.builder()
                .account(AccountDto.builder().availableLimit(200).activeCard(true).build())
                .violations(new ArrayList<>())
                .build());
        String jsonString = responseDtoMapper.responseListDtoToString(responses);
        assertNotNull(jsonString);
        assertEquals(jsonString, "[{\"account\":{\"active-card\":true,\"available-limit\":200},\"violations\":[]}]");
    }
}
