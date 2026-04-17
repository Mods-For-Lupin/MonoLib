package io.github.jason13official.monolib.impl.common.serialization.codecs.map;

import com.mojang.datafixers.util.Either;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.MapCodec;
import io.github.jason13official.monolib.Constants;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.StringJoiner;
import java.util.UUID;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;
import net.minecraft.Optionull;
import net.minecraft.commands.synchronization.ArgumentTypeInfo;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.UUIDUtil;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentSerialization;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.StatType;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.util.valueproviders.FloatProvider;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.world.Difficulty;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.sensing.SensorType;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.entity.npc.villager.VillagerProfession;
import net.minecraft.world.entity.npc.villager.VillagerType;
import net.minecraft.world.entity.schedule.Activity;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.biome.BiomeSource;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.chunk.status.ChunkStatus;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.gameevent.PositionSourceType;
import net.minecraft.world.level.levelgen.DensityFunction;
import net.minecraft.world.level.levelgen.SurfaceRules;
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicateType;
import net.minecraft.world.level.levelgen.carver.WorldCarver;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.featuresize.FeatureSizeType;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacerType;
import net.minecraft.world.level.levelgen.feature.rootplacers.RootPlacerType;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProviderType;
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecoratorType;
import net.minecraft.world.level.levelgen.feature.trunkplacers.TrunkPlacerType;
import net.minecraft.world.level.levelgen.heightproviders.HeightProviderType;
import net.minecraft.world.level.levelgen.placement.PlacementModifierType;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;
import net.minecraft.world.level.levelgen.structure.placement.StructurePlacementType;
import net.minecraft.world.level.levelgen.structure.pools.StructurePoolElementType;
import net.minecraft.world.level.levelgen.structure.templatesystem.PosRuleTestType;
import net.minecraft.world.level.levelgen.structure.templatesystem.RuleTestType;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;
import net.minecraft.world.level.levelgen.structure.templatesystem.rule.blockentity.RuleBlockEntityModifierType;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryContainer;
import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.providers.nbt.NbtProvider;
import net.minecraft.world.level.storage.loot.providers.number.NumberProvider;
import net.minecraft.world.level.storage.loot.providers.score.ScoreboardNameProvider;
import org.apache.commons.lang3.EnumUtils;
import org.apache.commons.lang3.StringUtils;
import org.joml.Vector3fc;

/**
 * Adapted from Darkhax's <a href="https://github.com/Darkhax-Minecraft/Bookshelf">Bookshelf</a>
 */
@SuppressWarnings("unused")
public class MapCodecs {

  // JAVA TYPES
  public static final MapCodecHelper<Boolean> BOOLEAN = new MapCodecHelper<>(Codec.BOOL);
  public static final MapCodecHelper<Byte> BYTE = new MapCodecHelper<>(Codec.BYTE);
  public static final MapCodecHelper<Short> SHORT = new MapCodecHelper<>(Codec.SHORT);
  public static final MapCodecHelper<Integer> INT = new MapCodecHelper<>(Codec.INT);
  public static final MapCodecHelper<Float> FLOAT = new MapCodecHelper<>(Codec.FLOAT);
  public static final MapCodecHelper<Long> LONG = new MapCodecHelper<>(Codec.LONG);
  public static final MapCodecHelper<Double> DOUBLE = new MapCodecHelper<>(Codec.DOUBLE);
  public static final MapCodecHelper<String> STRING = new MapCodecHelper<>(Codec.STRING);
  public static final MapCodecHelper<UUID> UUID = new MapCodecHelper<>(UUIDUtil.CODEC);

