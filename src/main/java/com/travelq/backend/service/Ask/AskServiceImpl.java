package com.travelq.backend.service.Ask;

import com.travelq.backend.dto.Ask.AskCreateDTO;
import com.travelq.backend.dto.Ask.AskResponseDTO;
import com.travelq.backend.dto.Ask.AskUpdateDTO;
import com.travelq.backend.dto.common.ApiResponseDTO;
import com.travelq.backend.dto.common.PageDTO;
import com.travelq.backend.entity.Ask;
import com.travelq.backend.entity.Member;
import com.travelq.backend.mapper.AskMapper;
import com.travelq.backend.mapper.PageMapper;
import com.travelq.backend.repository.AskRepository;
import com.travelq.backend.repository.MemberRepository;
import com.travelq.backend.util.PostSearchSpecs;
import com.travelq.backend.util.PostSpecs;
import com.travelq.backend.util.StatusCode;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class AskServiceImpl implements AskService {
    private final AskRepository askRepository;
    private final MemberRepository memberRepository;
    private final AskMapper askMapper;
    private final PageMapper pageMapper;

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

    // 게시판 등록
    @Override
    public ResponseEntity<ApiResponseDTO<AskCreateDTO>> createPost(AskCreateDTO askCreateDTO) {
        // 회원 ID 체크. ID가 없거나 잘못된 ID면 400 에러 리턴
        Optional<Member> member = memberRepository.findById(askCreateDTO.getMemberId());
        if (askCreateDTO.getMemberId() == null || member.isEmpty()) {
            log.error("질문 게시글 등록 실패!! : 회원 아이디가 잘못 되었습니다.");
            ApiResponseDTO<AskCreateDTO> response = ApiResponseDTO.<AskCreateDTO>builder()
                    .success(false)
                    .message("존재하지 않는 회원입니다")
                    .build();
            return ResponseEntity.badRequest().body(response);
        }
        // 회원 ID 인증 완료. 게시물 등록 시작
        log.info("질문 게시글 등록 시작!!");

        Ask newAskPost = Ask.builder()
                .member(member.get())
                .title(askCreateDTO.getTitle())
                .content(askCreateDTO.getContent())
                .author(member.get().getNickName())
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

        return ResponseEntity.ok(response);
    }

    // 게시글 수정
    @Override
    public ResponseEntity<ApiResponseDTO<AskUpdateDTO>> updatePost(Long askId,AskUpdateDTO askUpdateDTO) {
        // 게시글을 작성한 회원과 지우려는 회원이 일치하는지 확인
        // Spring Security를 사용하여 현재 로그인한 회원과 게시글을 작성한 회원의 일치 여부 판별

        // 게시글이 존재하는지 확인
        Ask ask = askRepository.findById(askId)
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 게시물입니다. 다시 시도해주세요"));

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
    public ResponseEntity<ApiResponseDTO<?>> deletePost(Long id) {
        // 게시글을 작성한 회원과 지우려는 회원이 일치하는지 확인
        // Spring Security를 사용하여 현재 로그인한 회원과 게시글을 작성한 회원의 일치 여부 판별

        // 일단은 일치한다고 생각하고 로직 작성
        Ask ask = askRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 게시물입니다. 다시 시도해주세요"));

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
}
