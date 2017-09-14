package api;

import com.fasterxml.jackson.annotation.JsonProperty;
import generated.tables.records.TagsRecord;

public class TagResponse {
    @JsonProperty
    Integer receipt_id;

    @JsonProperty
    String tag;


    public TagResponse(TagsRecord dbRecord) {
        this.receipt_id = dbRecord.getReceiptId();
        this.tag = dbRecord.getTag();
    }
}