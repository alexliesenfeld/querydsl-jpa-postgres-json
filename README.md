# PostJava

Java work with PostgreSQL

* Single class to rule both json and jsonb
* Hibernate json/jsonb dialect registry
* json/jsonb operator for QueryDSL
* json/jsonb function integration for QueryDSL

## Get started

### Install dialect

```xml
<dependency>
    <groupId>com.github.wenerme.postjava</groupId>
    <artifactId>postjava</artifactId>
    <version>1.0.1</version>
</dependency>
```

```yaml
# Use the predefined dialect
spring.jpa.properties.hibernate.dialect: com.github.wenerme.postjava.hibernate.dialect.PostgreSQLJsonDialect
```

Or use your customized dialect

```java
public class PostgreSQLCustomDialect extends PostgreSQLDialect {

  public PostgreSQLCustomDialect() {
    super();
    // Add json/jsonb support
    PostgreSQLJsonDialect.register(this);
  }
}
```

### Define entity
* [vladmihalcea/hibernate-types](https://github.com/vladmihalcea/hibernate-types) provide json type support for hibernate

```xml
<dependency>
    <groupId>com.vladmihalcea</groupId>
    <artifactId>hibernate-types-52</artifactId>
    <version>2.2.1</version>
</dependency>
```

```java
@TypeDefs({
  @TypeDef(name = "json", typeClass = JsonStringType.class),
  @TypeDef(name = "jsonb", typeClass = JsonBinaryType.class),
  @TypeDef(name = "json-node", typeClass = JsonNodeStringType.class),
  @TypeDef(name = "jsonb-node", typeClass = JsonNodeBinaryType.class),
})
@Entity
@Table(name = "users")
@Setter
@Getter
class UserEntity {
  Integer id;
  @Type(type="json-node")
  JsonNode attributes;
  @Type(type="jsonb")
  Map<String,String> labels;
}
```

### Work with QueryDSL

```java
class PlayJson{
  public static void main(String[] args) {
    // Will auto detect json/jsonb
    JsonPath attrs = JsonPath.of(QUserEntity.userEntity.attributes);
    attrs.get("name").asText().like("wener%"); // String expression
    attrs.get("age").asInt().gt(18); // Integer expression
    attrs.get("score").asFloat().gt(1.5); // Float expression
    attrs.get("resources").contain(1).isTrue(); // Is array contain element 
    attrs.get("resources").contain("A").isTrue().not(); // Is array not contain element 
    attrs.get("resources").contain("A").isFalse(); // Is array not contain element
    attrs.get("resources").length().gt(0); // Is array length > 0
  }
}
```
