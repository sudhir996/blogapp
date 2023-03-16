package com.myblog.blogapp.service;

import com.myblog.blogapp.payload.PostDto;
import com.myblog.blogapp.payload.PostResponse;

import java.util.List;

public interface PostService {

    PostDto createPost(PostDto postDto);                  //@PostMapping : CREATE

    PostResponse getAllPosts(int pageNo, int pageSize, String sortBy, String sortDir);       //@GetMapping : READ all data from db

    PostDto getPostById(long id);                         //@GetMapping : READ only one data from db

    PostDto updatePost(PostDto postdto, long id);         //@PutMapping : UPDATE req data of JSON obj

    void deletePost(long id);                          //@DeleteMapping : DELETE req data of JSON obj
}
