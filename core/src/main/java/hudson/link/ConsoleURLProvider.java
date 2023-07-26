package hudson.link;

import hudson.ExtensionList;
import hudson.ExtensionPoint;
import hudson.Util;
import hudson.model.Run;
import jenkins.model.Jenkins;

public abstract class ConsoleURLProvider implements ExtensionPoint {

    public String getConsoleURL(Run<?, ?> run) {
        return get().getConsoleURL(run);
    }

    public static ExtensionList<ConsoleURLProvider> all() {
        return ExtensionList.lookup(ConsoleURLProvider.class);
    }

    public static ConsoleURLProvider get() {
        return all().stream().findFirst().orElse(ConsoleURLProviderImpl.INSTANCE);
    }

    static class ConsoleURLProviderImpl extends ConsoleURLProvider {

        static final ConsoleURLProvider INSTANCE = new ConsoleURLProviderImpl();

        @Override
        public String getConsoleURL(Run<?, ?> run) {
            return getRoot() + Util.encode(run.getUrl()) + "console";
        }

        public String getRoot() {
            String root = Jenkins.get().getRootUrl();
            if (root == null) {
                root = "http://unconfigured-jenkins-location/";
            }
            return Util.encode(root);
        }
    }
}
