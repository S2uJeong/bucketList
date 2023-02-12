package com.team9.bucket_list.repository;

import com.team9.bucket_list.domain.entity.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProfileRepository extends JpaRepository<Profile, Long>{

    Optional<Profile> findByMember_Id(Long memberId);

}



