package team3.kummit.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class EmotionBandCreateRequest {

    @NotBlank(message = "감정은 필수입니다")
    private String emotion;

    @NotBlank(message = "설명은 필수입니다")
    private String description;

    @NotNull(message = "음악 정보는 필수입니다")
    @Valid
    private SongDto song;

    @Data
    public static class SongDto{
        @NotBlank(message = "제목은 필수입니다")
        private String title;

        @NotBlank(message = "아티스트는 필수입니다")
        private String artist;

        @NotBlank(message = "앨범 이미지 링크는 필수입니다")
        private String albumImageLink;

        @NotBlank(message = "미리보기 링크는 필수입니다")
        private String previewLink;
    }
}
