package authorization.project.service;

import authorization.project.service.impl.StandardWriterService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Unit and integration tests for {@link StandardWriterServiceTest}
 *
 * @version 1.0
 * @since 1.0
 */
@ExtendWith(MockitoExtension.class)
public class StandardWriterServiceTest {

    private final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();

    /**
     * Class Under Test
     */
    @InjectMocks
    private StandardWriterService standardWriterService;

    @BeforeEach
    public void setUp() {
        System.setOut(new PrintStream(outputStreamCaptor));
    }

    /**
     * <b>When<b/> writeWelcomeMessage method is called
     * <b>Then<b/> should print in console
     */
    @Test
    public void whenWriteWelcomeMessage_shouldPrintInConsole() {
        standardWriterService.writeWelcomeMessage();
        assertTrue(outputStreamCaptor.toString().length() > 0);
    }

    /**
     * <b>Given<b/> a message
     * <b>When<b/> writeResponse method is called
     * <b>Then<b/> should print in console
     */
    @Test
    public void givenAMessage_whenWriteResponse_shouldPrintInConsole() {
        standardWriterService.writeResponse("hello");
        assertTrue(outputStreamCaptor.toString().equals("hello\n"));
    }

    /**
     * <b>When<b/> writeResponse method is called
     * <b>Then<b/> should print in console
     */
    @Test
    public void writeExitMessage_shouldPrintInConsole() {
        standardWriterService.writeExitMessage();
        assertTrue(outputStreamCaptor.toString().length() > 0);
    }

}
