package com.cuoco.adapter.in.controller;

import com.cuoco.adapter.in.controller.model.ParametricResponse;
import com.cuoco.application.port.in.GetAllPreparationTimesQuery;
import com.cuoco.application.usecase.model.PreparationTime;
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
@RequestMapping("/preparation-times")
public class PreparationTimeControllerAdapter {

    private GetAllPreparationTimesQuery getAllPreparationTimesQuery;

    public PreparationTimeControllerAdapter(GetAllPreparationTimesQuery getAllPreparationTimesQuery) {
        this.getAllPreparationTimesQuery = getAllPreparationTimesQuery;
    }

    @GetMapping
    @Tag(name = "Parametric Endpoints")
    @Operation(summary = "GET all the preparation times")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Return all the existent preparation times",
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
    public ResponseEntity<List<ParametricResponse>> getAllPreparationTimes() {
        log.info("Executing GET all preparation times");
        List<PreparationTime> preparationTimes = getAllPreparationTimesQuery.execute();
        List<ParametricResponse> response = preparationTimes.stream().map(ParametricResponse::fromDomain).toList();

        log.info("All preparation times are retrieved successfully");
        return ResponseEntity.ok(response);
    }
}
