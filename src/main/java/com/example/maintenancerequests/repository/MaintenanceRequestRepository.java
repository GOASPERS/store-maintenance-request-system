package com.example.maintenancerequests.repository;

import com.example.maintenancerequests.entity.MaintenanceRequest;
import com.example.maintenancerequests.enums.RequestStatus;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MaintenanceRequestRepository extends JpaRepository<MaintenanceRequest, Long> {

    List<MaintenanceRequest> findByStoreId(Long storeId);

    List<MaintenanceRequest> findByStatus(RequestStatus status);

    List<MaintenanceRequest> findByAssignedEngineerId(Long assignedEngineerId);
}
