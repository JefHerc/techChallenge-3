package com.fiap.gestao_servicos.architecture;

import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

@AnalyzeClasses(
        packages = "com.fiap.gestao_servicos",
        importOptions = {ImportOption.DoNotIncludeTests.class}
)
class ArchitectureTest {

    @ArchTest
    static final ArchRule core_should_not_depend_on_frameworks_or_infrastructure =
            noClasses()
                    .that().resideInAPackage("..core..")
                    .should().dependOnClassesThat()
                    .resideInAnyPackage(
                            "..infrastructure..",
                            "org.springframework..",
                            "jakarta.."
                    );

    @ArchTest
    static final ArchRule controllers_should_depend_only_on_usecases_from_core =
            noClasses()
                    .that().resideInAPackage("..infrastructure.controller..")
                    .should().dependOnClassesThat()
                    .resideInAnyPackage(
                            "..core.repository..",
                            "..infrastructure.persistence.."
                    );

    @ArchTest
    static final ArchRule usecases_should_not_depend_on_infrastructure =
            noClasses()
                    .that().resideInAPackage("..core.usecase..")
                    .should().dependOnClassesThat()
                    .resideInAnyPackage("..infrastructure..");

}
