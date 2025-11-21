package com.example.todoapp.repository;

import com.example.todoapp.entity.TodoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TodoRepository extends JpaRepository<TodoEntity, Long> {
    List<TodoEntity> findByTitleContaining(String title);
    List<TodoEntity> findByCompleted(Boolean completed);
    void deleteByCompleted(boolean completed);
    //void deleteCompleted();


}
