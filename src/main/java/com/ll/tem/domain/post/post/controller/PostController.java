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
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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

    @GetMapping
    @ResponseBody
    public String showList() {
        String ul = "<ul>" + posts
                .reversed()
                .stream()
                .map(post -> "<li>%s</li>".formatted(post.getTitle()))
                .collect(Collectors.joining()) + "</ul>";

        String body = """
                <h1>글 목록</h1>
                
                %s
                
                <a href="/posts/write">글쓰기</a>
                """.formatted(ul);

        return body;
    }


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
            PostWriteForm form
    ) {
        return "domain/post/post/write";
    }

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