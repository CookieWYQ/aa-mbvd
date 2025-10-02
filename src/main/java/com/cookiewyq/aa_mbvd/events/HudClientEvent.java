package com.cookiewyq.aa_mbvd.events;

import com.cookiewyq.aa_mbvd.capability.Capabilities;
import com.cookiewyq.aa_mbvd.common.BooleanHolder;
import com.cookiewyq.aa_mbvd.configs.ModConfigs;
import com.cookiewyq.aa_mbvd.container.CourtRecordsContainer;
import com.cookiewyq.aa_mbvd.items.ModItems;
import com.cookiewyq.aa_mbvd.items.custom.badges.IBadge;
import com.cookiewyq.aa_mbvd.items.custom.badges.ModThrowableItem;
import com.cookiewyq.aa_mbvd.items.custom.photos.PhotoItem;
import com.cookiewyq.aa_mbvd.keyBinding.ModKeyBindings;
import com.cookiewyq.aa_mbvd.network.Networking;
import com.cookiewyq.aa_mbvd.network.sendPacks.LittleMatterSendPacket;
import com.cookiewyq.aa_mbvd.screen.*;
import com.cookiewyq.aa_mbvd.sound.ModSounds;
import com.cookiewyq.aa_mbvd.util.little_matter.LittleMatter_Langs;
import com.cookiewyq.aa_mbvd.util.little_matter.LittleMatter_Roles;
import com.cookiewyq.aa_mbvd.util.little_matter.LittleMatter_Words;
import com.cookiewyq.aa_mbvd.util.tools;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.SimpleSound;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.network.NetworkHooks;
import org.lwjgl.glfw.GLFW;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.UUID;

import static com.cookiewyq.aa_mbvd.AA_MbvdMod.PLOGGER;
import static com.cookiewyq.aa_mbvd.events.ShowEvidence.handleShowEvidenceSendPacket;
import static com.cookiewyq.aa_mbvd.keyBinding.ModKeyBindings.Show_Badge__Key;

