package com.br.engine;

import com.sun.jersey.core.header.ContentDisposition;
import com.sun.jersey.multipart.FormDataBodyPart;
import org.codehaus.jettison.json.JSONArray;
import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import javax.ws.rs.core.MediaType;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

public class ServiceFilePrepareWriteToFileServerTest {

    private static final String CONO        = "010";
    private static final String DIVI        = "100";
    private static final String SERVICENAME = "IT";
    private static final String SERVICENO   = "IT001";
    // start of a saved name: cono_divi_service_serviceno (ServiceFile.savedFileName)
    private static final String PREFIX      = CONO + "_" + DIVI + "_" + SERVICENAME + "_" + SERVICENO;
    private static final String USERNAME    = "user01";
    private static final String FILE_DIR    = ServiceFile.FILE_DIR;

    @BeforeClass
    public static void createTestDir() {
        new File(FILE_DIR).mkdirs();
    }

    @After
    public void deleteTestFiles() {
        File[] files = new File(FILE_DIR).listFiles((d, n) -> n.startsWith(SERVICENO + "_") || n.startsWith(PREFIX + "_"));
        if (files != null) for (File f : files) f.delete();
    }

    private FormDataBodyPart mockFilePart(String fileName, String mediaType) {
        FormDataBodyPart part = Mockito.mock(FormDataBodyPart.class);
        ContentDisposition cd = Mockito.mock(ContentDisposition.class);
        MediaType mt = Mockito.mock(MediaType.class);
        when(cd.getFileName()).thenReturn(fileName);
        when(part.getContentDisposition()).thenReturn(cd);
        when(part.getValueAs(InputStream.class)).thenReturn(new ByteArrayInputStream(new byte[]{1, 2, 3}));
        when(part.getMediaType()).thenReturn(mt);
        when(mt.toString()).thenReturn(mediaType);
        return part;
    }

    private FormDataBodyPart mockFieldPart(String fieldName) {
        FormDataBodyPart part = Mockito.mock(FormDataBodyPart.class);
        when(part.getValue()).thenReturn(fieldName);
        return part;
    }

    // -----------------------------------------------------------------------
    // Size mismatch
    // -----------------------------------------------------------------------

    @Test(expected = IllegalArgumentException.class)
    public void testSizeMismatch_throwsIllegalArgument() throws Exception {
        List<FormDataBodyPart> files  = Collections.singletonList(mockFilePart("a.pdf", "application/pdf"));
        List<FormDataBodyPart> fields = Collections.emptyList();

        ServiceFile.prepareWriteToFileServer(CONO, DIVI, SERVICENAME, SERVICENO, files, fields, USERNAME);
    }

    // -----------------------------------------------------------------------
    // File not created on disk → DB insert skipped
    // -----------------------------------------------------------------------

    @Test
    public void testFileNotOnDiskAfterWrite_skipsDbInsert() throws Exception {
        List<FormDataBodyPart> files  = Collections.singletonList(mockFilePart("doc.pdf", "application/pdf"));
        List<FormDataBodyPart> fields = Collections.singletonList(mockFieldPart("attachDoc"));

        try (MockedStatic<ServiceFile> fileUtil = Mockito.mockStatic(ServiceFile.class,
                     withSettings().defaultAnswer(Mockito.CALLS_REAL_METHODS));
             MockedStatic<FlowInsert> insertData = Mockito.mockStatic(FlowInsert.class)) {

            // writeFileServer does nothing → file is never created on disk
            fileUtil.when(() -> ServiceFile.writeFileServer(any(), anyString(), anyString()))
                    .thenReturn("fake_path");

            ServiceFile.prepareWriteToFileServer(CONO, DIVI, SERVICENAME, SERVICENO, files, fields, USERNAME);

            insertData.verify(
                    () -> FlowInsert.addServiceFile(any(), any(), any(), any(), anyInt(), any(), any(), any(), any(), any()),
                    Mockito.never());
        }
    }

    // -----------------------------------------------------------------------
    // File written to disk → DB insert called
    // -----------------------------------------------------------------------

