package team3.kummit.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import team3.kummit.api.EmotionBandCreateRequest;
import team3.kummit.api.EmotionBandCreateResponse;
import team3.kummit.api.EmotionBandDetailResponse;
import team3.kummit.api.EmotionBandListResponse;
import team3.kummit.service.EmotionBandService;
import team3.kummit.service.MemberService;

@Tag(name = "EmotionBand", description = "감정밴드 관리")
@RequestMapping("/api/emotion-bands")
@RestController
@RequiredArgsConstructor
public class EmotionBandController {

    private final EmotionBandService emotionBandService;
    private final MemberService memberService;

    @PostMapping()
    @Operation(
        summary = "새 감정밴드 생성",
        description = "새로운 감정밴드를 생성합니다.",
        tags = {"EmotionBand"}
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "감정밴드 생성 성공", content = @Content(mediaType = "application/json")),
        @ApiResponse(responseCode = "400", description = "잘못된 요청"),
        @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    public ResponseEntity<EmotionBandCreateResponse> createEmotionBand(
            @Parameter(description = "사용자 ID") @RequestParam(required = true) Long memberId,
            @Valid @RequestBody EmotionBandCreateRequest emotionBandCreateRequest) {
        Long emotionBandId = emotionBandService.createEmotionBand(memberId, emotionBandCreateRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(new EmotionBandCreateResponse(emotionBandId));
    }

    @Operation(summary = "감정밴드 목록 조회", description = "인기 밴드(좋아요 수 기준 상위 3개)와 전체 밴드(최신순 상위 10개)를 조회합니다")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @GetMapping
    public ResponseEntity<EmotionBandListResponse> getEmotionBandLists(
            @Parameter(description = "사용자 ID (선택사항)") @RequestParam(required = false) Long memberId) {
        EmotionBandListResponse response = emotionBandService.getEmotionBandLists(memberId);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "감정밴드 상세 조회", description = "감정밴드의 상세 정보, 음악 목록, 댓글 목록을 조회합니다")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "감정밴드를 찾을 수 없음"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @GetMapping("/{emotionBandId}/detail")
    public ResponseEntity<EmotionBandDetailResponse> getEmotionBandDetail(
            @Parameter(description = "감정밴드 ID") @PathVariable Long emotionBandId,
            @Parameter(description = "사용자 ID") @RequestParam Long memberId) {
        EmotionBandDetailResponse response = emotionBandService.getEmotionBandDetail(emotionBandId, memberId);
        return ResponseEntity.ok(response);
    }
}
