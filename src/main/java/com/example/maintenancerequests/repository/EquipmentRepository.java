package com.example.maintenancerequests.repository;

import com.example.maintenancerequests.entity.Equipment;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EquipmentRepository extends JpaRepository<Equipment, Long> {

    List<Equipment> findByStoreId(Long storeId);
}
