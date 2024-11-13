package com.sparta.gitandrun.region.repository;

import com.sparta.gitandrun.region.entity.Region;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RegionRepository extends JpaRepository<Region, Long> {

    // regionName으로 Region을 조회할 수 있는 메서드 예시
    Optional<Region> findByRegionName(String regionName);
}
