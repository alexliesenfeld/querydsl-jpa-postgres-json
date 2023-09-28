package com.github.alexliesenfeld.querydsl.jpa.hibernate.repository;

import com.github.alexliesenfeld.querydsl.jpa.hibernate.entity.TestEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface TestRepository extends JpaRepository<TestEntity, UUID>, QuerydslPredicateExecutor<TestEntity> {
}
