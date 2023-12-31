package com.ll.medium.domain.member.member.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ll.medium.domain.member.member.entity.Member;
import com.ll.medium.domain.member.member.service.MemberService;
import com.ll.medium.global.rq.Rq.Rq;
import com.ll.medium.global.rsData.RsData.RsData;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Controller
@RequestMapping("/member")
@RequiredArgsConstructor
public class MemberController {
	private final MemberService memberService;
	private final Rq rq;

	@PreAuthorize("isAnonymous()")
	@GetMapping("/join")
	public String showJoin() {
		return "domain/member/member/join";
	}

	@Setter
	@Getter
	public static class JoinForm {
		@NotBlank
		private String username;
		@NotBlank
		private String password;
		@NotBlank
		private String nickname;
	}

	@PreAuthorize("isAnonymous()")
	@PostMapping("/join")
	public String join(@Valid JoinForm joinForm) {
		RsData<Member> joinRs = memberService
			.join(joinForm.getUsername(), joinForm.getPassword(), joinForm.getNickname());

		return rq.redirectOrBack(joinRs, "/member/login");
	}

	@GetMapping("/login")
	public String showLogin() {
		return "domain/member/member/login";
	}
}