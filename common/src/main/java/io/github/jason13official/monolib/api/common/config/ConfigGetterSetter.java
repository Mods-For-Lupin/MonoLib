package io.github.jason13official.monolib.api.common.config;

import io.github.jason13official.monolib.api.common.GetterSetter;
import java.util.function.Consumer;
import java.util.function.Supplier;

public record ConfigGetterSetter<T>(String key, Supplier<T> getter, Consumer<T> setter) implements GetterSetter<T> {

  public record Commented<T>(String key, Supplier<T> getter, Consumer<T> setter, String comment) implements GetterSetter<T> {
  }
}
