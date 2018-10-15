package com.example.json.service.jackson;

import com.fasterxml.jackson.databind.ObjectMapper;


import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


public class MainJackson {


    public static void main(String[] args) throws IOException {

        ParserJsonWithJackson parserJsonWithJackson = new ParserJsonWithJackson();
        parserJsonWithJackson.parserJson(parserJsonWithJackson.json).forEach((k, v) ->
                System.out.println(k + "--" + v));

    }
}




