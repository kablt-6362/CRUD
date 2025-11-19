package com.example.todoapp.controller;

import com.example.todoapp.dto.TodoDto;
import com.example.todoapp.repository.TodoRepository;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class todoController {
    // private TodoRepository todoRepository = new TodoRepository();
    private final TodoRepository todoRepository;

    public todoController(TodoRepository todoRepository) {
        this.todoRepository = todoRepository;
    }




    @GetMapping("/todos")
    public String todos(Model model){
    //TodoRepository todoRepository = new TodoRepository();
    // 이전의 만든 repository는 매서드 밖으로 꺼내와서 쓸수가없다.
        // 위의 선언은 다른 저장소의 객체이다.그래서 최상위 매서드에 저장소 객체를 생성하여 사용한다
        List<TodoDto> todos = todoRepository.findAll();
        model.addAttribute("todos",todos);
        return "todos";
    }

    @GetMapping("/todos/new")
    public String newTodo(){
        return "new";
    }

    @GetMapping("/todos/create")
    public String create(@RequestParam String title
            , @RequestParam String content
            , Model model){
        TodoDto todoDto = new TodoDto(null,title,content,false);
        //TodoRepository todoRepository = new TodoRepository();

        TodoDto todo = todoRepository.save(todoDto);

        model.addAttribute("todo",todo);

        //return "create";
        // 바로 todos로 가는 주소로 반환
        return "redirect:/todos";
    }

    @GetMapping("/todos/{id}")
    public String detail(@PathVariable Long id,
                         Model model){
//        TodoDto todo = todoRepository.findById(id);
        try{
            TodoDto todo = todoRepository.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("todo not found!!!!") );
            model.addAttribute("todo",todo);
            return "detail";
        }
        catch (IllegalArgumentException e) {
            return "redirect:/todos";
        }
    }

    @GetMapping("/todos/{id}/delete")
    public String delete(@PathVariable Long id,Model model){
        todoRepository.deleteById(id);
        return "redirect:/todos";
    }

    @GetMapping("/todos/{id}/edit")
    public String edit(@PathVariable Long id,Model model){
        try{
            TodoDto todo = todoRepository.findById(id).orElseThrow(()-> new IllegalArgumentException("todo not Found!!!"));
            model.addAttribute("todo",todo);
            return "edit";
        }catch (IllegalArgumentException e){
        return "redirect:/todos";
        }



    }

    @GetMapping("/todos/{id}/update")
    public String update(@PathVariable Long id
            ,Model model
            ,@RequestParam String title
            ,@RequestParam String content
            ,@RequestParam(defaultValue = (String)"false") Boolean completed)
            //input의 checkbox 속성은 체크를 안할시 아무것도 반화하지 않기에
            //이 매서드의 requestParam의 중 completed가 받을 값이 없어서 문제가 생김
            //defaultValue를 사용하여 반환값이 없을시 false로 반환하도록 지정
    {
        try{
            TodoDto todo = todoRepository.findById(id).orElseThrow();

            todo.setTitle(title);
            todo.setContent(content);
            todo.setCompleted(completed);
            todoRepository.save(todo);
            return "redirect:/todos/"+id;
        } catch (IllegalArgumentException e) {
            return "redirect:/todos";
        }


    }

    @GetMapping("/todos/search")
    public String search(@RequestParam String keyword,Model model){
        List<TodoDto> todos = todoRepository.findByTitleContain(keyword);
        model.addAttribute("todos",todos);
        return "todos";
    }

    @GetMapping("/todos/active")
    public String actice(Model model){
        List<TodoDto> todos = todoRepository.findByCompleted(false);
        model.addAttribute("todos",todos);
        return "todos";
    }

    @GetMapping("/todos/completed")
    public String completed(Model model){
        List<TodoDto> todos = todoRepository.findByCompleted(true);
        model.addAttribute("todos",todos);
        return "todos";
    }

}
