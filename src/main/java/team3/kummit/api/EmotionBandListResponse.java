package team3.kummit.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmotionBandListResponse {
    private List<EmotionBandResponse> popularBands; // 인기 밴드 (좋아요 수 기준 상위 3개)
    private List<EmotionBandResponse> allBands;     // 전체 밴드 (최신순 상위 10개)
}
