package team3.kummit.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import team3.kummit.api.EmotionBandArchiveResponse;
import team3.kummit.domain.EmotionBand;
import team3.kummit.domain.EmotionBandArchive;
import team3.kummit.domain.Member;
import team3.kummit.repository.EmotionBandArchiveRepository;
import team3.kummit.repository.EmotionBandRepository;
import team3.kummit.repository.MemberRepository;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class EmotionBandArchiveServiceTest {

    @Mock
    private EmotionBandArchiveRepository archiveRepository;

    @Mock
    private EmotionBandRepository emotionBandRepository;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private EntityValidator entityValidator;

    @InjectMocks
    private EmotionBandArchiveService emotionBandArchiveService;

    private Member testMember;
    private EmotionBand testEmotionBand;
    private EmotionBandArchive testArchive;

    @BeforeEach
    void setUp() {
        testMember = Member.builder()
                .id(1L)
                .name("테스트유저")
                .email("test@test.com")
                .bandJoinCount(0)
                .build();

        testEmotionBand = EmotionBand.builder()
                .id(1L)
                .creator(testMember)
                .creatorName("테스트유저")
                .emotion("기쁨")
                .description("테스트 감정밴드")
                .endTime(LocalDateTime.now().plusDays(1))
                .likeCount(0)
                .peopleCount(0)
                .songCount(0)
                .commentCount(0)
                .build();

        testArchive = EmotionBandArchive.builder()
                .id(1L)
                .creator(testMember)
                .emotionBand(testEmotionBand)
                .createdAt(LocalDateTime.now())
                .build();
    }

    @Test
    @DisplayName("보관 추가 성공")
    void toggleArchive_AddArchive_Success() {
        // given
        Long emotionBandId = 1L;
        Long memberId = 1L;

        when(emotionBandRepository.findById(emotionBandId))
                .thenReturn(java.util.Optional.of(testEmotionBand));
        when(memberRepository.findById(memberId))
                .thenReturn(java.util.Optional.of(testMember));
        when(archiveRepository.existsByCreatorIdAndEmotionBandId(memberId, emotionBandId))
                .thenReturn(false);
        when(archiveRepository.save(any(EmotionBandArchive.class)))
                .thenReturn(testArchive);
        when(emotionBandRepository.save(any(EmotionBand.class)))
                .thenReturn(testEmotionBand.toBuilder().peopleCount(1).build());
        when(memberRepository.save(any(Member.class)))
                .thenReturn(testMember.toBuilder().bandJoinCount(1).build());
        doNothing().when(entityValidator).validateMember(any());
        doNothing().when(entityValidator).validateActiveEmotionBand(any());

        // when
        EmotionBandArchiveResponse result = emotionBandArchiveService.toggleArchive(emotionBandId, memberId);

        // then
        assertThat(result).isNotNull();
        assertThat(result.isArchived()).isTrue();
        assertThat(result.getMessage()).isEqualTo("보관되었습니다.");
        verify(archiveRepository).save(any(EmotionBandArchive.class));
        verify(emotionBandRepository).save(any(EmotionBand.class));
        verify(memberRepository).save(any(Member.class));
    }

    @Test
    @DisplayName("보관 해제 성공")
    void toggleArchive_RemoveArchive_Success() {
        // given
        Long emotionBandId = 1L;
        Long memberId = 1L;

        when(emotionBandRepository.findById(emotionBandId))
                .thenReturn(java.util.Optional.of(testEmotionBand));
        when(memberRepository.findById(memberId))
                .thenReturn(java.util.Optional.of(testMember));
        when(archiveRepository.existsByCreatorIdAndEmotionBandId(memberId, emotionBandId))
                .thenReturn(true);
        when(archiveRepository.findByCreatorIdAndEmotionBandId(memberId, emotionBandId))
                .thenReturn(java.util.Optional.of(testArchive));
        when(emotionBandRepository.save(any(EmotionBand.class)))
                .thenReturn(testEmotionBand.toBuilder().peopleCount(0).build());
        when(memberRepository.save(any(Member.class)))
                .thenReturn(testMember.toBuilder().bandJoinCount(0).build());
        doNothing().when(entityValidator).validateMember(any());
        doNothing().when(entityValidator).validateActiveEmotionBand(any());

        // when
        EmotionBandArchiveResponse result = emotionBandArchiveService.toggleArchive(emotionBandId, memberId);

        // then
        assertThat(result).isNotNull();
        assertThat(result.isArchived()).isFalse();
        assertThat(result.getMessage()).isEqualTo("보관이 해제되었습니다.");
        verify(archiveRepository).delete(testArchive);
        verify(emotionBandRepository).save(any(EmotionBand.class));
        verify(memberRepository).save(any(Member.class));
    }

    @Test
    @DisplayName("보관 여부 확인 - 보관된 경우")
    void isArchived_WhenArchived_ReturnsTrue() {
        // given
        Long emotionBandId = 1L;
        Long memberId = 1L;

        when(archiveRepository.existsByCreatorIdAndEmotionBandId(memberId, emotionBandId))
                .thenReturn(true);

        // when
        boolean result = emotionBandArchiveService.isArchived(emotionBandId, memberId);

        // then
        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("보관 여부 확인 - 보관되지 않은 경우")
    void isArchived_WhenNotArchived_ReturnsFalse() {
        // given
        Long emotionBandId = 1L;
        Long memberId = 1L;

        when(archiveRepository.existsByCreatorIdAndEmotionBandId(memberId, emotionBandId))
                .thenReturn(false);

        // when
        boolean result = emotionBandArchiveService.isArchived(emotionBandId, memberId);

        // then
        assertThat(result).isFalse();
    }
}
