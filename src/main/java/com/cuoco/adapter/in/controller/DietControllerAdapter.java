package com.cuoco.adapter.in.controller;

import com.cuoco.adapter.in.controller.model.ParametricResponse;
import com.cuoco.application.port.in.GetDietsQuery;
import com.cuoco.application.usecase.model.Diet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/diet")
public class DietControllerAdapter {

    static final Logger log = LoggerFactory.getLogger(DietControllerAdapter.class);

    private final GetDietsQuery getDietsQuery;

    public DietControllerAdapter(GetDietsQuery getDietsQuery) {
        this.getDietsQuery = getDietsQuery;
    }

    @GetMapping
    public ResponseEntity<?> getAll() {
        log.info("GET all diets");
        List<Diet> diets = getDietsQuery.execute();
        List<ParametricResponse> response = diets.stream().map(this::buildParametricResponse).toList();

        log.info("All diets are retrieved successfully");
        return ResponseEntity.ok(response);
    }

    private ParametricResponse buildParametricResponse(Diet diet) {
        return ParametricResponse.builder()
                .id(diet.getId())
                .description(diet.getDescription())
                .build();
    }
}
