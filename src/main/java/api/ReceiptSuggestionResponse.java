package api;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;

/**
 * Represents the result of an OCR parse
 */
public class ReceiptSuggestionResponse {
    @JsonProperty
    public final String merchantName;

    @JsonProperty
    public final BigDecimal amount;

    @JsonProperty
    public final Integer v1;

    @JsonProperty
    public final Integer v2;

    @JsonProperty
    public final Integer v3;

    @JsonProperty
    public final Integer v4;


    public ReceiptSuggestionResponse(String merchantName, BigDecimal amount, Integer v1, Integer v2, Integer v3, Integer v4) {
        this.merchantName = merchantName;
        this.amount = amount;
        this.v1 = v1;
        this.v2= v2;
        this.v3 = v3;
        this.v4 = v4;
    }
}
