package net.creeperhost.sharerecipe;

public record Background(String backgroundIdentifier, int width, int height,
                         java.util.List<NerfedGuiGraphics.CapturedString> strings,
                         java.util.List<ShareButtonController.Tooltip> tooltips) {
}
