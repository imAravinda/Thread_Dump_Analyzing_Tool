package com.dfn.dump_analyzer_backend.service;

import com.dfn.dump_analyzer_backend.model.ThreadDump;
import com.dfn.dump_analyzer_backend.model.ThreadDumpAnalyzingResult;
import com.sun.org.apache.xpath.internal.operations.Bool;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface ThreadDumpServiceI {
    ThreadDumpAnalyzingResult getAnalyzingResult(MultipartFile file);

    ThreadDumpAnalyzingResult getThredsFilterByState(String state);

    ThreadDumpAnalyzingResult getThreadsFilterByPackage(String Pkg);

    List<ThreadDump.CommonCategories> getSameStackTraces();

    List<ThreadDump.CommonCategories> getPoolCategories();

    ThreadDumpAnalyzingResult getThreadsInSamePool(String pool);

    ThreadDumpAnalyzingResult getDetailsOfDaemonAndNonDaemonThreads(boolean isDaemon);

    ThreadDumpAnalyzingResult.DeadLockResult getDeadLock();

    ThreadDumpAnalyzingResult getHighCPUConsumingThreads();

    ThreadDumpAnalyzingResult getGarbageThreads();

    Map<String, List<ThreadDump>> getThreadsWithSameWitingResource();

}
