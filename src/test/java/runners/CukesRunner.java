package runners;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.AfterClass;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(

        features = "src/test/resources/features",
        glue = "stepDefinitions",
        plugin = {"pretty", "json:target/cucumber.json"},
        dryRun = false,
        tags = "@travel"


)
public class CukesRunner {

}