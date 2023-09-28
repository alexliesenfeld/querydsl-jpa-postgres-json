package com.github.alexliesenfeld.querydsl.jpa.hibernate;

import com.github.alexliesenfeld.querydsl.jpa.hibernate.dto.ChildSampleData;
import com.github.alexliesenfeld.querydsl.jpa.hibernate.dto.SampleData;
import com.github.alexliesenfeld.querydsl.jpa.hibernate.entity.QTestEntity;
import com.github.alexliesenfeld.querydsl.jpa.hibernate.entity.TestEntity;
import com.github.alexliesenfeld.querydsl.jpa.hibernate.initializer.Initializer;
import com.github.alexliesenfeld.querydsl.jpa.hibernate.repository.TestRepository;
import com.querydsl.core.types.dsl.BooleanExpression;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.StreamSupport;

//@ExtendWith({SpringExtension.class})
@ExtendWith(SpringExtension.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestPropertySource("classpath:application.properties")
@ContextConfiguration(classes = {IntegrationTest.TestConfig.class}, initializers ={Initializer.class})
public class IntegrationTest {

    @SpringBootApplication
    @TestPropertySource("classpath:application.properties")
    @ContextConfiguration(initializers ={ Initializer.class})
    static class TestConfig {}

    @Autowired
    TestRepository testRepository;

    @Test
    public void testContet() {
        TestEntity testEntity = new TestEntity();
        testEntity.setFileId(UUID.randomUUID());
        testEntity.setExtension("txt");
        testEntity.setFileName("filename");
        testEntity.setLength(10l);
        testEntity.setEnumList(List.of(EnumTest.TEST1));
        testEntity.setChildParam(SampleData.builder()
                        .idField(0)
                        .stringField("0")
                        .child(ChildSampleData.builder()
                                .intField(1)
                                .integerField(2)
                                .longField(3L)
                                .longClsField(4L)
                                .fieldString("5")
                                .build())
                .build());
        Map<String, String> tags = new LinkedHashMap<>();
        tags.put("key1", "val1");
        tags.put("key2", "val2");
        tags.put("key3", "val3");
        testEntity.setTags(tags);

        testRepository.saveAndFlush(testEntity);

        testEntity = new TestEntity();
        testEntity.setFileId(UUID.randomUUID());
        testEntity.setExtension("txt");
        testEntity.setFileName("filename");
        testEntity.setLength(10L);
        testEntity.setEnumList(List.of(EnumTest.TEST1, EnumTest.TEST3));
        testEntity.setChildParam(SampleData.builder()
                .idField(6)
                .stringField("6")
                .child(ChildSampleData.builder()
                        .intField(7)
                        .integerField(8)
                        .longField(9L)
                        .longClsField(10L)
                        .fieldString("11")
                        .build())
                .build());

        tags = new LinkedHashMap<>();
        tags.put("key1", "val21");
        tags.put("key3", "val23");
        testEntity.setTags(tags);

        testRepository.saveAndFlush(testEntity);

        QTestEntity q = QTestEntity.testEntity;
        BooleanExpression expression = q.fileName.eq("filename");
        JsonPath jsonPath = JsonPath.of(q.tags);
        BooleanExpression exp = expression.and(jsonPath.get("key1").asText().eq("val21"));

        JsonPath childCheck = JsonPath.of(q.childParam);
        exp = exp.and(childCheck.get("child").get("longField").asLong().eq(9L));

        JsonPath listPath = JsonPath.of(q.enumList);
        exp = exp.and(listPath.contains(EnumTest.TEST3));
        Iterable<TestEntity> entities = testRepository.findAll(exp);

        Assert.assertEquals(1, StreamSupport.stream(entities.spliterator(), false).count());

    }

    @Test
    public void testContet2() {
        TestEntity testEntity = new TestEntity();
        testEntity.setFileId(UUID.randomUUID());
        testEntity.setExtension("txt");
        testEntity.setFileName("filename");
        testEntity.setLength(10L);
        testEntity.setEnumList(List.of(EnumTest.TEST1, EnumTest.TEST3));
        testEntity.setChildParam(SampleData.builder()
                .idField(6)
                .stringField("6")
                .child(ChildSampleData.builder()
                        .intField(7)
                        .integerField(8)
                        .longField(9L)
                        .longClsField(10L)
                        .fieldString("11")
                        .build())
                .build());

        Map<String, String> tags = new LinkedHashMap<>();
        tags.put("key1", "val21");
        tags.put("key3", "val23");
        testEntity.setTags(tags);

        testRepository.saveAndFlush(testEntity);

        QTestEntity q = QTestEntity.testEntity;
        JsonPath listPath = JsonPath.of(q.enumList);
        BooleanExpression exp = listPath.contains(EnumTest.TEST3);
        Iterable<TestEntity> entities = testRepository.findAll(exp);

        Assert.assertEquals(1, StreamSupport.stream(entities.spliterator(), false).count());

    }
    // tests
}