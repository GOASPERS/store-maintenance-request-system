package com.example.maintenancerequests.controller;

import com.example.maintenancerequests.dto.CommentResponseDto;
import com.example.maintenancerequests.dto.CreateCommentRequestDto;
import com.example.maintenancerequests.service.CommentService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/requests/{id}/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping
    public ResponseEntity<CommentResponseDto> addComment(
            @PathVariable Long id,
            @Valid @RequestBody CreateCommentRequestDto dto
    ) {
        CommentResponseDto createdComment = commentService.addComment(id, dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdComment);
    }

    @GetMapping
    public ResponseEntity<List<CommentResponseDto>> getCommentsByMaintenanceRequestId(@PathVariable Long id) {
        return ResponseEntity.ok(commentService.getCommentsByMaintenanceRequestId(id));
    }
}
