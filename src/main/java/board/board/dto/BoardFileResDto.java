package board.board.dto;

import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BoardFileResDto {

    private int idx;

    private int boardIdx;

    private String originalFileName;

    private String storedFilePath;

    private long fileSize;

    private String creatorId;

    private LocalDateTime createdDatetime = LocalDateTime.now();

    private String updaterId;

    private LocalDateTime updatedDatetime;

}
