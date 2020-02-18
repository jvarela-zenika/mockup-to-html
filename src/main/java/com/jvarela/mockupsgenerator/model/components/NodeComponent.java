package com.jvarela.mockupsgenerator.model.components;

import com.jvarela.mockupsgenerator.enums.NodeComponentType;
import com.jvarela.mockupsgenerator.enums.PositionEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.Random;

@Data
@AllArgsConstructor
@Builder
public class NodeComponent {
    private NodeComponentType type;
    private PositionEnum position;
    private float xSpace;
    private float ySpace;

    @Override
    public String toString() {
        return type.name().toLowerCase() + "-" + position.name().toLowerCase();
    }

    public void randomizePosition() {
        // between [1 & 3]
        int rgn = new Random().nextInt(4) + 1;
        switch (rgn) {
            case 1:
                position = PositionEnum.LEFT;
                break;
            case 2:
                position = PositionEnum.CENTER;
                break;
            case 3:
                position = PositionEnum.RIGHT;
        }
    }
}
