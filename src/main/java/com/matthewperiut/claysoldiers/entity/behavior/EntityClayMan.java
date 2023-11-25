package com.matthewperiut.claysoldiers.entity.behavior;

import com.matthewperiut.claysoldiers.item.ItemClayMan;
import com.matthewperiut.claysoldiers.item.ItemListener;
import com.matthewperiut.claysoldiers.util.ClientUtil;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.pathing.EntityPath;
import net.minecraft.entity.block.ChestBlockEntity;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.food.FoodItem;
import net.minecraft.util.io.CompoundTag;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.modificationstation.stationapi.api.network.packet.MessagePacket;
import net.modificationstation.stationapi.api.server.entity.HasTrackingParameters;
import net.modificationstation.stationapi.api.server.entity.MobSpawnDataProvider;
import net.modificationstation.stationapi.api.util.Identifier;

import java.util.Arrays;
import java.util.List;

import static com.matthewperiut.claysoldiers.ClaySoldiersMod.MODID;

@HasTrackingParameters(trackingDistance = 160, updatePeriod = 2)
public class EntityClayMan extends AbstractAnimalEntity implements MobSpawnDataProvider {
    public int clayTeam;
    public int weaponPoints;
    public int armorPoints;
    public int foodLeft;
    public int sugarTime;
    public int resPoints;
    public int entCount;
    public int strikeTime;
    public int climbTime;
    public int gooTime;
    public int smokeTime;
    public int gooStock;
    public int smokeStock;
    public int logs;
    public int rocks;
    public int blockX;
    public int blockY;
    public int blockZ;
    public int throwTime;
    public float swingLeft;
    public boolean gunpowdered;
    public boolean king;
    public boolean glowing;
    public boolean isSwinging;
    public boolean stickSharp;
    public boolean armorPadded;
    public boolean heavyCore;
    public boolean isSwingingLeft;
    public Entity targetFollow;
    public Entity killedByPlayer;

    public EntityClayMan(World world) {
        super(world);
        this.health = 20;
        this.clayTeam = 0;
        this.standingEyeHeight = 0.0F;
        this.field_1641 = 0.1F;
        this.movementSpeed = 0.3F;
        this.setSize(0.15F, 0.4F);
        this.setPosition(this.x, this.y, this.z);
        this.texture = this.clayManTexture(0);
        this.renderDistanceMultiplier = 5.0;
    }

