package team3.kummit.controller;


import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import team3.kummit.api.*;
import team3.kummit.api.profile.MemberBandResponse;
import team3.kummit.api.profile.MemberBandResponseDto;
import team3.kummit.api.profile.MemberProfileResponse;
import team3.kummit.controller.parser.MemberApiParser;
import team3.kummit.domain.EmotionBand;
import team3.kummit.domain.Member;
import team3.kummit.service.EmotionBandArchiveService;
import team3.kummit.service.EmotionBandLikeService;
import team3.kummit.service.EmotionBandService;
import team3.kummit.service.MemberService;


@Slf4j
@Tag(name = "회원", description = "회원 API")
@RequestMapping("/api/member")
@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;
    private final EmotionBandService emotionBandService;
    private final EmotionBandLikeService emotionBandLikeService;
    private final EmotionBandArchiveService emotionBandArchiveService;
    private final MemberApiParser memberApiParser;


    @Operation(
        summary = "회원 로그인",
        description = "이름을 가지고 요청을 합니다. 테스트 데이터로 박태희, 소일전 이렇게 넣어놨습니다"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "로그인 성공"),
        @ApiResponse(responseCode = "401", description = "로그인 실패: 인증되지 않음")
    })
    @PostMapping("/login")
    public ResponseEntity<MemberLoginResponse> memberLogin(
            @Parameter(description = "로그인 요청 데이터 (이메일과 비밀번호 포함)")
            @RequestBody MemberLoginRequest memberLoginRequest) {
        Member member = memberService.findByName(memberLoginRequest.name());
        return ResponseEntity.ok(new MemberLoginResponse(member.getId()));
    }

    @Operation(
        summary = "회원 프로필 조회",
        description = "회원 ID를 통해 회원의 프로필 정보를 조회합니다. 회원 가입 날짜, 밴드 생성/참여 횟수, 좋아요 수, 추가한 곡 수 등의 정보를 반환합니다.",
        parameters = {
            @Parameter(name = "memberId", description = "사용자 ID", required = true)
        }
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "프로필 조회 성공"),
        @ApiResponse(responseCode = "404", description = "회원 정보를 찾을 수 없음")
    })
    @GetMapping("/profile")
    public ResponseEntity<MemberProfileResponse> memberProfileGet(
            @Parameter(description = "사용자 ID") @RequestParam Long memberId) {
        Member member = memberService.findById(memberId);
        return ResponseEntity.ok(memberApiParser.mapToMemberProfileResponse(member));
    }

    @Operation(
        summary = "사용자가 생성한 감정 밴드 목록 조회",
        description = "사용자의 ID를 전달받아, 해당 사용자가 생성한 감정 밴드 목록을 조회합니다. 조회된 감정 밴드의 상세 정보가 포함됩니다.",
        parameters = {
            @Parameter(name = "memberId", description = "사용자 ID", required = true)
        }
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "조회 성공"),
        @ApiResponse(responseCode = "404", description = "조회에 필요한 감정 밴드를 찾을 수 없음")
    })
    @GetMapping("/my-band")
    public ResponseEntity<MemberBandResponse> memberMyBand(
            @Parameter(description = "사용자 ID") @RequestParam Long memberId) {
        List<Long> bandIdListOfMember = emotionBandService.findEmotionBandIdListByMemberId(memberId);
        List<EmotionBand> allByEmotionBandIdList = emotionBandService.findAllByEmotionBandIdListWithSongs(bandIdListOfMember);
        List<MemberBandResponseDto> collect = allByEmotionBandIdList.stream().map(memberApiParser::mapToMemberBandResponse).collect(Collectors.toList());
        return ResponseEntity.ok(new MemberBandResponse(collect));
    }

    @Operation(
            summary = "사용자가 공감한 감정 밴드 목록 조회",
            description = "사용자의 ID를 전달받아, 해당 사용자가 공감한 감정 밴드 목록을 조회합니다. 조회된 감정 밴드의 상세 정보가 포함됩니다.",
            parameters = {
                    @Parameter(name = "memberId", description = "사용자 ID", required = true)
            }
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공"),
            @ApiResponse(responseCode = "404", description = "조회에 필요한 감정 밴드를 찾을 수 없음")
    })
    @GetMapping("/like-band")
    public ResponseEntity<MemberBandResponse> memberLikeBand(
            @Parameter(description = "사용자 ID") @RequestParam Long memberId) {
        List<Long> bandIdListOfMember = emotionBandLikeService.findEmotionBandListByMemberId(memberId);
        List<EmotionBand> allByEmotionBandIdList = emotionBandService.findAllByEmotionBandIdListWithSongs(bandIdListOfMember);
        List<MemberBandResponseDto> collect = allByEmotionBandIdList.stream().map(memberApiParser::mapToMemberBandResponse).collect(Collectors.toList());
        return ResponseEntity.ok(new MemberBandResponse(collect));
    }


    @Operation(
            summary = "사용자가 저장한 감정 밴드 목록 조회",
            description = "사용자의 ID를 전달받아, 해당 사용자가 저장한 감정 밴드 목록을 조회합니다. 조회된 감정 밴드의 상세 정보가 포함됩니다.",
            parameters = {
                    @Parameter(name = "memberId", description = "사용자 ID", required = true)
            }
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공"),
            @ApiResponse(responseCode = "404", description = "조회에 필요한 감정 밴드를 찾을 수 없음")
    })
    @GetMapping("/archive-band")
    public ResponseEntity<MemberBandResponse> memberArchiveBand(
            @Parameter(description = "사용자 ID") @RequestParam Long memberId) {
        List<Long> bandIdListOfMember = emotionBandArchiveService.findArchivedEmotionBandIdsByMember(memberId);
        List<EmotionBand> allByEmotionBandIdList = emotionBandService.findAllByEmotionBandIdListWithSongs(bandIdListOfMember);
        List<MemberBandResponseDto> collect = allByEmotionBandIdList.stream().map(memberApiParser::mapToMemberBandResponse).collect(Collectors.toList());
        return ResponseEntity.ok(new MemberBandResponse(collect));
    }


}
