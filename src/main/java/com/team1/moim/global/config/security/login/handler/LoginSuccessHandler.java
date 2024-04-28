package com.team1.moim.global.config.security.login.handler;

import com.team1.moim.domain.member.entity.Account;
import com.team1.moim.domain.member.entity.LoginType;
import com.team1.moim.domain.member.entity.Member;
import com.team1.moim.domain.member.exception.AccountNotFoundException;
import com.team1.moim.domain.member.exception.MemberNotFoundException;
import com.team1.moim.domain.member.repository.AccountRepository;
import com.team1.moim.domain.member.repository.MemberRepository;
import com.team1.moim.global.config.security.jwt.JwtProvider;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;

/**
 * 커스텀한 JSON 로그인 필터가 정상적으로 통과하여 인증 처리가 되면 (로그인 성공)
 * 이 로그인 성공 핸들러가 동작
 */

@Slf4j
@RequiredArgsConstructor
public class LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JwtProvider jwtProvider;
    private final MemberRepository memberRepository;
    private final AccountRepository accountRepository;

    @Value("${jwt.access.expiration}")
    private String accessTokenExpiration;

    @Override
    @Transactional
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException {
        log.info("onAuthenticationSuccess() 진입");
        String email = extractUsername(authentication); // 인증 정보에서 Username(email) 추출
        String role = memberRepository.findByEmail(email)
                .orElseThrow(MemberNotFoundException::new)
                .getRole().name();
        Account findAccount = accountRepository.findByEmail(email).orElseThrow(AccountNotFoundException::new);
        Member findMember = findAccount.getMember();
        String accessToken = jwtProvider.createAccessToken(findMember.getNickname(), role);
        String refreshToken = jwtProvider.createRefreshToken();

        jwtProvider.sendAccessAndRefreshToken(response, accessToken, refreshToken);

        findMember.updateRefreshToken(refreshToken);
        findMember.updateRepresentativeData(findAccount.getEmail(), findAccount.getProfileImage(), LoginType.NORMAL);
        memberRepository.saveAndFlush(findMember);

        log.info("로그인에 성공하였습니다. 이메일 : {}", email);
        log.info("로그인에 성공하였습니다. AccessToken : {}", accessToken);
        log.info("발급된 AccessToken 만료 기간 : {}", accessTokenExpiration);
    }

    private String extractUsername(Authentication authentication){
        log.info("extractUsername() 진입");
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        return userDetails.getUsername();
    }


}
