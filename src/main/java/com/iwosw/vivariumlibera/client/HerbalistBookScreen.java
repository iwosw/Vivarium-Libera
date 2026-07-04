package com.iwosw.vivariumlibera.client;

import com.iwosw.vivariumlibera.VivariumLibera;
import com.iwosw.vivariumlibera.registry.ModItems;
import java.util.List;
import java.util.function.Supplier;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public final class HerbalistBookScreen extends Screen {
    private static final int BACKDROP = 0x9917130F;
    private static final int COVER = 0xFF6B4E37;       // Warmer, lighter leather
    private static final int COVER_DARK = 0xFF3D2B1F;  // Rich dark leather
    private static final int PAGE = 0xFFFFE9C0;      // Warm yellow parchment
    private static final int PAGE_DARK = 0xFFF0D5A3; // Aged yellow parchment for cards/blocks
    private static final int PAGE_SHADOW = 0xFFB89561;
    private static final int INK = 0xFF110A05;       // Darker soot black ink
    private static final int MUTED = 0xFF4A3421;     // Darker sepia brown
    private static final int GOLD = 0xFFD2B26A;      // Brighter antique gold
    private static final int GOLD_DARK = 0xFF836235; // Deeper bronze gold
    private static final int GREEN = 0xFF405F32;
    private static final int AMBER = 0xFF8E6129;
    private static final int BLUE = 0xFF43566A;
    private static final int RED = 0xFF71332A;
    private static final int VELLUM_LIGHT = 0xFFFFF1D6; // Lighter cream for highlight
    private static final int RUBY = 0xFF7A2C25;
    private static final int SUCCESS = GREEN;
    private static final int WARN = AMBER;
    private static final int ACCENT = BLUE;
    private static final int DANGER = RED;
    private static final int LOGO_TEXTURE_SIZE = 32;
    private static final ResourceLocation LOGO_TEXTURE = ResourceLocation.fromNamespaceAndPath(VivariumLibera.MOD_ID, "textures/gui/logo.png");
    private static final List<Spread> SPREADS = List.of(
            new Spread(
                    "Книга травника",
                    "Vivarium Libera",
                    "Рукописный гербарий для путников, лекарей и хранителей живого мира.",
                    "Смотри на форму листа, место роста и цвет: так луг превращается в карту.",
                    List.of(
                            entry("Полынь", "Artemisia", "Сухой серебристый лист, горечь и дым.", () -> new ItemStack(ModItems.WORMWOOD_ITEM.get()), SUCCESS),
                            entry("Календула", "Calendula", "Теплый сад лекаря, мягкий оранжевый свет.", () -> new ItemStack(ModItems.CALENDULA_ITEM.get()), WARN)
                    )
            ),
            new Spread(
                    "Сухие и лесные",
                    "Глава I",
                    "Травы опушек, троп и каменистых краев.",
                    "Их легче читать по силуэту: сухой лист, высокий стебель, дорожный цветок.",
                    List.of(
                            entry("Полынь", "Artemisia", "Серебристая горечь сухих мест.", () -> new ItemStack(ModItems.WORMWOOD_ITEM.get()), SUCCESS),
                            entry("Крапива", "Urtica", "Жгучий знак влажной плодородной земли.", () -> new ItemStack(ModItems.NETTLE_ITEM.get()), SUCCESS),
                            entry("Зверобой", "Hypericum", "Желтый свет просек и безопасных полян.", () -> new ItemStack(ModItems.ST_JOHNS_WORT_ITEM.get()), WARN),
                            entry("Цикорий", "Cichorium", "Синий дорожный цветок у троп.", () -> new ItemStack(ModItems.CHICORY_ITEM.get()), ACCENT)
                    )
            ),
            new Spread(
                    "Берег и болото",
                    "Глава II",
                    "Растения воды должны вести игрока к ручьям, низинам и мелководью.",
                    "Если трава выглядит холодной и плотной, рядом должна чувствоваться вода.",
                    List.of(
                            entry("Аир", "Sweet Flag", "Мечевидные листья у тихой воды.", () -> new ItemStack(ModItems.CALAMUS_ITEM.get()), ACCENT),
                            entry("Рогоз", "Cattail", "Высокий страж стоячей воды.", () -> new ItemStack(ModItems.CATTAIL_ITEM.get()), ACCENT),
                            entry("Мята", "Mentha", "Свежая влажная трава берега.", () -> new ItemStack(ModItems.MINT_ITEM.get()), SUCCESS),
                            entry("Окопник", "Comfrey", "Темная плодородная низина.", () -> new ItemStack(ModItems.COMFREY_ITEM.get()), SUCCESS)
                    )
            ),
            new Spread(
                    "Сад лекаря",
                    "Глава III",
                    "Теплые, спокойные и аккуратные растения для двора знахаря.",
                    "Они должны выглядеть полезными, собранными и почти домашними.",
                    List.of(
                            entry("Календула", "Calendula", "Оранжевый свет грядки.", () -> new ItemStack(ModItems.CALENDULA_ITEM.get()), WARN),
                            entry("Шалфей", "Sage", "Сухой строгий кустик у камня.", () -> new ItemStack(ModItems.SAGE_ITEM.get()), SUCCESS),
                            entry("Очанка", "Eyebright", "Малый цветок внимательного глаза.", () -> new ItemStack(ModItems.EYEBRIGHT_ITEM.get()), WARN),
                            entry("Валериана", "Valeriana", "Тихий цвет сна и вечернего воздуха.", () -> new ItemStack(ModItems.VALERIAN_PINK_ITEM.get()), ACCENT)
                    )
            ),
            new Spread(
                    "Ядовитый лист",
                    "Глава IV",
                    "Опасные травы должны быть красивыми, но тревожными.",
                    "Темные ягоды и тяжелые цветы не обязаны кричать, но должны настораживать.",
                    List.of(
                            entry("Белена", "Hyoscyamus", "Тусклая трава дурного сна.", () -> new ItemStack(ModItems.HENBANE_ITEM.get()), DANGER),
                            entry("Дурман", "Datura", "Крупное колдовское растение.", () -> new ItemStack(ModItems.DATURA_ITEM.get()), DANGER),
                            entry("Вороний глаз", "Crow's Eye", "Темная ягода среди листьев.", () -> new ItemStack(ModItems.CROWS_EYE_ITEM.get()), DANGER),
                            entry("Белладонна", "Belladonna", "Ягоды ведьминой алхимии.", () -> new ItemStack(ModItems.BELLADONNA_ITEM.get()), DANGER)
                    )
            )
    );

    private int spread;
    private Entry selectedEntry;
    private MonasticButton backButton;
    private MonasticButton nextButton;

    private HerbalistBookScreen() {
        this(0);
    }

    private HerbalistBookScreen(int spread) {
        this(spread, null);
    }

    private HerbalistBookScreen(int spread, Entry selectedEntry) {
        super(Component.literal("Книга травника"));
        this.spread = spread;
        this.selectedEntry = selectedEntry;
    }

    public static void open() {
        Minecraft.getInstance().setScreen(new HerbalistBookScreen());
    }

    @Override
    protected void init() {
        int bookWidth = bookWidth();
        int bookHeight = bookHeight();
        int left = (this.width - bookWidth) / 2;
        int top = (this.height - bookHeight) / 2;
        if (this.selectedEntry != null) {
            this.addRenderableWidget(new MonasticButton(left + 38, top + bookHeight - 31, 82, 22, Component.literal("Назад"), () -> Minecraft.getInstance().setScreen(new HerbalistBookScreen(this.spread))));
            this.addRenderableWidget(new MonasticButton(left + bookWidth - 120, top + bookHeight - 31, 82, 22, Component.literal("Закрыть"), this::onClose));
            return;
        }
        this.backButton = this.addRenderableWidget(new MonasticButton(left + 38, top + bookHeight - 31, 36, 22, Component.literal("<"), () -> move(-1)));
        this.nextButton = this.addRenderableWidget(new MonasticButton(left + bookWidth - 74, top + bookHeight - 31, 36, 22, Component.literal(">"), () -> move(1)));
        this.addRenderableWidget(new MonasticButton(this.width / 2 - 50, top + bookHeight - 31, 100, 22, Component.literal("Закрыть"), this::onClose));
        updateButtons();
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (this.selectedEntry != null && (keyCode == 256 || keyCode == 259)) {
            Minecraft.getInstance().setScreen(new HerbalistBookScreen(this.spread));
            return true;
        }
        if (keyCode == 266 || keyCode == 263) {
            move(-1);
            return true;
        }
        if (keyCode == 267 || keyCode == 262) {
            move(1);
            return true;
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public void renderBackground(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        // Do nothing to prevent default background rendering from drawing on top of the book
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        renderBackdrop(guiGraphics); // Draw backdrop first, behind the book
        Spread current = SPREADS.get(this.spread);
        int bookWidth = bookWidth();
        int bookHeight = bookHeight();
        int left = (this.width - bookWidth) / 2;
        int top = (this.height - bookHeight) / 2;
        int pageWidth = (bookWidth - 28) / 2;
        renderBook(guiGraphics, left, top, bookWidth, bookHeight, pageWidth);
        if (this.selectedEntry != null) {
            renderDetailLeft(guiGraphics, left + 34, top + 35, pageWidth - 58, bookHeight - 86);
            renderDetailRight(guiGraphics, left + pageWidth + 41, top + 35, pageWidth - 66, bookHeight - 86);
        } else {
            renderLeftPage(guiGraphics, current, left + 28, top + 28, pageWidth - 42, bookHeight - 72);
            renderPlantCards(guiGraphics, current.entries(), left + pageWidth + 32, top + 30, pageWidth - 50, mouseX, mouseY);
            drawCentered(guiGraphics, (this.spread + 1) + " / " + SPREADS.size(), left + bookWidth / 2, top + bookHeight - 21, GOLD_DARK);
        }
        super.render(guiGraphics, mouseX, mouseY, partialTick); // Draws buttons on top of the book
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (button == 0 && this.selectedEntry == null) {
            Entry entry = entryAt(mouseX, mouseY);
            if (entry != null) {
                Minecraft.getInstance().setScreen(new HerbalistBookScreen(this.spread, entry));
                return true;
            }
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    private void renderBackdrop(GuiGraphics guiGraphics) {
        guiGraphics.fill(0, 0, this.width, this.height, BACKDROP);
        guiGraphics.fill(0, 0, this.width, 10, 0x33130C07);
        guiGraphics.fill(0, this.height - 10, this.width, this.height, 0x33130C07);
    }

    private void renderBook(GuiGraphics guiGraphics, int left, int top, int bookWidth, int bookHeight, int pageWidth) {
        guiGraphics.fill(left + 8, top + 10, left + bookWidth + 10, top + bookHeight + 10, 0x44000000);
        guiGraphics.fill(left, top, left + bookWidth, top + bookHeight, COVER_DARK);
        guiGraphics.fill(left + 4, top + 4, left + bookWidth - 4, top + bookHeight - 4, COVER);
        renderPanelBorder(guiGraphics, left + 3, top + 3, bookWidth - 6, bookHeight - 6, GOLD_DARK, GOLD);
        renderPanelBorder(guiGraphics, left + 10, top + 10, bookWidth - 20, bookHeight - 20, COVER_DARK, GOLD_DARK);

        // Render Cover Metal Corners & Clasps
        renderCoverCorners(guiGraphics, left, top, bookWidth, bookHeight);
        renderCoverClasps(guiGraphics, left, top, bookWidth, bookHeight);

        int leftPageX = left + 22;
        int rightPageX = left + pageWidth + 27;
        int pageY = top + 18;
        int pageHeight = bookHeight - 54;
        renderPage(guiGraphics, leftPageX, pageY, pageWidth - 16, pageHeight, true);
        renderPage(guiGraphics, rightPageX, pageY, pageWidth - 22, pageHeight, false);

        int spineX = left + pageWidth + 11;
        guiGraphics.fill(spineX, top + 14, spineX + 15, top + bookHeight - 14, COVER_DARK);
        guiGraphics.fill(spineX + 4, top + 17, spineX + 8, top + bookHeight - 17, GOLD_DARK);
        guiGraphics.fill(spineX + 8, top + 17, spineX + 11, top + bookHeight - 17, 0x4423110A);

        // Render Red Silk Ribbon Bookmark
        int rx = left + pageWidth + 15;
        int ry = top + bookHeight - 10;
        int rh = 24;
        guiGraphics.fill(rx, top + 14, rx + 7, ry + rh, 0xFF9E2A2B);
        guiGraphics.fill(rx, top + 14, rx + 1, ry + rh, 0xFFBA3F3F);
        guiGraphics.fill(rx + 6, top + 14, rx + 7, ry + rh, 0xFF7D1B1C);
        int bottomY = ry + rh;
        guiGraphics.hLine(rx + 3, rx + 3, bottomY - 4, BACKDROP);
        guiGraphics.hLine(rx + 2, rx + 4, bottomY - 3, BACKDROP);
        guiGraphics.hLine(rx + 1, rx + 5, bottomY - 2, BACKDROP);
        guiGraphics.hLine(rx, rx + 6, bottomY - 1, BACKDROP);

        renderAlchemySeal(guiGraphics, left + 42, top + 39, 0x1A836235);
        renderAlchemySeal(guiGraphics, left + bookWidth - 60, top + 39, 0x1A836235);
        renderCornerStuds(guiGraphics, left, top, bookWidth, bookHeight);
    }

    private void renderPage(GuiGraphics guiGraphics, int x, int y, int width, int height, boolean leftPage) {
        guiGraphics.fill(x + (leftPage ? 2 : 0), y + 3, x + width + (leftPage ? 2 : 3), y + height + 4, PAGE_SHADOW);
        guiGraphics.fill(x, y, x + width, y + height, VELLUM_LIGHT);
        guiGraphics.fill(x + 5, y + 5, x + width - 5, y + height - 5, PAGE);
        renderPageTexture(guiGraphics, x, y, width, height);
        renderPanelBorder(guiGraphics, x, y, width, height, PAGE_SHADOW, GOLD_DARK);
        
        // Spine Curl Shadow
        int shadowWidth = 14;
        if (leftPage) {
            int rightEdge = x + width - 1;
            for (int i = 0; i < shadowWidth; i++) {
                int alpha = (int) (0x24 * ((float) (shadowWidth - i) / shadowWidth));
                int color = (alpha << 24) | 0x110A05;
                guiGraphics.fill(rightEdge - i, y + 1, rightEdge - i + 1, y + height - 1, color);
            }
        } else {
            int leftEdge = x;
            for (int i = 0; i < shadowWidth; i++) {
                int alpha = (int) (0x24 * ((float) (shadowWidth - i) / shadowWidth));
                int color = (alpha << 24) | 0x110A05;
                guiGraphics.fill(leftEdge + i, y + 1, leftEdge + i + 1, y + height - 1, color);
            }
        }

        guiGraphics.hLine(x + 13, x + width - 14, y + 18, GOLD_DARK);
        guiGraphics.hLine(x + 13, x + width - 14, y + height - 18, GOLD_DARK);
    }

    private void renderPageTexture(GuiGraphics guiGraphics, int x, int y, int width, int height) {
        guiGraphics.fill(x + 4, y + 4, x + width - 4, y + 8, 0x10FFFFFF);
        guiGraphics.fill(x + 4, y + height - 8, x + width - 4, y + height - 4, 0x0E7B542C);
        guiGraphics.fill(x + 4, y + 8, x + 8, y + height - 8, 0x0E7B542C);
        guiGraphics.fill(x + 6, y + 6, x + 18, y + 18, 0x22FFFFFF);
        guiGraphics.fill(x + width - 20, y + height - 20, x + width - 7, y + height - 7, 0x127B542C);
        
        // Age mottling spots (fixed positions to prevent flickering)
        drawMottle(guiGraphics, x + width / 4, y + height / 5, 6, 4, 0x095E4127);
        drawMottle(guiGraphics, x + width * 3 / 4, y + height * 2 / 3, 5, 5, 0x075E4127);
        drawMottle(guiGraphics, x + width / 5, y + height * 3 / 4, 8, 3, 0x085E4127);
        drawMottle(guiGraphics, x + width * 2 / 3, y + height / 6, 4, 6, 0x0A5E4127);
    }

    private void drawMottle(GuiGraphics guiGraphics, int mx, int my, int mw, int mh, int color) {
        guiGraphics.fill(mx, my, mx + mw, my + mh, color);
        guiGraphics.fill(mx + 1, my - 1, mx + mw - 1, my, color);
        guiGraphics.fill(mx + 1, my + mh, mx + mw - 1, my + mh + 1, color);
        guiGraphics.fill(mx - 1, my + 1, mx, my + mh - 1, color);
        guiGraphics.fill(mx + mw, my + 1, mx + mw + 1, my + mh - 1, color);
    }

    private void renderCornerStuds(GuiGraphics guiGraphics, int x, int y, int width, int height) {
        int[][] studs = {{18, 18}, {width - 24, 18}, {18, height - 24}, {width - 24, height - 24}};
        for (int[] stud : studs) {
            guiGraphics.fill(x + stud[0], y + stud[1], x + stud[0] + 6, y + stud[1] + 6, GOLD_DARK);
            guiGraphics.fill(x + stud[0] + 1, y + stud[1] + 1, x + stud[0] + 5, y + stud[1] + 5, GOLD);
        }
    }

    private void renderPanelBorder(GuiGraphics guiGraphics, int x, int y, int width, int height, int dark, int bright) {
        guiGraphics.hLine(x, x + width - 1, y, dark);
        guiGraphics.hLine(x, x + width - 1, y + height - 1, dark);
        guiGraphics.vLine(x, y, y + height - 1, dark);
        guiGraphics.vLine(x + width - 1, y, y + height - 1, dark);
        guiGraphics.fill(x, y, x + 4, y + 4, bright);
        guiGraphics.fill(x + width - 4, y, x + width, y + 4, bright);
        guiGraphics.fill(x, y + height - 4, x + 4, y + height, bright);
        guiGraphics.fill(x + width - 4, y + height - 4, x + width, y + height, bright);
    }

    private void renderAlchemySeal(GuiGraphics guiGraphics, int x, int y, int color) {
        guiGraphics.hLine(x + 5, x + 25, y + 15, color);
        guiGraphics.vLine(x + 15, y + 5, y + 25, color);
        guiGraphics.hLine(x + 9, x + 21, y + 8, color);
        guiGraphics.hLine(x + 9, x + 21, y + 22, color);
        guiGraphics.vLine(x + 8, y + 9, y + 21, color);
        guiGraphics.vLine(x + 22, y + 9, y + 21, color);
        guiGraphics.fill(x + 13, y + 13, x + 18, y + 18, color);
    }

    private void renderLeftPage(GuiGraphics guiGraphics, Spread current, int x, int y, int width, int height) {
        drawCentered(guiGraphics, current.kicker(), x + width / 2, y, GOLD_DARK);
        drawCentered(guiGraphics, current.title(), x + width / 2, y + 14, INK);
        guiGraphics.hLine(x + 17, x + width - 17, y + 31, GOLD_DARK);
        guiGraphics.hLine(x + 31, x + width - 31, y + 34, PAGE_SHADOW);
        
        int contentY = y + 46;
        if (this.spread == 0) {
            renderBookLogo(guiGraphics, x + width / 2, y + 42, 48);
            contentY = y + 98;
        }

        int introHeight = drawTextWithDropCap(guiGraphics, current.intro(), x + 11, contentY, width - 22, INK);
        int noteY = contentY + introHeight + 8;
        
        // Draw note with 11px line spacing
        List<FormattedCharSequence> noteLines = this.font.split(Component.literal(current.note()), width - 22);
        int maxNoteLines = 5;
        int noteCount = Math.min(noteLines.size(), maxNoteLines);
        for (int i = 0; i < noteCount; i++) {
            guiGraphics.drawString(this.font, noteLines.get(i), x + 11, noteY + i * 11, MUTED, false);
        }
        
        int panelY = y + height - 70;
        guiGraphics.fill(x + 16, panelY, x + width - 16, panelY + 54, 0x33836235);
        renderPanelBorder(guiGraphics, x + 16, panelY, width - 32, 54, GOLD_DARK, PAGE_SHADOW);
        drawCentered(guiGraphics, "ORA ET HERBA", x + width / 2, panelY + 9, INK);
        drawCentered(guiGraphics, "лист, место, польза", x + width / 2, panelY + 23, MUTED);
        drawCentered(guiGraphics, "кликни по растению", x + width / 2, panelY + 37, GOLD_DARK);
    }

    private void renderBookLogo(GuiGraphics guiGraphics, int centerX, int y, int size) {
        int x = centerX - size / 2;
        guiGraphics.fill(x - 5, y - 5, x + size + 5, y + size + 5, 0x663A2117);
        renderPanelBorder(guiGraphics, x - 6, y - 6, size + 12, size + 12, GOLD_DARK, GOLD);
        guiGraphics.blit(LOGO_TEXTURE, x, y, size, size, 0.0F, 0.0F, LOGO_TEXTURE_SIZE, LOGO_TEXTURE_SIZE, LOGO_TEXTURE_SIZE, LOGO_TEXTURE_SIZE);
    }

    private void renderPlantCards(GuiGraphics guiGraphics, List<Entry> entries, int x, int y, int width, int mouseX, int mouseY) {
        int cardHeight = cardHeight();
        for (int i = 0; i < entries.size(); i++) {
            Entry entry = entries.get(i);
            int cardY = y + i * (cardHeight + cardGap());
            boolean hovered = mouseX >= x && mouseX < x + width && mouseY >= cardY && mouseY < cardY + cardHeight;
            
            if (hovered) {
                // Outer glowing gold border
                renderPanelBorder(guiGraphics, x - 1, cardY - 1, width + 2, cardHeight + 2, 0x44D2B26A, 0x44D2B26A);
            }
            
            guiGraphics.fill(x + 2, cardY + 2, x + width + 2, cardY + cardHeight + 2, 0x33000000);
            guiGraphics.fill(x, cardY, x + width, cardY + cardHeight, hovered ? VELLUM_LIGHT : PAGE_DARK);
            renderPanelBorder(guiGraphics, x, cardY, width, cardHeight, hovered ? GOLD : GOLD_DARK, entry.accent());
            guiGraphics.fill(x + 3, cardY + 3, x + 7, cardY + cardHeight - 3, entry.accent());
            guiGraphics.fill(x + 13, cardY + 9, x + 45, cardY + 41, 0x663A2117);
            renderPanelBorder(guiGraphics, x + 12, cardY + 8, 35, 35, GOLD_DARK, PAGE_SHADOW);
            
            float bobOffset = 0.0F;
            if (hovered) {
                bobOffset = (float) Math.sin(System.currentTimeMillis() * 0.005) * 1.2F;
            }
            renderIcon(guiGraphics, entry.icon().get(), x + 17, (int) (cardY + 13 + bobOffset), 1.55F);
            
            guiGraphics.drawString(this.font, entry.name(), x + 56, cardY + 7, INK, false);
            guiGraphics.drawString(this.font, entry.latin(), x + 56, cardY + 18, MUTED, false);
            
            // Draw card description using 11px line spacing
            List<FormattedCharSequence> descLines = this.font.split(Component.literal(entry.text()), width - 62);
            int descCount = Math.min(descLines.size(), 2);
            for (int j = 0; j < descCount; j++) {
                guiGraphics.drawString(this.font, descLines.get(j), x + 56, cardY + 31 + j * 11, INK, false);
            }
            
            guiGraphics.drawString(this.font, "§oЧитать запись...", x + width - 82, cardY + cardHeight - 13, hovered ? RUBY : GOLD_DARK, false);
        }
    }

    private Entry entryAt(double mouseX, double mouseY) {
        int bookWidth = bookWidth();
        int bookHeight = bookHeight();
        int left = (this.width - bookWidth) / 2;
        int top = (this.height - bookHeight) / 2;
        int pageWidth = (bookWidth - 28) / 2;
        int x = left + pageWidth + 32;
        int y = top + 30;
        int width = pageWidth - 50;
        List<Entry> entries = SPREADS.get(this.spread).entries();
        for (int i = 0; i < entries.size(); i++) {
            int cardY = y + i * (cardHeight() + cardGap());
            if (mouseX >= x && mouseX < x + width && mouseY >= cardY && mouseY < cardY + cardHeight()) {
                return entries.get(i);
            }
        }
        return null;
    }

    private int cardHeight() {
        return 58;
    }

    private int cardGap() {
        return 9;
    }

    private void renderDetailLeft(GuiGraphics guiGraphics, int x, int y, int width, int height) {
        Entry entry = this.selectedEntry;
        drawCentered(guiGraphics, "SCRIPTORIUM HERBARIA", x + width / 2, y, GOLD_DARK);
        drawCentered(guiGraphics, entry.name(), x + width / 2, y + 16, INK);
        drawCentered(guiGraphics, entry.latin(), x + width / 2, y + 29, MUTED);
        
        int fx = x + width / 2 - 31;
        int fy = y + 50;
        int fsize = 62;
        guiGraphics.fill(fx + 1, fy + 1, fx + fsize - 1, fy + fsize - 1, 0x663A2117);
        renderPanelBorder(guiGraphics, fx, fy, fsize, fsize, GOLD_DARK, entry.accent());
        
        // Ornamental corners on the large frame
        guiGraphics.fill(fx - 2, fy - 2, fx, fy, GOLD);
        guiGraphics.fill(fx + fsize, fy - 2, fx + fsize + 2, fy, GOLD);
        guiGraphics.fill(fx - 2, fy + fsize, fx, fy + fsize + 2, GOLD);
        guiGraphics.fill(fx + fsize, fy + fsize, fx + fsize + 2, fy + fsize + 2, GOLD);
        
        float bobOffset = (float) Math.sin(System.currentTimeMillis() * 0.004) * 2.0F;
        renderIcon(guiGraphics, entry.icon().get(), fx + 15, (int) (fy + 15 + bobOffset), 2.0F);
        
        guiGraphics.hLine(x + 24, x + width - 24, y + 136, GOLD_DARK);
        guiGraphics.hLine(x + 48, x + width - 48, y + 139, PAGE_SHADOW);
        drawWrapped(guiGraphics, "Внешний знак: " + entry.text(), x + 12, y + 156, width - 24, INK, 5);
        drawWrapped(guiGraphics, "Запись переписана в полевой манере: сверяй форму, цвет и место роста перед сбором.", x + 12, y + 214, width - 24, MUTED, 5);
    }

    private void renderDetailRight(GuiGraphics guiGraphics, int x, int y, int width, int height) {
        drawCentered(guiGraphics, "Заметки переписчика", x + width / 2, y, GOLD_DARK);
        renderDetailBlock(guiGraphics, x, y + 28, width, "Место роста", habitat());
        renderDetailBlock(guiGraphics, x, y + 92, width, "Сбор", harvest());
        renderDetailBlock(guiGraphics, x, y + 156, width, "Полезность", usefulness());
        renderDetailBlock(guiGraphics, x, y + 220, width, "Осторожность", caution());
    }

    private void renderDetailBlock(GuiGraphics guiGraphics, int x, int y, int width, String title, String text) {
        guiGraphics.fill(x, y, x + width, y + 54, PAGE_DARK);
        renderPanelBorder(guiGraphics, x, y, width, 54, GOLD_DARK, PAGE_SHADOW);
        guiGraphics.drawString(this.font, "§6✦ §r" + title, x + 10, y + 7, RUBY, false);
        drawWrapped(guiGraphics, text, x + 10, y + 21, width - 20, INK, 3);
    }

    private String habitat() {
        Spread spread = SPREADS.get(this.spread);
        return spread.intro() + " " + spread.note();
    }

    private String harvest() {
        return "Срезай аккуратно ножом или собирай рукой, если нужна целая травяная запись для гербария.";
    }

    private String usefulness() {
        return "Подходит для травничества, монастырских записей и будущих смесей. Цвет метки показывает характер растения.";
    }

    private String caution() {
        return this.selectedEntry.accent() == DANGER ? "Ядовитый знак: не используй как пищу и держи отдельно от лечебных трав." : "Безопаснее ядовитых трав, но перед применением сверяй запись с местом сбора.";
    }

    private void renderIcon(GuiGraphics guiGraphics, ItemStack stack, int x, int y, float scale) {
        guiGraphics.pose().pushPose();
        guiGraphics.pose().translate(x, y, 0.0F);
        guiGraphics.pose().scale(scale, scale, 1.0F);
        guiGraphics.renderItem(stack, 0, 0);
        guiGraphics.pose().popPose();
    }

    private void drawWrapped(GuiGraphics guiGraphics, String text, int x, int y, int width, int color, int maxLines) {
        List<FormattedCharSequence> lines = this.font.split(Component.literal(text), width);
        int lineCount = Math.min(lines.size(), maxLines);
        for (int i = 0; i < lineCount; i++) {
            guiGraphics.drawString(this.font, lines.get(i), x, y + i * 11, color, false);
        }
    }

    private void drawCentered(GuiGraphics guiGraphics, String text, int x, int y, int color) {
        guiGraphics.drawString(this.font, text, x - this.font.width(text) / 2, y, color, false);
    }

    private void move(int direction) {
        this.spread = Math.max(0, Math.min(SPREADS.size() - 1, this.spread + direction));
        updateButtons();
    }

    private void updateButtons() {
        if (this.backButton != null) {
            this.backButton.active = this.spread > 0;
        }
        if (this.nextButton != null) {
            this.nextButton.active = this.spread < SPREADS.size() - 1;
        }
    }

    private void renderCoverCorners(GuiGraphics guiGraphics, int left, int top, int width, int height) {
        drawCornerBracket(guiGraphics, left + 4, top + 4, 1, 1);
        drawCornerBracket(guiGraphics, left + width - 4, top + 4, -1, 1);
        drawCornerBracket(guiGraphics, left + 4, top + height - 4, 1, -1);
        drawCornerBracket(guiGraphics, left + width - 4, top + height - 4, -1, -1);
    }

    private void drawCornerBracket(GuiGraphics guiGraphics, int cx, int cy, int dirX, int dirY) {
        int size = 16;
        int thickness = 4;
        int hX1 = cx + (dirX == 1 ? 0 : -size);
        int hX2 = cx + (dirX == 1 ? size : 0);
        int hY1 = cy + (dirY == 1 ? 0 : -thickness);
        int hY2 = cy + (dirY == 1 ? thickness : 0);
        guiGraphics.fill(hX1, hY1, hX2, hY2, GOLD);
        renderPanelBorder(guiGraphics, hX1, hY1, size, thickness, GOLD_DARK, GOLD);
        int vX1 = cx + (dirX == 1 ? 0 : -thickness);
        int vX2 = cx + (dirX == 1 ? thickness : 0);
        int vY1 = cy + (dirY == 1 ? 0 : -size);
        int vY2 = cy + (dirY == 1 ? size : 0);
        guiGraphics.fill(vX1, vY1, vX2, vY2, GOLD);
        renderPanelBorder(guiGraphics, vX1, vY1, thickness, size, GOLD_DARK, GOLD);
        int rx = cx + dirX * 2 - 2;
        int ry = cy + dirY * 2 - 2;
        guiGraphics.fill(rx, ry, rx + 4, ry + 4, COVER_DARK);
        guiGraphics.fill(rx + 1, ry + 1, rx + 3, ry + 3, GOLD);
    }

    private void renderCoverClasps(GuiGraphics guiGraphics, int left, int top, int width, int height) {
        int claspY = top + height / 2 - 6;
        guiGraphics.fill(left - 8, claspY, left, claspY + 8, COVER_DARK);
        guiGraphics.fill(left - 8, claspY + 1, left, claspY + 7, COVER);
        guiGraphics.fill(left - 12, claspY - 2, left - 8, claspY + 10, GOLD_DARK);
        guiGraphics.fill(left - 11, claspY - 1, left - 9, claspY + 9, GOLD);
        guiGraphics.fill(left - 10, claspY + 3, left - 8, claspY + 5, COVER_DARK);
        guiGraphics.fill(width + left, claspY, width + left + 10, claspY + 8, COVER_DARK);
        guiGraphics.fill(width + left, claspY + 1, width + left + 10, claspY + 7, COVER);
        guiGraphics.fill(width + left + 10, claspY - 1, width + left + 14, claspY + 9, GOLD_DARK);
        guiGraphics.fill(width + left + 11, claspY, width + left + 13, claspY + 8, GOLD);
    }

    private int drawTextWithDropCap(GuiGraphics guiGraphics, String text, int x, int y, int width, int color) {
        if (text == null || text.isEmpty()) return 0;
        
        char firstChar = text.charAt(0);
        String restText = text.substring(1).trim();
        
        int dropCapSize = 20;
        int gap = 6;
        int indentWidth = width - dropCapSize - gap;
        
        // Draw drop cap background
        int capX = x;
        int capY = y + 1;
        guiGraphics.fill(capX, capY, capX + dropCapSize, capY + dropCapSize, RUBY);
        renderPanelBorder(guiGraphics, capX, capY, dropCapSize, dropCapSize, GOLD_DARK, GOLD);
        
        // Draw the letter (scaled up)
        String letterStr = String.valueOf(firstChar);
        guiGraphics.pose().pushPose();
        float scale = 2.0F;
        float letterWidth = this.font.width(letterStr) * scale;
        float letterHeight = 8 * scale;
        float lx = capX + (dropCapSize - letterWidth) / 2.0F;
        float ly = capY + (dropCapSize - letterHeight) / 2.0F;
        guiGraphics.pose().translate(lx, ly, 0.0F);
        guiGraphics.pose().scale(scale, scale, 1.0F);
        guiGraphics.drawString(this.font, letterStr, 0, 0, VELLUM_LIGHT, false);
        guiGraphics.pose().popPose();
        
        // Wrap rest of text
        List<String> words = new java.util.ArrayList<>(List.of(restText.split(" ")));
        List<String> indentLines = new java.util.ArrayList<>();
        StringBuilder currentLine = new StringBuilder();
        int wordIdx = 0;
        
        while (wordIdx < words.size() && indentLines.size() < 2) {
            String word = words.get(wordIdx);
            String test = currentLine.length() == 0 ? word : currentLine + " " + word;
            if (this.font.width(test) <= indentWidth) {
                if (currentLine.length() > 0) currentLine.append(" ");
                currentLine.append(word);
                wordIdx++;
            } else {
                if (currentLine.length() > 0) {
                    indentLines.add(currentLine.toString());
                    currentLine = new StringBuilder();
                } else {
                    indentLines.add(word);
                    wordIdx++;
                }
            }
        }
        if (currentLine.length() > 0 && indentLines.size() < 2) {
            indentLines.add(currentLine.toString());
            currentLine = new StringBuilder();
        }
        
        // Draw indented lines
        int lineHeight = 11;
        for (int i = 0; i < indentLines.size(); i++) {
            guiGraphics.drawString(this.font, indentLines.get(i), x + dropCapSize + gap, y + i * lineHeight, color, false);
        }
        
        int totalHeight = indentLines.size() * lineHeight;
        
        if (wordIdx < words.size()) {
            List<String> remainingWords = words.subList(wordIdx, words.size());
            List<String> fullLines = new java.util.ArrayList<>();
            currentLine = new StringBuilder();
            
            for (String word : remainingWords) {
                String test = currentLine.length() == 0 ? word : currentLine + " " + word;
                if (this.font.width(test) <= width) {
                    if (currentLine.length() > 0) currentLine.append(" ");
                    currentLine.append(word);
                } else {
                    if (currentLine.length() > 0) {
                        fullLines.add(currentLine.toString());
                        currentLine = new StringBuilder(word);
                    } else {
                        fullLines.add(word);
                    }
                }
            }
            if (currentLine.length() > 0) {
                fullLines.add(currentLine.toString());
            }
            
            int startY = y + indentLines.size() * lineHeight;
            for (int i = 0; i < fullLines.size(); i++) {
                guiGraphics.drawString(this.font, fullLines.get(i), x, startY + i * lineHeight, color, false);
            }
            totalHeight += fullLines.size() * lineHeight;
        }
        
        return Math.max(totalHeight, dropCapSize + 2);
    }

    private int bookWidth() {
        return Math.min(720, this.width - 18);
    }

    private int bookHeight() {
        return Math.min(380, this.height - 24);
    }

    private static Entry entry(String name, String latin, String text, Supplier<ItemStack> icon, int accent) {
        return new Entry(name, latin, text, icon, accent);
    }

    private static final class MonasticButton extends AbstractWidget {
        private final Runnable action;

        private MonasticButton(int x, int y, int width, int height, Component message, Runnable action) {
            super(x, y, width, height, message);
            this.action = action;
        }

        @Override
        public void onClick(double mouseX, double mouseY) {
            this.action.run();
        }

        @Override
        protected void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
            int x = getX();
            int y = getY();
            int width = getWidth();
            int height = getHeight();
            boolean hovered = this.active && mouseX >= x && mouseX < x + width && mouseY >= y && mouseY < y + height;
            
            if (this.active && hovered) {
                double time = System.currentTimeMillis() * 0.007;
                int alpha = (int) (110 + 60 * Math.sin(time));
                int pulseColor = (alpha << 24) | (GOLD & 0xFFFFFF);
                guiGraphics.fill(x - 1, y - 1, x + width + 1, y + height + 1, pulseColor);
            }

            int fill = this.active ? (hovered ? GOLD : GOLD_DARK) : 0xFF4C3724;
            int dark = this.active ? (hovered ? GOLD_DARK : 0xFF4C3724) : 0xFF24170E;
            int bright = this.active ? (hovered ? VELLUM_LIGHT : GOLD) : 0xFF5D5142;
            int text = this.active ? (hovered ? INK : PAGE) : 0xFF8A7A62;

            guiGraphics.fill(x + 2, y + 2, x + width + 2, y + height + 2, 0x33000000);
            guiGraphics.fill(x, y, x + width, y + height, dark);
            guiGraphics.fill(x + 2, y + 2, x + width - 2, y + height - 2, fill);
            guiGraphics.hLine(x + 4, x + width - 5, y + 4, bright);
            guiGraphics.hLine(x + 4, x + width - 5, y + height - 5, PAGE_SHADOW);
            guiGraphics.vLine(x + 4, y + 4, y + height - 5, bright);
            guiGraphics.vLine(x + width - 5, y + 4, y + height - 5, PAGE_SHADOW);
            guiGraphics.fill(x, y, x + 4, y + 4, bright);
            guiGraphics.fill(x + width - 4, y, x + width, y + 4, dark);
            guiGraphics.fill(x, y + height - 4, x + 4, y + height, dark);
            guiGraphics.fill(x + width - 4, y + height - 4, x + width, y + height, bright);
            
            var font = Minecraft.getInstance().font;
            String label = getMessage().getString();
            if (this.active && hovered && !"<".equals(label) && !">".equals(label)) {
                label = "§6✦ §r" + label + " §6✦";
            }
            guiGraphics.drawString(font, label, x + width / 2 - font.width(label) / 2, y + (height - 8) / 2, text, false);
        }

        @Override
        protected void updateWidgetNarration(NarrationElementOutput narrationElementOutput) {
        }
    }

    private record Spread(String title, String kicker, String intro, String note, List<Entry> entries) {
    }

    private record Entry(String name, String latin, String text, Supplier<ItemStack> icon, int accent) {
    }
}
