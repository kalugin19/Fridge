package ru.kalugin19.fridge.android.pub.v2.data.model;

import java.io.IOException;

/**
 * No Internet Exception
 *
 * @author Kalugin Valeriy
 */

public class NoInternetException extends IOException {

    NoInternetException() {
    }

    @SuppressWarnings("unused")
    public NoInternetException(Throwable cause) {
        super(cause);
    }
}
