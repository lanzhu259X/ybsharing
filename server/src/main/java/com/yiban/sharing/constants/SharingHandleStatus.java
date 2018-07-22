package com.yiban.sharing.constants;

import org.springframework.util.StringUtils;

public enum SharingHandleStatus {

    UNPROCESS,
    PROCESSING,
    PROCESSED;

    public static SharingHandleStatus getByName(String handleStatus) {
        if (StringUtils.isEmpty(handleStatus)) {
            return null;
        }
        for (SharingHandleStatus status : SharingHandleStatus.values()) {
            if (status.name().equalsIgnoreCase(handleStatus)) {
                return status;
            }
        }
        return null;
    }
}