    @Test
    public void testSingleFile_writtenSuccessfully_insertsDbRecord() throws Exception {
        List<FormDataBodyPart> files  = Collections.singletonList(mockFilePart("report.pdf", "application/pdf"));
        List<FormDataBodyPart> fields = Collections.singletonList(mockFieldPart("attachReport"));

        try (MockedStatic<ServiceFile> fileUtil = Mockito.mockStatic(ServiceFile.class,
                     withSettings().defaultAnswer(Mockito.CALLS_REAL_METHODS));
             MockedStatic<FlowInsert> insertData = Mockito.mockStatic(FlowInsert.class)) {

            // writeFileServer creates a real empty file so File.exists() returns true
            fileUtil.when(() -> ServiceFile.writeFileServer(any(), anyString(), anyString()))
                    .thenAnswer(inv -> {
                        String fn = inv.getArgument(1);
                        String fp = inv.getArgument(2);
                        new File(fp + fn).createNewFile();
                        return fp + fn;
                    });
            insertData.when(() -> FlowInsert.addServiceFile(
                    any(), any(), any(), any(), anyInt(), any(), any(), any(), any(), any()))
                    .thenReturn("ok");

            JSONArray result = ServiceFile.prepareWriteToFileServer(
                    CONO, DIVI, SERVICENAME, SERVICENO, files, fields, USERNAME);

            assertEquals(1, result.length());
            assertEquals("report.pdf",   result.getJSONObject(0).getString("originalName"));
            assertEquals("attachReport", result.getJSONObject(0).getString("fieldName"));
            assertTrue(result.getJSONObject(0).getString("savedName").startsWith(PREFIX + "_1_"));

            insertData.verify(() -> FlowInsert.addServiceFile(
                    eq(CONO), eq(DIVI), eq(SERVICENAME), eq(SERVICENO),
                    eq(1), eq("attachReport"), eq("report.pdf"),
                    eq("application/pdf"), anyString(), eq(USERNAME)));
        }
    }

    @Test
    public void testMultipleFiles_allWritten_returnsAllRecords() throws Exception {
        List<FormDataBodyPart> files = Arrays.asList(
                mockFilePart("a.pdf", "application/pdf"),
                mockFilePart("b.png", "image/png"),
                mockFilePart("c.txt", "text/plain"));
        List<FormDataBodyPart> fields = Arrays.asList(
                mockFieldPart("field1"),
                mockFieldPart("field2"),
                mockFieldPart("field3"));

        try (MockedStatic<ServiceFile> fileUtil = Mockito.mockStatic(ServiceFile.class,
                     withSettings().defaultAnswer(Mockito.CALLS_REAL_METHODS));
             MockedStatic<FlowInsert> insertData = Mockito.mockStatic(FlowInsert.class)) {

            fileUtil.when(() -> ServiceFile.writeFileServer(any(), anyString(), anyString()))
                    .thenAnswer(inv -> {
                        String fn = inv.<String>getArgument(1);
                        String fp = inv.<String>getArgument(2);
                        new File(fp + fn).createNewFile();
                        return fp + fn;
                    });
            insertData.when(() -> FlowInsert.addServiceFile(
                    any(), any(), any(), any(), anyInt(), any(), any(), any(), any(), any()))
                    .thenReturn("ok");

            JSONArray result = ServiceFile.prepareWriteToFileServer(
                    CONO, DIVI, SERVICENAME, SERVICENO, files, fields, USERNAME);

            assertEquals(3, result.length());
            insertData.verify(
                    () -> FlowInsert.addServiceFile(any(), any(), any(), any(), anyInt(), any(), any(), any(), any(), any()),
                    Mockito.times(3));
        }
    }

    // -----------------------------------------------------------------------
    // Empty file list
    // -----------------------------------------------------------------------

    @Test
    public void testEmptyFileList_returnsEmptyArray() throws Exception {
        try (MockedStatic<FlowInsert> insertData = Mockito.mockStatic(FlowInsert.class)) {
            JSONArray result = ServiceFile.prepareWriteToFileServer(
                    CONO, DIVI, SERVICENAME, SERVICENO,
                    Collections.emptyList(), Collections.emptyList(), USERNAME);

            assertEquals(0, result.length());
            insertData.verifyNoInteractions();
        }
    }

    // -----------------------------------------------------------------------
    // Saved file name format: {serviceno}_{index}_{originalName}
    // -----------------------------------------------------------------------

