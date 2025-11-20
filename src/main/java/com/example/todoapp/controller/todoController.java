package com.example.todoapp.controller;

import com.example.todoapp.dto.TodoDto;
import com.example.todoapp.repository.TodoRepository;
import com.example.todoapp.service.TodoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/todos")
public class todoController {
    // private TodoRepository todoRepository = new TodoRepository();
//    private final TodoRepository todoRepository;
//    public todoController(TodoRepository todoRepository) {
//        this.todoRepository = todoRepository;
//    }

    private final TodoService todoService;

    public todoController(TodoService todoService) {
        this.todoService = todoService;
    }

    @GetMapping
    public String todos(Model model){
        List<TodoDto> todos = todoService.getAllTodos();
        model.addAttribute("todos",todos);
        model.addAttribute("totalConut",todoService.gettotalCount());
        model.addAttribute("completedCount",todoService.getCompletedCount());
        model.addAttribute("activeCount",todoService.getActiveCount());
//        List<Integer> list = todoService.Total();
//        model.addAttribute("list",list);
        return "todos";
    }

    @GetMapping("/new")
    public String newTodo(Model model){
        model.addAttribute("todo",new TodoDto());
        return "form";
    }

//    @GetMapping("/create")
    @PostMapping()
    public String create(
            //@RequestParam String title
           // , @RequestParam String content
             RedirectAttributes redirectAttributes,
            @ModelAttribute TodoDto todo
            //Model model
             ){

        try{
            todoService.createTodoDto(todo);
            redirectAttributes.addFlashAttribute("message","할 일이 생성 되었습니다");
            return "redirect:/todos";
        }catch (IllegalArgumentException e){
            redirectAttributes.addFlashAttribute("error",e.getMessage());
            return "redirect:/todos/new";
        }
    }

    @GetMapping("/{id}")
    public String detail(@PathVariable Long id,
                         Model model){
//        TodoDto todo = todoRepository.findById(id);
        try{
//            TodoDto todo = todoRepository.findById(id)
//                    .orElseThrow(() -> new IllegalArgumentException("todo not found!!!!") );
            TodoDto todo = todoService.getTodoById(id);
            model.addAttribute("todo",todo);
            return "detail";
        }
        catch (IllegalArgumentException e) {
            return "redirect:/todos";
        }
    }

    @GetMapping("/{id}/delete")
    public String delete(@PathVariable Long id,Model model,
                         RedirectAttributes redirectAttributes){
//        todoRepository.deleteById(id);
        todoService.deleteTodoById(id);
        redirectAttributes.addFlashAttribute("message","할 일이 삭제되었습니다.");
        redirectAttributes.addFlashAttribute("status","delete");
        return "redirect:/todos";
    }

    @GetMapping("/{id}/update")
    public String edit(@PathVariable Long id,Model model){
        try{
            // TodoDto todo = todoRepository.findById(id).orElseThrow(()-> new IllegalArgumentException("todo not Found!!!"));
            TodoDto todo =  todoService.getTodoById(id);
            model.addAttribute("todo",todo);
            return "form";
        }catch (IllegalArgumentException e){
        return "redirect:/todos";
        }



    }

    @PostMapping("/{id}/update")
    public String update(@PathVariable Long id
            ,Model model
            ,RedirectAttributes redirectAttributes
            ,@ModelAttribute TodoDto todo
                 )
            //input의 checkbox 속성은 체크를 안할시 아무것도 반화하지 않기에
            //이 매서드의 requestParam의 중 completed가 받을 값이 없어서 문제가 생김
            //defaultValue를 사용하여 반환값이 없을시 false로 반환하도록 지정
    {
        try{
            //TodoDto todo = todoRepository.findById(id).orElseThrow(()-> new IllegalArgumentException("not found todo!"));

//            todo.setTitle(title);
//            todo.setContent(content);
//            todo.setCompleted(completed);
            //todoRepository.save(todo);
            todoService.updateTodoById(id,todo);
            redirectAttributes.addFlashAttribute("message","할 일이 수정되었습니다");

            return "redirect:/todos/"+id;
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("message","없는 할 일 입니다");
            return "redirect:/todos";
        }


    }

    @GetMapping("/search")
    public String search(@RequestParam String keyword,Model model){
        //List<TodoDto> todos = todoRepository.findByTitleContain(keyword);
        List<TodoDto> todos = todoService.searchTodos(keyword);
        model.addAttribute("todos",todos);
        return "todos";
    }

    @GetMapping("/active")
    public String actice(Model model){
        //List<TodoDto> todos = todoRepository.findByCompleted(false);
        List<TodoDto> todos = todoService.getTodosByCompleted(false);
        model.addAttribute("todos",todos);
        return "todos";
    }

    @GetMapping("/completed")
    public String completed(Model model){
//        List<TodoDto> todos = todoRepository.findByCompleted(true);
        List<TodoDto> todos = todoService.getTodosByCompleted(true);
        model.addAttribute("todos",todos);
        return "todos";
    }

    @GetMapping("/{id}/toggle")
    public String toggle(@PathVariable Long id,Model model){
        try{
//            TodoDto todo = todoRepository.findById(id)
//                    .orElseThrow(()->new IllegalArgumentException("todo not found"));
//            todo.setCompleted(!todo.isCompleted());
//            todoRepository.save(todo);
            todoService.toggleCompleted(id);
            return "redirect:/todos/" + id;
        }catch(IllegalArgumentException e){
            return "redirect:/todos";
        }
    }

    @GetMapping("/delete-completed")
    public String deleteCompleted(RedirectAttributes redirectAttributes) {
        // delete all
        todoService.deleteCompletedTodos();
        redirectAttributes.addFlashAttribute("message", "완료된 할일 삭제");
        return "redirect:/todos";
    }


    //1.제목 검증 추가
    // -제목이 비어있으면 예외처리
    // - 제목이 50자 초과시 예외

    // 2. 통계 기능 추가
    // - 전체,완료된, 미완료 할 일 개수 => todos 에 표시

    // 3. 완료된 할일 일괄 삭제



}
