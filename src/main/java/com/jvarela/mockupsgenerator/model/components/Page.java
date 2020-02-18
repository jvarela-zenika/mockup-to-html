package com.jvarela.mockupsgenerator.model.components;

import com.jvarela.mockupsgenerator.enums.ElementType;
import com.jvarela.mockupsgenerator.model.factories.PageElementFactory;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Page {
    private List<PageElement> elements = new ArrayList<>();

    @Override
    public String toString() {
        String ls = System.lineSeparator();
        return String.format(
                "<START>%s{%s%s%s}%s<END>",
                ls,
                ls,
                elements.stream().map(PageElement::toString).collect(Collectors.joining(ls)),
                ls,
                ls
        );
    }

    public static Page getRandomPage(){
        Random random = new Random();
        Page page = new Page();
        List<PageElement> elements = new ArrayList<>();

        if (random.nextBoolean()) {
            elements.add(PageElementFactory.getRandomizedPageElement(ElementType.HEADER));
        }

        if (random.nextInt(10) > 3) {
            elements.add(PageElementFactory.getRandomizedPageElement(ElementType.BODY));
        }

        if (random.nextBoolean()) {
            elements.add(PageElementFactory.getRandomizedPageElement(ElementType.FOOTER));
        }

        page.setElements(elements);
        return page;
    }
}
