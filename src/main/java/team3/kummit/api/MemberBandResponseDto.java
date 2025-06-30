package team3.kummit.dto;

import java.time.LocalDateTime;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MemberBandResponseDto {

    private String emotion;
    private String writerName;
    private String description;

    private List<Music> musicList;

    private Integer likeCount;
    private Integer peopleCount;
    private Integer songCount;
    private Integer commentCount;

    private LocalDateTime endTime;

    public record Music(
            String albumImageLink,
            String title,
            String artist,
            String previewLink
    ) {}
}
