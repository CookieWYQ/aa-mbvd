package com.cookiewyq.aa_mbvd.entities.custom;

import com.cookiewyq.aa_mbvd.entities.ModEntityTypes;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.monster.BlazeEntity;
import net.minecraft.entity.projectile.ProjectileItemEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.particles.IParticleData;
import net.minecraft.particles.ItemParticleData;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class ModThrowableEntity extends ProjectileItemEntity {
    private static final DataParameter<ItemStack> ITEM = EntityDataManager.createKey(ModThrowableEntity.class, DataSerializers.ITEMSTACK);

    public ModThrowableEntity(EntityType<? extends ModThrowableEntity> entityType, World world) {
        super(entityType, world);
    }

    public ModThrowableEntity(World worldIn, LivingEntity throwerIn) {
        super(ModEntityTypes.BADGE.get(), throwerIn, worldIn);
    }

    public ModThrowableEntity(World worldIn, double x, double y, double z) {
        super(ModEntityTypes.BADGE.get(), x, y, z, worldIn);
    }

    @Override
    protected void registerData() {
        super.registerData();
        this.dataManager.register(ITEM, ItemStack.EMPTY);
    }

    public void setItem(ItemStack stack) {
        this.dataManager.set(ITEM, stack);
    }

    @Override
    protected Item getDefaultItem() {
        ItemStack stack = this.dataManager.get(ITEM);
        return stack.isEmpty() ? Items.SNOWBALL : stack.getItem();
    }

    @Override
    public ItemStack getItem() {
        ItemStack stack = this.dataManager.get(ITEM);
        return stack.isEmpty() ? new ItemStack(this.getDefaultItem()) : stack;
    }

    @Override
    public void writeAdditional(CompoundNBT compound) {
        super.writeAdditional(compound);
        ItemStack stack = this.dataManager.get(ITEM);
        if (!stack.isEmpty()) {
            compound.put("Item", stack.write(new CompoundNBT()));
        }
    }

    @Override
    public void readAdditional(CompoundNBT compound) {
        super.readAdditional(compound);
        ItemStack stack = ItemStack.read(compound.getCompound("Item"));
        this.dataManager.set(ITEM, stack);
    }

    @Override
    public IPacket<?> createSpawnPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    public void callSupperOnImpact(RayTraceResult result) {
        super.onImpact(result);
    }

    @OnlyIn(Dist.CLIENT)
    private IParticleData makeParticle() {
        ItemStack itemstack = this.getItem();
        return itemstack.isEmpty() ? ParticleTypes.ITEM_SNOWBALL : new ItemParticleData(ParticleTypes.ITEM, itemstack);
    }

    /**
     * Handler for {@link World#setEntityState}
     */
    @OnlyIn(Dist.CLIENT)
    @Override
    public void handleStatusUpdate(byte id) {
        if (id == 3) {
            IParticleData iparticledata = this.makeParticle();

            for(int i = 0; i < 8; ++i) {
                this.world.addParticle(iparticledata, this.getPosX(), this.getPosY(), this.getPosZ(), 0.0D, 0.0D, 0.0D);
            }
        }
    }

    /**
     * Called when the arrow hits an entity
     */
    @Override
    protected void onEntityHit(EntityRayTraceResult result) {
        super.onEntityHit(result);
        Entity entity = result.getEntity();
        int i = entity instanceof BlazeEntity ? 3 : 0;
        entity.attackEntityFrom(DamageSource.causeThrownDamage(this, this.getShooter()), (float)i);
    }

    /**
     * Called when this EntityFireball hits a block or entity.
     */
    @Override
    protected void onImpact(RayTraceResult result) {
        super.onImpact(result);
        if (!this.world.isRemote) {
            this.world.setEntityState(this, (byte)3);
            this.remove();
        }
    }
}
