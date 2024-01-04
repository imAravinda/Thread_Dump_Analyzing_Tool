package com.dfn.dump_analyzer_backend.service;

import com.dfn.dump_analyzer_backend.model.ThreadDump;
import com.dfn.dump_analyzer_backend.model.ThreadDumpAnalyzingResult;
import com.sun.org.apache.xpath.internal.operations.Bool;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ThreadDumpServiceI {
    ThreadDumpAnalyzingResult getAnalyzingResult(MultipartFile file);

    ThreadDumpAnalyzingResult getThredsFilterByState(String state);

    ThreadDumpAnalyzingResult getThreadsFilterByPackage(String Pkg);

    ThreadDumpAnalyzingResult getThreadsFilterByClass(String className);

    ThreadDumpAnalyzingResult getThreadsFilterByMethod(String methodName);

    List<String> getThreadsInSameStackTrace();

    List<ThreadDump.CommonPools> getPoolCategories();

    ThreadDumpAnalyzingResult getThreadsInSamePool(String pool);

    ThreadDumpAnalyzingResult getDetailsOfDaemonAndNonDaemonThreads(boolean isDaemon);

    ThreadDumpAnalyzingResult.DeadLockResult getDeadLock();

    ThreadDumpAnalyzingResult getHighCPUConsumingThreads();

    ThreadDumpAnalyzingResult getBlockedThreads();

    ThreadDumpAnalyzingResult getGarbageThreads();
}
