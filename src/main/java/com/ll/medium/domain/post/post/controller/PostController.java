package com.ll.medium.domain.post.post.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.ll.medium.domain.post.post.entity.Post;
import com.ll.medium.domain.post.post.service.PostService;
import com.ll.medium.global.rq.Rq.Rq;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/post")
@RequiredArgsConstructor
public class PostController {
	private final PostService postService;
	private final Rq rq;

	@GetMapping("/{id}")
	public String showDetail(@PathVariable long id) {
		rq.setAttribute("post", postService.findById(id).get());

		return "domain/post/post/detail";
	}

	@GetMapping("/list")
	public String showList(
		@RequestParam(name = "sortCode", required = false, defaultValue = "") String sortCode,
		@RequestParam(name = "kwType", required = false, defaultValue = "") String kwType,
		@RequestParam(value = "kw", defaultValue = "") String kw,
		@RequestParam(value = "page", defaultValue = "1") int page) {
		List<Sort.Order> sorts = new ArrayList<>();
		sorts.add(Sort.Order.desc("id"));
		Pageable pageable = PageRequest.of(page - 1, 10, Sort.by(sorts));

		Page<Post> postPage = postService.search(sortCode, kwType, kw, pageable);
		rq.setAttribute("postPage", postPage);
		rq.setAttribute("page", page);

		return "domain/post/post/list";
	}
}
