package com.burseker.hiphub.fswatchdog.utils;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class PrinterUtils {
    public static String listToString(List<String> val){
        return val.stream().collect(Collectors.joining(System.lineSeparator() + "  - ", "[" + System.lineSeparator() + "  - ", System.lineSeparator() + "]"));
    }

    public static <T> String listToString(Iterable<T> val){
        return StreamSupport
                .stream(val.spliterator(), false)
                .map(T::toString)
                .collect(Collectors.joining(System.lineSeparator() + "  - ", "[" + System.lineSeparator() + "  - ", System.lineSeparator() + "]"));
    }

//    public static String mapToString(Map<String> val){
//        return val.stream().collect(Collectors.joining(System.lineSeparator() + "  - ", "[" + System.lineSeparator() + "  - ", System.lineSeparator() + "]"));
//    }
}
