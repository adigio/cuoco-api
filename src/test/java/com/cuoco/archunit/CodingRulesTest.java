package com.cuoco.archunit;

import com.tngtech.archunit.base.DescribedPredicate;
import com.tngtech.archunit.core.domain.JavaClass.Predicates;
import com.tngtech.archunit.core.importer.ImportOption.DoNotIncludeTests;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition;
import com.tngtech.archunit.library.GeneralCodingRules;

@AnalyzeClasses(packages = "com.cuoco", importOptions = DoNotIncludeTests.class)
public class CodingRulesTest {

    @ArchTest
    static final ArchRule exceptions_should_respect_naming_convention =
            ArchRuleDefinition.classes()
                    .that().resideInAPackage("..exception..")
                    .should().haveSimpleNameEndingWith("Exception");

    @ArchTest
    static final ArchRule use_cases_should_respect_naming_convention =
            ArchRuleDefinition.classes()
                    .that().resideInAPackage("..application.usecase..")
                    .and(DescribedPredicate.not(Predicates.resideInAnyPackage("..usecase.model..")))
                    .and(DescribedPredicate.not(Predicates.resideInAnyPackage("..usecase.domainservice..")))
                    .and().haveNameNotMatching(".*\\$.*")
                    .should().haveSimpleNameEndingWith("UseCase");

    @ArchTest
    static final ArchRule domain_services_should_respect_naming_convention =
            ArchRuleDefinition.classes()
                    .that().resideInAPackage("..usecase.domainservice..")
                    .and().haveNameNotMatching(".*\\$.*")
                    .should().haveSimpleNameEndingWith("DomainService");

    @ArchTest
    static final ArchRule in_ports_should_respect_naming_convention =
            ArchRuleDefinition.classes()
                    .that().areInterfaces().and().resideInAPackage("..application.port.in..")
                    .should().haveSimpleNameEndingWith("Query")
                    .orShould().haveSimpleNameEndingWith("Command");

    @ArchTest
    static final ArchRule out_ports_should_respect_naming_convention =
            ArchRuleDefinition.classes()
                    .that().areInterfaces().and().resideInAPackage("..application.port.out..")
                    .should().haveSimpleNameEndingWith("Repository");

    @ArchTest
    static final ArchRule adapters_should_respect_naming_convention =
            ArchRuleDefinition.classes()
                    .that().resideInAPackage("..adapter.in.*").or().resideInAPackage("..adapter.out.*")
                    .and().haveNameNotMatching(".*\\$.*")
                    .should().haveSimpleNameEndingWith("Adapter");

    @ArchTest
    static final ArchRule no_generic_exceptions = GeneralCodingRules.NO_CLASSES_SHOULD_THROW_GENERIC_EXCEPTIONS;

    @ArchTest
    static final ArchRule no_standard_streams = GeneralCodingRules.NO_CLASSES_SHOULD_ACCESS_STANDARD_STREAMS;

    @ArchTest
    static final ArchRule no_java_logging = GeneralCodingRules.NO_CLASSES_SHOULD_USE_JAVA_UTIL_LOGGING;

    @ArchTest
    static final ArchRule no_jodatime = GeneralCodingRules.NO_CLASSES_SHOULD_USE_JODATIME;

    @ArchTest
    static final ArchRule classes_must_not_be_suffixed_with_impl =
            ArchRuleDefinition.noClasses()
                    .should().haveSimpleNameEndingWith("Impl")
                    .because("En serio, y si nos esforzamos un poco más?");
}