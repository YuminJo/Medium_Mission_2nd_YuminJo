package com.ll.medium.domain.post.post.service;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ll.medium.domain.member.member.entity.Member;
import com.ll.medium.domain.post.post.entity.Post;
import com.ll.medium.domain.post.post.repository.PostRepository;
import com.ll.medium.global.rsData.RsData.RsData;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostService {
	private final PostRepository postRepository;

	@Transactional
	public RsData<Post> write(Member author, String title, String body, boolean isPublished, boolean isPaid) {
		Post post = Post.builder()
			.author(author)
			.title(title)
			.body(body)
			.isPublished(isPublished)
			.isPaid(isPaid)
			.build();

		postRepository.save(post);

		return RsData.of("200", "글이 작성되었습니다.", post);
	}

	@Transactional
	public void modify(Post post, String title, String body, boolean published) {
		post.setTitle(title);
		post.setBody(body);
		post.setPublished(published);
	}

	public boolean canModify(Member actor, Post post) {
		return actor.equals(post.getAuthor());
	}

	public Object findTop30ByIsPublishedOrderByIdDesc(boolean isPublished) {
		return postRepository.findTop30ByIsPublishedOrderByIdDesc(isPublished);
	}

	public Optional<Post> findById(long id) {
		return postRepository.findById(id);
	}

	public Page<Post> search(String sortCode, String kwType, String kw, Pageable pageable) {
		//return postRepository.findByTitleContainingIgnoreCaseOrBodyContainingIgnoreCase(kw, kw, pageable);
		// if (sortCode != null || kwType != null) {
		return postRepository.findBySortCodeAndKwTypeAndKw(sortCode, kwType, kw, kw, pageable);
		// } else
		// 	return postRepository.findByTitleContainingIgnoreCaseOrBodyContainingIgnoreCase(kw, kw, pageable);
	}
}