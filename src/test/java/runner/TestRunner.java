package runner;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;

@CucumberOptions(
    features = "src/test/resources/features/sauce_demo.feature",  //  .feature file
    glue = {"steps"},                                             //  Step Definitions
    plugin = {"pretty", "html:target/cucumber-reports.html"},      // report
    monochrome = true                                             
)
public class TestRunner extends AbstractTestNGCucumberTests {
}
