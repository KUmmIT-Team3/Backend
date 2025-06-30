package team3.kummit.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import team3.kummit.domain.Comment;
import team3.kummit.domain.EmotionBand;
import team3.kummit.domain.Member;
import team3.kummit.dto.CommentRequest;
import team3.kummit.dto.CommentResponse;
import team3.kummit.repository.CommentRepository;
import team3.kummit.repository.EmotionBandRepository;
import team3.kummit.repository.MemberRepository;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final EmotionBandRepository emotionBandRepository;
    private final MemberRepository memberRepository;
    private final EntityValidator entityValidator;

    @Transactional
    public CommentResponse createComment(Long emotionBandId, Long memberId, CommentRequest request) {
        EmotionBand emotionBand = emotionBandRepository.findById(emotionBandId).orElse(null);
        Member member = memberRepository.findById(memberId).orElse(null);

        entityValidator.validateActiveEmotionBand(emotionBand);
        entityValidator.validateMember(member);

        Comment comment = Comment.builder()
                .emotionBand(emotionBand)
                .creator(member)
                .creatorName(member.getName())
                .comment(request.getComment())
                .build();

        Comment savedComment = commentRepository.save(comment);

        emotionBand.incrementCommentCount();
        emotionBandRepository.save(emotionBand);

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
