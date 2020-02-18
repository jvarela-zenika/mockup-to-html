package com.jvarela.mockupsgenerator.enums;

import lombok.Getter;

import java.util.Arrays;

@Getter
public enum NodeComponentType {
    TEXT, TITLE, SUBTITLE, LINK, IMAGE, BUTTON;

    private int value;

    public static NodeComponentType fromInt(int id) {
        return Arrays
                .stream(NodeComponentType.values())
                .filter(nodeComponentType -> nodeComponentType.ordinal() == id)
                .findFirst().orElse(TEXT);
    }
}
