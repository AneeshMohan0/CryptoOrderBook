package com.interview.util;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Utility {

    private Utility() {

    }

    public static Map getNewMap() {
        return new ConcurrentHashMap<>();
    }
}
