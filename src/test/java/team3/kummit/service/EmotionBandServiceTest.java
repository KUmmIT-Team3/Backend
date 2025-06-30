package team3.kummit.service;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import team3.kummit.api.EmotionBandCreateRequest;
import team3.kummit.domain.Member;
import team3.kummit.repository.MemberRepository;

@SpringBootTest
@Transactional
class EmotionBandServiceTest {

    @Autowired
    EmotionBandService emotionBandService;

    @Autowired
    MemberRepository memberRepository;

    @Test
    void testCreateEmotionBand_Success() {
        // Arrange
        Member creator = Member.builder()
                .name("테스트유저")
                .email("test@test.com")
                .password("password123")
                .signUpDate(LocalDate.now())
                .bandCreateCount(0)
                .bandJoinCount(0)
                .likeCount(0)
                .songAddCount(0)
                .build();

        EmotionBandCreateRequest.SongDto songDto = new EmotionBandCreateRequest.SongDto();
        songDto.setTitle("Test Song");
        songDto.setArtist("Test Artist");
        songDto.setAlbumImageLink("https://test.com/image.jpg");
        songDto.setPreviewLink("https://test.com/preview.mp3");

        EmotionBandCreateRequest request = new EmotionBandCreateRequest();
        request.setEmotion("Happiness");
        request.setDescription("Test Description");
        request.setSong(songDto);

        // Act
        memberRepository.save(creator);
        Long resultId = emotionBandService.createEmotionBand(creator.getId(), request);

        // Assert
        assertNotNull(resultId);
    }

}
