package com.cookiewyq.aa_mbvd.entities.custom;

import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.util.HandSide;
import net.minecraft.world.World;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.IAnimationTickable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Collections;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class PhoenixWrightEntity extends LivingEntity implements IAnimatable, IAnimationTickable {
    private final AnimationFactory factory = new AnimationFactory(this);

    // 动画状态管理
    private String currentAnimation = "phoenix_wright_thinking";
    private boolean shouldPlayAnimation = false;

    public PhoenixWrightEntity(EntityType<? extends PhoenixWrightEntity> type, World world) {
        super(type, world);
    }

    public static AttributeModifierMap.MutableAttribute setCustomAttributes() {
        return MobEntity.func_233666_p_()
                .createMutableAttribute(Attributes.MAX_HEALTH, 20.0D)
                .createMutableAttribute(Attributes.MOVEMENT_SPEED, 0.5D)
                .createMutableAttribute(Attributes.ATTACK_DAMAGE, 3.0D);
    }

    @Override
    public void registerControllers(final AnimationData data) {
        data.addAnimationController(new AnimationController<>(this, "controller", 0, this::predicate));
    }

    private <E extends IAnimatable> PlayState predicate(AnimationEvent<E> event) {
        if (shouldPlayAnimation) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation(currentAnimation, false));
            shouldPlayAnimation = false;
            return PlayState.CONTINUE;
        }

        // 默认播放思考动画
        event.getController().setAnimation(new AnimationBuilder().addAnimation("phoenix_wright_thinking", true));
        return PlayState.CONTINUE;
    }

    // 公共方法用于触发动画
    public void triggerAnimation(String animationName) {
        this.currentAnimation = animationName;
        this.shouldPlayAnimation = true;
    }

    // 通过枚举触发动画
    public enum PhoenixWrightAnimations {
        OBJECTION("phoenix_wright_objection"),
        HOLDIT("phoenix_wright_holdit"),
        THINKING("phoenix_wright_thinking");

        private final String id;

        PhoenixWrightAnimations(String id) {
            this.id = id;
        }

        public String getId() {
            return id;
        }
    }

    public void triggerAnimation(PhoenixWrightAnimations animation) {
        triggerAnimation(animation.getId());
    }

    @Override
    public AnimationFactory getFactory() {
        return this.factory;
    }

    @Override
    public Iterable<ItemStack> getArmorInventoryList() {
        return Collections.singleton(ItemStack.EMPTY);
    }

    @Override
    public ItemStack getItemStackFromSlot(EquipmentSlotType equipmentSlotType) {
        return ItemStack.EMPTY;
    }

    @Override
    public void setItemStackToSlot(EquipmentSlotType equipmentSlotType, ItemStack itemStack) {
        // 空实现
    }

    @Override
    public HandSide getPrimaryHand() {
        return HandSide.LEFT;
    }

    @Override
    public int tickTimer() {
        return this.ticksExisted;
    }

    // 测试方法：在实体被攻击时播放 objection 动画
    @Override
    public boolean attackEntityFrom(net.minecraft.util.DamageSource source, float amount) {
        triggerAnimation(PhoenixWrightAnimations.OBJECTION);
        return super.attackEntityFrom(source, amount);
    }

    // 测试方法：定期播放不同动画
    @Override
    public void tick() {
        super.tick();

        // 仅在服务端执行，避免客户端和服务端不同步
        if (!this.world.isRemote && this.ticksExisted % 200 == 0) { // 每10秒
            int animIndex = (this.ticksExisted / 200) % 3;
            switch (animIndex) {
                case 0:
                    triggerAnimation(PhoenixWrightAnimations.OBJECTION);
                    break;
                case 1:
                    triggerAnimation(PhoenixWrightAnimations.HOLDIT);
                    break;
                case 2:
                    triggerAnimation(PhoenixWrightAnimations.THINKING);
                    break;
            }
        }
    }
}
