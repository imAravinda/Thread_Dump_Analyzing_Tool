package com.dfn.dump_analyzer_backend;

import com.dfn.dump_analyzer_backend.model.ThreadDump;
import com.dfn.dump_analyzer_backend.model.ThreadDumpAnalyzingResult;
import com.dfn.dump_analyzer_backend.service.ThreadDumpServiceI;
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

@SpringBootTest
public class ThreadDumpServiceTest {
    @Mock
    private ThreadDumpAnalyzingResult threadDumpAnalyzingResult;
    @Mock
    private MultipartFile mockFile;
    @InjectMocks
    private ThreadDumpService threadDumpService;
    private Set<ThreadDump> threadDumps = new HashSet<>();
    private ThreadDump threadDump = new ThreadDump();

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
        threadDump.setState("RUNNABLE");
        threadDump.setThreadId("1");
        threadDumps.add(threadDump);
        threadDumpService.setThreadDumps(threadDumps);
        threadDumpService.getThredsFilterByState("RUNNABLE");
    }

    @Test
    public void getThreadsFilterByStateNotMatchTest(){
        threadDump.setState("RUNNABLE");
        threadDump.setThreadId("1");
        threadDumps.add(threadDump);
        threadDumpService.setThreadDumps(threadDumps);
        threadDumpService.getThredsFilterByState("NO WAITING");
    }

    @Test
    public void getThreadsFilterByPkgMatchTest(){
        threadDump.setPackageDetailsAffectedByThread("jdk.internal.misc.Unsafe.park");
        threadDumps.add(threadDump);
        threadDumpService.setThreadDumps(threadDumps);
        threadDumpService.getThreadsFilterByPackage("Unsafe.park");
    }

    @Test
    public void getThreadsFilterByPkgNotMatchTest(){
        threadDump.setPackageDetailsAffectedByThread("jdk.internal.misc.Unsafe.park");
        threadDumps.add(threadDump);
        threadDumpService.setThreadDumps(threadDumps);
        threadDumpService.getThreadsFilterByPackage("ref");
    }


}
