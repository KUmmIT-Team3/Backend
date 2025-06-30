package team3.kummit.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import team3.kummit.domain.Comment;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommentResponse {
    private Long id;
    private String creatorName;
    private String comment;

    public static CommentResponse from(Comment comment) {
        return CommentResponse.builder()
                .id(comment.getId())
                .creatorName(comment.getCreatorName())
                .comment(comment.getComment())
                .build();
    }
}
