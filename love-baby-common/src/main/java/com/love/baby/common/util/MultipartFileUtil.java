package com.love.baby.common.util;

import org.springframework.web.multipart.MultipartFile;

import java.io.*;

/**
 *
 * @author Pierantonio Cangianiello
 */
public class MultipartFileUtil implements MultipartFile {

    private final String name;
    private final String originalFileName;
    private final String contentType;
    private final byte[] payload;

    public MultipartFileUtil(String originalFileName, byte[] payload,String contentType) {
        this.originalFileName = originalFileName;
        this.payload = payload;
        this.name = "file";
        this.contentType = contentType;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getOriginalFilename() {
        return originalFileName;
    }

    @Override
    public String getContentType() {
        return contentType;
    }

    @Override
    public boolean isEmpty() {
        return payload.length == 0;
    }

    @Override
    public long getSize() {
        return payload.length;
    }

    @Override
    public byte[] getBytes() throws IOException {
        return payload;
    }

    @Override
    public InputStream getInputStream() throws IOException {
        return new ByteArrayInputStream(payload);
    }

    @Override
    public void transferTo(File dest) throws IOException, IllegalStateException {
        new FileOutputStream(dest).write(payload);
    }

}
