package authorization.project.dto.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

/**
 * DTO Class that represents a transaction
 *
 * @version 1.0
 * @since 1.0
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class TransactionDto {
    /**
     * String that represents the MERCHANT'S NAME who perform the transaction
     */
    private String merchant;

    /**
     * OffsetDateTime that represents the TRANSACTION'S CREATION DATE
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
    private OffsetDateTime time;

    /**
     * Integer that represents the TRANSACTION'S AMOUNT
     */
    private Integer amount;
}
