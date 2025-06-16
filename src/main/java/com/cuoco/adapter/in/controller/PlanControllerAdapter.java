package com.cuoco.adapter.in.controller;

import com.cuoco.adapter.in.controller.model.ParametricResponse;
import com.cuoco.application.port.in.GetAllPlansQuery;
import com.cuoco.application.usecase.model.Plan;
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
@RequestMapping("/plans")
public class PlanControllerAdapter {

    private final GetAllPlansQuery getAllPlansQuery;

    public PlanControllerAdapter(GetAllPlansQuery getAllPlansQuery) {
        this.getAllPlansQuery = getAllPlansQuery;
    }

    @GetMapping
    @Tag(name = "Parametric Endpoints")
    @Operation(summary = "GET all the plans")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Return all the existent plans",
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
        log.info("GET all available plans");
        List<Plan> plans = getAllPlansQuery.execute();
        List<ParametricResponse> response = plans.stream().map(this::buildParametricResponse).toList();

        log.info("All plans are retrieved successfully");
        return ResponseEntity.ok(response);
    }

    private ParametricResponse buildParametricResponse(Plan plan) {
        return ParametricResponse.builder()
                .id(plan.getId())
                .description(plan.getDescription())
                .build();
    }
}
