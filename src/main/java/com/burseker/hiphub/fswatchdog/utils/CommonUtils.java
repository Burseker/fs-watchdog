package com.burseker.hiphub.fswatchdog.utils;

import java.net.URL;
import java.nio.file.Path;

public class CommonUtils {

    public static Path URL2Path(final URL url){
        return UnhandledExceptionWrapper.call(()->Path.of(url.toURI()));
    }
}
