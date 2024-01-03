package com.ll.medium.global.init;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;

import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;

import com.ll.medium.domain.member.member.entity.Member;
import com.ll.medium.domain.member.member.service.MemberService;
import com.ll.medium.domain.post.post.service.PostService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Configuration
@Profile("!prod")
@Slf4j
@RequiredArgsConstructor
public class NotProd {
	private final MemberService memberService;
	private final PostService postService;
	private final Random random = new Random();

	@Bean
	@Order(3)
	public ApplicationRunner initNotProd() {
		return args -> {
			if (memberService.findByUsername("user1").isPresent())
				return;

			List<Member> memberList = initializeMembers();

			generatePosts(memberList, true, 1, 100);
			generatePosts(memberList, false, 101, 111);
		};
	}

	private List<Member> initializeMembers() {
		List<Member> memberList = new ArrayList<>();

		//유료 회원 100명 생성
		IntStream.rangeClosed(1, 100).forEach(i -> {
			String username = String.format("user%s", i);
			Member member = memberService.join(username, "1234",username+i).getData();

			//유료 회원으로 설정
			memberService.findByUsername(username).ifPresent(user -> memberService.setIsPaidTrueByUsername(username));
			memberList.add(member);
		});

		//테스트로 어드민만 유료회원으로 설정
		//memberService.findByUsername("admin").ifPresent(member -> memberService.setIsPaidTrueByUsername("admin"));

		return memberList;
	}

	private void generatePosts(List<Member> memberList, boolean isPaid, int start, int end) {
		IntStream.rangeClosed(start, end).forEach(i -> {
			int randomUser = random.nextInt(memberList.size());
			postService.write(memberList.get(randomUser), "Paid Title " + i, "내용 " + i, true, isPaid);
		});
	}
}
