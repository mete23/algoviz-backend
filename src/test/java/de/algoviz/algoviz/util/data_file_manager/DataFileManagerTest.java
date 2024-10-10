package de.algoviz.algoviz.util.data_file_manager;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class DataFileManagerTest {
    private static final String ROOT_FOLDER = "./src/main/resources/data_resources";
    private static final String TEST_TEXT1 = """
            This an example text.
            And it takes several lines.
            """;
    private static final String TEST_FILE_NAME = "exampleFile.txt";
    private static final String TEST_FOLDER_NAME = "testFolder/";
    private static final String SECOND_TEST_FOLDER = "secondTestFolder/";
    private static final String TEST_TEXT2 = """
            This another example text.
            And it will
            takes much more
            then several lines.
            """;

    //test
    @Test
    void testCompleteCycle() {
        // insert data
        try {
            DataFileManager.insertData(TEST_FOLDER_NAME, TEST_FILE_NAME, TEST_TEXT1);
        } catch (IOException e) {
            fail();
        }

        // read data
        String inputRead = "";
        try {
            inputRead = DataFileManager.getData(TEST_FOLDER_NAME, TEST_FILE_NAME);
        } catch (IOException e) {
            fail();
        }
        assertEquals(TEST_TEXT1, inputRead);

        // actualise data
        try {
            DataFileManager.actualiseData(TEST_FOLDER_NAME, TEST_FILE_NAME, TEST_TEXT2);
        } catch (IOException e) {
            fail();
        }

        // read data
        inputRead = "";
        try {
            inputRead = DataFileManager.getData(TEST_FOLDER_NAME, TEST_FILE_NAME);
        } catch (IOException e) {
            fail();
        }
        assertEquals(TEST_TEXT2, inputRead);

        // delete data
        try {
            DataFileManager.removeData(TEST_FOLDER_NAME, TEST_FILE_NAME);
        } catch (IOException e) {
            fail();
        }
        if (checkIfNotDelete(ROOT_FOLDER, TEST_FOLDER_NAME.substring(0, TEST_FOLDER_NAME.length() - 1))) {
            fail();
        }
    }

    @Test
    void testDelete() {
        try {
            DataFileManager.insertData(TEST_FOLDER_NAME + SECOND_TEST_FOLDER, TEST_FILE_NAME, TEST_TEXT1);
        } catch (IOException e) {
            fail();
        }

        try {
            DataFileManager.removeData(TEST_FOLDER_NAME + SECOND_TEST_FOLDER, TEST_FILE_NAME);
        } catch (IOException e) {
            fail();
        }
        if (checkIfNotDelete(ROOT_FOLDER, TEST_FOLDER_NAME.substring(0, TEST_FOLDER_NAME.length() - 1))) {
            fail();
        }
    }

    @Test
    void MainPathTraversalPath() {
        Assertions.assertThrows(Exception.class, () ->
                DataFileManager.getData("../../../../main/java/de/algoviz/algoviz/",
                        "AlgovizApplication.java"));
    }

    @Test
    void MainPathTraversalFileName() {
        Assertions.assertThrows(Exception.class, () ->
                DataFileManager.getData("",
                        "../../../../main/java/de/algoviz/algoviz/AlgovizApplication.java"));
    }

    @Test
    void MainPathTraversalPathAndFileName() {
        Assertions.assertThrows(Exception.class, () ->
                DataFileManager.getData("//.main/java/de/algoviz/algoviz/",
                        "AlgovizApplication.java"));
    }

    private boolean checkIfNotDelete(String folder, String folderDeleted) {
        File rootFolder = new File(folder);
        File[] children = rootFolder.listFiles();
        assert children != null;
        for (File file : children) {
            if (file.getName().equals(folderDeleted)) {
                return true;
            }
        }
        return false;
    }

}