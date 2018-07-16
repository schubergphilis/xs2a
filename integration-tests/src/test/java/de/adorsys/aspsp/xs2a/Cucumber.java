package de.adorsys.aspsp.xs2a;

import cucumber.api.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(cucumber.api.junit.Cucumber.class)
@CucumberOptions(
    features = "src/test/resources/features",
    glue = "de.adorsys.aspsp.xs2a.stepdefinitions")
public class Cucumber { }
