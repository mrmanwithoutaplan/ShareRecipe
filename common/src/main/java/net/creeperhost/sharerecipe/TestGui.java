package net.creeperhost.sharerecipe;

import net.creeperhost.polylib.client.screen.widget.buttons.DropdownButton;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;
import java.util.List;

public class TestGui extends Screen
{
    private DropdownButton<Menu> menuDropdownButton;
    private  DropdownButton<Menu> specialnewbutton;

    public TestGui()
    {
        super(Component.empty());
    }

    @Override
    protected void init()
    {
        super.init();
        List<String> strings = new ArrayList<>();
        strings.add("Mute");
        strings.add("Add friend");
        strings.add("Mention");
        addRenderableWidget(menuDropdownButton = new DropdownButton<>(50, 20, Component.literal("Dropdown Button"), new Menu(strings), true, button -> {}));
        addRenderableWidget(Button.builder(Component.literal("TestMod test screen"), button ->
        {
            System.out.println("You pressed me");
        }).pos((width / 2) - 80, 40).build());

        addRenderableWidget(new IconButton(width - 100, height - 100, 50, 50, Component.literal(""), e -> {
            System.out.println("moo");
        })
                .setIcon(ResourceLocation.fromNamespaceAndPath(ShareRecipe.MOD_ID, "textures/gui/sunglasses_cow_icon.png"), 16, 16)
        );
        addRenderableWidget(new IconButton(width - 100, height - 150, 50, 50, Component.literal(""), e -> System.out.println("meow"))
                .setIcon(ResourceLocation.fromNamespaceAndPath(ShareRecipe.MOD_ID, "textures/gui/other_cat_icon.png"), 16, 16)
        );

        addRenderableWidget(Button.builder(Component.literal("Main Menu"), button -> minecraft.setScreen(new TitleScreen())).pos((width / 2) - 60, height - 50).build());
    }

    public static class Menu implements DropdownButton.IDropdownOption
    {
        List<DropdownButton.IDropdownOption> possibleValsCache;
        public String option;

        public Menu(List<String> options)
        {
            possibleValsCache = new ArrayList<>();
            possibleValsCache.add(this);
            option = options.get(0);
            options.remove(0);
            for (String option : options)
            {
                possibleValsCache.add(new Menu(possibleValsCache, option));
            }
        }

        public Menu(List<DropdownButton.IDropdownOption> vals, String option)
        {
            possibleValsCache = vals;
            this.option = option;
        }

        @Override
        public String getTranslate(DropdownButton.IDropdownOption current, boolean dropdownOpen)
        {
            return option;
        }

        @Override
        public List<DropdownButton.IDropdownOption> getPossibleVals()
        {
            return possibleValsCache;
        }
    }
}