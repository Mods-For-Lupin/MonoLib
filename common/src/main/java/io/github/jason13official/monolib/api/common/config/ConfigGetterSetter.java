package io.github.jason13official.monolib.api.common.config;

import java.util.function.Consumer;
import java.util.function.Supplier;

public record ConfigGetterSetter<T>(String key, Supplier<T> getter, Consumer<T> setter) {

  public record Commented<T>(String key, Supplier<T> getter, Consumer<T> setter, String comment) {

  }
}
