package com.cuoco.adapter.in.controller;

import com.cuoco.adapter.in.controller.model.ParametricResponse;
import com.cuoco.adapter.in.controller.model.UnitResponse;
import com.cuoco.application.port.in.GetAllUnitsQuery;
import com.cuoco.application.usecase.model.Unit;
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
@RequestMapping("/units")
public class UnitControllerAdapter {

    private final GetAllUnitsQuery getAllUnitsQuery;

    public UnitControllerAdapter(GetAllUnitsQuery getAllUnitsQuery) {
        this.getAllUnitsQuery = getAllUnitsQuery;
    }

    @GetMapping
    @Tag(name = "Parametric Endpoints")
    @Operation(summary = "GET all the measure units")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Return all the existent units",
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
    public ResponseEntity<List<UnitResponse>> getAll() {
        log.info("Executing GET all measure units");
        List<Unit> units = getAllUnitsQuery.execute();
        List<UnitResponse> response = units.stream().map(this::buildParametricResponse).toList();

        log.info("All units are retrieved successfully");
        return ResponseEntity.ok(response);
    }

    private UnitResponse buildParametricResponse(Unit unit) {
        return UnitResponse.builder()
                .id(unit.getId())
                .description(unit.getDescription())
                .symbol(unit.getSymbol())
                .build();
    }
}
