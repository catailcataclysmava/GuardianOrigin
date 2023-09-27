

package com.avacat.guardianorigin;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import io.github.edwinmindcraft.apoli.api.IDynamicFeatureConfiguration;

import java.util.Optional;


public record SummonSwordConfig(Optional<String> s) implements IDynamicFeatureConfiguration {
    public static final MapCodec<SummonSwordConfig> MAP_CODEC = RecordCodecBuilder.mapCodec((instance) -> {
        return instance.group(
                Codec.STRING.optionalFieldOf("s").forGetter(SummonSwordConfig::b)
        ).apply(instance, SummonSwordConfig::new);
    });

    public Optional<String> b() {
        return this.s;
    }

    public static final Codec<SummonSwordConfig> CODEC;

    public SummonSwordConfig(Optional<String> s) {
        this.s = s;
    }

    static {
        CODEC = MAP_CODEC.codec();
    }
}
