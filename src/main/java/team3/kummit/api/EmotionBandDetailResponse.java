package team3.kummit.dto;

import java.time.LocalDateTime;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import team3.kummit.domain.EmotionBand;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmotionBandDetailResponse {
    private Long id;
    private String creatorName;
    private String emotion;
    private String description;
    private LocalDateTime endTime;
    private Integer likeCount;
    private Integer peopleCount;
    private Integer songCount;
    private Integer commentCount;
    private List<SongResponse> songs;
    private List<CommentResponse> comments;
    private boolean isArchived;
    private boolean liked;

    public static EmotionBandDetailResponse from(EmotionBand emotionBand, List<SongResponse> songs, List<CommentResponse> comments, boolean isArchived, boolean liked) {
        return EmotionBandDetailResponse.builder()
                .id(emotionBand.getId())
                .creatorName(emotionBand.getCreatorName())
                .emotion(emotionBand.getEmotion())
                .description(emotionBand.getDescription())
                .endTime(emotionBand.getEndTime())
                .likeCount(emotionBand.getLikeCount())
                .peopleCount(emotionBand.getPeopleCount())
                .songCount(emotionBand.getSongCount())
                .commentCount(emotionBand.getCommentCount())
                .songs(songs)
                .comments(comments)
                .isArchived(isArchived)
                .liked(liked)
                .build();
    }
}
