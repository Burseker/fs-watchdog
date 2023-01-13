package com.burseker.hiphub.fswatchdog.utils;

import java.util.Optional;

public class OptionalTry {
    public static <R> Optional<R> of(CheckedSupplier<R> f){
        try {
            return Optional.ofNullable(f.get());
        } catch (Exception e) {
            return Optional.empty();
        }
    }
}
