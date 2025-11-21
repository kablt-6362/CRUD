package com.example.todoapp.entity;

import jakarta.persistence.*;

@Entity
@Table(name="todos") //생성할 테이블 이름 설정 가능
public class TodoEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private  Long id;

    private String title;
    private String content;
    private boolean completed;

    public TodoEntity() {

    }

    public TodoEntity(String title, String content, boolean completed) {
        this.title = title;
        this.content = content;
        this.completed = completed;
    }

    public Long getId() {
        return id;
    }

//    public void setId(Long id) {
//        this.id = id;
//    } pk로 자동으로 들어가기 떄문에 setId를 만들지는 않는다.

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }
}
