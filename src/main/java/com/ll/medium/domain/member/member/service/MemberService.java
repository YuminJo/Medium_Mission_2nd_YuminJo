package com.ll.medium.domain.member.member.service;

import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ll.medium.domain.member.member.entity.Member;
import com.ll.medium.domain.member.member.repository.MemberRepository;
import com.ll.medium.global.rsData.RsData.RsData;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {
	private final MemberRepository memberRepository;
	private final PasswordEncoder passwordEncoder;

	@Transactional
	public RsData<Member> join(String username, String password, String nickname) {
		if (findByUsername(username).isPresent()) {
			return RsData.of("400-2", "이미 존재하는 회원입니다.");
		}

		Member member = Member.builder()
			.username(username)
			.password(passwordEncoder.encode(password))
			.nickname(nickname)
			.build();
		memberRepository.save(member);

		return RsData.of("200", "%s님 환영합니다. 회원가입이 완료되었습니다. 로그인 후 이용해주세요.".formatted(member.getUsername()), member);
	}

	public Optional<Member> findByUsername(String username) {
		return memberRepository.findByUsername(username);
	}

	public long count() {
		return memberRepository.count();
	}

	@Transactional
	public RsData<Member> setIsPaidTrueByUsername(String username) {
		memberRepository.setIsPaidTrueByUsername(username);
		return RsData.of("200", "모든 회원의 isPaid를 true로 변경하였습니다.");
	}

	@Transactional
	public RsData<Member> whenSocialLogin(String providerTypeCode, String username, String nickname,
		String profileImgUrl) {
		Optional<Member> opMember = findByUsername(username);

		return opMember.map(member -> RsData.of("200", "이미 존재합니다.", member))
			.orElseGet(() -> join(username, "", nickname));
	}
}
