package ru.kalugin19.fridge.android.pub.v1.data.model;

import java.io.IOException;

/**
 * No Internet Exception
 *
 * @author Kalugin Valeriy
 */

public class NoInternetException extends IOException {

    public NoInternetException() {
    }

    @SuppressWarnings("unused")
    public NoInternetException(Throwable cause) {
        super(cause);
    }
}
