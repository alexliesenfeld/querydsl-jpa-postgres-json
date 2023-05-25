package com.github.alexliesenfeld.querydsl.jpa.hibernate.entity;

import com.github.alexliesenfeld.querydsl.jpa.hibernate.EnumTest;
import com.github.alexliesenfeld.querydsl.jpa.hibernate.dto.SampleData;
import io.hypersistence.utils.hibernate.type.json.JsonBinaryType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Type;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Entity
@Table(name = "document")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TestEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID fileId;
    private String fileName;
    private String extension;
    private long length;

    @Column(columnDefinition = "jsonb")
    @Type(JsonBinaryType.class)
//    @JdbcTypeCode(SqlTypes.JSON)
    private Map<String, String> tags;

    @Column(columnDefinition = "jsonb")
    @Type(JsonBinaryType.class)
//    @JdbcTypeCode(SqlTypes.JSON)
    private SampleData childParam;

    @Column(columnDefinition = "jsonb")
    @Type(JsonBinaryType.class)
    private List<EnumTest> enumList;
}