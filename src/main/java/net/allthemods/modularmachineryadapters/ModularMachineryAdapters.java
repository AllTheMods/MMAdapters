package net.allthemods.modularmachineryadapters;

import hellfirepvp.modularmachinery.common.crafting.adapter.RecipeAdapter;
import net.allthemods.modularmachineryadapters.forestry.ThermionicFabricatorRecipeAdaptor;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import static net.allthemods.modularmachineryadapters.ModularMachineryAdapters.*;

@Mod(modid = MOD_ID, name = MOD_NAME, version = VERSION, dependencies = DEPEND)
@EventBusSubscriber
public class ModularMachineryAdapters {

    public static final String MOD_ID = "modularmachineryadapters";
    public static final String MOD_NAME = "ModularMachineryAdapters";
    public static final String VERSION = "1.0.0";
    public static final String DEPEND = "required-after:modularmachinery;after:forestry";

    @SubscribeEvent
    public static void registerAdapters(RegistryEvent.Register<RecipeAdapter> event) {
        if (Loader.isModLoaded("forestry")) {
            event.getRegistry().register(new ThermionicFabricatorRecipeAdaptor());
        }
    }
}
