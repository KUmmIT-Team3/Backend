package team3.kummit.service;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

import team3.kummit.domain.EmotionBand;
import team3.kummit.domain.Member;
import team3.kummit.exception.ResourceNotFoundException;

class EntityValidatorTest {
    private final EntityValidator validator = new EntityValidator();

    @Test
    void validateActiveEmotionBand() {
        EmotionBand band = EmotionBand.builder()
            .endTime(LocalDateTime.now().plusMinutes(10))
            .build();
        validator.validateActiveEmotionBand(band);
    }

    @Test
    void validateActiveEmotionBand_throws_ResourceNotFoundException() {
        assertThatThrownBy(() -> validator.validateActiveEmotionBand(null))
            .isInstanceOf(ResourceNotFoundException.class)
            .hasMessage("감정밴드를 찾을 수 없습니다.");
    }

    @Test
    void validateActiveEmotionBand_throws_IllegalArgumentException() {
        EmotionBand band = EmotionBand.builder()
            .endTime(LocalDateTime.now().minusMinutes(1))
            .build();
        assertThatThrownBy(() -> validator.validateActiveEmotionBand(band))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("이미 종료된 감정밴드입니다.");
    }

    @Test
    void validateMember_throws_ResourceNotFoundException() {
        assertThatThrownBy(() -> validator.validateMember(null))
            .isInstanceOf(ResourceNotFoundException.class)
            .hasMessage("사용자를 찾을 수 없습니다.");
    }

    @Test
    void validateMember() {
        Member member = Member.builder().id(1L).name("테스트").build();
        validator.validateMember(member);
    }
}
