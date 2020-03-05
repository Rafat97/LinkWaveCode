package com.example.linkwave;

public class Logger {
    private static Logger mInstance = new Logger();

    public static Logger getInstance() {
        return mInstance;
    }

    private Logger() {
    }

    public void Log(String msg) {
        System.out.println("LinkWave App: " + msg);
    }
}
