package team3.kummit.service.music;


import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
class MusicApiResponseParser {

    private final ObjectMapper objectMapper = new ObjectMapper(); // 테스트를 위해 직접 생성

    public List<MusicResponse> parseMusicResponse(String response) {

        JsonNode root;
        try{
            root = objectMapper.readTree(response);
        }catch (JsonProcessingException e){
            log.error("parseMusicResponse error: {}", e.getMessage());
            throw new HttpMessageNotReadableException(e.getMessage(),e, null);
        }
        return StreamSupport.stream(root.withArray("results").spliterator(), false)
                .map(node -> objectMapper.convertValue(node, MusicResponse.class))
                .collect(Collectors.toList());
    }


}
