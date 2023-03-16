package com.myblog.blogapp.controller;

import com.myblog.blogapp.payload.PostDto;
import com.myblog.blogapp.payload.PostResponse;
import com.myblog.blogapp.service.PostService;
import com.myblog.blogapp.utils.AppConstants;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/posts")
public class PostController {

    private PostService postService;

    public PostController(PostService postService) {             //act as @Autowired (constructor with argument)
        this.postService = postService;
    }

    @PreAuthorize("hasRole('ADMIN')")                            //Role is ADMIN, this method cam run
    @PostMapping                                                 //CREATE: Post is created(JSON to Java in db)
    public ResponseEntity<Object> createPost(@Valid @RequestBody PostDto postDto, BindingResult bindingResult){   //bindingResult helps to Response back
        if(bindingResult.hasErrors()){
            return new ResponseEntity<>(bindingResult.getFieldError().getDefaultMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
       return new ResponseEntity<>(postService.createPost(postDto), HttpStatus.CREATED);                                          //ResponseEntity<>(save, http status code)
    }


    //localhost:8080/api/posts
    //localhost:8080/api/posts?pageNo=0&pageSize=5
    //localhost:8080/api/posts?pageNo=0&pageSize=5&sortBy=title&sortDir=desc
    @GetMapping                                             //READ all record from db: take Java obj from db and convert into JSON in Postman
    public PostResponse getAllPosts(                                                                                           //get - dto, not entity
         @RequestParam (value = "pageNo" , defaultValue = AppConstants.DEFAULT_PAGE_NUMBER , required = false) int pageNo ,
         @RequestParam (value = "pageSize" , defaultValue = AppConstants.DEFAULT_PAGE_SIZE , required = false) int pageSize ,
         @RequestParam (value = "sortBy" , defaultValue = AppConstants.DEFAULT_SORT_BY , required = false) String sortBy ,
         @RequestParam (value = "sortDir" , defaultValue = AppConstants.DEFAULT_SORT_DIR , required = false) String sortDir
         ){
        return postService.getAllPosts(pageNo ,pageSize ,sortBy ,sortDir);                                                     //calling service layer
    }


    //http://localhost:8080/api/posts/1000
    @GetMapping("/{id}")                                    //READ one record from db: take Java obj from db and convert into JSON in Postman
    public ResponseEntity<PostDto> getPostById(@PathVariable("id") long id){
        return ResponseEntity.ok(postService.getPostById(id));          //return dto object
    }


    //http://localhost:8080/api/posts/1
    @PutMapping("/{id}")                                    //UPDATE: take JSON and put that into Java object (dto obj, id)
    public ResponseEntity<PostDto> updatePost(@RequestBody PostDto postdto, @PathVariable("id") long id){
        PostDto dto = postService.updatePost(postdto, id);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }


    //http://localhost:8080/api/posts/1
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletePost(@PathVariable("id") long id){
        postService.deletePost(id);
        return new ResponseEntity<>("Post entity deleted successfully", HttpStatus.OK);
    }

}