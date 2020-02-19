package com.jvarela.mockupsgenerator.model.components;

import com.jvarela.mockupsgenerator.model.factories.NodeComponentFactory;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.stream.Collectors;

@Data
@Builder
public class ElementNode {
    private int xCapacity;
    private int yCapacity;
    private List<NodeComponent> components;

    public ElementNode(int xCapacity, int yCapacity, List<NodeComponent> components) {
        this.xCapacity = xCapacity;
        this.yCapacity = yCapacity;
        this.components = CollectionUtils.isEmpty(components) ? new ArrayList<>() : components;
    }

    @Override
    public String toString() {
        String ls = System.lineSeparator();
        return getName() + " {" + ls + components.stream().map(NodeComponent::toString).collect(Collectors.joining(", ")) + ls + "}";
    }

    public String getName() {
        return getNumberLabel(getXCapacity()) + "-" + getNumberLabel(getYCapacity());
    }

    private String getNumberLabel(int number) {
        String label = "";
        switch (number) {
            case 1:
                label = "simple";
                break;
            case 2:
                label = "double";
                break;
            case 4:
                label = "quadruple";
        }
        return label;
    }

    public static ElementNode getRandomNodeWithCapacityBetweenInclusive(
            int maxXCapacity,
            int maxYCapacity
    ){
        int xCapacity = getRandomCapacity(maxXCapacity);
        int yCapacity = getRandomCapacity(maxYCapacity);

        ElementNode node = ElementNode.builder()
                .xCapacity(xCapacity)
                .yCapacity(yCapacity)
                .build();

        node.fillWithRandomComponents();

        return node;
    }

    private static Integer getRandomCapacity(int currentCapacity) {
        Random random = new Random();
        int capacity = random.nextInt(currentCapacity) + 1;
        if (capacity == 3) {
            capacity = 2;
        } else if (capacity > 4) {
            capacity = 4;
        }
        return capacity;
    }

    public void fillWithRandomComponents(){
        NodeComponent currentComponent = NodeComponentFactory.getRandomWhereSpaceSmallerOrEqualThan(xCapacity, yCapacity);
        float currentXCapacity = (float) xCapacity;
        float currentYCapacity = (float) yCapacity;

        while (currentComponent != null){
            currentComponent.randomizePosition();
            this.components.add(currentComponent);

            currentXCapacity -= currentComponent.getXSpace();
            currentYCapacity -= currentComponent.getYSpace();
            currentComponent = NodeComponentFactory.getRandomWhereSpaceSmallerOrEqualThan(currentXCapacity, currentYCapacity);
        }
    }
}
