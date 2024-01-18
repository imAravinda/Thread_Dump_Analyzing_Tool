package com.dfn.dump_analyzer_backend.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ThreadDumpAnalyzingResult {

    private Set<ThreadDump> threadDumps;


    public Set<ThreadDump> getThreadDumps() {
        return threadDumps;
    }

    public void setThreadDumps(Set<ThreadDump> threadDumps) {
        this.threadDumps = threadDumps;
    }


    public static class DeadLockResult{
        private  boolean deadlock;
        private List<ThreadDump> threadsInCycle = new ArrayList<>();
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
