package com.example.todoapp.service;

import com.example.todoapp.dto.TodoDto;
import com.example.todoapp.repository.TodoRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TodoService {

    private final TodoRepository todoRepository;

    public TodoService(TodoRepository todoRepository) {
        this.todoRepository = todoRepository;
    }

    public List<TodoDto> getAllTodos(){
        return todoRepository.findAll();
    }

    public TodoDto getTodoById(Long id){
        return todoRepository.findById(id)
                .orElseThrow(
                        ()->new IllegalArgumentException("not found todo : id"+id));
    }

    public void deleteTodoById(Long id){
        getTodoById(id);
        todoRepository.deleteById(id);
    }

    public TodoDto updateTodoById(Long id,TodoDto newTodo){
        TodoDto originTodo = getTodoById(id);

        vaildateTitle(newTodo.getTitle());

        originTodo.setTitle(newTodo.getTitle());
        originTodo.setContent(newTodo.getContent());
        originTodo.setCompleted(newTodo.isCompleted());

        return todoRepository.save(originTodo);

    }

    public TodoDto createTodoDto(TodoDto todo){
        vaildateTitle(todo.getTitle());
        return todoRepository.save(todo);
    }

    public List<TodoDto> searchTodos(String keyword){
        return todoRepository.findByTitleContain(keyword);
    }

    public List<TodoDto> getTodosByCompleted(Boolean completed){
        return todoRepository.findByCompleted(completed);
    }

    public TodoDto toggleCompleted(Long id){
        TodoDto todo = getTodoById(id);
        todo.setCompleted(!todo.isCompleted());
        return todoRepository.save(todo);
    }

    private void vaildateTitle(String title){
        if(title == null || title.trim().isEmpty()){
            throw new IllegalArgumentException("제목은 필수입니다");
        }
        if(title.length() > 50){
            throw new IllegalArgumentException("제목은 50자를 넘을수 없습니다.");
        }
    }

    public long gettotalCount(){
        return todoRepository.findAll().size();
    }
    public long getActiveCount(){
        return todoRepository.findByCompleted(false).size();
    }
    public long getCompletedCount(){
        return todoRepository.findByCompleted(true).size();
    }

    public void deleteCompletedTodos(){

        todoRepository.deleteCompleted();
    }

    // 제목 검증. 시도한거
    public void TodoTitleCheck(Long id){
        TodoDto todo = getTodoById(id);
        String title = todo.getTitle();
        if(title != null){
            if(title.length() <= 50){
                System.out.println("사용가능 Title");
            }
        }
    }

    // 통계 기능. 시도한거
    public List<Integer> Total(){
        int total = todoRepository.findAll().size();
        int active = todoRepository.findByCompleted(false).size();
        int completed = todoRepository.findByCompleted(true).size();

        List<Integer> totalList = new ArrayList<>();
        totalList.add(total);
        totalList.add(active);
        totalList.add(completed);

        return totalList;
    }


}
