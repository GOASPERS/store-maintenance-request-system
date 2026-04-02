package com.example.maintenancerequests.repository;

import com.example.maintenancerequests.entity.Store;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StoreRepository extends JpaRepository<Store, Long> {
}
