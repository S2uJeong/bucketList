package com.team9.bucket_list.repository;

import com.team9.bucket_list.domain.entity.Application;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ApplicationRepository extends JpaRepository<Application, Long> {
}
