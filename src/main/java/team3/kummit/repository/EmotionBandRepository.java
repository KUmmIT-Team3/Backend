package team3.kummit.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import team3.kummit.domain.EmotionBand;

public interface EmotionBandRepository extends JpaRepository<EmotionBand, Long> {

    // 현재 시간이 endTime을 지나지 않은 밴드들 중 좋아요 수 기준 상위 3개
    @Query("SELECT e FROM EmotionBand e WHERE e.endTime > :currentTime ORDER BY e.likeCount DESC")
    List<EmotionBand> findAllByLikeCountAndEndTimeAfter(LocalDateTime currentTime);

    // 현재 시간이 endTime을 지나지 않은 밴드들 중 endTime 기준 최신순
    @Query("SELECT e FROM EmotionBand e WHERE e.endTime > :currentTime ORDER BY e.endTime DESC")
    List<EmotionBand> findAllByEndTimeDescAndEndTimeAfter(LocalDateTime currentTime);

    @Query("SELECT eb.id FROM EmotionBand eb where eb.creator.id =:memberId")
    List<Long> findPkListByCreator(Long memberId);

    @Query("SELECT eb FROM EmotionBand eb left join fetch eb.songs ebs " +
            "WHERE eb.endTime > :currentTime and eb.id in :emotionBandIdList")
    List<EmotionBand> findAllByEmotionBandIdListWithSongs(List<Long> emotionBandIdList, LocalDateTime currentTime);


}
