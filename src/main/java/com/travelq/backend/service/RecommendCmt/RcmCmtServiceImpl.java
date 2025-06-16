package com.travelq.backend.service.RecommendCmt;

import com.travelq.backend.dto.RecommendCmt.RcmCmtRequestDTO;
import com.travelq.backend.dto.common.ApiResponseDTO;
import com.travelq.backend.entity.Member;
import com.travelq.backend.entity.Recommend;
import com.travelq.backend.entity.RecommendCmt;
import com.travelq.backend.repository.MemberRepository;
import com.travelq.backend.repository.RecommendCmtRepository;
import com.travelq.backend.repository.RecommendRepository;
import com.travelq.backend.util.CustomUserDetail;
import com.travelq.backend.util.commonCode.StatusCode;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

import java.io.IOException;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class RcmCmtServiceImpl implements RcmCmtService {
    private final MemberRepository memberRepository;
    private final RecommendRepository recommendRepository;
    private final RecommendCmtRepository recommendCmtRepository;

    // 추천 게시글 댓글 등록
    @Override
    public ResponseEntity<ApiResponseDTO<RcmCmtRequestDTO>> createRcmCmt(@RequestBody @Valid RcmCmtRequestDTO rcmCmtRequestDTO,
                                                                         Authentication authentication) throws IOException {
        // 현재 로그인한 회원 정보
        CustomUserDetail customUserDetail = (CustomUserDetail) authentication.getPrincipal();
        Member authUser = memberRepository.findByEmail(customUserDetail.getUsername())
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 회원입니다."));

        // 댓글 등록 게시글
        Recommend nowPost = recommendRepository.findById(rcmCmtRequestDTO.getRecommendId())
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 게시글입니다."));

        // 추천 게시판 댓글 등록
        RecommendCmt newCmt = RecommendCmt.builder()
                .recommend(nowPost)
                .memberId(authUser.getId())
                .author(authUser.getNickName())
                .content(rcmCmtRequestDTO.getContent())
                .reportCount(0)
                .state(StatusCode.NORMAL)
                .build();

        recommendCmtRepository.save(newCmt);

        ApiResponseDTO<RcmCmtRequestDTO> response = ApiResponseDTO.<RcmCmtRequestDTO>builder()
                .success(true)
                .message("댓글이 등록되었습니다.")
                .data(rcmCmtRequestDTO)
                .build();

        return ResponseEntity.ok(response);
    }

    // 추천 게시글 댓글 삭제
    @Override
    public ResponseEntity<ApiResponseDTO<?>> deleteRcmCmt(Long rcmCmtId, Authentication authentication) throws IOException {
        // 댓글 작성자와 현재 사용자 일치 여부
        // 현재 사용자
        CustomUserDetail customUserDetail = (CustomUserDetail) authentication.getPrincipal();
        Member authedUser = memberRepository.findByEmail(customUserDetail.getUsername())
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 회원입니다."));

        // 댓글 작성자
        RecommendCmt nowCmt = recommendCmtRepository.findById(rcmCmtId).orElseThrow(() -> new EntityNotFoundException("존재하지 않는 댓글입니다."));

        if (!nowCmt.getMemberId().equals(authedUser.getId())) {
            // 댓글 삭제 권한 X
            ApiResponseDTO<?> response = ApiResponseDTO.builder()
                    .success(false)
                    .message("삭제 권한이 없습니다.")
                    .data(null)
                    .build();

            return ResponseEntity.ok(response);
        }

        // 댓글 삭제 권한 O
        nowCmt.updateState(StatusCode.DELETED);
        recommendCmtRepository.save(nowCmt);

        ApiResponseDTO<?> response = ApiResponseDTO.builder()
                .success(true)
                .message("삭제 되었습니다.")
                .data(null)
                .build();

        return ResponseEntity.ok(response);
    }

    // 추천 게시글 댓글 신고
    @Override
    public ResponseEntity<ApiResponseDTO<?>> reportRcmCmt(Long rcmCmtId) throws IOException {
        // 댓글 조회
        RecommendCmt cmt = recommendCmtRepository.findById(rcmCmtId).orElseThrow(() -> new EntityNotFoundException("존재하지 않는 댓글입니다."));

        // 신고 카운트 + 1
        cmt.updateReportCount(cmt.getReportCount() + 1);
        recommendCmtRepository.save(cmt);

        // 응답
        ApiResponseDTO<?> response = ApiResponseDTO.builder()
                .success(true)
                .message("신고 되었습니다.")
                .data(null)
                .build();

        return ResponseEntity.ok(response);
    }
}
