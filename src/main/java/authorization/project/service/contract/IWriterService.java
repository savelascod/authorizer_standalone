package authorization.project.service.contract;

/**
 * Contract that defines the operations to write messages
 *
 * @version 1.0
 * @since 1.0
 */
public interface IWriterService {
    /**
     * Writes the welcome message and instructions
     */
    void writeWelcomeMessage();

    /**
     * Writes the given message
     *
     * @param response -> String with message to print
     */
    void writeResponse(String response);

    /**
     * Writes end message
     */
    void writeExitMessage();
}
