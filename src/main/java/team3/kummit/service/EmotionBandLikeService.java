package team3.kummit.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import team3.kummit.domain.*;
import team3.kummit.repository.EmotionBandLikeRepository;
import team3.kummit.repository.EmotionBandRepository;
import team3.kummit.repository.MemberRepository;

@Service
@RequiredArgsConstructor
public class EmotionBandLikeService {
    private final EmotionBandLikeRepository likeRepository;
    private final EmotionBandRepository bandRepository;
    private final MemberRepository memberRepository;
    private final EntityValidator entityValidator;

    @Transactional
    public boolean toggleLike(Long emotionBandId, Long memberId) {
        EmotionBand band = bandRepository.findById(emotionBandId).orElse(null);
        Member member = memberRepository.findById(memberId).orElse(null);

        entityValidator.validateActiveEmotionBand(band);
        entityValidator.validateMember(member);

        return likeRepository.findByCreatorIdAndEmotionBandId(memberId, emotionBandId)
                .map(like -> {
                    likeRepository.delete(like);
                    bandRepository.decrementLikeCount(emotionBandId);
                    memberRepository.decrementLikeCount(memberId);
                    return false;
                })
                .orElseGet(() -> {
                    likeRepository.save(EmotionBandLike.builder()
                            .creator(member)
                            .emotionBand(band)
                            .build());
                    bandRepository.incrementLikeCount(emotionBandId);
                    memberRepository.incrementLikeCount(memberId);
                    return true;
                });
    }

    @Transactional(readOnly = true)
    public boolean isLiked(Long emotionBandId, Long memberId) {
        return likeRepository.findByCreatorIdAndEmotionBandId(memberId, emotionBandId).isPresent();
    }

    public List<Long> findEmotionBandListByMemberId(Long memberId) {
        return likeRepository.findEmotionBandIdListByMemberId(memberId);
    }
}
