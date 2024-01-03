package com.ll.medium.domain.post.post.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ll.medium.domain.post.post.entity.Post;

public interface PostRepository extends JpaRepository<Post, Long> {
	List<Post> findTop30ByIsPublishedOrderByIdDesc(boolean isPublished);

	// Page<Post> findByTitleContainingIgnoreCaseOrBodyContainingIgnoreCase(@Param("kw") String kw,
	// 	@Param("kw_") String kw_, Pageable pageable);

	@Query("SELECT p FROM Post p "
		+ "WHERE "
		+ "LOWER(p.title) LIKE LOWER(CONCAT('%', :kw, '%')) OR "
		+ "LOWER(p.body) LIKE LOWER(CONCAT('%', :kw, '%')) OR "
		+ "LOWER(p.author.username) LIKE LOWER(CONCAT('%', :kw, '%'))"
		+ "AND (:kwType = 'title' OR :kwType = 'body' OR :kwType = 'author') "
		+ "AND ("
		+ "    (:kwType = 'title' AND LOWER(p.title) LIKE LOWER(CONCAT('%', :kw, '%'))) OR "
		+ "    (:kwType = 'body' AND LOWER(p.body) LIKE LOWER(CONCAT('%', :kw_, '%'))) OR "
		+ "    (:kwType = 'author' AND LOWER(p.author.username) LIKE LOWER(CONCAT('%', :kw, '%'))) "
		+ ") "
		+ "GROUP BY p "
		+ "ORDER BY "
		+ "CASE WHEN :sortCode = 'idDesc' THEN p.id END DESC, "
		+ "CASE WHEN :sortCode = 'isAsc' THEN p.id END ASC, "
		+ "CASE WHEN :sortCode = 'hitDesc' THEN p.hitCount END DESC ")
	Page<Post> findBySortCodeAndKwTypeAndKw(
		@Param("sortCode") String sortCode,
		@Param("kwType") String kwType,
		@Param("kw") String kw,
		@Param("kw_") String kw_,
		Pageable pageable
	);
}
