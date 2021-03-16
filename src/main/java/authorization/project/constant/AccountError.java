package authorization.project.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Enum that defines business errors in Account creation
 *
 * @version 1.0
 * @since 1.0
 */
@Getter
@AllArgsConstructor
public enum AccountError {
    ERROR_0("account-already-initialized"),
    ERROR_1("card-not-active"),
    ERROR_2("account-not-initialized");
    private String message;
}
