package com.dfn.dump_analyzer_backend;

import com.dfn.dump_analyzer_backend.model.ThreadDump;
import com.dfn.dump_analyzer_backend.model.ThreadDumpAnalyzingResult;
import com.dfn.dump_analyzer_backend.service.impl.ThreadDumpService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.*;

@SpringBootTest
public class ThreadDumpServiceTest {
    @Mock
    private ThreadDumpAnalyzingResult threadDumpAnalyzingResult;
    @Mock
    private MultipartFile mockFile;
    @InjectMocks
    private ThreadDumpService threadDumpService;
    private Set<ThreadDump> threadDumps = new HashSet<>();

    public void setThreadDetails(){
        ThreadDump threadDump1 = new ThreadDump();
        threadDump1.setState("WAITING");
        threadDump1.setThreadId("1");
        threadDump1.setPool("default-threads - 1");
        threadDump1.setDaemon(true);
        threadDump1.setPackageDetailsAffectedByThread("java.lang.Object.wait");
        threadDump1.setStackTrace("java.lang.Thread.State: WAITING (on object monitor)" +
                "at java.lang.Object.wait(java.base@17.0.8/Native Method) - waiting on <0x000000030e3dc070> (a java.util.TaskQueue)" +
                "at java.lang.Object.wait(java.base@17.0.8/Object.java:338)" +
                "at java.util.TimerThread.mainLoop(java.base@17.0.8/Timer.java:537) - locked <0x000000030e3dc070> (a java.util.TaskQueue)" +
                "at java.util.TimerThread.run(java.base@17.0.8/Timer.java:516)");
        threadDumps.add(threadDump1);

        ThreadDump threadDump2 = new ThreadDump();
        threadDump2.setState("WAITING");
        threadDump2.setThreadId("2");
        threadDump2.setPool("default-threads - 2");
        threadDump2.setDaemon(true);
        threadDump2.setPackageDetailsAffectedByThread("java.lang.Object.wait");
        threadDump2.setStackTrace("java.lang.Thread.State: WAITING (on object monitor)" +
                "at java.lang.Object.wait(java.base@17.0.8/Native Method) - waiting on <0x00000003000e1480> (a java.util.TaskQueue)" +
                "at java.lang.Object.wait(java.base@17.0.8/Object.java:338)" +
                "at java.util.TimerThread.mainLoop(java.base@17.0.8/Timer.java:537) - locked <0x00000003000e1480> (a java.util.TaskQueue)" +
                "at java.util.TimerThread.run(java.base@17.0.8/Timer.java:516)");
        threadDumps.add(threadDump2);

        ThreadDump threadDump = new ThreadDump();
        threadDump.setState("RUNNABLE");
        threadDump.setThreadId("3");
        threadDump.setPool("pool-19-thread-4");
        threadDump.setDaemon(false);
        threadDump.setCpuTime(1050);
        threadDump.setStackTrace("java.lang.Thread.State: RUNNABLE\\tat sun.nio.ch.FileDispatcherImpl.force0(java.base@17.0.8/Native Method)\\tat sun.nio.ch.FileDispatcherImpl.force(java.base@17.0.8/FileDispatcherImpl.java:82)\\tat sun.nio.ch.FileChannelImpl.force(java.base@17.0.8/FileChannelImpl.java:468)\\tat org.apache.activemq.artemis.core.io.nio.NIOSequentialFile.sync(NIOSequentialFile.java:318)\\tat org.apache.activemq.artemis.core.paging.impl.Page.sync(Page.java:484)\\tat org.apache.activemq.artemis.core.paging.impl.PagingStoreImpl.ioSync(PagingStoreImpl.java:327)\\tat org.apache.activemq.artemis.core.paging.impl.PageSyncTimer.tick(PageSyncTimer.java:96)\\tat org.apache.activemq.artemis.core.paging.impl.PageSyncTimer.run(PageSyncTimer.java:81)\\tat org.apache.activemq.artemis.core.server.ActiveMQScheduledComponent.runForExecutor(ActiveMQScheduledComponent.java:313)\\tat org.apache.activemq.artemis.core.server.ActiveMQScheduledComponent.lambda$bookedRunForScheduler$2(ActiveMQScheduledComponent.java:320)\\tat org.apache.activemq.artemis.core.server.ActiveMQScheduledComponent$$Lambda$867/0x00007f55b9a6eae0.run(Unknown Source)\\tat org.apache.activemq.artemis.utils.actors.OrderedExecutor.doTask(OrderedExecutor.java:42)\\tat org.apache.activemq.artemis.utils.actors.OrderedExecutor.doTask(OrderedExecutor.java:31)\\tat org.apache.activemq.artemis.utils.actors.ProcessorBase.executePendingTasks(ProcessorBase.java:65)\\tat org.apache.activemq.artemis.utils.actors.ProcessorBase$$Lambda$786/0x00007f55b990f8a0.run(Unknown Source)\\tat java.util.concurrent.ThreadPoolExecutor.runWorker(java.base@17.0.8/ThreadPoolExecutor.java:1136)\\tat java.util.concurrent.ThreadPoolExecutor$Worker.run(java.base@17.0.8/ThreadPoolExecutor.java:635)\\tat org.apache.activemq.artemis.utils.ActiveMQThreadFactory$1.run(ActiveMQThreadFactory.java:118)");
        threadDumps.add(threadDump);

        ThreadDump threadDump3 = new ThreadDump();
        threadDump3.setState("RUNNABLE");
        threadDump3.setThreadId("4");
        threadDump3.setPool("pool-19-thread-3");
        threadDump3.setDaemon(false);
        threadDump3.setCpuTime(1200);
        threadDump3.setStackTrace("java.lang.Thread.State: RUNNABLE");
        threadDumps.add(threadDump3);
    }
    @Test
    public void ValidFileContentTest() throws IOException {
        // Mocking
        Mockito.when(mockFile.getOriginalFilename()).thenReturn("validFile.tdump");
        Mockito.when(mockFile.getBytes()).thenReturn("valid thread dump content".getBytes());
        threadDumpService.getAnalyzingResult(mockFile);
        Mockito.verify(threadDumpAnalyzingResult).setThreadDumps(Mockito.anySet());
    }

