package team3.kummit.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import team3.kummit.domain.EmotionBand;


@DataJpaTest
class EmotionBandRepositoryTest {

    @Autowired
    EmotionBandRepository repository;

    @Test
    void findAllByEmotionBandIdListWithSongs() {
        List<Long> emotionBandIdListByCreator = repository.findPkListByCreator(1L);
    }
    @Test
    void findAllByEmotionBandIdList() {
        List<EmotionBand> allByEmotionBandIdList = repository.findAllByEmotionBandIdListWithSongs(List.of(1L, 2L, 3L), LocalDateTime.now());
    }
}
