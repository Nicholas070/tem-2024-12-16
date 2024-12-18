package com.ll.tem.domain.post.post.controller;

import com.ll.tem.domain.post.post.entity.Post;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/posts")
public class PostController {
    private List<Post> posts = new ArrayList<>() {{
        add(
                Post.builder()
                        .title("제목1")
                        .content("내용1")
                        .build()
        );

        add(
                Post.builder()
                        .title("제목2")
                        .content("내용2")
                        .build()
        );

        add(
                Post.builder()
                        .title("제목3")
                        .content("내용3")
                        .build()
        );
    }};
//인텔리제이 설정화면한번열어주시겠어요
    @GetMapping
    public String showList(Model model) {
        model.addAttribute("posts", posts.reversed());

        return "domain/post/post/list";
    }


//    @GetMapping("/{id}")
//    public String showDetail(Model model, @PathVariable("id") Integer id) {
//        Post post = posts
//                .stream()
//                .filter(p -> p.getId() == id.longValue())
//                .findFirst()
//                .orElseThrow();
//
//        model.addAttribute("post", post);
//
//        return "domain/post/post/detail";
//    }


    private record PostWriteForm(
            @NotBlank(message = "01-제목을 입력해주세요.")
            @Length(min = 2, message = "02-제목을 2자 이상 입력해주세요.")
            String title,
            @NotBlank(message = "03-내용을 입력해주세요.")
            @Length(min = 2, message = "04-내용을 2자 이상 입력해주세요.")
            String content
    ) {
    }

    @GetMapping("/write")
    public String showWrite(
    ) {
       return "test";
    }//잠시 쉬고계시지요... 혼지서 하시려구요..? ㅎ...
    //이거 그대로 커밋해서 올려주세요 제 컴퓨터에서 테스트해보겠습니다. 아 제 주소에 그대로 커밋해볼까요? 네네 넵

    @PostMapping("/write")
    public String write(
            @Valid PostWriteForm form,
            BindingResult bindingResult,
            Model model
    ) {
        if (bindingResult.hasErrors()) {
            return "domain/post/post/write";
        }

        posts.add(
                Post.builder()
                        .title(form.title)
                        .content(form.content)
                        .build()
        );

        return "redirect:/posts";
    }
}