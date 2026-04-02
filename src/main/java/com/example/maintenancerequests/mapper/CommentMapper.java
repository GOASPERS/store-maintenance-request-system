package com.example.maintenancerequests.mapper;

import com.example.maintenancerequests.dto.CommentResponseDto;
import com.example.maintenancerequests.dto.CreateCommentRequestDto;
import com.example.maintenancerequests.entity.Comment;
import com.example.maintenancerequests.entity.MaintenanceRequest;
import com.example.maintenancerequests.entity.User;
import org.springframework.stereotype.Component;

@Component
public class CommentMapper {

    public Comment toEntity(CreateCommentRequestDto dto, MaintenanceRequest maintenanceRequest, User author) {
        if (dto == null) {
            return null;
        }

        return Comment.builder()
                .maintenanceRequest(maintenanceRequest)
                .author(author)
                .text(dto.getText())
                .build();
    }

    public CommentResponseDto toResponseDto(Comment comment) {
        if (comment == null) {
            return null;
        }

        return CommentResponseDto.builder()
                .id(comment.getId())
                .maintenanceRequestId(comment.getMaintenanceRequest() != null ? comment.getMaintenanceRequest().getId() : null)
                .authorId(comment.getAuthor() != null ? comment.getAuthor().getId() : null)
                .authorFullName(comment.getAuthor() != null ? comment.getAuthor().getFullName() : null)
                .text(comment.getText())
                .createdAt(comment.getCreatedAt())
                .build();
    }
}
