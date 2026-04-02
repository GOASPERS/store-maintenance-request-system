package com.example.maintenancerequests.service;

import com.example.maintenancerequests.dto.CommentResponseDto;
import com.example.maintenancerequests.dto.CreateCommentRequestDto;
import java.util.List;

public interface CommentService {

    CommentResponseDto addComment(Long maintenanceRequestId, CreateCommentRequestDto dto);

    List<CommentResponseDto> getCommentsByMaintenanceRequestId(Long maintenanceRequestId);
}
