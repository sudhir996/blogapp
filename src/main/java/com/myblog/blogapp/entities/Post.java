package com.myblog.blogapp.entities;

import lombok.*;
import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(
        name="posts",
        uniqueConstraints = {@UniqueConstraint(columnNames = {"title"})}
      )
public class Post {                                             //post is ONE i.e. parent table

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="title", nullable = false)               //unique
    private String title;

    @Column(name="description", nullable = false)
    private String description;


    @Column(name="content", nullable = false)
    private String content;  //255 characters


    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    Set<Comment> comments = new HashSet<>();                      //comment is MANY i.e. child table. so,we use SET to store many comments
}
