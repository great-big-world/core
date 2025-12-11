package dev.creoii.greatbigworld.data;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.Map;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;

public record Mappings(Identifier registry, Map<Identifier, Value> values, Value defaultValue) {
    public static final Codec<Mappings> CODEC = RecordCodecBuilder.create(instance -> {
        return instance.group(Identifier.CODEC.fieldOf("registry").forGetter(mappings -> {
            return mappings.registry;
        }), Codec.unboundedMap(Identifier.CODEC, Value.CODEC).fieldOf("values").forGetter(mappings -> {
            return mappings.values;
        }), Value.CODEC.fieldOf("default").forGetter(mappings -> {
            return mappings.defaultValue;
        })).apply(instance, Mappings::new);
    });

    public Registry<?> getRegistry(RegistryAccess registryManager) {
        return registryManager.lookup(ResourceKey.createRegistryKey(registry)).orElseThrow();
    }

    public Value getValue(RegistryAccess registryManager, Identifier key) {
        if (values.containsKey(key) && getRegistry(registryManager).containsKey(key)) {
            return values.get(key);
        }
        return defaultValue;
    }

    public record Value(PrimitiveType type, Object obj) {
        public static final Codec<Value> CODEC = Codec.either(Codec.FLOAT, Codec.STRING).xmap(either -> {
            return either.left().isPresent() ? new Value(PrimitiveType.NUMBER, either.left().get()) : new Value(PrimitiveType.STRING, either.right().get());
        }, value -> {
            if (value.obj() instanceof String s)
                return Either.right(s);
            else return Either.left((Float) value.obj);
        });

        public Number getAsNumber() {
            return (Number) obj;
        }

        public String getAsString() {
            return (String) obj;
        }

        public enum PrimitiveType {
            NUMBER,
            STRING
        }
    }
}
