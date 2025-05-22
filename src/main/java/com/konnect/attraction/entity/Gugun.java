package com.konnect.attraction.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "guguns")
@Getter
@Setter
public class Gugun {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer no;

    @Column(name = "gugun_code", nullable = false)
    private Integer gugunCode;

    @Column(name = "gugun_name", length = 20)
    private String gugunName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sido_code", referencedColumnName = "sido_code")
    private Sido sido;
}
