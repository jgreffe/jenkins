package io.jenkins.plugins.asyncaperiodictest;

import java.io.IOException;
import java.util.logging.Logger;

import hudson.Extension;
import hudson.model.AperiodicWork;
import hudson.model.AsyncAperiodicWork;
import hudson.model.TaskListener;

@Extension
public class CustomAsyncAperiodicWorker extends AsyncAperiodicWork {

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
