package com.example.todoapp.repository;

import com.example.todoapp.dto.TodoDto;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class TodoRepository {
    private final Map<Long, TodoDto> storage = new ConcurrentHashMap<>();// Map의 구조 [{key : value},...]
    private Long nextId = 1L ;

}