  // REGISTRIES
  public static final MapCodecHelper<Holder<GameEvent>> GAME_EVENT = RegistryMapCodecHelper.create(BuiltInRegistries.GAME_EVENT);
  public static final MapCodecHelper<Holder<SoundEvent>> SOUND_EVENT = RegistryMapCodecHelper.create(BuiltInRegistries.SOUND_EVENT);
  public static final MapCodecHelper<Holder<Fluid>> FLUID = RegistryMapCodecHelper.create(BuiltInRegistries.FLUID);
  public static final MapCodecHelper<Holder<MobEffect>> MOB_EFFECT = RegistryMapCodecHelper.create(BuiltInRegistries.MOB_EFFECT);
  public static final MapCodecHelper<Holder<Block>> BLOCK = RegistryMapCodecHelper.create(BuiltInRegistries.BLOCK);
  public static final MapCodecHelper<Holder<EntityType<?>>> ENTITY_TYPE = RegistryMapCodecHelper.create(BuiltInRegistries.ENTITY_TYPE);
  public static final MapCodecHelper<Holder<Item>> ITEM = RegistryMapCodecHelper.create(BuiltInRegistries.ITEM);
  public static final MapCodecHelper<Holder<Potion>> POTION = RegistryMapCodecHelper.create(BuiltInRegistries.POTION);
  public static final MapCodecHelper<Holder<ParticleType<?>>> PARTICLE_TYPE = RegistryMapCodecHelper.create(BuiltInRegistries.PARTICLE_TYPE);
  public static final MapCodecHelper<Holder<BlockEntityType<?>>> BLOCK_ENTITY_TYPE = RegistryMapCodecHelper.create(BuiltInRegistries.BLOCK_ENTITY_TYPE);
  public static final MapCodecHelper<Holder<Identifier>> CUSTOM_STAT = RegistryMapCodecHelper.create(BuiltInRegistries.CUSTOM_STAT);
  public static final MapCodecHelper<Holder<ChunkStatus>> CHUNK_STATUS = RegistryMapCodecHelper.create(BuiltInRegistries.CHUNK_STATUS);
  public static final MapCodecHelper<Holder<RuleTestType<?>>> RULE_TEST = RegistryMapCodecHelper.create(BuiltInRegistries.RULE_TEST);
  public static final MapCodecHelper<Holder<RuleBlockEntityModifierType<?>>> RULE_BLOCK_ENTITY_MODIFIER = RegistryMapCodecHelper.create(BuiltInRegistries.RULE_BLOCK_ENTITY_MODIFIER);
  public static final MapCodecHelper<Holder<PosRuleTestType<?>>> POS_RULE_TEST = RegistryMapCodecHelper.create(BuiltInRegistries.POS_RULE_TEST);
  public static final MapCodecHelper<Holder<MenuType<?>>> MENU = RegistryMapCodecHelper.create(BuiltInRegistries.MENU);
  public static final MapCodecHelper<Holder<RecipeType<?>>> RECIPE_TYPE = RegistryMapCodecHelper.create(BuiltInRegistries.RECIPE_TYPE);
  public static final MapCodecHelper<Holder<RecipeSerializer<?>>> RECIPE_SERIALIZER = RegistryMapCodecHelper.create(BuiltInRegistries.RECIPE_SERIALIZER);
  public static final MapCodecHelper<Holder<Attribute>> ATTRIBUTE = RegistryMapCodecHelper.create(BuiltInRegistries.ATTRIBUTE);
  public static final MapCodecHelper<Holder<PositionSourceType<?>>> POSITION_SOURCE_TYPE = RegistryMapCodecHelper.create(BuiltInRegistries.POSITION_SOURCE_TYPE);
  public static final MapCodecHelper<Holder<ArgumentTypeInfo<?, ?>>> COMMAND_ARGUMENT_TYPE = RegistryMapCodecHelper.create(BuiltInRegistries.COMMAND_ARGUMENT_TYPE);
  public static final MapCodecHelper<Holder<StatType<?>>> STAT_TYPE = RegistryMapCodecHelper.create(BuiltInRegistries.STAT_TYPE);
  public static final MapCodecHelper<Holder<VillagerType>> VILLAGER_TYPE = RegistryMapCodecHelper.create(BuiltInRegistries.VILLAGER_TYPE);
  public static final MapCodecHelper<Holder<VillagerProfession>> VILLAGER_PROFESSION = RegistryMapCodecHelper.create(BuiltInRegistries.VILLAGER_PROFESSION);
  public static final MapCodecHelper<Holder<PoiType>> POINT_OF_INTEREST_TYPE = RegistryMapCodecHelper.create(BuiltInRegistries.POINT_OF_INTEREST_TYPE);
  public static final MapCodecHelper<Holder<MemoryModuleType<?>>> MEMORY_MODULE_TYPE = RegistryMapCodecHelper.create(BuiltInRegistries.MEMORY_MODULE_TYPE);
  public static final MapCodecHelper<Holder<SensorType<?>>> SENSOR_TYPE = RegistryMapCodecHelper.create(BuiltInRegistries.SENSOR_TYPE);
  public static final MapCodecHelper<Holder<Activity>> ACTIVITY = RegistryMapCodecHelper.create(BuiltInRegistries.ACTIVITY);
  public static final MapCodecHelper<Holder<MapCodec<? extends LootPoolEntryContainer>>> LOOT_POOL_ENTRY_TYPE = RegistryMapCodecHelper.create(BuiltInRegistries.LOOT_POOL_ENTRY_TYPE);
  public static final MapCodecHelper<Holder<MapCodec<? extends LootItemFunction>>> LOOT_FUNCTION_TYPE = RegistryMapCodecHelper.create(BuiltInRegistries.LOOT_FUNCTION_TYPE);
  public static final MapCodecHelper<Holder<MapCodec<? extends LootItemCondition>>> LOOT_CONDITION_TYPE = RegistryMapCodecHelper.create(BuiltInRegistries.LOOT_CONDITION_TYPE);
  public static final MapCodecHelper<Holder<MapCodec<? extends NumberProvider>>> LOOT_NUMBER_PROVIDER_TYPE = RegistryMapCodecHelper.create(BuiltInRegistries.LOOT_NUMBER_PROVIDER_TYPE);
  public static final MapCodecHelper<Holder<MapCodec<? extends NbtProvider>>> LOOT_NBT_PROVIDER_TYPE = RegistryMapCodecHelper.create(BuiltInRegistries.LOOT_NBT_PROVIDER_TYPE);
  public static final MapCodecHelper<Holder<MapCodec<? extends ScoreboardNameProvider>>> LOOT_SCORE_PROVIDER_TYPE = RegistryMapCodecHelper.create(BuiltInRegistries.LOOT_SCORE_PROVIDER_TYPE);
  public static final MapCodecHelper<Holder<MapCodec<? extends FloatProvider>>> FLOAT_PROVIDER_TYPE = RegistryMapCodecHelper.create(BuiltInRegistries.FLOAT_PROVIDER_TYPE);
  public static final MapCodecHelper<Holder<MapCodec<? extends IntProvider>>> INT_PROVIDER_TYPE = RegistryMapCodecHelper.create(BuiltInRegistries.INT_PROVIDER_TYPE);
  public static final MapCodecHelper<Holder<HeightProviderType<?>>> HEIGHT_PROVIDER_TYPE = RegistryMapCodecHelper.create(BuiltInRegistries.HEIGHT_PROVIDER_TYPE);
  public static final MapCodecHelper<Holder<BlockPredicateType<?>>> BLOCK_PREDICATE_TYPE = RegistryMapCodecHelper.create(BuiltInRegistries.BLOCK_PREDICATE_TYPE);
  public static final MapCodecHelper<Holder<WorldCarver<?>>> CARVER = RegistryMapCodecHelper.create(BuiltInRegistries.CARVER);
  public static final MapCodecHelper<Holder<Feature<?>>> FEATURE = RegistryMapCodecHelper.create(BuiltInRegistries.FEATURE);
  public static final MapCodecHelper<Holder<StructurePlacementType<?>>> STRUCTURE_PLACEMENT = RegistryMapCodecHelper.create(BuiltInRegistries.STRUCTURE_PLACEMENT);
  public static final MapCodecHelper<Holder<StructurePieceType>> STRUCTURE_PIECE = RegistryMapCodecHelper.create(BuiltInRegistries.STRUCTURE_PIECE);
  public static final MapCodecHelper<Holder<StructureType<?>>> STRUCTURE_TYPE = RegistryMapCodecHelper.create(BuiltInRegistries.STRUCTURE_TYPE);
  public static final MapCodecHelper<Holder<PlacementModifierType<?>>> PLACEMENT_MODIFIER_TYPE = RegistryMapCodecHelper.create(BuiltInRegistries.PLACEMENT_MODIFIER_TYPE);
  public static final MapCodecHelper<Holder<BlockStateProviderType<?>>> BLOCKSTATE_PROVIDER_TYPE = RegistryMapCodecHelper.create(BuiltInRegistries.BLOCKSTATE_PROVIDER_TYPE);
  public static final MapCodecHelper<Holder<FoliagePlacerType<?>>> FOLIAGE_PLACER_TYPE = RegistryMapCodecHelper.create(BuiltInRegistries.FOLIAGE_PLACER_TYPE);
  public static final MapCodecHelper<Holder<TrunkPlacerType<?>>> TRUNK_PLACER_TYPE = RegistryMapCodecHelper.create(BuiltInRegistries.TRUNK_PLACER_TYPE);
  public static final MapCodecHelper<Holder<RootPlacerType<?>>> ROOT_PLACER_TYPE = RegistryMapCodecHelper.create(BuiltInRegistries.ROOT_PLACER_TYPE);
  public static final MapCodecHelper<Holder<TreeDecoratorType<?>>> TREE_DECORATOR_TYPE = RegistryMapCodecHelper.create(BuiltInRegistries.TREE_DECORATOR_TYPE);
  public static final MapCodecHelper<Holder<FeatureSizeType<?>>> FEATURE_SIZE_TYPE = RegistryMapCodecHelper.create(BuiltInRegistries.FEATURE_SIZE_TYPE);
  public static final MapCodecHelper<Holder<MapCodec<? extends BiomeSource>>> BIOME_SOURCE = RegistryMapCodecHelper.create(BuiltInRegistries.BIOME_SOURCE);
  public static final MapCodecHelper<Holder<MapCodec<? extends ChunkGenerator>>> CHUNK_GENERATOR = RegistryMapCodecHelper.create(BuiltInRegistries.CHUNK_GENERATOR);
  public static final MapCodecHelper<Holder<MapCodec<? extends SurfaceRules.ConditionSource>>> MATERIAL_CONDITION = RegistryMapCodecHelper.create(BuiltInRegistries.MATERIAL_CONDITION);
  public static final MapCodecHelper<Holder<MapCodec<? extends SurfaceRules.RuleSource>>> MATERIAL_RULE = RegistryMapCodecHelper.create(BuiltInRegistries.MATERIAL_RULE);
  public static final MapCodecHelper<Holder<MapCodec<? extends DensityFunction>>> DENSITY_FUNCTION_TYPE = RegistryMapCodecHelper.create(BuiltInRegistries.DENSITY_FUNCTION_TYPE);
  public static final MapCodecHelper<Holder<StructureProcessorType<?>>> STRUCTURE_PROCESSOR = RegistryMapCodecHelper.create(BuiltInRegistries.STRUCTURE_PROCESSOR);
  public static final MapCodecHelper<Holder<StructurePoolElementType<?>>> STRUCTURE_POOL_ELEMENT = RegistryMapCodecHelper.create(BuiltInRegistries.STRUCTURE_POOL_ELEMENT);
  public static final MapCodecHelper<Holder<CreativeModeTab>> CREATIVE_MODE_TAB = RegistryMapCodecHelper.create(BuiltInRegistries.CREATIVE_MODE_TAB);

