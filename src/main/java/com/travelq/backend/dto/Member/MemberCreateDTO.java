package com.travelq.backend.dto.Member;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.io.Serializable;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
@Builder
@ToString
public class MemberCreateDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    // 이름
    @NotBlank(message = "이름을 입력해주세요")
    private String name;

    //닉네임
    @NotBlank(message = "닉네임을 입력해주세요")
    private String nickname;

    // 이메일
    @NotBlank(message = "이메일을 입력해주세요")
    private String email;

    // 생년월일
    @NotBlank(message = "생년월일을 입력해주세요")
    @Column(name = "birthday", nullable = false, length = 8)
    private String birthday;

    // 주소
    private String address;
}
