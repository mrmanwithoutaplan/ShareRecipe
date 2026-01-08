package net.creeperhost.sharerecipe;

import com.google.gson.Gson;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.IRecipeLayoutDrawable;
import mezz.jei.api.gui.builder.ITooltipBuilder;
import mezz.jei.api.gui.buttons.IButtonState;
import mezz.jei.api.gui.buttons.IIconButtonController;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotView;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.gui.inputs.IJeiUserInput;
import mezz.jei.api.ingredients.ITypedIngredient;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.category.IRecipeCategory;
import mezz.jei.common.gui.elements.DrawableResource;
import mezz.jei.library.gui.ingredients.RecipeSlot;
import mezz.jei.library.ingredients.TypedIngredient;
import net.creeperhost.sharerecipe.mixin.DrawableResourceAccessor;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Rect2i;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import org.apache.commons.io.IOUtils;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public class ShareButtonController implements IIconButtonController {
    private final IRecipeLayoutDrawable layoutDrawable;

    public ShareButtonController(IRecipeLayoutDrawable layoutDrawable) {
        this.layoutDrawable = layoutDrawable;
    }

    @Override
    public boolean onPress(IJeiUserInput input) {
        if (input.isSimulate()) return true;
        try {
            IRecipeLayoutDrawable<?> recipeLayout = this.layoutDrawable;
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
                    list.stream().filter(iTypedIngredient -> iTypedIngredient instanceof TypedIngredient<?>).map(iTypedIngredient -> (TypedIngredient) iTypedIngredient).filter(typedIngredient -> typedIngredient.getType() == VanillaTypes.ITEM_STACK).<Optional>map(typedIngredient -> typedIngredient.getItemStack()).filter(itemStack -> itemStack.isPresent()).map(itemStack -> (ItemStack) itemStack.get()).forEach(stack -> {
                        ResourceLocation rs = BuiltInRegistries.ITEM.getKey(stack.getItem());
                        shareIngredient.add(new ShareIngredient(rs.toString(), stack.getCount(), "itemstack"));
                    });
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

            CompletableFuture.runAsync(() -> {
                try {
                    Gson gson = new Gson();
                    String json = gson.toJson(recipeData);
                    URL url = new URI("http://localhost:5000/recipe").toURL();
                    HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                    urlConnection.setRequestMethod("PUT");
                    urlConnection.setDoOutput(true);
                    urlConnection.setRequestProperty("Content-Type", "application/json");
                    OutputStream outputStream = urlConnection.getOutputStream();
                    byte[] bytes = json.getBytes(StandardCharsets.UTF_8);
                    outputStream.write(bytes);
                    if (urlConnection.getResponseCode() == 200) {
                        InputStream inputStream = urlConnection.getInputStream();
                        String body = IOUtils.toString(inputStream, StandardCharsets.UTF_8);
                        ShareResult shareResult = gson.fromJson(body, ShareResult.class);
                        if (Minecraft.getInstance().player != null) {
                            MutableComponent link = Component.literal(shareResult.url);
                            link.setStyle(link.getStyle().applyFormat(ChatFormatting.BLUE).applyFormat(ChatFormatting.UNDERLINE).withClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, shareResult.url)));
                            Component finished = Component.literal("[ShareRecipe] Your content is now available on ShareRecipe! ").append(link);
                            Minecraft.getInstance().execute(() -> Minecraft.getInstance().player.sendSystemMessage(finished));
                        }
                    } else {
                        if (Minecraft.getInstance() != null && Minecraft.getInstance().player != null) {
                            Component finished = Component.literal("[ShareRecipe] An error occurred uploading your content to ShareRecipe.");
                            Minecraft.getInstance().execute(() -> Minecraft.getInstance().player.sendSystemMessage(finished));
                        }
                    }

                    urlConnection.disconnect();
                } catch (Exception e2) {}
            });

        } catch (Exception ex) {
//          ex.printStackTrace();
        }
        return true;
    }

    @Override
    public void getTooltips(ITooltipBuilder tooltip) {
        IIconButtonController.super.getTooltips(tooltip);
    }

    @Override
    public void initState(IButtonState state) {
        IIconButtonController.super.initState(state);
    }

    @Override
    public void updateState(IButtonState state) {
        IIconButtonController.super.updateState(state);
    }
}
