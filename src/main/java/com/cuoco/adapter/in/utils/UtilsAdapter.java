package com.cuoco.adapter.in.utils;

import com.cuoco.adapter.in.controller.model.ParametricResponse;
import com.cuoco.application.usecase.model.Parametric;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class UtilsAdapter {

    public static ParametricResponse mapNull(Parametric source) {
        return source != null ? ParametricResponse.fromDomain(source) : null;
    }

    public static List<ParametricResponse> mapNullOrEmpty(Collection<? extends Parametric> source) {
        return Optional.ofNullable(source)
                .orElse(Collections.emptyList())
                .stream()
                .map(ParametricResponse::fromDomain)
                .toList();
    }
}
