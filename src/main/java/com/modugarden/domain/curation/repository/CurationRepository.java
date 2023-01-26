package com.modugarden.domain.curation.repository;

import com.modugarden.domain.category.entity.InterestCategory;
import com.modugarden.domain.curation.entity.Curation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CurationRepository extends JpaRepository<Curation, Long> {
    //회원 id로 조회
    Page<Curation> findAllByUser_Id(Long user_id, Pageable pageable);
    //카테고리와 제목 검색
    Slice<Curation> findAllByCategoryAndTitleLikeOrderByCreatedDateDesc(InterestCategory category, String title, Pageable pageable);
    //카테고리로 생성일자 순 조회
    Slice<Curation> findAllByCategoryOrderByCreatedDateDesc(InterestCategory category, Pageable pageable);
    //유저 아이디로 게시물 수 조회
    Long countByUser_Id(Long user_id);
}
