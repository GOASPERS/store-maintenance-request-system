package com.example.maintenancerequests.repository;

import com.example.maintenancerequests.entity.StatusHistory;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StatusHistoryRepository extends JpaRepository<StatusHistory, Long> {

    List<StatusHistory> findByMaintenanceRequestIdOrderByChangedAtAsc(Long maintenanceRequestId);
}
