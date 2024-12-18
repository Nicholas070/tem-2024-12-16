package com.ll.tem.domain.post.post.controller;

import com.ll.tem.domain.post.post.entity.Post;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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

    @GetMapping
    public String showList(Model model) {
        model.addAttribute("posts", posts.reversed());
              //여기까지가폴더  / 파일이름
               //folder/folder/folder/file
        return "domain/post/post/list";
    }
//짜잔 이게 갑자기 왜 이렇게 해결된건가요..?와..
    //저렇게 수정해주니 되었네요, 에이치티엠엘파일도 제대로 경로랑 이름수정해주고요
    //강사님은 경로랑 파일을 구분지어 하지않고 파일명에 경로를 쭉입력해서 만들다보면 자동으로 합쳐지게 하시더라구요
    // . 이아니라 / 로 구분지어서 입력하셔야됩니다!
    //아까 제가 . . . 으로 이어서 만든것도 잘못된 방법이에요

    @GetMapping("/{id}")
    public String showDetail(Model model, @PathVariable("id") Integer id) {
        Post post = posts
                .stream()
                .filter(p -> p.getId() == id.longValue())
                .findFirst()
                .orElseThrow();

        model.addAttribute("post", post);

        return "domain/post/post/detail";
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
            PostWriteForm postWriteForm
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