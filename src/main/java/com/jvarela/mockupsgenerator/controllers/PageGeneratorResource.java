package com.jvarela.mockupsgenerator.controllers;

import com.jvarela.mockupsgenerator.services.GeneratorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PageGeneratorResource {

    @Autowired
    private GeneratorService pageGeneratorService;

    @GetMapping("/generator/page")
    public String generatePage(){
        return pageGeneratorService.generatePage().toString();
    }
}
