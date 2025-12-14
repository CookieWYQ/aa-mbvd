package com.cookiewyq.aa_mbvd.util;

import com.cookiewyq.aa_mbvd.events.ModTickEvents;
import com.cookiewyq.aa_mbvd.items.custom.badges.AttorneysBadge;
import com.cookiewyq.aa_mbvd.items.custom.badges.Prosbadge;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.util.Direction;
import net.minecraft.util.math.*;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.type.inventory.ICurioStacksHandler;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class tools {
    /**
     * 检查当前是否为第一人称视角
     *
     * @param mc Minecraft客户端实例
     * @return 如果是第一人称视角返回true，否则返回false
     */
    public static boolean isFirstPersonCamera(Minecraft mc) {
        int perspective = mc.gameSettings.getPointOfView().ordinal();
        return perspective == tool_enums.CameraMode.FIRST_PERSON.getValue();
    }

    /**
     * 获取当前相机模式
     *
     * @param mc Minecraft客户端实例
     * @return 当前相机模式枚举值
     */
    public static tool_enums.CameraMode getCameraMode(Minecraft mc) {
        int perspective = mc.gameSettings.getPointOfView().ordinal();
        return tool_enums.CameraMode.fromValue(perspective);
    }

    /**
     * 获取玩家面向的主要方向
     *
     * @param player 玩家实体
     * @return 玩家面向的主要方向
     */
    public Direction getPlayerCardinalDirection(PlayerEntity player) {
        return Direction.getFacingDirections(player)[0];
    }

    /**
     * 根据玩家的视角方向获取视线在X、Y、Z轴上的单位增量，支持对角线方向
     *
     * @param player          玩家实体
     * @param useCardinalOnly 是否只使用主要方向（东南西北上下），false时支持对角线方向
     * @return 包含X、Y、Z轴单位增量的数组，格式为 [xUnit, yUnit, zUnit]
     */
    public static double[] getPlayerViewDirectionUnits(PlayerEntity player, boolean useCardinalOnly) {
        // 获取玩家的视线向量
        Vector3d lookVec = player.getLook(1.0f);

        // 取得X、Y、Z分量
        double x = lookVec.x;
        double y = lookVec.y;
        double z = lookVec.z;

        // 如果只使用主要方向
        if (useCardinalOnly) {
            double absX = Math.abs(x);
            double absY = Math.abs(y);
            double absZ = Math.abs(z);

            // 找到最大的分量
            if (absX >= absY && absX >= absZ) {
                return new double[]{x > 0 ? 1.0 : -1.0, 0.0, 0.0};
            } else if (absY >= absX && absY >= absZ) {
                return new double[]{0.0, y > 0 ? 1.0 : -1.0, 0.0};
            } else {
                return new double[]{0.0, 0.0, z > 0 ? 1.0 : -1.0};
            }
        }

        // 处理对角线方向
        double threshold = 0.3826834323650898; // cos(67.5°) ≈ 0.3827

        // 标准化向量
        double length = Math.sqrt(x * x + y * y + z * z);
        if (length > 0) {
            x /= length;
            y /= length;
            z /= length;
        }

        // 确定主要方向分量
        double xUnit = 0.0;
        double yUnit = 0.0;
        double zUnit = 0.0;

        // X轴方向
        if (Math.abs(x) > threshold) {
            xUnit = x > 0 ? 1.0 : -1.0;
        }

        // Y轴方向
        if (Math.abs(y) > threshold) {
            yUnit = y > 0 ? 1.0 : -1.0;
        }

        // Z轴方向
        if (Math.abs(z) > threshold) {
            zUnit = z > 0 ? 1.0 : -1.0;
        }

        return new double[]{xUnit, yUnit, zUnit};
    }

    /**
     * 根据玩家的视角方向获取视线在X、Y、Z轴上的单位增量，支持对角线方向（简化版本）
     *
     * @param player 玩家实体
     * @return 包含X、Y、Z轴单位增量的数组，格式为 [xUnit, yUnit, zUnit]
     */
    public static double[] getPlayerViewDirectionUnits(PlayerEntity player) {
        // 获取玩家的视线向量
        Vector3d lookVec = player.getLook(1.0f);

        // 取得X、Y、Z分量
        double x = lookVec.x;
        double y = lookVec.y;
        double z = lookVec.z;

        // 标准化向量
        double length = Math.sqrt(x * x + y * y + z * z);
        if (length > 0) {
            x /= length;
            y /= length;
            z /= length;
        }

        // 使用阈值确定方向分量（22.5°的余弦值）
        double threshold = 0.3826834323650898; // cos(67.5°)

        double xUnit = 0.0;
        double yUnit = 0.0;
        double zUnit = 0.0;

        // X轴方向分量
        if (Math.abs(x) > threshold) {
            xUnit = x > 0 ? 1.0 : -1.0;
        }

        // Y轴方向分量
        if (Math.abs(y) > threshold) {
            yUnit = y > 0 ? 1.0 : -1.0;
        }

        // Z轴方向分量
        if (Math.abs(z) > threshold) {
            zUnit = z > 0 ? 1.0 : -1.0;
        }

        return new double[]{xUnit, yUnit, zUnit};
    }

    /**
     * 获取玩家视角的精确方向向量（单位向量）
     *
     * @param player 玩家实体
     * @return 包含X、Y、Z轴单位向量的数组，格式为 [xUnit, yUnit, zUnit]
     */
    public static double[] getPlayerViewDirectionVector(PlayerEntity player) {
        // 获取玩家的视线向量
        Vector3d lookVec = player.getLook(1.0f);

        // 取得X、Y、Z分量
        double x = lookVec.x;
        double y = lookVec.y;
        double z = lookVec.z;

        // 标准化为单位向量
        double length = Math.sqrt(x * x + y * y + z * z);
        if (length > 0) {
            x /= length;
            y /= length;
            z /= length;
        } else {
            // 如果向量为零，默认朝北
            x = 0.0;
            y = 0.0;
            z = -1.0;
        }

        return new double[]{x, y, z};
    }

    /**
     * 获取玩家视角的水平方向单位向量（忽略Y轴），支持对角线方向
     *
     * @param player 玩家实体
     * @return 包含X、Z轴单位向量的数组，格式为 [xUnit, zUnit]
     */
    public static double[] getPlayerHorizontalDirectionVector(PlayerEntity player) {
        // 获取玩家的视线向量
        Vector3d lookVec = player.getLook(1.0f);

        // 只取水平分量（X和Z）
        double x = lookVec.x;
        double z = lookVec.z;

        // 标准化为单位向量（仅水平方向）
        double length = Math.sqrt(x * x + z * z);
        if (length > 0) {
            x /= length;
            z /= length;
        } else {
            // 如果没有水平分量（直视上下），默认朝北
            x = 0.0;
            z = -1.0;
        }

        // 使用阈值确定方向分量（22.5°的余弦值）
        double threshold = 0.3826834323650898; // cos(67.5°)

        double xUnit = 0.0;
        double zUnit = 0.0;

        // X轴方向分量
        if (Math.abs(x) > threshold) {
            xUnit = x > 0 ? 1.0 : -1.0;
        }

        // Z轴方向分量
        if (Math.abs(z) > threshold) {
            zUnit = z > 0 ? 1.0 : -1.0;
        }

        return new double[]{xUnit, zUnit};
    }

    /**
     * 获取玩家视角的垂直分量
     *
     * @param player 玩家实体
     * @return Y轴方向的单位向量值
     */
    public static double getPlayerVerticalDirectionComponent(PlayerEntity player) {
        // 获取玩家的视线向量
        Vector3d lookVec = player.getLook(1.0f);

        // 取得Y分量
        double y = lookVec.y;

        // 标准化向量
        double length = Math.sqrt(lookVec.x * lookVec.x + y * y + lookVec.z * lookVec.z);
        if (length > 0) {
            y /= length;
        }

        return y;
    }

    /**
     * 获取玩家正在看向的实体
     *
     * @param player   玩家实体
     * @param distance 检测距离
     * @return 玩家正在看向的实体，如果未看向任何实体则返回null
     */
    @Nullable
    public static Entity getEntityPlayerIsLookingAt(PlayerEntity player, double distance) {
        RayTraceResult result = player.pick(distance, 1.0F, false);
        if (result.getType() == RayTraceResult.Type.ENTITY) {
            EntityRayTraceResult entityResult = (EntityRayTraceResult) result;
            return entityResult.getEntity();
        }
        return null;
    }

    /**
     * 计算射线与包围盒的交点
     *
     * @param rayOrigin    射线起点
     * @param rayDirection 射线方向
     * @param box          包围盒
     * @return 交点坐标，如果没有交点则返回null
     */
    @Nullable
    private static Vector3d calculateRayBoxIntersection(Vector3d rayOrigin, Vector3d rayDirection, AxisAlignedBB box) {
        // 射线方向单位化
        Vector3d dir = rayDirection.normalize();

        // 计算与包围盒六个面的交点
        double tMin = (box.minX - rayOrigin.x) / dir.x;
        double tMax = (box.maxX - rayOrigin.x) / dir.x;

        if (tMin > tMax) {
            double temp = tMin;
            tMin = tMax;
            tMax = temp;
        }

        double tyMin = (box.minY - rayOrigin.y) / dir.y;
        double tyMax = (box.maxY - rayOrigin.y) / dir.y;

        if (tyMin > tyMax) {
            double temp = tyMin;
            tyMin = tyMax;
            tyMax = temp;
        }

        if ((tMin > tyMax) || (tyMin > tMax)) {
            return null;
        }

        if (tyMin > tMin) {
            tMin = tyMin;
        }

        if (tyMax < tMax) {
            tMax = tyMax;
        }

        double tzMin = (box.minZ - rayOrigin.z) / dir.z;
        double tzMax = (box.maxZ - rayOrigin.z) / dir.z;

        if (tzMin > tzMax) {
            double temp = tzMin;
            tzMin = tzMax;
            tzMax = temp;
        }

        if ((tMin > tzMax) || (tzMin > tMax)) {
            return null;
        }

        if (tzMin > tMin) {
            tMin = tzMin;
        }

        if (tzMax < tMax) {
            tMax = tzMax;
        }

        // 返回最近的交点
        if (tMin >= 0) {
            return rayOrigin.add(dir.scale(tMin));
        }

        if (tMax >= 0) {
            return rayOrigin.add(dir.scale(tMax));
        }

        return null;
    }


    /**
     * 获取玩家正在看向的方块位置
     *
     * @param player   玩家实体
     * @param distance 检测距离
     * @return 玩家正在看向的方块位置，如果未看向任何方块则返回null
     */
    @Nullable
    public static BlockPos getBlockPlayerIsLookingAt(PlayerEntity player, double distance) {
        RayTraceResult result = player.pick(distance, 1.0F, false);
        if (result.getType() == RayTraceResult.Type.BLOCK) {
            BlockRayTraceResult blockResult = (BlockRayTraceResult) result;
            return blockResult.getPos();
        }
        return null;
    }

    /**
     * 获取玩家视线终点坐标
     *
     * @param player   玩家实体
     * @param distance 视线距离
     * @return 视线终点坐标
     */
    public static Vector3d getPlayerLookEndPosition(PlayerEntity player, double distance) {
        RayTraceResult result = player.pick(distance, 1.0F, false);
        return result.getHitVec();
    }

    /**
     * 获取玩家眼睛位置
     *
     * @param player 玩家实体
     * @return 玩家眼睛位置坐标
     */
    public static Vector3d getPlayerEyePosition(PlayerEntity player) {
        return player.getEyePosition(1.0F);
    }

    /**
     * 使用Minecraft客户端的原版视线检测
     *
     * @param player 玩家实体
     * @return 玩家正在看向的实体
     */
    @Nullable
    public static Entity getEntityPlayerIsLookingAtVanilla(PlayerEntity player) {
        double distance = 64.0D;

        // 使用原版方法
        Vector3d eyePosition = player.getEyePosition(1.0F);
        Vector3d viewVector = player.getLook(1.0F);
        Vector3d traceEnd = eyePosition.add(viewVector.x * distance, viewVector.y * distance, viewVector.z * distance);

        // 创建实体检测范围
        AxisAlignedBB searchBox = player.getBoundingBox().expand(viewVector.scale(distance)).grow(1.0D);

        // 获取范围内的所有实体
        List<Entity> entities = player.world.getEntitiesInAABBexcluding(
                player,
                searchBox,
                (entity) -> !entity.isSpectator() && entity.canBeCollidedWith()
        );

        Entity closestEntity = null;
        double closestDistance = distance;

        for (Entity entity : entities) {
            AxisAlignedBB entityBox = entity.getBoundingBox().grow(entity.getCollisionBorderSize());
            Optional<Vector3d> traceResult = entityBox.rayTrace(eyePosition, traceEnd);

            if (traceResult.isPresent()) {
                double hitDistance = eyePosition.distanceTo(new Vector3d(traceResult.get().getX(), traceResult.get().getY(), traceResult.get().getZ()));
                if (hitDistance < closestDistance) {
                    closestEntity = entity;
                    closestDistance = hitDistance;
                }
            }
        }

        return closestEntity;
    }

    /**
     * 强制检测玩家附近所有实体并选择最近的
     *
     * @param player 玩家实体
     * @return 最近的实体
     */
    @Nullable
    public static Entity getNearestEntityToPlayerCrosshair(PlayerEntity player) {
        double maxDistance = 64.0D;
        Vector3d eyePos = player.getEyePosition(1.0F);
        Vector3d lookVec = player.getLook(1.0F);

        List<Entity> entities = player.world.getEntitiesInAABBexcluding(
                player,
                player.getBoundingBox().grow(maxDistance),
                entity -> entity.isAlive() && entity != player
        );

        Entity targetEntity = null;
        double minAngle = Double.MAX_VALUE;

        for (Entity entity : entities) {
            Vector3d entityPos = entity.getPositionVec();
            Vector3d toEntity = entityPos.subtract(eyePos).normalize();

            // 计算视线向量与到实体向量之间的夹角
            double dotProduct = lookVec.dotProduct(toEntity);
            double angle = Math.acos(Math.max(-1.0, Math.min(1.0, dotProduct)));

            // 如果夹角小于60度（在视线范围内）且更接近中心线
            if (angle < Math.toRadians(60) && angle < minAngle) {
                targetEntity = entity;
                minAngle = angle;
            }
        }

        return targetEntity;
    }

//    @Nullable
//    public static PlayerEntity getPlayerEntityFromUUID(UUID uuid) {
//        if (Minecraft.getInstance().world != null) {
//            for (PlayerEntity player : Minecraft.getInstance().world.getPlayers()) {
//                if (player.getUniqueID().equals(uuid)) {
//                    return player;
//                }
//            }
//        }
//        return null;
//    }

    @Nullable
    public static PlayerEntity getPlayerEntityFromUUIDByVanilla(UUID uuid) {
        if (Minecraft.getInstance().world != null) {
            return Minecraft.getInstance().world.getPlayerByUuid(uuid);
        }
        return null;
    }

    @Nullable
    public static PlayerEntity getPlayerEntityFromUUIDByTickEvent(UUID uuid) {
        return ModTickEvents.getPlayerByUUID(uuid);
    }

    /**
     * 获取玩家视线方向上最近的实体（精确版本）
     *
     * @param player 玩家实体
     * @return 玩家视线方向上最近的实体，如果没有则返回null
     */
    @Nullable
    public static Entity getEntityPlayerIsLookingAt_usable(PlayerEntity player) {
        // 使用更直接的方式检测玩家视线中的实体
        double reachDistance = 5.0D;
        Vector3d eyePosition = player.getEyePosition(1.0F);
        Vector3d lookVec = player.getLookVec();
        Vector3d endPosition = eyePosition.add(lookVec.x * reachDistance, lookVec.y * reachDistance, lookVec.z * reachDistance);

        AxisAlignedBB searchBox = new AxisAlignedBB(eyePosition, endPosition).grow(1.0D);
        EntityRayTraceResult entityTraceResult = ProjectileHelper.rayTraceEntities(
                player.world,
                player,
                eyePosition,
                endPosition,
                searchBox,
                entity -> entity instanceof LivingEntity && entity != player
        );
        if (entityTraceResult != null) {
            return entityTraceResult.getEntity();
        }
        return null;
    }

    /**
     * 重命名物品栈
     *
     * @param itemStack 要重命名的物品栈
     * @param newName   新的显示名称
     **/
    public static void renameItemStack(ItemStack itemStack, ITextComponent newName) {
        // 直接设置显示名称
        itemStack.setDisplayName(newName);
    }

    /**
     * 获取实体的默认显示名称
     *
     * @param entity 要获取显示名称的实体
     * @return 显示名称
     **/
    public static TranslationTextComponent getTranslatedEntityName(Entity entity) {
        return new TranslationTextComponent(entity.getType().getTranslationKey());
    }

    /**
     * 获取指定插槽的指定物品
     *
     * @param player          玩家
     * @param slotIdentifier  插槽标识符
     * @param index           物品索引
     * @return 指定插槽的指定物品
     */
    public static ItemStack getCurioItemFromSlot(PlayerEntity player, String slotIdentifier, int index) {
        return CuriosApi.getCuriosHelper().getCuriosHandler(player).map(handler -> {
            ICurioStacksHandler stacksHandler = handler.getCurios().get(slotIdentifier);
            if (stacksHandler != null && index < stacksHandler.getSlots()) {
                return stacksHandler.getStacks().getStackInSlot(index);
            }
            return ItemStack.EMPTY;
        }).orElse(ItemStack.EMPTY);
    }

    public static int getWornCuriosType(PlayerEntity player) {
        return CuriosApi.getCuriosHelper().getEquippedCurios(player)
                .map(handler -> {
                    for (int i = 0; i < handler.getSlots(); i++) {
                        ItemStack stack = handler.getStackInSlot(i);
                        if (stack.getItem() instanceof AttorneysBadge) {
                            return 1;
                        } else if (stack.getItem() instanceof Prosbadge) {
                            return 2;
                        }
                    }
                    return -1;
                })
                .orElse(-1);
    }

    public static boolean hasWornCurios(PlayerEntity player) {
        return CuriosApi.getCuriosHelper().getEquippedCurios(player)
                .map(handler -> {
                    for (int i = 0; i < handler.getSlots(); i++) {
                        ItemStack stack = handler.getStackInSlot(i);
                        if (stack.getItem() instanceof AttorneysBadge || stack.getItem() instanceof Prosbadge) {
                            return true;
                        }
                    }
                    return false;
                })
                .orElse(false);
    }

    public static CompoundNBT writeToNBT(TranslationTextComponent component) {
        CompoundNBT nbt = new CompoundNBT();
        nbt.putString("key", component.getKey());

        // 保存格式化参数
        ListNBT argsList = new ListNBT();
        for (Object arg : component.getFormatArgs()) {
            CompoundNBT argNBT = new CompoundNBT();
            if (arg instanceof ITextComponent) {
                argNBT.putString("type", "text_component");
                argNBT.putString("value", ITextComponent.Serializer.toJson(((ITextComponent) arg)));
            } else if (arg != null) {
                argNBT.putString("type", "string");
                argNBT.putString("value", arg.toString());
            } else {
                argNBT.putString("type", "null");
            }
            argsList.add(argNBT);
        }
        nbt.put("formatArgs", argsList);

        return nbt;
    }


    public static TranslationTextComponent readFromNBT(CompoundNBT nbt) {
        String key = nbt.getString("key");

        ListNBT argsList = nbt.getList("formatArgs", 10);
        Object[] formatArgs = new Object[argsList.size()];

        for (int i = 0; i < argsList.size(); i++) {
            CompoundNBT argNBT = argsList.getCompound(i);
            String type = argNBT.getString("type");

            switch (type) {
                case "text_component":
                    try {
                        formatArgs[i] = ITextComponent.Serializer.getComponentFromJson(argNBT.getString("value"));
                    } catch (Exception e) {
                        formatArgs[i] = new StringTextComponent("Error loading component");
                    }
                    break;
                case "string":
                    formatArgs[i] = argNBT.getString("value");
                    break;
                case "null":
                    formatArgs[i] = null;
                    break;
            }
        }

        return new TranslationTextComponent(key, formatArgs);
    }

}
