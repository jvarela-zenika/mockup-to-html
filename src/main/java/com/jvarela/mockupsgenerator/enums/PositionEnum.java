package com.jvarela.mockupsgenerator.enums;

import lombok.Getter;

import java.util.Arrays;
import java.util.Random;

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

    public static PositionEnum getRandomPosition()  {
        return PositionEnum.values()[new Random().nextInt(PositionEnum.values().length)];
    }
}
