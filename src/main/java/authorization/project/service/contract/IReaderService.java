package authorization.project.service.contract;

import java.util.List;
import java.util.Optional;

/**
 * Service contract that defines methods to read Streams of data
 *
 * @version 1.0
 * @since 1.0
 */
public interface IReaderService {
    /**
     * Reads input from stdin
     *
     * @return dataStream -> List of objects retrieved from stdin
     */
    Optional<List<? extends Object>> readFromStdin();
}
