package io.github.jason13official.monolib.api.common;

import java.util.function.Consumer;
import java.util.function.Supplier;

public interface GetterSetter<T> {

  Supplier<T> getter();

  Consumer<T> setter();

  default T get() {

    return getter().get();
  }

  default void set(T value) {

    setter().accept(value);
  }
}
