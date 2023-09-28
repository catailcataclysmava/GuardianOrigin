package com.avacat.guardianorigin;

import com.avacat.guardianorigin.BoundWeapon.PlayerBoundWeaponProvider;
import io.github.edwinmindcraft.apoli.api.power.factory.EntityAction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;

import javax.swing.text.html.Option;
import java.util.Currency;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

public class BindSwordEntityAction extends EntityAction<BindSwordConfig> {
    public BindSwordEntityAction() {
        super(BindSwordConfig.CODEC);
    }

    public void execute(BindSwordConfig configuration, Entity entity) {
        if (entity instanceof Player){
            Player player = (Player)entity;
            ItemStack selected = player.getInventory().getSelected();
            player.getCapability(PlayerBoundWeaponProvider.PLAYER_BOUND_WEAPON).ifPresent(weapon ->{
                ItemStack currentWeapon = (weapon.getWeapon());
                if (currentWeapon == null || currentWeapon.isEmpty() || !currentWeapon.getTag().getBoolean("Bound Weapon")){
                    if (selected.getItem() instanceof SwordItem || selected.getItem() instanceof BowItem ||
                            selected.getItem() instanceof TridentItem || selected.getItem() instanceof CrossbowItem){
                        CompoundTag tag = selected.getTag();
                        tag.putByte("Unbreakable", (byte)1);
                        tag.putBoolean("Bound Weapon",true);
                        tag.putUUID("Owner",player.getUUID());

                        selected.setTag(tag);
                        weapon.bindWeapon(selected);

                    }
                    else  player.sendSystemMessage(Component.literal("Bound item must be a sword, trident,bow, or crossbow"));
                }
                else{
                    if (selected.sameItem(currentWeapon) && selected.getTag().getUUID("Owner").equals(player.getUUID())){
                        CompoundTag tag = selected.getTag();

                        tag.putByte("Unbreakable", (byte)0);

                        tag.putBoolean("Bound Weapon",false);

                        tag.putUUID("Owner", new UUID(0L,0L));

                        selected.setTag(tag);
                        weapon.bindWeapon(null);

                    }
                    else  player.sendSystemMessage(Component.literal("Hold your bound weapon first"));

                }

            });


            //check if there is a bound sword
                // if there is bound sword check the item in the players hand is it
                    //if the item is the bound sword, unbind the sword & remove tags on held sword
                    //else tell the player to first hold sword
                //else check the item in hand is a sword
                    // if the item is a sword give it the tags & bind the sword
                    // else tell the player to hold a sword
        }
    }
}

