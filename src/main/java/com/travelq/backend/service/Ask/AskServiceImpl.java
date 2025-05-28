package com.travelq.backend.service.Ask;

import com.travelq.backend.dto.Ask.AskCreateDTO;
import com.travelq.backend.dto.Ask.AskResponseDTO;
import com.travelq.backend.dto.Ask.AskUpdateDTO;
import com.travelq.backend.dto.common.ApiResponseDTO;
import com.travelq.backend.dto.common.PageDTO;
import com.travelq.backend.entity.Ask;
import com.travelq.backend.entity.Member;
import com.travelq.backend.mapper.AskCmtMapper;
import com.travelq.backend.mapper.AskMapper;
import com.travelq.backend.mapper.PageMapper;
import com.travelq.backend.repository.AskRepository;
import com.travelq.backend.repository.MemberRepository;
import com.travelq.backend.util.CustomUserDetail;
import com.travelq.backend.util.search.PostSearchSpecs;
import com.travelq.backend.util.search.PostSpecs;
import com.travelq.backend.util.commonCode.StatusCode;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class AskServiceImpl implements AskService {
    private final AskRepository askRepository;
    private final MemberRepository memberRepository;
    private final AskMapper askMapper;
    private final PageMapper pageMapper;
    private final AskCmtMapper askCmtMapper;

    // 게시판 목록 조회
    @Override
    public PageDTO<AskResponseDTO, PostSearchSpecs> getList(PostSearchSpecs postSearchSpecs, Pageable pageable) {
        // 검색 조건 postSearchSpecs에 기반하여 Specification 생성
        Specification<Ask> spec = PostSpecs.bySearch(postSearchSpecs);

        // 페이지 번호 조정 (URL 파라미터로 받아온 Pageable의 페이지 번호 - 1 (Spring은 0페이지 부터 시작))
        int pageNum = (pageable.getPageNumber() < 1) ? 0 : pageable.getPageNumber() - 1;
        Pageable correctedPageable = PageRequest.of(pageNum, pageable.getPageSize(), pageable.getSort());

        return pageMapper.toPageDTO(askRepository.findAll(spec, correctedPageable).map(askMapper::toResponseDTO), postSearchSpecs);
    }

    // 게시글 상세 페이지
    @Override
    public ResponseEntity<ApiResponseDTO<AskResponseDTO>> detailPost(Long id) throws IOException {
        // 게시글 조회
        Ask ask = askRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("존재하지 않는 게시글입니다."));
        // 조회수 증가
        ask.updateViewCount(ask.getViewCount() + 1);
        askRepository.save(ask);
        AskResponseDTO responseDTO = askMapper.toResponseDTO(ask);
        ApiResponseDTO<AskResponseDTO> response = ApiResponseDTO.<AskResponseDTO>builder()
                .success(true)
                .message("게시글 조회에 성공하였습니다.")
                .data(responseDTO)
                .build();

        return ResponseEntity.ok(response);
    }

    // 게시판 등록
    @Override
    public ResponseEntity<ApiResponseDTO<AskCreateDTO>> createPost(AskCreateDTO askCreateDTO, Authentication authentication) {
        String username = authentication.getName();
        log.info("현재 사용자 이메일 : {}", username);
        // 회원 ID 체크. ID가 없거나 잘못된 ID면 400 에러 리턴
        Member member = memberRepository.findByEmail(username)
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 회원입니다."));

        // 회원 ID 인증 완료. 게시물 등록 시작
        log.info("질문 게시글 등록 시작!!");

        Ask newAskPost = Ask.builder()
                .member(member)
                .title(askCreateDTO.getTitle())
                .content(askCreateDTO.getContent())
                .author(member.getNickName())
                .state(StatusCode.NORMAL)
                .viewCount(0)
                .reportCount(0)
                .build();
        askRepository.save(newAskPost);

        log.info("질문 게시글 등록 완료");

        ApiResponseDTO<AskCreateDTO> response = ApiResponseDTO.<AskCreateDTO>builder()
                .success(true)
                .message("게시글이 정상 등록되었습니다!!")
                .data(askCreateDTO)
                .build();

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // 게시글 수정 권한 체크
    @Override
    public ResponseEntity<ApiResponseDTO<?>> checkAuth(Long id, Authentication authentication) throws IOException {
        // 게시글이 존재하는지 확인
        Ask ask = askRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 게시물입니다. 다시 시도해주세요"));

        // 게시글을 작성한 회원과 수정하려는 회원이 일치하는지 확인
        CustomUserDetail userDetail = (CustomUserDetail) authentication.getPrincipal();

        // 게시글 작성 유저와 인증 유저가 일치 하지 않는 경우
        if (!ask.getMember().getName().equals(userDetail.getName())) {
            ApiResponseDTO<AskUpdateDTO> response = ApiResponseDTO.<AskUpdateDTO>builder()
                    .success(false)
                    .message("권한이 없습니다")
                    .data(null)
                    .build();
            return ResponseEntity.ok(response);
        } else {
            ApiResponseDTO<AskUpdateDTO> response = ApiResponseDTO.<AskUpdateDTO>builder()
                    .success(true)
                    .message("권한 인증 성공")
                    .data(null)
                    .build();
            return ResponseEntity.ok(response);
        }

    }

    // 게시글 수정
    @Override
    public ResponseEntity<ApiResponseDTO<AskUpdateDTO>> updatePost(Long id, AskUpdateDTO askUpdateDTO, Authentication authentication) {
        // 게시글이 존재하는지 확인
        Ask ask = askRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 게시물입니다. 다시 시도해주세요"));

        // 게시글을 작성한 회원과 수정하려는 회원이 일치하는지 확인
        CustomUserDetail userDetail = (CustomUserDetail) authentication.getPrincipal();

        // 게시글 작성 유저와 인증 유저가 일치 하지 않는 경우
        if (!ask.getMember().getName().equals(userDetail.getName())) {
            ApiResponseDTO<AskUpdateDTO> response = ApiResponseDTO.<AskUpdateDTO>builder()
                    .success(false)
                    .message("권한이 없습니다")
                    .data(askUpdateDTO)
                    .build();
            return ResponseEntity.ok(response);
        }

        // 게시글 수정
        // 프론트에서 수정작업을 할 때 기존의 제목과 내용이 들어가있음
        ask.updateTitle(askUpdateDTO.getTitle());
        ask.updateContent(askUpdateDTO.getContent());
        askRepository.save(ask);

        log.info("질문 게시글 수정 완료");

        ApiResponseDTO<AskUpdateDTO> response = ApiResponseDTO.<AskUpdateDTO>builder()
                .success(true)
                .message("수정 성공하였습니다!!")
                .data(askUpdateDTO)
                .build();
        return ResponseEntity.ok(response);
    }

    // 게시물 삭제
    @Override
    public ResponseEntity<ApiResponseDTO<?>> deletePost(Long id, Authentication authentication) {
        // 일단은 일치한다고 생각하고 로직 작성
        Ask ask = askRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 게시물입니다. 다시 시도해주세요"));

        // 게시글을 작성한 회원과 지우려는 회원이 일치하는지 확인
        CustomUserDetail userDetail = (CustomUserDetail) authentication.getPrincipal();

        log.info("로그인 유저 이름 : {}", userDetail.getName());

        // 일치하지 않는 경우
        if (!ask.getMember().getName().equals(userDetail.getName())) {
            ApiResponseDTO<AskUpdateDTO> response = ApiResponseDTO.<AskUpdateDTO>builder()
                    .success(false)
                    .message("권한이 없습니다")
                    .data(null)
                    .build();
            return ResponseEntity.ok(response);
        }

        // 게시글 삭제
        ask.updateState(StatusCode.DELETED);
        askRepository.save(ask);

        log.info("게시글 삭제 성공");

        ApiResponseDTO<?> response = ApiResponseDTO.builder()
                .success(true)
                .message("게시글 삭제 성공")
                .build();

        return ResponseEntity.ok(response);
    }

    // 게시글 신고
    @Override
    public ResponseEntity<ApiResponseDTO<?>> reportPost(Long id) throws IOException {

        // 게시글 조회
        Ask ask = askRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("존재하지 않는 게시글입니다."));

        ask.updateReportCount(ask.getReportCount() + 1);
        askRepository.save(ask);

        ApiResponseDTO<?> response = ApiResponseDTO.builder()
                .success(true)
                .message("신고가 완료되었습니다.")
                .data(null)
                .build();

        return ResponseEntity.ok(response);
    }
}
