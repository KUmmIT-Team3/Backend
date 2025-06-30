package team3.kummit.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class EmotionBandArchiveResponse {
    private boolean archived;
    private String message;

    public static EmotionBandArchiveResponse of(boolean archived, String message) {
        return new EmotionBandArchiveResponse(archived, message);
    }
}
