package team3.kummit.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class EmotionBandLikeResponse {

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class LikeToggleResponse {
        private boolean liked;
        private String message;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class LikeStatusResponse {
        private boolean liked;
    }
}
