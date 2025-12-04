package net.creeperhost.sharerecipe.mixin;

import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.IRecipeLayoutDrawable;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotView;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.ingredients.ITypedIngredient;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.category.IRecipeCategory;
import mezz.jei.common.gui.elements.DrawableResource;
import mezz.jei.common.util.ImmutableRect2i;
import mezz.jei.gui.input.IUserInputHandler;
import mezz.jei.gui.input.handlers.CombinedInputHandler;
import mezz.jei.gui.input.handlers.ProxyInputHandler;
import mezz.jei.gui.recipes.RecipeGuiLayouts;
import mezz.jei.gui.recipes.RecipeLayoutWithButtons;
import mezz.jei.library.gui.ingredients.RecipeSlot;
import mezz.jei.library.ingredients.TypedIngredient;
import net.creeperhost.sharerecipe.*;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.Rect2i;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Mixin(RecipeGuiLayouts.class)
public abstract class RecipeGuiLayoutsMixin {

    @Final
    @Shadow(remap = false)
    private List<RecipeLayoutWithButtons<?>> recipeLayoutsWithButtons;

    @Unique
    private final HashMap<RecipeLayoutWithButtons<?>, IconButton> sharerecipe$ourButtons = new HashMap<>();
    @Unique
    private IUserInputHandler sharerecipe$cachedInputHandler;

    @Inject(method = "draw(Lnet/minecraft/client/gui/GuiGraphics;II)Ljava/util/Optional;", at = @At("HEAD"), remap = false)
    private void draw(GuiGraphics guiGraphics, int mouseX, int mouseY, CallbackInfoReturnable<Optional<IRecipeLayoutDrawable<?>>> cir) {
        for (IconButton button : sharerecipe$ourButtons.values()) {
            button.render(guiGraphics, mouseX, mouseY, 0);
        }
    }

