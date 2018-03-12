package com.eric.druid.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

@RestController
public class IndexController {

    @RequestMapping("/")
    public String homeJavaEight() {
        List<String> helloWorld = Arrays.asList("Hello, ", "Welcome", "To");
        List<String> javaEight = Arrays.asList("Eric's", "Project!");
        Stream<String> helloStreams = Stream.concat(helloWorld.stream(), javaEight.stream());
        StringBuilder sb = new StringBuilder();
        helloStreams.forEach(s -> sb.append(s + " "));
        return sb.toString() ;
    }
}
