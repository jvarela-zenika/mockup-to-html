package com.jvarela.mockupsgenerator.model.factories;

import com.jvarela.mockupsgenerator.enums.NodeComponentType;
import com.jvarela.mockupsgenerator.enums.PositionEnum;
import com.jvarela.mockupsgenerator.model.components.ElementNode;
import com.jvarela.mockupsgenerator.model.components.NodeComponent;
import com.jvarela.mockupsgenerator.services.dto.BodyRow;
import lombok.experimental.UtilityClass;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

@UtilityClass
public class NodeComponentFactory {

    private static Map<NodeComponentType, NodeComponent> components = new HashMap<>();

    public NodeComponent getTitleComponent() {
        NodeComponent component = components.get(NodeComponentType.TITLE);
        if (component == null) {
            component = NodeComponent.builder()
                    .type(NodeComponentType.TITLE)
                    .position(PositionEnum.LEFT)
                    .xSpace(1f)
                    .ySpace(4f)
                    .build();
            components.put(NodeComponentType.TITLE, component);
        }
        return component;
    }

    public NodeComponent getSubTitleComponent() {
        NodeComponent component = components.get(NodeComponentType.SUBTITLE);
        if (component == null) {
            component = NodeComponent.builder()
                    .type(NodeComponentType.SUBTITLE)
                    .position(PositionEnum.LEFT)
                    .xSpace(.5f)
                    .ySpace(2f)
                    .build();
            components.put(NodeComponentType.SUBTITLE, component);
        }
        return component;
    }

    public NodeComponent getTextComponent() {
        NodeComponent component = components.get(NodeComponentType.TEXT);
        if (component == null) {
            component = NodeComponent.builder()
                    .type(NodeComponentType.TEXT)
                    .position(PositionEnum.LEFT)
                    .xSpace(.5f)
                    .ySpace(1f)
                    .build();
            components.put(NodeComponentType.TEXT, component);
        }
        return component;
    }

    public NodeComponent getLinkComponent() {
        NodeComponent component = components.get(NodeComponentType.LINK);
        if (component == null) {
            component = NodeComponent.builder()
                    .type(NodeComponentType.LINK)
                    .position(PositionEnum.LEFT)
                    .xSpace(.5f)
                    .ySpace(.5f)
                    .build();
            components.put(NodeComponentType.LINK, component );
        }
        return component;
    }

    public NodeComponent getButtonComponent() {
        NodeComponent component = components.get(NodeComponentType.BUTTON);
        if (component == null) {
            component = NodeComponent.builder()
                    .type(NodeComponentType.BUTTON)
                    .position(PositionEnum.LEFT)
                    .xSpace(.5f)
                    .ySpace(.5f)
                    .build();
            components.put(NodeComponentType.BUTTON, component);
        }
        return component;
    }

    public NodeComponent getImageComponent() {
        NodeComponent component = components.get(NodeComponentType.IMAGE);
        if (component == null) {
            component = NodeComponent.builder()
                    .type(NodeComponentType.IMAGE)
                    .position(PositionEnum.LEFT)
                    .xSpace(1f)
                    .ySpace(1f)
                    .build();
            components.put(NodeComponentType.IMAGE, component);
        }
        return component;
    }

    public List<NodeComponent> getAllComponents() {
        return Arrays.asList(
                getButtonComponent(),
                getImageComponent(),
                getLinkComponent(),
                getSubTitleComponent(),
                getTextComponent(),
                getTitleComponent()
        );
    }

    public NodeComponent getRandomWhereSpaceSmallerOrEqualThan(float x, float y) {

        List<NodeComponent> filteredComponents = getAllComponents()
                .stream()
                .filter(nodeComponent -> nodeComponent.getType() != NodeComponentType.IMAGE)
                .filter(nodeComponent -> nodeComponent.getXSpace() <= x && nodeComponent.getYSpace() <= y)
                .collect(Collectors.toList());

        Collections.shuffle(filteredComponents);

        return filteredComponents
                .stream()
                .findFirst()
                .orElse(null);
    }

}
