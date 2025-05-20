package com.konnect.attraction.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "attractions")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Attraction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer no;

    @Column(name = "content_id")
    private Integer contentId;

    @Column(name = "title", length = 500)
    private String title;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "content_type_id", referencedColumnName = "content_type_id")
    private ContentType contentType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "area_code", referencedColumnName = "sido_code")
    private Sido sido;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumns({
            @JoinColumn(name = "area_code",     referencedColumnName = "sido_code",  insertable = false, updatable = false),
            @JoinColumn(name = "si_gun_gu_code", referencedColumnName = "gugun_code", insertable = false, updatable = false)
    })
    private Gugun gugun;

    @Column(name = "first_image1", length = 100)
    private String firstImage1;

    @Column(name = "first_image2", length = 100)
    private String firstImage2;

    @Column(name = "map_level")
    private Integer mapLevel;

    @Column(name = "latitude", precision = 20, scale = 17)
    private BigDecimal latitude;

    @Column(name = "longitude", precision = 20, scale = 17)
    private BigDecimal longitude;

    @Column(name = "tel", length = 20)
    private String tel;

    @Column(name = "addr1", length = 100)
    private String addr1;

    @Column(name = "addr2", length = 100)
    private String addr2;

    @Column(name = "homepage", length = 1000)
    private String homepage;

    @Column(name = "overview", length = 10000)
    private String overview;

    // getters and setters
}
