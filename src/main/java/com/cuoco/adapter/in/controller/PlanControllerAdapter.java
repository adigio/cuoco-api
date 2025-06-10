package com.cuoco.adapter.in.controller;

import com.cuoco.adapter.in.controller.model.ParametricResponse;
import com.cuoco.application.port.in.GetPlansQuery;
import com.cuoco.application.usecase.model.Plan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/plan")
public class PlanControllerAdapter {

    static final Logger log = LoggerFactory.getLogger(PlanControllerAdapter.class);

    private final GetPlansQuery getPlansQuery;

    public PlanControllerAdapter(GetPlansQuery getPlansQuery) {
        this.getPlansQuery = getPlansQuery;
    }

    @GetMapping
    public ResponseEntity<?> getAll() {
        log.info("GET all available plans");
        List<Plan> plans = getPlansQuery.execute();
        List<ParametricResponse> response = plans.stream().map(this::buildParametricResponse).toList();

        log.info("All plans retrieved successfully");
        return ResponseEntity.ok(response);
    }

    private ParametricResponse buildParametricResponse(Plan plan) {
        return ParametricResponse.builder()
                .id(plan.getId())
                .description(plan.getDescription())
                .build();
    }
}
