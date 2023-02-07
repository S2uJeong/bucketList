package com.team9.bucket_list.repository.comment;

import com.team9.bucket_list.domain.entity.Profile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProfileRepository extends JpaRepository<Profile, Long> {
}
