package com.mmos.mmos.src.domain.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.*;

@NoArgsConstructor(access = PROTECTED)
@Getter
@Entity
public class Files {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long fileIndex;

    @Column
    private String storeFileUrl;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "postIndex")
    private Post post;

    public Files(String storeFileUrl, Post post) {
        this.storeFileUrl = storeFileUrl;
        this.post = post;
    }
}
