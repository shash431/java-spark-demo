package com.turan.spark.integration;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(format = {"pretty", "json:cucumber-api.json"})

// The "IT" prefix is what makes it an Integration test as far as Maven failsafe is concerned, don't rename!
public class CucumberRunnerIT {

}
