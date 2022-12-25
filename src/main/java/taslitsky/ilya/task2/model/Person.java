package taslitsky.ilya.task2.model;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.Data;

import java.time.Instant;

import taslitsky.ilya.task2.annotation.Property;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class Person {
  @Property(name = "stringProperty")
  private String name;
  @Property(name = "numberProperty")
  private int age;
  @Property(format = "dd.MM.yyyy HH:mm")
  private Instant timeProperty;
}
