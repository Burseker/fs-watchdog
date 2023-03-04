package com.burseker.hiphub.fswatchdog.common.nonproject;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

@Slf4j
public class EncodingTest {

    @Test
    void encodingTest() throws UnsupportedEncodingException {
        log.info("testEndpoint(). entry");

        String testString = "Какие то символы на русском";
        log.info(testString);
        log.info(new BigInteger(1, testString.getBytes()).toString(16));
        String utf8EncodedString = new String(testString.getBytes(StandardCharsets.UTF_8));
        log.info(utf8EncodedString);
        log.info(new BigInteger(1, utf8EncodedString.getBytes()).toString(16));

        // Setting the file encoding explicitly
        // to a new value
        //System.setProperty("file.encoding", "UTF-8");
        System.setProperty("file.encoding", "windows-1251");

        //рабочий метод для IDEA, не очень хороший способ, надо еще копать
        //https://youtrack.jetbrains.com/issue/IDEA-276155/Unable-to-change-gradle-build-output-encoding

        log.info("Кодировка по умолчанию Charset.defaultCharset(): {}", Charset.defaultCharset());
        log.info("Кодировка по умолчанию System.getProperty(\"file.encoding\"){}", System.getProperty("file.encoding"));
        log.info("Кодировка по умолчанию getCharacterEncoding(){}", getCharacterEncoding());
        //https://www.geeksforgeeks.org/how-to-get-and-set-default-character-encoding-or-charset-in-java/
    }

    public static String getCharacterEncoding()
    {

        // Creating and initializing byte array
        // with some random character say it be N

        // Here N = w
        byte[] byte_array = { 'w' };

        // Creating an object of inputStream
        InputStream inStream = new ByteArrayInputStream(byte_array);

        // Now, opening new file input stream reader
        InputStreamReader streamReader = new InputStreamReader(inStream);

        String defaultCharset = streamReader.getEncoding();

        // Returning the default character encoded
        // Here it is for N = 'w'
        return defaultCharset;
    }
}
