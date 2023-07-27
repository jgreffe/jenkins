package hudson.model;

import hudson.link.ConsoleURLProvider;

import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.jvnet.hudson.test.JenkinsRule;

public class RunConsoleTest {

    @Rule
    public JenkinsRule j = new JenkinsRule();

    @Test
    public void defaultConsoleUrl() throws Exception {
        FreeStyleProject p = j.createFreeStyleProject("something");
        FreeStyleBuild b = j.buildAndAssertSuccess(p);
        MatcherAssert.assertThat(b.getConsoleUrl(), Matchers.endsWith("job/something/1/console"));
    }

    @Test
    public void extendedConsoleUrl() throws Exception {
        j.jenkins.getExtensionList(ConsoleURLProvider.class).add(0, new CustomConsoleURLProvider());
        FreeStyleProject p = j.createFreeStyleProject("something");
        FreeStyleBuild b = j.buildAndAssertSuccess(p);
        Assert.assertEquals("job/something/1/custom", b.getConsoleUrl());
    }

    public static final class CustomConsoleURLProvider extends ConsoleURLProvider {
        @Override
        public String getConsoleURL(Run<?, ?> run) {
            return run.getUrl() + "custom";
        }
    }
}
