package com.konnect.attraction.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Table(name = "contenttypes")
@Getter
@Setter
public class ContentType {
    @Id
    @Column(name = "content_type_id")
    private Integer contentTypeId;

    @Column(name = "content_type_name", length = 45)
    private String contentTypeName;
}

