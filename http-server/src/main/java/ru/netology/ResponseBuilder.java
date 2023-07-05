package ru.netology;

public final class ResponseBuilder {

    public static byte[] okResponse(String mimeType, long length) {
        return ("HTTP/1.1 200 OK\r\n" +
                "Content-Type: " + mimeType + "\r\n" +
                "Content-Length: " + length + "\r\n" +
                "Connection: close\r\n" +
                "\r\n"
        ).getBytes();
    }

    public static byte[] notFoundResponse() {
        return ("HTTP/1.1 404 Not Found\r\n" +
                "Content-Length: 0\r\n" +
                "Connection: close\r\n" +
                "\r\n"
        ).getBytes();
    }
}
