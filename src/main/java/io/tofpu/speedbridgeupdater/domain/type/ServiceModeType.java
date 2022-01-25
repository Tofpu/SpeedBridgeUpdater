package io.tofpu.speedbridgeupdater.domain.type;

public enum ServiceModeType {
    LOCALLY, PTERODACTYL;

    public static ServiceModeType match(String type) {
        switch (type.toUpperCase()) {
            case "PTERODACTYL": {
                return PTERODACTYL;
            }
            case "LOCALLY": {
                return LOCALLY;
            }
        }
        return null;
    }
}
