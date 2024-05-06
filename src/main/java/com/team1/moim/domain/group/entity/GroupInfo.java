package com.team1.moim.domain.group.entity;

import com.team1.moim.domain.member.entity.Member;
import com.team1.moim.global.config.BaseTimeEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GroupInfo extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 모임 식별 번호
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "groups_id")
    private Group group;

    // 회원 ID
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    // 동의 여부
    // P(Pending)이면 알림은 갔지만 등록 또는 거절을 누르지 않은 상태
    // Y이면 등록을 누른 상태
    // N이면 참여를 거절한 상태
    @Builder.Default
    @Column(nullable = false)
    private String isAgreed = "P";

    // 삭제여부 (Y, N)
    @Column(nullable = false)
    @Builder.Default
    private String isDeleted = "N";

    // 일정 등록 여부
    @Column(nullable = false)
    @Builder.Default
    private String isAddEvent = "N";

    public void delete() {
        this.isDeleted = "Y";
    }

    public void attachGroup(Group group){
        this.group = group;
        group.getGroupInfos().add(this);
    }

    public void addEvent() {
        this.isAddEvent = "Y";
    }

    public void vote(String agree){
        this.isAgreed = agree;
    }
}
