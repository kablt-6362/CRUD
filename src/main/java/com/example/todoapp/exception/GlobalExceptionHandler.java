package com.example.todoapp.exception;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@ControllerAdvice //spring지원 어노테이션. 나올수 있는 예외처리 케이스 작성시 상황에 맞게 출력해줌
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public String handleResourceNouFound(
            ResourceNotFoundException e,
            RedirectAttributes redirectAttributes
    ){
        redirectAttributes.addFlashAttribute("message",e.getMessage());
        redirectAttributes.addFlashAttribute("status","error");
        return "redirect:/todos";
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public String handleIllegalArgumentException(
            IllegalArgumentException e,
            RedirectAttributes redirectAttributes
    ){
        redirectAttributes.addFlashAttribute("message",e.getMessage());
        redirectAttributes.addFlashAttribute("status","warning");
        return "redirect:/todos";
    }

    @ExceptionHandler(Exception.class)
    public String handleException(
            Exception e,
            RedirectAttributes redirectAttributes
    ){
        redirectAttributes.addFlashAttribute("message",e.getMessage());
        return "redirect:/todos";

    }

}
