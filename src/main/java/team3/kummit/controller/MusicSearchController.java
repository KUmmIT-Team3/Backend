package team3.kummit.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import team3.kummit.api.music.MusicSearchResponse;
import team3.kummit.service.music.MusicResponse;
import team3.kummit.service.music.MusicSearchService;


@Slf4j
@Tag(name = "Music Search", description = "음악 검색 API")
@RequestMapping("/api/music")
@RestController
@RequiredArgsConstructor
public class MusicSearchController {

    private final MusicSearchService musicSearchService;

    @Operation(summary = "음악 검색", description = "사용자의 검색어를 기반으로 음악을 검색합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "검색 성공",
                         content = @Content(mediaType = "application/json",
                         schema = @Schema(implementation = MusicSearchResponse.class))),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @GetMapping("/search")
    public ResponseEntity<MusicSearchResponse> listenMusicSearchRequest(
            @Parameter(description = "음악 검색어 (필수)", required = true, example = "IU")
            @RequestParam(value = "query") String userQuery) {
        List<MusicResponse> musicResponses = musicSearchService.searchMusic(userQuery);
        return ResponseEntity.status(HttpStatus.OK)
                             .body(new MusicSearchResponse(musicResponses.size(), musicResponses));
    }
}
