package ru.netology;

import java.util.concurrent.Executors;

public class Main {

    public static void main(String[] args) {
        new HttpServer(Executors.newFixedThreadPool(64), 9999).start();
    }
}


