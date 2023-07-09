package ru.netology;

import java.util.List;

// принцип SOLID
// класс должен обладпть одной ответственнностью
// разные степени абстракции у методов
public final class PathValidator {
    private static final List<String> VALID_PATHS = List.of(
            "/index.html",
            "/spring.svg",
            "/spring.png",
            "/resources.html",
            "/styles.css",
            "/app.js",
            "/links.html",
            "/forms.html",
            "/classic.html",
            "/events.html",
            "/events.js"
    );

    public static boolean validate(String path) {
        return VALID_PATHS.contains(path);
    }

    public static boolean validate(int len) {
        return len == 3;
    }
}

// перегрузка функций