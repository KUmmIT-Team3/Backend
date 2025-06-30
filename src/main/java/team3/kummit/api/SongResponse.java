package team3.kummit.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import team3.kummit.domain.Song;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SongResponse {
    private Long id;
    private String title;
    private String artist;
    private String creatorName;
    private LocalDateTime createdAt;
    private String albumImageLink;
    private String previewLink;
    private Long emotionBandId;
    private String emotion;

    public static SongResponse from(Song song) {
        return SongResponse.builder()
                .id(song.getId())
                .title(song.getTitle())
                .artist(song.getArtist())
                .creatorName(song.getCreatorName())
                .createdAt(song.getCreatedAt())
                .albumImageLink(song.getAlbumImageLink())
                .previewLink(song.getPreviewLink())
                .emotionBandId(song.getEmotionBand().getId())
                .emotion(song.getEmotionBand().getEmotion())
                .build();
    }
}
