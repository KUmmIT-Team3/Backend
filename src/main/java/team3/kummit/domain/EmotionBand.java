package team3.kummit.domain;


import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder(toBuilder = true)
@AllArgsConstructor
public class EmotionBand {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "emotion_band_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="member_id")
    private Member creator;
    private String creatorName; // 불필요한 조인 막기
    private String emotion;
    private String description;
    private LocalDateTime endTime;

    private Integer likeCount;
    private Integer peopleCount;
    private Integer songCount;
    private Integer commentCount;

    @OneToMany(mappedBy = "emotionBand", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Song> songs = new CopyOnWriteArrayList<>();

    public void incrementCommentCount() { this.commentCount++; }

}
