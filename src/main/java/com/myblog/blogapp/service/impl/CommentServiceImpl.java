package com.myblog.blogapp.service.impl;

import com.myblog.blogapp.entities.Comment;
import com.myblog.blogapp.entities.Post;
import com.myblog.blogapp.exception.ResourceNotFoundException;
import com.myblog.blogapp.payload.CommentDto;
import com.myblog.blogapp.repository.CommentRepository;
import com.myblog.blogapp.repository.PostRepository;
import com.myblog.blogapp.service.CommentService;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CommentServiceImpl implements CommentService {
    private ModelMapper mapper;

    private CommentRepository commentRepository;
    private PostRepository postRepository;

    public CommentServiceImpl(CommentRepository commentRepository, PostRepository postRepository, ModelMapper mapper) {      //act as @Autowired
        this.commentRepository = commentRepository;
        this.postRepository = postRepository;
        this.mapper = mapper;
    }

    @Override
    public CommentDto createComment(long postId, CommentDto commentDto) {         //for this postId save the comment
        Post post = postRepository.findById(postId).orElseThrow(                  //find, for this postId, post is having or not
                () -> new ResourceNotFoundException("post", "id", postId)         //resourceName:post, fieldName:id, fieldValue:postId(1)
        );

        Comment comment = mapToComment(commentDto);                          //before saving in db, we required java obj. so,dto to entity

        comment.setPost(post);                                               //for this post
        Comment newComment = commentRepository.save(comment);                //we save comment

        return mapToDto(newComment);                                         //after saving in db, we required JSON obj. so,entity to dto
    }
    Comment mapToComment(CommentDto commentDto){                             //dto to entity method
        Comment comment = mapper.map(commentDto, Comment.class);
//        Comment comment = new Comment();
//        comment.setName(commentDto.getName());
//        comment.setEmail(commentDto.getEmail());
//        comment.setBody(commentDto.getBody());
        return comment;
    }
    CommentDto mapToDto (Comment comment){                                    //entity to dto method
        CommentDto commentDto = mapper.map(comment, CommentDto.class);
//        CommentDto commentDto = new CommentDto();
//        commentDto.setId(comment.getId());
//        commentDto.setName(comment.getName());
//        commentDto.setEmail(comment.getEmail());
//        commentDto.setBody(comment.getBody());
        return commentDto;
    }


    @Override
    public List<CommentDto> getCommentByPostId(long postId) {
        List<Comment> comments = commentRepository.findByPostId(postId);               //find all comments for a given postId
        return comments.stream().map(comment -> mapToDto(comment)).collect(Collectors.toList());
    }


    @Override
    public CommentDto updateComment(long postId, long id, CommentDto commentDto) {
        Post post = postRepository.findById(postId).orElseThrow(                       //find, for this postId, post is having or not
                () -> new ResourceNotFoundException("post", "id", postId)
        );
        Comment comment = commentRepository.findById(id).orElseThrow(                  //find, for this comment id, comment is having or not
                () -> new ResourceNotFoundException("comment", "id", id)
        );

        comment.setName(commentDto.getName());                                        //updating comment get from dto i.e JSON obj.
        comment.setEmail(commentDto.getEmail());                                      //and set comment in db as java obj.
        comment.setBody(commentDto.getBody());

        Comment updatedComment = commentRepository.save(comment);                     //after saving, it will return new updatedComment i.e. java obj.

        return mapToDto(updatedComment);                                              //converting updatedComment to dto
    }


    @Override
    public void deleteComment(long postId, long commentId) {
        Post post = postRepository.findById(postId).orElseThrow(
                () -> new ResourceNotFoundException("post", "id", postId)
        );
        Comment comment = commentRepository.findById(commentId).orElseThrow(
                () -> new ResourceNotFoundException("comment", "id", commentId)
        );

          commentRepository.deleteById(commentId);
    }

}
