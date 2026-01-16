package net.creeperhost.sharerecipe;

import com.google.gson.Gson;
import com.mojang.blaze3d.pipeline.RenderTarget;
import com.mojang.blaze3d.pipeline.TextureTarget;
import com.mojang.blaze3d.platform.NativeImage;
import com.mojang.blaze3d.systems.RenderSystem;
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
import net.creeperhost.sharerecipe.mixin.DrawableResourceAccessor;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.Rect2i;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import org.apache.commons.codec.digest.DigestUtils;
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

public class ShareButtonController<T> implements IIconButtonController {
    private final IRecipeLayoutDrawable<T> layoutDrawable;

    public ShareButtonController(IRecipeLayoutDrawable<T> layoutDrawable) {
        this.layoutDrawable = layoutDrawable;
    }

    public byte[] actualRenderCall(RenderTarget framebuffer, int bufferWidth, int bufferHeight, float scale, Rect2i rect, GuiGraphics guiGraphics) {
        Minecraft client = Minecraft.getInstance();
        framebuffer.clear(Minecraft.ON_OSX);
        framebuffer.bindWrite(true);

        RenderSystem.viewport(0, 0, bufferWidth, bufferHeight);
        RenderSystem.disableScissor();
        RenderSystem.enableDepthTest();

        org.joml.Matrix4f projectionMatrix = new org.joml.Matrix4f()
                .setOrtho(0.0F, (float)bufferWidth, (float)bufferHeight, 0.0F, -1000.0F, 1000.0F);
        RenderSystem.setProjectionMatrix(projectionMatrix, com.mojang.blaze3d.vertex.VertexSorting.ORTHOGRAPHIC_Z);

        //            com.mojang.blaze3d.vertex.ByteBufferBuilder byteBuffer = new com.mojang.blaze3d.vertex.ByteBufferBuilder(2048);
        //            net.minecraft.client.renderer.MultiBufferSource.BufferSource bufferSource =
        //                    net.minecraft.client.renderer.MultiBufferSource.immediate(byteBuffer);

        int x = rect.getX();
        int y = rect.getY();

        this.layoutDrawable.setPosition(0, 0);


        guiGraphics.pose().pushPose();
        guiGraphics.pose().scale(scale, scale, 1.0f);

        ShareRecipe.fakeZero.set(true);
        this.layoutDrawable.drawRecipe(guiGraphics, 0, 0);
        ShareRecipe.fakeZero.set(false);

        this.layoutDrawable.setPosition(x, y);

        //            if (background != null) {
        //                background.draw(guiGraphics);
        //            }
        //            recipeCategory.draw(recipe, drawable.getRecipeSlotsView(), guiGraphics, 0, 0);

        guiGraphics.flush();
        //            bufferSource.endBatch(); // Forces the draw to the Framebuffer
        guiGraphics.pose().popPose();

        NativeImage image = new NativeImage(bufferWidth, bufferHeight, false);
        RenderSystem.bindTexture(framebuffer.getColorTextureId());
        image.downloadTexture(0, false);
        image.flipY();


        try {
            return image.asByteArray();
//            image.writeToFile(new File(client.gameDirectory, "render_debug.png"));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            image.close();
            framebuffer.destroyBuffers();
            client.getMainRenderTarget().bindWrite(true);
            client.resizeDisplay();
        }
        return null;
    }

    public record BackgroundRender(byte[] image, int width, int height, List<NerfedGuiGraphics.CapturedString> strings) {}

