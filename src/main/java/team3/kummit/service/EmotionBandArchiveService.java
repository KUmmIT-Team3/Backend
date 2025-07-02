package team3.kummit.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import team3.kummit.api.EmotionBandArchiveResponse;
import team3.kummit.domain.EmotionBand;
import team3.kummit.domain.EmotionBandArchive;
import team3.kummit.domain.Member;
import team3.kummit.repository.EmotionBandArchiveRepository;
import team3.kummit.repository.EmotionBandRepository;
import team3.kummit.repository.MemberRepository;

@Service
@RequiredArgsConstructor
public class EmotionBandArchiveService {
    private final EmotionBandArchiveRepository archiveRepository;
    private final EmotionBandRepository emotionBandRepository;
    private final MemberRepository memberRepository;
    private final EntityValidator entityValidator;

    @Transactional
    public EmotionBandArchiveResponse toggleArchive(Long emotionBandId, Long memberId) {
        EmotionBand emotionBand = emotionBandRepository.findById(emotionBandId).orElse(null);
        Member member = memberRepository.findById(memberId).orElse(null);

        entityValidator.validateMember(member);
        entityValidator.validateActiveEmotionBand(emotionBand);

        boolean isArchived = archiveRepository.existsByCreatorIdAndEmotionBandId(memberId, emotionBandId);

        if (isArchived) {
            archiveRepository.findByCreatorIdAndEmotionBandId(memberId, emotionBandId)
                    .ifPresent(archiveRepository::delete);
            updateCounts(emotionBand, member, false);
            return EmotionBandArchiveResponse.of(false, "보관이 해제되었습니다.");
        } else {
            EmotionBandArchive archive = EmotionBandArchive.builder()
                    .creator(member)
                    .emotionBand(emotionBand)
                    .build();
            archiveRepository.save(archive);
            updateCounts(emotionBand, member, true);
            return EmotionBandArchiveResponse.of(true, "보관되었습니다.");
        }
    }

    private void updateCounts(EmotionBand emotionBand, Member member, boolean increment) {
        if (increment) {
            emotionBand.incrementPeopleCount();
            member.incrementBandJoinCount();
        } else {
            emotionBand.decrementPeopleCount();
            member.decrementBandJoinCount();
        }
        emotionBandRepository.save(emotionBand);
        memberRepository.save(member);
    }

    @Transactional(readOnly = true)
    public boolean isArchived(Long emotionBandId, Long memberId) {
        return archiveRepository.existsByCreatorIdAndEmotionBandId(memberId, emotionBandId);
    }

    public List<Long> findArchivedEmotionBandIdsByMember(Long memberId){
        return archiveRepository.findEmotionBandIdListByMemberId(memberId);
    }
}
