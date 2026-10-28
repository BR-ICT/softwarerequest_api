package com.br.engine;

import com.sun.jersey.core.header.ContentDisposition;
import com.sun.jersey.multipart.FormDataBodyPart;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONObject;
import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import javax.ws.rs.core.MediaType;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

public class ServiceFilePrepareUpdateToFileServerTest {

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

    /** Builds a getServiceFile JSON array with the given rows (FILINE, FISNAM, FIFNAM, FITYPE, FIREM1). */
    private String dbJson(Object[]... rows) {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < rows.length; i++) {
            Object[] r = rows[i];
            if (i > 0) sb.append(",");
            sb.append("{\"FILINE\":").append(r[0])
              .append(",\"FISNAM\":\"").append(r[1]).append("\"")
              .append(",\"FIFNAM\":\"").append(r[2]).append("\"")
              .append(",\"FITYPE\":\"").append(r[3]).append("\"")
              .append(",\"FIREM1\":\"").append(r[4]).append("\"}");
        }
        return sb.append("]").toString();
    }

    /** Stubs writeFileServer to create an actual empty file so File.exists() returns true. */
    private void stubWriteCreatesFile(MockedStatic<ServiceFile> fileUtil) {
        fileUtil.when(() -> ServiceFile.writeFileServer(any(), anyString(), anyString()))
                .thenAnswer(inv -> {
                    String fn = inv.<String>getArgument(1);
                    String fp = inv.<String>getArgument(2);
                    new File(fp + fn).createNewFile();
                    return fp + fn;
                });
    }

    /** Stubs writeFileServer to do nothing (file is NOT created on disk). */
    private void stubWriteNoOp(MockedStatic<ServiceFile> fileUtil) {
        fileUtil.when(() -> ServiceFile.writeFileServer(any(), anyString(), anyString()))
                .thenReturn("fake_path");
    }

    // -----------------------------------------------------------------------
    // Size mismatch
    // -----------------------------------------------------------------------

    @Test(expected = IllegalArgumentException.class)
    public void testSizeMismatch_throwsIllegalArgument() throws Exception {
        List<FormDataBodyPart> files  = Collections.singletonList(mockFilePart("a.pdf", "application/pdf"));
        List<FormDataBodyPart> fields = Collections.emptyList();

        try (MockedStatic<FlowQuery> selectData = Mockito.mockStatic(FlowQuery.class)) {
            selectData.when(() -> FlowQuery.getServiceFile(any(), any(), any(), any())).thenReturn("[]");
            ServiceFile.prepareUpdateToFileServer(CONO, DIVI, SERVICENAME, SERVICENO, files, fields, USERNAME);
        }
    }

    // -----------------------------------------------------------------------
    // Skip: same field, same file name and type
    // -----------------------------------------------------------------------

    @Test
    public void testExistingField_sameFile_skip() throws Exception {
        List<FormDataBodyPart> files  = Collections.singletonList(mockFilePart("doc.pdf", "application/pdf"));
        List<FormDataBodyPart> fields = Collections.singletonList(mockFieldPart("attach"));

        String existing = dbJson(new Object[]{1, "attach", "doc.pdf", "application/pdf", "IT001_1_doc.pdf"});

        try (MockedStatic<FlowQuery> selectData = Mockito.mockStatic(FlowQuery.class);
             MockedStatic<ServiceFile> fileUtil = Mockito.mockStatic(ServiceFile.class,
                     withSettings().defaultAnswer(Mockito.CALLS_REAL_METHODS));
             MockedStatic<FlowUpdate> updateData = Mockito.mockStatic(FlowUpdate.class)) {

            selectData.when(() -> FlowQuery.getServiceFile(CONO, DIVI, SERVICENAME, SERVICENO))
                    .thenReturn(existing);

            JSONArray result = ServiceFile.prepareUpdateToFileServer(
                    CONO, DIVI, SERVICENAME, SERVICENO, files, fields, USERNAME);

            assertEquals(1, result.length());
            assertEquals("skip", result.getJSONObject(0).getString("action"));
            fileUtil.verify(() -> ServiceFile.writeFileServer(any(), anyString(), anyString()), Mockito.never());
            updateData.verify(
                    () -> FlowUpdate.updateServiceFile(any(), any(), any(), any(), any(), any(), any(), any(), any()),
                    Mockito.never());
        }
    }

    // -----------------------------------------------------------------------
    // Update: same field, different file name
    // -----------------------------------------------------------------------

    @Test
    public void testExistingField_differentFileName_update() throws Exception {
        List<FormDataBodyPart> files  = Collections.singletonList(mockFilePart("new.pdf", "application/pdf"));
        List<FormDataBodyPart> fields = Collections.singletonList(mockFieldPart("attach"));

        String existing = dbJson(new Object[]{1, "attach", "old.pdf", "application/pdf", "IT001_1_old.pdf"});

        try (MockedStatic<FlowQuery> selectData = Mockito.mockStatic(FlowQuery.class);
             MockedStatic<ServiceFile> fileUtil = Mockito.mockStatic(ServiceFile.class,
                     withSettings().defaultAnswer(Mockito.CALLS_REAL_METHODS));
             MockedStatic<FlowUpdate> updateData = Mockito.mockStatic(FlowUpdate.class)) {

            selectData.when(() -> FlowQuery.getServiceFile(CONO, DIVI, SERVICENAME, SERVICENO))
                    .thenReturn(existing);
            stubWriteCreatesFile(fileUtil);
            fileUtil.when(() -> ServiceFile.deleteFileServer(anyString())).thenAnswer(inv -> null);
            updateData.when(() -> FlowUpdate.updateServiceFile(any(), any(), any(), any(), any(), any(), any(), any(), any()))
                    .thenReturn("ok");

            JSONArray result = ServiceFile.prepareUpdateToFileServer(
                    CONO, DIVI, SERVICENAME, SERVICENO, files, fields, USERNAME);

            assertEquals(1, result.length());
            assertEquals("update", result.getJSONObject(0).getString("action"));
            fileUtil.verify(() -> ServiceFile.deleteFileServer(contains("IT001_1_old.pdf")));
            updateData.verify(() -> FlowUpdate.updateServiceFile(
                    eq(CONO), eq(DIVI), eq(SERVICENAME), eq(SERVICENO),
                    eq("attach"), eq("new.pdf"), eq("application/pdf"), anyString(), eq(USERNAME)));
        }
    }

    // -----------------------------------------------------------------------
    // Insert: new field name not in DB
    // -----------------------------------------------------------------------

    @Test
    public void testNewField_insert() throws Exception {
        List<FormDataBodyPart> files  = Collections.singletonList(mockFilePart("extra.pdf", "application/pdf"));
        List<FormDataBodyPart> fields = Collections.singletonList(mockFieldPart("newField"));

        // DB has a different field; maxLine = 1 → new insert uses line 2
        String existing = dbJson(new Object[]{1, "other", "other.pdf", "application/pdf", "IT001_1_other.pdf"});

        try (MockedStatic<FlowQuery> selectData = Mockito.mockStatic(FlowQuery.class);
             MockedStatic<ServiceFile> fileUtil = Mockito.mockStatic(ServiceFile.class,
                     withSettings().defaultAnswer(Mockito.CALLS_REAL_METHODS));
             MockedStatic<FlowInsert> insertData = Mockito.mockStatic(FlowInsert.class);
             MockedStatic<FlowDelete> deleteData = Mockito.mockStatic(FlowDelete.class)) {

            selectData.when(() -> FlowQuery.getServiceFile(CONO, DIVI, SERVICENAME, SERVICENO))
                    .thenReturn(existing);
            stubWriteCreatesFile(fileUtil);
            fileUtil.when(() -> ServiceFile.deleteFileServer(anyString())).thenAnswer(inv -> null);
            insertData.when(() -> FlowInsert.addServiceFile(
                    any(), any(), any(), any(), anyInt(), any(), any(), any(), any(), any()))
                    .thenReturn("ok");

            JSONArray result = ServiceFile.prepareUpdateToFileServer(
                    CONO, DIVI, SERVICENAME, SERVICENO, files, fields, USERNAME);

            // Result contains: 1 insert (newField) + 1 delete (orphan "other")
            JSONObject insertEntry = null;
            for (int i = 0; i < result.length(); i++) {
                if ("insert".equals(result.getJSONObject(i).getString("action"))) {
                    insertEntry = result.getJSONObject(i);
                }
            }
            assertNotNull("Expected an insert action", insertEntry);
            assertEquals("newField", insertEntry.getString("fieldName"));

            insertData.verify(() -> FlowInsert.addServiceFile(
                    eq(CONO), eq(DIVI), eq(SERVICENAME), eq(SERVICENO),
                    eq(2), eq("newField"), eq("extra.pdf"), eq("application/pdf"),
                    anyString(), eq(USERNAME)));
        }
    }

    // -----------------------------------------------------------------------
    // Delete: orphaned DB record absent from incoming submission
    // -----------------------------------------------------------------------

    @Test
    public void testOrphanedField_deletesFileAndDbRecord() throws Exception {
        String existing = dbJson(new Object[]{1, "orphan", "old.pdf", "application/pdf", "IT001_1_old.pdf"});

        try (MockedStatic<FlowQuery> selectData = Mockito.mockStatic(FlowQuery.class);
             MockedStatic<ServiceFile> fileUtil = Mockito.mockStatic(ServiceFile.class,
                     withSettings().defaultAnswer(Mockito.CALLS_REAL_METHODS));
             MockedStatic<FlowDelete> deleteData = Mockito.mockStatic(FlowDelete.class)) {

            selectData.when(() -> FlowQuery.getServiceFile(CONO, DIVI, SERVICENAME, SERVICENO))
                    .thenReturn(existing);
            fileUtil.when(() -> ServiceFile.deleteFileServer(anyString())).thenAnswer(inv -> null);

            JSONArray result = ServiceFile.prepareUpdateToFileServer(
                    CONO, DIVI, SERVICENAME, SERVICENO,
                    Collections.emptyList(), Collections.emptyList(), USERNAME);

            assertEquals(1, result.length());
            assertEquals("delete", result.getJSONObject(0).getString("action"));
            assertEquals("orphan",  result.getJSONObject(0).getString("fieldName"));
            fileUtil.verify(() -> ServiceFile.deleteFileServer(contains("IT001_1_old.pdf")));
            deleteData.verify(() -> FlowDelete.deleteServiceFile(
                    eq(CONO), eq(DIVI), eq(SERVICENAME), eq(SERVICENO), eq("orphan")));
        }
    }

    // -----------------------------------------------------------------------
    // No existing records + no incoming files → empty result
    // -----------------------------------------------------------------------

    @Test
    public void testNoExistingAndNoIncoming_returnsEmptyArray() throws Exception {
        try (MockedStatic<FlowQuery> selectData = Mockito.mockStatic(FlowQuery.class)) {
            selectData.when(() -> FlowQuery.getServiceFile(CONO, DIVI, SERVICENAME, SERVICENO))
                    .thenReturn(null);

            JSONArray result = ServiceFile.prepareUpdateToFileServer(
                    CONO, DIVI, SERVICENAME, SERVICENO,
                    Collections.emptyList(), Collections.emptyList(), USERNAME);

            assertEquals(0, result.length());
        }
    }

    // -----------------------------------------------------------------------
    // Mixed actions: skip + update + delete in one call
    // -----------------------------------------------------------------------

    @Test
    public void testMixedActions_skipUpdateDelete() throws Exception {
        // DB: field1 (unchanged), field2 (renamed), field3 (orphan)
        String existing = dbJson(
                new Object[]{1, "field1", "a.pdf", "application/pdf", "IT001_1_a.pdf"},
                new Object[]{2, "field2", "b.pdf", "application/pdf", "IT001_2_b.pdf"},
                new Object[]{3, "field3", "c.pdf", "application/pdf", "IT001_3_c.pdf"});

        // Incoming: field1 same (skip), field2 renamed (update); field3 absent (delete)
        List<FormDataBodyPart> files = Arrays.asList(
                mockFilePart("a.pdf",    "application/pdf"),
                mockFilePart("b_new.pdf", "application/pdf"));
        List<FormDataBodyPart> fields = Arrays.asList(
                mockFieldPart("field1"),
                mockFieldPart("field2"));

        try (MockedStatic<FlowQuery> selectData = Mockito.mockStatic(FlowQuery.class);
             MockedStatic<ServiceFile> fileUtil = Mockito.mockStatic(ServiceFile.class,
                     withSettings().defaultAnswer(Mockito.CALLS_REAL_METHODS));
             MockedStatic<FlowUpdate> updateData = Mockito.mockStatic(FlowUpdate.class);
             MockedStatic<FlowDelete> deleteData = Mockito.mockStatic(FlowDelete.class)) {

            selectData.when(() -> FlowQuery.getServiceFile(CONO, DIVI, SERVICENAME, SERVICENO))
                    .thenReturn(existing);
            stubWriteCreatesFile(fileUtil);
            fileUtil.when(() -> ServiceFile.deleteFileServer(anyString())).thenAnswer(inv -> null);
            updateData.when(() -> FlowUpdate.updateServiceFile(any(), any(), any(), any(), any(), any(), any(), any(), any()))
                    .thenReturn("ok");

            JSONArray result = ServiceFile.prepareUpdateToFileServer(
                    CONO, DIVI, SERVICENAME, SERVICENO, files, fields, USERNAME);

            int skipCount = 0, updateCount = 0, deleteCount = 0;
            for (int i = 0; i < result.length(); i++) {
                String action = result.getJSONObject(i).getString("action");
                if ("skip".equals(action))   skipCount++;
                if ("update".equals(action)) updateCount++;
                if ("delete".equals(action)) deleteCount++;
            }
            assertEquals(1, skipCount);
            assertEquals(1, updateCount);
            assertEquals(1, deleteCount);
        }
    }

    // -----------------------------------------------------------------------
    // Exception propagation
    // -----------------------------------------------------------------------

    @Test(expected = Exception.class)
    public void testException_fromUpdateServiceFile_propagates() throws Exception {
        List<FormDataBodyPart> files  = Collections.singletonList(mockFilePart("new.pdf", "application/pdf"));
        List<FormDataBodyPart> fields = Collections.singletonList(mockFieldPart("attach"));

        String existing = dbJson(new Object[]{1, "attach", "old.pdf", "application/pdf", "IT001_1_old.pdf"});

        try (MockedStatic<FlowQuery> selectData = Mockito.mockStatic(FlowQuery.class);
             MockedStatic<ServiceFile> fileUtil = Mockito.mockStatic(ServiceFile.class,
                     withSettings().defaultAnswer(Mockito.CALLS_REAL_METHODS));
             MockedStatic<FlowUpdate> updateData = Mockito.mockStatic(FlowUpdate.class)) {

            selectData.when(() -> FlowQuery.getServiceFile(CONO, DIVI, SERVICENAME, SERVICENO))
                    .thenReturn(existing);
            stubWriteCreatesFile(fileUtil);
            fileUtil.when(() -> ServiceFile.deleteFileServer(anyString())).thenAnswer(inv -> null);
            updateData.when(() -> FlowUpdate.updateServiceFile(any(), any(), any(), any(), any(), any(), any(), any(), any()))
                    .thenThrow(new RuntimeException("DB error"));

            ServiceFile.prepareUpdateToFileServer(
                    CONO, DIVI, SERVICENAME, SERVICENO, files, fields, USERNAME);
        }
    }

    // -----------------------------------------------------------------------
    // Update: same filename, different file type → still triggers update
    // -----------------------------------------------------------------------

    @Test
    public void testExistingField_fileTypeChanged_update() throws Exception {
        // doc.pdf name unchanged but media type changed → fileTypeChanged=true → update
        List<FormDataBodyPart> files  = Collections.singletonList(mockFilePart("doc.pdf", "image/png"));
        List<FormDataBodyPart> fields = Collections.singletonList(mockFieldPart("attach"));

        String existing = dbJson(new Object[]{1, "attach", "doc.pdf", "application/pdf", "IT001_1_doc.pdf"});

        try (MockedStatic<FlowQuery> selectData = Mockito.mockStatic(FlowQuery.class);
             MockedStatic<ServiceFile> fileUtil = Mockito.mockStatic(ServiceFile.class,
                     withSettings().defaultAnswer(Mockito.CALLS_REAL_METHODS));
             MockedStatic<FlowUpdate> updateData = Mockito.mockStatic(FlowUpdate.class)) {

            selectData.when(() -> FlowQuery.getServiceFile(CONO, DIVI, SERVICENAME, SERVICENO))
                    .thenReturn(existing);
            stubWriteCreatesFile(fileUtil);
            fileUtil.when(() -> ServiceFile.deleteFileServer(anyString())).thenAnswer(inv -> null);
            updateData.when(() -> FlowUpdate.updateServiceFile(any(), any(), any(), any(), any(), any(), any(), any(), any()))
                    .thenReturn("ok");

            JSONArray result = ServiceFile.prepareUpdateToFileServer(
                    CONO, DIVI, SERVICENAME, SERVICENO, files, fields, USERNAME);

            assertEquals(1, result.length());
            assertEquals("update", result.getJSONObject(0).getString("action"));
            updateData.verify(() -> FlowUpdate.updateServiceFile(
                    eq(CONO), eq(DIVI), eq(SERVICENAME), eq(SERVICENO),
                    eq("attach"), eq("doc.pdf"), eq("image/png"), anyString(), eq(USERNAME)));
        }
    }

    // -----------------------------------------------------------------------
    // File not written to disk: update path → DB update skipped
    // -----------------------------------------------------------------------

    @Test
    public void testFileNotOnDiskAfterWrite_update_skipsDbUpdate() throws Exception {
        List<FormDataBodyPart> files  = Collections.singletonList(mockFilePart("new.pdf", "application/pdf"));
        List<FormDataBodyPart> fields = Collections.singletonList(mockFieldPart("attach"));

        String existing = dbJson(new Object[]{1, "attach", "old.pdf", "application/pdf", "IT001_1_old.pdf"});

        try (MockedStatic<FlowQuery> selectData = Mockito.mockStatic(FlowQuery.class);
             MockedStatic<ServiceFile> fileUtil = Mockito.mockStatic(ServiceFile.class,
                     withSettings().defaultAnswer(Mockito.CALLS_REAL_METHODS));
             MockedStatic<FlowUpdate> updateData = Mockito.mockStatic(FlowUpdate.class)) {

            selectData.when(() -> FlowQuery.getServiceFile(CONO, DIVI, SERVICENAME, SERVICENO))
                    .thenReturn(existing);
            stubWriteNoOp(fileUtil); // writeFileServer creates no file on disk
            fileUtil.when(() -> ServiceFile.deleteFileServer(anyString())).thenAnswer(inv -> null);

            JSONArray result = ServiceFile.prepareUpdateToFileServer(
                    CONO, DIVI, SERVICENAME, SERVICENO, files, fields, USERNAME);

            assertEquals(0, result.length());
            updateData.verify(
                    () -> FlowUpdate.updateServiceFile(any(), any(), any(), any(), any(), any(), any(), any(), any()),
                    Mockito.never());
        }
    }

    // -----------------------------------------------------------------------
    // File not written to disk: insert path → DB insert skipped
    // -----------------------------------------------------------------------

    @Test
    public void testFileNotOnDiskAfterWrite_insert_skipsDbInsert() throws Exception {
        List<FormDataBodyPart> files  = Collections.singletonList(mockFilePart("new.pdf", "application/pdf"));
        List<FormDataBodyPart> fields = Collections.singletonList(mockFieldPart("newField"));

        try (MockedStatic<FlowQuery> selectData = Mockito.mockStatic(FlowQuery.class);
             MockedStatic<ServiceFile> fileUtil = Mockito.mockStatic(ServiceFile.class,
                     withSettings().defaultAnswer(Mockito.CALLS_REAL_METHODS));
             MockedStatic<FlowInsert> insertData = Mockito.mockStatic(FlowInsert.class)) {

            selectData.when(() -> FlowQuery.getServiceFile(CONO, DIVI, SERVICENAME, SERVICENO))
                    .thenReturn(null);
            stubWriteNoOp(fileUtil);

            JSONArray result = ServiceFile.prepareUpdateToFileServer(
                    CONO, DIVI, SERVICENAME, SERVICENO, files, fields, USERNAME);

            assertEquals(0, result.length());
            insertData.verify(
                    () -> FlowInsert.addServiceFile(any(), any(), any(), any(), anyInt(), any(), any(), any(), any(), any()),
                    Mockito.never());
        }
    }

    // -----------------------------------------------------------------------
    // No existing DB records: first incoming file inserted at line 1
    // -----------------------------------------------------------------------

    @Test
    public void testNoExistingDb_newFile_insertsWithLineOne() throws Exception {
        List<FormDataBodyPart> files  = Collections.singletonList(mockFilePart("first.pdf", "application/pdf"));
        List<FormDataBodyPart> fields = Collections.singletonList(mockFieldPart("attach"));

        try (MockedStatic<FlowQuery> selectData = Mockito.mockStatic(FlowQuery.class);
             MockedStatic<ServiceFile> fileUtil = Mockito.mockStatic(ServiceFile.class,
                     withSettings().defaultAnswer(Mockito.CALLS_REAL_METHODS));
             MockedStatic<FlowInsert> insertData = Mockito.mockStatic(FlowInsert.class)) {

            selectData.when(() -> FlowQuery.getServiceFile(CONO, DIVI, SERVICENAME, SERVICENO))
                    .thenReturn(null); // no existing records → maxLine = 0
            stubWriteCreatesFile(fileUtil);
            insertData.when(() -> FlowInsert.addServiceFile(
                    any(), any(), any(), any(), anyInt(), any(), any(), any(), any(), any()))
                    .thenReturn("ok");

            JSONArray result = ServiceFile.prepareUpdateToFileServer(
                    CONO, DIVI, SERVICENAME, SERVICENO, files, fields, USERNAME);

            assertEquals(1, result.length());
            assertEquals("insert", result.getJSONObject(0).getString("action"));
            insertData.verify(() -> FlowInsert.addServiceFile(
                    eq(CONO), eq(DIVI), eq(SERVICENAME), eq(SERVICENO),
                    eq(1), eq("attach"), eq("first.pdf"), eq("application/pdf"),
                    eq(PREFIX + "_1_first.pdf"), eq(USERNAME)));
        }
    }

    // -----------------------------------------------------------------------
    // Multiple new fields: maxLine increments correctly (2, 3)
    // -----------------------------------------------------------------------

    @Test
    public void testMultipleNewFields_maxLineIncrements() throws Exception {
        // DB has field1 at line 1 → maxLine=1; two new fields get lines 2 and 3
        String existing = dbJson(new Object[]{1, "field1", "a.pdf", "application/pdf", "IT001_1_a.pdf"});

        List<FormDataBodyPart> files = Arrays.asList(
                mockFilePart("a.pdf", "application/pdf"),   // field1 → skip (unchanged)
                mockFilePart("b.pdf", "application/pdf"),   // newField1 → insert at line 2
                mockFilePart("c.pdf", "application/pdf"));  // newField2 → insert at line 3
        List<FormDataBodyPart> fields = Arrays.asList(
                mockFieldPart("field1"),
                mockFieldPart("newField1"),
                mockFieldPart("newField2"));

        try (MockedStatic<FlowQuery> selectData = Mockito.mockStatic(FlowQuery.class);
             MockedStatic<ServiceFile> fileUtil = Mockito.mockStatic(ServiceFile.class,
                     withSettings().defaultAnswer(Mockito.CALLS_REAL_METHODS));
             MockedStatic<FlowInsert> insertData = Mockito.mockStatic(FlowInsert.class)) {

            selectData.when(() -> FlowQuery.getServiceFile(CONO, DIVI, SERVICENAME, SERVICENO))
                    .thenReturn(existing);
            stubWriteCreatesFile(fileUtil);
            insertData.when(() -> FlowInsert.addServiceFile(
                    any(), any(), any(), any(), anyInt(), any(), any(), any(), any(), any()))
                    .thenReturn("ok");

            ServiceFile.prepareUpdateToFileServer(
                    CONO, DIVI, SERVICENAME, SERVICENO, files, fields, USERNAME);

            insertData.verify(() -> FlowInsert.addServiceFile(
                    eq(CONO), eq(DIVI), eq(SERVICENAME), eq(SERVICENO),
                    eq(2), eq("newField1"), anyString(), anyString(), anyString(), eq(USERNAME)));
            insertData.verify(() -> FlowInsert.addServiceFile(
                    eq(CONO), eq(DIVI), eq(SERVICENAME), eq(SERVICENO),
                    eq(3), eq("newField2"), anyString(), anyString(), anyString(), eq(USERNAME)));
        }
    }

    // -----------------------------------------------------------------------
    // Exception from FlowInsert.addServiceFile propagates
    // -----------------------------------------------------------------------

    @Test(expected = Exception.class)
    public void testException_fromInsertData_propagates() throws Exception {
        List<FormDataBodyPart> files  = Collections.singletonList(mockFilePart("new.pdf", "application/pdf"));
        List<FormDataBodyPart> fields = Collections.singletonList(mockFieldPart("newField"));

        try (MockedStatic<FlowQuery> selectData = Mockito.mockStatic(FlowQuery.class);
             MockedStatic<ServiceFile> fileUtil = Mockito.mockStatic(ServiceFile.class,
                     withSettings().defaultAnswer(Mockito.CALLS_REAL_METHODS));
             MockedStatic<FlowInsert> insertData = Mockito.mockStatic(FlowInsert.class)) {

            selectData.when(() -> FlowQuery.getServiceFile(CONO, DIVI, SERVICENAME, SERVICENO))
                    .thenReturn(null);
            stubWriteCreatesFile(fileUtil);
            insertData.when(() -> FlowInsert.addServiceFile(
                    any(), any(), any(), any(), anyInt(), any(), any(), any(), any(), any()))
                    .thenThrow(new RuntimeException("DB insert failed"));

            ServiceFile.prepareUpdateToFileServer(
                    CONO, DIVI, SERVICENAME, SERVICENO, files, fields, USERNAME);
        }
    }

    // -----------------------------------------------------------------------
    // Orphan with empty FIREM1: deleteFileServer NOT called, deleteServiceFile IS called
    // -----------------------------------------------------------------------

    @Test
    public void testOrphanWithEmptySavedName_skipsDeleteFile() throws Exception {
        // FIREM1="" → orphanSavedName is empty → deleteFileServer must NOT be called
        String existing = dbJson(new Object[]{1, "orphan", "old.pdf", "application/pdf", ""});

        try (MockedStatic<FlowQuery> selectData = Mockito.mockStatic(FlowQuery.class);
             MockedStatic<ServiceFile> fileUtil = Mockito.mockStatic(ServiceFile.class,
                     withSettings().defaultAnswer(Mockito.CALLS_REAL_METHODS));
             MockedStatic<FlowDelete> deleteData = Mockito.mockStatic(FlowDelete.class)) {

            selectData.when(() -> FlowQuery.getServiceFile(CONO, DIVI, SERVICENAME, SERVICENO))
                    .thenReturn(existing);

            JSONArray result = ServiceFile.prepareUpdateToFileServer(
                    CONO, DIVI, SERVICENAME, SERVICENO,
                    Collections.emptyList(), Collections.emptyList(), USERNAME);

            assertEquals(1, result.length());
            assertEquals("delete", result.getJSONObject(0).getString("action"));
            fileUtil.verify(() -> ServiceFile.deleteFileServer(anyString()), Mockito.never());
            deleteData.verify(() -> FlowDelete.deleteServiceFile(
                    eq(CONO), eq(DIVI), eq(SERVICENAME), eq(SERVICENO), eq("orphan")));
        }
    }
}
