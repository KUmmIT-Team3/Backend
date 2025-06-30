package team3.kummit.service.comment;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import team3.kummit.api.CommentResponse;
import team3.kummit.domain.Comment;
import team3.kummit.domain.EmotionBand;
import team3.kummit.domain.Member;
import team3.kummit.repository.CommentRepository;
import team3.kummit.repository.EmotionBandRepository;
import team3.kummit.repository.MemberRepository;
import team3.kummit.service.CommentService;

@ExtendWith(MockitoExtension.class)
class CommentGetServiceTest {

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private EmotionBandRepository emotionBandRepository;

    @Mock
    private MemberRepository memberRepository;

    @InjectMocks
    private CommentService commentService;

    private Member testMember;
    private EmotionBand testEmotionBand;
    private Comment testComment1;
    private Comment testComment2;

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
                .build();

        testComment1 = Comment.builder()
                .id(1L)
                .emotionBand(testEmotionBand)
                .creator(testMember)
                .creatorName("테스트유저")
                .comment("첫 번째 댓글")
                .createdAt(LocalDateTime.now().minusHours(1))
                .build();

        testComment2 = Comment.builder()
                .id(2L)
                .emotionBand(testEmotionBand)
                .creator(testMember)
                .creatorName("테스트유저")
                .comment("두 번째 댓글")
                .createdAt(LocalDateTime.now())
                .build();
    }

    @Test
    @DisplayName("감정밴드 댓글 목록 조회")
    void getComments_Success() {
        Long emotionBandId = 1L;
        List<Comment> comments = Arrays.asList(testComment2, testComment1);
        when(commentRepository.findByEmotionBandIdOrderByCreatedAtDesc(emotionBandId))
                .thenReturn(comments);

        List<CommentResponse> result = commentService.getComments(emotionBandId);

        assertThat(result).hasSize(2);
        // 최신 댓글이 먼저 나와야 함
        assertThat(result.get(0).getId()).isEqualTo(2L);
        assertThat(result.get(0).getComment()).isEqualTo("두 번째 댓글");
        assertThat(result.get(1).getId()).isEqualTo(1L);
        assertThat(result.get(1).getComment()).isEqualTo("첫 번째 댓글");
    }

    @Test
    @DisplayName("댓글이 없는 감정밴드의 댓글 조회")
    void getComments_EmptyList() {
        Long emotionBandId = 1L;
        when(commentRepository.findByEmotionBandIdOrderByCreatedAtDesc(emotionBandId))
                .thenReturn(Arrays.asList());

        List<CommentResponse> result = commentService.getComments(emotionBandId);

        assertThat(result).isEmpty();
    }
}
