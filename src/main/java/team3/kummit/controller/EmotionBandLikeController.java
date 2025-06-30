package team3.kummit.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import lombok.RequiredArgsConstructor;
import team3.kummit.api.EmotionBandLikeResponse;
import team3.kummit.service.EmotionBandLikeService;

@Tag(name = "EmotionBand-Like", description = "감정밴드 좋아요 관리")
@RequestMapping("/api/emotion-bands")
@RestController
@RequiredArgsConstructor
public class EmotionBandLikeController {

    private final EmotionBandLikeService emotionBandLikeService;

    @Operation(summary = "좋아요 토글", description = "감정밴드에 좋아요를 누르거나 해제합니다")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "잘못된 요청 (입력 데이터 오류 등)"),
            @ApiResponse(responseCode = "404", description = "감정밴드를 찾을 수 없음"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @PostMapping("/{emotionBandId}/like")
    public ResponseEntity<EmotionBandLikeResponse.LikeToggleResponse> toggleLike(
            @Parameter(description = "감정밴드 ID") @PathVariable Long emotionBandId,
            @Parameter(description = "사용자 ID") @RequestParam Long memberId) {
        boolean liked = emotionBandLikeService.toggleLike(emotionBandId, memberId);
        String message = liked ? "좋아요가 추가되었습니다." : "좋아요가 해제되었습니다.";
        return ResponseEntity.ok(new EmotionBandLikeResponse.LikeToggleResponse(liked, message));
    }
}
