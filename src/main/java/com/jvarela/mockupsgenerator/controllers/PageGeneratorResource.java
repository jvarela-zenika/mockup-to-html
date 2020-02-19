package com.jvarela.mockupsgenerator.controllers;

import com.jvarela.mockupsgenerator.model.components.Page;
import com.jvarela.mockupsgenerator.services.GeneratorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class PageGeneratorResource {

    @Autowired
    private GeneratorService pageGeneratorService;

    @CrossOrigin
    @GetMapping("/page-generator")
        public Page generatePage(){
        return pageGeneratorService.generatePage();
    }
}
