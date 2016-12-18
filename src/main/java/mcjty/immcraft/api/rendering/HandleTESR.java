package mcjty.immcraft.api.rendering;


import mcjty.immcraft.api.IImmersiveCraft;
import mcjty.immcraft.api.generic.GenericBlock;
import mcjty.immcraft.api.generic.GenericTE;
import mcjty.immcraft.api.handles.IInterfaceHandle;
import mcjty.lib.tools.MinecraftTools;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class HandleTESR<T extends GenericTE> extends TileEntitySpecialRenderer<T> {

    protected final GenericBlock block;
    protected final IImmersiveCraft api;

    protected Vec3d textOffset = new Vec3d(0, 0, 0);

    public HandleTESR(GenericBlock block, IImmersiveCraft api) {
        this.block = block;
        this.api = api;
    }

    @Override
    public void renderTileEntityAt(T tileEntity, double x, double y, double z, float partialTicks, int destroyStage) {

        IBlockState state = tileEntity.getWorld().getBlockState(tileEntity.getPos());
        if (!(state.getBlock() instanceof GenericBlock)) {
            // Safety. In some situations (like with shaders mod installed) this gets called
            // when the block is already removed
            return;
        }

        GlStateManager.pushAttrib();
        GlStateManager.pushMatrix();

        GlStateManager.translate(x + .5, y, z + .5);
        GlStateManager.disableRescaleNormal();

        BlockRenderHelper.rotateFacing(tileEntity, block.getMetaUsage());
        renderExtra(tileEntity);
        renderHandles(tileEntity);

        GlStateManager.popMatrix();
        GlStateManager.popAttrib();
    }

    protected void renderHandles(T tileEntity) {
        double distanceSq = MinecraftTools.getPlayer(Minecraft.getMinecraft()).getDistanceSq(tileEntity.getPos());
        if (distanceSq > api.getMaxHandleRenderDistanceSquared()) {
            return;
        }

        bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
        IInterfaceHandle selectedHandle = BlockRenderHelper.getFacingInterfaceHandle(tileEntity, block);
        BlockRenderHelper.renderInterfaceHandles(api, tileEntity, selectedHandle, textOffset);
    }

    protected void renderExtra(T tileEntity) {

    }
}