  // ENUMS
  public static final MapCodecHelper<Rarity> ITEM_RARITY = new MapCodecHelper<>(enumerable(Rarity.class));
  public static final MapCodecHelper<AttributeModifier.Operation> ATTRIBUTE_OPERATION = new MapCodecHelper<>(enumerable(AttributeModifier.Operation.class));
  public static final MapCodecHelper<Direction> DIRECTION = new MapCodecHelper<>(enumerable(Direction.class));
  public static final MapCodecHelper<Direction.Axis> AXIS = new MapCodecHelper<>(enumerable(Direction.Axis.class));
  public static final MapCodecHelper<Direction.Plane> PLANE = new MapCodecHelper<>(enumerable(Direction.Plane.class));
  public static final MapCodecHelper<MobCategory> MOB_CATEGORY = new MapCodecHelper<>(enumerable(MobCategory.class));
  public static final MapCodecHelper<DyeColor> DYE_COLOR = new MapCodecHelper<>(enumerable(DyeColor.class));
  public static final MapCodecHelper<SoundSource> SOUND_SOURCE = new MapCodecHelper<>(enumerable(SoundSource.class));
  public static final MapCodecHelper<Difficulty> DIFFICULTY = new MapCodecHelper<>(enumerable(Difficulty.class));
  public static final MapCodecHelper<EquipmentSlot> EQUIPMENT_SLOT = new MapCodecHelper<>(enumerable(EquipmentSlot.class));
  public static final MapCodecHelper<Mirror> MIRROR = new MapCodecHelper<>(enumerable(Mirror.class));
  public static final MapCodecHelper<Rotation> ROTATION = new MapCodecHelper<>(enumerable(Rotation.class));

