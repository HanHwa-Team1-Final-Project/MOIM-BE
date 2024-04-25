package com.team1.moim.domain.group.entity;

import com.team1.moim.domain.member.entity.Member;
import com.team1.moim.global.config.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "groups")
public class Group extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 호스트ID
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    // 제목
    @Column(nullable = false)
    private String title;

    // 내용
    @Column(length=1000)
    private String contents;

    // 활동시간
    @Column(nullable = false)
    private int runningTime;

    // 희망 시작 날짜
    @Column(nullable = false)
    private LocalDate expectStartDate;

    // 희망 종료 날짜
    @Column(nullable = false)
    private LocalDate expectEndDate;

    // 희망 시작 시간
    @Column(nullable = false)
    private LocalTime expectStartTime;

    // 희망 종료 시간
    @Column(nullable = false)
    private LocalTime expectEndTime;

    // 장소
    private String place;

    // 첨부파일 경로
    @Setter
    private String filePath;

    // 투표 마감 시간
    @Column(nullable = false)
    private LocalDateTime voteDeadline;

    // 최종 확정 일정
    private LocalDateTime confirmedDateTime;

    // 확정 여부 (Y, N)
    @Column(nullable = false)
    private String isConfirmed = "N";

    // 참여자수
    private int participants;

    // 삭제여부 (Y, N)
    @Column(nullable = false)
    private String isDeleted = "N";

    @OneToMany(mappedBy = "group", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<GroupInfo> groupInfos = new ArrayList<>();

    @OneToMany(mappedBy = "group", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<GroupAlarm> groupAlarms = new ArrayList<>();

    @Builder
    public Group(Member member, String title, String contents, int runningTime, LocalDate expectStartDate,
                 LocalDate expectEndDate, LocalTime expectStartTime, LocalTime expectEndTime,
                 String place, LocalDateTime voteDeadline, LocalDateTime confirmedDateTime, int participants) {
        this.member = member;
        this.title = title;
        this.contents = contents;
        this.runningTime = runningTime;
        this.expectStartDate = expectStartDate;
        this.expectEndDate = expectEndDate;
        this.expectStartTime = expectStartTime;
        this.expectEndTime = expectEndTime;
        this.place = place;
        this.voteDeadline = voteDeadline;
        this.confirmedDateTime = confirmedDateTime;
        this.participants = participants;
    }

    public void delete() {
        this.isDeleted = "Y";
    }

    public void confirm() {
        this.isConfirmed = "Y";
    }

    public void confirm(String confirmDay) {
        this.isConfirmed = "Y";
        this.confirmedDateTime = LocalDateTime.parse(confirmDay);
    }

    public void setConfirmedDateTime(LocalDateTime confirmedDateTime){
        this.confirmedDateTime = confirmedDateTime;
    }
}
