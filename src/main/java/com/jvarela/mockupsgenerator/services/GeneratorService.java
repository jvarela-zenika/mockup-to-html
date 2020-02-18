package com.jvarela.mockupsgenerator.services;

import com.jvarela.mockupsgenerator.model.components.Page;
import org.springframework.stereotype.Service;

@Service
public class GeneratorService {
    public Page generatePage() {
        return Page.getRandomPage();
    }
}