  // MINECRAFT TYPES
  public static final MapCodecHelper<Identifier> RESOURCE_LOCATION = new MapCodecHelper<>(Identifier.CODEC);
  public static final MapCodecHelper<CompoundTag> COMPOUND_TAG = new MapCodecHelper<>(CompoundTag.CODEC);
  public static final MapCodecHelper<ItemStack> ITEM_STACK = new MapCodecHelper<>(ItemStack.CODEC);
  public static final MapCodecHelper<Component> TEXT = new MapCodecHelper<>(ComponentSerialization.CODEC);
  public static final MapCodecHelper<BlockPos> BLOCK_POS = new MapCodecHelper<>(BlockPos.CODEC);
  public static final MapCodecHelper<Ingredient> INGREDIENT = new MapCodecHelper<>(Ingredient.CODEC);
  public static final MapCodec<BlockState> BLOCK_STATE_MAP_CODEC = Codec.mapPair(BLOCK.get().fieldOf("block"), Codec.unboundedMap(Codec.STRING, Codec.STRING).optionalFieldOf("properties"))
      .flatXmap(MapCodecs::decodeBlockState, MapCodecs::encodeBlockState);
  public static final MapCodecHelper<BlockState> BLOCK_STATE = new MapCodecHelper<>(BLOCK_STATE_MAP_CODEC.codec());
  public static final MapCodecHelper<Vector3fc> VECTOR_3F = new MapCodecHelper<>(ExtraCodecs.VECTOR3F);

