package com.example.todoapp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class todoController {
    @GetMapping("/todos")
    public String todos(){

        return "todos";
    }

}
