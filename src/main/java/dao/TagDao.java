package dao;

import generated.tables.records.ReceiptsRecord;
import generated.tables.records.TagsRecord;
import org.jooq.Configuration;
import org.jooq.DSLContext;
import org.jooq.impl.DSL;

import java.util.List;

import static generated.Tables.TAGS;
import static generated.Tables.RECEIPTS;

public class TagDao {
    DSLContext dsl;

    public TagDao(Configuration jooqConfig) {
        this.dsl = DSL.using(jooqConfig);
    }

    public void insert(String tagName, int lookupId) {
            dsl.insertInto(TAGS, TAGS.TAG, TAGS.RECEIPT_ID)
                .values(tagName,lookupId)
                .execute();
    }

    public void delete(String tagName, int lookupId) {
                dsl.delete(TAGS)
                .where(TAGS.RECEIPT_ID.eq(lookupId)).and(TAGS.TAG.eq(tagName))
                .execute();
    }

    public List<TagsRecord> findReceiptWithTag(String tagName, int lookupId) {
        return dsl.selectFrom(TAGS).where(TAGS.RECEIPT_ID.eq(lookupId)).and(TAGS.TAG.eq(tagName)).fetch();
    }

    public List<ReceiptsRecord> getAllReceiptsWithTag(String tagName) {
        return dsl.select()
                .from(RECEIPTS)
                .join(TAGS).on(RECEIPTS.ID.equal(TAGS.RECEIPT_ID))
                .where(TAGS.TAG.eq(tagName))
                .fetchInto(ReceiptsRecord.class);
    }

    public List<TagsRecord> getAllTagsForReceiptId(int receiptId) {
        return dsl.select()
                .from(TAGS)
                .where(TAGS.RECEIPT_ID.eq(receiptId))
                .fetchInto(TagsRecord.class);
    }

    public List<TagsRecord> getAllTags() {
        return dsl.select()
                .from(TAGS)
                .fetchInto(TagsRecord.class);
    }
}

