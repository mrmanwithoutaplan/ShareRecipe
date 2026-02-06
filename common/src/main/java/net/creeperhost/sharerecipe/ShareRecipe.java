package net.creeperhost.sharerecipe;

import net.creeperhost.polylib.development.DevelopmentTools;
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
