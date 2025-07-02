package team3.kummit.controller.parser;

import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import team3.kummit.api.profile.MemberBandResponseDto;
import team3.kummit.api.profile.MemberProfileResponse;
import team3.kummit.domain.EmotionBand;
import team3.kummit.domain.Member;

@Component
public class MemberApiParser {

    public MemberProfileResponse mapToMemberProfileResponse(Member member){
        return new MemberProfileResponse(
                member.getName(),
                member.getSignUpDate(),
                member.getBandCreateCount(),
                member.getBandJoinCount(),
                member.getLikeCount(),
                member.getSongAddCount());
    }

    public MemberBandResponseDto mapToMemberBandResponse(EmotionBand band){
        return new MemberBandResponseDto(
                band.getEmotion(),
                band.getCreatorName(),
                band.getDescription(),
                band.getSongs().stream()
                        .map(song -> new MemberBandResponseDto.Music(
                                song.getAlbumImageLink(),
                                song.getTitle(),
                                song.getArtist(),
                                song.getPreviewLink()
                        ))
                        .collect(Collectors.toList()),
                band.getLikeCount(),
                band.getPeopleCount(),
                band.getSongCount(),
                band.getCommentCount(),
                band.getEndTime()
        );
    }
}
