package team3.kummit.repository;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import team3.kummit.domain.EmotionBand;


@SpringBootTest
class EmotionBandRepositoryTest {

    @Autowired
    EmotionBandRepository repository;

    @Test
    void findAllByEmotionBandIdList() {
        List<Long> emotionBandIdListByCreator = repository.findEmotionBandIdListByCreator(1L);
    }
    @Test
    void indAllByEmotionBandIdList() {
        List<EmotionBand> allByEmotionBandIdList = repository.findAllByEmotionBandIdList(List.of(1L, 2L, 3L), LocalDateTime.now());
    }
}
