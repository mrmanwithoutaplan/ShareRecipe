package net.creeperhost.sharerecipe;

import dev.architectury.event.events.client.ClientGuiEvent;
import net.creeperhost.polylib.development.DevelopmentTools;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.network.chat.Component;

import java.util.List; 
import java.util.concurrent.atomic.AtomicBoolean;

public final class ShareRecipe {
    public static final String MOD_ID = "sharerecipe";
    public static AtomicBoolean fakeZero = new AtomicBoolean(false);

    public static void init() {
        DevelopmentTools.initClient();
        ModPackInfo.init();

//        ClientGuiEvent.INIT_POST.register((screen, access) ->
//        {
//            if(screen instanceof TitleScreen titleScreen)
//            {
//                Button debugScreen = Button.builder(Component.literal("TestMod test screen"), button ->
//                {
//                    Minecraft.getInstance().setScreen(new TestGui());
//                }).pos((titleScreen.width / 2) - 80, 40).build();
//
//                List<GuiEventListener> children = (List<GuiEventListener>) screen.children();
//
//                ((ScreenMixin)titleScreen).sharerecipe$getRenderables().add(debugScreen);
//                children.add(debugScreen);
//            }
//        });
    }
}
