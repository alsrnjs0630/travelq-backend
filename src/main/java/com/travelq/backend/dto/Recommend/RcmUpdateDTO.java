package com.travelq.backend.dto.Recommend;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RcmUpdateDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    // 제목
    private String title;
    // 내용
    private String content;
    // 기존 이미지
    private List<String> originalImages;
    // 새로운 이미지
    @JsonIgnore
    private List<MultipartFile> newImages;
    // 삭제 이미지
    private List<String> deletedImages;

}
