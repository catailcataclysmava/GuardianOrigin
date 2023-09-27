package com.avacat.guardianorigin.sound;

import com.avacat.guardianorigin.GuardianOrigin;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.apache.commons.compress.archivers.zip.ScatterZipOutputStream;

public class ModSounds {
    public static  final DeferredRegister<SoundEvent> SOUND_EVENTS =
            DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, GuardianOrigin.MODID);

public static final RegistryObject<SoundEvent> SUMMONED_BOUND_WEAPON =
        registerSoundEvents("summoned_bound_weapon");
    public static final RegistryObject<SoundEvent> UNSUMMONED_BOUND_WEAPON =
            registerSoundEvents("unsummoned_bound_weapon");



    private static RegistryObject<SoundEvent> registerSoundEvents(String name){
        return SOUND_EVENTS.register(name, () -> new SoundEvent(new ResourceLocation(GuardianOrigin.MODID, name)));
    };

    public static void register(IEventBus eventBus){
        SOUND_EVENTS.register(eventBus);
    }
}
