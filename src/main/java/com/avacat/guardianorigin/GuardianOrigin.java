package com.avacat.guardianorigin;

import com.avacat.guardianorigin.sound.ModSounds;
import com.mojang.logging.LogUtils;

import io.github.edwinmindcraft.apoli.api.power.factory.EntityAction;
import io.github.edwinmindcraft.apoli.api.registry.ApoliRegistries;
import net.minecraft.tags.TagKey;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import org.slf4j.Logger;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(GuardianOrigin.MODID)
public class GuardianOrigin
{
    // Define mod id in a common place for everything to reference
    public static final String MODID = "guardianorigin";
    // Directly reference a slf4j logger
    public static final Logger LOGGER = LogUtils.getLogger();

    public static final DeferredRegister<EntityAction<?>> ENTITY_ACTIONS;
    private static RegistryObject<BindSwordEntityAction> BIND_SWORD;
    private static RegistryObject<SummonSwordEntityAction> SUMMON_SWORD;
    private static String VERSION = "0.1";

    public GuardianOrigin()
    {

        IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();
        String version = ModLoadingContext.get().getActiveContainer().getModInfo().getVersion().toString();
        if (version.contains("+")) {
            version = version.split("\\+")[0];
        }
        if (version.contains("-")) {
            version = version.split("-")[0];
        }
        GuardianOrigin.VERSION = version;

        GuardianOrigin.register();


    }

    public static void register() {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        ENTITY_ACTIONS.register(bus);
        BIND_SWORD = ENTITY_ACTIONS.register("bind_sword", BindSwordEntityAction::new);
        SUMMON_SWORD = ENTITY_ACTIONS.register("summon_sword", SummonSwordEntityAction::new);
        ModSounds.register(bus);


    }
    static {
        ENTITY_ACTIONS = DeferredRegister.create(ApoliRegistries.ENTITY_ACTION_KEY, MODID);

    }



}
