package net.allthemods.modularmachineryadapters.forestry;

import com.google.common.collect.Lists;
import forestry.api.recipes.IFabricatorRecipe;
import forestry.api.recipes.RecipeManagers;
import hellfirepvp.modularmachinery.common.crafting.MachineRecipe;
import hellfirepvp.modularmachinery.common.crafting.adapter.RecipeAdapter;
import hellfirepvp.modularmachinery.common.crafting.helper.ComponentRequirement;
import hellfirepvp.modularmachinery.common.crafting.requirements.RequirementEnergy;
import hellfirepvp.modularmachinery.common.crafting.requirements.RequirementFluid;
import hellfirepvp.modularmachinery.common.crafting.requirements.RequirementItem;
import hellfirepvp.modularmachinery.common.machine.MachineComponent;
import hellfirepvp.modularmachinery.common.modifier.RecipeModifier;
import hellfirepvp.modularmachinery.common.util.ItemUtils;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidStack;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.List;

public class ThermionicFabricatorRecipeAdaptor extends RecipeAdapter {
    public ThermionicFabricatorRecipeAdaptor() {
        super(new ResourceLocation("modularmachineryadapters", "thermionic_fabricator"));
    }

    @Nonnull
    @Override
    public Collection<MachineRecipe> createRecipesFor(ResourceLocation owningMachineName, List<RecipeModifier> modifiers, List<ComponentRequirement<?>> additionalRequirements) {
        List<MachineRecipe> machineRecipes = Lists.newArrayList();

        int incId = 0;
        for (IFabricatorRecipe fabricatorRecipe : RecipeManagers.fabricatorManager.recipes()) {
            int tickTime = Math.round(Math.max(1, RecipeModifier.applyModifiers(modifiers, RecipeModifier.TARGET_DURATION, null, 4, false)));
            MachineRecipe recipe = createRecipeShell(
                    new ResourceLocation("forestry", "thermionic_fabricator_recipe_" + incId++),
                    owningMachineName,
                    tickTime, 0);

            fabricatorRecipe.getIngredients()
                    .stream()
                    .flatMap(Collection::stream)
                    .forEach(input -> {
                        int inAmount = Math.round(RecipeModifier.applyModifiers(modifiers,
                                RecipeModifier.TARGET_ITEM, MachineComponent.IOType.INPUT,
                                input.getCount(), false));
                        if (inAmount > 0) {
                            recipe.addRequirement(new RequirementItem(MachineComponent.IOType.INPUT,
                                    ItemUtils.copyStackWithSize(input, inAmount)));
                        }
                    });

            fabricatorRecipe.getOreDicts()
                    .forEach(input -> {
                        int inAmount = Math.round(RecipeModifier.applyModifiers(modifiers,
                                RecipeModifier.TARGET_ITEM, MachineComponent.IOType.INPUT,
                                1, false));
                        if (inAmount > 0) {
                            recipe.addRequirement(new RequirementItem(MachineComponent.IOType.INPUT,
                                    input, inAmount));
                        }
                    });

            FluidStack fluidInput = fabricatorRecipe.getLiquid();
            int fluidInAmount = Math.round(RecipeModifier.applyModifiers(modifiers, RecipeModifier.TARGET_ITEM, MachineComponent.IOType.OUTPUT, fluidInput.amount, false));
            if (fluidInAmount > 0) {
                recipe.addRequirement(new RequirementFluid(MachineComponent.IOType.INPUT, new FluidStack(fluidInput, fluidInAmount)));
            }

            int inEnergy = Math.round(RecipeModifier.applyModifiers(modifiers, RecipeModifier.TARGET_ENERGY, MachineComponent.IOType.INPUT, 50, false));
            if (inEnergy > 0) {
                recipe.addRequirement(new RequirementEnergy(MachineComponent.IOType.INPUT, inEnergy));
            }

            ItemStack output = fabricatorRecipe.getRecipeOutput();
            int outAmount = Math.round(RecipeModifier.applyModifiers(modifiers, RecipeModifier.TARGET_ITEM, MachineComponent.IOType.OUTPUT, output.getCount(), false));
            if (outAmount > 0) {
                recipe.addRequirement(new RequirementItem(MachineComponent.IOType.OUTPUT, ItemUtils.copyStackWithSize(output, outAmount)));
            }

            machineRecipes.add(recipe);
        }
        return machineRecipes;
    }
}
