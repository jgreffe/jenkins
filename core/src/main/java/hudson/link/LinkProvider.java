package hudson.link;

import hudson.ExtensionList;
import hudson.ExtensionPoint;
import hudson.model.Job;
import hudson.model.Run;

public abstract class LinkProvider implements ExtensionPoint {

    public String getRedirectURL(Run<?, ?> run) {
        return get().getRedirectURL(run);
    }

    public String getRedirectURL(Job<?, ?> job) {
        return get().getRedirectURL(job);
    }

    public static ExtensionList<LinkProvider> all() {
        return ExtensionList.lookup(LinkProvider.class);
    }

    public static LinkProvider get() {
        return all().stream().findFirst().orElse(LinkProviderImpl.INSTANCE);
    }

    static class LinkProviderImpl extends LinkProvider {

        static final LinkProvider INSTANCE = new LinkProviderImpl();

        @Override
        public String getRedirectURL(Run<?, ?> run) {
            return run.getUrl();
        }

        @Override
        public String getRedirectURL(Job<?, ?> job) {
            return job.getUrl();
        }
    }
}
