package com.example.todoapp.repository;

import com.example.todoapp.entity.TodoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TodoRepository extends JpaRepository<TodoEntity, Long> {
    List<TodoEntity> findByTitleContain(String keyword);
    List<TodoEntity> findByCompleted(Boolean completed);
    void deletedByCompleted(boolean completed);
    //void deleteCompleted();


}
