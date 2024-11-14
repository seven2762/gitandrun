package com.sparta.gitandrun.region.service;

import com.sparta.gitandrun.region.entity.Region;
import com.sparta.gitandrun.region.repository.RegionRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.annotation.PostConstruct;
import java.util.Optional;

@Service
public class RegionService {

    private final RegionRepository regionRepository;

    public RegionService(RegionRepository regionRepository) {
        this.regionRepository = regionRepository;
    }

    // 애플리케이션 시작 시 초기 Region 데이터를 설정하는 메서드
    @PostConstruct
    @Transactional
    public void initializeRegions() {
        Optional<Region> existingRegion = regionRepository.findById(1L);
        if (existingRegion.isEmpty()) {
            Region seoulRegion = new Region();
            seoulRegion.setRegionName("서울시");
            seoulRegion.setHirnkRegionId(null);  // 부모 지역이 없을 경우 null로 설정
            regionRepository.save(seoulRegion);
        }
    }
}
