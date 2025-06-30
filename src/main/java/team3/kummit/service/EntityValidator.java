package team3.kummit.service;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import team3.kummit.domain.EmotionBand;
import team3.kummit.domain.Member;
import team3.kummit.exception.ResourceNotFoundException;
import team3.kummit.repository.EmotionBandRepository;
import team3.kummit.repository.MemberRepository;

@Service
@RequiredArgsConstructor
public class EntityValidator {

    private final MemberRepository memberRepository;
    private final EmotionBandRepository emotionBandRepository;

    public Member validateMember(Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new ResourceNotFoundException("사용자를 찾을 수 없습니다."));
    }

    public EmotionBand validateActiveEmotionBand(Long emotionBandId) {
        EmotionBand emotionBand = emotionBandRepository.findById(emotionBandId)
                .orElseThrow(() -> new ResourceNotFoundException("감정밴드를 찾을 수 없습니다."));

        if (emotionBand.getEndTime().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("이미 종료된 감정밴드입니다.");
        }

        return emotionBand;
    }
}
