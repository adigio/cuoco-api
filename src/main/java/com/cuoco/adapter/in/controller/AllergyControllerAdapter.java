package com.cuoco.adapter.in.controller;

import com.cuoco.adapter.in.controller.model.ParametricResponse;
import com.cuoco.application.port.in.GetAllAllergiesQuery;
import com.cuoco.application.usecase.model.Allergy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/allergy")
public class AllergyControllerAdapter {

    static final Logger log = LoggerFactory.getLogger(AllergyControllerAdapter.class);

    private final GetAllAllergiesQuery getAllAllergiesQuery;

    public AllergyControllerAdapter(GetAllAllergiesQuery getAllAllergiesQuery) {
        this.getAllAllergiesQuery = getAllAllergiesQuery;
    }

    @GetMapping
    public ResponseEntity<List<ParametricResponse>> getAll() {
        log.info("GET all allergies");
        List<Allergy> allergies = getAllAllergiesQuery.execute();
        List<ParametricResponse> response = allergies.stream().map(this::buildParametricResponse).toList();

        log.info("All allergies are retrieved successfully");
        return ResponseEntity.ok(response);
    }

    private ParametricResponse buildParametricResponse(Allergy allergy) {
        return ParametricResponse.builder()
                .id(allergy.getId())
                .description(allergy.getDescription())
                .build();
    }
}
