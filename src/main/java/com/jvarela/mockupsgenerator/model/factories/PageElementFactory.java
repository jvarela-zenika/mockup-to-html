package com.jvarela.mockupsgenerator.model.factories;

import com.jvarela.mockupsgenerator.enums.ElementType;
import com.jvarela.mockupsgenerator.model.components.ElementNode;
import com.jvarela.mockupsgenerator.model.components.NodeComponent;
import com.jvarela.mockupsgenerator.model.components.PageElement;
import com.jvarela.mockupsgenerator.services.dto.BodyRow;
import lombok.experimental.UtilityClass;

import java.util.*;
import java.util.stream.Collectors;

@UtilityClass
public class PageElementFactory {

    private static Map<ElementType, PageElement> elements = new HashMap<>();

    public PageElement getHeaderComponent() {
        PageElement element = elements.get(ElementType.HEADER);
        if (element == null) {
            element = PageElement.builder()
                    .type(ElementType.HEADER)
                    .xCapacity(1)
                    .yCapacity(6)
                    .build();
            elements.put(ElementType.HEADER, element);
        }
        return element;
    }

    public PageElement getBodyComponent() {
        PageElement element = elements.get(ElementType.BODY);
        if (element == null) {
            element = PageElement.builder()
                    .type(ElementType.BODY)
                    .xCapacity(5)
                    .yCapacity(4)
                    .build();
            elements.put(ElementType.BODY, element);
        }
        return element;
    }

    public PageElement getFooterComponent() {
        PageElement element = elements.get(ElementType.FOOTER);
        if (element == null) {
            element = PageElement.builder()
                    .type(ElementType.FOOTER)
                    .xCapacity(0)
                    .yCapacity(0)
                    .build();
            elements.put(ElementType.FOOTER, element);
        }
        return element;
    }

    public PageElement getRandomizedPageElement(ElementType pageElementType) {

        if (pageElementType == ElementType.FOOTER) {
            return getFooterComponent();
        }

        if (pageElementType == ElementType.HEADER) {
            return getRandomizedHeaderElement();
        }

        return getRandomizedBodyElement();
    }

    private static PageElement getRandomizedBodyElement() {
        PageElement body = getBodyComponent();

        int currentXCapacity = body.getXCapacity();

        List<BodyRow> rows = new ArrayList<>();

        while (currentXCapacity > 0) {
            BodyRow row = BodyRow.builder()
                    .xCapacity(getRandomCapacity(currentXCapacity))
                    .build();

            currentXCapacity -= row.getXCapacity();

            rows.add(row);
        }

        Random random = new Random();

        rows.forEach(row -> {
            int currentYCapacity = body.getYCapacity();

            while (currentYCapacity > 0) {
                ElementNode node;
                if (random.nextInt(10) > 2) {
                    if (currentYCapacity == 4 && random.nextInt(10) > 3) {
                        NodeComponent titleComponent = NodeComponentFactory.getTitleComponent();
                        titleComponent.randomizePosition(null);
                        node = ElementNode.builder()
                                .xCapacity(row.getXCapacity())
                                .yCapacity(currentYCapacity)
                                .components(Collections.singletonList(titleComponent))
                                .build();
                        node.fillWithRandomComponents();
                    } else {
                        node = ElementNode
                                .getRandomNodeWithCapacityBetweenInclusive(row.getXCapacity(), 1, row.getXCapacity(), currentYCapacity);
                    }
                } else {
                    node = ElementNode.builder()
                            .xCapacity(row.getXCapacity())
                            .yCapacity(getRandomCapacity(currentYCapacity)).build();
                }

                currentYCapacity -= node.getYCapacity();
                row.getNodes().add(node);
            }
        });

        Collections.shuffle(rows);

        body.setNodes(
                rows.stream()
                        .map(BodyRow::getNodes)
                        .flatMap(Collection::stream)
                        .collect(Collectors.toList())
        );

        return body;
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


    private static PageElement getRandomizedHeaderElement() {
        PageElement header = getHeaderComponent();
        Random random = new Random();
        List<ElementNode> headerNodes = new ArrayList<>();
        int headerCapacity = header.getYCapacity();

        if (random.nextBoolean()) {
            NodeComponent imageComponent = NodeComponentFactory.getImageComponent();
            imageComponent.randomizePosition(null);

            ElementNode imageNode = ElementNode.builder()
                    .xCapacity(1)
                    .yCapacity(random.nextInt(2) + 1)
                    .components(Collections.singletonList(imageComponent))
                    .build();

            headerCapacity -= imageNode.getYCapacity();
            headerNodes.add(imageNode);
        }

        while (headerCapacity > 0) {
            int capacity = getRandomCapacity(headerCapacity);

            if (random.nextBoolean()) {
                ElementNode linkNode = ElementNode.builder()
                        .xCapacity(1)
                        .yCapacity(getRandomCapacity(capacity))
                        .build();
                int numberOfLinks = random.nextInt((linkNode.getYCapacity() * 2) + 1);
                for (int i = 0; i < numberOfLinks; i++) {
                    if (random.nextBoolean()) {
                        NodeComponent linkComponent = NodeComponentFactory.getLinkComponent();
                        linkComponent.randomizePosition(linkNode);
                        linkNode.getComponents().add(linkComponent);
                    }
                }
                headerCapacity -= linkNode.getYCapacity();
                headerNodes.add(linkNode);
            } else {
                ElementNode randomEmptyNode = ElementNode.builder()
                        .xCapacity(1)
                        .yCapacity(capacity)
                        .build();

                headerCapacity -= randomEmptyNode.getYCapacity();

                headerNodes.add(randomEmptyNode);
            }
        }

        Collections.shuffle(headerNodes);
        header.setNodes(headerNodes);
        return header;
    }
}