@Mod.EventBusSubscriber(value = Dist.CLIENT)
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class HudClientEvent {

    private static PlayerEntity player;

    private static boolean wasShowItemKeyPressed = false;
    private static boolean isShowItemKeyPressed;
    private static long displayItemStartTime = 0;
    private static ItemStack displayedItem = ItemStack.EMPTY;

    // LittleMatter显示状态管理
    private static boolean wasLittleMatterKeyPressed = false;
    private static long displayLittleMatterStartTime = 0;
    private static LittleMatter_Words displayedWord = null;
    private static LittleMatter_Roles displayedRole = null;
    private static LittleMatter_Langs displayedLang = null;
    private static boolean wasLittleMatterKeyPressedThirdPerson = false;
    private static long displayLittleMatterStartTimeThirdPerson = 0;
    private static LittleMatter_Words displayedWordThirdPerson = null;
    private static LittleMatter_Roles displayedRoleThirdPerson = null;
    private static LittleMatter_Langs displayedLangThirdPerson = null;
    private static PlayerEntity displayedPlayerThirdPerson = null;

    private static boolean wasShowPhotoKeyPressed = false;
    private static boolean isPhotoDisplayed = false;
    private static PhotoShowingScreen currentPhotoScreen = null;


    private static RotatingItemHUD rotatingItemHUD = null;

    // 添加一个变量来记录游戏暂停开始的时间
    private static long pauseStartTime = 0;
    // 记录累计的暂停时间
    private static long totalPausedTime = 0;
    // 记录上一帧是否暂停
    private static boolean wasPaused = false;

    public static STATEHolder stateHolder = new STATEHolder();
    public static BooleanHolder isShowEvidence = new BooleanHolder();

    public static class STATEHolder {
        public STATE state = STATE.None;

        public STATE getState() {
            return state;
        }

        public void setState(STATE s) {
            state = s;
        }
    }

    public enum STATE {
        Pre,
        Post,
        Showing,
        None
    }

    public static ItemStack getDisplayedItem() {
        return displayedItem;
    }

    public static long getDisplayItemStartTime() {
        return displayItemStartTime;
    }

    public static long getDisplayingItemTime() {
        return (System.currentTimeMillis() - displayItemStartTime);
    }

    public static long getDisplayingLittleMatterTime() {
        return (System.currentTimeMillis() - displayLittleMatterStartTime);
    }

    // 添加检查是否正在显示物品的方法
    public static boolean isDisplayingItem() {
        return !displayedItem.isEmpty() && (getEffectiveTime() - displayItemStartTime) < 2000;
    }

    // 添加检查是否正在显示LittleMatter的方法
    public static boolean isDisplayingLittleMatter() {
        return displayedWord != null && (getEffectiveTime() - displayLittleMatterStartTime) < 1500;
    }

    public static boolean isPlayingPhoto() {
        return isPhotoDisplayed && currentPhotoScreen != null;
    }

    public static boolean isDisplayingLittleMatterThirdPerson() {
        return displayedWordThirdPerson != null && (getEffectiveTime() - displayLittleMatterStartTimeThirdPerson) < 1500;
    }

    public static boolean isShowItemKeyPressed() {
        return isShowItemKeyPressed;
    }

    @Nullable
    public static UUID getShowingEvidencePlayerUUID() {
        if (getDisplayingEvidencePlayer() != null) {
            return getDisplayingEvidencePlayer().getUniqueID();
        }
        return null;
    }

    @Nullable
    public static PlayerEntity getDisplayingEvidencePlayer() {
        return player;
    }


    // 添加检查是否正在显示任何内容的方法
    public static boolean isDisplayingAnything() {
        return isDisplayingItem() || isDisplayingLittleMatter() || isPlayingPhoto() || isDisplayingRotatingItemHUD();
    }

    private static boolean isDisplayingRotatingItemHUD() {
        return !displayedItem.isEmpty() && !rotatingItemHUD.isFinished();
    }

    /**
     * 检查当前是否有Little Matter的图片显示（包括第一人称和第三人称）
     *
     * @return 如果有任何Little Matter正在显示则返回true，否则返回false
     */
    public static boolean isDisplayingLittleMatterHUD() {
        return isDisplayingLittleMatter() || isDisplayingLittleMatterThirdPerson();
    }

    /**
     * 获取当前"有效时间"，排除游戏暂停期间的时间
     * @return 有效的系统时间（毫秒）
     */
    public static long getEffectiveTime() {
        Minecraft mc = Minecraft.getInstance();
        boolean isPaused = mc.isGamePaused();

        // 检查暂停状态变化
        if (isPaused && !wasPaused) {
            // 刚刚进入暂停状态
            pauseStartTime = System.currentTimeMillis();
        } else if (!isPaused && wasPaused) {
            // 刚刚退出暂停状态
            totalPausedTime += System.currentTimeMillis() - pauseStartTime;
        }

        wasPaused = isPaused;

        // 返回排除暂停时间的有效时间
        return System.currentTimeMillis() - totalPausedTime;
    }

    /**
     * 重置暂停时间计数器
     */
    public static void resetPauseTime() {
        totalPausedTime = 0;
        pauseStartTime = 0;
        wasPaused = false;
    }

    /**
     * 处理自动显示"看招"Little Matter HUD的逻辑
     * 只有在当前没有其他Little Matter显示时才会触发
     */
    public static void handleAutoDisplayTakethat(PlayerEntity player) {
        // 检查是否满足触发条件且当前没有Little Matter在显示
        if (!isDisplayingLittleMatterHUD() && hasBadge(player)) {
            // 设置显示"看招"
            displayedWord = LittleMatter_Words.Take_that;
            displayedRole = getRoleFromCurioItem((ModThrowableItem) tools.getCurioItemFromSlot(player, "charm_badge", 0).getItem());
            displayedLang = LittleMatter_Langs.getRandomLang();
            displayLittleMatterStartTime = System.currentTimeMillis();

            // 播放相应的声音
            RegistryObject<SoundEvent> sound = ModSounds.getLittleMatterSound(displayedWord, displayedRole, displayedLang);
            if (sound != null && sound.isPresent()) {
                Minecraft.getInstance().getSoundHandler().play(
                        SimpleSound.master(sound.get(), 1.0F, 1.0F)
                );
            } else {
                System.out.println("Sound not found for: " + displayedWord + ", " + displayedRole + ", " + displayedLang);
            }
        }
    }

    private static INamedContainerProvider createContainerProvider(World worldIn, BlockPos pos) {
        return new INamedContainerProvider() {
            @Override
            public ITextComponent getDisplayName() {
                return new TranslationTextComponent("screen.court_records").mergeStyle(TextFormatting.BOLD).mergeStyle(TextFormatting.WHITE);
            }

            @Nonnull
            @Override
            public Container createMenu(int i, PlayerInventory playerInventory, PlayerEntity playerEntity) {
                return new CourtRecordsContainer(i, worldIn, pos, playerInventory, playerEntity);
            }
        };
    }

    public static boolean hasBadge(PlayerEntity player) {
        return tools.getCurioItemFromSlot(player, "charm_badge", 0).getItem() instanceof IBadge;
    }

    @SubscribeEvent
    public static void onKeyInput(InputEvent.KeyInputEvent event) {
        if (player != null &&
                event.getKey() == ModKeyBindings.Open_Court_Records__Key.getKey().getKeyCode() &&
                player.world != null) {
            player.getPosition();
            ServerPlayerEntity serverPlayer = (ServerPlayerEntity) tools.getPlayerEntityFromUUIDByTickEvent(player.getUniqueID());
            if (serverPlayer != null) {
                INamedContainerProvider containerProvider = createContainerProvider(player.world, player.getPosition());
                NetworkHooks.openGui(serverPlayer, containerProvider, player.getPosition());
            }
        }
    }


    // 在 HudClientEvent.java 中修改相关代码
    @SubscribeEvent
    public static void onOverlayRender(RenderGameOverlayEvent event) {
        if (event.getType() != RenderGameOverlayEvent.ElementType.ALL) {
            return;
        }

        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null) return;

        player = mc.player;

        LittleMatterDisplayManager.renderDisplays();

        // 处理LittleMatter按键按下事件
        boolean isObjectionKeyPressed = ModKeyBindings.Objection__Key.isKeyDown();
        boolean isHolditKeyPressed = ModKeyBindings.Holdit__Key.isKeyDown();
        boolean isTakethatKeyPressed = ModKeyBindings.Takethat__Key.isKeyDown();

        // 检查是否有任何LittleMatter按键被按下
        boolean isAnyLittleMatterKeyPressed = isObjectionKeyPressed || isHolditKeyPressed || isTakethatKeyPressed;

        // 检查是否为第一人称
        boolean isFirstPerson = tools.isFirstPersonCamera(mc);

        // 第一人称逻辑
        if (isFirstPerson) {
            handleFirstPersonLittleMatter(isAnyLittleMatterKeyPressed, isHolditKeyPressed, isTakethatKeyPressed);
        }
        // 第三人称逻辑
        else {
            handleThirdPersonLittleMatter(isAnyLittleMatterKeyPressed, isHolditKeyPressed, isTakethatKeyPressed, player);
        }

        // 显示LittleMatterHUD（第一人称）
        ItemStack i = tools.getCurioItemFromSlot(player, "charm_badge", 0);
        boolean c = !i.isEmpty();
        if (c) {
            c = false;
            ModThrowableItem badge = (ModThrowableItem) i.getItem();
            displayedRole = getRoleFromCurioItem(badge);
            if (!(displayedRole == LittleMatter_Roles.None)) {
                c = true;
            }
        }
        if (isFirstPerson && isDisplayingLittleMatter() && c) {
            LittleMatterHUD littleMatterHUD = new LittleMatterHUD(event.getMatrixStack(), displayedWord, displayedRole, displayedLang, displayLittleMatterStartTime);
            littleMatterHUD.show();
        }

        // 显示LittleMatterHUD（第三人称）
        if (!isFirstPerson && isDisplayingLittleMatterThirdPerson()) {
            LittleMatterHeadHUD littleMatterHeadHUD = new LittleMatterHeadHUD(
                    event.getMatrixStack(),
                    displayedWordThirdPerson,
                    displayedRoleThirdPerson,
                    displayedLangThirdPerson,
                    displayLittleMatterStartTimeThirdPerson,
                    displayedPlayerThirdPerson
            );
            littleMatterHeadHUD.renderHeadDisplay();
        }


        ///////////////////////////////////////////////////////////////////////////////////////////////

        if (ModConfigs.isEnableOldSchoolShowingEvidenceHUD.get()) {
            // 处理出示证物按键按下事件
            isShowItemKeyPressed = Show_Badge__Key.isKeyDown() && hasBadge(player);

            // 只有在没有显示任何内容时才能显示新物品
            if (isShowItemKeyPressed && !isDisplayingAnything()) {
                if (!wasShowItemKeyPressed) {
                    // 按键刚刚被按下，记录开始时间和物品

                    displayItemStartTime = System.currentTimeMillis();
                    displayedItem = player.getHeldItem(Hand.MAIN_HAND).copy();
                    if (displayedItem.isEmpty()) {
                        displayedItem = tools.getCurioItemFromSlot(player, "charm_badge", 0);
                        if (displayedItem.isEmpty()) {
                            displayedItem = tools.getCurioItemFromSlot(player, "charm_mayas_magatama", 0);
                        }
                    }

                    PLOGGER.debug("Fuckkkkkkkkkkk Pre");

                    handleShowEvidenceSendPacket(STATE.Pre, displayedItem, player, getDisplayingItemTime(), displayItemStartTime);

                    stateHolder.setState(STATE.Pre);
                    isShowEvidence.set(true);

                    player.getCapability(Capabilities.SHOWING_EVIDENCE_DATA_CAPABILITY).ifPresent(data -> {
                        data.setShowEvidence(true);
                        data.setState(STATE.Pre);
                        data.setDisplayingItemTime(getDisplayingItemTime());
                        data.setDisplayedItem(displayedItem);
                        data.setDisplayingEvidencePlayer(player);
                    });

                    if (!displayedItem.isEmpty()) {
                        mc.getSoundHandler().play(SimpleSound.master(ModSounds.SHOW_BADGE.get(), 1F, 3F));
                    }
                }
            }

            wasShowItemKeyPressed = isShowItemKeyPressed;

            // 显示物品HUD（持续2秒）
            if (isDisplayingItem()) {

                handleShowEvidenceSendPacket(STATE.Showing, displayedItem, player, getDisplayingItemTime(), displayItemStartTime);

                stateHolder.setState(STATE.Showing);
                isShowEvidence.set(true);

                player.getCapability(Capabilities.SHOWING_EVIDENCE_DATA_CAPABILITY).ifPresent(data -> {
                    data.setShowEvidence(true);
                    data.setState(STATE.Showing);
                    data.setDisplayingItemTime(getDisplayingItemTime());
                    data.setDisplayedItem(displayedItem);
                    data.setDisplayingEvidencePlayer(player);
                });

                ShowingEvidenceHUD showingEvidenceHUD = new ShowingEvidenceHUD(event.getMatrixStack(), displayedItem);
                showingEvidenceHUD.show();
            } else if (!displayedItem.isEmpty() && getDisplayingItemTime() >= 2000) {
                // 2秒后清除显示的物品

                handleShowEvidenceSendPacket(STATE.Post, displayedItem, player, getDisplayingItemTime(), displayItemStartTime);

                stateHolder.setState(STATE.Post);
                isShowEvidence.set(true);

                player.getCapability(Capabilities.SHOWING_EVIDENCE_DATA_CAPABILITY).ifPresent(data -> {
                    data.setShowEvidence(true);
                    data.setState(STATE.Post);
                    data.setDisplayingItemTime(getDisplayingItemTime());
                    data.setDisplayedItem(displayedItem);
                    data.setDisplayingEvidencePlayer(player);
                });

                displayedItem = ItemStack.EMPTY;
            }
        }

        ///////////////////////////////////////////////////////////////////////////////////////////////


        ///////////////////////////////////////////////////////////////////////////////////////////////

        else {
            // 处理出示证物按键按下事件
            isShowItemKeyPressed = Show_Badge__Key.isKeyDown() && hasBadge(player);

            // 检测按键按下事件
            if (isShowItemKeyPressed && !wasShowItemKeyPressed && !isDisplayingAnything()) {
                PLOGGER.info("检测到出示证物按键按下");

                // 获取物品
                displayedItem = player.getHeldItem(Hand.MAIN_HAND).copy();
                if (displayedItem.isEmpty()) {
                    if (!Screen.hasShiftDown()) {
                        displayedItem = tools.getCurioItemFromSlot(player, "charm_badge", 0);
                    } else {
                        displayedItem = tools.getCurioItemFromSlot(player, "charm_mayas_magatama", 0);
                    }
                }

                if (!displayedItem.isEmpty()) {
                    PLOGGER.info("玩家手中有物品: {}", displayedItem.getItem().getRegistryName());

                    handleShowEvidenceSendPacket(STATE.Pre, displayedItem, player, getDisplayingItemTime(), displayItemStartTime);

                    stateHolder.setState(STATE.Pre);
                    isShowEvidence.set(true);

                    player.getCapability(Capabilities.SHOWING_EVIDENCE_DATA_CAPABILITY).ifPresent(data -> {
                        data.setShowEvidence(true);
                        data.setState(STATE.Pre);
                        data.setDisplayingItemTime(getDisplayingItemTime());
                        data.setDisplayedItem(displayedItem);
                        data.setDisplayingEvidencePlayer(player);
                    });
                    // 创建或重置旋转物品HUD
                    rotatingItemHUD = new RotatingItemHUD(event.getMatrixStack(), displayedItem);

                    if (!displayedItem.isEmpty()) {
                        // 播放相应的声音
                        displayedLang = LittleMatter_Langs.JP;
                        LittleMatter_Roles role = getRoleFromCurioItem((ModThrowableItem) tools.getCurioItemFromSlot(player, "charm_badge", 0).getItem());
                        RegistryObject<SoundEvent> sound = ModSounds.getLittleMatterSound(LittleMatter_Words.Take_that, role, displayedLang);
                        if (sound != null && sound.isPresent()) {
                            Minecraft.getInstance().getSoundHandler().play(
                                    SimpleSound.master(sound.get(), 1.0F, 1.0F)
                            );
                        } else {
                            System.out.println("Sound not found for: " + LittleMatter_Words.Take_that + ", " + role + ", " + displayedLang);
                        }
                    }

                } else {
                    PLOGGER.info("玩家手中没有物品");
                }
            }

            wasShowItemKeyPressed = isShowItemKeyPressed;

            // 显示旋转物品HUD
            if (rotatingItemHUD != null) {
                if (!rotatingItemHUD.isFinished()) {
                    handleShowEvidenceSendPacket(STATE.Showing, displayedItem, player, getDisplayingItemTime(), displayItemStartTime);

                    stateHolder.setState(STATE.Showing);
                    isShowEvidence.set(true);

                    player.getCapability(Capabilities.SHOWING_EVIDENCE_DATA_CAPABILITY).ifPresent(data -> {
                        data.setShowEvidence(true);
                        data.setState(STATE.Showing);
                        data.setDisplayingItemTime(getDisplayingItemTime());
                        data.setDisplayedItem(displayedItem);
                        data.setDisplayingEvidencePlayer(player);
                    });
                    rotatingItemHUD.show();

                } else {
                    // 动画结束后清理
                    rotatingItemHUD = null;
                    handleShowEvidenceSendPacket(STATE.Post, displayedItem, player, getDisplayingItemTime(), displayItemStartTime);

                    stateHolder.setState(STATE.Post);
                    isShowEvidence.set(true);

                    player.getCapability(Capabilities.SHOWING_EVIDENCE_DATA_CAPABILITY).ifPresent(data -> {
                        data.setShowEvidence(true);
                        data.setState(STATE.Post);
                        data.setDisplayingItemTime(getDisplayingItemTime());
                        data.setDisplayedItem(displayedItem);
                        data.setDisplayingEvidencePlayer(player);
                    });

                    displayedItem = ItemStack.EMPTY;
                }
            }
        }

        ///////////////////////////////////////////////////////////////////////////////////////////////

        // 处理照片显示按键按下事件
        boolean isShowPhotoKeyPressed = ModKeyBindings.Show_Photo__Key.isKeyDown();

        // 检测按键按下事件（从释放到按下）
        if (isShowPhotoKeyPressed && !wasShowPhotoKeyPressed) {
            if (player.getHeldItem(Hand.MAIN_HAND).getItem() instanceof PhotoItem) {
                // 切换显示状态
                isPhotoDisplayed = !isPhotoDisplayed;

                if (isPhotoDisplayed) {
                    // 显示照片
                    currentPhotoScreen = new PhotoShowingScreen(event.getMatrixStack(), (PhotoItem) player.getHeldItem(Hand.MAIN_HAND).getItem());
                    mc.getSoundHandler().play(SimpleSound.master(ModSounds.OPEN_DETAILS.get(), 1F, 3F));
                } else {
                    // 隐藏照片
                    mc.getSoundHandler().play(SimpleSound.master(ModSounds.CLOSE_DETAILS.get(), 1F, 3F));
                    currentPhotoScreen = null;
                }
            }
        }

        wasShowPhotoKeyPressed = isShowPhotoKeyPressed;

        // 显示照片（如果处于显示状态）
        if (isPhotoDisplayed && currentPhotoScreen != null) {
            currentPhotoScreen.show();
        }
    }


    private static LittleMatter_Roles getRoleFromCurioItem(ModThrowableItem badge) {
        if (badge.equals(ModItems.AttorneysBadgeItem.get())) {
            return LittleMatter_Roles.Phoenix_Wright;
        } else if (badge.equals(ModItems.ProsbadgeItem.get())) {
            return LittleMatter_Roles.Miles_Edgeworth;
        } else {
            return LittleMatter_Roles.None;
        }
    }

    private static void handleFirstPersonLittleMatter(boolean isAnyLittleMatterKeyPressed,
                                                      boolean isHolditKeyPressed, boolean isTakethatKeyPressed) {
        if (isAnyLittleMatterKeyPressed && !isDisplayingAnything()) {
            if (!wasLittleMatterKeyPressed) {
                // 按键刚刚被按下，记录开始时间和随机生成的内容
                displayLittleMatterStartTime = System.currentTimeMillis();

                // 根据按下的键设置对应的词语
                if (isHolditKeyPressed) {
                    displayedWord = LittleMatter_Words.Hold_it;
                } else if (isTakethatKeyPressed) {
                    displayedWord = LittleMatter_Words.Take_that;
                } else {
                    displayedWord = LittleMatter_Words.Objection;
                }

                ItemStack i = tools.getCurioItemFromSlot(player, "charm_badge", 0);
                if (i.isEmpty()) {
                    wasLittleMatterKeyPressed = true;
                    return;
                }
                ModThrowableItem badge = (ModThrowableItem) i.getItem();
                displayedRole = getRoleFromCurioItem(badge);
                if (displayedRole == LittleMatter_Roles.None) {
                    wasLittleMatterKeyPressed = true;
                    return;
                }
                displayedLang = LittleMatter_Langs.getRandomLang();
                RegistryObject<SoundEvent> sound = ModSounds.getLittleMatterSound(displayedWord, displayedRole, displayedLang);
                if (sound != null && sound.isPresent()) {
                    Minecraft.getInstance().getSoundHandler().play(
                            SimpleSound.master(sound.get(), 1.0F, 1.0F)
                    );
                } else {
                    System.out.println("Sound not found for: " + displayedWord + ", " + displayedRole + ", " + displayedLang);
                }
            }
        }
        wasLittleMatterKeyPressed = isAnyLittleMatterKeyPressed;

        /////////////////////////////////////////////////////////////////////////////////////////////
        /////////////////////////////////////////////////////////////////////////////////////////////
        /////////////////////////////////////////////////////////////////////////////////////////////
        /////////////////////////////////////////////////////////////////////////////////////////////
        /////////////////////////////////////////////////////////////////////////////////////////////

        // 添加清理过期的 Little Matter 状态
        if (displayedWord != null && getDisplayingLittleMatterTime() >= 1500) {
            displayedWord = null;
            displayedRole = null;
            displayedLang = null;
        }

        if (displayedWordThirdPerson != null && (getEffectiveTime() - displayLittleMatterStartTimeThirdPerson) >= 1500) {
            displayedWordThirdPerson = null;
            displayedRoleThirdPerson = null;
            displayedLangThirdPerson = null;
            displayedPlayerThirdPerson = null;
        }

    }

    private static void handleThirdPersonLittleMatter(boolean isAnyLittleMatterKeyPressed,
                                                      boolean isHolditKeyPressed, boolean isTakethatKeyPressed, PlayerEntity player) {
        if (isAnyLittleMatterKeyPressed && !isDisplayingLittleMatterThirdPerson()) {
            if (!wasLittleMatterKeyPressedThirdPerson) {
                // 按键刚刚被按下，记录开始时间和随机生成的内容
                displayLittleMatterStartTimeThirdPerson = System.currentTimeMillis();
                displayedPlayerThirdPerson = player;

                // 根据按下的键设置对应的词语
                if (isHolditKeyPressed) {
                    displayedWordThirdPerson = LittleMatter_Words.Hold_it;
                } else if (isTakethatKeyPressed) {
                    displayedWordThirdPerson = LittleMatter_Words.Take_that;
                } else {
                    displayedWordThirdPerson = LittleMatter_Words.Objection;
                }

                ItemStack i = tools.getCurioItemFromSlot(player, "charm_badge", 0);
                if (i.isEmpty()) {
                    wasLittleMatterKeyPressedThirdPerson = true;
                    return;
                }
                ModThrowableItem badge = (ModThrowableItem) i.getItem();
                displayedRoleThirdPerson = getRoleFromCurioItem(badge);
                if (displayedRoleThirdPerson == LittleMatter_Roles.None) {
                    wasLittleMatterKeyPressedThirdPerson = true;
                    return;
                }
                displayedLangThirdPerson = LittleMatter_Langs.getRandomLang();

                // 播放声音（让附近玩家能听到）
                RegistryObject<SoundEvent> sound = ModSounds.getLittleMatterSound(displayedWordThirdPerson, displayedRoleThirdPerson, displayedLangThirdPerson);
                if (sound != null && sound.isPresent()) {
                    // 先在本地播放声音
                    Minecraft.getInstance().getSoundHandler().play(
                            SimpleSound.master(sound.get(), 1.0F, 1.0F)
                    );
                    // 然后发送网络包到服务器
                    Networking.INSTANCE.sendToServer(new LittleMatterSendPacket(
                            displayedWordThirdPerson,
                            displayedRoleThirdPerson,
                            displayedLangThirdPerson,
                            displayedPlayerThirdPerson.getPosX(),
                            displayedPlayerThirdPerson.getPosY(),
                            displayedPlayerThirdPerson.getPosZ()
                    ));
                } else {
                    System.out.println("Sound not found for: " + displayedWordThirdPerson + ", " + displayedRoleThirdPerson + ", " + displayedLangThirdPerson);
                }
            }
        }
        wasLittleMatterKeyPressedThirdPerson = isAnyLittleMatterKeyPressed;
    }
}


