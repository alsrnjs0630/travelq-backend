package com.travelq.backend.service.Recommend;

import com.travelq.backend.dto.Recommend.RcmRequestDTO;
import com.travelq.backend.dto.Recommend.RcmResponseDTO;
import com.travelq.backend.dto.Recommend.RcmUpdateDTO;
import com.travelq.backend.dto.common.ApiResponseDTO;
import com.travelq.backend.dto.common.PageDTO;
import com.travelq.backend.entity.Member;
import com.travelq.backend.entity.Recommend;
import com.travelq.backend.entity.RecommendImg;
import com.travelq.backend.mapper.PageMapper;
import com.travelq.backend.mapper.RcmMapper;
import com.travelq.backend.repository.MemberRepository;
import com.travelq.backend.repository.RecommendImgRepository;
import com.travelq.backend.repository.RecommendRepository;
import com.travelq.backend.util.CustomFileUtil;
import com.travelq.backend.util.CustomUserDetail;
import com.travelq.backend.util.commonCode.StatusCode;
import com.travelq.backend.util.search.PostSearchSpecs;
import com.travelq.backend.util.search.PostSpecs;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class RcmServiceImpl implements RcmService {

    private final RecommendImgRepository recommendImgRepository;
    private final RecommendRepository recommendRepository;
    private final MemberRepository memberRepository;
    private final CustomFileUtil customFileUtil;
    private final PageMapper pageMapper;
    private final RcmMapper rcmMapper;

    // 추천 게시글 목록
    @Override
    public PageDTO<RcmResponseDTO, PostSearchSpecs> getRecommendList(PostSearchSpecs postSearchSpecs, Pageable pageable) {
        // 검색 조건 postSearchSpecs에 기반하여 Specification 생성
        Specification<Recommend> spec = PostSpecs.bySearch(postSearchSpecs);

        // 페이지 번호 조정 (URL 파라미터로 받아온 Pageable의 페이지 번호 - 1 (Spring은 0페이지 부터 시작))
        int pageNum = (pageable.getPageNumber() < 1) ? 0 : pageable.getPageNumber() - 1;
        Pageable correctedPageable = PageRequest.of(pageNum, pageable.getPageSize(), pageable.getSort());

        return pageMapper.toPageDTO(recommendRepository.findAll(spec, correctedPageable).map(rcmMapper::toResponseDTO), postSearchSpecs);
    }

    // 추천 게시글 조회
    @Override
    public ResponseEntity<ApiResponseDTO<RcmResponseDTO>> getRecommend(Long id) {
        // 게시글 DB조회
        Recommend recommend = recommendRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("존재하지 않는 게시글입니다."));

        // 조회수 증가
        recommend.updateViewCount(recommend.getViewCount() + 1);
        recommendRepository.save(recommend);

        // 응답 DTO 생성
        RcmResponseDTO responseDTO = rcmMapper.toResponseDTO(recommend);

        // 게시글 이미지 리스트
        List<RecommendImg> rcmImages = recommendImgRepository.findByRecommendId(id);

        // 이미지 URL 리스트로 변환 후 responseDTO에 저장
        List<String> imagleUrls = customFileUtil.toImageUrls(rcmImages.stream().map(RecommendImg::getImageName).toList());

        responseDTO = responseDTO.toBuilder().images(imagleUrls).build();

        ApiResponseDTO<RcmResponseDTO> response = ApiResponseDTO.<RcmResponseDTO>builder()
                .success(true)
                .message("게시글 조회 성공")
                .data(responseDTO)
                .build();

        return ResponseEntity.ok(response);
    }

    // 추천 게시글 등록
    @Override
    public ResponseEntity<ApiResponseDTO<RcmRequestDTO>> createRcm(RcmRequestDTO rcmRequestDTO, Authentication authentication) throws IOException {
        // 회원 정보
        CustomUserDetail customUserDetail = (CustomUserDetail) authentication.getPrincipal();

        Member member = memberRepository.findByEmail(customUserDetail.getUsername())
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 회원입니다."));

        // 추천 게시글 등록
        Recommend rcm = Recommend.builder()
                .member(member)
                .author(member.getNickName())
                .title(rcmRequestDTO.getTitle())
                .content(rcmRequestDTO.getContent())
                .likeCount(0)
                .reportCount(0)
                .viewCount(0)
                .state(StatusCode.NORMAL)
                .build();
        recommendRepository.save(rcm);

        // 업로드 이미지 등록
        if (rcmRequestDTO.getImages() != null && !rcmRequestDTO.getImages().isEmpty()) {
            /*
             * 이미지명 변경 후 지정된 경로로 저장
             * 이미지명 = UUID + 이미지명
             * 지정된 경로 : file.rcmDir
             * */
            List<String> fileNames = customFileUtil.saveRcmImage(rcmRequestDTO.getImages());

            // DB 저장
            List<RecommendImg> images = fileNames.stream().map(image -> RecommendImg.builder()
                            .recommend(rcm)
                            .imageName(image)
                            .isDeleted(false)
                            .build())
                    .toList();
            recommendImgRepository.saveAll(images);
        }

        ApiResponseDTO<RcmRequestDTO> apiResponseDTO = ApiResponseDTO.<RcmRequestDTO>builder()
                .success(true)
                .message("추천 게시글 등록 완료")
                .data(rcmRequestDTO)
                .build();

        return ResponseEntity.ok(apiResponseDTO);
    }

    // 추천 게시글 수정 권한 확인
    @Override
    public ResponseEntity<ApiResponseDTO<?>> updateAuthCheck(Long id, Authentication authentication) throws IOException {
        // 현재 로그인한 유저 정보
        CustomUserDetail customUserDetail = (CustomUserDetail) authentication.getPrincipal();
        Member authedUser = memberRepository.findByEmail(customUserDetail.getUsername())
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 회원입니다."));

        // 게시글 정보
        Recommend rcm = recommendRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("존재하지 않는 게시글입니다."));

        // 작성자와 로그인 유저 일치 판별
        if (!rcm.getMember().equals(authedUser)) {
            // 작성자와 일치하지 않는 경우 수정 권한 X
            ApiResponseDTO<?> response = ApiResponseDTO.builder()
                    .success(false)
                    .message("수정 권한이 없습니다.")
                    .data(null)
                    .build();

            return ResponseEntity.ok(response);
        }

        ApiResponseDTO<?> response = ApiResponseDTO.builder()
                .success(true)
                .message("수정 권한 확인")
                .data(null)
                .build();

        return ResponseEntity.ok(response);
    }

    // 추천 게시글 수정
    @Override
    public ResponseEntity<ApiResponseDTO<RcmUpdateDTO>> updateRcm(Long id, RcmUpdateDTO rcmUpdateDTO, Authentication authentication) throws IOException {
        // 현재 인증된 사용자
        CustomUserDetail customUserDetail = (CustomUserDetail) authentication.getPrincipal();
        Member loginUser = memberRepository.findByEmail(customUserDetail.getUsername())
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 회원입니다."));

        // 현재 게시물
        Recommend rcm = recommendRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("존재하지 않는 게시물입니다."));

        // 게시물 수정 권한 확인 (권한 없는 경우)
        if (!loginUser.equals(rcm.getMember())) {
            ApiResponseDTO<RcmUpdateDTO> response = ApiResponseDTO.<RcmUpdateDTO>builder()
                    .success(false)
                    .message("수정 권한이 없습니다.")
                    .data(rcmUpdateDTO)
                    .build();

            return ResponseEntity.ok(response);
        }

        // 게시글 수정
        if (!rcmUpdateDTO.getTitle().equals(rcm.getTitle())) {
            rcm.updateTitle(rcmUpdateDTO.getTitle());
        }

        if (!rcmUpdateDTO.getContent().equals(rcm.getContent())) {
            rcm.updateContent(rcmUpdateDTO.getContent());
        }

        // 새 이미지 등록
        if (rcmUpdateDTO.getNewImages() != null && !rcmUpdateDTO.getNewImages().isEmpty()) {
            List<String> updateImgNames = customFileUtil.saveRcmImage(rcmUpdateDTO.getNewImages());

            // DB 저장
            List<RecommendImg> updatedImages = updateImgNames.stream().map(image -> RecommendImg.builder()
                            .recommend(rcm)
                            .imageName(image)
                            .isDeleted(false)
                            .build())
                    .toList();
            recommendImgRepository.saveAll(updatedImages);
        }

        // 삭제 이미지 상태 변경
        if (rcmUpdateDTO.getDeletedImages() != null && !rcmUpdateDTO.getDeletedImages().isEmpty()) {
            for (String imgName : rcmUpdateDTO.getDeletedImages()) {
                RecommendImg delImg = recommendImgRepository.findByImageName(imgName)
                        .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 이미지 파일입니다."));

                delImg.deleteImage();
                recommendImgRepository.delete(delImg);
            }
        }

        ApiResponseDTO<RcmUpdateDTO> response = ApiResponseDTO.<RcmUpdateDTO>builder()
                .success(true)
                .message("수정되었습니다.")
                .data(rcmUpdateDTO)
                .build();

        return ResponseEntity.ok(response);
    }

    // 추천 게시글 삭제
    @Override
    public ResponseEntity<ApiResponseDTO<?>> deleteRcm(Long id, Authentication authentication) throws IOException {
        // 현재 인증된 사용자
        CustomUserDetail customUserDetail = (CustomUserDetail) authentication.getPrincipal();
        Member loginUser = memberRepository.findByEmail(customUserDetail.getUsername())
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 회원입니다."));

        // 현재 게시물
        Recommend rcm = recommendRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("존재하지 않는 게시물입니다."));

        // 게시물 삭제 권한 확인 (권한 없는 경우)
        if (!loginUser.equals(rcm.getMember())) {
            ApiResponseDTO<RcmUpdateDTO> response = ApiResponseDTO.<RcmUpdateDTO>builder()
                    .success(false)
                    .message("삭제 권한이 없습니다.")
                    .data(null)
                    .build();

            return ResponseEntity.ok(response);
        }

        // 게시글 삭제
        rcm.updateState(StatusCode.DELETED);
        recommendRepository.save(rcm);

        ApiResponseDTO<?> response = ApiResponseDTO.builder()
                .success(true)
                .message("게시글이 삭제되었습니다.")
                .data(null)
                .build();

        return ResponseEntity.ok(response);
    }
}
