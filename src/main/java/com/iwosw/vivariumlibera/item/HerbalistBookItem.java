package com.iwosw.vivariumlibera.item;

import com.iwosw.vivariumlibera.block.entity.AlchemyTableBlockEntity;
import java.util.List;
import net.minecraft.ChatFormatting;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.server.network.Filterable;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.WrittenBookContent;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;

public final class HerbalistBookItem extends Item {
    public HerbalistBookItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        if (!(level.getBlockEntity(context.getClickedPos()) instanceof AlchemyTableBlockEntity table) || table.hasBook()) {
            return InteractionResult.PASS;
        }

        ItemStack stack = context.getItemInHand();
        if (!level.isClientSide() && table.placeBook(stack)) {
            Player player = context.getPlayer();
            if (player == null || !player.getAbilities().instabuild) {
                stack.shrink(1);
            }
        }

        return InteractionResult.sidedSuccess(level.isClientSide());
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (stack.get(DataComponents.WRITTEN_BOOK_CONTENT) == null) {
            stack.set(DataComponents.WRITTEN_BOOK_CONTENT, createContent());
        }

        if (level.isClientSide()) {
            openClientScreen();
        } else {
            player.awardStat(Stats.ITEM_USED.get(this));
        }

        return InteractionResultHolder.sidedSuccess(stack, level.isClientSide());
    }

    private static void openClientScreen() {
        try {
            Class.forName("com.iwosw.vivariumlibera.client.HerbalistBookScreen").getMethod("open").invoke(null);
        } catch (ReflectiveOperationException ignored) {
            // The client-only screen is unavailable on a dedicated server classpath.
        }
    }

    public static WrittenBookContent createContent() {
        return new WrittenBookContent(
                Filterable.passThrough("Книга травника"),
                "Vivarium Libera",
                0,
                List.of(
                        cover(),
                        preface(),
                        contents(),
                        chapter("I", "Сухие и лесные", "Полынь", "Крапива", "Зверобой", "Цикорий"),
                        entry("Полынь", "Artemisia", "✦", ChatFormatting.DARK_GREEN, "Серебристая горечь сухих мест. Пахнет пылью, дымом и старой защитной связкой.", "Место", "сухие луга, каменистые края"),
                        entry("Крапива", "Urtica", "♧", ChatFormatting.DARK_GREEN, "Жгучий знак влажной и плодородной земли. Часто растет у тени, воды и забытых троп.", "Сбор", "срезать ножом, не рвать рукой"),
                        entry("Зверобой", "Hypericum", "✚", ChatFormatting.GOLD, "Солнечная желтая трава просек. Делает поляну теплее и безопаснее на вид.", "Знак", "светлая луговая трава"),
                        entry("Цикорий", "Cichorium", "❦", ChatFormatting.BLUE, "Синий дорожный цветок для опушек, краев поселений и мест, где природа встречает тропу.", "Цвет", "холодный небесный синий"),
                        chapter("II", "Берег и болото", "Аир", "Рогоз", "Мята", "Окопник"),
                        entry("Аир болотный", "Sweet Flag", "≈", ChatFormatting.DARK_AQUA, "Мечевидные листья у воды. Верный указатель мелководья, ручья или заболоченной кромки.", "Место", "берег, болото, тихая вода"),
                        entry("Рогоз", "Cattail", "〰", ChatFormatting.DARK_AQUA, "Высокий страж стоячей воды. Его пух пригодится ремеслу, а силуэт делает берег узнаваемым.", "Дар", "пух рогоза"),
                        entry("Мята", "Mentha", "✿", ChatFormatting.DARK_AQUA, "Свежая влажная трава. Где растет мята, земля темнее, воздух прохладнее, а вода ближе.", "Место", "берег, ручей, влажная тень"),
                        entry("Окопник", "Comfrey", "☘", ChatFormatting.DARK_GREEN, "Плотная лекарская трава низин. Связывает травничество с темной плодородной почвой.", "Место", "влажные берега и низины"),
                        chapter("III", "Сад лекаря", "Календула", "Шалфей", "Очанка", "Валериана"),
                        entry("Календула", "Calendula", "✺", ChatFormatting.GOLD, "Оранжевый свет грядки. Мягкое растение, которое выглядит теплым и безопасным.", "Место", "сад, луг, светлая земля"),
                        entry("Шалфей", "Sage", "☉", ChatFormatting.DARK_GREEN, "Сухой строгий кустик, почти монастырская трава. Хорош у камня, двора и дома лекаря.", "Место", "сухой сад, двор, гряда"),
                        entry("Очанка", "Eyebright", "◌", ChatFormatting.YELLOW, "Малый светлый цветок, заметный только внимательному глазу. Трава ясного взгляда.", "Знак", "мелкое луговое растение"),
                        entry("Валериана", "Valeriana", "✽", ChatFormatting.LIGHT_PURPLE, "Высокие белые, розовые и красные цветы тихого сада. Трава сна и вечернего воздуха.", "Место", "сад лекаря, тихий луг"),
                        chapter("IV", "Луговые краски", "Колокольчики", "Тысячелистник", "Кислица", "Иван-чай"),
                        entry("Колокольчики", "Campanula", "⚘", ChatFormatting.BLUE, "Синие, розовые, фиолетовые и белые цветы. Делают луг мягким, не превращая его в шумный ковер.", "Цвета", "синий, розовый, фиолетовый, белый"),
                        entry("Тысячелистник", "Yarrow", "✣", ChatFormatting.GOLD, "Пучки белых, желтых, розовых и красных соцветий. Хорошая мозаика для разнотравья.", "Сбор", "дает нарезанный тысячелистник"),
                        entry("Кислица", "Wood Sorrel", "♣", ChatFormatting.RED, "Низкая красная или желтая травка подлеска. Делает землю живой мелким цветным рисунком.", "Место", "тень, подлесок, влажная почва"),
                        entry("Иван-чай", "Fireweed", "✧", ChatFormatting.LIGHT_PURPLE, "Высокая розовая свеча вырубок. Показывает, где природа возвращает себе землю.", "Место", "опушки, луга, просеки"),
                        chapter("V", "Ядовитый лист", "Белена", "Дурман", "Вороний глаз", "Белладонна"),
                        entry("Белена", "Hyoscyamus", "☠", ChatFormatting.DARK_RED, "Тусклая и тревожная трава дурного сна. Ее красота должна просить держать дистанцию.", "Осторожно", "ядовита"),
                        entry("Дурман", "Datura", "☾", ChatFormatting.DARK_PURPLE, "Крупное колдовское растение с тяжелым запахом. Красиво, но не дружелюбно к путнику.", "Осторожно", "ядовит"),
                        entry("Вороний глаз", "Crow's Eye", "●", ChatFormatting.DARK_PURPLE, "Темная ягода среди листьев. Недобрый знак: не все найденное стоит брать.", "Осторожно", "опасные ягоды"),
                        entry("Белладонна", "Belladonna", "◆", ChatFormatting.DARK_RED, "Соблазнительные ягоды и смертельный смысл. Растение темных садов и ведьминой алхимии.", "Осторожно", "ядовитые ягоды"),
                        chapter("VI", "Редкие знаки", "Ликорис", "Чертополох", "Сливовый саженец", "Тростник"),
                        entry("Ликорис", "Lycoris", "✹", ChatFormatting.RED, "Редкий алый цветок с почти погребальной красотой. Каждая находка должна казаться событием.", "Место", "редкие цветочные пятна"),
                        entry("Чертополох", "Thistle", "✷", ChatFormatting.DARK_GREEN, "Колючая гордая трава сухих склонов. Добавляет суровость полям, опушкам и северным лугам.", "Место", "склон, сухая почва"),
                        entry("Сливовый саженец", "Plum Sapling", "♠", ChatFormatting.DARK_RED, "Начало домашнего сада. Слива связывает дикое травничество с плодами, древесиной и заботой.", "Дар", "сливовая древесина и листья"),
                        entry("Тростник", "Sugar Cane", "✥", ChatFormatting.GOLD, "Не новая трава, но важный материал берега. Кожура и внутрянка поддерживают ремесло у воды.", "Дар", "сырье для будущих рецептов"),
                        closing()
                ),
                true
        );
    }

    private static Filterable<Component> cover() {
        return page(Component.empty()
                .append(Component.literal("✥ КНИГА ТРАВНИКА ✥\n").withStyle(ChatFormatting.DARK_GREEN, ChatFormatting.BOLD))
                .append(Component.literal("Vivarium Libera\n\n").withStyle(ChatFormatting.GOLD))
                .append(Component.literal("Рукописный гербарий для путников, лекарей, ведьм и хранителей живого мира.\n\n").withStyle(ChatFormatting.DARK_GRAY))
                .append(Component.literal("☘ Открывай у воды, у костра или в саду: каждая трава помнит свое место.").withStyle(ChatFormatting.DARK_GREEN)));
    }

    private static Filterable<Component> preface() {
        return page(Component.empty()
                .append(Component.literal("Предисловие\n").withStyle(ChatFormatting.GOLD, ChatFormatting.BOLD))
                .append(Component.literal("О травах свободного сада\n\n").withStyle(ChatFormatting.DARK_GREEN, ChatFormatting.BOLD))
                .append(Component.literal("Всякое растение здесь не украшение, а малая часть живого мира.\n\n").withStyle(ChatFormatting.DARK_GRAY))
                .append(Component.literal("Смотри на место: берег ведет к воде, сухой склон к пыли, темная ягода к опасности.").withStyle(ChatFormatting.DARK_GRAY)));
    }

    private static Filterable<Component> contents() {
        return page(Component.empty()
                .append(Component.literal("Оглавление\n\n").withStyle(ChatFormatting.GOLD, ChatFormatting.BOLD))
                .append(Component.literal("I  Сухие и лесные\n").withStyle(ChatFormatting.DARK_GREEN))
                .append(Component.literal("II Берег и болото\n").withStyle(ChatFormatting.DARK_AQUA))
                .append(Component.literal("III Сад лекаря\n").withStyle(ChatFormatting.GREEN))
                .append(Component.literal("IV Луговые краски\n").withStyle(ChatFormatting.LIGHT_PURPLE))
                .append(Component.literal("V  Ядовитый лист\n").withStyle(ChatFormatting.DARK_RED))
                .append(Component.literal("VI Редкие знаки").withStyle(ChatFormatting.DARK_PURPLE)));
    }

    private static Filterable<Component> chapter(String number, String title, String first, String second, String third, String fourth) {
        return page(Component.empty()
                .append(Component.literal("Глава " + number + "\n").withStyle(ChatFormatting.GOLD, ChatFormatting.BOLD))
                .append(Component.literal(title + "\n\n").withStyle(ChatFormatting.DARK_GREEN, ChatFormatting.BOLD))
                .append(Component.literal("✥ " + first + "\n").withStyle(ChatFormatting.DARK_GRAY))
                .append(Component.literal("✥ " + second + "\n").withStyle(ChatFormatting.DARK_GRAY))
                .append(Component.literal("✥ " + third + "\n").withStyle(ChatFormatting.DARK_GRAY))
                .append(Component.literal("✥ " + fourth + "\n\n").withStyle(ChatFormatting.DARK_GRAY))
                .append(Component.literal("Записывай цвет, место и пользу: так луг превращается в карту.").withStyle(ChatFormatting.DARK_GREEN)));
    }

    private static Filterable<Component> entry(String name, String latin, String glyph, ChatFormatting accent, String body, String noteTitle, String note) {
        return page(Component.empty()
                .append(Component.literal(glyph + " " + name + "\n").withStyle(accent, ChatFormatting.BOLD))
                .append(Component.literal(latin + "\n\n").withStyle(ChatFormatting.GOLD, ChatFormatting.ITALIC))
                .append(Component.literal(body + "\n\n").withStyle(ChatFormatting.DARK_GRAY))
                .append(Component.literal(noteTitle + ": ").withStyle(accent, ChatFormatting.BOLD))
                .append(Component.literal(note).withStyle(ChatFormatting.DARK_GRAY)));
    }

    private static Filterable<Component> closing() {
        return page(Component.empty()
                .append(Component.literal("✥ Заметки травника ✥\n\n").withStyle(ChatFormatting.DARK_GREEN, ChatFormatting.BOLD))
                .append(Component.literal("Береговые растения помогают искать воду.\n\n").withStyle(ChatFormatting.DARK_AQUA))
                .append(Component.literal("Ядовитые должны быть красивыми, но тревожными.\n\n").withStyle(ChatFormatting.DARK_RED))
                .append(Component.literal("Луговые цветы лучше работают пятнами: редкими, цветными и запоминающимися.").withStyle(ChatFormatting.DARK_GRAY)));
    }

    private static Filterable<Component> page(Component component) {
        return Filterable.passThrough(component);
    }
}
