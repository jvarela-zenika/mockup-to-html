package com.jvarela.mockupsgenerator.enums;

import lombok.Getter;

import java.util.Arrays;

@Getter
public enum PositionEnum {
    LEFT, CENTER, RIGHT;

    private int value;

    public static PositionEnum fromInt(int id) {
        return Arrays
                .stream(PositionEnum.values())
                .filter(position -> position.ordinal() == id)
                .findFirst().orElse(LEFT);
    }
}
