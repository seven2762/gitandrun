package com.sparta.gitandrun.region.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "p_region")
@Getter
@Setter
@NoArgsConstructor
public class Region {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "region_id", nullable = false)
    private Long regionId;

    @Column(name = "region_name", nullable = false, length = 100)
    private String regionName;

    @Column(name = "hirnk_region_id", nullable = true)
    private Long hirnkRegionId;

    public Region(String regionName, Long hirnkRegionId) {
        this.regionName = regionName;
        this.hirnkRegionId = hirnkRegionId;
    }
}
