package com.example.maintenancerequests.service;

import com.example.maintenancerequests.dto.CommentResponseDto;
import com.example.maintenancerequests.dto.CreateCommentRequestDto;
import com.example.maintenancerequests.entity.Comment;
import com.example.maintenancerequests.entity.MaintenanceRequest;
import com.example.maintenancerequests.entity.User;
import com.example.maintenancerequests.exception.AuthenticationRequiredException;
import com.example.maintenancerequests.exception.ResourceNotFoundException;
import com.example.maintenancerequests.mapper.CommentMapper;
import com.example.maintenancerequests.repository.CommentRepository;
import com.example.maintenancerequests.repository.MaintenanceRequestRepository;
import com.example.maintenancerequests.repository.UserRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final MaintenanceRequestRepository maintenanceRequestRepository;
    private final UserRepository userRepository;
    private final CommentMapper commentMapper;

    @Override
    @Transactional
    public CommentResponseDto addComment(Long maintenanceRequestId, CreateCommentRequestDto dto) {
        MaintenanceRequest maintenanceRequest = findMaintenanceRequestByIdOrThrow(maintenanceRequestId);
        User author = getCurrentAuthenticatedUser();

        Comment comment = commentMapper.toEntity(dto, maintenanceRequest, author);
        Comment savedComment = commentRepository.save(comment);

        return commentMapper.toResponseDto(savedComment);
    }

    @Override
    public List<CommentResponseDto> getCommentsByMaintenanceRequestId(Long maintenanceRequestId) {
        findMaintenanceRequestByIdOrThrow(maintenanceRequestId);

        return commentRepository.findByMaintenanceRequestIdOrderByCreatedAtAsc(maintenanceRequestId)
                .stream()
                .map(commentMapper::toResponseDto)
                .toList();
    }

    private MaintenanceRequest findMaintenanceRequestByIdOrThrow(Long id) {
        return maintenanceRequestRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Maintenance request not found with id: " + id));
    }

    private User getCurrentAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || authentication.getName() == null) {
            throw new AuthenticationRequiredException("Authenticated user not found in security context");
        }

        return userRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new AuthenticationRequiredException(
                        "Authenticated user not found with email: " + authentication.getName()
                ));
    }
}
