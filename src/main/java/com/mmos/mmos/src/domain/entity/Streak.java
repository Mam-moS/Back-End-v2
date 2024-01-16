package com.mmos.mmos.src.domain.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Getter
@NoArgsConstructor
public class Streak {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long streakIndex;

    @Column
    private Integer streakLevel = 0;

    @Column
    private LocalDate streakDate;

    @Column
    private boolean isChecked = false;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "userIndex")
    private Users user;

    public Streak(Integer streakLevel, LocalDate streakDate, Users user) {
        this.streakLevel = streakLevel;
        this.streakDate = streakDate;
        this.user = user;
    }

    public void updateStreak(Integer level) {
        this.streakLevel = level;
    }

    public void updateChecked(boolean isChecked) {
        this.isChecked = isChecked;
    }
}
