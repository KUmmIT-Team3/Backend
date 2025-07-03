package team3.kummit.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import team3.kummit.domain.Member;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByName(String name);

    @Modifying
    @Transactional
    @Query("update Member m set m.likeCount = m.likeCount + 1 where m.id = :memberId")
    int incrementLikeCount(@Param("memberId") Long memberId);

    @Modifying
    @Transactional
    @Query("update Member m set m.likeCount = m.likeCount - 1 where m.id = :memberId")
    int decrementLikeCount(@Param("memberId") Long memberId);
}
