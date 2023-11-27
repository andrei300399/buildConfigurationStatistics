package io.jenkins.plugins.sample;

import hudson.EnvVars;
import hudson.Extension;
import hudson.FilePath;
import hudson.Launcher;
import hudson.model.AbstractProject;
import hudson.model.Run;
import hudson.model.TaskListener;
import hudson.tasks.BuildStepDescriptor;
import hudson.tasks.Builder;
import java.io.IOException;
import jenkins.tasks.SimpleBuildStep;
import org.jenkinsci.Symbol;
import org.kohsuke.stapler.DataBoundConstructor;

public class HelloWorldBuilder extends Builder implements SimpleBuildStep {

    private final String name;

    @DataBoundConstructor
    public HelloWorldBuilder(String name) {
        this.name = name;
    }
    public String getName() {
        return name;
    }


    @Override
    public void perform(Run<?, ?> run, FilePath workspace, EnvVars env, Launcher launcher, TaskListener listener)
            throws InterruptedException, IOException {
        run.addAction(new HelloWorldAction(name));
        listener.getLogger().println("Hello, " + name + "!");
        listener.getLogger().println("Hello, " + run + "!");
        listener.getLogger().println("Hello, " + run.getParent().getBuilds() + "!");
        listener.getLogger().println("Hello, " + run.getParent().getConfigFile().asString() + "!");
    }

    @Symbol("greet")
    @Extension
    public static final class DescriptorImpl extends BuildStepDescriptor<Builder> {

        @Override
        public boolean isApplicable(Class<? extends AbstractProject> aClass) {
            return true;
        }

        @Override
        public String getDisplayName() {
            return Messages.HelloWorldBuilder_DescriptorImpl_DisplayName();
        }
    }
}
