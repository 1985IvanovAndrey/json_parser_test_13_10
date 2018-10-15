package com.example.json.controller;

import com.example.json.service.gson.ParserJsonWithGson;
import org.springframework.beans.factory.annotation.Autowired;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.TreeMap;

@RestController
public class StartController {

    @Autowired
    private ParserJsonWithGson parserJsonWithGson;

    @GetMapping ("/json")// вывод собранного json используя gson
    public Object createJsonWithGson(){
        return parserJsonWithGson.createJson();
    }
    @PostMapping("/get")//вывод разпарсинного json используя gson
    public void get(@RequestBody String json){
        parserJsonWithGson.parserJson(json).forEach((k,v)->
                System.out.println(k+"--"+v));
    }
}

