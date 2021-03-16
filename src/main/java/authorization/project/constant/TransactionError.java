package authorization.project.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Enum that defines business errors in transaction operation
 *
 * @version 1.0
 * @since 1.0
 */
@Getter
@AllArgsConstructor
public enum TransactionError {
    ERROR_0("high-frequency-small-interval"),
    ERROR_1("doubled-transaction"),
    ERROR_2("insufficient-limit");
    private String message;
}
