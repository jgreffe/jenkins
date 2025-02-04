package hudson;

import java.io.IOException;
import java.util.logging.Logger;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.jvnet.hudson.test.JenkinsRule;
import org.jvnet.hudson.test.TestExtension;
import org.jvnet.hudson.test.recipes.WithPlugin;

import hudson.model.AperiodicWork;
import hudson.model.AsyncAperiodicWork;
import hudson.model.TaskListener;

public class AsyncAperiodicWorkTest {

    @Rule
    public JenkinsRule j = new JenkinsRule();

    /**
     * Check a plugin containing an Extension with an AsyncAperiodicWork is registered only once
     */
    @Test
    public void checkDynamicLoad_singleRegistration() throws Throwable {
        ExtensionList.lookupSingleton(CustomExtension.class);
    }

    @TestExtension
    public static class CustomExtension {
        public CustomExtension() {
        }

        public static CustomExtension get() {
            return ExtensionList.lookupSingleton(CustomExtension.class);
        }
    }

    @TestExtension
    public static class CustomAsyncAperiodicWorker extends AsyncAperiodicWork {
        private static final Logger LOGGER = Logger.getLogger(CustomAsyncAperiodicWorker.class.getName());

        public CustomAsyncAperiodicWorker() {
            super("custom-async-worker");
            LOGGER.info("CustomAsyncAperiodicWorker created");
        }

        @Override
        protected void execute(TaskListener listener) throws IOException, InterruptedException {
            LOGGER.info(() -> "CustomAsyncAperiodicWorker started");
        }

        @Override
        public long getRecurrencePeriod() {
            LOGGER.info(() -> "CustomAsyncAperiodicWorker recurring period");
            CustomExtension.get();
            return 120;
        }

        @Override
        public AperiodicWork getNewInstance() {
            LOGGER.info(() -> "CustomAsyncAperiodicWorker new instance");
            return new CustomAsyncAperiodicWorker();
        }
    }
}
