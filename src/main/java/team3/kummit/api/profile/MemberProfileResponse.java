package team3.kummit.api.profile;

import java.time.LocalDate;

public record MemberProfileResponse(
        String name,
        LocalDate signUpDate,
        int bandCreateCount,
        int bandJoinCount,
        int likeCount,
        int songAddCount
) {}
