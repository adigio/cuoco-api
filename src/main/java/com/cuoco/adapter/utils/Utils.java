package com.cuoco.adapter.utils;

import com.cuoco.adapter.exception.UnprocessableException;
import com.cuoco.adapter.out.rest.gemini.model.wrapper.CandidateGeminiResponseModel;
import com.cuoco.adapter.out.rest.gemini.model.wrapper.ContentGeminiRequestModel;
import com.cuoco.adapter.out.rest.gemini.model.wrapper.GeminiResponseModel;
import com.cuoco.adapter.out.rest.gemini.model.wrapper.PartGeminiRequestModel;
import com.cuoco.shared.model.ErrorDescription;
import com.cuoco.shared.utils.Constants;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class Utils {

    private final static String INIT_JSON_STRING = "```json";
    private final static String FINAL_JSON_STRING = "```";

    @NotNull
    public static String sanitizeJsonResponse(GeminiResponseModel response) {
        String recipeResponseText = validate(response);

        if(recipeResponseText == null || recipeResponseText.isEmpty()) {
            throw new UnprocessableException(ErrorDescription.NOT_AVAILABLE.getValue());
        }

        return recipeResponseText
                .replaceAll(INIT_JSON_STRING, Constants.EMPTY.getValue())
                .replaceAll(FINAL_JSON_STRING, Constants.EMPTY.getValue())
                .trim();
    }

    @Nullable
    private static String validate(GeminiResponseModel response) {
        String recipeResponseText = null;

        if(response.getCandidates() != null && !response.getCandidates().isEmpty()) {
            CandidateGeminiResponseModel candidate = response.getCandidates().get(0);

            if(candidate.getContent() != null) {
                ContentGeminiRequestModel content = candidate.getContent();

                if(content.getParts() != null && !content.getParts().isEmpty()) {
                    PartGeminiRequestModel parts = content.getParts().get(0);

                    recipeResponseText = parts.getText();
                }
            }
        }

        return recipeResponseText;
    }

}
