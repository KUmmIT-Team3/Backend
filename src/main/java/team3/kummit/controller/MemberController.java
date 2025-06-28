package team3.kummit.controller;


import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import team3.kummit.domain.EmotionBand;
import team3.kummit.domain.Member;
import team3.kummit.dto.*;
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
        if(member == null){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        return ResponseEntity.ok(new MemberLoginResponse(member.getId()));
    }

    @Operation(
        summary = "회원 프로필 조회",
        description = "회원 ID를 통해 회원의 프로필 정보를 조회합니다. 회원 가입 날짜, 밴드 생성/참여 횟수, 좋아요 수, 추가한 곡 수 등의 정보를 반환합니다.",
        parameters = {
            @Parameter(name = "memberId", description = "사용자 ID (선택사항)", required = false)
        }
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "프로필 조회 성공"),
        @ApiResponse(responseCode = "404", description = "회원 정보를 찾을 수 없음")
    })
    @GetMapping("/profile")
    public ResponseEntity<MemberProfileResponse> memberProfileGet(
            @Parameter(description = "사용자 ID (선택사항)") @RequestParam Long memberId) {
        Member member = memberService.findById(memberId);
        return ResponseEntity.status(HttpStatus.CREATED).body(new MemberProfileResponse(
                member.getName(),
                member.getSignUpDate(),
                member.getBandCreateCount() != null ? member.getBandCreateCount() : 0,
                member.getBandJoinCount() != null ? member.getBandJoinCount() : 0,
                member.getLikeCount() != null ? member.getLikeCount() : 0,
                member.getSongAddCount() != null ? member.getSongAddCount() : 0));
    }

    @Operation(
        summary = "사용자가 생성한 감정 밴드 목록 조회",
        description = "사용자의 ID를 전달받아, 해당 사용자가 생성한 감정 밴드 목록을 조회합니다. 조회된 감정 밴드의 상세 정보가 포함됩니다.",
        parameters = {
            @Parameter(name = "memberId", description = "사용자 ID (선택사항)", required = false)
        }
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "조회 성공"),
        @ApiResponse(responseCode = "404", description = "조회에 필요한 감정 밴드를 찾을 수 없음")
    })
    @GetMapping("/my-band")
    public ResponseEntity<MemberBandResponse> memberMyBand(
            @Parameter(description = "사용자 ID") @RequestParam Long memberId) {
        List<Long> bandIdListOfMember = emotionBandService.findEmotionBandIdListByCreator(memberId);
        List<EmotionBand> allByEmotionBandIdList = emotionBandService.findAllByEmotionBandIdList(bandIdListOfMember);
        List<MemberBandResponseDto> collect = allByEmotionBandIdList.stream().map(this::toDto).collect(Collectors.toList());
        return ResponseEntity.ok(new MemberBandResponse(collect));
    }



    private MemberBandResponseDto toDto(EmotionBand band) {
        return new MemberBandResponseDto(
                band.getEmotion(),
                band.getCreatorName(),
                band.getDescription(),
                band.getSongs().stream()
                        .map(song -> new MemberBandResponseDto.Music(
                                song.getAlbumImageLink(),
                                song.getTitle(),
                                song.getArtist(),
                                song.getPreviewLink()
                        ))
                        .collect(Collectors.toList()),
                band.getLikeCount(),
                band.getPeopleCount(),
                band.getSongCount(),
                band.getCommentCount(),
                band.getEndTime()
        );
    }
















}
