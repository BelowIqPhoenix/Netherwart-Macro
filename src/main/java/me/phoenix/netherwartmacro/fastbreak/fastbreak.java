package me.phoenix.netherwartmacro.fastbreak;

import me.phoenix.netherwartmacro.Main;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C0APacketAnimation;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class fastbreak implements LeftClickListener {

    public static  Config config = new Config();

    public static Block currentBlock;

    private float currentDamage;

    private EnumFacing side;

    private int blockHitDelay;

    public static Block id;

    private BlockPos pos;

    public static BlockPos blockpos;

    public float mineSpeed;

    public float maxBlocks;

    public int fastbreakbps = 4;

    private NetHandlerPlayClient netClientHandler;

    @SubscribeEvent
    public void onRenderWorld(RenderWorldLastEvent event) {

        Minecraft mc = Minecraft.getMinecraft();
        if (fastbreakbps == 4)
            this.mineSpeed = 1.9F;
        try {
            if ((Minecraft.getMinecraft()).objectMouseOver == null || (Minecraft.getMinecraft()).objectMouseOver.getBlockPos() == null)
                return;
            if (Main.displayScreen != null || (Minecraft.getMinecraft()).currentScreen != null)
                return;
            if (!config.fastbreak || (Minecraft.getMinecraft()).theWorld.getBlockState((Minecraft.getMinecraft()).objectMouseOver.getBlockPos()).getBlock().getMaterial() == Material.air || (Minecraft.getMinecraft()).theWorld.getBlockState((Minecraft.getMinecraft()).objectMouseOver.getBlockPos()).getBlock() == Blocks.soul_sand || (Minecraft.getMinecraft()).theWorld.getBlockState((Minecraft.getMinecraft()).objectMouseOver.getBlockPos()).getBlock() == Blocks.farmland || (Minecraft.getMinecraft()).theWorld.getBlockState((Minecraft.getMinecraft()).objectMouseOver.getBlockPos()).getBlock() == Blocks.dirt || (Minecraft.getMinecraft()).theWorld.getBlockState((Minecraft.getMinecraft()).objectMouseOver.getBlockPos()).getBlock() == Blocks.stonebrick || (Minecraft.getMinecraft()).theWorld.getBlockState((Minecraft.getMinecraft()).objectMouseOver.getBlockPos()).getBlock() == Blocks.standing_sign || (Minecraft.getMinecraft()).theWorld.getBlockState((Minecraft.getMinecraft()).objectMouseOver.getBlockPos()).getBlock() == Blocks.ladder)
                return;
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            if ((Minecraft.getMinecraft()).objectMouseOver == null || (Minecraft.getMinecraft()).objectMouseOver.getBlockPos() == null)
                return;
            if (this.blockHitDelay > 0) {
                this.blockHitDelay--;
                return;
            }
            if (!mc.gameSettings.keyBindAttack.isKeyDown())
                return;
            blockpos = (Minecraft.getMinecraft()).objectMouseOver.getBlockPos();
            this.pos = blockpos;
            if (this.blockHitDelay > 0)
                return;
            if (blockpos == null || !this.pos.equals(blockpos))
                this.currentDamage = 0.0F;
            currentBlock = (Minecraft.getMinecraft()).theWorld.getBlockState(blockpos).getBlock();
            if (this.currentDamage == 0.0F) {
                if (this.blockHitDelay > 0)
                    return;
                currentBlock.onBlockClicked((World)mc.theWorld, blockpos, (EntityPlayer)mc.thePlayer);
                (Minecraft.getMinecraft()).thePlayer.sendQueue.addToSendQueue((Packet)new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.START_DESTROY_BLOCK, blockpos, (Minecraft.getMinecraft()).objectMouseOver.sideHit));
                if (currentBlock.getPlayerRelativeBlockHardness((EntityPlayer)(Minecraft.getMinecraft()).thePlayer, (World)(Minecraft.getMinecraft()).theWorld, blockpos) >= 1.0F) {
                    if (fastbreakbps == 4)
                        this.blockHitDelay = 2;
                    if ((Minecraft.getMinecraft()).theWorld.getBlockState((Minecraft.getMinecraft()).objectMouseOver.getBlockPos()).getBlock().getMaterial() != Material.air);
                    (Minecraft.getMinecraft()).playerController.onPlayerDestroyBlock(blockpos, (Minecraft.getMinecraft()).objectMouseOver.sideHit);
                    return;
                }
            }
            if ((Minecraft.getMinecraft()).theWorld.getBlockState((Minecraft.getMinecraft()).objectMouseOver.getBlockPos()).getBlock().getMaterial() != Material.air) {
                if (this.blockHitDelay > 0)
                    return;
                Block block = (Minecraft.getMinecraft()).theWorld.getBlockState((Minecraft.getMinecraft()).objectMouseOver.getBlockPos()).getBlock();
                this.currentDamage += currentBlock.getPlayerRelativeBlockHardness((EntityPlayer)(Minecraft.getMinecraft()).thePlayer, (World)(Minecraft.getMinecraft()).theWorld, blockpos) * (config.fastbreak ? this.mineSpeed : 1.0F);
            }
            if ((Minecraft.getMinecraft()).theWorld.getBlockState((Minecraft.getMinecraft()).objectMouseOver.getBlockPos()).getBlock().getMaterial() != Material.air) {
                if (this.blockHitDelay > 0)
                    return;
                (Minecraft.getMinecraft()).thePlayer.sendQueue.addToSendQueue((Packet)new C0APacketAnimation());
            }
            (Minecraft.getMinecraft()).theWorld.sendBlockBreakProgress((Minecraft.getMinecraft()).thePlayer.getEntityId(), blockpos, (int)(this.currentDamage * 2.55F) - 1);
            if (this.currentDamage >= 1.0F) {
                if (this.blockHitDelay > 0)
                    return;
                mc.thePlayer.sendQueue.addToSendQueue((Packet)new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.ABORT_DESTROY_BLOCK, blockpos, (Minecraft.getMinecraft()).objectMouseOver.sideHit));
                (Minecraft.getMinecraft()).thePlayer.sendQueue.addToSendQueue((Packet)new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.STOP_DESTROY_BLOCK, blockpos, (Minecraft.getMinecraft()).objectMouseOver.sideHit));
                this.currentDamage = 0.0F;
                if (fastbreakbps == 4)
                    this.blockHitDelay = 2;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onLeftClick() {
        if ((Minecraft.getMinecraft()).objectMouseOver == null || (Minecraft.getMinecraft()).objectMouseOver.getBlockPos() == null)
            return;
        if (config.fastbreak && (Minecraft.getMinecraft()).theWorld.getBlockState((Minecraft.getMinecraft()).objectMouseOver.getBlockPos()).getBlock().getMaterial() != Material.air)
            id = (Minecraft.getMinecraft()).theWorld.getBlockState((Minecraft.getMinecraft()).objectMouseOver.getBlockPos()).getBlock();
    }
}
