package com.dfn.dump_analyzer_backend.controller;

import com.dfn.dump_analyzer_backend.model.ThreadDump;
import com.dfn.dump_analyzer_backend.model.ThreadDumpAnalyzingResult;
import com.dfn.dump_analyzer_backend.service.impl.ThreadDumpService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/thread-dump")
public class ThreadDumpController {
    private ThreadDumpService threadDumpService;

    public ThreadDumpController(ThreadDumpService threadDumpService) {
        super();
        this.threadDumpService = threadDumpService;
    }

    @PostMapping("/analyze")
    public ThreadDumpAnalyzingResult uploadDumpFile(@RequestParam("file") MultipartFile file){
        return threadDumpService.getAnalyzingResult(file);
    }

    @PostMapping("/threadsByState")
    public ThreadDumpAnalyzingResult getThreadsByState(@RequestBody Map<String, String> requestBody){
        String state = requestBody.get("state");
        return threadDumpService.getThredsFilterByState(state);
    }

    @PostMapping("/threadsByPackage")
    public ThreadDumpAnalyzingResult getThreadsByPackageName(@RequestBody Map<String, String> requestBody){
        String pkgName = requestBody.get("packageName");
        return threadDumpService.getThreadsFilterByPackage(pkgName);
    }

    @GetMapping("/sameStackTraces")
    public List<String> getSameStackTraces(){
        return threadDumpService.getThreadsInSameStackTrace();
    }

    @GetMapping("/samePoolThreads")
    public List<ThreadDump.CommonPools> getCommonPoolThreads(){
        return threadDumpService.getPoolCategories();
    }

    @PostMapping("/threadsByPool")
    public ThreadDumpAnalyzingResult getThreadsByPool(@RequestBody Map<String, String> requestBody){
        String poolName = requestBody.get("poolName");
        return threadDumpService.getThreadsInSamePool(poolName);
    }

    @PostMapping("/threadsByDaemonOrNonDaemon")
    public ThreadDumpAnalyzingResult getDetailsOfDaemonOrNonDaemonThreads(@RequestBody Map<String, Boolean> requestBody){
        Boolean isDaemon = requestBody.get("isDaemon");
        return threadDumpService.getDetailsOfDaemonAndNonDaemonThreads(isDaemon);
    }

    @GetMapping("/detectDeadLock")
    public ThreadDumpAnalyzingResult.DeadLockResult detectDeadLock(){
        return threadDumpService.getDeadLock();
    }

    @GetMapping("/highCPUConsumingThreads")
    public ThreadDumpAnalyzingResult getHighCPUConsumingThreads(){
        return threadDumpService.getHighCPUConsumingThreads();
    }

    @GetMapping("/garbageCollecters")
    public ThreadDumpAnalyzingResult getGarbageCollectors(){
        return threadDumpService.getGarbageThreads();
    }
}
