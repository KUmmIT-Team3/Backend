package team3.kummit.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import team3.kummit.domain.EmotionBandLike;

public interface EmotionBandLikeRepository extends JpaRepository<EmotionBandLike, Long> {
    Optional<EmotionBandLike> findByCreatorIdAndEmotionBandId(Long memberId, Long emotionBandId);

    // 사용자가 공감한 밴드 ID 목록 조회
    @Query("SELECT ebl.emotionBand.id FROM EmotionBandLike ebl WHERE ebl.creator.id = :memberId")
    List<Long> findEmotionBandIdListByMemberId(@Param("memberId") Long memberId);

}
