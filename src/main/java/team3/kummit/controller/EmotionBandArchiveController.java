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
import team3.kummit.api.EmotionBandArchiveResponse;
import team3.kummit.service.EmotionBandArchiveService;

@Tag(name = "EmotionBand-Archive", description = "감정밴드 보관 관리")
@RequestMapping("/api/emotion-bands")
@RestController
@RequiredArgsConstructor
public class EmotionBandArchiveController {

    private final EmotionBandArchiveService emotionBandArchiveService;

    @Operation(summary = "보관 토글", description = "감정밴드를 보관하거나 보관 해제합니다")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "잘못된 요청"),
            @ApiResponse(responseCode = "404", description = "감정밴드 또는 사용자를 찾을 수 없음"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @PostMapping("/{emotionBandId}/archive")
    public ResponseEntity<EmotionBandArchiveResponse> toggleArchive(
            @Parameter(description = "감정밴드 ID") @PathVariable Long emotionBandId,
            @Parameter(description = "사용자 ID") @RequestParam Long memberId) {
        EmotionBandArchiveResponse response = emotionBandArchiveService.toggleArchive(emotionBandId, memberId);
        return ResponseEntity.ok(response);
    }
}
