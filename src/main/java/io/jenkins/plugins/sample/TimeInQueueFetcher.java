package io.jenkins.plugins.sample;

import hudson.model.Run;

import java.util.concurrent.TimeUnit;

public class TimeInQueueFetcher {
    public long getTimeInQueue(Run build) {
        long queuedTime = build.getStartTimeInMillis() - build.getTimeInMillis();
        return TimeUnit.MILLISECONDS.toMillis(queuedTime);
    }
}