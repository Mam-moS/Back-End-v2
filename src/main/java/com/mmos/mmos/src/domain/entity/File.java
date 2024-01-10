package com.mmos.mmos.src.domain.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.*;

@NoArgsConstructor(access = PROTECTED)
@Getter
@Entity
public class File {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long fileIndex;

    @Column
    private String storeFileUrl;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "postIndex")
    private Post post;

    public File(String storeFileUrl, Post post) {
        this.storeFileUrl = storeFileUrl;
        this.post = post;
    }
}
