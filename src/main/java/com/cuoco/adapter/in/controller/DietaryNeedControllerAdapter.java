package com.cuoco.adapter.in.controller;

import com.cuoco.adapter.in.controller.model.ParametricResponse;
import com.cuoco.application.port.in.GetAllDietaryNeedsQuery;
import com.cuoco.application.usecase.model.DietaryNeed;
import com.cuoco.shared.GlobalExceptionHandler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/dietary-needs")
public class DietaryNeedControllerAdapter {

    private final GetAllDietaryNeedsQuery getAllDietaryNeedsQuery;

    public DietaryNeedControllerAdapter(GetAllDietaryNeedsQuery getAllDietaryNeedsQuery) {
        this.getAllDietaryNeedsQuery = getAllDietaryNeedsQuery;
    }

    @GetMapping
    @Tag(name = "Parametric Endpoints")
    @Operation(summary = "GET all the dietary needs")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Return all the existent dietary needs",
                    content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = ParametricResponse.class))
                    )
            ),
            @ApiResponse(
                    responseCode = "503",
                    description = "Service unavailable",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = GlobalExceptionHandler.ApiErrorResponse.class)
                    )
            )
    })
    public ResponseEntity<List<ParametricResponse>> getAll() {
        log.info("Executing GET all dietary needs");

        List<DietaryNeed> dietaryNeeds = getAllDietaryNeedsQuery.execute();
        List<ParametricResponse> response = dietaryNeeds.stream().map(this::buildParametricResponse).toList();

        log.info("All dietary needs are retrieved successfully");
        return ResponseEntity.ok(response);
    }

    private ParametricResponse buildParametricResponse(DietaryNeed dietaryNeed) {
        return ParametricResponse.builder()
                .id(dietaryNeed.getId())
                .description(dietaryNeed.getDescription())
                .build();
    }
}
