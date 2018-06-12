package uk.co.compendiumdev.sourceextractor.cucumber;

import cucumber.api.junit.Cucumber;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@Cucumber.Options(format={"pretty", "html:target/cucumber"})
public class RunCukeTest {
}