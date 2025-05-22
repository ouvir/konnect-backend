package com.konnect.util;

import java.util.List;

public class ImageData {
    private final String thumbnailBase64;
    private final List<String> imagesBase64;

    public ImageData(String thumbnailBase64, List<String> imagesBase64) {
        this.thumbnailBase64 = thumbnailBase64;
        this.imagesBase64 = imagesBase64;
    }

    public String getThumbnailBase64() {
        return thumbnailBase64;
    }

    public List<String> getImagesBase64() {
        return imagesBase64;
    }
}
