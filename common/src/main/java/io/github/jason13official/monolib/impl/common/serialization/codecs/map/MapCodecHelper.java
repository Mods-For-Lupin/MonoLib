package io.github.jason13official.monolib.impl.common.serialization.codecs.map;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.lang.reflect.Array;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.function.IntFunction;

/**
 * Adapted from Darkhax's <a href="https://github.com/Darkhax-Minecraft/Bookshelf">Bookshelf</a>
 */
public class MapCodecHelper<T> {

  private final Codec<T> elementCodec;
  private final IntFunction<T[]> arrayBuilder;

  @SafeVarargs
  public MapCodecHelper(Codec<T> elementCodec, T... vargs) {
    if (vargs.length > 0) {
      throw new IllegalArgumentException("The arrayBuilder must be empty!");
    }
    this.elementCodec = elementCodec;
    this.arrayBuilder = size -> (T[]) Array.newInstance(vargs.getClass().getComponentType(), size);
  }

  public Codec<T> get() {
    return this.elementCodec;
  }

  public <O> RecordCodecBuilder<O, T> get(String fieldName, Function<O, T> getter) {
    return this.get().fieldOf(fieldName).forGetter(getter);
  }

  public <O> RecordCodecBuilder<O, T> get(String fieldName, Function<O, T> getter, T fallback) {
    return this.get().optionalFieldOf(fieldName, fallback).forGetter(getter);
  }

  public Codec<T[]> getArray() {
    return MapCodecs.flexibleArray(this.get(), this.arrayBuilder);
  }

  public <O> RecordCodecBuilder<O, T[]> getArray(String fieldName, Function<O, T[]> getter) {
    return this.getArray().fieldOf(fieldName).forGetter(getter);
  }

  public <O> RecordCodecBuilder<O, T[]> getArray(String fieldName, Function<O, T[]> getter, T... fallback) {
    return this.getArray().optionalFieldOf(fieldName, fallback).forGetter(getter);
  }

  public Codec<List<T>> getList() {
    return MapCodecs.flexibleList(this.get());
  }

  public <O> RecordCodecBuilder<O, List<T>> getList(String fieldName, Function<O, List<T>> getter) {
    return this.getList().fieldOf(fieldName).forGetter(getter);
  }

  public <O> RecordCodecBuilder<O, List<T>> getList(String fieldName, Function<O, List<T>> getter, List<T> fallback) {
    return this.getList().optionalFieldOf(fieldName, fallback).forGetter(getter);
  }

  public <O> RecordCodecBuilder<O, List<T>> getList(String fieldName, Function<O, List<T>> getter, T... fallback) {
    return this.getList().optionalFieldOf(fieldName, List.of(fallback)).forGetter(getter);
  }

  public Codec<Set<T>> getSet() {
    return MapCodecs.flexibleSet(this.get());
  }

  public <O> RecordCodecBuilder<O, Set<T>> getSet(String fieldName, Function<O, Set<T>> getter) {
    return this.getSet().fieldOf(fieldName).forGetter(getter);
  }

  public <O> RecordCodecBuilder<O, Set<T>> getSet(String fieldName, Function<O, Set<T>> getter, Set<T> fallback) {
    return this.getSet().optionalFieldOf(fieldName, fallback).forGetter(getter);
  }

  public <O> RecordCodecBuilder<O, Set<T>> getSet(String fieldName, Function<O, Set<T>> getter, T... fallback) {
    return this.getSet().optionalFieldOf(fieldName, Set.of(fallback)).forGetter(getter);
  }

  public MapCodec<Optional<T>> getOptional(String fieldName) {
    return this.get().optionalFieldOf(fieldName);
  }

  public <O> RecordCodecBuilder<O, Optional<T>> getOptional(String fieldName, Function<O, Optional<T>> getter) {
    return this.get().optionalFieldOf(fieldName).forGetter(getter);
  }

  public <O> RecordCodecBuilder<O, Optional<T>> getOptional(String fieldName, Function<O, Optional<T>> getter, Optional<T> fallback) {
    return MapCodecs.optional(this.get(), fieldName, fallback, true).forGetter(getter);
  }

  public MapCodec<T> getNullable(String fieldName) {
    return MapCodecs.nullable(this.get(), fieldName);
  }

  public <O> RecordCodecBuilder<O, T> getNullable(String fieldName, Function<O, T> getter) {
    return this.getNullable(fieldName).forGetter(getter);
  }
}
