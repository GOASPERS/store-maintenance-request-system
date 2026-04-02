package com.example.maintenancerequests.dto;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommentResponseDto {

    private Long id;
    private Long maintenanceRequestId;
    private Long authorId;
    private String authorFullName;
    private String text;
    private LocalDateTime createdAt;
}
