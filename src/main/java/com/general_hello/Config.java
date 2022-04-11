package com.general_hello;

import io.github.cdimascio.dotenv.Dotenv;

public class Config {
    // Simple Config getter
    private static final Dotenv dotenv = Dotenv.load();

    public static String get(String key) {
        return dotenv.get(key.toUpperCase());
    }
}
