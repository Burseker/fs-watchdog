package com.burseker.hiphub.fswatchdog.utils.enumerations;

import java.util.Optional;

/**
 * @author Burseker
 *
 * Конвертер String <-> Enum
 */
public class EnumConverter {

    /**
     * @param clazz - Class<T> Enum, который хотим распарсить
     * @param arg - Текстовое представление Enum
     * @param <T extends Enum<T>> - тип класса Enum
     * @return Optional<T> содержит полученный Enum либо Optional.empty()
     */
    public static <T extends Enum<T>> Optional<T> of(Class<T> clazz, String arg){
        if(arg==null) return Optional.empty();
        try {
            return Optional.of(T.valueOf(clazz, arg));
        } catch (IllegalArgumentException e){
            return Optional.empty();
        }
    }


    /**
     * @param clazz - Class<T> Enum, который хотим распарсить
     * @param arg - Текстовое представление Enum
     * @param defaultEnumValue - дефолтное значение Enum
     * @param <T extends Enum<T>> - тип класса Enum
     * @return T содержит полученный Enum
     */
    public static <T extends Enum<T>> T of(Class<T> clazz, String arg, T defaultEnumValue){
        if(arg==null) return defaultEnumValue;
        try {
            return T.valueOf(clazz, arg);
        } catch (IllegalArgumentException e){
            return defaultEnumValue;
        }
    }
}
