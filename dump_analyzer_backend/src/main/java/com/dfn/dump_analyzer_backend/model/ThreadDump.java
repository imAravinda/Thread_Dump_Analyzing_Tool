package com.dfn.dump_analyzer_backend.model;


public class ThreadDump {

    private String State;
    private String nativeId;
    private String threadId;
    private String resourceId;
    private boolean isBlocked = false;
    private String lockedResourceId;
    private String waitingResourceId;
    private String PackageDetailsAffectedByThread;
    private String packageName;
    private String className;
    private String methodName;
    private String StackTrace;
    private boolean isDaemon = false;
    private String pool;
    private CommonPools commonPools;
    private double cpuTime;
    private int priority;
    private double elapsedTime;
    private String lineNo;

    public String getState() {
        return State;
    }

    public void setState(String state) {
        State = state;
    }

    public String getNativeId() {
        return nativeId;
    }

    public void setNativeId(String nativeId) {
        this.nativeId = nativeId;
    }

    public String getThreadId() {
        return threadId;
    }

    public void setThreadId(String threadId) {
        this.threadId = threadId;
    }

    public String getResourceId() {
        return resourceId;
    }

    public void setResourceId(String resourceId) {
        this.resourceId = resourceId;
    }

    public boolean isBlocked() {
        return isBlocked;
    }

    public void setBlocked(boolean blocked) {
        isBlocked = blocked;
    }

    public String getLockedResourceId() {
        return lockedResourceId;
    }

    public void setLockedResourceId(String lockedResourceId) {
        this.lockedResourceId = lockedResourceId;
    }

    public String getWaitingResourceId() {
        return waitingResourceId;
    }

    public void setWaitingResourceId(String waitingResourceId) {
        this.waitingResourceId = waitingResourceId;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public String getPackageDetailsAffectedByThread() {
        return PackageDetailsAffectedByThread;
    }

    public void setPackageDetailsAffectedByThread(String packageDetailsAffectedByThread) {
        PackageDetailsAffectedByThread = packageDetailsAffectedByThread;
    }

    public String getStackTrace() {
        return StackTrace;
    }

    public void setStackTrace(String stackTrace) {
        StackTrace = stackTrace;
    }

    public boolean isDaemon() {
        return isDaemon;
    }

    public void setDaemon(boolean daemon) {
        isDaemon = daemon;
    }

    public String getPool() {
        return pool;
    }

    public void setPool(String pool) {
        this.pool = pool;
    }

    public CommonPools getCommonPools() {
        return commonPools;
    }

    public void setCommonPools(CommonPools commonPools) {
        this.commonPools = commonPools;
    }

    public double getCpuTime() {
        return cpuTime;
    }

    public void setCpuTime(double cpuTime) {
        this.cpuTime = cpuTime;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public double getElapsedTime() {
        return elapsedTime;
    }

    public void setElapsedTime(double elapsedTime) {
        this.elapsedTime = elapsedTime;
    }

    public String getLineNo() {
        return lineNo;
    }

    public void setLineNo(String lineNo) {
        this.lineNo = lineNo;
    }

    public static class CommonPools{
        private String poolName;
        private int count;

        public String getPoolName() {
            return poolName;
        }

        public void setPoolName(String poolName) {
            this.poolName = poolName;
        }

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }
    }
}
