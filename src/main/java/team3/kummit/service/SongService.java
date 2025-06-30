package team3.kummit.service;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import team3.kummit.api.SongRequest;
import team3.kummit.api.SongResponse;
import team3.kummit.domain.EmotionBand;
import team3.kummit.domain.Member;
import team3.kummit.domain.Song;
import team3.kummit.exception.ResourceNotFoundException;
import team3.kummit.repository.EmotionBandRepository;
import team3.kummit.repository.MemberRepository;
import team3.kummit.repository.SongRepository;

@Service
@RequiredArgsConstructor
public class SongService {
    private final SongRepository songRepository;
    private final EmotionBandRepository emotionBandRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public SongResponse addSong(Long emotionBandId, Long memberId, SongRequest request) {
        // 감정밴드가 존재하고 아직 종료되지 않았는지 확인
        EmotionBand emotionBand = emotionBandRepository.findById(emotionBandId)
                .orElseThrow(() -> new ResourceNotFoundException("감정밴드를 찾을 수 없습니다."));

        if (emotionBand.getEndTime().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("종료된 감정밴드에는 음악을 추가할 수 없습니다.");
        }

        // 사용자 확인
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new ResourceNotFoundException("사용자를 찾을 수 없습니다."));

        // 음악 생성
        Song song = Song.builder()
                .emotionBand(emotionBand)
                .creator(member)
                .creatorName(member.getName())
                .title(request.getTrackName())
                .artist(request.getArtistName())
                .albumImageLink(request.getArtworkUrl100())
                .previewLink(request.getPreviewUrl())
                .build();

        Song savedSong = songRepository.save(song);

        // 감정밴드의 음악 수 증가
        Integer currentSongCount = emotionBand.getSongCount() != null ? emotionBand.getSongCount() : 0;
        EmotionBand updatedEmotionBand = emotionBand.toBuilder()
                .songCount(currentSongCount + 1)
                .build();
        emotionBandRepository.save(updatedEmotionBand);

        // 사용자의 음악 추가 수 증가
        Integer currentSongAddCount = member.getSongAddCount() != null ? member.getSongAddCount() : 0;
        Member updatedMember = member.toBuilder()
                .songAddCount(currentSongAddCount + 1)
                .build();
        memberRepository.save(updatedMember);

        return SongResponse.from(savedSong);
    }
}
