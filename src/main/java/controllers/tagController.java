package controllers;

import api.ReceiptResponse;
import api.TagResponse;
import dao.ReceiptDao;
import dao.TagDao;
import generated.tables.records.ReceiptsRecord;
import generated.tables.records.TagsRecord;

import javax.ws.rs.*;
import java.util.List;
import javax.ws.rs.core.MediaType;

import static java.util.stream.Collectors.toList;


@Path("/tags")
public class tagController {
    final ReceiptDao receipts;
    final TagDao tags;

    public tagController(ReceiptDao receipts, TagDao tags) {
        this.receipts = receipts;
        this.tags = tags;
    }

    @PUT
    @Path("/{tag}")
    public void toggleTag(@PathParam("tag") String tagName, int lookupId) {
        // If the tag does not exist for a record, add it otherwise, remove it
        if (tags.findReceiptWithTag(tagName, lookupId).isEmpty()) {
            tags.insert(tagName, lookupId);
        } else {
            tags.delete(tagName, lookupId);
        }
    }

    @GET
    @Path("/{tag}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<ReceiptResponse> getAllReceiptsWithTag(@PathParam("tag") String tagName) {
        List<ReceiptsRecord> receiptRecords = tags.getAllReceiptsWithTag(tagName);
        return receiptRecords.stream().map(ReceiptResponse::new).collect(toList());
    }

    @GET
    @Path("/fetch/{receiptId}")
    @Produces(MediaType.APPLICATION_JSON)
    public List getTagsForReceipt(@PathParam("receiptId") int receiptId) {
        List<TagsRecord> tagRecords = tags.getAllTagsForReceiptId(receiptId);
        return tagRecords.stream().map(TagResponse::new).collect(toList());
    }

    @GET
    @Path("/fetch-all-tags")
    @Produces(MediaType.APPLICATION_JSON)
    public List getAllTags() {
        List<TagsRecord> tagRecords = tags.getAllTags();
        return tagRecords.stream().map(TagResponse::new).collect(toList());
    }
}
