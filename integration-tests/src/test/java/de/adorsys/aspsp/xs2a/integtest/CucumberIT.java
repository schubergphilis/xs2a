package de.adorsys.aspsp.xs2a.integtest;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
    features = "src/test/resources/features",
    glue = "de.adorsys.aspsp.xs2a.integtest.stepdefinitions",
    format = {"pretty", "html:target/report"},
    tags = {"~@ignore"})
public class CucumberIT { }
