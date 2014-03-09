package scratch.spring.webapp;

import cucumber.api.junit.Cucumber;
import org.junit.runner.RunWith;

/**
 * This is the class that will run the Cucumber Scenarios. It is executed with JUnit through the use of the
 * {@link Cucumber} runner.
 * <p/>
 * This runner will then scan the package that this class resides in for classes that have methods annotated with either
 * {@link cucumber.api.java.en.Given}, {@link cucumber.api.java.en.When}, {@link cucumber.api.java.en.Then}, or
 * {@link cucumber.api.java.en.And}. These will be run if they match any steps that have been referenced in any
 * {@code *.feature} files that reside in the same package.
 *
 * @author Karl Bennett
 */
@RunWith(Cucumber.class)
@Cucumber.Options(format = {"pretty", "html:target/cucumber-html-report", "json:target/cucumber-json-report.json"})
public class ITRunCucumber {
}