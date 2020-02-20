package com.jvarela.mockupsgenerator.model.components;

import com.jvarela.mockupsgenerator.enums.NodeComponentType;
import com.jvarela.mockupsgenerator.enums.PositionEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

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
        return type.name().toLowerCase();
    }

    public void randomizePosition(ElementNode node) {
        if (node == null) {
            position = PositionEnum.getRandomPosition();
        } else {
            position = node.getComponents()
                    .stream()
                    .findFirst()
                    .map(nodeComponent -> nodeComponent.position)
                    .orElse(PositionEnum.getRandomPosition());
        }
    }
}
