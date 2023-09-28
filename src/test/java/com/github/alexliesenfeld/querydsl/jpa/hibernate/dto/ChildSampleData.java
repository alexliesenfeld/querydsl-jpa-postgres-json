package com.github.alexliesenfeld.querydsl.jpa.hibernate.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChildSampleData {
    private int intField;
    private Integer integerField;

    private long longField;
    private Long longClsField;

    private String fieldString;
}
