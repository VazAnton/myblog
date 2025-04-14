package ru.yandex.practicum.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.dto.CommentInputDto;
import ru.yandex.practicum.dto.PostInputDto;
import ru.yandex.practicum.dto.PostOutputDto;
import ru.yandex.practicum.model.Paging;
import ru.yandex.practicum.service.CommentService;
import ru.yandex.practicum.service.PostService;

import java.util.List;

@Controller
@RequestMapping(path = "/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;
    private final CommentService commentService;

    @GetMapping(path = "/add")
    @ResponseStatus(HttpStatus.CREATED)
    public String addPost(@RequestBody PostInputDto postInput) {
        postService.addPost(postInput);
        return "add-post";
    }

    @GetMapping(path = "/{postId}/edit")
    public String updatePost(@RequestBody PostInputDto postInput,
                             @PathVariable long postId,
                             Model model) {
        postService.updatePost(postInput, postId);
        PostOutputDto postOutputDto = postService.getPost(postId);
        model.addAttribute("post", postOutputDto);
        return "add-post";
    }

    @GetMapping
    @ResponseStatus(HttpStatus.FOUND)
    public String getPostsWithParameters(@RequestParam(required = false) String search,
                                         @RequestParam(defaultValue = "0") Long pageNumber,
                                         @RequestParam(defaultValue = "10") Long pageSize,
                                         Model model) {
        List<PostOutputDto> posts = postService.getPostsWithParameters(search, pageNumber, pageSize);
        model.addAttribute("posts", posts);
        model.addAttribute("search", search);
        model.addAttribute("paging", new Paging(pageNumber, pageSize, postService.countAllPosts()));
        return "posts";
    }

    @GetMapping(path = "/{postId}")
    @ResponseStatus(HttpStatus.FOUND)
    public String getPost(@PathVariable long postId,
                          Model model) {
        PostOutputDto postOutputDto = postService.getPost(postId);
        model.addAttribute("post", postOutputDto);
        return "post";
    }

    @GetMapping(path = "/images/{postId}")
    public long getSizeOfImage(long postId) {
        return postService.getSizeOfImage(postId);
    } //е) GET "/images/{id}"

    @PostMapping(path = "/{postId}/like")
    public String likePost(@PathVariable long postId,
                           @RequestParam boolean like,
                           Model model) {
        postService.likePost(postId, like);
        PostOutputDto postOutputDto = postService.getPost(postId);
        model.addAttribute("post", postOutputDto);
        return "redirect:/posts/{postId}";
    }

    @PostMapping(path = "/{postId}/delete")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public String deletePost(@PathVariable long postId,
                             Model model,
                             @RequestParam(defaultValue = "0") Long pageNumber,
                             @RequestParam(defaultValue = "10") Long pageSize) {
        PostOutputDto postOutputDto = postService.getPost(postId);
        model.addAttribute("post", postOutputDto);
        model.addAttribute("paging", new Paging(pageNumber, pageSize, postService.countAllPosts()));
        postService.deletePost(postId);
        return "posts";
    }

    @PostMapping(path = "/{postId}/comments")
    @ResponseStatus(HttpStatus.CREATED)
    public String addComment(@RequestBody CommentInputDto commentInput,
                             @PathVariable long postId,
                             Model model) {
        commentService.addComment(commentInput, postId);
        PostOutputDto postOutputDto = postService.getPost(postId);
        model.addAttribute("post", postOutputDto);
        return "redirect:/posts/{postId}";
    }

    @PostMapping(path = "/{postId}/comments/{commentId}")
    public String updateComment(@RequestBody CommentInputDto commentInput,
                                @PathVariable long commentId,
                                @PathVariable long postId,
                                Model model) {
        commentService.updateComment(commentInput, commentId, postId);
        PostOutputDto postOutputDto = postService.getPost(postId);
        model.addAttribute("post", postOutputDto);
        return "redirect:/posts/{postId}";
    }

    @PostMapping(path = "/{postId}/comments/{commentId}/delete")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public String deleteComment(@PathVariable long commentId,
                                @PathVariable long postId,
                                Model model) {
        PostOutputDto postOutputDto = postService.getPost(postId);
        model.addAttribute("post", postOutputDto);
        commentService.deleteComment(commentId, postId);
        return "redirect:/posts/{postId}";
    }
}
