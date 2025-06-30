package team3.kummit.controller;

import java.util.List;

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
import team3.kummit.api.CommentRequest;
import team3.kummit.api.CommentResponse;
import team3.kummit.service.CommentService;

@Tag(name = "Comment", description = "댓글 관리")
@RequestMapping("/api/emotion-bands/{emotionBandId}/comments")
@RestController
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @Operation(summary = "댓글 작성", description = "감정밴드에 댓글을 작성합니다")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "잘못된 요청 (종료된 밴드, 입력 데이터 오류 등)"),
            @ApiResponse(responseCode = "404", description = "감정밴드 또는 사용자를 찾을 수 없음"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @PostMapping
    public ResponseEntity<CommentResponse> createComment(
            @Parameter(description = "감정밴드 ID") @PathVariable Long emotionBandId,
            @Parameter(description = "사용자 ID") @RequestParam Long memberId,
            @Valid @RequestBody CommentRequest request) {
        CommentResponse response = commentService.createComment(emotionBandId, memberId, request);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "댓글 목록 조회", description = "감정밴드의 모든 댓글을 조회합니다")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "감정밴드를 찾을 수 없음"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @GetMapping
    public ResponseEntity<List<CommentResponse>> getComments(
            @Parameter(description = "감정밴드 ID") @PathVariable Long emotionBandId) {
        List<CommentResponse> comments = commentService.getComments(emotionBandId);
        return ResponseEntity.ok(comments);
    }

}
