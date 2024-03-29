package com.mmos.mmos.src.domain.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class UserBadge {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userbadgeIndex;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "userIndex")
    private Users user;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "badgeIndex")
    private Badge badge;

    @Column
    private Boolean userbadgeIsVisible = false;

    public UserBadge(Users user, Badge badge) {
        this.user = user;
        this.badge = badge;
    }

    public void updateIsVisible(boolean status) {
        this.userbadgeIsVisible = status;
    }
}
