package authorization.project.service.impl;

import authorization.project.service.contract.IReaderService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static io.vavr.API.Try;

/**
 * @version 1.0
 * @since 1.0
 */
@AllArgsConstructor
public class StandardReaderService implements IReaderService {

    private final BufferedReader stdinReader;

    /**
     * Constructor with autowired dependencies
     */
    public StandardReaderService() {
        this.stdinReader = new BufferedReader(new InputStreamReader(System.in));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<List<? extends Object>> readFromStdin() {
        return Try(stdinReader::readLine).toEither()
                .fold(exception -> Optional.empty(), rawData -> Try(() -> readFromArrayInLine(rawData)).toEither()
                        .fold(readArrayException -> Try(() -> readFromObjectInLine(rawData)).toEither()
                                .fold(readObjectException -> Optional.empty(), objects -> objects), objects -> objects));
    }

    /**
     * Method that read the rawData and convert json Objects
     *
     * @param rawData with string value read from stdin
     * @return a List of parsed Objects
     * @throws IOException
     */
    private Optional<List<? extends Object>> readFromArrayInLine(String rawData) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        List<Object> data = Arrays.asList(mapper.readValue(rawData, Object[].class));
        return Optional.of(data);
    }

    /**
     * Method that read the rawData and convert a json Object
     *
     * @param rawData with string value read from stdin
     * @return a parsed Object
     * @throws IOException
     */
    private Optional<List<? extends Object>> readFromObjectInLine(String rawData) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        List<Object> data = Collections.singletonList(mapper.readValue(rawData, Object.class));
        return Optional.of(data);
    }
}
