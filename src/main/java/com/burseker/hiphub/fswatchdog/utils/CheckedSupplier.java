package com.burseker.hiphub.fswatchdog.utils;

@FunctionalInterface
public interface CheckedSupplier<T> {
    T get() throws Exception;
}
