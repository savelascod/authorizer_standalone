package authorization.project.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * An Exception that is thrown when a business error occurred
 *
 * @version 1.0
 * @since 1.0
 */
@Getter
@AllArgsConstructor
public class BusinessException extends Exception {
    private String message;
}
