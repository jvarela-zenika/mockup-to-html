package com.jvarela.mockupsgenerator.services.dto;

import com.jvarela.mockupsgenerator.model.components.ElementNode;
import lombok.Builder;
import lombok.Data;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
public class BodyRow {
    private int xCapacity;
    private List<ElementNode> nodes;

    public BodyRow(int xCapacity, List<ElementNode> nodes) {
        this.xCapacity = xCapacity;
        this.nodes = CollectionUtils.isEmpty(nodes) ? new ArrayList<>() : nodes;
    }
}
