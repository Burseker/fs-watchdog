package com.burseker.hiphub.fswatchdog.utils;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Path;

import static com.burseker.hiphub.fswatchdog.utils.CommonUtils.URL2Path;
import static org.junit.jupiter.api.Assertions.*;

class MD5UtilsTest {
    private static final String TEST_FILE_PATH = "util/files/fileForChecksum.dat";
    private static Path testFilePath = URL2Path(MD5UtilsTest.class.getClassLoader().getResource(TEST_FILE_PATH));

    @BeforeAll
    static void init() {
        System.out.println("absolute path to testFilePath=" + testFilePath.toAbsolutePath());
        assertTrue(Files.isRegularFile(testFilePath));
    }

    @Test
    void checksumByInputStream() {
        String md5 = new BigInteger(1, MD5Utils.checksumByInputStream(testFilePath)).toString(16);
        assertEquals("889e978178e2fe5942e1fa46f8993129", md5);
    }

    @Test
    void checksumByBufferedReading() {
        String md5 = new BigInteger(1, MD5Utils.checksumByBufferedReading(testFilePath)).toString(16);
        assertEquals("889e978178e2fe5942e1fa46f8993129", md5);
    }

    @Test
    void checksumByAllBytesReading() {
        String md5 = new BigInteger(1, MD5Utils.checksumByAllBytesReading(testFilePath)).toString(16);
        assertEquals("889e978178e2fe5942e1fa46f8993129", md5);
    }

    @Test
    void checksum() {
        assertEquals("889e978178e2fe5942e1fa46f8993129", MD5Utils.checksum(testFilePath));
    }
}