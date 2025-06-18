package com.cuoco.factory.gemini;

import com.cuoco.adapter.out.rest.gemini.model.wrapper.CandidateGeminiResponseModel;
import com.cuoco.adapter.out.rest.gemini.model.wrapper.ContentGeminiRequestModel;
import com.cuoco.adapter.out.rest.gemini.model.wrapper.GeminiResponseModel;
import com.cuoco.adapter.out.rest.gemini.model.wrapper.InlineDataGeminiRequestModel;
import com.cuoco.adapter.out.rest.gemini.model.wrapper.PartGeminiRequestModel;

import java.util.List;

public class GeminiResponseModelFactory {

    public static GeminiResponseModel create(String jsonResponse) {
        return GeminiResponseModel.builder()
                .candidates(List.of(CandidateGeminiResponseModel.builder().content(getContent(jsonResponse)).build()))
                .build();
    }

    public static ContentGeminiRequestModel getContent(String jsonResponse) {
        return ContentGeminiRequestModel.builder()
                .parts(List.of(
                        PartGeminiRequestModel.builder()
                                .inlineData(InlineDataGeminiRequestModel.builder().data("DATA").mimeType("MIMETYPE").build())
                                .text(jsonResponse != null ? jsonResponse : "RESPONSE TEXT")
                                .build()
                ))
                .build();
    }

}
