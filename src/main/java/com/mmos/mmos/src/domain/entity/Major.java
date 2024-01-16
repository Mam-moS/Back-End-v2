package com.mmos.mmos.src.domain.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class Major {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long majorIndex;

    @Column
    private String majorName;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "collegeIndex")
    private College college;

    @JsonManagedReference
    @OneToMany(mappedBy = "major", cascade = CascadeType.REMOVE,orphanRemoval = true)
    private List<Users> majorUsers;

    public Major(String name, College college) {
        this.majorName = name;
        this.college = college;
    }

    public void addUser(Users user){
        this.majorUsers.add(user);
    }
}
