package com.cuoco.adapter.in.controller;

import com.cuoco.adapter.in.controller.model.ParametricResponse;
import com.cuoco.application.port.in.GetCookLevelsQuery;
import com.cuoco.application.usecase.model.CookLevel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/cook-level")
public class CookLevelControllerAdapter {

    static final Logger log = LoggerFactory.getLogger(CookLevelControllerAdapter.class);

    private final GetCookLevelsQuery getCookLevelsQuery;

    public CookLevelControllerAdapter(GetCookLevelsQuery getCookLevelsQuery) {
        this.getCookLevelsQuery = getCookLevelsQuery;
    }

    @GetMapping
    public ResponseEntity<List<ParametricResponse>> getAll() {
        log.info("GET all cook levels");

        List<CookLevel> cookLevels = getCookLevelsQuery.execute();
        List<ParametricResponse> response = cookLevels.stream().map(this::buildParametricResponse).toList();

        log.info("All cook levels are retrieved successfully");
        return ResponseEntity.ok(response);
    }

    private ParametricResponse buildParametricResponse(CookLevel cookLevel) {
        return ParametricResponse.builder()
                .id(cookLevel.getId())
                .description(cookLevel.getDescription())
                .build();
    }
}
