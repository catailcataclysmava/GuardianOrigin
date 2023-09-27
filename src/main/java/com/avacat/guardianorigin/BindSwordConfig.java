

package com.avacat.guardianorigin;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import io.github.edwinmindcraft.apoli.api.IDynamicFeatureConfiguration;

import java.util.Optional;


public record BindSwordConfig(Optional<String> s) implements IDynamicFeatureConfiguration {
    public static final MapCodec<BindSwordConfig> MAP_CODEC = RecordCodecBuilder.mapCodec((instance) -> {
        return instance.group(
                Codec.STRING.optionalFieldOf("s").forGetter(BindSwordConfig::b)
        ).apply(instance, BindSwordConfig::new);
    });

    public Optional<String> b() {
        return this.s;
    }

    public static final Codec<BindSwordConfig> CODEC;

    public BindSwordConfig(Optional<String> s) {
        this.s = s;
    }

    static {
        CODEC = MAP_CODEC.codec();
    }
}
