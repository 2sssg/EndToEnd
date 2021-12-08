package com.javaproject.endtoend.repository;

import com.javaproject.endtoend.Constant.PhoneticVar;
import com.javaproject.endtoend.model.PhoneticRule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PhoneticRepository extends JpaRepository<PhoneticRule,Long> {
    Optional<PhoneticRule> findByFirstOP(String firstOP);
}
