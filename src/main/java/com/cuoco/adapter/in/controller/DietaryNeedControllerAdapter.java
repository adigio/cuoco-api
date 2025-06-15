package com.cuoco.adapter.in.controller;

import com.cuoco.adapter.in.controller.model.ParametricResponse;
import com.cuoco.application.port.in.GetAllDietaryNeedsQuery;
import com.cuoco.application.usecase.model.DietaryNeed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/dietary-need")
public class DietaryNeedControllerAdapter {

    static final Logger log = LoggerFactory.getLogger(DietaryNeedControllerAdapter.class);

    private final GetAllDietaryNeedsQuery getAllDietaryNeedsQuery;

    public DietaryNeedControllerAdapter(GetAllDietaryNeedsQuery getAllDietaryNeedsQuery) {
        this.getAllDietaryNeedsQuery = getAllDietaryNeedsQuery;
    }

    @GetMapping
    public ResponseEntity<List<ParametricResponse>> getAll() {
        log.info("GET all dietary needs");

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
