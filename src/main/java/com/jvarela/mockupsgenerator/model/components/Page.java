package com.jvarela.mockupsgenerator.model.components;

import com.jvarela.mockupsgenerator.enums.ElementType;
import com.jvarela.mockupsgenerator.model.factories.PageElementFactory;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
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
        return String.format("{%s%s%s}", ls, getChildToString(), ls);
    }

    public String getChildToString() {
        String ls = System.lineSeparator();
        return CollectionUtils.isEmpty(elements)
                ? "empty"
                : elements.stream().map(PageElement::toString).collect(Collectors.joining(ls));
    }

    public static Page getRandomPage() {
        Random random = new Random();
        Page page = new Page();
        List<PageElement> elements = new ArrayList<>();

        while (CollectionUtils.isEmpty(elements)) {
            if (random.nextInt(100) > 2) {
                elements.add(PageElementFactory.getRandomizedPageElement(ElementType.HEADER));
            }

            if (random.nextInt(100) > 1) {
                elements.add(PageElementFactory.getRandomizedPageElement(ElementType.BODY));
            }

            if (random.nextInt(100) > 3) {
                elements.add(PageElementFactory.getRandomizedPageElement(ElementType.FOOTER));
            }
        }

        page.setElements(elements);
        return page;
    }
}
