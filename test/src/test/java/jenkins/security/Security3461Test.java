package jenkins.security;

import static org.junit.Assert.assertNotNull;

import hudson.model.User;
import hudson.security.SecurityRealm;
import java.io.ObjectStreamException;
import java.io.Serializable;
import jenkins.model.IdStrategy;
import jenkins.model.Jenkins;
import org.junit.Rule;
import org.junit.Test;
import org.jvnet.hudson.test.Issue;
import org.jvnet.hudson.test.JenkinsSessionRule;
import org.kohsuke.stapler.DataBoundSetter;

public class Security3461Test {
    @Rule
    public JenkinsSessionRule sessions = new JenkinsSessionRule();

    @Test
    @Issue("SECURITY-3461")
    public void security3461() throws Throwable {
        sessions.then(r -> {
            MockSecurityRealm securityRealm = new MockSecurityRealm();
            securityRealm.setUserIdStrategy(new IdStrategy.CaseInsensitive());
            Jenkins.get().setSecurityRealm(securityRealm);

            User testUSER = User.getById("testUSER", true);
            testUSER.save();
            User anotherUSER = User.getById("anotherUSER", true);
            anotherUSER.save();

            // when updating the securityRealm, {@link User.reKey()} is hit, forcing to recreate the users.xml
            // but it seems the migration is wrong.
            MockSecurityRealm securityRealm2 = new MockSecurityRealm();
            securityRealm2.setUserIdStrategy(new IdStrategy.CaseSensitive());
            Jenkins.get().setSecurityRealm(securityRealm2);
        });
        sessions.then(r -> {
            User testUSER = User.getById("testUSER", false);
            assertNotNull(testUSER);
        });
    }
}

class MockSecurityRealm extends SecurityRealm implements Serializable {

    private IdStrategy userIdStrategy;

    @Override
    public IdStrategy getUserIdStrategy() {
        return userIdStrategy;
    }

    @DataBoundSetter
    public void setUserIdStrategy(IdStrategy userIdStrategy) {
        this.userIdStrategy = userIdStrategy;
    }

    @Override
    public SecurityComponents createSecurityComponents() {
        return new SecurityComponents();
    }

    protected Object readResolve() throws ObjectStreamException {
        return this;
    }
}
