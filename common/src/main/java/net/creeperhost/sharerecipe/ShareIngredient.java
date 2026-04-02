package net.creeperhost.sharerecipe;

public record ShareIngredient(String name, int count, boolean enchanted, boolean barVisible, int barColour, int barWidth, ShareButtonController.Tooltip tooltip,
                              String type
) {
}