  public static <T> Codec<List<T>> flexibleList(Codec<T> codec) {
    return Codec.either(codec.listOf(), codec).xmap(either -> either.map(Function.identity(), List::of), list -> list.size() == 1 ? Either.right(list.get(0)) : Either.left(list));
  }

  public static <T> Codec<Set<T>> flexibleSet(Codec<T> codec) {
    return flexibleList(codec).xmap(LinkedHashSet::new, ArrayList::new);
  }

  public static <T> Codec<T[]> flexibleArray(Codec<T> codec, IntFunction<T[]> arrayBuilder) {
    return flexibleList(codec).xmap(list -> list.toArray(arrayBuilder.apply(list.size())), List::of);
  }

  public static <T> MapCodec<T> fallback(Codec<T> codec, String name, Supplier<T> fallbackSupplier) {
    return fallback(codec, name, fallbackSupplier, true);
  }

  public static <T> MapCodec<Optional<T>> optional(Codec<T> codec, String name, Optional<T> fallback, boolean writesDefault) {
    return codec.optionalFieldOf(name)
        .xmap(o -> o.isPresent() ? o : fallback, a -> a.isEmpty() || (Objects.equals(a.get(), fallback.orElse(null)) && !writesDefault) ? Optional.empty() : a);
  }

  public static <T> MapCodec<T> nullable(Codec<T> codec, String fieldName) {
    return codec.optionalFieldOf(fieldName).xmap(optional -> optional.orElse(null), Optional::ofNullable);
  }

  public static <T> MapCodec<T> fallback(Codec<T> codec, String name, Supplier<T> fallbackSupplier, boolean writesDefault) {
    return codec.optionalFieldOf(name).xmap(value -> value.orElse(fallbackSupplier.get()), value -> {
      final T fallbackVal = fallbackSupplier.get();
      return Objects.equals(value, fallbackVal) && !writesDefault ? Optional.empty() : Optional.of(value);
    });
  }

  public static Set<String> getPossibleMatches(String input, Iterable<String> candidates, int threshold) {
    final HashSet<String> bestMatches = new HashSet<>();
    int distance = threshold;
    for (String candidate : candidates) {
      final int currentDistance = StringUtils.getLevenshteinDistance(input, candidate);
      if (currentDistance < distance) {
        distance = currentDistance;
        bestMatches.clear();
        bestMatches.add(candidate);
      } else if (currentDistance == distance) {
        bestMatches.add(candidate);
      }
    }
    return bestMatches;
  }

