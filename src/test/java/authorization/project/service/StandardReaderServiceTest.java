package authorization.project.service;

import authorization.project.service.impl.StandardReaderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

/**
 * Unit and integration tests for {@link StandardReaderService}
 *
 * @version 1.0
 * @since 1.0
 */
@ExtendWith(MockitoExtension.class)
public class StandardReaderServiceTest {

    /**
     * Dependency loading
     */
    @Mock
    private BufferedReader bufferedReader;

    /**
     * Class Under Test
     */
    @InjectMocks
    private StandardReaderService standardReaderService;

    @BeforeEach
    public void setUp() {
        standardReaderService = new StandardReaderService(bufferedReader);
    }

    @Test
    public void contextLoads() {
        assertNotNull(standardReaderService);
        assertNotNull(bufferedReader);
    }

    @Test
    public void noArgsConstructor() {
        StandardReaderService standardReaderService = new StandardReaderService();
        assertNotNull(standardReaderService);
    }

    /**
     * <b>When<b/> ReadFromStdin method is called
     * <b>Then<b/> should return a List of Objects If a Json Array was read
     */
    @Test
    public void whenReadFromStdinIsCalled_shouldReturnAListOfObjectsIfAJsonArrayWasRead() throws IOException {
        String jsonArray =
                "[{\"account\": {\"active-card\": true, \"available-limit\": 100}}," +
                        "{\"transaction\": {\"merchant\": \"Burger 'King\", \"amount\": 20, \"time\":\"2019-02-13T10:00:00.000Z\"}}]";
        when(bufferedReader.readLine()).thenReturn(jsonArray);
        Optional<List<?>> retrievedObjects = standardReaderService.readFromStdin();
        assertTrue(retrievedObjects.isPresent());
        assertTrue(retrievedObjects.get().size() == 2);
        assertTrue(((LinkedHashMap) retrievedObjects.get().get(0)).containsKey("account"));
        assertTrue(((LinkedHashMap) retrievedObjects.get().get(1)).containsKey("transaction"));
    }

    /**
     * <b>When<b/> ReadFromStdin method is called,
     * <b>Then<b/> should return a List of one Object If a Json Object was read
     */
    @Test
    public void whenReadFromStdinIsCalled_shouldReturnAListOfOneObjectIfAJsonObjectWasRead() throws IOException {
        String jsonObject = "{\"account\": {\"active-card\": true, \"available-limit\": 100}}";
        when(bufferedReader.readLine()).thenReturn(jsonObject);
        Optional<List<?>> retrievedObjects = standardReaderService.readFromStdin();
        assertTrue(retrievedObjects.isPresent());
        assertTrue(retrievedObjects.get().size() == 1);
        assertTrue(((LinkedHashMap) retrievedObjects.get().get(0)).containsKey("account"));
    }

    /**
     * <b>When<b/> ReadFromStdin method is called
     * <b>Then<b/> should return Optional of Empty If no Json Array or Json Object were provided
     */
    @Test
    public void whenReadFromStdinIsCalled_shouldReturnOptionalEmptyIfNoArrayOrObject() throws IOException {
        String noJsonValue = "EXIT";
        when(bufferedReader.readLine()).thenReturn(noJsonValue);
        Optional<List<?>> retrievedObjects = standardReaderService.readFromStdin();
        assertTrue(!retrievedObjects.isPresent());
    }

    /**
     * <b>When<b/> ReadFromStdin method is called and an Exception is Thrown
     * <b>Then<b/> should return Optional of Empty If no Json Array or Json Object were provided
     */
    @Test
    public void
    whenReadFromStdinIsCalledAndExceptionThrown_shouldReturnOptionalEmptyIfNoArrayOrObject() throws IOException {
        when(bufferedReader.readLine()).thenThrow(new RuntimeException());
        Optional<List<?>> retrievedObjects = standardReaderService.readFromStdin();
        assertTrue(!retrievedObjects.isPresent());
    }
}
