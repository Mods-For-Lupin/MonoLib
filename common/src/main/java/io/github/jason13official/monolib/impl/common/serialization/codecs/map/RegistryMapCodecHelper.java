package io.github.jason13official.monolib.impl.common.serialization.codecs.map;

import com.mojang.serialization.Codec;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.resources.RegistryFixedCodec;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;

/**
 * Adapted from Darkhax's <a href="https://github.com/Darkhax-Minecraft/Bookshelf">Bookshelf</a>
 */
public class RegistryMapCodecHelper<T> extends MapCodecHelper<Holder<T>> {

  private final MapCodecHelper<TagKey<T>> tagHelper;

  private RegistryMapCodecHelper(Codec<Holder<T>> holderCodec, ResourceKey<Registry<T>> key) {
    super(holderCodec);
    this.tagHelper = new MapCodecHelper<>(TagKey.codec(key));
  }

  public static <T> RegistryMapCodecHelper<T> create(Registry<T> registry) {
    return new RegistryMapCodecHelper<>(registry.holderByNameCodec(),
        (ResourceKey<Registry<T>>) registry.key());
  }

  public static <T> RegistryMapCodecHelper<T> create(ResourceKey<Registry<T>> key) {
    return new RegistryMapCodecHelper<>(RegistryFixedCodec.create(key), key);
  }

  public MapCodecHelper<TagKey<T>> tag() {
    return this.tagHelper;
  }
}
