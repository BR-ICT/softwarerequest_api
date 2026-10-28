package com.br.engine;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONObject;
import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.io.File;
import java.nio.file.Files;
import java.util.Base64;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class ServiceFilePrepareGetToFileServerTest {

    private static final String CONO        = "010";
    private static final String DIVI        = "100";
    private static final String SERVICENAME = "IT";
    private static final String SERVICENO   = "IT001";
    private static final String FILE_DIR    = ServiceFile.FILE_DIR;

    @BeforeClass
    public static void createTestDir() {
        new File(FILE_DIR).mkdirs();
    }

    @After
    public void deleteTestFiles() {
        File[] files = new File(FILE_DIR).listFiles((d, n) -> n.startsWith(SERVICENO + "_test_"));
        if (files != null) for (File f : files) f.delete();
    }

    /** Creates a file in the test directory with the given content bytes. */
    private File createTestFile(String name, byte[] content) throws Exception {
        File f = new File(FILE_DIR + name);
        Files.write(f.toPath(), content);
        return f;
    }

    /** Builds a getServiceFile single-row JSON result. */
    private String singleFileJson(String fieldName, String originalName, String savedName, String fileType) {
        return "[{\"FILINE\":1,\"FISNAM\":\"" + fieldName + "\",\"FIFNAM\":\"" + originalName
                + "\",\"FITYPE\":\"" + fileType + "\",\"FIREM1\":\"" + savedName + "\"}]";
    }

    // -----------------------------------------------------------------------
    // No files in DB
    // -----------------------------------------------------------------------

    @Test
    public void testNoFilesInDb_returnsEmptyArray() throws Exception {
        try (MockedStatic<FlowQuery> selectData = Mockito.mockStatic(FlowQuery.class)) {
            selectData.when(() -> FlowQuery.getServiceFile(CONO, DIVI, SERVICENAME, SERVICENO))
                    .thenReturn(null);

            JSONArray result = ServiceFile.prepareGetToFileServer(CONO, DIVI, SERVICENAME, SERVICENO);

            assertEquals(0, result.length());
        }
    }

    @Test
    public void testEmptyStringFromDb_returnsEmptyArray() throws Exception {
        try (MockedStatic<FlowQuery> selectData = Mockito.mockStatic(FlowQuery.class)) {
            selectData.when(() -> FlowQuery.getServiceFile(CONO, DIVI, SERVICENAME, SERVICENO))
                    .thenReturn("   ");

            JSONArray result = ServiceFile.prepareGetToFileServer(CONO, DIVI, SERVICENAME, SERVICENO);

            assertEquals(0, result.length());
        }
    }

    // -----------------------------------------------------------------------
    // File in DB but missing on disk → skipped
    // -----------------------------------------------------------------------

    @Test
    public void testFileInDbNotOnDisk_skipsEntry() throws Exception {
        try (MockedStatic<FlowQuery> selectData = Mockito.mockStatic(FlowQuery.class)) {
            selectData.when(() -> FlowQuery.getServiceFile(CONO, DIVI, SERVICENAME, SERVICENO))
                    .thenReturn(singleFileJson("field1", "doc.pdf",
                            "IT001_test_notexist.pdf", "application/pdf"));

            JSONArray result = ServiceFile.prepareGetToFileServer(CONO, DIVI, SERVICENAME, SERVICENO);

            assertEquals(0, result.length());
        }
    }

    // -----------------------------------------------------------------------
    // File on disk → base64 content returned
    // -----------------------------------------------------------------------

    @Test
    public void testSingleFile_onDisk_returnsBase64Content() throws Exception {
        String savedName = "IT001_test_doc.pdf";
        createTestFile(savedName, new byte[]{72, 101, 108, 108, 111}); // "Hello"

        try (MockedStatic<FlowQuery> selectData = Mockito.mockStatic(FlowQuery.class)) {
            selectData.when(() -> FlowQuery.getServiceFile(CONO, DIVI, SERVICENAME, SERVICENO))
                    .thenReturn(singleFileJson("field1", "doc.pdf", savedName, "application/pdf"));

            JSONArray result = ServiceFile.prepareGetToFileServer(CONO, DIVI, SERVICENAME, SERVICENO);

            assertEquals(1, result.length());
            JSONObject item = result.getJSONObject(0);
            assertEquals(SERVICENO, item.getString("serviceno"));
            assertEquals("field1",  item.getString("itemName"));
            assertEquals("doc.pdf", item.getString("fileName"));
            assertTrue(item.getString("content").contains(";base64,"));
            assertFalse(item.getString("content").isEmpty());
        }
    }

    @Test
    public void testMultipleFiles_onDisk_returnsAll() throws Exception {
        createTestFile("IT001_test_a.pdf", new byte[]{1, 2});
        createTestFile("IT001_test_b.png", new byte[]{3, 4});

        String multiJson = "[{\"FILINE\":1,\"FISNAM\":\"f1\",\"FIFNAM\":\"a.pdf\","
                + "\"FITYPE\":\"application/pdf\",\"FIREM1\":\"IT001_test_a.pdf\"},"
                + "{\"FILINE\":2,\"FISNAM\":\"f2\",\"FIFNAM\":\"b.png\","
                + "\"FITYPE\":\"image/png\",\"FIREM1\":\"IT001_test_b.png\"}]";

        try (MockedStatic<FlowQuery> selectData = Mockito.mockStatic(FlowQuery.class)) {
            selectData.when(() -> FlowQuery.getServiceFile(CONO, DIVI, SERVICENAME, SERVICENO))
                    .thenReturn(multiJson);

            JSONArray result = ServiceFile.prepareGetToFileServer(CONO, DIVI, SERVICENAME, SERVICENO);

            assertEquals(2, result.length());
        }
    }

    // -----------------------------------------------------------------------
    // Unknown MIME type → defaults to application/octet-stream
    // -----------------------------------------------------------------------

    @Test
    public void testUnknownMimeType_defaultsToOctetStream() throws Exception {
        // .xyz extension is not recognized on any OS → probeContentType returns null
        String savedName = "IT001_test_data.xyz";
        createTestFile(savedName, new byte[]{1});

        try (MockedStatic<FlowQuery> selectData = Mockito.mockStatic(FlowQuery.class)) {
            selectData.when(() -> FlowQuery.getServiceFile(CONO, DIVI, SERVICENAME, SERVICENO))
                    .thenReturn(singleFileJson("field1", "data.xyz", savedName, "application/octet-stream"));

            JSONArray result = ServiceFile.prepareGetToFileServer(CONO, DIVI, SERVICENAME, SERVICENO);

            assertEquals(1, result.length());
            // probeContentType returns null for .xyz → code falls back to application/octet-stream
            assertEquals("application/octet-stream", result.getJSONObject(0).getString("type"));
        }
    }

    // -----------------------------------------------------------------------
    // Empty FIREM1 (savedName) in DB row → entry skipped
    // -----------------------------------------------------------------------

    @Test
    public void testEmptyFIREM1_skipsEntry() throws Exception {
        String json = "[{\"FILINE\":1,\"FISNAM\":\"field1\",\"FIFNAM\":\"doc.pdf\","
                + "\"FITYPE\":\"application/pdf\",\"FIREM1\":\"\"}]";

        try (MockedStatic<FlowQuery> selectData = Mockito.mockStatic(FlowQuery.class)) {
            selectData.when(() -> FlowQuery.getServiceFile(CONO, DIVI, SERVICENAME, SERVICENO))
                    .thenReturn(json);

            JSONArray result = ServiceFile.prepareGetToFileServer(CONO, DIVI, SERVICENAME, SERVICENO);

            assertEquals(0, result.length());
        }
    }

    // -----------------------------------------------------------------------
    // Base64 content matches bytes written to disk
    // -----------------------------------------------------------------------

    @Test
    public void testBase64ContentIsCorrect() throws Exception {
        byte[] payload = {72, 101, 108, 108, 111}; // "Hello"
        String savedName = "IT001_test_hello.pdf";
        createTestFile(savedName, payload);

        try (MockedStatic<FlowQuery> selectData = Mockito.mockStatic(FlowQuery.class)) {
            selectData.when(() -> FlowQuery.getServiceFile(CONO, DIVI, SERVICENAME, SERVICENO))
                    .thenReturn(singleFileJson("field1", "hello.pdf", savedName, "application/pdf"));

            JSONArray result = ServiceFile.prepareGetToFileServer(CONO, DIVI, SERVICENAME, SERVICENO);

            String content = result.getJSONObject(0).getString("content");
            String b64Part = content.split(";base64,")[1];
            byte[] decoded = Base64.getDecoder().decode(b64Part);
            assertArrayEquals(payload, decoded);
        }
    }

    // -----------------------------------------------------------------------
    // Some files on disk, some missing → only present ones returned
    // -----------------------------------------------------------------------

    @Test
    public void testPartiallyMissingFiles_skipsOnlyMissing() throws Exception {
        createTestFile("IT001_test_present.pdf", new byte[]{1, 2, 3});
        // IT001_test_absent.pdf is intentionally NOT created

        String multiJson = "[{\"FILINE\":1,\"FISNAM\":\"f1\",\"FIFNAM\":\"present.pdf\","
                + "\"FITYPE\":\"application/pdf\",\"FIREM1\":\"IT001_test_present.pdf\"},"
                + "{\"FILINE\":2,\"FISNAM\":\"f2\",\"FIFNAM\":\"absent.pdf\","
                + "\"FITYPE\":\"application/pdf\",\"FIREM1\":\"IT001_test_absent.pdf\"}]";

        try (MockedStatic<FlowQuery> selectData = Mockito.mockStatic(FlowQuery.class)) {
            selectData.when(() -> FlowQuery.getServiceFile(CONO, DIVI, SERVICENAME, SERVICENO))
                    .thenReturn(multiJson);

            JSONArray result = ServiceFile.prepareGetToFileServer(CONO, DIVI, SERVICENAME, SERVICENO);

            assertEquals(1, result.length());
            assertEquals("f1", result.getJSONObject(0).getString("itemName"));
        }
    }
}
