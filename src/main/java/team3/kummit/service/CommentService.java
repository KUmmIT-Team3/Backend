package team3.kummit.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import team3.kummit.api.CommentRequest;
import team3.kummit.api.CommentResponse;
import team3.kummit.domain.Comment;
import team3.kummit.domain.EmotionBand;
import team3.kummit.domain.Member;
import team3.kummit.exception.ResourceNotFoundException;
import team3.kummit.repository.CommentRepository;
import team3.kummit.repository.EmotionBandRepository;
import team3.kummit.repository.MemberRepository;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final EmotionBandRepository emotionBandRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public CommentResponse createComment(Long emotionBandId, Long memberId, CommentRequest request) {
        // 감정밴드가 존재하고 아직 종료되지 않았는지 확인
        EmotionBand emotionBand = emotionBandRepository.findById(emotionBandId)
                .orElseThrow(() -> new ResourceNotFoundException("감정밴드를 찾을 수 없습니다."));

        if (emotionBand.getEndTime().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("종료된 감정밴드에는 댓글을 작성할 수 없습니다.");
        }

        // 사용자 확인
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new ResourceNotFoundException("사용자를 찾을 수 없습니다."));

        // 댓글 생성
        Comment comment = Comment.builder()
                .emotionBand(emotionBand)
                .creator(member)
                .creatorName(member.getName())
                .comment(request.getComment())
                .build();

        Comment savedComment = commentRepository.save(comment);

        // 감정밴드의 댓글 수 증가
        Integer currentCommentCount = emotionBand.getCommentCount() != null ? emotionBand.getCommentCount() : 0;
        EmotionBand updatedEmotionBand = emotionBand.toBuilder()
                .commentCount(currentCommentCount + 1)
                .build();
        emotionBandRepository.save(updatedEmotionBand);

        return CommentResponse.from(savedComment);
    }

    @Transactional(readOnly = true)
    public List<CommentResponse> getComments(Long emotionBandId) {
        List<Comment> comments = commentRepository.findByEmotionBandIdOrderByCreatedAtDesc(emotionBandId);
        return comments.stream()
                .map(CommentResponse::from)
                .collect(Collectors.toList());
    }

}
