package com.travelq.backend.dto.Recommend;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RcmRequestDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    // 제목
    private String title;
    // 내용
    private String content;
    // 업로드 이미지 리스트
    private List<MultipartFile> images;

}
