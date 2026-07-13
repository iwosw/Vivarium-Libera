package com.iwosw.vivariumlibera.client;

import com.iwosw.vivariumlibera.VivariumLibera;
import com.iwosw.vivariumlibera.block.AlchemyTableBlock;
import com.iwosw.vivariumlibera.block.entity.AlchemyTableBlockEntity;
import com.iwosw.vivariumlibera.registry.ModItems;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public final class AlchemyTableScreen extends Screen {
    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(
            VivariumLibera.MOD_ID, "textures/gui/alchemy_table.png");
    private static final int PANEL_SIZE = 220;
    private static final int INK = 0xFF2A160C;
    private static final int MUTED_INK = 0xFF684426;
    private static final int EMBER = 0xFFFF9B32;
    private static final int COLD_COAL = 0xFF55483E;
    private final BlockPos tablePos;

    private AlchemyTableScreen(BlockPos tablePos) {
        super(Component.translatable("gui.vivariumlibera.alchemy_table.title"));
        this.tablePos = tablePos.immutable();
    }

    public static void open(BlockPos tablePos) {
        Minecraft.getInstance().setScreen(new AlchemyTableScreen(tablePos));
    }

    @Override
    protected void init() {
        int left = (width - PANEL_SIZE) / 2;
        int top = (height - PANEL_SIZE) / 2;
        AlchemyButton bookButton = new AlchemyButton(
                left + 34,
                top + 110,
                86,
                15,
                Component.translatable("gui.vivariumlibera.alchemy_table.open_book"),
                HerbalistBookScreen::open);
        bookButton.active = hasLectern() && hasBook();
        addRenderableWidget(bookButton);
        addRenderableWidget(new AlchemyButton(
                left + 34,
                top + 128,
                86,
                14,
                Component.translatable("gui.vivariumlibera.alchemy_table.close"),
                this::onClose));
    }

    @Override
    public void renderBackground(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        guiGraphics.fill(0, 0, width, height, 0x55080503);
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        renderBackground(guiGraphics, mouseX, mouseY, partialTick);
        int left = (width - PANEL_SIZE) / 2;
        int top = (height - PANEL_SIZE) / 2;
        guiGraphics.blit(TEXTURE, left, top, 0, 0, PANEL_SIZE, PANEL_SIZE, PANEL_SIZE, PANEL_SIZE);

        int infoCenter = left + 77;
        drawCentered(guiGraphics, title, infoCenter, top + 59, INK);
        boolean lit = isLit();
        Component fireStatus = Component.translatable(lit
                ? "gui.vivariumlibera.alchemy_table.fire_lit"
                : "gui.vivariumlibera.alchemy_table.fire_unlit");
        drawCentered(guiGraphics, fireStatus, infoCenter, top + 73, lit ? EMBER : COLD_COAL);

        Component hint = Component.translatable(!hasLectern()
                ? "gui.vivariumlibera.alchemy_table.hint_no_lectern"
                : lit
                        ? "gui.vivariumlibera.alchemy_table.hint_lit"
                        : "gui.vivariumlibera.alchemy_table.hint_unlit");
        int lineY = top + 85;
        for (var line : font.split(hint, 82)) {
            drawCentered(guiGraphics, line, infoCenter, lineY, MUTED_INK);
            lineY += 10;
        }

        renderSelectedJug(guiGraphics, left, top);
        renderActionGuide(guiGraphics, left, top);

        super.render(guiGraphics, mouseX, mouseY, partialTick);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    private boolean hasBook() {
        return minecraft != null
                && minecraft.level != null
                && minecraft.level.getBlockEntity(tablePos) instanceof AlchemyTableBlockEntity table
                && table.hasBook();
    }

    private boolean hasLectern() {
        if (minecraft == null || minecraft.level == null) {
            return false;
        }
        BlockState state = minecraft.level.getBlockState(tablePos);
        return state.hasProperty(AlchemyTableBlock.HAS_LECTERN)
                && state.getValue(AlchemyTableBlock.HAS_LECTERN);
    }

    private ItemStack selectedJug() {
        if (minecraft != null
                && minecraft.level != null
                && minecraft.level.getBlockEntity(tablePos) instanceof AlchemyTableBlockEntity table) {
            return table.getStoredJug();
        }
        return ItemStack.EMPTY;
    }

    private void renderSelectedJug(GuiGraphics guiGraphics, int left, int top) {
        ItemStack jug = selectedJug();
        int jugCenterX = left + 157;
        drawCentered(guiGraphics,
                Component.translatable("gui.vivariumlibera.alchemy_table.selected_jug"),
                jugCenterX,
                top + 62,
                INK);
        if (jug.isEmpty()) {
            drawCentered(guiGraphics,
                    Component.translatable("gui.vivariumlibera.alchemy_table.no_jug"),
                    jugCenterX,
                    top + 91,
                    MUTED_INK);
            return;
        }

        guiGraphics.pose().pushPose();
        guiGraphics.pose().translate(left + 137, top + 72, 100.0F);
        guiGraphics.pose().scale(2.5F, 2.5F, 1.0F);
        guiGraphics.renderItem(jug, 0, 0);
        guiGraphics.pose().popPose();
        for (var line : font.split(jug.getHoverName(), 78)) {
            drawCentered(guiGraphics, line, jugCenterX, top + 119, INK);
            break;
        }
    }

    private void renderActionGuide(GuiGraphics guiGraphics, int left, int top) {
        renderGuideRow(guiGraphics, new ItemStack(Items.FLINT_AND_STEEL),
                Component.translatable("gui.vivariumlibera.alchemy_table.guide_ignite"), left + 32, top + 158);
        renderGuideRow(guiGraphics, new ItemStack(ModItems.JUG.get()),
                Component.translatable("gui.vivariumlibera.alchemy_table.guide_jug"), left + 32, top + 176);
        renderGuideRow(guiGraphics, new ItemStack(Items.LECTERN),
                Component.translatable("gui.vivariumlibera.alchemy_table.guide_lectern"), left + 32, top + 194);
    }

    private void renderGuideRow(GuiGraphics guiGraphics, ItemStack icon, Component text, int x, int y) {
        guiGraphics.renderItem(icon, x, y - 3);
        guiGraphics.fill(x + 19, y - 1, x + 153, y + 10, 0x99FFF0C8);
        guiGraphics.drawString(font, text, x + 23, y + 1, INK, false);
    }

    private void drawCentered(GuiGraphics guiGraphics, Component text, int centerX, int y, int color) {
        guiGraphics.drawString(font, text, centerX - font.width(text) / 2, y, color, false);
    }

    private void drawCentered(GuiGraphics guiGraphics, net.minecraft.util.FormattedCharSequence text, int centerX, int y, int color) {
        guiGraphics.drawString(font, text, centerX - font.width(text) / 2, y, color, false);
    }

    private boolean isLit() {
        if (minecraft == null || minecraft.level == null) {
            return false;
        }
        BlockState state = minecraft.level.getBlockState(tablePos);
        return state.hasProperty(AlchemyTableBlock.LIT) && state.getValue(AlchemyTableBlock.LIT);
    }

    private static final class AlchemyButton extends AbstractWidget {
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
            int brass = active ? (isHovered ? 0xFFFFDC78 : 0xFFE0B94F) : 0xFFB6A77E;
            int text = active ? 0xFF2B1A0B : 0xFF756A58;
            guiGraphics.fill(getX(), getY(), getX() + getWidth(), getY() + getHeight(), dark);
            guiGraphics.fill(getX() + 2, getY() + 2, getX() + getWidth() - 2, getY() + getHeight() - 2, wood);
            guiGraphics.hLine(getX() + 3, getX() + getWidth() - 4, getY() + 2, brass);
            guiGraphics.vLine(getX() + 2, getY() + 3, getY() + getHeight() - 4, brass);
            guiGraphics.fill(getX() + 3, getY() + 3, getX() + 5, getY() + 5, brass);
            guiGraphics.fill(getX() + getWidth() - 5, getY() + 3, getX() + getWidth() - 3, getY() + 5, brass);
            var font = Minecraft.getInstance().font;
            guiGraphics.drawString(font, getMessage(),
                    getX() + getWidth() / 2 - font.width(getMessage()) / 2,
                    getY() + (getHeight() - 8) / 2,
                    text,
                    false);
        }

        @Override
        protected void updateWidgetNarration(NarrationElementOutput narrationElementOutput) {
            defaultButtonNarrationText(narrationElementOutput);
        }
    }
}
