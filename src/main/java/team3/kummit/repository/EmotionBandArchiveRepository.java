package team3.kummit.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import team3.kummit.domain.EmotionBandArchive;

public interface EmotionBandArchiveRepository extends JpaRepository<EmotionBandArchive, Long> {
    Optional<EmotionBandArchive> findByCreatorIdAndEmotionBandId(Long memberId, Long emotionBandId);
    boolean existsByCreatorIdAndEmotionBandId(Long memberId, Long emotionBandId);

    // 사용자가 보관한 밴드 ID 목록 조회
    @Query("SELECT eba.emotionBand.id FROM EmotionBandArchive eba WHERE eba.creator.id = :memberId")
    List<Long> findEmotionBandIdListByMemberId(@Param("memberId") Long memberId);

}
