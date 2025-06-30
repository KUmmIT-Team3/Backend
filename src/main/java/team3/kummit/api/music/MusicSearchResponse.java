package team3.kummit.api.music;

import java.util.List;

import team3.kummit.service.music.MusicResponse;

public record MusicSearchResponse (Integer resultCount, List<MusicResponse> musics ){

}
