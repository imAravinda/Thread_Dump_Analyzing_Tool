package com.dfn.dump_analyzer_backend.service.impl;

import com.dfn.dump_analyzer_backend.model.ThreadDump;
import com.dfn.dump_analyzer_backend.model.ThreadDumpAnalyzingResult;
import com.dfn.dump_analyzer_backend.service.ThreadDumpServiceI;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class ThreadDumpService implements ThreadDumpServiceI {

    private Set<ThreadDump> threadDumps = new HashSet<>();
    private ThreadDumpAnalyzingResult threadDumpAnalyzingResult =  new ThreadDumpAnalyzingResult();

    @Override
    public ThreadDumpAnalyzingResult getAnalyzingResult(MultipartFile file) {
        Set<ThreadDump> threadDumpsLocal = new HashSet<>();
        String fileName = file.getOriginalFilename();
        if (fileName != null && !(fileName.endsWith(".txt") || fileName.endsWith(".tdump"))) {
            throw new IllegalArgumentException("Invalid file type. Please upload a .txt or .tdump file.");
        }
        try {
            String fileContent = new String(file.getBytes(), StandardCharsets.UTF_8);
            processFileContent(fileContent, threadDumpsLocal);
        } catch (IOException e) {
            throw new RuntimeException("Error reading the file", e);
        }
        threadDumps = threadDumpsLocal;
        threadDumpAnalyzingResult.setThreadDumps(threadDumps);
        return threadDumpAnalyzingResult;
    }

    @Override
    public ThreadDumpAnalyzingResult getThredsFilterByState(String state){
        Set<ThreadDump> filteredThreadDumps = new HashSet<>();
        ThreadDumpAnalyzingResult threadDumpAnalyzingResult = new ThreadDumpAnalyzingResult();
        for (ThreadDump threadDump : threadDumps) {
            if (threadDump.getState().toLowerCase().equals(state.toLowerCase())) {
                filteredThreadDumps.add(threadDump);
            }
        }
        threadDumpAnalyzingResult.setThreadDumps(filteredThreadDumps);
        return threadDumpAnalyzingResult;
    }

    @Override
    public ThreadDumpAnalyzingResult getThreadsFilterByPackage(String Pkg) {
        Set<ThreadDump> filteredThreadDumps = new HashSet<>();
        ThreadDumpAnalyzingResult threadDumpAnalyzingResult = new ThreadDumpAnalyzingResult();
        for (ThreadDump threadDump : threadDumps) {
            if (threadDump.getPackageDetailsAffectedByThread() != null && threadDump.getPackageDetailsAffectedByThread().contains(Pkg)) {
                filteredThreadDumps.add(threadDump);
            }
        }
        threadDumpAnalyzingResult.setThreadDumps(filteredThreadDumps);
        return threadDumpAnalyzingResult;
    }

    @Override
    public List<ThreadDump.CommonCategories> getPoolCategories() {
        List<String> pools = new ArrayList<>();
        for(ThreadDump threadDump:threadDumps){
            String pool = threadDump.getPool().replaceAll("\\d+$","");
            pools.add(pool);
        }
        ThreadDump.CommonCategories commonPools;
        Set<String> uniquePoolNames = new HashSet<>(pools);
        List<ThreadDump.CommonCategories> commonPoolsList = new ArrayList<>();
        for (String poolName : uniquePoolNames) {
            int count = 0;
            for (String pool : pools) {
                if (pool.equals(poolName)) {
                    count++;
                }
            }

            if (count > 1) {
                commonPools = new ThreadDump.CommonCategories();
                commonPools.setPoolName(poolName);
                commonPools.setCount(String.valueOf(count));
                commonPoolsList.add(commonPools);
            }
        }
        return commonPoolsList;
    }

    @Override
    public ThreadDumpAnalyzingResult getThreadsInSamePool(String pool) {
        Set<ThreadDump> dumpsInSamePool = new HashSet<>();
        ThreadDumpAnalyzingResult threadDumpAnalyzingResult = new ThreadDumpAnalyzingResult();
        for(ThreadDump threadDump:threadDumps){
            String modifiedPoolName = threadDump.getPool().replaceAll("\\d+$","");
            if(modifiedPoolName.equals(pool)){
                dumpsInSamePool.add(threadDump);
            }
        }
        threadDumpAnalyzingResult.setThreadDumps(dumpsInSamePool);
        return threadDumpAnalyzingResult;
    }

    @Override
    public ThreadDumpAnalyzingResult getDetailsOfDaemonAndNonDaemonThreads(boolean isDaemon) {
        Set<ThreadDump> Threads = new HashSet<>();
        ThreadDumpAnalyzingResult threadDumpAnalyzingResult = new ThreadDumpAnalyzingResult();
        for(ThreadDump threadDump:threadDumps){
            if(threadDump.isDaemon() == isDaemon){
               Threads.add(threadDump);
            }
        }
        threadDumpAnalyzingResult.setThreadDumps(Threads);
        return threadDumpAnalyzingResult;
    }


    @Override
    public ThreadDumpAnalyzingResult.DeadLockResult getDeadLock() {
        List<ThreadDump> blockedThreads = threadDumps.stream()
                .filter(threadDump -> "BLOCKED".equals(threadDump.getState()))
                .collect(Collectors.toList());

        Map<String, Set<String>> lockGraph = new HashMap<>();
        ThreadDumpAnalyzingResult.DeadLockResult deadLockResult = new ThreadDumpAnalyzingResult.DeadLockResult();
        List<ThreadDump> dumpList = new ArrayList<>();

        String lockedResourceId = "";
        String waitingResourceId = "";
        List<String> lockedResourceIds = new ArrayList<>();

        // Build a graph representing lock dependencies
        for (ThreadDump threadDump : blockedThreads) {
            lockedResourceId = threadDump.getLockedResourceId();
            waitingResourceId = threadDump.getWaitingResourceId();

            if (lockedResourceId != null && waitingResourceId != null) {
                lockGraph.computeIfAbsent(lockedResourceId, k -> new HashSet<>()).add(waitingResourceId);
                lockedResourceIds.add(lockedResourceId);
            }
        }

        // Detect cycles in the graph using depth-first search
        Set<String> visited = new HashSet<>();
        Set<String> inProgress = new HashSet<>();

        for (String node : lockGraph.keySet()) {
            if (!visited.contains(node) && hasCycle(lockGraph, node, visited, inProgress)) {
                deadLockResult.setDeadlock(true);
                break;
            }
        }
        if(deadLockResult.isDeadlock()){
            for(String lockedResourceIdNew:lockedResourceIds){
                for(ThreadDump threadDump:threadDumps){
                    if(lockedResourceIdNew.equals(threadDump.getWaitingResourceId())){
                        dumpList.add(threadDump);
                    }
                }
            }
            deadLockResult.setThreadsInCycle(dumpList);
        }
        return deadLockResult;
    }

    @Override
    public ThreadDumpAnalyzingResult getHighCPUConsumingThreads() {
        ThreadDumpAnalyzingResult threadDumpAnalyzingResult = new ThreadDumpAnalyzingResult();
        Set<ThreadDump> threadDumpList;
        Set<ThreadDump> filteredDumps = new HashSet<>();
        threadDumpList = getThredsFilterByState("RUNNABLE").getThreadDumps();
        for(ThreadDump threadDump:threadDumpList){
            String stackTrace = threadDump.getStackTrace();
            String regEx;
            if(threadDump.getCpuTime() > 1000){
                if(stackTrace.contains("java.lang.Thread.State")){
                    if(stackTrace.contains("Compiling") || hasHotspot(stackTrace)){
                        filteredDumps.add(threadDump);
                    }
                }
            }
            if(stackTrace.contains("sun.nio.ch.Net.accept") || stackTrace.contains("sun.nio.ch.Iocp")){
                filteredDumps.add(threadDump);
            }
            regEx = "\\b" + "Loop" + "\\b";
            if(extractDetails(stackTrace,regEx) != null){
                filteredDumps.add(threadDump);
            }
            if(stackTrace.contains("ThreadPoolExecutor")){
                filteredDumps.add(threadDump);
            }
        }
        threadDumpAnalyzingResult.setThreadDumps(filteredDumps);
        return threadDumpAnalyzingResult;

    }

    @Override
    public ThreadDumpAnalyzingResult getGarbageThreads() {
        Set<ThreadDump> garbageDumps = new HashSet<>();
        ThreadDumpAnalyzingResult garbageDumpResult = new ThreadDumpAnalyzingResult();
        for(ThreadDump threadDump:threadDumps){
            if(threadDump.getPool().contains("GC") || threadDump.getPool().contains("G1 Conc")){
                garbageDumps.add(threadDump);
            }
        }
        garbageDumpResult.setThreadDumps(garbageDumps);
        return garbageDumpResult;
    }

    @Override
    public List<ThreadDump.CommonCategories> getSameStackTraces() {
        Map<String, List<ThreadDump>> stackTraceMap = new HashMap<>();

        // Populate the map with thread dumps grouped by stack trace
        for (ThreadDump threadDump : threadDumps) {
            String stackTrace = threadDump.getStackTrace().replaceAll("<0x[0-9a-fA-F]+>","");

            if (!stackTraceMap.containsKey(stackTrace)) {
                stackTraceMap.put(stackTrace, new ArrayList<>());
            }

            stackTraceMap.get(stackTrace).add(threadDump);
        }

        // Extract common stack traces with counts
        List<ThreadDump.CommonCategories> commonStackTraceList = new ArrayList<>();
        for (Map.Entry<String, List<ThreadDump>> entry : stackTraceMap.entrySet()) {
            String stackTrace = entry.getKey();
            List<ThreadDump> relatedThreadDumps = entry.getValue();

            if (relatedThreadDumps.size() > 1) {
                ThreadDump.CommonCategories commonStackTrace = new ThreadDump.CommonCategories();
                commonStackTrace.setStackTrace(stackTrace);
                commonStackTrace.setCount(relatedThreadDumps.get(0).getState().toUpperCase() + " - " + relatedThreadDumps.size());
                commonStackTrace.setRelatedThreadDumps(relatedThreadDumps);
                commonStackTraceList.add(commonStackTrace);
            }
        }

        return commonStackTraceList;
    }

    @Override
    public Map<String, List<ThreadDump>> getThreadsWithSameWitingResource() {
        Map<String, List<ThreadDump>> dumps = new HashMap<>();

        for (ThreadDump threadDump : threadDumps) {
            if (threadDump.getWaitingResourceId() != null) {
                String waitingID = threadDump.getWaitingResourceId();
                dumps.computeIfAbsent(waitingID, k -> new ArrayList<>()).add(threadDump);
            }
        }

        return dumps;
    }

    private boolean hasCycle(Map<String, Set<String>> graph, String node, Set<String> visited, Set<String> inProgress) {
        visited.add(node);
        inProgress.add(node);

        for (String neighbor : graph.getOrDefault(node, new HashSet<>())) {
            if (!visited.contains(neighbor)) {
                if (hasCycle(graph, neighbor, visited, inProgress)) {
                    return true;
                }
            } else if (inProgress.contains(neighbor)) {
                return true; // Cycle detected
            }
        }

        inProgress.remove(node);
        return false;
    }

    private void processFileContent(String fileContent, Set<ThreadDump> threadDumps) {
        String[] lines = fileContent.split("\\r?\\n");
        boolean inThreadSection = false;
//        List<String> stackTraceLines = new ArrayList<>();
        StringBuilder currentThreadSection = new StringBuilder();
        for (int i = 0; i < lines.length-1; i++) {
            String line = lines[i];
            String nextLine = lines[i+1];
            if (line.startsWith("\"")) {
                currentThreadSection = new StringBuilder(line);
//                stackTraceLines.clear();
//                stackTraceLines.add(line);

            } else {
                currentThreadSection.append(line);
//                stackTraceLines.add(line);

                if( (line.trim().isEmpty() && (nextLine.startsWith("\"") || nextLine.startsWith("JNI")))){
                    inThreadSection = true;
                }
            }

            if (inThreadSection) {
//                if (!stackTraceLines.isEmpty()) {
//                    processStackTrace(stackTraceLines);
//                }
                inThreadSection = false;
                ThreadDump threadDump = populateThreadsDetails(currentThreadSection.toString());
                if (threadDump != null) {
                    threadDumps.add(threadDump);
                }
            }
        }
    }

//    private String processStackTrace(List<String> stackTraceLines) {
//        String StackTraceLocally = "";
//        for (String stackTraceLine : stackTraceLines) {
//            StackTraceLocally += stackTraceLine;
//        }
//        stackTrace = StackTraceLocally;
//        return stackTrace;
//    }

    private ThreadDump populateThreadsDetails(String line) {
        ThreadDump threadDump = new ThreadDump();
        threadDump.setThreadId(extractTid(line));
        threadDump.setNativeId(extractNid(line));
        threadDump.setState(extractState(line));
        if(threadDump.getState() != null && (threadDump.getState().equals("BLOCKED") || threadDump.getState().equals("WAITING") || threadDump.getState().equals("TIMED_WAITING"))){
                threadDump.setWaitingResourceId(extractWaitingOnResourceId(line));
        }
        threadDump.setLockedResourceId(extractLockedResourceId(line));
        threadDump.setPackageDetailsAffectedByThread(extractPackageDetailsAffectedByThread(line));
        setPackageDetails(threadDump);
        threadDump.setStackTrace(extractStackTrace(line));
        threadDump.setDaemon(identifyDeamonThreads(line));
        threadDump.setPool(extractPools(line));
        threadDump.setCpuTime(extractCpuTime(line));
        threadDump.setPriority(extractPriority(line));
        threadDump.setElapsedTime(extractElapsedTime(line));
        if(threadDump.getState() != null && threadDump.getState().equals("BLOCKED")){
            threadDump.setLineNo(extractCausedLinesForBlocking(line));
        }
        return threadDump.getThreadId() != null && threadDump.getState() != null ? threadDump : null; // Return null if no thread ID found
    }

    private String extractDetails(String line,String regEx){
        Pattern pattern = Pattern.compile(regEx);
        Matcher matcher = pattern.matcher(line);
        if(matcher.find()){
            return matcher.group(1);
        }
        return null;
    }

    private String extractTid(String line){
        Pattern pattern = Pattern.compile("(tid|id)=(0x[0-9a-fA-F]+|\\d+)");

        Matcher matcher = pattern.matcher(line);

        if (matcher.find()) {
            String value = matcher.group(2);
            if (value.startsWith("0x")) {
                // Hexadecimal value
                return value.substring(2);
            } else {
                // Decimal value
                return value;
            }
        } else {
            // Handle invalid input
            return null;
        }
    }

    private String extractState(String line){
        String regEx ="java\\.lang\\.Thread\\.State:\\s*(\\w+)";
        String regEx2 = "state=(\\w+)";
        String regEx3 = "nid=[0-9a-fA-Fx]+\\s+(\\S+)";
        if(extractDetails(line,regEx) != null){
            return extractDetails(line,regEx);
        }
        else if(extractDetails(line,regEx2) != null){
            return extractDetails(line,regEx2);
        }
        else{
            return extractDetails(line,regEx3);
        }
    }

    private String extractNid(String line){
        String regEx = "nid=([^\\s]+)";
        return extractDetails(line,regEx);
    }

    private String extractLockedResourceId(String line){
        String regEx = "- locked <(0x[\\da-fA-F]+)>.*";
        if(extractDetails(line,regEx) != null){
            return extractDetails(line,regEx);
        }else {
            return null;
        }
    }

    private String extractWaitingOnResourceId(String line){
        String regEx = "- waiting on <(0x[\\da-fA-F]+)>.*";
        String regEx1 = "- waiting to lock <(0x[\\da-fA-F]+)>.*";
        String regEx2 = "- parking to wait for  <(0x[\\da-fA-F]+)>.*";
        if(extractDetails(line,regEx) != null){
            return extractDetails(line,regEx);
        } else if (extractDetails(line,regEx1) != null) {
            return extractDetails(line,regEx1);
        } else{
            return extractDetails(line,regEx2);
        }
    }



    private String extractPackageDetailsAffectedByThread(String line) {
        String Regex = "at\\s+([^\\s]*+\\s+\\w*)";
        String modifiesLine = extractDetails(line, Regex);
        if(modifiesLine != null){
            int indexOfBracket = modifiesLine.indexOf('(');

            // Remove everything after the first opening bracket, if found
            if (indexOfBracket != -1) {
                return modifiesLine.substring(0, indexOfBracket);
            } else {
                return modifiesLine;
            }
        }
        return null;
    }

    private void setPackageDetails(ThreadDump threadDump){
        if(threadDump.getPackageDetailsAffectedByThread() != null){
            String line = threadDump.getPackageDetailsAffectedByThread();
            System.out.println(line);
            String[] words = line.split("[.$]|(?<=[a-z])(?=[A-Z])|\\d+");
            threadDump.setMethodName(words[words.length-1]);
            threadDump.setClassName(words[words.length-2]);
            threadDump.setPackageName(words[words.length-3]);
        }
    }

    private boolean identifyDeamonThreads(String line){
        String targetWord = "daemon";
        String regEx = "\\b" + targetWord + "\\b";
        Pattern pattern = Pattern.compile(regEx, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(line);
        if(matcher.find()){
            return true;
        }
        else {
            return false;
        }
    }

    private String extractPools(String line){
        String regEx = "\"([^\"]*)\"";
        return extractDetails(line,regEx);
    }

    private double extractCpuTime(String line){
        String regEx = "cpu=([0-9]+\\.[0-9]+)ms";
        String cpuTime = extractDetails(line,regEx);
        double cpuTimeValue = 0;
        if(cpuTime != null){
            cpuTimeValue = Double.parseDouble(cpuTime);
        }
        return cpuTimeValue;
    }

    private int extractPriority(String line){
        String regEx = "prio=([0-9])";
        int priority = 0;
        if(extractDetails(line,regEx) != null){
            priority = Integer.parseInt(extractDetails(line,regEx));
        }
        return priority;
    }

    private double extractElapsedTime(String line){
        String regEx = "elapsed=([0-9]+\\.[0-9]+)s";
        String elapsedTime = extractDetails(line,regEx);
        double elapsedTimeValue = 0;
        if(elapsedTime != null){
            elapsedTimeValue = Double.parseDouble(elapsedTime);
        }
        return elapsedTimeValue;
    }

    private String extractCausedLinesForBlocking(String line){
        String regEx = "at\\s+([^\\s]*+\\s+\\w*)";
        String regEx1 = "\\(([^)]+)\\)";
        if(extractDetails(line,regEx) != null){
            String detailLine = extractDetails(line, regEx);
            return extractDetails(detailLine,regEx1);
        }
        return null;
    }

    private boolean hasHotspot(String stackTrace) {
        String[] lines = stackTrace.split("\t");
        Map<String, List<Integer>> lineOccurrences = new HashMap<>();

        for (int i = 0; i < lines.length; i++) {
            String line = lines[i];
            lineOccurrences
                    .computeIfAbsent(line, k -> new ArrayList<>())
                    .add(i + 1); // Adding 1 because line numbers usually start from 1
        }

        return lineOccurrences
                .entrySet()
                .stream()
                .anyMatch(entry -> entry.getValue().size() > 1);
    }

    private String extractStackTrace(String line){
        String regEx = "java\\.lang\\.Thread\\.State:.*?\\tat .*";
        String regEx1 = "java\\.lang\\.Thread\\.State:.*";
        Pattern pattern = Pattern.compile(regEx + "|" + regEx1);
        Matcher matcher = pattern.matcher(line);

        if (matcher.find()) {
            return matcher.group();
        } else {
            return "";
        }
    }

    public Set<ThreadDump> getThreadDumps() {
        return threadDumps;
    }

    public void setThreadDumps(Set<ThreadDump> threadDumps) {
        this.threadDumps = threadDumps;
    }
}
