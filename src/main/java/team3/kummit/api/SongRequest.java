package team3.kummit.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SongRequest {
    private String artworkUrl100;
    private String trackName;
    private String artistName;
    private String previewUrl;
}
