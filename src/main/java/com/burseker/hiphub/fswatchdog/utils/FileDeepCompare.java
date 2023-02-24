package com.burseker.hiphub.fswatchdog.utils;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SeekableByteChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.NoSuchAlgorithmException;

import static java.nio.file.StandardOpenOption.READ;

public class FileDeepCompare {
    public static boolean isFilesEqualsFilesMismatchImpl(Path file1, Path file2) throws NoSuchAlgorithmException {
        //https://www.baeldung.com/java-12-new-features
        throw new NoSuchAlgorithmException("Files.mismatch(filePath1, filePath2) implemented in Java12");
    }

    private static boolean isFilesEqualsDirectCompareImpl(Path filePath1, Path filePath2){

        try (SeekableByteChannel sbc1 = Files.newByteChannel(filePath1, READ);
             SeekableByteChannel sbc2 = Files.newByteChannel(filePath2, READ)) {
            ByteBuffer buff1 = ByteBuffer.allocate(2048);
            ByteBuffer buff2 = ByteBuffer.allocate(2048);

            while(sbc1.read(buff1) != -1)
            {
                if(sbc2.read(buff2) == -1) return false;
                if(!buff1.equals(buff2))  return false;
                buff1.clear();
                buff2.clear();
            }
            if(sbc2.read(buff2) != -1) return false;
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }

        return true;
    }

    public static boolean isFilesEquals(Path filePath1, Path filePath2){
        return isFilesEqualsDirectCompareImpl(filePath1, filePath2);
    }
}
