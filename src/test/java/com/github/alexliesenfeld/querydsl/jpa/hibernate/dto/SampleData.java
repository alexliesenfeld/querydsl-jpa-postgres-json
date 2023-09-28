package com.github.alexliesenfeld.querydsl.jpa.hibernate.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SampleData {
    private int idField;
    private String stringField;
    private ChildSampleData child;
}
