package com.raymedis.rxviewui.service.fpdSdks.service;

public enum FpsStatus {

    UNKNOWN_MODE(0),
    NON_OPERATION_STATE(1),
    OPERATION_IDLE(2),
    RESET_MODE(3),
    DIAGNOSIS_MODE(4),
    DISCHARGE_MODE(5),
    AED_ONE_SHOT_MODE(6),
    AED_REPEAT_MODE(7),
    SOFTWARE_SYNC_MODE(8),
    SYSTEM_UP(9),
    SYSTEM_DOWN(10),
    ACQUIRE_MODE(11);

    private final int code;

    FpsStatus(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public static FpsStatus fromCode(int code) {
        for (FpsStatus mode : FpsStatus.values()) {
            if (mode.code == code) {
                return mode;
            }
        }
        throw new IllegalArgumentException("Invalid SystemMode code: " + code);
    }

}
