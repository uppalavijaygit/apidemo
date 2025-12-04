package org.example.apidemo.repository;

import org.example.apidemo.entity.Reminder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ReminderRepository extends JpaRepository<Reminder, String>, JpaSpecificationExecutor<Reminder> {
    Optional<Reminder> findByRef(String ref);
    boolean existsByRef(String ref);
}

