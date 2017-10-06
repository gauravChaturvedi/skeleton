package controllers;

import api.ReceiptSuggestionResponse;
import java.util.List;
import com.google.cloud.vision.v1.*;
import com.google.protobuf.ByteString;
import java.math.BigDecimal;
import java.util.Base64;
import java.util.Collections;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import org.hibernate.validator.constraints.NotEmpty;

import static java.lang.System.out;

@Path("/images")
@Consumes(MediaType.TEXT_PLAIN)
@Produces(MediaType.APPLICATION_JSON)
public class ReceiptImageController {
    private final AnnotateImageRequest.Builder requestBuilder;

    public ReceiptImageController() {
        // DOCUMENT_TEXT_DETECTION is not the best or only OCR method available
        Feature ocrFeature = Feature.newBuilder().setType(Feature.Type.TEXT_DETECTION).build();
        this.requestBuilder = AnnotateImageRequest.newBuilder().addFeatures(ocrFeature);

    }

    /**
     * This borrows heavily from the Google Vision API Docs.  See:
     * https://cloud.google.com/vision/docs/detecting-fulltext
     *
     * YOU SHOULD MODIFY THIS METHOD TO RETURN A ReceiptSuggestionResponse:
     *
     * public class ReceiptSuggestionResponse {
     *     String merchantName;
     *     String amount;
     * }
     */
    @POST
    public ReceiptSuggestionResponse parseReceipt(@NotEmpty String base64EncodedImage) throws Exception {
        Image img = Image.newBuilder().setContent(ByteString.copyFrom(Base64.getDecoder().decode(base64EncodedImage.replaceFirst("data:image/png;base64,", "")))).build();
        AnnotateImageRequest request = this.requestBuilder.setImage(img).build();

        try (ImageAnnotatorClient client = ImageAnnotatorClient.create()) {
            BatchAnnotateImagesResponse responses = client.batchAnnotateImages(Collections.singletonList(request));
            AnnotateImageResponse res = responses.getResponses(0);

            String merchantName = "";
            BigDecimal amount = null;

            // Your Algo Here!!
            // Sort text annotations by bounding polygon.  Top-most non-decimal text is the merchant
            // bottom-most decimal text is the total amount

            List<EntityAnnotation> textAnnotations = res.getTextAnnotationsList();

            for (int i = 0; i < textAnnotations.size(); i++) {
                EntityAnnotation textAnnotation = textAnnotations.get(i);
                if(merchantName.isEmpty()) {
                    if (!textAnnotation.getDescription().split("[\n\r\t]")[0].contains(".")) {
                        merchantName = textAnnotation.getDescription().split("[\n\r\t]")[0];
                    }
                }

                if (textAnnotation.getDescription().matches("[0-9]*\\.[0-9]{2}")) {
                    amount = new BigDecimal(textAnnotation.getDescription());
                }
            }

            String frame = res.getTextAnnotationsList().get(0).getBoundingPoly().toString();
            String[] vertices = frame.split("vertices ");
            String firstvertice = vertices[1];
            String secondvertice = vertices[3];
            String[] firstVerticeStringValue = firstvertice.split("\n");
            String[] secondVerticeStringValue = secondvertice.split("\n");

            // Extract vertex integer values
            Integer v1 = Integer.parseInt(firstVerticeStringValue[1].split(": ")[1]);
            Integer v2 = Integer.parseInt(firstVerticeStringValue[2].split(": ")[1]);
            Integer v3 = Integer.parseInt(secondVerticeStringValue[1].split(": ")[1]);
            Integer v4 = Integer.parseInt(secondVerticeStringValue[2].split(": ")[1]);

            // return new ReceiptSuggestionResponse(merchantName, amount, 1, 2, 3, 4);
            return new ReceiptSuggestionResponse(merchantName, amount, v1, v2, v3, v4);
        }
    }
}
