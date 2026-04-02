package com.example.maintenancerequests.config;

import com.example.maintenancerequests.entity.Equipment;
import com.example.maintenancerequests.entity.MaintenanceRequest;
import com.example.maintenancerequests.entity.Store;
import com.example.maintenancerequests.entity.User;
import com.example.maintenancerequests.enums.RequestPriority;
import com.example.maintenancerequests.enums.RequestStatus;
import com.example.maintenancerequests.enums.Role;
import com.example.maintenancerequests.repository.EquipmentRepository;
import com.example.maintenancerequests.repository.MaintenanceRequestRepository;
import com.example.maintenancerequests.repository.StoreRepository;
import com.example.maintenancerequests.repository.UserRepository;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@RequiredArgsConstructor
public class DataSeeder {

    private final UserRepository userRepository;
    private final StoreRepository storeRepository;
    private final EquipmentRepository equipmentRepository;
    private final MaintenanceRequestRepository maintenanceRequestRepository;
    private final PasswordEncoder passwordEncoder;

    @Bean
    public CommandLineRunner seedDemoData() {
        return args -> {
            if (userRepository.count() > 0 || storeRepository.count() > 0) {
                return;
            }

            userRepository.save(User.builder()
                    .fullName("Alice Admin")
                    .email("admin@demo.local")
                    .password(passwordEncoder.encode("Admin123!"))
                    .role(Role.ADMIN)
                    .active(true)
                    .build());

            User dispatcher = userRepository.save(User.builder()
                    .fullName("Diana Dispatcher")
                    .email("dispatcher@demo.local")
                    .password(passwordEncoder.encode("Dispatcher123!"))
                    .role(Role.DISPATCHER)
                    .active(true)
                    .build());

            User engineer = userRepository.save(User.builder()
                    .fullName("Ethan Engineer")
                    .email("engineer@demo.local")
                    .password(passwordEncoder.encode("Engineer123!"))
                    .role(Role.ENGINEER)
                    .active(true)
                    .build());

            User manager = userRepository.save(User.builder()
                    .fullName("Mia Manager")
                    .email("manager@demo.local")
                    .password(passwordEncoder.encode("Manager123!"))
                    .role(Role.MANAGER)
                    .active(true)
                    .build());

            Store storeOne = storeRepository.save(Store.builder()
                    .name("Downtown Market")
                    .address("12 Central Avenue")
                    .city("Moscow")
                    .contactPerson("Olga Petrov")
                    .contactPhone("+7-900-100-10-10")
                    .build());

            Store storeTwo = storeRepository.save(Store.builder()
                    .name("North Mall Store")
                    .address("45 North Ring Road")
                    .city("Saint Petersburg")
                    .contactPerson("Ivan Sidorov")
                    .contactPhone("+7-900-200-20-20")
                    .build());

            Equipment coffeeMachine = equipmentRepository.save(Equipment.builder()
                    .name("Coffee Machine")
                    .type("Appliance")
                    .serialNumber("CM-2026-001")
                    .store(storeOne)
                    .build());

            Equipment coolingUnit = equipmentRepository.save(Equipment.builder()
                    .name("Cooling Unit")
                    .type("Refrigeration")
                    .serialNumber("CU-2026-002")
                    .store(storeTwo)
                    .build());

            if (maintenanceRequestRepository.count() == 0) {
                maintenanceRequestRepository.save(MaintenanceRequest.builder()
                        .title("Coffee machine leaks water")
                        .description("Water is leaking from the lower panel near the tray area.")
                        .priority(RequestPriority.HIGH)
                        .status(RequestStatus.ASSIGNED)
                        .store(storeOne)
                        .equipment(coffeeMachine)
                        .createdBy(manager)
                        .assignedEngineer(engineer)
                        .dueDate(LocalDate.now().plusDays(2))
                        .build());

                maintenanceRequestRepository.save(MaintenanceRequest.builder()
                        .title("Cooling unit temperature issue")
                        .description("Temperature is above normal and products are warming up.")
                        .priority(RequestPriority.CRITICAL)
                        .status(RequestStatus.NEW)
                        .store(storeTwo)
                        .equipment(coolingUnit)
                        .createdBy(dispatcher)
                        .assignedEngineer(null)
                        .dueDate(LocalDate.now().plusDays(1))
                        .build());
            }
        };
    }
}
