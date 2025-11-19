package com.example.todoapp.repository;

import com.example.todoapp.dto.TodoDto;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class TodoRepository {
    private final Map<Long, TodoDto> storage = new ConcurrentHashMap<>();// Map의 구조 [{key : value},...]
    private Long nextId = 1L ;

    public TodoDto save(TodoDto todo){
        if(todo.getId() == null){
        todo.setId(nextId++);
        }
        storage.put(todo.getId(),todo);
        return todo;
    }

    public List<TodoDto> findAll(){
        return new ArrayList<>(storage.values());
    }

    public Optional<TodoDto> findById(Long id){
//        return  storage.get(id);
        return Optional.ofNullable(storage.get(id));

    }

    public void deleteById(Long id){
        storage.remove(id);
    }

    public List<TodoDto> findByTitleContain(String keyword){

        return storage.values().stream()
                .filter((todo)-> todo.getTitle().contains(keyword)).toList();
    }

    public List<TodoDto> findByCompleted(Boolean completed){
        return storage.values().stream()
                .filter(todo->todo.isCompleted() == completed).toList();
    }


}
