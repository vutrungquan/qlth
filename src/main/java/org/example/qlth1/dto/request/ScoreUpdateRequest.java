package org.example.qlth1.dto.request;

import java.util.List;

import lombok.Builder;
import lombok.Data;
@Builder
@Data
public class ScoreUpdateRequest {
    private Long studentId; // ID của học sinh
    private List<ScoreRequest> scores; // Danh sách điểm cho các môn học
}

