package com.team1.moim.domain.member.repository;

import com.team1.moim.domain.event.entity.Event;
import com.team1.moim.domain.member.entity.Member;
import com.team1.moim.domain.member.entity.SocialType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByNickname(String nickname);
    Optional<Member> findByEmail(String email);
    Optional<Member> findByRefreshToken(String refreshToken);
    Optional<Member> findBySocialTypeAndSocialId(SocialType socialType, String socialId);

    @Query("SELECT m FROM Member m WHERE m <> :member AND m.deleteYn = 'N'")
    List<Member> findAllMemberExcept(@Param("member") Member member);

}
