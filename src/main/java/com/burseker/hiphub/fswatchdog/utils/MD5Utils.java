package com.burseker.hiphub.fswatchdog.utils;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.nio.channels.SeekableByteChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import static java.nio.file.StandardOpenOption.READ;

public class MD5Utils {
    public static byte[] checksumByInputStream(Path filePath) {
        MessageDigest md;
        try {
            md = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalArgumentException(e);
        }

        try (InputStream is = Files.newInputStream(filePath);
             DigestInputStream dis = new DigestInputStream(is, md)) {
            while (dis.read() != -1); //empty loop to clear the data
            md = dis.getMessageDigest();
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }
        return md.digest();
    }

    public static byte[] checksumByBufferedReading(Path filePath) {
        MessageDigest md;
        try {
            md = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalArgumentException(e);
        }

        try (SeekableByteChannel sbc = Files.newByteChannel(filePath, READ)) {
            ByteBuffer buff = ByteBuffer.allocate(2048);
            while(sbc.read(buff) != -1)
            {
                buff.flip();
                md.update(buff);
                buff.clear();
            }
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }
        return md.digest();
    }

    public static byte[] checksumByAllBytesReading(Path filePath) {
        try {
            byte[] data = Files.readAllBytes(filePath);
            return MessageDigest.getInstance("MD5").digest(data);
        } catch (IOException | NoSuchAlgorithmException e) {
            throw new IllegalArgumentException(e);
        }
    }

    public static String checksum(Path filePath){
        return new BigInteger(1, checksumByBufferedReading(filePath)).toString(16);
    }
}
