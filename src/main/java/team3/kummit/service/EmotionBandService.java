package team3.kummit.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import team3.kummit.domain.EmotionBand;
import team3.kummit.domain.Member;
import team3.kummit.domain.Song;
import team3.kummit.dto.*;
import team3.kummit.exception.ResourceNotFoundException;
import team3.kummit.repository.EmotionBandRepository;
import team3.kummit.repository.SongRepository;

@Service
@RequiredArgsConstructor
public class EmotionBandService {
    private final EmotionBandRepository emotionBandRepository;
    private final EmotionBandLikeService emotionBandLikeService;
    private final CommentService commentService;
    private final SongRepository songRepository;
    private final MemberService memberService;

    @Transactional
    public Long createEmotionBand(Long memberId, EmotionBandCreateRequest emotionBandCreateRequest) {
        Member member = memberService.findById(memberId);
        EmotionBand emotionBand = emotionBandRepository.save(EmotionBand.builder()
                .creator(member)
                .creatorName(member.getName())
                .emotion(emotionBandCreateRequest.getEmotion())
                .description(emotionBandCreateRequest.getDescription())
                .endTime(LocalDateTime.now().plusDays(1))
                .likeCount(0)
                .peopleCount(0)
                .songCount(1)
                .commentCount(0).build());
        Song song = songRepository.save(Song.builder()
                .creator(member)
                .emotionBand(emotionBand)
                .creatorName(member.getName())
                .title(emotionBandCreateRequest.getSong().getTitle())
                .artist(emotionBandCreateRequest.getSong().getArtist())
                .albumImageLink(emotionBandCreateRequest.getSong().getAlbumImageLink())
                .previewLink(emotionBandCreateRequest.getSong().getPreviewLink())
                .createdAt(LocalDateTime.now())
                .build());
        member.incrementBandCreateCount();
        member.incrementSongAddCount();
        return emotionBand.getId();
    }

    @Transactional(readOnly = true)
    public EmotionBandListResponse getEmotionBandLists(Long memberId) {
        LocalDateTime currentTime = LocalDateTime.now();

        // 인기 밴드 조회 (좋아요 수 기준 상위 3개)
        List<EmotionBand> popularBands = emotionBandRepository.findTop3ByLikeCountAndEndTimeAfter(currentTime);
        List<EmotionBandResponse> popularBandResponses = popularBands.stream()
                .limit(3) // 최대 3개만
                .map(band -> {
                    boolean isLiked = memberId != null && emotionBandLikeService.isLiked(band.getId(), memberId);
                    return EmotionBandResponse.from(band, isLiked);
                })
                .collect(Collectors.toList());

        // 전체 밴드 조회 (최신순 상위 10개)
        List<EmotionBand> allBands = emotionBandRepository.findTop10ByEndTimeDescAndEndTimeAfter(currentTime);
        List<EmotionBandResponse> allBandResponses = allBands.stream()
                .limit(10) // 최대 10개만
                .map(band -> {
                    boolean isLiked = memberId != null && emotionBandLikeService.isLiked(band.getId(), memberId);
                    return EmotionBandResponse.from(band, isLiked);
                })
                .collect(Collectors.toList());

        return EmotionBandListResponse.builder()
                .popularBands(popularBandResponses)
                .allBands(allBandResponses)
                .build();
    }

    @Transactional(readOnly = true)
    public EmotionBandResponse getEmotionBand(Long emotionBandId, Long memberId) {
        EmotionBand band = emotionBandRepository.findById(emotionBandId)
                .orElseThrow(() -> new ResourceNotFoundException("감정밴드를 찾을 수 없습니다."));

        boolean isLiked = memberId != null && emotionBandLikeService.isLiked(emotionBandId, memberId);
        return EmotionBandResponse.from(band, isLiked);
    }

    @Transactional(readOnly = true)
    public EmotionBandDetailResponse getEmotionBandDetail(Long emotionBandId) {
        // 감정밴드 조회
        EmotionBand emotionBand = emotionBandRepository.findById(emotionBandId)
                .orElseThrow(() -> new ResourceNotFoundException("감정밴드를 찾을 수 없습니다."));

        // 음악 목록 조회
        List<SongResponse> songs = songRepository.findByEmotionBandIdOrderByCreatedAtDesc(emotionBandId)
                .stream()
                .map(SongResponse::from)
                .collect(Collectors.toList());

        // 댓글 목록 조회
        List<CommentResponse> comments = commentService.getComments(emotionBandId);

        return EmotionBandDetailResponse.from(emotionBand, songs, comments);
    }
}