    public BackgroundRender renderBackground() {
        IRecipeLayoutDrawable<T> drawable = this.layoutDrawable;


        Rect2i rect = drawable.getRect();

        int scale = 4;
        int logicalWidth = rect.getWidth();
        int logicalHeight = rect.getHeight();

        int bufferWidth = logicalWidth * scale;
        int bufferHeight = logicalHeight * scale;

        RenderTarget framebuffer = new TextureTarget(bufferWidth, bufferHeight, true, Minecraft.ON_OSX);
        framebuffer.setClearColor(0.0f, 0.0f, 0.0f, 0.0f);

        Minecraft client = Minecraft.getInstance();

        NerfedGuiGraphics guiGraphics = new NerfedGuiGraphics(client, client.renderBuffers().bufferSource());

        return new BackgroundRender(this.actualRenderCall(framebuffer, bufferWidth, bufferHeight, scale, rect, guiGraphics), bufferWidth, bufferHeight, guiGraphics.capturedStrings);
    }

    @Override
    public boolean onPress(IJeiUserInput input) {
        if (input.isSimulate()) return true;
        try {

            IRecipeLayoutDrawable<?> recipeLayout = this.layoutDrawable;
            IRecipeCategory<?> recipeCategory = recipeLayout.getRecipeCategory();

            BackgroundRender backgroundRender = renderBackground();

            String backgroundSha = DigestUtils.sha1Hex(backgroundRender.image());

            Background ourBackground = new Background(backgroundSha, backgroundRender.width(), backgroundRender.height(), backgroundRender.strings());
            String cat = recipeCategory.getTitle().getString();
            IRecipeSlotsView recipeSlotsView = recipeLayout.getRecipeSlotsView();
            List<IRecipeSlotView> inputSlots = recipeSlotsView.getSlotViews(RecipeIngredientRole.INPUT);
            List<IRecipeSlotView> outputSlots = recipeSlotsView.getSlotViews(RecipeIngredientRole.OUTPUT);

            int i = 0;
            List<ShareSlot> inputs =  new ArrayList<>();
            for (IRecipeSlotView inputSlot : inputSlots) {
                if (inputSlot instanceof RecipeSlot recipeSlot) {
                    Rect2i rect = recipeSlot.getRect();
                    List<ITypedIngredient<?>> list = inputSlot.getAllIngredients().toList();
                    List<ShareIngredient> shareIngredient = new ArrayList<>();
                    list.stream()
                        .filter(iTypedIngredient -> iTypedIngredient.getType() == VanillaTypes.ITEM_STACK)
                        .map(ITypedIngredient::getItemStack)
                        .filter(Optional::isPresent)
                        .map(Optional::get)
                        .forEach(stack -> {
                            ResourceLocation rs = BuiltInRegistries.ITEM.getKey(stack.getItem());
                            shareIngredient.add(new ShareIngredient(rs.toString(), stack.getCount(), "itemstack"));
                        }
                    );
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

                    boolean b = uploadBackground(backgroundRender.image(), backgroundSha);

                    if (!b) {
                        if (Minecraft.getInstance().player != null) {
                            Component finished = Component.literal("[ShareRecipe] An error occurred uploading your content to ShareRecipe.");
                            Minecraft.getInstance().execute(() -> Minecraft.getInstance().player.sendSystemMessage(finished));
                        }
                        return;
                    }

                    Gson gson = new Gson();
                    String json = gson.toJson(recipeData);
                    System.out.println(json);
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
                        if (Minecraft.getInstance().player != null) {
                            Component finished = Component.literal("[ShareRecipe] An error occurred uploading your content to ShareRecipe.");
                            Minecraft.getInstance().execute(() -> Minecraft.getInstance().player.sendSystemMessage(finished));
                        }
                    }

                    urlConnection.disconnect();
                } catch (Exception e2) {}
            });

        } catch (Exception ex) {
          ex.printStackTrace();
        }
        return true;
    }

    public static boolean uploadBackground(byte[] image, String hash) {
        try {
            URL url = new URI("http://localhost:5000/background/" + hash).toURL();
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("HEAD");
            if (urlConnection.getResponseCode() == 404) {
                url = new URI("http://localhost:5000/background").toURL();
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setDoOutput(true);
                urlConnection.setRequestMethod("PUT");
                urlConnection.getOutputStream().write(image);
                if (urlConnection.getResponseCode() == 204) {
                    return true;
                }
                return false;
            } else {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
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
