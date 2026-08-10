package com.ktnn.projects.cucumber.runner;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;

@CucumberOptions(
        features = "src/test/resources/features/pickList",
        glue = {"com.ktnn.projects.cucumber.hooks", "com.ktnn.projects.cucumber.steps"},
        plugin = {"pretty"},
        monochrome = true
)
public class PickListCucumberRunner extends AbstractTestNGCucumberTests {
}
