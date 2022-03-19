package com.interview.service;

import com.google.cloud.translate.v3.LocationName;
import com.google.cloud.translate.v3.TranslateTextRequest;
import com.google.cloud.translate.v3.TranslateTextResponse;
import com.google.cloud.translate.v3.TranslationServiceClient;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class TranslateService {
    TranslationServiceClient client;

    public String translate(String textToTranslate){
        try {
            client = TranslationServiceClient.create();
        } catch (IOException e) {
            e.printStackTrace();
        }
        String location = "us-central1";
        LocationName parent = LocationName.of("interviewbot-344613", location);
        String modelPath =
                String.format("projects/%s/locations/%s/models/%s", "interviewbot-344613", location, "don't know what is model");

        TranslateTextRequest request =
                TranslateTextRequest.newBuilder()
                        .setParent(parent.toString())
                        .setMimeType("text/plain")
                        .setSourceLanguageCode("ru")
                        .setTargetLanguageCode("en")
                        .addContents(textToTranslate)
                        .setModel(modelPath)
                        .build();

        TranslateTextResponse response = client.translateText(request);

        return response.getTranslationsList().get(1).getTranslatedText();
    }
}
