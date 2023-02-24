package com.burseker.hiphub.fswatchdog.utils;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.nio.file.Path;

import static com.burseker.hiphub.fswatchdog.utils.CommonUtils.URL2Path;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class FileDeepCompareTest {

    private static final String TEST_FILE1_PATH = "util/files/FileDeepCompareUTFile1.dat";
    private static final String TEST_FILE1_COPY_PATH = "util/files/FileDeepCompareUTFile1_copy.dat";
    private static final String TEST_FILE2_PATH = "util/files/FileDeepCompareUTFile2.dat";
    private static final Path testFile1Path = URL2Path(FileDeepCompareTest.class.getClassLoader().getResource(TEST_FILE1_PATH));
    private static final Path testFile1CopyPath = URL2Path(FileDeepCompareTest.class.getClassLoader().getResource(TEST_FILE1_COPY_PATH));
    private static final Path testFile2Path = URL2Path(FileDeepCompareTest.class.getClassLoader().getResource(TEST_FILE2_PATH));

    @BeforeAll
    static void init() {
//        System.out.println("absolute path to testFile1Path=" + testFile1Path.toAbsolutePath());
//        System.out.println("absolute path to testFile1CopyPath=" + testFile1CopyPath.toAbsolutePath());
//        System.out.println("absolute path to testFile2Path=" + testFile2Path.toAbsolutePath());
        assertTrue(Files.isRegularFile(testFile1Path));
        assertTrue(Files.isRegularFile(testFile1CopyPath));
        assertTrue(Files.isRegularFile(testFile2Path));
    }

    @Test
    void isFilesEqualsDirectCompareImplTest(){
        assertTrue(FileDeepCompare.isFilesEquals(testFile1Path, testFile1CopyPath));
        assertFalse(FileDeepCompare.isFilesEquals(testFile1Path, testFile2Path));
    }
}