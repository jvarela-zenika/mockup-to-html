package com.jvarela.mockupsgenerator.enums;

import lombok.Getter;

import java.util.Arrays;

@Getter
public enum ElementType {
    BODY, HEADER, FOOTER;

    private int value;

    public static ElementType fromInt(int id) {
        return Arrays
                .stream(ElementType.values())
                .filter(elementTypes -> elementTypes.ordinal() == id)
                .findFirst().orElse(BODY);
    }
}
