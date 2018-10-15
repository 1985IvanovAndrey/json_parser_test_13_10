package com.example.json.controller;

import com.example.json.service.gson.ParserJsonWithGson;
import com.example.json.service.jackson.ParserJsonWithJackson;
import jdk.nashorn.internal.ir.ObjectNode;
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
    @Autowired
    private ParserJsonWithJackson parserJsonWithJackson;

    @GetMapping ("/json/create/gson")// вывод собранного json используя gson
    public Object createJsonWithGson(){
        return parserJsonWithGson.createJson();
    }
    @PostMapping("/getGson")//вывод раcпарсинного json используя gson
    public void getGson(@RequestBody String json){
        parserJsonWithGson.parserJson(json).forEach((k,v)->
                System.out.println(k+"--"+v));
    }
    @PostMapping("/getJackson")//вывод раcпарсинного json используя jackson
    public void getJackson(@RequestBody String json){
        parserJsonWithJackson.parserJson(json).forEach((k,v)->
                System.out.println(k+"--"+v));
    }
    @GetMapping ("/json/create/jackson")//вывод собранного json используя jackson
    public Object createJsonWithJackson(){
        return parserJsonWithJackson.createJson();
    }
}

