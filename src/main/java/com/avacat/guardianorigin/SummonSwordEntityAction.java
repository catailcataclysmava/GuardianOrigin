package com.avacat.guardianorigin;

import com.avacat.guardianorigin.BoundWeapon.PlayerBoundWeaponProvider;
import com.avacat.guardianorigin.sound.ModSounds;
import io.github.edwinmindcraft.apoli.api.power.factory.EntityAction;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class SummonSwordEntityAction extends EntityAction<BindSwordConfig> {
    public SummonSwordEntityAction() {
        super(BindSwordConfig.CODEC);
    }

    public void execute(BindSwordConfig configuration, Entity entity) {
        if (entity instanceof Player){
            Player player = (Player)entity;
            player.getCapability(PlayerBoundWeaponProvider.PLAYER_BOUND_WEAPON).ifPresent(weapon -> {
                ItemStack currentWeapon = (weapon.getWeapon());
                if (currentWeapon != null && !currentWeapon.isEmpty()){
                    ItemStack selected = player.getInventory().getSelected();
                    if (selected.isEmpty()){
                        summonSword(player, currentWeapon);

                    }
                    else{
                        if (selected.sameItem(currentWeapon) && selected.getTag().getBoolean("Bound Weapon")){
                            player.getInventory().setItem(player.getInventory().selected, ItemStack.EMPTY);
                            player.level.playSound(null ,player.position().x(),player.position().y(),player.position().z(),
                                    ModSounds.UNSUMMONED_BOUND_WEAPON.get(), SoundSource.MASTER, 1f,1f);
                        }
                        else if(player.getInventory().getFreeSlot() >= 0){
                            player.getInventory().setItem(player.getInventory().getFreeSlot(),player.getInventory().getSelected());
                            summonSword(player,currentWeapon);
                        }
                        else{
                            player.sendSystemMessage(Component.literal("No place to move held item(s)"));
                        }
                    }
                }
                else {
                    player.sendSystemMessage(Component.literal("First bind a weapon to summon it"));
                }
            });
            //check if a sword is bound
                //check if the hand is empty
                    //if the hand is empty, place bound sword in hand & remove any existing bound sword
                    //if the hand is not empty
                        //if the item is the bound sword, dismiss the sword
                        //else check if there is a free inventory slot
                           //if there is a free slot move the held item there & place sword in hand & remove any existing bound sword
                          // if there is no free slot error message

        }
    }
    private void summonSword(Player player, ItemStack currentWeapon) {
        int size = player.getInventory().getContainerSize();
        for (int i=0;i<size;i++){

           ItemStack item = player.getInventory().getItem(i);
           if (item.getTag()!= null && item.getTag().getBoolean("Bound Weapon")){
               player.getInventory().setItem(i, ItemStack.EMPTY);
           }
        }
        player.getInventory().setItem(player.getInventory().selected, currentWeapon);
        player.level.playSound(null ,player.position().x(),player.position().y(),player.position().z(),
    ModSounds.SUMMONED_BOUND_WEAPON.get(), SoundSource.MASTER, 1f,1f);
        player.getLevel().getServer().getLevel(player.getLevel().dimension()).sendParticles
                    (ParticleTypes.ENCHANT,player.getX(),player.getY()+1,player.getZ(),300,0.5d,.5d,0.5d,0.5);

    }

}

