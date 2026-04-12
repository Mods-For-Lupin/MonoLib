package com.cursee.monolib.core.util;

@Deprecated
@FunctionalInterface
public interface TriFunction<T1, T2, T3, R> {

  @Deprecated
  R apply(T1 t1, T2 t2, T3 t3);
}
