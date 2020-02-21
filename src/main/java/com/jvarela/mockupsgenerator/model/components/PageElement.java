package com.jvarela.mockupsgenerator.model.components;

import com.jvarela.mockupsgenerator.enums.ElementType;
import lombok.Builder;
import lombok.Data;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder
public class PageElement {
    private int xCapacity;
    private int yCapacity;
    private ElementType type;
    private List<ElementNode> nodes;

    public PageElement(int xCapacity, int yCapacity, ElementType type, List<ElementNode> nodes) {
        this.xCapacity = xCapacity;
        this.yCapacity = yCapacity;
        this.type = type;
        this.nodes = CollectionUtils.isEmpty(nodes) ? new ArrayList<>() : nodes;
    }

    @Override
    public String toString() {
        String ls = System.lineSeparator();
        return type.name().toLowerCase() + " {" + ls + getChildrenToString() + ls + "}";
    }

    private String getChildrenToString() {
        String ls = System.lineSeparator();

        return CollectionUtils.isEmpty(nodes) ?
                ""
                : nodes.stream().map(ElementNode::toString).collect(Collectors.joining(ls));
    }
}
