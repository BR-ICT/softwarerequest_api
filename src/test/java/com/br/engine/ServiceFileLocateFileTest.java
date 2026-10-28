package com.br.engine;

import java.io.File;

import org.junit.After;
import org.junit.Test;

import static org.junit.Assert.*;

public class ServiceFileLocateFileTest {

    // a name no real request has; deleted after each test
    private static final String NAME = "LOCATETEST_1_doc.pdf";

    @After
    public void deleteTestFiles() {
        new File(ServiceFile.FILE_DIR, NAME).delete();
    }

    @Test
    public void testSavedFileName_hasCompanyServiceAndNumber() {
        assertEquals("10_101_USRQ_26000001_1_ทดสอบ Files 1.txt",
                ServiceFile.savedFileName(" 10", "101 ", "USRQ", "26000001", 1, "ทดสอบ Files 1.txt"));
    }

    @Test
    public void testFileDir_isFileFolderOfConfigUnderFileBaseDir() {
        assertTrue(ServiceFile.FILE_DIR, ServiceFile.FILE_DIR.endsWith(EngineConfig.FILEFOLDER + File.separator));
    }

    @Test
    public void testLocate_inFileDir() throws Exception {
        new File(ServiceFile.FILE_DIR).mkdirs();
        File file = new File(ServiceFile.FILE_DIR, NAME);
        file.createNewFile();
        assertEquals(file.getPath(), ServiceFile.locateFile("SVRQ", NAME).getPath());
    }

    @Test
    public void testLocate_missing_fileDir() {
        assertEquals(new File(ServiceFile.FILE_DIR, NAME).getPath(),
                ServiceFile.locateFile("SVRQ", NAME).getPath());
    }

    @Test
    public void testFileDir_noFolderName_throws() {
        try {
            ServiceFile.fileDir(" ");
            fail("expected an exception");
        } catch (IllegalStateException e) {
            assertTrue(e.getMessage().contains("FILEFOLDER"));
        }
    }
}
