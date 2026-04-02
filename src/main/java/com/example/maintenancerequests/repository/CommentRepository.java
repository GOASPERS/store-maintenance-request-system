package com.example.maintenancerequests.repository;

import com.example.maintenancerequests.entity.Comment;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findByMaintenanceRequestIdOrderByCreatedAtAsc(Long maintenanceRequestId);
}
