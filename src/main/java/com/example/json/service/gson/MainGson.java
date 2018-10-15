package com.example.json.service.gson;

public class MainGson {

    public static void main(String[] args) {

        ParserJsonWithGson parserJsonWithGson = new ParserJsonWithGson();

        parserJsonWithGson.parserJson(parserJsonWithGson.json).forEach((k, v) ->
                System.out.println(k + "--" + v));

    }
}
