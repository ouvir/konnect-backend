package com.konnect.attraction.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "sidos")
@Getter
@Setter
public class Sido {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer no;

    @Column(name = "sido_code", nullable = false, unique = true)
    private Integer sidoCode;

    @Column(name = "sido_name", length = 20)
    private String sidoName;
}
