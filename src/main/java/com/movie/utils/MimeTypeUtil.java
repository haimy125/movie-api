package com.movie.utils;

import org.apache.tika.Tika;

public class MimeTypeUtil {
    private static final Tika tika = new Tika();

    /**
     * Xác định MIME type từ dữ liệu nhị phân.
     *
     * @param data byte array chứa dữ liệu file
     * @return MIME type dưới dạng String (ví dụ: "image/png")
     */
    public static String detectMimeType(byte[] data) {
        if (data == null || data.length == 0) {
            throw new IllegalArgumentException("Dữ liệu không hợp lệ");
        }
        return tika.detect(data);
    }
}
