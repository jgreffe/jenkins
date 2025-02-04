package io.jenkins.plugins.asyncaperiodictest;

import java.util.logging.Logger;

import hudson.Extension;
import hudson.ExtensionList;

@Extension
public class CustomExtension {
    private static final Logger LOGGER = Logger.getLogger(CustomExtension.class.getName());

    public CustomExtension() {
        LOGGER.info(() -> "Instantiating CustomExtension");
    }

    public static CustomExtension get() {
        return ExtensionList.lookupSingleton(CustomExtension.class);
    }
}
