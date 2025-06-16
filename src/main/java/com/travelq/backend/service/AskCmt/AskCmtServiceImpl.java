package com.travelq.backend.service.AskCmt;

import com.travelq.backend.dto.AskCmt.AskCmtRequestDTO;
import com.travelq.backend.dto.common.ApiResponseDTO;
import com.travelq.backend.entity.Ask;
import com.travelq.backend.entity.AskCmt;
import com.travelq.backend.entity.Member;
import com.travelq.backend.mapper.AskCmtMapper;
import com.travelq.backend.repository.AskCmtRepository;
import com.travelq.backend.repository.AskRepository;
import com.travelq.backend.repository.MemberRepository;
import com.travelq.backend.util.CustomUserDetail;
import com.travelq.backend.util.commonCode.StatusCode;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class AskCmtServiceImpl implements AskCmtService{
    private final AskCmtRepository askCmtRepository;
    private final AskCmtMapper askCmtMapper;
    private final MemberRepository memberRepository;
    private final AskRepository askRepository;

    // 댓글 작성
    @Override
    public ResponseEntity<ApiResponseDTO<AskCmtRequestDTO>> createAskCmt(AskCmtRequestDTO askCmtRequestDTO, Authentication authentication) {
        // 현재 로그인 회원 인증 정보
        CustomUserDetail user = (CustomUserDetail) authentication.getPrincipal();
        // 회원 정보
        Member member = memberRepository.findByEmail(user.getUsername())
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 회원입니다."));

        log.info("회원정보 조회 성공");

        // 게시판 정보
        Ask ask = askRepository.findById(askCmtRequestDTO.getAskId())
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 게시글입니다."));

        log.info("게시글 정보 조회 성공");

        // 질문 게시글 댓글 생성
        AskCmt askCmt = AskCmt.builder()
                .ask(ask)
                .memberId(member.getId())
                .author(member.getNickName())
                .content(askCmtRequestDTO.getContent())
                .reportCount(0)
                .state(StatusCode.NORMAL)
                .build();

        ask.createCmt(askCmt);
        askRepository.save(ask);
        askCmtRepository.save(askCmt);

        log.info("질문 게시글 {} 댓글 작성 성공", ask.getId());

        ApiResponseDTO<AskCmtRequestDTO> apiResponseDTO = ApiResponseDTO.<AskCmtRequestDTO>builder()
                .success(true)
                .message("작성 되었습니다")
                .data(askCmtRequestDTO)
                .build();

        return ResponseEntity.ok(apiResponseDTO);
    }

    // 질문 게시판 댓글 삭제
    @Override
    public ResponseEntity<ApiResponseDTO<?>> deleteAskCmt(Long askCmtId, Authentication authentication) {
        // 현재 사용자 정보
        CustomUserDetail user = (CustomUserDetail) authentication.getPrincipal();
        Member member = memberRepository.findByEmail(user.getUsername())
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 회원입니다."));

        // 게시글 댓글 조회
        AskCmt askCmt = askCmtRepository.findById(askCmtId)
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 댓글입니다."));

        // 삭제 권한 확인 (댓글 작성 유저와 현재 유저가 같은지 판별)
        if (!askCmt.getMemberId().equals(member.getId())) {
            // 작성자와 사용자가 다른 경우
            log.info("삭제권한이 없습니다.");
            ApiResponseDTO<Object> response = ApiResponseDTO.builder()
                    .success(false)
                    .message("삭제 권한이 없습니다.")
                    .data(null)
                    .build();

            return ResponseEntity.ok(response);
        }

        // 작성자와 사용자가 일치 (삭제 가능)
        log.info("게시글 댓글 삭제 시작");

        askCmt.updateState(StatusCode.DELETED);
        askCmtRepository.save(askCmt);

        ApiResponseDTO<Object> response = ApiResponseDTO.builder()
                .success(true)
                .message("삭제 되었습니다.")
                .data(null)
                .build();

        return ResponseEntity.ok(response);
    }

    // 댓글 신고
    @Override
    public ResponseEntity<ApiResponseDTO<?>> reportAskCmt(Long askCmtId) {
        // 질문 게시글 댓글 조회
        AskCmt cmt = askCmtRepository.findById(askCmtId).orElseThrow(() -> new EntityNotFoundException("존재하지 않는 댓글입니다."));

        // 신고 카운트 + 1
        cmt.updateReportCount(cmt.getReportCount() + 1);
        askCmtRepository.save(cmt);

        ApiResponseDTO<Object> response = ApiResponseDTO.builder()
                .success(true)
                .message("신고 되었습니다")
                .data(null)
                .build();

        return ResponseEntity.ok(response);
    }
}
