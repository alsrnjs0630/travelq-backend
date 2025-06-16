package com.travelq.backend.util;

import com.travelq.backend.entity.RecommendImg;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;


/**
 * The type Custom file util.
 */
@Slf4j
@Component
public class CustomFileUtil {

    // 추천 게시물 이미지 파일 저장 경로
    @Value("${file.rcmDir}")
    private String fileRcmDir;

    // 이미지 URL 기본 경로
    private static final String BASE_IMAGE_URL = "http://localhost:8080/resources/recommends/";

    /**
     * 이미지 파일명 URL로 변환
     *
     * @param fileName 업로드 파일명
     * @return the string
     */
    public static String toImageUrl(String fileName) {
        return BASE_IMAGE_URL + fileName;
    }

    /**
     * 이미지 경로 초기화
     *
     * @throws IOException the io exception
     */
    @PostConstruct
    public void init() throws IOException {
        Path tempFolderPath = Paths.get(fileRcmDir);
        // 디렉토리가 존재하지 않으면 생성, 존재하면 아무작업 하지 않음
        Files.createDirectories(tempFolderPath);
        // 절대 경로로 변환
        fileRcmDir = tempFolderPath.toAbsolutePath().toString();

        log.info("추천 게시판 이미지 파일 업로드 경로 : {}", fileRcmDir);
    }

    /**
     * 이미지 저장
     *
     * @param files 업로드 파일 리스트
     * @return 업로드 파일 리스트
     * @throws IOException the io exception
     */
    public List<String> saveRcmImage(List<MultipartFile> files) throws IOException {
        if (files == null || files.isEmpty()) {
            return null;
        }

        // 업로드 파일 리스트
        List<String> fileNames = new ArrayList<>();

        for (MultipartFile file : files) {
            // UUID를 사용하여 중복되는 파일명이 없도록 함
            String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();

            // 파일 저장 경로
            Path path = Paths.get(fileRcmDir, fileName);

            try {
                // 설정한 경로로 파일 저장
                Files.copy(file.getInputStream(), path);
                fileNames.add(fileName);
            } catch (IOException e) {
                throw new RuntimeException("이미지 파일 업로드 중 에러 발생");
            }
        }

        return fileNames;
    }

    /**
     * 이미지 파일명 리스트 URL로 변환
     *
     * @param fileNames 이미지 파일명 리스트
     * @return the list
     */
    public List<String> toImageUrls(List<String> fileNames) {
        // 리스트가 비어있다면 빈 리스트 반환
        if (fileNames == null || fileNames.isEmpty()) {
            return Collections.emptyList();
        }
        return fileNames.stream()
                .map(CustomFileUtil::toImageUrl)
                .collect(Collectors.toList());
    }
}
