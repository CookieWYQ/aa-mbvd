package com.cookiewyq.aa_mbvd;

import com.cookiewyq.aa_mbvd.blocks.ModBlocks;
import com.cookiewyq.aa_mbvd.capability.Capabilities;
import com.cookiewyq.aa_mbvd.capability.CourtRecordProvider;
import com.cookiewyq.aa_mbvd.capability.IShowingEvidenceData;
import com.cookiewyq.aa_mbvd.configs.ModConfigs;
import com.cookiewyq.aa_mbvd.container.ModContainerTypes;
import com.cookiewyq.aa_mbvd.enchantments.ModEnchantments;
import com.cookiewyq.aa_mbvd.entities.ModEntityTypes;
import com.cookiewyq.aa_mbvd.events.KeyInputHandler;
import com.cookiewyq.aa_mbvd.events.ModForgeEvents;
import com.cookiewyq.aa_mbvd.items.MinecraftEvidences;
import com.cookiewyq.aa_mbvd.items.ModItems;
import com.cookiewyq.aa_mbvd.keyBinding.ModKeyBindings;
import com.cookiewyq.aa_mbvd.network.Networking;
import com.cookiewyq.aa_mbvd.renderers.PhoenixWrightRenderer;
import com.cookiewyq.aa_mbvd.screen.CourtRecordScreen;
import com.cookiewyq.aa_mbvd.sound.ModSounds;
import com.cookiewyq.aa_mbvd.tileentity.ModTileEntities;
import com.cookiewyq.aa_mbvd.villagers.ModPOIs;
import com.cookiewyq.aa_mbvd.villagers.ModVillagerProfessions;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.client.renderer.entity.SpriteRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import software.bernie.geckolib3.GeckoLib;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotTypeMessage;

@Mod(AA_MbvdMod.MOD_ID)
public class AA_MbvdMod {
    public static final String MOD_ID = "aa_mbvd";
    public static final Logger PLOGGER = LogManager.getLogger();

    public AA_MbvdMod() {
        IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();

        ModSounds.registerLittleMatterSounds();
        ModSounds.register(eventBus);
        ModItems.register(eventBus);
        ModEntityTypes.register(eventBus);
        ModBlocks.register(eventBus);
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, ModConfigs.COMMON_CONFIG);
        PLOGGER.info("About to register network messages");
        Networking.registerMessage();
        PLOGGER.info("Network messages registered");
        ModContainerTypes.register(eventBus);
        ModTileEntities.register(eventBus);
        ModEnchantments.register(eventBus);
        ModPOIs.register(eventBus);
        ModVillagerProfessions.register(eventBus);

        // Register the setup method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
        // Register the enqueueIMC method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::enqueueIMC);
        // Register the processIMC method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::processIMC);
        // Register the doClientStuff method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::doClientStuff);

        eventBus.addListener(Capabilities::registerCapabilities);

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);

        // 注册Forge事件总线上的事件监听器
        MinecraftForge.EVENT_BUS.register(new Object() {
            @SubscribeEvent
            public void onLivingDeath(LivingDeathEvent event) {
                ModForgeEvents.onLivingDeath(event);
            }

            @SubscribeEvent
            public void onLivingHurt(LivingHurtEvent event) {
                ModForgeEvents.onLivingHurt(event);
            }

            @SubscribeEvent
            public void onPlayerTick(TickEvent.PlayerTickEvent event) {
                ModForgeEvents.onPlayerTick(event);
            }

            @SubscribeEvent
            public void onAttachCapabilities(AttachCapabilitiesEvent<Entity> event) {
                if (event.getObject() instanceof PlayerEntity) {
                    PLOGGER.debug("Fuck onAttachCapabilities");
                    // 为玩家实体附加能力
                    event.addCapability(new ResourceLocation(AA_MbvdMod.MOD_ID, "showing_evidence_data"),
                            new IShowingEvidenceData.Provider());

                    if (!event.getObject().getCapability(Capabilities.COURT_RECORD_CAPABILITY).isPresent()) {
                        event.addCapability(new ResourceLocation(AA_MbvdMod.MOD_ID, "court_record_inventory"),
                                new CourtRecordProvider());
                    }

                }
            }
        });
        
        // 显式注册按键事件监听器
        PLOGGER.info("[DEBUG] Registering KeyInputHandler");
        MinecraftForge.EVENT_BUS.register(KeyInputHandler.class);
    }

    private void setup(final FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            MinecraftEvidences.register();
            GeckoLib.initialize();
        });
    }

    private void doClientStuff(final FMLClientSetupEvent event) {
        ModKeyBindings.register(event);

        // 添加这一行来注册容器屏幕
        event.enqueueWork(() -> {
            ScreenManager.registerFactory(
                    ModContainerTypes.COURTRECORDS_CONTAINER.get(),
                    CourtRecordScreen::new
            );
        });

        RenderingRegistry.registerEntityRenderingHandler(ModEntityTypes.BADGE.get(),
                manager -> new SpriteRenderer<>(manager, Minecraft.getInstance().getItemRenderer()));

        RenderingRegistry.registerEntityRenderingHandler(ModEntityTypes.PhoenixWright.get(),
                PhoenixWrightRenderer::new);
    }


    private void enqueueIMC(final InterModEnqueueEvent event) {
        // 注册第一个 CHARM 槽位
        InterModComms.sendTo(CuriosApi.MODID, SlotTypeMessage.REGISTER_TYPE,
                () -> new SlotTypeMessage.Builder("charm_mayas_magatama").build());

        // 注册第二个 CHARM 槽位
        InterModComms.sendTo(CuriosApi.MODID, SlotTypeMessage.REGISTER_TYPE,
                () -> new SlotTypeMessage.Builder("charm_badge").build());
    }


    private void processIMC(final InterModProcessEvent event) {
    }

    @SubscribeEvent
    public void onServerStarting(FMLServerStartingEvent event) {
    }

    @Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class RegistryEvents {
        @SubscribeEvent
        public static void onBlocksRegistry(final RegistryEvent.Register<Block> blockRegistryEvent) {
            PLOGGER.info("HELLO from Register Block");
        }
    }
}