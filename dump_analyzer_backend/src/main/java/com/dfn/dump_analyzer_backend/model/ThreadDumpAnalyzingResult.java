package com.dfn.dump_analyzer_backend.model;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class ThreadDumpAnalyzingResult {

    private List<ThreadDump> threadDumps;
    private DeadLockResult deadLockResult;

    public DeadLockResult getDeadLockResult() {
        return deadLockResult;
    }

    public void setDeadLockResult(DeadLockResult deadLockResult) {
        this.deadLockResult = deadLockResult;
    }

    public List<ThreadDump> getThreadDumps() {
        return threadDumps;
    }

    public void setThreadDumps(List<ThreadDump> threadDumps) {
        this.threadDumps = threadDumps;
    }


    public static class DeadLockResult{
        private  boolean deadlock;
        private List<ThreadDump> threadsInCycle;
        private Set<String> deadLockDetails;

        public boolean isDeadlock() {
            return deadlock;
        }

        public void setDeadlock(boolean deadlock) {
            this.deadlock = deadlock;
        }

        public List<ThreadDump> getThreadsInCycle() {
            return threadsInCycle;
        }

        public void setThreadsInCycle(List<ThreadDump> threadsInCycle) {
            this.threadsInCycle = threadsInCycle;
        }

        public Set<String> getDeadLockDetails() {
            return deadLockDetails;
        }

        public void setDeadLockDetails(Set<String> deadLockDetails) {
            this.deadLockDetails = deadLockDetails;
        }
    }

}
