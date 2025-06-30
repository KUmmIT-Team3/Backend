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
import team3.kummit.api.SongRequest;
import team3.kummit.api.SongResponse;
import team3.kummit.service.SongService;

@Tag(name = "Song", description = "음악 관리")
@RequestMapping("/api/emotion-bands/{emotionBandId}/songs")
@RestController
@RequiredArgsConstructor
public class SongController {

    private final SongService songService;

    @Operation(summary = "음악 추가", description = "감정밴드에 음악을 추가합니다")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "잘못된 요청 (종료된 밴드, 입력 데이터 오류 등)"),
            @ApiResponse(responseCode = "404", description = "감정밴드 또는 사용자를 찾을 수 없음"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @PostMapping
    public ResponseEntity<SongResponse> addSong(
            @Parameter(description = "감정밴드 ID") @PathVariable Long emotionBandId,
            @Parameter(description = "사용자 ID") @RequestParam Long memberId,
            @RequestBody SongRequest request) {
        SongResponse response = songService.addSong(emotionBandId, memberId, request);
        return ResponseEntity.ok(response);
    }
}
