package hudson;

import java.io.File;
import java.net.URL;

import org.apache.commons.io.FileUtils;
import org.junit.Rule;
import org.junit.Test;
import org.jvnet.hudson.test.JenkinsRule;

public class AsyncAperiodicWorkWithDynamicLoadTest {

    @Rule
    public JenkinsRule j = new JenkinsRule();

    /**
     * Check a plugin containing an Extension with an AsyncAperiodicWork is registered only once
     */
    @Test
    public void checkDynamicLoad_singleRegistration() throws Throwable {
        URL res = AsyncAperiodicWorkWithDynamicLoadTest.class.getClassLoader().getResource("plugins/async-aperiodic-test.hpi");
        File f = new File(j.jenkins.getRootDir(), "plugins/async-aperiodic-test.jpi");
        FileUtils.copyURLToFile(res, f);
        j.jenkins.pluginManager.dynamicLoad(f);
        ExtensionList.lookupSingleton(
                j.jenkins.pluginManager.uberClassLoader.loadClass("io.jenkins.plugins.asyncaperiodictest.CustomExtension"));
    }
}
