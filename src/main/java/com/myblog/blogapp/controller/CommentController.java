package com.myblog.blogapp.controller;

import com.myblog.blogapp.payload.CommentDto;
import com.myblog.blogapp.service.CommentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/")
public class CommentController {

    private CommentService commentService;

    public CommentController(CommentService commentService) {            //act as @Autowired
        this.commentService = commentService;
    }

    @PostMapping("posts/{postId}/comments")                               //CREATE: Comment is created(JSON to Java in db)
    public ResponseEntity<CommentDto> createComment(@PathVariable("postId") long postId, @RequestBody CommentDto commentDto){
        CommentDto dto = commentService.createComment(postId, commentDto);
        return new ResponseEntity<>(dto, HttpStatus.CREATED);
    }


    //localhost:8080/api/posts/1/comments
    @GetMapping("posts/{postId}/comments")                               //READ all record from db: take Java obj from db and convert into JSON in Postman
    public List<CommentDto> getAllCommentsByPostId(@PathVariable("postId") long postId){
        List<CommentDto> dto = commentService.getCommentByPostId(postId);
        return dto;
    }


    @PutMapping("/posts/{postId}/comments/{id}")                         //UPDATE: take postId,id, JSON and put that into Java object
    public ResponseEntity<CommentDto> updateComment(@PathVariable("postId") long postId, @PathVariable("id") long id,
                                                    @RequestBody CommentDto commentDto){
        CommentDto dto = commentService.updateComment(postId, id, commentDto);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }


    @DeleteMapping("/posts/{postId}/comments/{id}")                       //DELETE: take postId,id and delete if exist
    public ResponseEntity<String> deleteComment(@PathVariable("postId") long postId, @PathVariable("id") long commentId){
        commentService.deleteComment(postId, commentId);

        return new ResponseEntity<>("Comment is deleted!", HttpStatus.OK);
    }

}