    @Test
    public void InvalidFileTypeTest() {
        Mockito.when(mockFile.getOriginalFilename()).thenReturn("invalidFile.pdf");
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            threadDumpService.getAnalyzingResult(mockFile);
        });
        try {
            Mockito.verify(mockFile, Mockito.never()).getBytes();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Mockito.verify(threadDumpAnalyzingResult, Mockito.never()).setThreadDumps(Mockito.anySet());
    }

    @Test
    public void IOExceptionReadingFileTest() throws IOException {
        Mockito.when(mockFile.getOriginalFilename()).thenReturn("validFile.tdump");
        Mockito.when(mockFile.getBytes()).thenThrow(new IOException("Simulated IOException"));
        Assertions.assertThrows(RuntimeException.class, () -> {
            threadDumpService.getAnalyzingResult(mockFile);
        });
        Mockito.verify(threadDumpAnalyzingResult, Mockito.never()).setThreadDumps(Mockito.anySet());
    }

    @Test
    public void getThreadsFilterByStateMatchTest(){
        setThreadDetails();
        threadDumpService.setThreadDumps(threadDumps);
        threadDumpAnalyzingResult = threadDumpService.getThredsFilterByState("RUNNABLE");

        assertEquals(2,threadDumpAnalyzingResult.getThreadDumps().size());
    }

    @Test
    public void getThreadsFilterByStateNotMatchTest(){
        setThreadDetails();
        threadDumpService.setThreadDumps(threadDumps);
        threadDumpAnalyzingResult = threadDumpService.getThredsFilterByState("NO WAITING");

        assertEquals(0,threadDumpAnalyzingResult.getThreadDumps().size());
    }

    @Test
    public void getThreadsFilterByPkgMatchTest(){
        setThreadDetails();
        threadDumpService.setThreadDumps(threadDumps);
        threadDumpAnalyzingResult = threadDumpService.getThreadsFilterByPackage("Object.wait");

        assertEquals(2,threadDumpAnalyzingResult.getThreadDumps().size());
    }

    @Test
    public void getThreadsFilterByPkgNotMatchTest(){
        setThreadDetails();
        threadDumpService.setThreadDumps(threadDumps);
        threadDumpAnalyzingResult = threadDumpService.getThreadsFilterByPackage("ref");

        assertEquals(0,threadDumpAnalyzingResult.getThreadDumps().size());
    }

    @Test
    public void getSameStackTracesTest(){
        setThreadDetails();
        threadDumpService.setThreadDumps(threadDumps);

        List<ThreadDump.CommonCategories> commonStackTraceList = threadDumpService.getSameStackTraces();
        assertEquals(1, commonStackTraceList.size());

        ThreadDump.CommonCategories commonStackTrace1 = commonStackTraceList.get(0);
        assertEquals("java.lang.Thread.State: WAITING (on object monitor)" +
                        "at java.lang.Object.wait(java.base@17.0.8/Native Method) - waiting on  (a java.util.TaskQueue)" +
                        "at java.lang.Object.wait(java.base@17.0.8/Object.java:338)" +
                        "at java.util.TimerThread.mainLoop(java.base@17.0.8/Timer.java:537) - locked  (a java.util.TaskQueue)" +
                        "at java.util.TimerThread.run(java.base@17.0.8/Timer.java:516)",
                commonStackTrace1.getStackTrace());

        assertEquals("WAITING - 2", commonStackTrace1.getCount());
        assertEquals(2, commonStackTrace1.getRelatedThreadDumps().size());
    }



    @Test
    public void getPoolCategoriesTest(){
        setThreadDetails();
        threadDumpService.setThreadDumps(threadDumps);
        List<ThreadDump.CommonCategories> commonPoolCategories = threadDumpService.getPoolCategories();
        assertEquals(2,commonPoolCategories.size());

        ThreadDump.CommonCategories commonPoolCategory = commonPoolCategories.get(0);

        assertEquals("pool-19-thread-",commonPoolCategory.getPoolName());
        assertEquals("2",commonPoolCategory.getCount());
    }

    @Test
    public void getPoolCategories_EmptyRecordTest(){
        ThreadDump threadDump = new ThreadDump();
        threadDump.setState("WAITING");
        threadDump.setThreadId("2");
        threadDump.setPool("default-threads - 2");
        threadDump.setPackageDetailsAffectedByThread("java.lang.Object.wait");
        threadDump.setStackTrace("java.lang.Thread.State: WAITING (on object monitor)" +
                "at java.lang.Object.wait(java.base@17.0.8/Native Method) - waiting on <0x00000003000e1480> (a java.util.TaskQueue)" +
                "at java.lang.Object.wait(java.base@17.0.8/Object.java:338)" +
                "at java.util.TimerThread.mainLoop(java.base@17.0.8/Timer.java:537) - locked <0x00000003000e1480> (a java.util.TaskQueue)" +
                "at java.util.TimerThread.run(java.base@17.0.8/Timer.java:516)");
        threadDumps.add(threadDump);

        ThreadDump threadDump1 = new ThreadDump();
        threadDump1.setState("RUNNABLE");
        threadDump1.setThreadId("3");
        threadDump1.setPool("pool-19-thread-4");
        threadDump1.setStackTrace("java.lang.Thread.State: RUNNABLE");
        threadDumps.add(threadDump1);
        threadDumpService.setThreadDumps(threadDumps);
        List<ThreadDump.CommonCategories> commonPoolCategories = threadDumpService.getPoolCategories();
        assertEquals(0,commonPoolCategories.size());
    }

    @Test
    public void getThreadsInSamePool_MatchTest(){
        setThreadDetails();
        threadDumpService.setThreadDumps(threadDumps);
        threadDumpAnalyzingResult = threadDumpService.getThreadsInSamePool("default-threads - ");
        assertEquals(2,threadDumpAnalyzingResult.getThreadDumps().size());
    }

    @Test
    public void getThreadsInSamePool_NotMatchTest(){
        setThreadDetails();
        threadDumpService.setThreadDumps(threadDumps);
        threadDumpAnalyzingResult = threadDumpService.getThreadsInSamePool("G1 Refine#");
        assertEquals(0,threadDumpAnalyzingResult.getThreadDumps().size());
    }

    @Test
    public void getDetailsOfDeamonAndNonDeamonThreads_DeamonThreadsTest(){
        setThreadDetails();
        threadDumpService.setThreadDumps(threadDumps);
        threadDumpAnalyzingResult = threadDumpService.getDetailsOfDaemonAndNonDaemonThreads(true);
        assertEquals(2,threadDumpAnalyzingResult.getThreadDumps().size());
        for(ThreadDump threadDump1 : threadDumpAnalyzingResult.getThreadDumps()){
            assertEquals(true,threadDump1.isDaemon());
        }
    }

    @Test
    public void getDetailsOfDeamonAndNonDeamonThreads_NonDeamonThreadsTest(){
        setThreadDetails();
        threadDumpService.setThreadDumps(threadDumps);
        threadDumpAnalyzingResult = threadDumpService.getDetailsOfDaemonAndNonDaemonThreads(false);
        assertEquals(2,threadDumpAnalyzingResult.getThreadDumps().size());
        for(ThreadDump threadDump1 : threadDumpAnalyzingResult.getThreadDumps()){
            assertEquals(false,threadDump1.isDaemon());
        }
    }

    @Test
    public void getDeadlock_NoDeadlockTest(){
        setThreadDetails();
        threadDumpService.setThreadDumps(threadDumps);
        ThreadDumpAnalyzingResult.DeadLockResult isDeadlock = threadDumpService.getDeadLock();

        assertFalse(isDeadlock.isDeadlock());
        assertTrue(isDeadlock.getThreadsInCycle().isEmpty());
    }

    @Test
    public void getDeadlock_DeadLockOccuredTest(){

        ThreadDump threadDump1 = new ThreadDump();
        threadDump1.setState("BLOCKED");
        threadDump1.setThreadId("1");
        threadDump1.setWaitingResourceId("10");
        threadDump1.setLockedResourceId("12");
        threadDumps.add(threadDump1);

        ThreadDump threadDump2 = new ThreadDump();
        threadDump2.setState("BLOCKED");
        threadDump2.setThreadId("2");
        threadDump2.setWaitingResourceId("12");
        threadDump2.setLockedResourceId("10");
        threadDumps.add(threadDump2);

        threadDumpService.setThreadDumps(threadDumps);
        ThreadDumpAnalyzingResult.DeadLockResult isDeadLock = threadDumpService.getDeadLock();

        assertTrue(isDeadLock.isDeadlock());
    }

    @Test
    public void getHighCPUConsumingThreadsTest(){
        setThreadDetails();
        threadDumpService.setThreadDumps(threadDumps);
        threadDumpAnalyzingResult = threadDumpService.getHighCPUConsumingThreads();
        assertEquals(1,threadDumpAnalyzingResult.getThreadDumps().size());
    }

    @Test
    public void getHighCPUConsumingThreads_NoHighCPUThreadsTest(){
        ThreadDump threadDump1 = new ThreadDump();
        threadDump1.setState("WAITING");
        threadDump1.setThreadId("1");
        threadDump1.setStackTrace("java.lang.Thread.State: WAITING (on object monitor)"+
                "at java.lang.Object.wait(java.base@17.0.8/Native Method) - waiting on <0x000000030e3dc070> (a java.util.TaskQueue)" +
                "at java.lang.Object.wait(java.base@17.0.8/Object.java:338)" +
                "at java.util.TimerThread.mainLoop(java.base@17.0.8/Timer.java:537) - locked <0x000000030e3dc070> (a java.util.TaskQueue)" +
                "at java.util.TimerThread.run(java.base@17.0.8/Timer.java:516)");
        threadDumps.add(threadDump1);

        ThreadDump threadDump2 = new ThreadDump();
        threadDump2.setState("RUNNABLE");
        threadDump2.setThreadId("2");
        threadDump2.setStackTrace("java.lang.Thread.State: RUNNABLE");
        threadDumps.add(threadDump2);

        threadDumpService.setThreadDumps(threadDumps);
        threadDumpAnalyzingResult = threadDumpService.getHighCPUConsumingThreads();
        assertEquals(0,threadDumpAnalyzingResult.getThreadDumps().size());
    }

    @Test
    public void getGarbageThreadsTest(){
        ThreadDump threadDump1 = new ThreadDump();
        threadDump1.setState("RUNNABLE");
        threadDump1.setThreadId("1");
        threadDump1.setPool("GC Thread#9");
        threadDumps.add(threadDump1);

        ThreadDump threadDump2 = new ThreadDump();
        threadDump2.setState("RUNNABLE");
        threadDump2.setThreadId("2");
        threadDump2.setPool("GC Thread#3");
        threadDumps.add(threadDump2);

        threadDumpService.setThreadDumps(threadDumps);
        threadDumpAnalyzingResult = threadDumpService.getGarbageThreads();
        assertEquals(2,threadDumpAnalyzingResult.getThreadDumps().size());
    }

    @Test
    public void getGrabageThreads_NoGarbageCollectorsTest(){
        setThreadDetails();
        threadDumpService.setThreadDumps(threadDumps);
        threadDumpAnalyzingResult = threadDumpService.getGarbageThreads();

        assertEquals(0,threadDumpAnalyzingResult.getThreadDumps().size());
    }
}
