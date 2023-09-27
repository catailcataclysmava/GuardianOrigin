package com.avacat.guardianorigin.event;

import com.avacat.guardianorigin.BoundWeapon.PlayerBoundWeapon;
import com.avacat.guardianorigin.BoundWeapon.PlayerBoundWeaponProvider;
import com.avacat.guardianorigin.GuardianOrigin;
import com.avacat.guardianorigin.sound.ModSounds;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.monster.Guardian;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrownTrident;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TridentItem;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.GrindstoneEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.enchanting.EnchantmentLevelSetEvent;
import net.minecraftforge.event.entity.ProjectileImpactEvent;
import net.minecraftforge.event.entity.item.ItemTossEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingGetProjectileEvent;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import net.minecraftforge.event.entity.player.PlayerContainerEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.level.NoteBlockEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.common.Mod;

import java.awt.event.ContainerEvent;
import java.util.List;

@Mod.EventBusSubscriber(modid = GuardianOrigin.MODID)
public class ModEvents {
    @SubscribeEvent
    public static void onAttachCapabilitiesPlayer(AttachCapabilitiesEvent<Entity> event) {
        if(event.getObject() instanceof Player) {
            if(!event.getObject().getCapability(PlayerBoundWeaponProvider.PLAYER_BOUND_WEAPON).isPresent()) {
                event.addCapability(new ResourceLocation(GuardianOrigin.MODID, "properties"), new PlayerBoundWeaponProvider());
            }
        }
    }

    @SubscribeEvent
    public static void onPlayerCloned(PlayerEvent.Clone event) {
        if(event.isWasDeath()) {
            event.getOriginal().reviveCaps();
            event.getOriginal().getCapability(PlayerBoundWeaponProvider.PLAYER_BOUND_WEAPON).ifPresent(oldStore -> {
                event.getEntity().getCapability(PlayerBoundWeaponProvider.PLAYER_BOUND_WEAPON).ifPresent(newStore -> {
                    newStore.copyFrom(oldStore);
                    event.getOriginal().invalidateCaps();
                });
            });
        }
    }

    @SubscribeEvent
    public static void onRegisterCapabilities(RegisterCapabilitiesEvent event) {
        event.register(PlayerBoundWeapon.class);
    }

    @SubscribeEvent
    public static  void on_toss (ItemTossEvent event){
        if (event.isCancelable()){
            Player player = event.getPlayer();
            ItemStack droppedItem = event.getEntity().getItem();
            if (droppedItem.getTag() != null && droppedItem.getTag().getBoolean("Bound Weapon")){
                event.setCanceled(true);
                player.level.playSound(null ,player.position().x(),player.position().y(),player.position().z(),
                        ModSounds.UNSUMMONED_BOUND_WEAPON.get(), SoundSource.MASTER, 1f,1f);


            }
        }
    }

    @SubscribeEvent
    public static void on_chest_close (PlayerContainerEvent.Close event){
        AbstractContainerMenu openContainer = event.getContainer();
        AbstractContainerMenu playerInventory = event.getEntity().inventoryMenu;
        if (!openContainer.equals(playerInventory)) {
            int items = event.getContainer().slots.size();
            for (int i = 0; i < items; i++) {
                if (event.getContainer().getSlot(i).container == event.getEntity().getInventory()) continue;
                ItemStack item = event.getContainer().getSlot(i).getItem();
                if (item.getTag() != null && item.getTag().getBoolean("Bound Weapon")) {
                    //                items.set(i, ItemStack.EMPTY);
                    event.getContainer().getSlot(i).set(ItemStack.EMPTY);
                    event.getEntity().level.playSound(null ,event.getEntity().position().x(),event.getEntity().position().y(),event.getEntity().position().z(),
                            ModSounds.UNSUMMONED_BOUND_WEAPON.get(), SoundSource.MASTER, 1f,1f);
                }
            }
        }
        // removing all weapons that dont belong to player closing chest.
        int size = event.getEntity().getInventory().getContainerSize();
        for (int i=0;i<size;i++){

            ItemStack item = event.getEntity().getInventory().getItem(i);
            if (item.getTag()!= null && item.getTag().getBoolean("Bound Weapon") && !item.getTag().getUUID("Owner").equals(event.getEntity().getUUID())){
                event.getEntity().getInventory().setItem(i, ItemStack.EMPTY);
                event.getEntity().level.playSound(null ,event.getEntity().position().x(),event.getEntity().position().y(),event.getEntity().position().z(),
                        ModSounds.UNSUMMONED_BOUND_WEAPON.get(), SoundSource.MASTER, 1f,1f);
            }
        }
    }
    @SubscribeEvent
    public static void on_item_pickup (EntityItemPickupEvent event){
        if ( event.getItem().getItem().getTag() != null &&  event.getItem().getItem().getTag().getBoolean("Bound Weapon") ) {

            event.getItem().setItem(ItemStack.EMPTY);
            event.setCanceled(true);
        }


    }

     @SubscribeEvent
     public static void projectileimpact (ProjectileImpactEvent event){

       if ( event.getProjectile() instanceof ThrownTrident &&
               event.getProjectile().serializeNBT().getCompound("Trident").getCompound("tag").getBoolean("Bound Weapon")){
             event.getProjectile().kill();
         }
     }

    @SubscribeEvent
    public static void on_set_enchant_level(EnchantmentLevelSetEvent event ){
        if (event.getItem().getTag() != null && event.getItem().getTag().getBoolean("Bound Weapon")) {
            event.setEnchantLevel(0);
        }

    }
    @SubscribeEvent
    public static void grindestoneevent (GrindstoneEvent.OnplaceItem event){
        if (event.getBottomItem().getTag() != null && event.getBottomItem().getTag().getBoolean("Bound Weapon")){
            event.setCanceled(true);
        }
        if (event.getTopItem().getTag() != null && event.getTopItem().getTag().getBoolean("Bound Weapon")){
            event.setCanceled(true);
        }

    }
    @SubscribeEvent
    public static void playerdeath (LivingDeathEvent event){
        if (event.getEntity() instanceof Player){
            for (int i=0;i<((Player) event.getEntity()).getInventory().getContainerSize();i++){

                ItemStack item = ((Player) event.getEntity()).getInventory().getItem(i);
                if (item.getTag()!= null && item.getTag().getBoolean("Bound Weapon")){
                    ((Player) event.getEntity()).getInventory().setItem(i, ItemStack.EMPTY);
                }
            }
        }
    }

}