    @Test
    public void testSavedFileName_containsServiceNoAndIndex() throws Exception {
        List<FormDataBodyPart> files  = Collections.singletonList(mockFilePart("photo.png", "image/png"));
        List<FormDataBodyPart> fields = Collections.singletonList(mockFieldPart("photo"));

        try (MockedStatic<ServiceFile> fileUtil = Mockito.mockStatic(ServiceFile.class,
                     withSettings().defaultAnswer(Mockito.CALLS_REAL_METHODS));
             MockedStatic<FlowInsert> insertData = Mockito.mockStatic(FlowInsert.class)) {

            fileUtil.when(() -> ServiceFile.writeFileServer(any(), anyString(), anyString()))
                    .thenAnswer(inv -> {
                        String fn = inv.<String>getArgument(1);
                        String fp = inv.<String>getArgument(2);
                        new File(fp + fn).createNewFile();
                        return fp + fn;
                    });
            insertData.when(() -> FlowInsert.addServiceFile(
                    any(), any(), any(), any(), anyInt(), any(), any(), any(), any(), any()))
                    .thenReturn("ok");

            JSONArray result = ServiceFile.prepareWriteToFileServer(
                    CONO, DIVI, SERVICENAME, SERVICENO, files, fields, USERNAME);

            String savedName = result.getJSONObject(0).getString("savedName");
            assertTrue(savedName.startsWith(PREFIX + "_1_"));
            assertTrue(savedName.endsWith("photo.png"));
        }
    }

    // -----------------------------------------------------------------------
    // Exception propagation
    // -----------------------------------------------------------------------

    @Test(expected = IOException.class)
    public void testWriteFileServer_throwsIOException_propagates() throws Exception {
        List<FormDataBodyPart> files  = Collections.singletonList(mockFilePart("doc.pdf", "application/pdf"));
        List<FormDataBodyPart> fields = Collections.singletonList(mockFieldPart("attachDoc"));

        try (MockedStatic<ServiceFile> fileUtil = Mockito.mockStatic(ServiceFile.class,
                     withSettings().defaultAnswer(Mockito.CALLS_REAL_METHODS))) {

            fileUtil.when(() -> ServiceFile.writeFileServer(any(), anyString(), anyString()))
                    .thenThrow(new IOException("disk full"));

            ServiceFile.prepareWriteToFileServer(CONO, DIVI, SERVICENAME, SERVICENO, files, fields, USERNAME);
        }
    }

    @Test(expected = Exception.class)
    public void testAddServiceFile_throwsException_propagates() throws Exception {
        List<FormDataBodyPart> files  = Collections.singletonList(mockFilePart("doc.pdf", "application/pdf"));
        List<FormDataBodyPart> fields = Collections.singletonList(mockFieldPart("attachDoc"));

        try (MockedStatic<ServiceFile> fileUtil = Mockito.mockStatic(ServiceFile.class,
                     withSettings().defaultAnswer(Mockito.CALLS_REAL_METHODS));
             MockedStatic<FlowInsert> insertData = Mockito.mockStatic(FlowInsert.class)) {

            fileUtil.when(() -> ServiceFile.writeFileServer(any(), anyString(), anyString()))
                    .thenAnswer(inv -> {
                        String fn = inv.<String>getArgument(1);
                        String fp = inv.<String>getArgument(2);
                        new File(fp + fn).createNewFile();
                        return fp + fn;
                    });
            insertData.when(() -> FlowInsert.addServiceFile(
                    any(), any(), any(), any(), anyInt(), any(), any(), any(), any(), any()))
                    .thenThrow(new Exception("DB insert failed"));

            ServiceFile.prepareWriteToFileServer(CONO, DIVI, SERVICENAME, SERVICENO, files, fields, USERNAME);
        }
    }

    // -----------------------------------------------------------------------
    // Verify correct index per file and exact DB arguments
    // -----------------------------------------------------------------------

