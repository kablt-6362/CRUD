package com.example.todoapp.service;

import com.example.todoapp.dto.TodoDto;
import com.example.todoapp.entity.TodoEntity;
import com.example.todoapp.exception.ResourceNotFoundException;
import com.example.todoapp.repository.TodoRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional // 매서드를 하나의 일처리로 생각하고 매서드안의 내용을 transaction기능을 부여한다
public class TodoService {

    private final TodoRepository todoRepository;

    public TodoService(TodoRepository todoRepository) {
        this.todoRepository = todoRepository;
    }

    public TodoDto createTodoDto(TodoDto dto){
        vaildateTitle(dto.getTitle());
        //저장소에 저장하기위해 TodoEntity로 객체 생성 후 dto값 생성자에 대입
        TodoEntity entity = new TodoEntity(dto.getTitle(), dto.getContent(),dto.isCompleted());
        // 저장소에 추가 후 TodoEntity 형태로 반환
        TodoEntity saved = todoRepository.save(entity);


        return toDto(saved);
    }

    private TodoEntity findEntityById(Long id){
        return todoRepository.findById(id)
                .orElseThrow(
                        ()->new ResourceNotFoundException("not found todo : id"+id));
    }

    public TodoDto getTodoById(Long id){
        TodoEntity entity = findEntityById(id);
        return toDto(entity);
    }


    public List<TodoDto> getAllTodos(){
        return todoRepository.findAll().stream().map(this::toDto)
                .collect(Collectors.toList());
    }


    public void deleteTodoById(Long id){
        //getTodoById(id);
        findEntityById(id);
        todoRepository.deleteById(id);
    }

    public TodoDto updateTodoById(Long id,TodoDto dto){
        vaildateTitle(dto.getTitle());

        TodoEntity entity = findEntityById(id);

        entity.setTitle(dto.getTitle());
        entity.setContent(dto.getContent());
        entity.setCompleted(dto.isCompleted());

        return toDto(entity);
        //save안쓴 이유: transaction설정시 매서드 실행완료 후 자동 저장하기때문
    }


    public List<TodoDto> searchTodos(String keyword){
        return todoRepository.findByTitleContaining(keyword).stream().map(this::toDto).collect(Collectors.toList());
    }

    public List<TodoDto> getTodosByCompleted(Boolean completed){
        return todoRepository.findByCompleted(completed).stream().map(this::toDto).collect(Collectors.toList());
    }

    public TodoDto toggleCompleted(Long id){
        TodoEntity entity = findEntityById(id);
        entity.setCompleted(!entity.isCompleted());
        return toDto(entity);
    }

    //제목검증 매서드
    private void vaildateTitle(String title){
        if(title == null || title.trim().isEmpty()){
            throw new IllegalArgumentException("제목은 필수입니다");
        }
        if(title.length() > 50){
            throw new IllegalArgumentException("제목은 50자를 넘을수 없습니다.");
        }
    }

    //전체,완료,미완료 수
    public long gettotalCount(){
        return todoRepository.findAll().size();
    }
    public long getActiveCount(){
        return todoRepository.findByCompleted(false).size();
    }
    public long getCompletedCount(){
        return todoRepository.findByCompleted(true).size();
    }

    //완료된일 모두 삭제
    public void deleteCompletedTodos(){

        todoRepository.deleteByCompleted(true);
    }

//    // 제목 검증. 시도한거
//    public void TodoTitleCheck(Long id){
//        TodoDto todo = getTodoById(id);
//        String title = todo.getTitle();
//        if(title != null){
//            if(title.length() <= 50){
//                System.out.println("사용가능 Title");
//            }
//        }
//    }
//
//    // 통계 기능. 시도한거
//    public List<Integer> Total(){
//        int total = todoRepository.findAll().size();
//        int active = todoRepository.findByCompleted(false).size();
//        int completed = todoRepository.findByCompleted(true).size();
//
//        List<Integer> totalList = new ArrayList<>();
//        totalList.add(total);
//        totalList.add(active);
//        totalList.add(completed);
//
//        return totalList;
//    }

    private TodoDto toDto(TodoEntity entity){
        TodoDto dto = new TodoDto();
        dto.setId(entity.getId());
        dto.setContent(entity.getContent());
        dto.setTitle(entity.getTitle());
        dto.setCompleted(entity.isCompleted());
        return dto;
    }


}