    public EntityClayMan(World world, double x, double y, double z, int i) {
        super(world);
        this.health = 20;
        this.clayTeam = i;
        this.standingEyeHeight = 0.0F;
        this.field_1641 = 0.1F;
        this.movementSpeed = 0.3F;
        this.setSize(0.15F, 0.4F);
        this.setPosition(x, y, z);
        this.texture = this.clayManTexture(i);
        this.renderDistanceMultiplier = 5.0;
        this.world.playSound(this, "step.gravel", 0.8F, ((this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F) * 0.9F);
    }

    public String clayManTexture(int i) {
        String joe = "claysoldiers:textures/entities/clay";
        if (i == 0) {
            joe = joe + "Grey";
        } else if (i == 1) {
            joe = joe + "Red";
        } else if (i == 2) {
            joe = joe + "Yellow";
        } else if (i == 3) {
            joe = joe + "Green";
        } else if (i == 4) {
            joe = joe + "Blue";
        } else if (i == 5) {
            joe = joe + "Orange";
        } else {
            joe = joe + "Purple";
        }

        return joe + ".png";
    }

    public int teamCloth(int teamNum) {
        if (teamNum == 0) {
            return 8;
        } else if (teamNum == 1) {
            return 14;
        } else if (teamNum == 2) {
            return 4;
        } else if (teamNum == 3) {
            return 13;
        } else if (teamNum == 4) {
            return 11;
        } else {
            return teamNum == 5 ? 1 : 10;
        }
    }

    public int teamDye(int teamNum) {
        if (teamNum == 0) {
            return 8;
        } else if (teamNum == 1) {
            return 1;
        } else if (teamNum == 2) {
            return 11;
        } else if (teamNum == 3) {
            return 2;
        } else if (teamNum == 4) {
            return 4;
        } else {
            return teamNum == 5 ? 14 : 5;
        }
    }

    @Override
    public void tickHandSwing() {
        super.tickHandSwing();
        if (this.strikeTime > 0) {
            --this.strikeTime;
        }

        if (this.sugarTime > 0) {
            this.movementSpeed = 0.6F + (this.entity == null && this.targetFollow == null ? 0.0F : 0.15F);
            --this.sugarTime;
        } else {
            this.movementSpeed = 0.3F + (this.entity == null && this.targetFollow == null ? 0.0F : 0.15F);
        }

        if (this.method_1393()) {
            this.jumping = true;
        }

        int i;
        double frogman;
        double aaa;
        if (this.foodLeft > 0 && this.health <= 15 && this.health > 0) {
            for (i = 0; i < 12; ++i) {
                frogman = this.x + (double) (this.rand.nextFloat() - this.rand.nextFloat()) * 0.125;
                double b = this.y + 0.25 + (double) (this.rand.nextFloat() - this.rand.nextFloat()) * 0.125;
                aaa = this.z + (double) (this.rand.nextFloat() - this.rand.nextFloat()) * 0.125;
                ClientUtil.addPoofParticle(this.world, frogman, b, aaa, Item.RAW_PORKCHOP);
            }

            this.health += 15;
            --this.foodLeft;
        }

        if (this.onGround) {
            this.climbTime = 10;
        }

        if (this.smokeTime > 0) {
            --this.smokeTime;
        }

        if (this.throwTime > 0) {
            --this.throwTime;
        }

        if (this.gooTime > 0) {
            int j;
            this.xVelocity = 0.0;
            this.yVelocity = 0.0;
            this.zVelocity = 0.0;
            this.forwardVelocity = 0.0F;
            this.horizontalVelocity = 0.0F;
            this.jumping = false;
            this.movementSpeed = 0.0F;
            --this.gooTime;
            i = MathHelper.floor(this.x);
            j = MathHelper.floor(this.boundingBox.minY - 1.0);
            int k = MathHelper.floor(this.z);
            j = this.world.getBlockId(i, j, k);
            if (j > 0 && j < 128 && (j == 0 || Block.BY_ID[j].getCollisionShape(this.world, i, j, k) == null)) {
                this.gooTime = 0;
            }
        }

        if (this.throwTime > 6) {
            this.movementSpeed = -this.movementSpeed;
        }

        ++this.entCount;
        if (this.entity != null && this.entity.removed) {
            this.entity = null;
            this.setTarget(null);
        } else if (this.entity != null && this.rand.nextInt(25) == 0 && ((double) this.distanceTo(this.entity) > 8.0 || !this.method_928(this.entity))) {
            this.entity = null;
            this.setTarget(null);
        }

        if (this.targetFollow != null && this.targetFollow.removed) {
            this.targetFollow = null;
            this.setTarget(null);
        } else if (this.targetFollow != null && this.rand.nextInt(25) == 0 && ((double) this.distanceTo(this.targetFollow) > 8.0 || !this.method_928(this.targetFollow))) {
            this.targetFollow = null;
            this.setTarget(null);
        }

        if (this.smokeTime <= 0 && this.entCount > 2 + this.rand.nextInt(2) && this.health > 0) {
            this.entCount = 0;
            List list = this.world.getEntities(this, this.boundingBox.expand(8.0, 5.0, 8.0));

            EntityClayMan ec;
            for (int j = 0; j < list.size(); ++j) {
                Entity entity = (Entity) list.get(j);
                // method_928 is something like this entity can see them
                if (entity instanceof EntityClayMan && this.rand.nextInt(3) == 0 && this.method_928(entity)) {
                    EntityClayMan clayman = (EntityClayMan) entity;
                    if (clayman.health > 0 && clayman.clayTeam != this.clayTeam && this.clayTeam > 0 && this.logs <= 0) {
                        if (clayman.king) {
                            if (this.entity == null || !(this.entity instanceof EntityClayMan)) {
                                this.entity = clayman;
                                break;
                            }

                            ec = (EntityClayMan) this.entity;
                            if (!ec.king) {
                                this.entity = clayman;
                                break;
                            }
                        } else if (this.entity == null) {
                            this.entity = clayman;
                            break;
                        }
                    } else if (clayman.health > 0 && this.targetFollow == null && this.entity == null && clayman.king && clayman.clayTeam == this.clayTeam && (double) this.distanceTo(clayman) > 3.0) {
                        this.targetFollow = clayman;
                        break;
                    }
                } else if (this.entity == null && entity instanceof MonsterEntity mob && this.method_928(entity)) {
                    if (mob.method_634() != null) {
                        this.entity = mob;
                        break;
                    }
                } else {
                    if (this.entity == null && this.targetFollow == null && !this.heavyCore && this.logs <= 0 && this.vehicle == null && entity instanceof EntityDirtHorse && entity.passenger == null && this.method_928(entity)) {
                        this.targetFollow = entity;
                        break;
                    }

                    if (this.entity == null && this.targetFollow == null && entity instanceof FishHookEntity && this.method_928(entity)) {
                        this.targetFollow = entity;
                        break;
                    }

                    if (this.entity == null && (this.targetFollow == null || this.targetFollow instanceof EntityClayMan) && entity instanceof ItemEntity item && this.method_928(entity)) {
                        if (item.stack != null) {
                            ItemStack stack = item.stack;
                            if (stack.count > 0) {
                                if (this.weaponPoints <= 0 && stack.itemId == Item.STICK.id) {
                                    this.targetFollow = item;
                                    break;
                                }

                                if (this.armorPoints <= 0 && stack.itemId == Item.LEATHER.id) {
                                    this.targetFollow = item;
                                    break;
                                }

                                if (this.rocks <= 0 && stack.itemId == Block.GRAVEL.id) {
                                    this.targetFollow = item;
                                    break;
                                }

                                if (!this.glowing && stack.itemId == Item.GLOWSTONE_DUST.id) {
                                    this.targetFollow = item;
                                    break;
                                }

                                if (!this.king && stack.itemId == Item.GOLD_INGOT.id) {
                                    boolean jack = false;
                                    List list2 = this.world.getEntities(this, this.boundingBox.expand(24.0, 16.0, 24.0));

                                    for (int k = 0; k < list2.size(); ++k) {
                                        Entity entity2 = (Entity) list2.get(k);
                                        if (entity2 instanceof EntityClayMan clayman) {
                                            if (clayman.clayTeam == this.clayTeam && clayman.king) {
                                                jack = true;
                                                break;
                                            }
                                        }
                                    }

                                    if (!jack) {
                                        this.targetFollow = item;
                                        break;
                                    }
                                } else {
                                    if (!this.gunpowdered && stack.itemId == Item.GUNPOWDER.id) {
                                        this.targetFollow = item;
                                        break;
                                    }

                                    if (this.sugarTime <= 0 && stack.itemId == Item.SUGAR.id) {
                                        this.targetFollow = item;
                                        break;
                                    }

                                    if (this.foodLeft <= 0 && stack.getItem() != null && stack.getItem() instanceof FoodItem) {
                                        this.targetFollow = item;
                                        break;
                                    }

                                    if (this.resPoints <= 0 && stack.itemId == Item.CLAY.id) {
                                        this.targetFollow = item;
                                        break;
                                    }

                                    if (this.gooStock <= 0 && stack.itemId == Item.SLIMEBALL.id) {
                                        this.targetFollow = item;
                                        break;
                                    }

                                    if (this.smokeStock <= 0 && stack.itemId == Item.REDSTONE_DUST.id) {
                                        this.targetFollow = item;
                                        break;
                                    }

                                    if (this.weaponPoints > 0 && !this.stickSharp && stack.itemId == Item.FLINT.id) {
                                        this.targetFollow = item;
                                        break;
                                    }

                                    if (this.armorPoints > 0 && !this.armorPadded && stack.itemId == Block.WOOL.id) {
                                        this.targetFollow = item;
                                        break;
                                    }

                                    if (!this.heavyCore && this.vehicle == null && stack.itemId == Item.IRON_INGOT.id) {
                                        this.targetFollow = item;
                                        break;
                                    }

                                    if (this.resPoints > 0 && stack.getItem() != null && stack.getItem() instanceof ItemClayMan ic) {
                                        if (ic.clayTeam == this.clayTeam) {
                                            this.targetFollow = item;
                                            break;
                                        }
                                    } else {
                                        if (stack.itemId == Item.DYE_POWDER.id && stack.getMeta() == this.teamDye(this.clayTeam)) {
                                            this.targetFollow = item;
                                            break;
                                        }

                                        if (stack.itemId == Block.LOG.id && this.vehicle == null) {
                                            int gottam = 0;
                                            if (this.logs < 20 && stack.count >= 5) {
                                                gottam = 1;
                                            }

                                            if (gottam > 0) {
                                                this.targetFollow = item;
                                                break;
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            if (this.entity != null) {
                if (this.strikeTime <= 0 && this.method_928(this.entity) && (double) this.distanceTo(this.entity) < (this.weaponPoints > 0 ? 1.0 : 0.7) + (double) this.rand.nextFloat() * 0.1) {
                    if (this.hitTargetMakesDead(this.entity)) {
                        this.entity = null;
                        this.setTarget(null);
                    }
                } else if (this.rocks > 0 && this.throwTime <= 0 && this.method_928(this.entity)) {
                    frogman = this.distanceTo(this.entity);
                    if (frogman >= 1.75 && frogman <= 7.0) {
                        --this.rocks;
                        this.throwTime = 20;
                        this.throwRockAtEnemy(this.entity);
                    }
                }
            } else if (this.targetFollow != null) {
                if (!this.method_633() || this.rand.nextInt(10) == 0) {
                    this.setTarget(this.world.findPathTo(this.targetFollow, this, 16.0F));
                }

                if (this.targetFollow instanceof ItemEntity item) {
                    if (item.stack != null && this.method_928(item) && (double) this.distanceTo(item) < 0.75) {
                        ItemStack stack = item.stack;
                        if (stack.count > 0) {
                            if (stack.itemId == Item.STICK.id) {
                                this.weaponPoints = 15;
                                this.stickSharp = false;
                                this.gotcha((ItemEntity) this.targetFollow);
                            } else if (stack.itemId == Item.LEATHER.id) {
                                this.armorPoints = 15;
                                this.armorPadded = false;
                                this.gotcha((ItemEntity) this.targetFollow);
                            } else if (stack.itemId == Block.GRAVEL.id) {
                                this.rocks = 15;
                                this.gotcha((ItemEntity) this.targetFollow);
                            } else if (stack.itemId == Item.GLOWSTONE_DUST.id) {
                                this.glowing = true;
                                this.gotcha((ItemEntity) this.targetFollow);
                            } else if (stack.itemId == Item.GOLD_INGOT.id) {
                                boolean jack = false;
                                List list2 = this.world.getEntities(this, this.boundingBox.expand(24.0, 16.0, 24.0));

                                for (int gottam = 0; gottam < list2.size(); ++gottam) {
                                    Entity entity2 = (Entity) list2.get(gottam);
                                    if (entity2 instanceof EntityClayMan clayman) {
                                        if (clayman.clayTeam == this.clayTeam && clayman.king) {
                                            jack = true;
                                            break;
                                        }
                                    }
                                }

                                if (!jack) {
                                    this.king = true;
                                    this.gotcha((ItemEntity) this.targetFollow);
                                    item.remove();
                                } else {
                                    this.targetFollow = null;
                                    this.setTarget(null);
                                }
                            } else if (stack.itemId == Item.GUNPOWDER.id) {
                                this.gunpowdered = true;
                                this.gotcha((ItemEntity) this.targetFollow);
                            } else if (stack.itemId == Item.SUGAR.id) {
                                this.sugarTime = 1200;
                                this.gotcha((ItemEntity) this.targetFollow);
                            } else if (stack.getItem() != null && stack.getItem() instanceof FoodItem) {
                                this.foodLeft = 4;
                                this.gotcha((ItemEntity) this.targetFollow);
                            } else if (stack.itemId == Item.CLAY.id) {
                                this.resPoints = 4;
                                this.gotcha((ItemEntity) this.targetFollow);
                            } else if (stack.itemId == Item.REDSTONE_DUST.id) {
                                this.smokeStock = 2;
                                this.gotcha((ItemEntity) this.targetFollow);
                            } else if (stack.itemId == Item.SLIMEBALL.id) {
                                this.gooStock = 2;
                                this.gotcha((ItemEntity) this.targetFollow);
                            } else if (stack.itemId == Item.IRON_INGOT.id) {
                                this.heavyCore = true;
                                this.gotcha((ItemEntity) this.targetFollow);
                            } else {
                                double a;
                                double b;
                                double c;
                                if (stack.itemId == Item.FLINT.id) {
                                    if (this.weaponPoints > 0) {
                                        this.stickSharp = true;

                                        for (int j = 0; j < 4; ++j) {
                                            a = this.x + (double) (this.rand.nextFloat() - this.rand.nextFloat()) * 0.125;
                                            b = this.boundingBox.minY + 0.125 + (double) (this.rand.nextFloat() - this.rand.nextFloat()) * 0.25;
                                            c = this.z + (double) (this.rand.nextFloat() - this.rand.nextFloat()) * 0.125;
                                            ClientUtil.addDiggingParticle(this.world, a, b, c, 0.0, 0.0, 0.0, Block.WOOD, 0, 0);
                                        }

                                        this.world.playSound(this, "random.wood click", 0.75F, 1.0F / (this.rand.nextFloat() * 0.2F + 0.9F));
                                    }

                                    this.targetFollow = null;
                                } else if (stack.itemId == Block.WOOL.id) {
                                    if (this.armorPoints > 0) {
                                        this.armorPadded = true;

                                        for (int j = 0; j < 4; ++j) {
                                            a = this.x + (double) (this.rand.nextFloat() - this.rand.nextFloat()) * 0.125;
                                            b = this.boundingBox.minY + 0.125 + (double) (this.rand.nextFloat() - this.rand.nextFloat()) * 0.25;
                                            c = this.z + (double) (this.rand.nextFloat() - this.rand.nextFloat()) * 0.125;
                                            ClientUtil.addDiggingParticle(this.world, a, b, c, 0.0, 0.0, 0.0, Block.WOOL, 0, 0);
                                        }

                                        this.world.playSound(this, "step.cloth", 0.75F, 1.0F / (this.rand.nextFloat() * 0.2F + 0.9F));
                                    }

                                    this.targetFollow = null;
                                } else if (stack.getItem() != null && stack.getItem() instanceof ItemClayMan) {
                                    this.swingArm();
                                    this.world.playSound(item, "step.gravel", 0.8F, ((this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F) * 0.9F);
                                    Item item1 = ItemListener.greyDoll.asItem();
                                    if (this.clayTeam == 1) {
                                        item1 = ItemListener.redDoll.asItem();
                                    } else if (this.clayTeam == 2) {
                                        item1 = ItemListener.yellowDoll.asItem();
                                    } else if (this.clayTeam == 3) {
                                        item1 = ItemListener.greenDoll.asItem();
                                    } else if (this.clayTeam == 4) {
                                        item1 = ItemListener.blueDoll.asItem();
                                    } else if (this.clayTeam == 5) {
                                        item1 = ItemListener.orangeDoll.asItem();
                                    } else if (this.clayTeam == 6) {
                                        item1 = ItemListener.purpleDoll.asItem();
                                    }

                                    // todo:
                                    //for(int q = 0; q < 18; ++q) {
                                    //a = this.x + (double)(this.rand.nextFloat() - this.rand.nextFloat()) * 0.125D;
                                    //double b = this.y + 0.25D + (double)(this.rand.nextFloat() - this.rand.nextFloat()) * 0.125D;
                                    //double c = this.z + (double)(this.rand.nextFloat() - this.rand.nextFloat()) * 0.125D;
                                    //ModLoader.getMinecraftInstance().effectRenderer.addEffect(new EntitySlimeFX(this.worldObj, a, b, c, item1));
                                    //}

                                    ec = new EntityClayMan(this.world, item.x, item.y, item.z, this.clayTeam);
                                    this.world.spawnEntity(ec);
                                    this.gotcha((ItemEntity) this.targetFollow);
                                    --this.resPoints;
                                } else if (stack.itemId == Block.LOG.id && this.vehicle == null) {
                                    int gottam = 0;
                                    if (this.logs < 20 && stack.count >= 5) {
                                        gottam = 1;
                                    }

                                    if (gottam > 0) {
                                        this.world.playSound(this, "random.pop", 0.2F, ((this.rand.nextFloat() - this.rand.nextFloat()) * 0.7F + 1.0F) * 2.0F);
                                        if (gottam == 1) {
                                            this.logs += 5;
                                            if (item.stack != null) {
                                                ItemStack var10000 = item.stack;
                                                var10000.count -= 5;
                                            }
                                        }

                                        if (item.stack == null || item.stack.count <= 0) {
                                            item.remove();
                                        }
                                    }

                                    this.setTarget(null);
                                    this.targetFollow = null;
                                } else if (stack.itemId == Item.DYE_POWDER.id) {
                                    this.targetFollow = null;
                                }
                            }
                        }
                    }
                } else if (this.targetFollow instanceof EntityClayMan && (double) this.distanceTo(this.targetFollow) < 1.75) {
                    this.targetFollow = null;
                } else if (this.targetFollow instanceof FishHookEntity && (double) this.distanceTo(this.targetFollow) < 1.0) {
                    this.targetFollow = null;
                } else if (this.targetFollow instanceof EntityDirtHorse && (double) this.distanceTo(this.targetFollow) < 0.75 && this.gooTime <= 0) {
                    if (this.vehicle == null && this.targetFollow.passenger == null && !this.heavyCore && this.logs <= 0) {
                        this.startRiding(this.targetFollow);
                        this.world.playSound(this, "step.gravel", 0.6F, 1.0F / (this.rand.nextFloat() * 0.2F + 0.9F));
                    }

                    this.targetFollow = null;
                }
            } else {
                this.updateBlockFinder();
                if (this.logs > 0 && this.rand.nextInt(16) == 0) {
                    this.updateBuildings();
                }
            }
        }

        if (this.isSwinging) {
            this.lastHandSwingProgress += 0.15F;
            this.handSwingProgress += 0.15F;
            if (this.lastHandSwingProgress > 1.0F || this.handSwingProgress > 1.0F) {
                this.isSwinging = false;
                this.lastHandSwingProgress = 0.0F;
                this.handSwingProgress = 0.0F;
            }
        }

        if (this.isSwingingLeft) {
            this.swingLeft += 0.15F;
            if (this.swingLeft > 1.0F) {
                this.isSwingingLeft = false;
                this.swingLeft = 0.0F;
            }
        }

    }

    public void updateBlockFinder() {
        int x = MathHelper.floor(this.x);
        int y = MathHelper.floor(this.boundingBox.minY);
        int z = MathHelper.floor(this.z);
        if (this.blockX != 0 && this.blockY != 0 && this.blockZ != 0 && !this.method_633()) {
            EntityPath emily = this.world.method_189(this, this.blockX, this.blockY, this.blockZ, 16.0F);
            if (emily != null && this.rand.nextInt(5) != 0) {
                this.setTarget(emily);
            } else {
                this.blockX = 0;
                this.blockY = 0;
                this.blockZ = 0;
            }
        }

        int i = x;
        int j = y;
        int k = z;

        for (int q = 0; q < 16; ++q) {
            if (j >= 4 && j <= 124 && this.isAirySpace(i, j, k) && !this.isAirySpace(i, j - 1, k)) {
                int b = j - 1;
                if (this.checkSides(i, b, k, i, j, k, this.blocDist(i, b, k, x, y, z), q == 0)) {
                    break;
                }

                ++b;
                int a = i - 1;
                if (this.checkSides(a, b, k, i, j, k, this.blocDist(a, b, k, x, y, z), q == 0)) {
                    break;
                }

                a += 2;
                if (this.checkSides(a, b, k, i, j, k, this.blocDist(a, b, k, x, y, z), q == 0)) {
                    break;
                }

                --a;
                int c = k - 1;
                if (this.checkSides(a, b, c, i, j, k, this.blocDist(a, b, c, x, y, z), q == 0)) {
                    break;
                }

                c += 2;
                if (this.checkSides(a, b, c, i, j, k, this.blocDist(a, b, c, x, y, z), q == 0)) {
                    break;
                }

                i = x + this.rand.nextInt(6) - this.rand.nextInt(6);
                j = y + this.rand.nextInt(3) - this.rand.nextInt(3);
                k = z + this.rand.nextInt(6) - this.rand.nextInt(6);
            }
        }

    }

    public double blocDist(int a, int b, int c, int x, int y, int z) {
        double i = a - x;
        double j = b - y;
        double k = c - z;
        return Math.sqrt(i * i + j * j + k * k);
    }

    public boolean isAirySpace(int x, int y, int z) {
        int p = this.world.getBlockId(x, y, z);
        return p == 0 || Block.BY_ID[p].getCollisionShape(this.world, x, y, z) == null;
    }

    public boolean checkSides(int a, int b, int c, int i, int j, int k, double dist, boolean first) {
        if (b > 4 && b < 124 && this.world.getBlockId(a, b, c) == Block.CHEST.id) {
            if (first && this.blockX == i && this.blockY == j && this.blockZ == k) {
                this.setTarget(null);
                this.blockX = 0;
                this.blockY = 0;
                this.blockZ = 0;
                this.chestOperations(a, b, c, true);
                return true;
            }

            if (this.blockX == 0 && this.blockY == 0 && this.blockZ == 0 && this.chestOperations(a, b, c, false)) {
                EntityPath emily = this.world.method_189(this, i, j, k, 16.0F);
                if (emily != null) {
                    this.setTarget(emily);
                    this.blockX = i;
                    this.blockY = j;
                    this.blockZ = k;
                    return true;
                }
            }
        }

        return false;
    }

    public boolean chestOperations(int x, int y, int z, boolean arrived) {
        BlockEntity te = this.world.getBlockEntity(x, y, z);
        if (te != null && te instanceof ChestBlockEntity chest) {

            for (int q = 0; q < chest.getInventorySize(); ++q) {
                if (chest.getInventoryItem(q) != null) {
                    ItemStack stack = chest.getInventoryItem(q);
                    if (stack.count > 0) {
                        if (this.weaponPoints <= 0 && stack.itemId == Item.STICK.id) {
                            if (arrived) {
                                this.weaponPoints = 15;
                                this.stickSharp = false;
                                this.gotcha(chest, q);
                            }

                            return true;
                        }

                        if (this.armorPoints <= 0 && stack.itemId == Item.LEATHER.id) {
                            if (arrived) {
                                this.armorPoints = 15;
                                this.armorPadded = false;
                                this.gotcha(chest, q);
                            }

                            return true;
                        }

                        if (this.rocks <= 0 && stack.itemId == Block.GRAVEL.id) {
                            if (arrived) {
                                this.rocks = 15;
                                this.gotcha(chest, q);
                            }

                            return true;
                        }

                        if (!this.glowing && stack.itemId == Item.GLOWSTONE_DUST.id) {
                            if (arrived) {
                                this.glowing = true;
                                this.gotcha(chest, q);
                            }

                            return true;
                        }

                        int u;
                        if (!this.king && stack.itemId == Item.GOLD_INGOT.id) {
                            boolean jack = false;
                            List list2 = this.world.getEntities(this, this.boundingBox.expand(24.0, 16.0, 24.0));

                            for (u = 0; u < list2.size(); ++u) {
                                Entity entity2 = (Entity) list2.get(u);
                                if (entity2 instanceof EntityClayMan clayman) {
                                    if (clayman.clayTeam == this.clayTeam && clayman.king) {
                                        jack = true;
                                        break;
                                    }
                                }
                            }

                            if (!jack) {
                                if (arrived) {
                                    this.king = true;
                                    this.gotcha(chest, q);
                                }

                                return true;
                            }
                        } else {
                            if (!this.gunpowdered && stack.itemId == Item.GUNPOWDER.id) {
                                if (arrived) {
                                    this.gunpowdered = true;
                                    this.gotcha(chest, q);
                                }

                                return true;
                            }

                            if (this.sugarTime <= 0 && stack.itemId == Item.SUGAR.id) {
                                if (arrived) {
                                    this.sugarTime = 1200;
                                    this.gotcha(chest, q);
                                }

                                return true;
                            }

                            if (this.foodLeft <= 0 && stack.getItem() != null && stack.getItem() instanceof FoodItem) {
                                if (arrived) {
                                    this.foodLeft = 4;
                                    this.gotcha(chest, q);
                                }

                                return true;
                            }

                            if (this.resPoints <= 0 && stack.itemId == Item.CLAY.id) {
                                if (arrived) {
                                    this.resPoints = 4;
                                    this.gotcha(chest, q);
                                }

                                return true;
                            }

                            if (this.gooStock <= 0 && stack.itemId == Item.SLIMEBALL.id) {
                                if (arrived) {
                                    this.gooStock = 2;
                                    this.gotcha(chest, q);
                                }

                                return true;
                            }

                            if (this.smokeStock <= 0 && stack.itemId == Item.REDSTONE_DUST.id) {
                                if (arrived) {
                                    this.smokeStock = 2;
                                    this.gotcha(chest, q);
                                }

                                return true;
                            }

                            if (stack.itemId == Block.LOG.id && this.vehicle == null) {
                                int gottam = 0;
                                if (this.logs < 20 && stack.count >= 5) {
                                    gottam = 1;
                                }

                                if (arrived && gottam > 0) {
                                    this.world.playSound(this, "random.pop", 0.2F, ((this.rand.nextFloat() - this.rand.nextFloat()) * 0.7F + 1.0F) * 2.0F);
                                    if (gottam == 1) {
                                        this.logs += 5;
                                        chest.takeInventoryItem(q, 5);
                                    }
                                }

                                return gottam > 0 || arrived;
                            }

                            double a;
                            double b;
                            int j;
                            //if (this.weaponPoints > 0 && !this.stickSharp && stack.itemId == ItemBase.flint.id) {
                            if (this.weaponPoints > 0 && !this.stickSharp && stack.itemId == Item.FLINT.id) {
                                if (arrived) {
                                    this.stickSharp = true;

                                    for (j = 0; j < 4; ++j) {
                                        a = this.x + (double) (this.rand.nextFloat() - this.rand.nextFloat()) * 0.125;
                                        a = this.boundingBox.minY + 0.125 + (double) (this.rand.nextFloat() - this.rand.nextFloat()) * 0.25;
                                        b = this.z + (double) (this.rand.nextFloat() - this.rand.nextFloat()) * 0.125;
                                        ClientUtil.addDiggingParticle(this.world, a, a, b, 0.0, 0.0, 0.0, Block.WOOD, 0, 0);
                                    }

                                    this.world.playSound(this, "random.wood click", 0.75F, 1.0F / (this.rand.nextFloat() * 0.2F + 0.9F));
                                }

                                return true;
                            }

                            if (this.armorPoints > 0 && !this.armorPadded && stack.itemId == Block.WOOL.id) {
                                if (arrived) {
                                    this.armorPadded = true;

                                    for (j = 0; j < 4; ++j) {
                                        a = this.x + (double) (this.rand.nextFloat() - this.rand.nextFloat()) * 0.125;
                                        a = this.boundingBox.minY + 0.125 + (double) (this.rand.nextFloat() - this.rand.nextFloat()) * 0.25;
                                        b = this.z + (double) (this.rand.nextFloat() - this.rand.nextFloat()) * 0.125;
                                        ClientUtil.addDiggingParticle(this.world, a, a, b, 0.0, 0.0, 0.0, Block.WOOL, 0, 0);
                                    }

                                    this.world.playSound(this, "step.cloth", 0.75F, 1.0F / (this.rand.nextFloat() * 0.2F + 0.9F));
                                }

                                return true;
                            }

                            if (!this.heavyCore && this.vehicle == null && stack.itemId == Item.IRON_INGOT.id) {
                                if (arrived) {
                                    this.heavyCore = true;
                                    this.gotcha(chest, q);
                                }

                                return true;
                            }

                            if (this.resPoints > 0 && stack.getItem() != null && stack.getItem() instanceof ItemClayMan ic) {
                                if (ic.clayTeam == this.clayTeam) {
                                    if (arrived) {
                                        this.swingArm();
                                        Item item1 = ItemListener.greyDoll.asItem();
                                        if (this.clayTeam == 1) {
                                            item1 = ItemListener.redDoll.asItem();
                                        } else if (this.clayTeam == 2) {
                                            item1 = ItemListener.yellowDoll.asItem();
                                        } else if (this.clayTeam == 3) {
                                            item1 = ItemListener.greenDoll.asItem();
                                        } else if (this.clayTeam == 4) {
                                            item1 = ItemListener.blueDoll.asItem();
                                        } else if (this.clayTeam == 5) {
                                            item1 = ItemListener.orangeDoll.asItem();
                                        } else if (this.clayTeam == 6) {
                                            item1 = ItemListener.purpleDoll.asItem();
                                        }

                                        for (u = 0; u < 18; ++u) {
                                            a = this.x + (double) (this.rand.nextFloat() - this.rand.nextFloat()) * 0.125;
                                            b = this.y + 0.25 + (double) (this.rand.nextFloat() - this.rand.nextFloat()) * 0.125;
                                            double c = this.z + (double) (this.rand.nextFloat() - this.rand.nextFloat()) * 0.125;
                                            ClientUtil.addPoofParticle(this.world, a, b, c, item1);
                                        }

                                        double a1 = this.x + (double) (this.rand.nextFloat() - this.rand.nextFloat()) * 0.125;
                                        double b1 = this.y + (double) this.rand.nextFloat() * 0.125;
                                        double c1 = this.z + (double) (this.rand.nextFloat() - this.rand.nextFloat()) * 0.125;
                                        EntityClayMan ec = new EntityClayMan(this.world, a1, b1, c1, this.clayTeam);
                                        this.world.spawnEntity(ec);
                                        this.gotcha(chest, q);
                                        --this.resPoints;
                                    }

                                    return true;
                                }
                            }
                        }
                    }
                }
            }
        }

        return false;
    }

    public void updateBuildings() {
        int x = MathHelper.floor(this.x);
        int y = MathHelper.floor(this.boundingBox.minY);
        int z = MathHelper.floor(this.z);
        if (y >= 4 && y <= 120) {
            int broad = 2;
            int high = 3;
            if (this.logs == 20) {
                broad = 3;
                high = 4;
            }

            boolean flag = false;

            for (int a = -broad; a < broad + 1 && !flag; ++a) {
                for (int b = -1; b < high + 1 && !flag; ++b) {
                    for (int c = -broad; c < broad + 1 && !flag; ++c) {
                        if (b == -1) {
                            if (this.isAirySpace(x + a, y + b, z + c)) {
                                flag = true;
                            }
                        } else if (!this.isAirySpace(x + a, y + b, z + c) || this.world.getMaterial(x + a, y + b, z + c) == Material.WATER) {
                            flag = true;
                        }
                    }
                }
            }

            if (!flag) {
                double gee = broad;
                List list = this.world.getEntities(this, this.boundingBox.expand(gee, gee, gee));
                if (list.size() > 0) {
                    flag = true;
                }
            }

            if (!flag) {
                if (this.logs == 20 && this.rand.nextInt(2) == 0) {
                    this.buildHouseThree();
                } else if (this.logs >= 10 && this.rand.nextInt(3) > 0) {
                    this.buildHouseTwo();
                } else if (this.logs >= 5) {
                    this.buildHouseOne();
                }
            }

        }
    }

    public void dropLogs() {
        this.dropItem(Block.LOG.id, this.logs);
        this.logs = 0;
    }

    public void buildHouseOne() {
        int x = MathHelper.floor(this.x + 0.5);
        int y = MathHelper.floor(this.boundingBox.minY);
        int z = MathHelper.floor(this.z + 0.5);
        int direction = this.rand.nextInt(4);

        for (int j = 0; j < 3; ++j) {
            int b = j;

            for (int i = -1; i < 3; ++i) {
                for (int k = -1; k < 2; ++k) {
                    int a = i;
                    int c = k;
                    if (direction % 2 == 0) {
                        a = -i;
                        c = -k;
                    }

                    if (direction / 2 == 0) {
                        int swap = a;
                        a = c;
                        c = swap;
                    }

                    if (j == 0) {
                        if (i != -1 && i != 2 && k != -1) {
                            this.world.setBlock(x + a, y + b, z + c, 0);
                        } else {
                            this.world.setBlock(x + a, y + b, z + c, Block.WOOD.id);
                        }
                    } else if (j == 1) {
                        if (i == -1) {
                            this.world.setBlockInChunk(x + a, y + b, z + c, Block.WOOD_STAIRS.id);
                            this.world.setBlockMeta(x + a, y + b, z + c, (direction + 2 + (direction % 2 == 0 ? 1 : -1)) % 4);
                        } else if (i == 2) {
                            this.world.setBlockInChunk(x + a, y + b, z + c, Block.WOOD_STAIRS.id);
                            this.world.setBlockMeta(x + a, y + b, z + c, (direction + 2) % 4);
                        } else if (k == -1) {
                            this.world.setBlock(x + a, y + b, z + c, Block.WOOD.id);
                        } else {
                            this.world.setBlock(x + a, y + b, z + c, 0);
                        }
                    } else if (i == 0) {
                        this.world.setBlockInChunk(x + a, y + b, z + c, Block.WOOD_STAIRS.id);
                        this.world.setBlockMeta(x + a, y + b, z + c, (direction + 2 + (direction % 2 == 0 ? 1 : -1)) % 4);
                    } else if (i == 1) {
                        this.world.setBlockInChunk(x + a, y + b, z + c, Block.WOOD_STAIRS.id);
                        this.world.setBlockMeta(x + a, y + b, z + c, (direction + 2) % 4);
                    } else {
                        this.world.setBlock(x + a, y + b, z + c, 0);
                    }
                }
            }
        }

        this.world.playSound(this, "random.wood click", 1.75F, 1.0F / (this.rand.nextFloat() * 0.2F + 0.9F));
        this.world.playSound(this, "step.wood", 1.75F, 1.0F / (this.rand.nextFloat() * 0.2F + 0.9F));
        this.logs -= 5;
    }

    public void buildHouseTwo() {
        int x = MathHelper.floor(this.x);
        int y = MathHelper.floor(this.boundingBox.minY);
        int z = MathHelper.floor(this.z);
        int direction = this.rand.nextInt(4);

        for (int j = 0; j < 3; ++j) {
            int b = j;

            for (int i = -2; i < 3; ++i) {
                for (int k = -2; k < 3; ++k) {
                    int a = i;
                    int c = k;
                    if (direction % 2 == 0) {
                        a = -i;
                        c = -k;
                    }

                    if (direction / 2 == 0) {
                        int swap = a;
                        a = c;
                        c = swap;
                    }

                    if (i != -2 && i != 2 || k != -2 && k != 2) {
                        if (j != 0 && j != 1) {
                            if (j == 2) {
                                if (i == -2) {
                                    this.world.setBlockInChunk(x + a, y + b, z + c, Block.WOOD_STAIRS.id);
                                    this.world.setBlockMeta(x + a, y + b, z + c, (direction + 2 + (direction % 2 == 0 ? 1 : -1)) % 4);
                                } else if (i == 2) {
                                    this.world.setBlockInChunk(x + a, y + b, z + c, Block.WOOD_STAIRS.id);
                                    this.world.setBlockMeta(x + a, y + b, z + c, (direction + 2) % 4);
                                } else if (k == -2) {
                                    this.world.setBlockInChunk(x + a, y + b, z + c, Block.WOOD_STAIRS.id);
                                    this.world.setBlockMeta(x + a, y + b, z + c, (direction + (direction % 2 == 0 ? 1 : -1)) % 4);
                                } else if (k == 2) {
                                    this.world.setBlockInChunk(x + a, y + b, z + c, Block.WOOD_STAIRS.id);
                                    this.world.setBlockMeta(x + a, y + b, z + c, direction % 4);
                                } else {
                                    this.world.setBlock(x + a, y + b, z + c, Block.WOOD.id);
                                }
                            }
                        } else if (i != -2 && i != 2 && k != -2 && (k != 2 || i == 0 && j != 1)) {
                            this.world.setBlock(x + a, y + b, z + c, 0);
                        } else {
                            this.world.setBlock(x + a, y + b, z + c, Block.WOOD.id);
                        }
                    }
                }
            }
        }

        this.world.playSound(this, "random.wood click", 1.75F, 1.0F / (this.rand.nextFloat() * 0.2F + 0.9F));
        this.world.playSound(this, "step.wood", 1.75F, 1.0F / (this.rand.nextFloat() * 0.2F + 0.9F));
        this.logs -= 10;
    }

    public void buildHouseThree() {
        int x = MathHelper.floor(this.x);
        int y = MathHelper.floor(this.boundingBox.minY);
        int z = MathHelper.floor(this.z);
        int direction = this.rand.nextInt(4);

        for (int j = 0; j < 4; ++j) {
            int b = j;

            for (int i = -3; i < 4; ++i) {
                for (int k = -2; k < 3; ++k) {
                    int a = i;
                    int c = k;
                    if (direction % 2 == 0) {
                        a = -i;
                        c = -k;
                    }

                    if (direction / 2 == 0) {
                        int swap = a;
                        a = c;
                        c = swap;
                    }

                    if (i != -3 && i != 3 || k != -2 && k != 2) {
                        if (j < 3) {
                            if (i != -3 && i != 3 && k != -2 && (k != 2 || i == 0 && j <= 0)) {
                                if (i == -2 && j == 0 && k == 0) {
                                    this.world.setBlockInChunk(x + a, y + b, z + c, Block.CHEST.id);
                                    this.world.setBlockMeta(x + a, y + b, z + c, (direction + 2) % 4);
                                    ChestBlockEntity chest = (ChestBlockEntity) this.world.getBlockEntity(x + a, y + b, z + c);
                                    chest.setInventoryItem(0, new ItemStack(Item.STICK, 16, 0));
                                } else if (i == 0 && j == 0 && k == -1) {
                                    this.world.setBlockInChunk(x + a, y + b, z + c, Block.WOOD_STAIRS.id);
                                    this.world.setBlockMeta(x + a, y + b, z + c, (direction + 2 + (direction % 2 == 0 ? 1 : -1)) % 4);
                                } else if (i == 1 && j == 1 && k == -1) {
                                    this.world.setBlockInChunk(x + a, y + b, z + c, Block.WOOD_STAIRS.id);
                                    this.world.setBlockMeta(x + a, y + b, z + c, (direction + 2 + (direction % 2 == 0 ? 1 : -1)) % 4);
                                } else if (i == 2 && j == 1 && k == -1) {
                                    this.world.setBlock(x + a, y + b, z + c, Block.WOOD.id);
                                } else if (i == 2 && j == 2 && k == 0) {
                                    this.world.setBlockInChunk(x + a, y + b, z + c, Block.WOOD_STAIRS.id);
                                    this.world.setBlockMeta(x + a, y + b, z + c, (direction + (direction % 2 == 0 ? 1 : -1)) % 4);
                                } else if (i == 0 && j == 2 && k == -1) {
                                    this.world.setBlock(x + a, y + b, z + c, 0);
                                } else if (i == 1 && j == 2 && k == -1) {
                                    this.world.setBlock(x + a, y + b, z + c, 0);
                                } else if (i == 2 && j == 2 && k == -1) {
                                    this.world.setBlock(x + a, y + b, z + c, 0);
                                } else if (j == 2) {
                                    this.world.setBlock(x + a, y + b, z + c, Block.WOOD.id);
                                } else {
                                    this.world.setBlock(x + a, y + b, z + c, 0);
                                }
                            } else {
                                this.world.setBlock(x + a, y + b, z + c, Block.WOOD.id);
                            }
                        } else if (j == 3) {
                            if (i != -3 && i != 3 && k != -2 && (k != 2 || i == 0 && j <= 0)) {
                                this.world.setBlock(x + a, y + b, z + c, 0);
                            } else if (i != -2 && i != 0 && i != 2 && k != 0) {
                                this.world.setBlock(x + a, y + b, z + c, Block.STONE_SLAB.id);
                                this.world.setBlockMeta(x + a, y + b, z + c, 2);
                            } else {
                                this.world.setBlock(x + a, y + b, z + c, Block.WOOD.id);
                            }
                        }
                    }
                }
            }
        }

        this.world.playSound(this, "random.wood click", 1.75F, 1.0F / (this.rand.nextFloat() * 0.2F + 0.9F));
        this.world.playSound(this, "step.wood", 1.75F, 1.0F / (this.rand.nextFloat() * 0.2F + 0.9F));
        this.logs -= 20;
    }

    public void travel(float f, float f1) {
        super.travel(f, f1);
        double d2 = (this.x - this.prevX) * 2.0;
        double d3 = (this.z - this.prevZ) * 2.0;
        float f5 = MathHelper.sqrt(d2 * d2 + d3 * d3) * 4.0F;
        if (f5 > 1.0F) {
            f5 = 1.0F;
        }

        this.limbDistance += (f5 - this.limbDistance) * 0.4F;
        this.field_1050 += this.limbDistance;
    }

    public void swingArm() {
        if (!this.isSwinging) {
            this.isSwinging = true;
            this.lastHandSwingProgress = 0.0F;
            this.handSwingProgress = 0.0F;
        }

    }

    public void swingLeftArm() {
        if (!this.isSwingingLeft) {
            this.isSwingingLeft = true;
            this.swingLeft = 0.01F;
        }

    }

    public boolean hitTargetMakesDead(Entity e) {
        this.strikeTime = 12;
        this.swingArm();
        int power = this.weaponPoints > 0 ? 3 + this.rand.nextInt(2) + (this.stickSharp ? 1 : 0) : 2;
        if (this.weaponPoints > 0) {
            --this.weaponPoints;
        }

        boolean flag = e.damage(this, power);
        if (flag && e instanceof LivingEntity el) {
            return el.health <= 0;
        }

        return false;
    }

    public void throwRockAtEnemy(Entity entity) {
        double d = entity.x - this.x;
        double d1 = entity.z - this.z;
        EntityGravelChunk entitygravelchunk = new EntityGravelChunk(this.world, this, this.clayTeam);
        entitygravelchunk.y += 0.3999999761581421;
        double d2 = entity.y + (double) entity.getStandingEyeHeight() - 0.10000000298023223 - entitygravelchunk.y;
        float f1 = MathHelper.sqrt(d * d + d1 * d1) * 0.2F;
        this.world.spawnEntity(entitygravelchunk);
        entitygravelchunk.setArrowHeading(d, d2 + (double) f1, d1, 0.6F, 12.0F);
        this.attackTime = 30;
        this.forwardVelocity = -this.forwardVelocity;
        this.yaw = (float) (Math.atan2(d1, d) * 180.0 / 3.1415927410125732) - 90.0F;
        this.field_663 = true;
        this.swingLeftArm();
    }

    public void gotcha(ItemEntity item) {
        this.world.playSound(item, "random.pop", 0.2F, ((this.rand.nextFloat() - this.rand.nextFloat()) * 0.7F + 1.0F) * 2.0F);
        if (item.stack != null) {
            --item.stack.count;
            if (item.stack.count <= 0) {
                item.remove();
            }
        } else {
            item.remove();
        }

        this.targetFollow = null;
        this.setTarget(null);
    }

    public void gotcha(ChestBlockEntity chest, int q) {
        this.world.playSound(this, "random.pop", 0.2F, ((this.rand.nextFloat() - this.rand.nextFloat()) * 0.7F + 1.0F) * 2.0F);
        chest.takeInventoryItem(q, 1);
    }

    public void writeNBT(CompoundTag nbttagcompound) {
        super.writeNBT(nbttagcompound);
        nbttagcompound.put("ClayTeam", (short) this.clayTeam);
        nbttagcompound.put("WeaponPoints", (short) this.weaponPoints);
        nbttagcompound.put("ArmorPoints", (short) this.armorPoints);
        nbttagcompound.put("FoodLeft", (short) this.foodLeft);
        nbttagcompound.put("SugarTime", (short) this.sugarTime);
        nbttagcompound.put("ResPoints", (short) this.resPoints);
        nbttagcompound.put("StrikeTime", (short) this.strikeTime);
        nbttagcompound.put("ClimbTime", (short) this.climbTime);
        nbttagcompound.put("GooTime", (short) this.gooTime);
        nbttagcompound.put("SmokeTime", (short) this.smokeTime);
        nbttagcompound.put("GooStock", (short) this.gooStock);
        nbttagcompound.put("SmokeStock", (short) this.smokeStock);
        nbttagcompound.put("Logs", (short) this.logs);
        nbttagcompound.put("Rocks", (short) this.rocks);
        nbttagcompound.put("Gunpowdered", this.gunpowdered);
        nbttagcompound.put("King", this.king);
        nbttagcompound.put("Glowing", this.glowing);
        nbttagcompound.put("StickSharp", this.stickSharp);
        nbttagcompound.put("ArmorPadded", this.armorPadded);
        nbttagcompound.put("HeavyCore", this.heavyCore);
    }

    public void readNBT(CompoundTag nbttagcompound) {
        super.readNBT(nbttagcompound);
        this.clayTeam = nbttagcompound.getShort("ClayTeam");
        this.texture = this.clayManTexture(this.clayTeam);
        this.weaponPoints = nbttagcompound.getShort("WeaponPoints");
        this.armorPoints = nbttagcompound.getShort("ArmorPoints");
        this.foodLeft = nbttagcompound.getShort("FoodLeft");
        this.sugarTime = nbttagcompound.getShort("SugarTime");
        this.resPoints = nbttagcompound.getShort("ResPoints");
        this.strikeTime = nbttagcompound.getShort("StrikeTime");
        this.climbTime = nbttagcompound.getShort("ClimbTime");
        this.gooTime = nbttagcompound.getShort("GooTime");
        this.smokeTime = nbttagcompound.getShort("SmokeTime");
        this.gooStock = nbttagcompound.getShort("GooStock");
        this.smokeStock = nbttagcompound.getShort("SmokeStock");
        this.logs = nbttagcompound.getShort("Logs");
        this.rocks = nbttagcompound.getShort("Rocks");
        this.gunpowdered = nbttagcompound.getBoolean("Gunpowdered");
        this.king = nbttagcompound.getBoolean("King");
        this.glowing = nbttagcompound.getBoolean("Glowing");
        this.stickSharp = nbttagcompound.getBoolean("StickSharp");
        this.armorPadded = nbttagcompound.getBoolean("ArmorPadded");
        this.heavyCore = nbttagcompound.getBoolean("HeavyCore");
    }

    protected String getHurtSound() {
        this.world.playSound(this, "random.hurt", 0.6F, (this.rand.nextFloat() * 0.2F + 1.6F));
        this.world.playSound(this, "step.gravel", 0.6F, 1.0F / (this.rand.nextFloat() * 0.2F + 0.9F));
        return "";
    }

    protected String getDeathSound() {
        this.world.playSound(this, "random.hurt", 0.6F, (this.rand.nextFloat() * 0.2F + 1.6F));
        return "step.gravel";
    }

    protected void jump() {
        if (this.gooTime <= 0) {
            if (this.sugarTime > 0) {
                this.yVelocity = 0.375;
            } else {
                this.yVelocity = 0.275;
            }

        }
    }

    public boolean method_932() {
        if (this.logs <= 0 && this.field_1624 && !this.onGround && this.climbTime > 0) {
            if (this.climbTime != 10) {
                this.throwTime = 5;
                --this.climbTime;
                return true;
            }

            if (this.yVelocity < 0.05) {
                --this.climbTime;
                this.throwTime = 5;
                return true;
            }
        }

        return false;
    }

    protected boolean canDespawn() {
        return false;
    }

    public boolean hasStick() {
        return this.weaponPoints > 0;
    }

    public boolean hasArmor() {
        return this.armorPoints > 0;
    }

    public boolean hasSpecks() {
        return this.gunpowdered;
    }

    public boolean hasCrown() {
        return this.king;
    }

    public boolean isGlowing() {
        return this.glowing;
    }

    public boolean isSharpened() {
        return this.stickSharp;
    }

    public boolean isPadded() {
        return this.armorPadded;
    }

    public boolean isGooey() {
        return this.gooTime > 0;
    }

    public boolean hasLogs() {
        return this.logs > 0;
    }

    public float armLeft() {
        return this.swingLeft;
    }

    public boolean hasRocks() {
        return this.rocks > 0 && this.throwTime <= 0 && this.logs <= 0;
    }

    public void getDrops() {
        if (!this.gunpowdered) {
            Item item1 = ItemListener.greyDoll.asItem();
            if (this.clayTeam == 1) {
                item1 = ItemListener.redDoll.asItem();
            } else if (this.clayTeam == 2) {
                item1 = ItemListener.yellowDoll.asItem();
            } else if (this.clayTeam == 3) {
                item1 = ItemListener.greenDoll.asItem();
            } else if (this.clayTeam == 4) {
                item1 = ItemListener.blueDoll.asItem();
            } else if (this.clayTeam == 5) {
                item1 = ItemListener.orangeDoll.asItem();
            } else if (this.clayTeam == 6) {
                item1 = ItemListener.purpleDoll.asItem();
            }

            this.dropItem(item1.id, 1);
            if (this.resPoints > 0) {
                this.dropItem(Item.CLAY.id, 1);
            }

            if (this.weaponPoints > 7 && this.rand.nextInt(2) == 0) {
                this.dropItem(Item.STICK.id, 1);
            }

            if (this.armorPoints > 7 && this.rand.nextInt(2) == 0) {
                this.dropItem(Item.LEATHER.id, 1);
            }

            if (this.rocks > 7 && this.rand.nextInt(2) == 0) {
                this.dropItem(Block.GRAVEL.id, 1);
            }

            if (this.smokeStock > 1 && this.rand.nextInt(2) == 0) {
                this.dropItem(Item.REDSTONE_DUST.id, 1);
            }

            if (this.gooStock > 1 && this.rand.nextInt(2) == 0) {
                this.dropItem(Item.SLIMEBALL.id, 1);
            }

            if (this.smokeStock > 1 && this.rand.nextInt(2) == 0) {
                this.dropItem(Item.REDSTONE_DUST.id, 1);
            }

            if (this.gooStock > 1 && this.rand.nextInt(2) == 0) {
                this.dropItem(Item.SLIMEBALL.id, 1);
            }

            if (this.glowing && this.rand.nextInt(2) == 0) {
                this.dropItem(Item.GLOWSTONE_DUST.id, 1);
            }

            if (this.king) {
                this.dropItem(Item.GOLD_INGOT.id, 1);
            }

            if (this.heavyCore) {
                this.dropItem(Item.IRON_INGOT.id, 1);
            }

            if (this.logs > 0) {
                this.dropLogs();
            }
        }

    }

    public boolean damage(Entity e, int i) {
        if (this.vehicle != null && i < 100 && this.rand.nextInt(2) == 0) {
            return this.vehicle.damage(e, i);
        } else {
            if (e != null && e instanceof EntityClayMan james) {
                if (james.clayTeam == this.clayTeam) {
                    return false;
                }

                if (this.logs > 0) {
                    this.dropLogs();
                }

                if (this.smokeTime <= 0) {
                    this.entity = e;
                }

                if (this.armorPoints > 0) {
                    i /= 2;
                    if (this.armorPadded) {
                        --i;
                    }

                    --this.armorPoints;
                    if (i < 0) {
                        i = 0;
                    }
                }

                if (this.health - i > 0) {
                    int j;
                    double a;
                    double b;
                    double c;
                    if ((james.smokeStock <= 0 || this.smokeTime <= 0 || this.rand.nextInt(2) == 0) && james.gooStock > 0 && this.gooTime <= 0 && this.onGround) {
                        --james.gooStock;
                        this.gooTime = 150;
                        this.world.playSound(this, "mob.slimeattack", 0.75F, 1.0F / (this.rand.nextFloat() * 0.2F + 0.9F));

                        for (j = 0; j < 4; ++j) {
                            a = this.x + (double) (this.rand.nextFloat() - this.rand.nextFloat()) * 0.125;
                            b = this.boundingBox.minY + 0.125 + (double) (this.rand.nextFloat() - this.rand.nextFloat()) * 0.125;
                            c = this.z + (double) (this.rand.nextFloat() - this.rand.nextFloat()) * 0.125;
                            this.world.addParticle("slime", a, b, c, 0.0, 0.1, 0.0);
                        }

                        this.xVelocity = 0.0;
                        this.yVelocity = 0.0;
                        this.zVelocity = 0.0;
                        this.forwardVelocity = 0.0F;
                        this.horizontalVelocity = 0.0F;
                        this.jumping = false;
                    } else if (james.smokeStock > 0 && this.smokeTime <= 0) {
                        --james.smokeStock;
                        this.smokeTime = 100;
                        this.world.playSound(this, "random.fizz", 0.75F, 1.0F / (this.rand.nextFloat() * 0.2F + 0.9F));

                        for (j = 0; j < 8; ++j) {
                            a = this.x + (double) (this.rand.nextFloat() - this.rand.nextFloat()) * 0.125;
                            b = this.boundingBox.minY + 0.25 + (double) this.rand.nextFloat() * 0.25;
                            c = this.z + (double) (this.rand.nextFloat() - this.rand.nextFloat()) * 0.125;
                            this.world.addParticle("reddust", a, b, c, 0.0, 0.1, 0.0);
                        }

                        this.targetFollow = null;
                        this.entity = null;
                        this.setTarget(null);
                    }
                }
            } else {
                i = 20;
                if (e instanceof FishHookEntity) {
                    return false;
                }
            }

            boolean fred = super.damage(e, i);
            if (fred && this.health <= 0) {
                Item item1 = ItemListener.greyDoll.asItem();
                if (this.clayTeam == 1) {
                    item1 = ItemListener.redDoll.asItem();
                } else if (this.clayTeam == 2) {
                    item1 = ItemListener.yellowDoll.asItem();
                } else if (this.clayTeam == 3) {
                    item1 = ItemListener.greenDoll.asItem();
                } else if (this.clayTeam == 4) {
                    item1 = ItemListener.blueDoll.asItem();
                } else if (this.clayTeam == 5) {
                    item1 = ItemListener.orangeDoll.asItem();
                } else if (this.clayTeam == 6) {
                    item1 = ItemListener.purpleDoll.asItem();
                }

                for (int q = 0; q < 24; ++q) {
                    double a = this.x + (double) (this.rand.nextFloat() - this.rand.nextFloat()) * 0.125;
                    double b = this.y + 0.25 + (double) (this.rand.nextFloat() - this.rand.nextFloat()) * 0.125;
                    double c = this.z + (double) (this.rand.nextFloat() - this.rand.nextFloat()) * 0.125;
                    ClientUtil.addPoofParticle(this.world, a, b, c, item1);
                }

                this.removed = true;
                if (e != null && e instanceof PlayerEntity) {
                    this.killedByPlayer = e;
                }

                if (this.gunpowdered) {
                    this.world.createExplosion(null, this.x, this.y, this.z, 1.0F);
                }
            }

            return fred;
        }
    }

    public void accelerate(double d, double d1, double d2) {
        if (this.gooTime <= 0) {
            this.xVelocity += d;
            this.yVelocity += d1;
            this.zVelocity += d2;
        }
    }

    public void method_925(Entity entity, int i, double d, double d1) {
        if (this.gooTime <= 0) {
            super.method_925(entity, i, d, d1);
            if (entity != null && entity instanceof EntityClayMan ec) {
                if ((!ec.heavyCore || !this.heavyCore) && (ec.heavyCore || this.heavyCore)) {
                    if (!ec.heavyCore && this.heavyCore) {
                        this.xVelocity *= 0.2;
                        this.yVelocity *= 0.4;
                        this.zVelocity *= 0.2;
                    } else {
                        this.xVelocity *= 1.5;
                        this.zVelocity *= 1.5;
                    }
                } else {
                    this.xVelocity *= 0.6;
                    this.yVelocity *= 0.75;
                    this.zVelocity *= 0.6;
                }
            } else if (entity != null && entity instanceof EntityGravelChunk) {
                this.xVelocity *= 0.6;
                this.yVelocity *= 0.75;
                this.zVelocity *= 0.6;
            }

        }
    }

    // scope needed for horse
    public boolean isJumping() {
        return jumping;
    }

    public void setJumping(boolean j) {
        jumping = j;
    }

    public float getForwardVelocity() {
        return forwardVelocity;
    }

    public float getHorizontalVelocity() {
        return horizontalVelocity;
    }

    public float getFallDistance() {
        return fallDistance;
    }

    public void setFallDistance(float f) {
        fallDistance = f;
    }

    @Override
    public Identifier getHandlerIdentifier() {
        return Identifier.of(MODID, "claysoldier");
    }

    @Override
    public void writeToMessage(MessagePacket message) {
        // message.ints.length is 4, so let's add a fifth
        int[] newInts = Arrays.copyOf(message.ints, message.ints.length + 1);
        newInts[newInts.length - 1] = clayTeam;
        message.ints = newInts;
    }

    @Override
    public void readFromMessage(MessagePacket message) {
        clayTeam = message.ints[4];
        this.texture = clayManTexture(clayTeam);
    }
}