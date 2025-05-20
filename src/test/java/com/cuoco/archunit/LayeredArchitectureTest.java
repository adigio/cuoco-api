package com.cuoco.archunit;

import com.tngtech.archunit.core.importer.ImportOption.DoNotIncludeTests;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;
import com.tngtech.archunit.library.Architectures;

@AnalyzeClasses(packages = "com.cuoco", importOptions = DoNotIncludeTests.class)
public class LayeredArchitectureTest {

    private static final String ADAPTER = "adapter";
    private static final String APPLICATION = "application";
    private static final String CONFIG = "configuration";

    @ArchTest
    static final ArchRule layer_dependencies_are_respected = Architectures.layeredArchitecture()
            .consideringOnlyDependenciesInLayers()
            .layer(CONFIG).definedBy("com.cuoco.shared.config..")
            .layer(ADAPTER).definedBy("com.cuoco.adapter..")
            .layer(APPLICATION).definedBy("com.cuoco.application..")
            .whereLayer(APPLICATION).mayOnlyBeAccessedByLayers(ADAPTER, CONFIG)
            .whereLayer(ADAPTER).mayOnlyBeAccessedByLayers(CONFIG);
}