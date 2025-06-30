package team3.kummit.service.comment;

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

import team3.kummit.api.CommentRequest;
import team3.kummit.api.CommentResponse;
import team3.kummit.domain.Comment;
import team3.kummit.domain.EmotionBand;
import team3.kummit.domain.Member;
import team3.kummit.exception.ResourceNotFoundException;
import team3.kummit.repository.CommentRepository;
import team3.kummit.repository.EmotionBandRepository;
import team3.kummit.repository.MemberRepository;
import team3.kummit.service.CommentService;

@ExtendWith(MockitoExtension.class)
class CommentCreateServiceTest {

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
    private CommentRequest testCommentRequest;
    private Comment savedComment;

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

        testCommentRequest = new CommentRequest("테스트 댓글입니다.");

        savedComment = Comment.builder()
                .id(1L)
                .emotionBand(testEmotionBand)
                .creator(testMember)
                .creatorName("테스트유저")
                .comment("테스트 댓글입니다.")
                .createdAt(LocalDateTime.now())
                .build();
    }

    @Test
    @DisplayName("댓글 생성 성공 및 댓글 수 업데이트 확인")
    void createComment_Success() {
        Long emotionBandId = 1L;
        Long memberId = 1L;

        when(emotionBandRepository.findById(emotionBandId))
                .thenReturn(java.util.Optional.of(testEmotionBand));
        when(memberRepository.findById(memberId))
                .thenReturn(java.util.Optional.of(testMember));
        when(commentRepository.save(any(Comment.class)))
                .thenReturn(savedComment);
        when(emotionBandRepository.save(any(EmotionBand.class)))
                .thenReturn(testEmotionBand.toBuilder().commentCount(1).build());

        CommentResponse result = commentService.createComment(emotionBandId, memberId, testCommentRequest);

        assertThat(result).isNotNull();
        assertThat(result.getComment()).isEqualTo("테스트 댓글입니다.");
        assertThat(result.getCreatorName()).isEqualTo("테스트유저");

        verify(emotionBandRepository).save(any(EmotionBand.class));
    }

    @Test
    @DisplayName("존재하지 않는 감정밴드에 댓글 생성 시 예외 발생")
    void createComment_EmotionBandNotFound_ThrowsException() {
        Long emotionBandId = 999L;
        Long memberId = 1L;

        when(emotionBandRepository.findById(emotionBandId))
                .thenReturn(java.util.Optional.empty());

        assertThatThrownBy(() -> commentService.createComment(emotionBandId, memberId, testCommentRequest))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("감정밴드를 찾을 수 없습니다.");
    }

    @Test
    @DisplayName("종료된 감정밴드에 댓글 생성 시 예외 발생")
    void createComment_ExpiredEmotionBand_ThrowsException() {
        // given
        Long emotionBandId = 1L;
        Long memberId = 1L;

        EmotionBand expiredEmotionBand = testEmotionBand.toBuilder()
                .endTime(LocalDateTime.now().minusDays(1))
                .build();

        when(emotionBandRepository.findById(emotionBandId))
                .thenReturn(java.util.Optional.of(expiredEmotionBand));

        // when & then
        assertThatThrownBy(() -> commentService.createComment(emotionBandId, memberId, testCommentRequest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("종료된 감정밴드에는 댓글을 작성할 수 없습니다.");
    }
}