    @Inject(method = "updateRecipeButtonPositions()V", at = @At("TAIL"), remap = false)
    private void updateRecipeButtonPositions(CallbackInfo ci) {
        for (RecipeLayoutWithButtons<?> recipeLayoutsWithButton : recipeLayoutsWithButtons) {
            GuiIconToggleButtonMixin bookmarkButton = (GuiIconToggleButtonMixin) recipeLayoutsWithButton.bookmarkButton();
            ImmutableRect2i immutableRect2i = bookmarkButton.sharerecipe$getArea();
            int width = immutableRect2i.getWidth();
            int height = immutableRect2i.getHeight();
            int x = immutableRect2i.getX();
            int y = immutableRect2i.getY() - height - 2;
            if (!sharerecipe$ourButtons.containsKey(recipeLayoutsWithButton)) {
                sharerecipe$ourButtons.put(recipeLayoutsWithButton, new IconButton(x, y, width, height, Component.literal(""), e -> {
                    try {
                        IRecipeLayoutDrawable<?> recipeLayout = recipeLayoutsWithButton.recipeLayout();
                        IRecipeCategory<?> recipeCategory = recipeLayout.getRecipeCategory();
                        IDrawable background = recipeCategory.getBackground();
                        Background ourBackground = null;
                        if (background instanceof DrawableResource) {
                            DrawableResourceAccessor resourceDrawable = (DrawableResourceAccessor) background;
                            ourBackground = new Background(resourceDrawable.sharerecipe$getResourceLocation(), resourceDrawable.sharerecipe$getWidth(), resourceDrawable.sharerecipe$getHeight(), resourceDrawable.sharerecipe$getU(), resourceDrawable.sharerecipe$getV(), resourceDrawable.sharerecipe$getWidth(), resourceDrawable.sharerecipe$getHeight());
                        }
                        String cat = recipeCategory.getTitle().getString();
                        IRecipeSlotsView recipeSlotsView = recipeLayout.getRecipeSlotsView();
                        List<IRecipeSlotView> inputSlots = recipeSlotsView.getSlotViews(RecipeIngredientRole.INPUT);
                        List<IRecipeSlotView> outputSlots = recipeSlotsView.getSlotViews(RecipeIngredientRole.OUTPUT);

                        int i = 0;
                        List<ShareSlot> inputs =  new ArrayList<>();
                        for (IRecipeSlotView inputSlot : inputSlots) {
                            if (inputSlot instanceof RecipeSlot) {
                                RecipeSlot recipeSlot = (RecipeSlot)inputSlot;
                                Rect2i rect = recipeSlot.getRect();
                                List<ITypedIngredient<?>> list = inputSlot.getAllIngredients().toList();
                                List<ShareIngredient> shareIngredient = new ArrayList<>();
                                for (ITypedIngredient<?> iTypedIngredient : list) {
                                    if (iTypedIngredient instanceof TypedIngredient<?>) {
                                        TypedIngredient typedIngredient = (TypedIngredient) iTypedIngredient;
                                        if (typedIngredient.getType() == VanillaTypes.ITEM_STACK) {
                                            Optional itemStack = typedIngredient.getItemStack();
                                            if (itemStack.isPresent()) {
                                                ItemStack stack = (ItemStack) itemStack.get();
                                                ResourceLocation rs = BuiltInRegistries.ITEM.getKey(stack.getItem());
                                                shareIngredient.add(new ShareIngredient(rs.toString(), stack.getCount(), "itemstack"));
                                            }
                                        }
                                    }
                                }
                                inputs.add(new ShareSlot(i, rect, shareIngredient));
                            }
                            i++;
                        }

                        int outputSlotNum = 0;
                        List<ShareSlot> outputs = new ArrayList<>();
                        for (IRecipeSlotView outputSlot : outputSlots) {
                            if (outputSlot instanceof RecipeSlot) {
                                Optional<ItemStack> stack = outputSlot.getDisplayedItemStack();
                                Rect2i rect = ((RecipeSlot) outputSlot).getRect();
                                if (stack.isPresent()) {
                                    ItemStack iStack = stack.get();
                                    ResourceLocation rs = BuiltInRegistries.ITEM.getKey(iStack.getItem());
                                    outputs.add(new ShareSlot(outputSlotNum, rect, List.of(new ShareIngredient(rs.toString(), iStack.getCount(), "itemstack"))));
    //                                System.out.println("OUTPUT: " + stack.get());
                                }
                            }
                            outputSlotNum++;
                        }
                        RecipeData recipeData = new RecipeData(cat, inputs, outputs, ourBackground);
                        System.out.println(recipeData.getRecipe_category());
                        recipeData.getInputs().forEach(System.out::println);
                        recipeData.getOutputs().forEach(System.out::println);

                    } catch (Exception ex) {
//                        ex.printStackTrace();
                    }
                }).setIcon(ResourceLocation.fromNamespaceAndPath(ShareRecipe.MOD_ID, "textures/gui/other_cat_icon.png"), 8, 8));
            } else {
                IconButton iconButton = sharerecipe$ourButtons.get(recipeLayoutsWithButton);
                iconButton.setX(x);
                iconButton.setY(y);
            }
        }
    }

    @Inject(method = "setRecipeLayoutsWithButtons(Ljava/util/List;)V", at = @At("TAIL"), remap = false)
    public void setRecipeLayoutsWithButtons(List<RecipeLayoutWithButtons<?>> recipeLayoutsWithButtons, CallbackInfo ci) {
        sharerecipe$ourButtons.clear();
        sharerecipe$cachedInputHandler = null;
    }

    @Inject(method = "createInputHandler()Lmezz/jei/gui/input/IUserInputHandler;", at = @At("RETURN"), remap = false, cancellable = true)
    public void createInputHandler(CallbackInfoReturnable<IUserInputHandler> cir) {
        IUserInputHandler returnValue = cir.getReturnValue();
        cir.setReturnValue(new CombinedInputHandler("sharerecipe", returnValue,
            new ProxyInputHandler(() -> {
                if (sharerecipe$cachedInputHandler == null) {
                    List<ButtonInputHandler> handlers = this.sharerecipe$ourButtons.values().stream()
                            .map(ButtonInputHandler::new)
                            .toList();
                    List<IUserInputHandler> handlerRet = new ArrayList<>(handlers);
                    sharerecipe$cachedInputHandler = new CombinedInputHandler("sharerecipecombined", handlerRet);
                }
                return sharerecipe$cachedInputHandler;
            }))
        );
    }
}