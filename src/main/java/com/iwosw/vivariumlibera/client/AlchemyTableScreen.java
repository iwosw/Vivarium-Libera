package com.iwosw.vivariumlibera.client;

import com.iwosw.vivariumlibera.block.AlchemyTableBlock;
import com.iwosw.vivariumlibera.block.entity.AlchemyLiquid;
import com.iwosw.vivariumlibera.block.entity.AlchemyTableBlockEntity;
import com.iwosw.vivariumlibera.menu.AlchemyTableMenu;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public final class AlchemyTableScreen extends AbstractContainerScreen<AlchemyTableMenu> {
    private static final int INK = 0xFF2A160C;
    private static final int PANEL = 0xFFEFE0BC;
    private static final int PANEL_EDGE = 0xFF6D5425;
    private static final int PANEL_SHADE = 0xFFD9C39A;
    private static final int SLOT_EDGE = 0xFF5C4326;
    private static final int SLOT_BG = 0xFFCDB48C;
    private static final int GAUGE_BG = 0xFF8C7250;
    private static final int EMBER = 0xFFFF9B32;
    private static final int STRONG_FLAME = 0xFF64C8FF;
    private static final int COLD_COAL = 0xFF55483E;

    private static final int FLAME_X = 18;
    private static final int FLAME_Y = 36;
    private static final int FLAME_SIZE = 14;
    private static final int GAUGE_X = 40;
    private static final int GAUGE_Y = 17;
    private static final int GAUGE_WIDTH = 10;
    private static final int GAUGE_HEIGHT = 52;
    private static final int ARROW_X = 60;
    private static final int ARROW_Y = 56;
    private static final int ARROW_WIDTH = 48;
    private static final int ARROW_HEIGHT = 10;

    public AlchemyTableScreen(AlchemyTableMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
        imageWidth = 176;
        imageHeight = 166;
        inventoryLabelY = imageHeight - 94;
    }

    @Override
    protected void init() {
        super.init();
        AlchemyButton bookButton = new AlchemyButton(
                leftPos + imageWidth - 62,
                topPos + 3,
                54,
                12,
                Component.translatable("gui.vivariumlibera.alchemy_table.open_book"),
                HerbalistBookScreen::open);
        bookButton.active = hasLecternWithBook();
        addRenderableWidget(bookButton);
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.render(guiGraphics, mouseX, mouseY, partialTick);
        renderTooltip(guiGraphics, mouseX, mouseY);
        renderIndicatorTooltips(guiGraphics, mouseX, mouseY);
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
        int left = leftPos;
        int top = topPos;

        guiGraphics.fill(left, top, left + imageWidth, top + imageHeight, PANEL_EDGE);
        guiGraphics.fill(left + 2, top + 2, left + imageWidth - 2, top + imageHeight - 2, PANEL);
        guiGraphics.fill(left + 2, top + imageHeight - 4, left + imageWidth - 2, top + imageHeight - 2, PANEL_SHADE);
        guiGraphics.fill(left + 2, top + inventoryLabelY - 2, left + imageWidth - 2, top + inventoryLabelY - 1, PANEL_SHADE);

        for (Slot slot : menu.slots) {
            drawSlot(guiGraphics, left + slot.x, top + slot.y);
        }

        renderFlame(guiGraphics, left, top);
        renderLiquidGauge(guiGraphics, left, top);
        renderProgressArrow(guiGraphics, left, top);
    }

    private void drawSlot(GuiGraphics guiGraphics, int x, int y) {
        guiGraphics.fill(x - 1, y - 1, x + 17, y + 17, SLOT_EDGE);
        guiGraphics.fill(x, y, x + 16, y + 16, SLOT_BG);
    }

    private void renderFlame(GuiGraphics guiGraphics, int left, int top) {
        int x = left + FLAME_X;
        int y = top + FLAME_Y;
        guiGraphics.fill(x, y + FLAME_SIZE - 2, x + FLAME_SIZE, y + FLAME_SIZE, COLD_COAL);
        if (!menu.isLit()) {
            return;
        }
        int height = Math.max(2, menu.getLitScaled(FLAME_SIZE));
        int color = menu.isStrongFire() ? STRONG_FLAME : EMBER;
        int width = Math.max(4, FLAME_SIZE - 2 * (FLAME_SIZE - height) / 3);
        int centerOffset = (FLAME_SIZE - width) / 2;
        guiGraphics.fill(x + centerOffset, y + FLAME_SIZE - height, x + centerOffset + width, y + FLAME_SIZE, color);
    }

    private void renderLiquidGauge(GuiGraphics guiGraphics, int left, int top) {
        int x = left + GAUGE_X;
        int y = top + GAUGE_Y;
        guiGraphics.fill(x - 1, y - 1, x + GAUGE_WIDTH + 1, y + GAUGE_HEIGHT + 1, SLOT_EDGE);
        guiGraphics.fill(x, y, x + GAUGE_WIDTH, y + GAUGE_HEIGHT, GAUGE_BG);
        int portions = menu.getLiquidPortions();
        if (menu.getLiquid() == AlchemyLiquid.NONE || portions <= 0) {
            return;
        }
        int height = Math.min(GAUGE_HEIGHT, portions * GAUGE_HEIGHT / AlchemyTableBlockEntity.PORTIONS_PER_JUG);
        guiGraphics.fill(x, y + GAUGE_HEIGHT - height, x + GAUGE_WIDTH, y + GAUGE_HEIGHT, liquidColor(menu.getLiquid()));
    }

    private void renderProgressArrow(GuiGraphics guiGraphics, int left, int top) {
        int x = left + ARROW_X;
        int y = top + ARROW_Y;
        guiGraphics.fill(x - 1, y - 1, x + ARROW_WIDTH + 1, y + ARROW_HEIGHT + 1, SLOT_EDGE);
        guiGraphics.fill(x, y, x + ARROW_WIDTH, y + ARROW_HEIGHT, GAUGE_BG);
        int progress = menu.getBrewScaled(ARROW_WIDTH);
        if (progress > 0) {
            guiGraphics.fill(x, y, x + progress, y + ARROW_HEIGHT, menu.isStrongFire() ? STRONG_FLAME : EMBER);
        }
    }

    private void renderIndicatorTooltips(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        if (isHovering(FLAME_X, FLAME_Y, FLAME_SIZE, FLAME_SIZE, mouseX, mouseY)) {
            Component text = menu.isStrongFire()
                    ? Component.translatable("gui.vivariumlibera.alchemy_table.fire_strong")
                    : Component.translatable(menu.isLit()
                            ? "gui.vivariumlibera.alchemy_table.fire_lit"
                            : "gui.vivariumlibera.alchemy_table.fire_unlit");
            guiGraphics.renderTooltip(font, text, mouseX, mouseY);
        } else if (isHovering(GAUGE_X, GAUGE_Y, GAUGE_WIDTH, GAUGE_HEIGHT, mouseX, mouseY)) {
            AlchemyLiquid liquid = menu.getLiquid();
            Component text = !menu.hasCauldron()
                    ? Component.translatable("gui.vivariumlibera.alchemy_table.no_cauldron")
                    : liquid == AlchemyLiquid.NONE
                    ? Component.translatable("gui.vivariumlibera.alchemy_table.liquid_empty")
                    : Component.translatable(
                            "gui.vivariumlibera.alchemy_table.liquid." + liquid.getSerializedName(),
                            menu.getLiquidPortions());
            guiGraphics.renderTooltip(font, text, mouseX, mouseY);
        }
    }

    private static int liquidColor(AlchemyLiquid liquid) {
        return switch (liquid) {
            case WATER -> 0xFF3F76E4;
            case OIL -> 0xFFC9A038;
            case WINE -> 0xFF7B2434;
            case NONE -> GAUGE_BG;
        };
    }

    private boolean hasLecternWithBook() {
        if (minecraft == null || minecraft.level == null) {
            return false;
        }
        BlockState state = minecraft.level.getBlockState(menu.getTablePos());
        boolean hasLectern = state.hasProperty(AlchemyTableBlock.HAS_LECTERN)
                && state.getValue(AlchemyTableBlock.HAS_LECTERN);
        return hasLectern
                && minecraft.level.getBlockEntity(menu.getTablePos()) instanceof AlchemyTableBlockEntity table
                && table.hasBook();
    }

    private final class AlchemyButton extends AbstractWidget {
        private final Runnable action;

        private AlchemyButton(int x, int y, int width, int height, Component message, Runnable action) {
            super(x, y, width, height, message);
            this.action = action;
        }

        @Override
        public void onClick(double mouseX, double mouseY) {
            if (active) {
                action.run();
            }
        }

        @Override
        protected void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
            int dark = active ? 0xFF6D5425 : 0xFF8B806C;
            int wood = active ? (isHovered ? 0xFFD5B85C : 0xFFA6A764) : 0xFFB0A88F;
            int text = active ? 0xFF2B1A0B : 0xFF756A58;
            guiGraphics.fill(getX(), getY(), getX() + getWidth(), getY() + getHeight(), dark);
            guiGraphics.fill(getX() + 1, getY() + 1, getX() + getWidth() - 1, getY() + getHeight() - 1, wood);
            guiGraphics.drawString(font, getMessage(),
                    getX() + getWidth() / 2 - font.width(getMessage()) / 2,
                    getY() + (getHeight() - 8) / 2 + 1,
                    text,
                    false);
        }

        @Override
        protected void updateWidgetNarration(NarrationElementOutput narrationElementOutput) {
            defaultButtonNarrationText(narrationElementOutput);
        }
    }
}
