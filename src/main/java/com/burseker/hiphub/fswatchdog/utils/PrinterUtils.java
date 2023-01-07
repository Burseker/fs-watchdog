package com.burseker.hiphub.fswatchdog.utils;

import java.util.List;
import java.util.stream.Collectors;

public class PrinterUtils {
    public static String listToString(List<String> val){
        return val.stream().collect(Collectors.joining(System.lineSeparator() + "  - ", "[" + System.lineSeparator() + "  - ", System.lineSeparator() + "]"));
    }
}
