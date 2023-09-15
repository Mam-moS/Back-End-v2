package com.mmos.mmos.src.domain.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.ColumnDefault;

import java.util.ArrayList;
import java.util.List;

@Entity
@ToString
@Getter
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long user_index;

    @Column
    private String user_id;

    @Column
    private String user_password;

    @Column
    private String user_name;

    @Column
    private String user_nickname;

    @Column
    @ColumnDefault("null")  // 추후에 기본 이미지 주소로 변경
    private String user_profile_image;

    @Column
    @ColumnDefault("0")
    private Long user_total_study_time;

    @Column
    @ColumnDefault("0")
    private Integer user_tier;

    @Column
    private Long user_student_id;

    @Column
    @ColumnDefault("0")
    private Long user_total_completed_schedule_num;

    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Calendar> user_calendars = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE, orphanRemoval = true)
    @ColumnDefault("null")
    private List<UserBadge> user_userbadges;

    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE, orphanRemoval = true)
    @ColumnDefault("null")
    private List<UserStudy> user_userstudies;

    @Column
    @ColumnDefault("true")
    private boolean user_status;

    @ManyToOne
    @JoinColumn(name = "university_index")
    private University university;

    @Builder
    public User(String user_id, String user_password, String user_name, String user_nickname, Long user_student_id) {
        this.user_id = user_id;
        this.user_password = user_password;
        this.user_name = user_name;
        this.user_nickname = user_nickname;
        this.user_student_id = user_student_id;
    }


    public void addCalendars(Calendar calendar) {
        System.out.println("addCalendars = " + calendar.toString());
        this.user_calendars.add(calendar);
    }

    public void addUserBadges(UserBadge userBadge) {
        this.user_userbadges.add(userBadge);
    }

    public void adduserStudies(UserStudy userStudy) {
        this.user_userstudies.add(userStudy);
    }
}