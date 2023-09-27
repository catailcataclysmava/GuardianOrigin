package com.avacat.guardianorigin.BoundWeapon;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import org.checkerframework.checker.units.qual.C;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PlayerBoundWeaponProvider implements ICapabilityProvider, INBTSerializable<CompoundTag> {
    public static Capability<PlayerBoundWeapon> PLAYER_BOUND_WEAPON = CapabilityManager.get(new CapabilityToken<PlayerBoundWeapon>() {});
    private PlayerBoundWeapon weapon = null;
    private final LazyOptional<PlayerBoundWeapon> optional = LazyOptional.of(this::createPlayerBoundWeapon);

    private PlayerBoundWeapon createPlayerBoundWeapon() {
        if (this.weapon == null){
            this.weapon = new PlayerBoundWeapon();
        }
        return this.weapon;
    }


    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if(cap == PLAYER_BOUND_WEAPON){
            return optional.cast();
        }
        return LazyOptional.empty();
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag nbt = new CompoundTag();
        createPlayerBoundWeapon().saveNBTData(nbt);
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        createPlayerBoundWeapon().loadNBTData(nbt);

    }
}
