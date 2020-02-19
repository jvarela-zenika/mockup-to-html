package com.jvarela.mockupsgenerator.controllers;

import com.jvarela.mockupsgenerator.services.GeneratorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class PageGeneratorResource {

    @Autowired
    private GeneratorService pageGeneratorService;

    @GetMapping("/page-generator")
    public String generatePage(){
        return pageGeneratorService.generatePage().toString();
    }
}
