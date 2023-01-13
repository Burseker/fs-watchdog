package com.burseker.hiphub.fswatchdog.utils;

public class UnhandledExceptionWrapper {
    public static <R> R call(CheckedSupplier<R> f){
        try {
            return f.get();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
