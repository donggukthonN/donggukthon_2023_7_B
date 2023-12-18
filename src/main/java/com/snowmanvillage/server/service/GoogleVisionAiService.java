package com.snowmanvillage.server.service;

import com.google.cloud.vision.v1.*;
import com.google.protobuf.ByteString;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;

@Service
public class GoogleVisionAiService {

    public Boolean postPhotoAnalyze(MultipartFile file) throws IOException {
        ByteString imgBytes = ByteString.readFrom(file.getInputStream());

        Image img = Image.newBuilder().setContent(imgBytes).build();
        Feature feat = Feature.newBuilder().setType(Feature.Type.LABEL_DETECTION).build();
        AnnotateImageRequest request = AnnotateImageRequest.newBuilder()
                .addFeatures(feat)
                .setImage(img)
                .build();

        try (ImageAnnotatorClient vision = ImageAnnotatorClient.create()) {
            AnnotateImageResponse response = vision.batchAnnotateImages(Arrays.asList(request))
                    .getResponses(0);
            if (response.hasError()) {
                System.err.println("Error: " + response.getError().getMessage());
                return false;
            }

            for (EntityAnnotation annotation : response.getLabelAnnotationsList()) {
                if (annotation.getDescription().toLowerCase().contains("snowman") &&
                        annotation.getScore() >= 0.7) {
                    return true;
                }
            }
        }
        return false;
    }
}
