package io.tofpu.speedbridgeupdater.exception;

public final class InvalidServiceModeException extends Exception {
    public InvalidServiceModeException(final String type) {
        super(type + " is an invalid service mode type!");
    }
}
