package com.backend.domain.post.post.controller;

import com.backend.domain.post.post.entity.Post;
import com.backend.domain.post.post.service.PostService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;



    //입력값을 모아서 객체로 사용하기위한 역할
    @AllArgsConstructor
    @Getter
    public static class PostWriteForm{
        @NotBlank(message = "1-제목을 입력해주세요")
        @Size(min = 1, max = 10, message = "2-제목은 2글자 이상, 10글자 이하로 입력해주세요")
        private String title;

        @NotBlank(message = "3-내용을 입력해주세요")
        @Size(min = 1, max = 100, message = "4-내용은 2글자 이상 100글자 이하로 입력해주세요")
        private String content;
    }


    @GetMapping("/posts/write")
    public String write(@ModelAttribute("postWriteForm") PostWriteForm form){
        return "post/write"; // post는 template하위의 write.html이 들어있는 폴더명
    }

    //POST 요청으로 받는다
    @PostMapping("/posts/doWrite")
    public String doWrite(
            //다음은 프레임워크 규칙임
            //@ModelAttribute("")은 생략 가능 , 객체들은 자동으로 model에 담긴다는듯
            //@ModelAttribute를 이용해서 이름 재정의 안할 시 : PostWriteform 클래스이름의 첫번째 단어를 소문자로 바꿔서 매개값으로 사용
            @Valid @ModelAttribute("postWriteForm") PostWriteForm form // PostWriteForm 클래스 형태로 변환해서 받겠다
            , BindingResult bindingResult//보고서 : 이걸 도입하면 내부적으로 체크하되 문제가 발생해도 일단 넘어감
            , Model model
    ){
        //보고서 사용법
        if(bindingResult.hasErrors()){
            return "post/write";
        }

        Post post = postService.write(form.title, form.content);
        model.addAttribute("id", post.getId());
        return "post/writeDone";
    }
}
