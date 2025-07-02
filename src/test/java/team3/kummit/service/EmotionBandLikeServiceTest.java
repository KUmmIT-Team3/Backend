package team3.kummit.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import team3.kummit.domain.EmotionBand;
import team3.kummit.domain.EmotionBandLike;
import team3.kummit.domain.Member;
import team3.kummit.repository.EmotionBandLikeRepository;
import team3.kummit.repository.EmotionBandRepository;
import team3.kummit.repository.MemberRepository;

@ExtendWith(MockitoExtension.class)
class EmotionBandLikeServiceTest {

    @Mock
    private EmotionBandLikeRepository likeRepository;

    @Mock
    private EmotionBandRepository bandRepository;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private EntityValidator entityValidator;

    @InjectMocks
    private EmotionBandLikeService emotionBandLikeService;

    private Member testMember;
    private EmotionBand testEmotionBand;
    private EmotionBandLike testLike;

    @BeforeEach
    void setUp() {
        testMember = Member.builder()
                .id(1L)
                .name("테스트유저")
                .email("test@test.com")
                .likeCount(0)
                .build();

        testEmotionBand = EmotionBand.builder()
                .id(1L)
                .creator(testMember)
                .creatorName("테스트유저")
                .emotion("기쁨")
                .description("테스트 감정밴드")
                .endTime(LocalDateTime.now().plusDays(1))
                .likeCount(0)
                .peopleCount(3)
                .songCount(2)
                .commentCount(3)
                .build();

        testLike = EmotionBandLike.builder()
                .id(1L)
                .creator(testMember)
                .emotionBand(testEmotionBand)
                .build();
    }

    @Test
    @DisplayName("좋아요 추가 성공")
    void toggleLike_AddLike_Success() {
        Long emotionBandId = 1L;
        Long memberId = 1L;

        when(bandRepository.findById(emotionBandId))
                .thenReturn(Optional.of(testEmotionBand));
        when(memberRepository.findById(memberId))
                .thenReturn(Optional.of(testMember));
        when(likeRepository.findByCreatorIdAndEmotionBandId(memberId, emotionBandId))
                .thenReturn(Optional.empty());
        when(likeRepository.save(any(EmotionBandLike.class)))
                .thenReturn(testLike);
        doNothing().when(entityValidator).validateMember(any());
        doNothing().when(entityValidator).validateActiveEmotionBand(any());

        boolean result = emotionBandLikeService.toggleLike(emotionBandId, memberId);

        assertThat(result).isTrue();
        verify(likeRepository).save(any(EmotionBandLike.class));
        verify(bandRepository).incrementLikeCount(emotionBandId);
        verify(memberRepository).incrementLikeCount(memberId);
    }

    @Test
    @DisplayName("좋아요 해제 성공")
    void toggleLike_RemoveLike_Success() {
        Long emotionBandId = 1L;
        Long memberId = 1L;

        when(bandRepository.findById(emotionBandId))
                .thenReturn(Optional.of(testEmotionBand));
        when(memberRepository.findById(memberId))
                .thenReturn(Optional.of(testMember));
        when(likeRepository.findByCreatorIdAndEmotionBandId(memberId, emotionBandId))
                .thenReturn(Optional.of(testLike));
        doNothing().when(entityValidator).validateMember(any());
        doNothing().when(entityValidator).validateActiveEmotionBand(any());

        boolean result = emotionBandLikeService.toggleLike(emotionBandId, memberId);

        assertThat(result).isFalse();
        verify(likeRepository).delete(testLike);
        verify(bandRepository).decrementLikeCount(emotionBandId);
        verify(memberRepository).decrementLikeCount(memberId);
    }

    @Test
    @DisplayName("좋아요 여부 조회 - 좋아요한 경우")
    void isLiked_WhenLiked_ReturnsTrue() {
        Long emotionBandId = 1L;
        Long memberId = 1L;

        when(likeRepository.findByCreatorIdAndEmotionBandId(memberId, emotionBandId))
                .thenReturn(Optional.of(testLike));

        boolean result = emotionBandLikeService.isLiked(emotionBandId, memberId);

        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("좋아요 여부 조회 - 좋아요하지 않은 경우")
    void isLiked_WhenNotLiked_ReturnsFalse() {
        Long emotionBandId = 1L;
        Long memberId = 1L;

        when(likeRepository.findByCreatorIdAndEmotionBandId(memberId, emotionBandId))
                .thenReturn(Optional.empty());

        boolean result = emotionBandLikeService.isLiked(emotionBandId, memberId);

        assertThat(result).isFalse();
    }
}
