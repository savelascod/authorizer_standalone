package authorization.project.mapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import authorization.project.dto.response.ResponseDto;

import java.util.List;

/**
 * Mapper class for {@link ResponseDto}
 *
 * @version 1.0
 * @since 1.0
 */
public class ResponseDtoMapper extends DefaultMapper {
    public String responseListDtoToString(List<ResponseDto> responseDto) throws JsonProcessingException {
        return getObjectMapper().writeValueAsString(responseDto);
    }
}
