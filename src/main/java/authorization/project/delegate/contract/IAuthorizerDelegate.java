package authorization.project.delegate.contract;

import authorization.project.dto.response.ResponseDto;

import java.util.List;
import java.util.Optional;

/**
 * Contract that specifies operations to be performed in order to authorize transactions
 *
 * @version 1.0
 * @since 1.0
 */
public interface IAuthorizerDelegate {

    /**
     * Method that receives a stream of data, and authorize requests.
     *
     * @param requestList a list of Objects with stream of data.
     * @return a List with the responses for each request.
     */
    List<ResponseDto> authorizeRequests(Optional<List<? extends Object>> requestList);

    /**
     * Method that retrieve the stream of transactions and performs operations
     */
    void performActions();

    /**
     * Method that convert json parsed object to proper DTO and call delegates to process requests.
     *
     * @param jsonParsedObject -> Parsed Json Object from stdin
     * @return a {@link ResponseDto} instance with the result of the operation
     */
    ResponseDto processRequest(Object jsonParsedObject);
}
