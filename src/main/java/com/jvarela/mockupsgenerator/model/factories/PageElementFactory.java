package com.jvarela.mockupsgenerator.model.factories;

import com.jvarela.mockupsgenerator.enums.ElementType;
import com.jvarela.mockupsgenerator.model.components.ElementNode;
import com.jvarela.mockupsgenerator.model.components.NodeComponent;
import com.jvarela.mockupsgenerator.model.components.PageElement;
import com.jvarela.mockupsgenerator.services.dto.BodyRow;
import lombok.experimental.UtilityClass;
import org.springframework.util.CollectionUtils;

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
                if (random.nextInt(100) > 12) {
                    node = ElementNode
                            .getRandomNodeWithCapacityBetweenInclusive(
                                    row.getXCapacity(),
                                    1,
                                    row.getXCapacity(),
                                    currentYCapacity
                            );
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
        rows.forEach(row -> row.setNodes(mergeEmptyNodes(4, row)));
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
        int headerCapacity = header.getYCapacity();
        BodyRow row = BodyRow.builder()
                .xCapacity(1)
                .nodes(new ArrayList<>())
                .build();

        if (random.nextInt(10) > 2) {
            NodeComponent imageComponent = NodeComponentFactory.getImageComponent();
            imageComponent.randomizePosition(null);

            ElementNode imageNode = ElementNode.builder()
                    .xCapacity(1)
                    .yCapacity(random.nextInt(2) + 1)
                    .components(Collections.singletonList(imageComponent))
                    .build();

            headerCapacity -= imageNode.getYCapacity();
            row.getNodes().add(imageNode);
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
                    if (random.nextInt(10) > 2) {
                        NodeComponent linkComponent = NodeComponentFactory.getLinkComponent();
                        linkComponent.randomizePosition(linkNode);
                        linkNode.getComponents().add(linkComponent);
                    }
                }
                headerCapacity -= linkNode.getYCapacity();
                row.getNodes().add(linkNode);
            } else {
                ElementNode randomEmptyNode = ElementNode.builder()
                        .xCapacity(1)
                        .yCapacity(capacity)
                        .build();
                headerCapacity -= randomEmptyNode.getYCapacity();
                row.getNodes().add(randomEmptyNode);
            }
        }

        Collections.shuffle(row.getNodes());
        row.setNodes(mergeEmptyNodes(6, row));
        header.setNodes(row.getNodes());
        return header;
    }


    public List<ElementNode> mergeEmptyNodes(int yCapacity, BodyRow row) {
        List<ElementNode> resultList = new ArrayList<>();
        List<ElementNode> workingList = new ArrayList<>(row.getNodes());
        List<ElementNode> blackList = new ArrayList<>();
        int currentXCapacity;
        int currentYCapacity;
        boolean isNotFinishedMerge = true;

        while (isNotFinishedMerge) {
            isNotFinishedMerge = false;

            for (int i = 0; i < workingList.size(); i++) {
                ElementNode currentNode = workingList.get(i);

                if (blackList.contains(currentNode)) {
                    continue;
                }

                if (
                        !CollectionUtils.isEmpty(currentNode.getComponents())
                                || i == workingList.size() - 1
                                || !CollectionUtils.isEmpty(workingList.get(i + 1).getComponents()
                        )
                ) {
                    resultList.add(currentNode);
                    continue;
                }

                ElementNode nextNode = workingList.get(i + 1);

                currentXCapacity = getCurrentXCapacity(row, resultList);
                currentYCapacity = getCurrentYCapacity(yCapacity, resultList);

                int xMergedSpace = currentNode.getXCapacity() + nextNode.getXCapacity();
                xMergedSpace = getCapacity(xMergedSpace, currentXCapacity);
                int yMergedSpace = currentNode.getYCapacity() + nextNode.getYCapacity();
                yMergedSpace = getCapacity(yMergedSpace, currentYCapacity);

                if (xMergedSpace <= currentXCapacity && currentNode.getYCapacity() == nextNode.getYCapacity()) {
                    currentNode.setXCapacity(xMergedSpace);
                    resultList.add(currentNode);
                    blackList.add(nextNode);
                    isNotFinishedMerge = true;
                } else if (yMergedSpace <= currentYCapacity && currentNode.getXCapacity() == nextNode.getXCapacity()) {
                    currentNode.setYCapacity(yMergedSpace);
                    resultList.add(currentNode);
                    blackList.add(nextNode);
                    isNotFinishedMerge = true;
                } else {
                    resultList.add(currentNode);
                }
            }

            if (isNotFinishedMerge) {
                workingList.clear();
                workingList.addAll(new ArrayList<>(resultList));
                resultList.clear();
                blackList.clear();
            }

        }

        currentXCapacity = getCurrentXCapacity(row, resultList);
        currentYCapacity = getCurrentYCapacity(yCapacity, resultList);

        if (currentXCapacity > 0 && currentYCapacity > 0) {
            resultList.add(
                    ElementNode.builder().xCapacity(currentXCapacity).yCapacity(currentYCapacity).build());
        }

        return resultList;
    }

    private static int getCurrentYCapacity(int yCapacity, List<ElementNode> resultList) {
        return yCapacity - resultList
                .stream()
                .map(ElementNode::getYCapacity)
                .reduce(Integer::sum)
                .orElse(0);
    }

    private static int getCurrentXCapacity(BodyRow row, List<ElementNode> resultList) {
        return (
                resultList
                        .stream()
                        .map(ElementNode::getXCapacity)
                        .reduce(Integer::sum)
                        .orElse(0) % row.getXCapacity()
        ) == 0
                ? row.getXCapacity()
                : 1;
    }

    public int getCapacity(int capacity, int maxCapacity) {
        if (capacity > 4) {
            return 4;
        }
        return capacity == 3 ? maxCapacity : capacity;
    }
}
