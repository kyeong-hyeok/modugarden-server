package com.modugarden.domain.curation.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CurationLikeResponseDto {
    private Long id;
    private Long likeNum;
}