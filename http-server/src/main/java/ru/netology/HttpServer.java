package ru.netology;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.concurrent.ExecutorService;

public class HttpServer implements Server {

    private final ExecutorService executorService;
    private final int port;

    public HttpServer(ExecutorService executorService, int port) {
        this.executorService = executorService;
        this.port = port;
    }
// В задании сказано, выделить FixedThreadPool, то есть ExecutorService executor = Executors.newFixedThreadPool(5)
    @Override
    public void start() {
        try (final var serverSocket = new ServerSocket(port)) {
            while (!serverSocket.isClosed()) {
                final var socket = serverSocket.accept();
                executorService.execute(() -> requestHandler(socket));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // хэндлер - обработчик запроса
    private void requestHandler(Socket socket) {
        // read only request line for simplicity
        // must be in form GET /path HTTP/1.1
        try (final var in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             final var out = new BufferedOutputStream(socket.getOutputStream())) {
            // read only request line for simplicity
            // must be in form GET /path HTTP/1.1
            final var requestLine = in.readLine();
            final var parts = requestLine.split(" ");

            if (!PathValidator.validate(parts.length)) {
                // just close socket
                return;
            }

            Request request = new Request(parts[1]);
            if (!PathValidator.validate(request.getPath())) {
                out.write(ResponseBuilder.notFoundResponse());
                out.flush(); // прочитать
                return;
            }

            final var filePath = Path.of(".", "public", request.getPath());
            final var mimeType = Files.probeContentType(filePath);

            // special case for classic
            if (request.getPath().equals("/classic.html")) {
                final var template = Files.readString(filePath);
                final var content = template.replace(
                        "{time}",
                        LocalDateTime.now().toString()
                ).getBytes();
                out.write(ResponseBuilder.okResponse(mimeType, content.length));
                out.write(content);
                out.flush();
                return;
            }

            final var length = Files.size(filePath);
            out.write(ResponseBuilder.okResponse(mimeType, length));
            Files.copy(filePath, out);
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
