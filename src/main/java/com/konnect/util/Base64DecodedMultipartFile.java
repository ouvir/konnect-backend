package com.konnect.util;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.Base64;

/**
 * Base64로 인코딩된 문자열을 MultipartFile로 변환하는 구현체입니다.
 */
@Getter
@Setter
public class Base64DecodedMultipartFile implements MultipartFile {

    private String name;
    private String originalFilename;
    private String contentType;
    private byte[] data;

    /**
     * base64 문자열만 받아 MultipartFile로 변환합니다.
     * fieldName은 "file", 파일명은 "file.{extension}"으로 설정됩니다.
     *
     * @param base64String "data:<contentType>;base64,<data>" 형태
     */
    public Base64DecodedMultipartFile(String base64String) {
        init(base64String, "file", null);
    }

    /**
     * base64 문자열과 폼 필드명, 원본 파일명을 받아 MultipartFile로 변환합니다.
     *
     * @param base64String "data:<contentType>;base64,<data>" 형태
     * @param fieldName    form-data 필드 이름
     * @param fileName     MultipartFile#getOriginalFilename()
     */
    public Base64DecodedMultipartFile(String base64String, String fieldName, String fileName) {
        init(base64String, fieldName, fileName);
    }

    private void init(String base64String, String fieldName, String fileName) {
        String[] parts = base64String.split(",");
        if (parts.length != 2) {
            throw new IllegalArgumentException("Invalid base64 format");
        }
        String meta = parts[0];
        String dataPart = parts[1];
        this.contentType = meta.substring(meta.indexOf(":") + 1, meta.indexOf(";"));
        this.data = Base64.getDecoder().decode(dataPart);
        this.name = fieldName;
        if (fileName != null) {
            this.originalFilename = fileName;
        } else {
            String ext = contentType.substring(contentType.indexOf("/") + 1);
            this.originalFilename = "file." + ext;
        }
    }

    @Override public String getName()             { return name; }
    @Override public String getOriginalFilename() { return originalFilename; }
    @Override public String getContentType()      { return contentType; }

    @Override
    public boolean isEmpty() {
        return data == null || data.length == 0;
    }

    @Override
    public long getSize() {
        return data.length;
    }

    @Override
    public byte[] getBytes() {
        return data;
    }

    @Override
    public InputStream getInputStream() {
        return new ByteArrayInputStream(data);
    }

    @Override
    public void transferTo(File dest) throws IOException {
        try (OutputStream os = new FileOutputStream(dest)) {
            os.write(data);
        }
    }
}
