package hu.matenikula.foxmanager.domain;

import lombok.Getter;

@Getter
public enum Gender {
    MALE("Hím"),
    FEMALE("Nőstény");

    private final String displayName;

    Gender(String displayName) {
        this.displayName = displayName;
    }

}