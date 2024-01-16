package com.mmos.mmos.src.domain.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.mmos.mmos.src.domain.dto.request.PostSaveRequestDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@DynamicInsert
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long postIndex;

    @Column
    private String postTitle;

    @Lob
    @Column
    private String postContents;

    @Column
    private String postImage;

    @Column
    private Boolean postIsNotice = true;

    @Column
    private String postWriterName;

    @Column
    private Long postWriterIndex;

    @Column
    private Timestamp postCreatedAt;

    @Column
    private Timestamp postUpdatedAt;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "studyIndex")
    private Study study = null;

    @JsonManagedReference
    @OneToMany(mappedBy = "post", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Files> files = new ArrayList<>();

    public Post(PostSaveRequestDto postSaveRequestDto, Users user, Study study, Timestamp postCreatedAt, Timestamp postUpdatedAt) {
        this.postIsNotice = postSaveRequestDto.getIsNotice();
        this.postTitle = postSaveRequestDto.getPostTitle();
        this.postContents = postSaveRequestDto.getPostContents();
        this.postWriterIndex = user.getUserIndex();
        this.postWriterName = user.getUsername();
        this.study = study;
        this.postCreatedAt = postCreatedAt;
        this.postUpdatedAt = postUpdatedAt;
    }

    public void addFile(Files file) {
        this.files.add(file);
    }

    public void setUpdatedAt(Timestamp now) {
        this.postUpdatedAt = now;
    }

    public void updateWriter(Users user) {
        this.postWriterIndex = user.getUserIndex();
        this.postWriterName = user.getUsername();
    }

    public void updateTitle(String title) {
        this.postTitle = title;
    }

    public void updateContents(String contents) {
        this.postContents = contents;
    }

    public void updateImage(String image) {
        this.postImage = image;
    }
}