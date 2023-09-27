package com.avacat.guardianorigin.BoundWeapon;

import com.avacat.guardianorigin.GuardianOrigin;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

public class PlayerBoundWeapon {
//    private ItemStack weapon;
    private CompoundTag tag;

    public ItemStack getWeapon(){

        return ItemStack.of(tag);
    }

    public void bindWeapon(ItemStack weapon){
        if (weapon != null) {
            this.tag = weapon.serializeNBT();
        }
        else tag = new CompoundTag();

    }
    public void copyFrom(PlayerBoundWeapon source){
        this.tag = source.tag;
    }
    public void saveNBTData(CompoundTag nbt){
        if (tag == null){

            nbt.put("Bound Weapon",ItemStack.EMPTY.serializeNBT());
        }
        else {

            CompoundTag weaponNBT = tag;
            nbt.put("Bound Weapon", weaponNBT);
        }
    }
    public void loadNBTData(CompoundTag nbt){
        CompoundTag weaponNBT = (CompoundTag)nbt.get("Bound Weapon");
        if (weaponNBT != null) {
            tag = weaponNBT;


        }
    }

}
