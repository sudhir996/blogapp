package com.myblog.blogapp.service.impl;

import com.myblog.blogapp.entities.Post;
import com.myblog.blogapp.exception.ResourceNotFoundException;
import com.myblog.blogapp.payload.PostDto;
import com.myblog.blogapp.payload.PostResponse;
import com.myblog.blogapp.repository.PostRepository;
import com.myblog.blogapp.service.PostService;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PostServiceImpl implements PostService {

    private ModelMapper mapper;
    private PostRepository postRepo;

    public PostServiceImpl(PostRepository postRepo, ModelMapper mapper) {       //act as @Autowired (constructor with argument)
        this.postRepo = postRepo;
        this.mapper = mapper;
    }

    @Override
    public PostDto createPost(PostDto postDto) {     //In this method, req Dto to Entity becoz repository intract with entity only
        Post post = mapToEntity(postDto);            //call mapToEntity and return back to post
        Post postEntity = postRepo.save(post);       //saving and converted into postEntity
        PostDto dto = mapToDto(postEntity);          //call mapToDto and return back to dto
        return dto;
    }
    public Post mapToEntity(PostDto postDto){                        //dto to entity method
        Post post = mapper.map(postDto, Post.class);

//        Post post = new Post();
//        post.setTitle(postDto.getTitle());                         //get dto object and set to entity object
//        post.setDescription(postDto.getDescription());
//        post.setContent(postDto.getContent());
        return post;
    }
    public PostDto mapToDto(Post post){                             //entity to dto method
        PostDto dto = mapper.map(post, PostDto.class);

//        PostDto dto = new PostDto();
//        dto.setId(post.getId());                                    //get post object and set to dto object
//        dto.setTitle(post.getTitle());
//        dto.setDescription(post.getDescription());
//        dto.setContent(post.getContent());
        return dto;
    }


    @Override
    public PostResponse getAllPosts(int pageNo, int pageSize, String sortBy, String sortDir) {          //new list of dto objects return to controller
        //Sorting
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ?
                Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();                             //act as if-else
        //Pagination
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);                              //import from data.domain
        Page<Post> posts = postRepo.findAll(pageable);                                    //return type of this is Page
        List<Post> content = posts.getContent();                   //find all the data from db and give list of entity objects(Page converted into List)

        List<PostDto> contents = content.stream().map(post -> mapToDto(post)).collect(Collectors.toList());//java8 feature : stream api concept used (//convert into List of entity obj into dto, and store into list)

        PostResponse postResponse = new PostResponse();
        postResponse.setContent(contents);                                 //give contents from list i.e. JSON obj.
        postResponse.setPageNo(posts.getNumber());                         //give current PageNo
        postResponse.setPageSize(posts.getSize());                         //give current PageSize
        postResponse.setTotalElements(posts.getTotalElements());           //give total elements from the db
        postResponse.setTotalPages(posts.getTotalPages());                 //give total Pages created
        postResponse.setLast(posts.isLast());                              //give last page : true/false
        return postResponse;
    }


    @Override
    public PostDto getPostById(long id) {
        Post post = postRepo.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("post", "id", id)     //resourceName:post, fieldName:id, fieldValue:1
        );
        PostDto postDto = mapToDto(post);                      //entity to dto
        return postDto;
    }


    @Override
    public PostDto updatePost(PostDto postDto, long id) {
        Post post = postRepo.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Post", "id", id)
        );
        post.setTitle(postDto.getTitle());
        post.setDescription(postDto.getDescription());
        post.setContent(postDto.getContent());

        Post newPost = postRepo.save(post);
        return mapToDto(newPost);
    }


    @Override
    public void deletePost(long id) {
        Post post = postRepo.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Post", "id", id)
        );
        postRepo.deleteById(id);
    }

}
