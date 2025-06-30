package team3.kummit.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
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

import team3.kummit.api.SongRequest;
import team3.kummit.api.SongResponse;
import team3.kummit.domain.EmotionBand;
import team3.kummit.domain.Member;
import team3.kummit.domain.Song;
import team3.kummit.exception.ResourceNotFoundException;
import team3.kummit.repository.EmotionBandRepository;
import team3.kummit.repository.MemberRepository;
import team3.kummit.repository.SongRepository;

@ExtendWith(MockitoExtension.class)
class SongServiceTest {

    @Mock
    private SongRepository songRepository;

    @Mock
    private EmotionBandRepository emotionBandRepository;

    @Mock
    private MemberRepository memberRepository;

    @InjectMocks
    private SongService songService;

    private Member testMember;
    private EmotionBand testEmotionBand;
    private SongRequest testSongRequest;
    private Song savedSong;

    @BeforeEach
    void setUp() {
        testMember = Member.builder()
                .id(1L)
                .name("테스트유저")
                .email("test@test.com")
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

        testSongRequest = new SongRequest(
                "https://is1-ssl.mzstatic.com/image/thumb/Music/3e/f7/85/mzi.qrpfabxe.jpg/100x100bb.jpg",
                "첫사랑이죠 (Instrumental)",
                "아이유 & 나윤권",
                "https://audio-ssl.itunes.apple.com/itunes-assets/AudioPreview113/v4/9a/5c/aa/9a5caac8-1e46-3c9b-1024-1bc855eeb513/mzaf_8752617331599882863.plus.aac.p.m4a"
        );

        savedSong = Song.builder()
                .id(1L)
                .emotionBand(testEmotionBand)
                .creator(testMember)
                .creatorName("테스트유저")
                .title("첫사랑이죠 (Instrumental)")
                .artist("아이유 & 나윤권")
                .albumImageLink("https://is1-ssl.mzstatic.com/image/thumb/Music/3e/f7/85/mzi.qrpfabxe.jpg/100x100bb.jpg")
                .previewLink("https://audio-ssl.itunes.apple.com/itunes-assets/AudioPreview113/v4/9a/5c/aa/9a5caac8-1e46-3c9b-1024-1bc855eeb513/mzaf_8752617331599882863.plus.aac.p.m4a")
                .createdAt(LocalDateTime.now())
                .build();
    }

    @Test
    @DisplayName("음악 추가 성공 - songCount 업데이트 확인")
    void addSong_Success() {
        Long emotionBandId = 1L;
        Long memberId = 1L;

        when(emotionBandRepository.findById(emotionBandId))
                .thenReturn(java.util.Optional.of(testEmotionBand));
        when(memberRepository.findById(memberId))
                .thenReturn(java.util.Optional.of(testMember));
        when(songRepository.save(any(Song.class)))
                .thenReturn(savedSong);
        when(emotionBandRepository.save(any(EmotionBand.class)))
                .thenReturn(testEmotionBand.toBuilder().songCount(1).build());
        when(memberRepository.save(any(Member.class)))
                .thenReturn(testMember.toBuilder().songAddCount(1).build());

        SongResponse result = songService.addSong(emotionBandId, memberId, testSongRequest);

        assertThat(result).isNotNull();
        assertThat(result.getTitle()).isEqualTo("첫사랑이죠 (Instrumental)");
        assertThat(result.getArtist()).isEqualTo("아이유 & 나윤권");
        assertThat(result.getCreatorName()).isEqualTo("테스트유저");
        assertThat(result.getEmotionBandId()).isEqualTo(1L);
        assertThat(result.getEmotion()).isEqualTo("기쁨");

        verify(emotionBandRepository).save(any(EmotionBand.class));
        verify(memberRepository).save(any(Member.class));
    }

    @Test
    @DisplayName("존재하지 않는 감정밴드에 음악 추가 시 예외 발생")
    void addSong_EmotionBandNotFound_ThrowsException() {
        Long emotionBandId = 999L;
        Long memberId = 1L;

        when(emotionBandRepository.findById(emotionBandId))
                .thenReturn(java.util.Optional.empty());

        assertThatThrownBy(() -> songService.addSong(emotionBandId, memberId, testSongRequest))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("감정밴드를 찾을 수 없습니다.");
    }

    @Test
    @DisplayName("존재하지 않는 사용자로 음악 추가 시 예외 발생")
    void addSong_MemberNotFound_ThrowsException() {
        Long emotionBandId = 1L;
        Long memberId = 999L;

        when(emotionBandRepository.findById(emotionBandId))
                .thenReturn(java.util.Optional.of(testEmotionBand));
        when(memberRepository.findById(memberId))
                .thenReturn(java.util.Optional.empty());

        assertThatThrownBy(() -> songService.addSong(emotionBandId, memberId, testSongRequest))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("사용자를 찾을 수 없습니다.");
    }

    @Test
    @DisplayName("종료된 감정밴드에 음악 추가 시 예외 발생")
    void addSong_ExpiredEmotionBand_ThrowsException() {
        Long emotionBandId = 1L;
        Long memberId = 1L;

        EmotionBand expiredEmotionBand = testEmotionBand.toBuilder()
                .endTime(LocalDateTime.now().minusDays(1))
                .build();

        when(emotionBandRepository.findById(emotionBandId))
                .thenReturn(java.util.Optional.of(expiredEmotionBand));

        assertThatThrownBy(() -> songService.addSong(emotionBandId, memberId, testSongRequest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("종료된 감정밴드에는 음악을 추가할 수 없습니다.");
    }
}