  public static <T> String formatCollection(Collection<T> collection) {
    return formatCollection(collection, entry -> "\"" + entry.toString() + "\"", ", ");
  }

  public static <T> String formatCollection(Collection<T> collection, Function<T, String> formatter, String delimiter) {
    return collection.size() == 1 ? formatter.apply(collection.stream().findFirst().get()) : collection.stream().map(formatter).collect(Collectors.joining(delimiter));
  }

  public static <T extends Enum<T>> Codec<T> enumerable(Class<T> enumClass) {

    final Map<String, T> enumValues = EnumUtils.getEnumMap(enumClass);

    final Function<String, T> fromString = name -> {
      T value = enumValues.get(name);
      if (value == null) {
        value = enumValues.get(name.toUpperCase(Locale.ROOT));
      }
      return value;
    };

    final UnaryOperator<String> errorMessage = name -> {
      final StringJoiner message = new StringJoiner(" ");
      message.add("Unable to find " + enumClass.getSimpleName() + " entry \"" + name + "\".");
      final Set<String> similarMatches = getPossibleMatches(name, enumValues.keySet(), 2);
      if (!similarMatches.isEmpty()) {
        message.add("Did you mean \"" + similarMatches.stream().findFirst().get() + "\"?");
      }
      message.add("Available Options are " + formatCollection(enumValues.keySet()));
      return message.toString();
    };

    return Codec.STRING.flatXmap(string -> Optionull.mapOrElse(fromString.apply(string), DataResult::success, () -> DataResult.error(() -> errorMessage.apply(string))),
        object -> DataResult.success(object.name()));
  }

  @SuppressWarnings({"rawtypes", "unchecked"})
  private static DataResult<BlockState> decodeBlockState(Pair<Holder<Block>, Optional<Map<String, String>>> props) {

    final Block block = props.getFirst().value();
    final Map<String, String> properties = props.getSecond().orElse(new HashMap<>());
    BlockState state = block.defaultBlockState();

    if (!properties.isEmpty()) {

      final StateDefinition<Block, BlockState> definition = block.getStateDefinition();

      for (Map.Entry<String, String> entry : properties.entrySet()) {

        final Property<? extends Comparable<?>> property = definition.getProperty(entry.getKey());

        if (property != null) {

          final Optional<?> value = property.getValue(entry.getValue());

          if (value.isPresent()) {

            try {
              state = state.setValue((Property) property, (Comparable) value.get());
            } catch (final Exception e) {
              Constants.LOG.error("Failed to update state for block {} with valid value {}={}. The mod that adds this block may have a serious issue.", BuiltInRegistries.BLOCK.getKey(block),
                  entry.getKey(), entry.getValue());
              return DataResult.error(e::getMessage);
            }
          } else {
            return DataResult.error(
                () -> "\"" + entry.getValue() + "\" is not a valid value for property \"" + property.getName() + "\" on block \"" + BuiltInRegistries.BLOCK.getKey(block) + "\". Available values: "
                    + property.getAllValues().map(propVal -> ((Property) property).getName(propVal.value())).collect(Collectors.joining()));
          }
        } else {
          return DataResult.error(
              () -> "The property \"" + entry.getKey() + "\" is not valid for block \"" + BuiltInRegistries.BLOCK.getKey(block) + "\". Available properties: " + definition.getProperties().stream()
                  .map(Property::getName).collect(Collectors.joining()));
        }
      }
    }

    return DataResult.success(state);
  }

  @SuppressWarnings({"rawtypes", "unchecked", "deprecation"})
  private static DataResult<Pair<Holder<Block>, Optional<Map<String, String>>>> encodeBlockState(BlockState state) {

    final Map<String, String> propertyMap = new HashMap<>();

    state.getValues().forEach(propValue -> {
      final Property prop = propValue.property();
      propertyMap.put(prop.getName(), prop.getName(propValue.value()));
    });

    return DataResult.success(new Pair<>(state.getBlock().builtInRegistryHolder(), Optional.ofNullable(propertyMap.isEmpty() ? null : propertyMap)));
  }
}