    @Test
    public void testMultipleFiles_verifyCorrectIndexPerFile() throws Exception {
        List<FormDataBodyPart> files = Arrays.asList(
                mockFilePart("a.pdf", "application/pdf"),
                mockFilePart("b.pdf", "application/pdf"));
        List<FormDataBodyPart> fields = Arrays.asList(
                mockFieldPart("field1"),
                mockFieldPart("field2"));

        try (MockedStatic<ServiceFile> fileUtil = Mockito.mockStatic(ServiceFile.class,
                     withSettings().defaultAnswer(Mockito.CALLS_REAL_METHODS));
             MockedStatic<FlowInsert> insertData = Mockito.mockStatic(FlowInsert.class)) {

            fileUtil.when(() -> ServiceFile.writeFileServer(any(), anyString(), anyString()))
                    .thenAnswer(inv -> {
                        String fn = inv.<String>getArgument(1);
                        String fp = inv.<String>getArgument(2);
                        new File(fp + fn).createNewFile();
                        return fp + fn;
                    });
            insertData.when(() -> FlowInsert.addServiceFile(
                    any(), any(), any(), any(), anyInt(), any(), any(), any(), any(), any()))
                    .thenReturn("ok");

            ServiceFile.prepareWriteToFileServer(CONO, DIVI, SERVICENAME, SERVICENO, files, fields, USERNAME);

            // file index is 1-based: first file → 1, second → 2
            insertData.verify(() -> FlowInsert.addServiceFile(
                    eq(CONO), eq(DIVI), eq(SERVICENAME), eq(SERVICENO),
                    eq(1), eq("field1"), eq("a.pdf"), eq("application/pdf"),
                    eq(PREFIX + "_1_a.pdf"), eq(USERNAME)));
            insertData.verify(() -> FlowInsert.addServiceFile(
                    eq(CONO), eq(DIVI), eq(SERVICENAME), eq(SERVICENO),
                    eq(2), eq("field2"), eq("b.pdf"), eq("application/pdf"),
                    eq(PREFIX + "_2_b.pdf"), eq(USERNAME)));
        }
    }

    // -----------------------------------------------------------------------
    // Verify writeFileServer receives the correct assembled file name
    // -----------------------------------------------------------------------

    @Test
    public void testWriteFileServer_calledWithCorrectFileName() throws Exception {
        List<FormDataBodyPart> files  = Collections.singletonList(mockFilePart("invoice.pdf", "application/pdf"));
        List<FormDataBodyPart> fields = Collections.singletonList(mockFieldPart("invoice"));

        try (MockedStatic<ServiceFile> fileUtil = Mockito.mockStatic(ServiceFile.class,
                     withSettings().defaultAnswer(Mockito.CALLS_REAL_METHODS));
             MockedStatic<FlowInsert> insertData = Mockito.mockStatic(FlowInsert.class)) {

            fileUtil.when(() -> ServiceFile.writeFileServer(any(), anyString(), anyString()))
                    .thenAnswer(inv -> {
                        String fn = inv.<String>getArgument(1);
                        String fp = inv.<String>getArgument(2);
                        new File(fp + fn).createNewFile();
                        return fp + fn;
                    });
            insertData.when(() -> FlowInsert.addServiceFile(
                    any(), any(), any(), any(), anyInt(), any(), any(), any(), any(), any()))
                    .thenReturn("ok");

            ServiceFile.prepareWriteToFileServer(CONO, DIVI, SERVICENAME, SERVICENO, files, fields, USERNAME);

            fileUtil.verify(() -> ServiceFile.writeFileServer(
                    any(InputStream.class),
                    eq(PREFIX + "_1_invoice.pdf"),
                    eq(FILE_DIR)));
        }
    }

    // -----------------------------------------------------------------------
    // Filename normalization: Windows full path stripped to basename
    // -----------------------------------------------------------------------

    @Test
    public void testWindowsPathInFileName_stripped() throws Exception {
        List<FormDataBodyPart> files  = Collections.singletonList(
                mockFilePart("C:\\Users\\someone\\invoice.pdf", "application/pdf"));
        List<FormDataBodyPart> fields = Collections.singletonList(mockFieldPart("invoice"));

        try (MockedStatic<ServiceFile> fileUtil = Mockito.mockStatic(ServiceFile.class,
                     withSettings().defaultAnswer(Mockito.CALLS_REAL_METHODS));
             MockedStatic<FlowInsert> insertData = Mockito.mockStatic(FlowInsert.class)) {

            fileUtil.when(() -> ServiceFile.writeFileServer(any(), anyString(), anyString()))
                    .thenAnswer(inv -> {
                        String fn = inv.<String>getArgument(1);
                        String fp = inv.<String>getArgument(2);
                        new File(fp + fn).createNewFile();
                        return fp + fn;
                    });
            insertData.when(() -> FlowInsert.addServiceFile(
                    any(), any(), any(), any(), anyInt(), any(), any(), any(), any(), any()))
                    .thenReturn("ok");

            JSONArray result = ServiceFile.prepareWriteToFileServer(
                    CONO, DIVI, SERVICENAME, SERVICENO, files, fields, USERNAME);

            assertEquals("invoice.pdf", result.getJSONObject(0).getString("originalName"));
            assertTrue(result.getJSONObject(0).getString("savedName").endsWith("invoice.pdf"));
        }
    }
}
