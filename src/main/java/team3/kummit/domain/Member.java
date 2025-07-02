package team3.kummit.domain;

import java.time.LocalDate;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder(toBuilder = true)
@AllArgsConstructor
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    private String name;
    private LocalDate signUpDate;
    private String email;
    private String password;

    private Integer bandCreateCount;
    private Integer bandJoinCount;
    private Integer likeCount;
    private Integer songAddCount;

    public void incrementBandCreateCount() {
        this.bandCreateCount++;
    }
    public void incrementSongAddCount() {
        this.songAddCount++;
    }
    public void incrementBandJoinCount() {
        this.bandJoinCount++;
    }
    public void decrementBandJoinCount() {
        this.bandJoinCount--;
    }

}
