package io.tofpu.speedbridgeupdater.exception;

public final class InvalidPanelURLException extends Exception {
    public InvalidPanelURLException(final String url) {
        super(url + " is an invalid panel url.");
    }
}
