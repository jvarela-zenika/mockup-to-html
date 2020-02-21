package com.jvarela.mockupsgenerator.model.components;

import com.jvarela.mockupsgenerator.model.factories.NodeComponentFactory;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.util.CollectionUtils;

import java.util.*;
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
        return getName() + " {" + ls + getChildToString() + ls + "}";
    }

    public String getChildToString() {
        return CollectionUtils.isEmpty(components)
                ? ""
                : components.stream().map(NodeComponent::toString).collect(Collectors.joining(", "));
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
            int minXCapacity,
            int minYCapacity,
            int maxXCapacity,
            int maxYCapacity
    ) {
        int xCapacity = getRandomRangeCapacity(minXCapacity, maxXCapacity);
        int yCapacity = getRandomRangeCapacity(minYCapacity, maxYCapacity);

        ElementNode node = ElementNode.builder()
                .xCapacity(xCapacity)
                .yCapacity(yCapacity)
                .build();

        node.fillWithRandomComponents();

        return node;
    }

    private static int getRandomRangeCapacity(int min, int max) {
        Random random = new Random();
        int capacity = random.nextInt((max + 1) - min) + min;
        if (capacity == 3) {
            capacity = max >= 4 ? 4 : 2;
        } else if (capacity > 4) {
            capacity = 4;
        }
        return capacity;
    }

    private static Integer getRandomCapacity(int currentCapacity) {
        Random random = new Random();
        int capacity = random.nextInt(currentCapacity) + 1;
        if (capacity == 3) {
            capacity = currentCapacity >= 4 ? 4 : 2;
        } else if (capacity > 4) {
            capacity = 4;
        }
        return capacity;
    }

    public void fillWithRandomComponents() {
        float currentXCapacity = (float) xCapacity;
        float currentYCapacity = (float) yCapacity;

        if (CollectionUtils.isEmpty(components) && new Random().nextInt(10) > 7){
            components.add(NodeComponentFactory.getImageComponent());
        } else {
            for (NodeComponent component : components) {
                currentXCapacity -= component.getXSpace();
                currentYCapacity -= component.getYSpace();
            }

            NodeComponent currentComponent = NodeComponentFactory.getRandomWhereSpaceSmallerOrEqualThan(currentXCapacity, currentYCapacity);
            while (currentComponent != null) {
                currentComponent.randomizePosition(this);
                this.components.add(currentComponent);
                currentXCapacity -= currentComponent.getXSpace();
                currentYCapacity -= currentComponent.getYSpace();
                currentComponent = NodeComponentFactory.getRandomWhereSpaceSmallerOrEqualThan(currentXCapacity, currentYCapacity);
            }
        }
    }
}
