package com.github.alexliesenfeld.querydsl.jpa.hibernate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Preconditions;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.PathMetadata;
import com.querydsl.core.types.PathMetadataFactory;
import com.querydsl.core.types.Visitor;
import com.querydsl.core.types.dsl.*;

import java.lang.reflect.AnnotatedElement;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nullable;
import lombok.Getter;
import org.hibernate.annotations.Type;

/**
 * @author <a href=http://github.com/wenerme>wener</a>
 * @since 2018/6/13
 */
public class JsonPath implements Path<Object> {

  protected final PathMetadata metadata;
  protected final String property;
  protected final Path<?> parent;
  protected final ObjectMapper mapper;
  @Getter protected final boolean jsonb;

  protected JsonPath(Path<?> parent) {
    this(parent, ".");
  }

  protected JsonPath(Path<?> parent, String property) {
    this(parent, property, isJsonb(parent));
  }

  protected JsonPath(Path<?> parent, String property, boolean jsonb) {
    this.metadata = PathMetadataFactory.forProperty(parent, property);
    this.property = property;
    this.parent = parent;
    this.jsonb = jsonb;
    this.mapper = new ObjectMapper();
  }

  public static boolean isJsonb(Path<?> path) {
    if (path instanceof JsonPath) {
      return ((JsonPath) path).isJsonb();
    }
    Type type = path.getAnnotatedElement().getAnnotation(Type.class);
    if (type != null) {
      return type.type().contains("jsonb");
    }
    return false;
  }

  public static JsonPath of(Path<?> path) {
    return new JsonPath(path, ".", isJsonb(path));
  }

  public static JsonPath ofJson(Path<?> path) {
    return new JsonPath(path, ".", false);
  }

  public static JsonPath ofJsonb(Path<?> path) {
    return new JsonPath(path, ".", true);
  }

  @Override
  public PathMetadata getMetadata() {
    return metadata;
  }

  @Override
  public Path<?> getRoot() {
    return metadata.getRootPath();
  }

  @Override
  public AnnotatedElement getAnnotatedElement() {
    return parent.getAnnotatedElement();
  }

  @Nullable
  @Override
  public <R, C> R accept(Visitor<R, C> v, @Nullable C context) {
    // NOTE HQL does not support nested functions, so hql_json_path(hql_json_path(?,?),?) will fail
    throw new AssertionError("This should not happen");
  }

  protected List<Object> properties() {
    List<Object> p;

    if (parent instanceof JsonPath) {
      p = ((JsonPath) parent).properties();
    } else {
      p = new ArrayList<>();
      p.add(parent);
    }
    if (!property.equals(".")) {
      p.add(property);
    }
    return p;
  }

  @Override
  public Class<? extends Object> getType() {
    return Object.class;
  }

  public StringExpression asText() {
    List<Object> args = properties();
    StringBuilder sb = new StringBuilder();
    sb.append("hql_json_text").append('(');
    generateArgs(sb, args.size());
    sb.append(')');
    return Expressions.stringTemplate(sb.toString(), args);
  }

  public NumberTemplate<Integer> asInt() {
    List<Object> args = properties();
    StringBuilder sb = new StringBuilder();
    sb.append("hql_json_int(");
    generateArgs(sb, args.size());
    sb.append(")");
    return Expressions.numberTemplate(Integer.class, sb.toString(), args);
  }

  public NumberTemplate<Long> asLong() {
    List<Object> args = properties();
    StringBuilder sb = new StringBuilder();
    sb.append("hql_json_long(");
    generateArgs(sb, args.size());
    sb.append(")");
    return Expressions.numberTemplate(Long.class, sb.toString(), args);
  }

  public NumberTemplate<Short> asShort() {
    List<Object> args = properties();
    StringBuilder sb = new StringBuilder();
    sb.append("hql_json_short(");
    generateArgs(sb, args.size());
    sb.append(")");
    return Expressions.numberTemplate(Short.class, sb.toString(), args);
  }

  public NumberTemplate<Float> asFloat() {
    List<Object> args = properties();
    StringBuilder sb = new StringBuilder();
    sb.append("hql_json_float(");
    generateArgs(sb, args.size());
    sb.append(")");
    return Expressions.numberTemplate(Float.class, sb.toString(), args);
  }

  public NumberTemplate<Double> asDouble() {
    List<Object> args = properties();
    StringBuilder sb = new StringBuilder();
    sb.append("hql_json_double(");
    generateArgs(sb, args.size());
    sb.append(")");
    return Expressions.numberTemplate(Double.class, sb.toString(), args);
  }

  public BooleanTemplate asBool() {
    List<Object> args = properties();
    StringBuilder sb = new StringBuilder();
    sb.append("hql_json_bool(");
    generateArgs(sb, args.size());
    sb.append(")");
    return Expressions.booleanTemplate(sb.toString(), args);
  }


  public JsonPath get(String property) {
    return new JsonPath(this, property);
  }

  public JsonPath get(String... properties) {
    JsonPath p = this;
    for (String s : properties) {
      p = p.get(s);
    }
    return p;
  }

  protected CharSequence generateArgs(StringBuilder sb, int n) {
    return generateArgs(sb, 0, n - 1);
  }

  protected CharSequence generateArgs(StringBuilder sb, int start, int end) {
    for (int i = start; i <= end; i++) {
      sb.append('{').append(i).append('}');
      if (i != end) {
        sb.append(',');
      }
    }
    return sb;
  }

  /** Type of value object, array, string, number, boolean, and null. */
  public StringExpression type() {
    List<Object> args = properties();
    StringBuilder sb = new StringBuilder();
    functionNameOf(sb, "typeof").append('(');
    generateArgs(sb, args.size());
    sb.append(')');
    return Expressions.stringTemplate(sb.toString(), args);
  }

  /** json array length */
  public NumberTemplate<Integer> length() {
    List<Object> args = properties();
    StringBuilder sb = new StringBuilder();
    functionNameOf(sb, "array_length").append('(');
    generateArgs(sb, args.size());
    sb.append(')');
    return Expressions.numberTemplate(Integer.class, sb.toString(), args);
  }

  protected String functionNamePrefix() {
    return isJsonb() ? "hql_jsonb_" : "hql_json_";
  }

  protected StringBuilder functionNameOf(StringBuilder sb, String n) {
    return sb.append(functionNamePrefix()).append(n);
  }

  /** JSONB {@code @>} syntax */
  public BooleanExpression contains(Object value) {
    checkJsonb();
    List<Object> args = properties();
    args.add(toJsonString(value));
    StringBuilder sb = new StringBuilder();
    functionNameOf(sb, "contains").append('(');
    generateArgs(sb, args.size());
    sb.append(') = true');
    return Expressions.booleanTemplate(sb.toString(), args);
  }

  private String toJsonString(Object value){
    try {
      return this.mapper.writeValueAsString(value);
    } catch (JsonProcessingException e) {
      throw new RuntimeException(e);
    }
  }
  protected void checkJsonb() {
    Preconditions.checkArgument(isJsonb(), "This function required jsonb type");
  }
}
