package team3.kummit.service;

import java.time.LocalDateTime;

import org.springframework.stereotype.Component;

import team3.kummit.domain.EmotionBand;
import team3.kummit.domain.Member;
import team3.kummit.exception.ResourceNotFoundException;

@Component
public class EntityValidator {
    public void validateMember(Member member) {
        if (member == null) {
            throw new ResourceNotFoundException("사용자를 찾을 수 없습니다.");
        }
    }

    public void validateActiveEmotionBand(EmotionBand emotionBand) {
        if (emotionBand == null) {
            throw new ResourceNotFoundException("감정밴드를 찾을 수 없습니다.");
        }
        if (emotionBand.getEndTime().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("이미 종료된 감정밴드입니다.");
        }
    }
}
