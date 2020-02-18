package com.jvarela.mockupsgenerator.model.factories;

import com.jvarela.mockupsgenerator.enums.ElementType;
import com.jvarela.mockupsgenerator.model.components.NodeComponent;
import com.jvarela.mockupsgenerator.model.components.PageElement;
import com.jvarela.mockupsgenerator.model.components.ElementNode;
import lombok.experimental.UtilityClass;

import java.util.*;

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
                    .xCapacity(4)
                    .yCapacity(5)
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
            elements.put(ElementType.FOOTER,element);
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

        PageElement body = getBodyComponent();

        List<ElementNode> bodyNodes = new ArrayList<>();
        int currentXCapacity = body.getXCapacity();
        int currentYCapacity = body.getYCapacity();
        Random random = new Random();

        while (currentXCapacity >= 1 && currentYCapacity >= 1) {

            int xCapacity = getRandomCapacity(currentXCapacity);
            int yCapacity = getRandomCapacity(currentYCapacity);

            ElementNode node;

            if (random.nextBoolean()) {
                node = ElementNode
                        .getRandomNodeWithCapacityBetweenInclusive(1, 1, xCapacity, yCapacity);
            } else {
                node = ElementNode.builder().xCapacity(xCapacity).yCapacity(yCapacity).build();
            }

            currentXCapacity -= node.getXCapacity();
            currentYCapacity -= node.getYCapacity();

            bodyNodes.add(node);
        }
        body.setNodes(bodyNodes);
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
        int headerCapacity = header.getXCapacity();

        if (random.nextBoolean()) {
            NodeComponent imageComponent = NodeComponentFactory.getImageComponent();
            imageComponent.randomizePosition();

            ElementNode imageNode = ElementNode.builder()
                    .xCapacity(random.nextInt(3))
                    .yCapacity(1)
                    .components(Collections.singletonList(imageComponent))
                    .build();

            headerCapacity -= imageNode.getXCapacity();
            headerNodes.add(imageNode);

        }

        if (random.nextBoolean()) {
            ElementNode linkNode = ElementNode.builder()
                    .xCapacity(random.nextInt(5))
                    .yCapacity(1)
                    .build();

            if (linkNode.getXCapacity() == 3) {
                linkNode.setXCapacity(2);
            }

            for (int i = 0; i < random.nextInt((linkNode.getXCapacity() * 2) + 1); i++) {
                if (random.nextBoolean()) {
                    NodeComponent linkComponent = NodeComponentFactory.getLinkComponent();
                    linkComponent.randomizePosition();
                    linkNode.getComponents().add(linkComponent);
                }
            }
            headerCapacity -= linkNode.getXCapacity();
            headerNodes.add(linkNode);
        }

        while (headerCapacity > 0) {
            int capacity = random.nextInt(headerCapacity) + 1;
            if (capacity == 3) {
                capacity = 2;
            } else if (capacity > 4) {
                capacity = 4;
            }
            ElementNode randomEmptyNode = ElementNode.builder()
                    .xCapacity(capacity)
                    .yCapacity(1)
                    .build();

            headerCapacity -= randomEmptyNode.getXCapacity();

            headerNodes.add(randomEmptyNode);
        }

        Collections.shuffle(headerNodes);
        header.setNodes(headerNodes);
        return header;
    }
}
