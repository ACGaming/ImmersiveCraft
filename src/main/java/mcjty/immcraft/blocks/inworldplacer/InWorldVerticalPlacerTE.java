package mcjty.immcraft.blocks.inworldplacer;

import mcjty.immcraft.blocks.generic.GenericInventoryTE;
import mcjty.immcraft.blocks.generic.handles.InputInterfaceHandle;
import mcjty.immcraft.config.GeneralConfiguration;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

public class InWorldVerticalPlacerTE extends GenericInventoryTE {

    public static final int SLOT_INPUT1 = 0;
    public static final int SLOT_INPUT2 = 1;
    public static final int SLOT_INPUT3 = 2;
    public static final int SLOT_INPUT4 = 3;

    public InWorldVerticalPlacerTE() {
        super(4);
        addInterfaceHandle(new InputInterfaceHandle().slot(SLOT_INPUT1).side(EnumFacing.SOUTH).bounds(0, .5f, .5f, 1).renderOffset(new Vec3(-.23, 0.73, -0.46)));
        addInterfaceHandle(new InputInterfaceHandle().slot(SLOT_INPUT2).side(EnumFacing.SOUTH).bounds(.5f, .5f, 1, 1).renderOffset(new Vec3(.23, 0.73, -0.46)));
        addInterfaceHandle(new InputInterfaceHandle().slot(SLOT_INPUT3).side(EnumFacing.SOUTH).bounds(0, 0, .5f, .5f).renderOffset(new Vec3(-.23, 0.27, -0.46)));
        addInterfaceHandle(new InputInterfaceHandle().slot(SLOT_INPUT4).side(EnumFacing.SOUTH).bounds(.5f, 0, 1, .5f).renderOffset(new Vec3(.23, 0.27, -0.46)));
    }

    @Override
    public void setInventorySlotContents(int index, ItemStack stack) {
        super.setInventorySlotContents(index, stack);
        for (int i = 0 ; i < 4 ; i++) {
            if (inventoryHelper.hasStack(i)) {
                return;
            }
        }
        // Self destruct
        worldObj.setBlockToAir(getPos());
    }

    public static boolean isValidPlacableBlock(World world, BlockPos pos, EnumFacing side, Block block) {
        if (!block.isBlockSolid(world, pos, side)) {
            return false;
        }
        if (!block.getMaterial().isSolid()) {
            return false;
        }
        if (!block.isNormalCube()) {
            return false;
        }
        return true;
    }

    public static void addItems(GenericInventoryTE inventory, EntityPlayer player, ItemStack heldItem) {
        inventory.setInventorySlotContents(SLOT_INPUT1, heldItem);
        inventory.markDirtyClient();
        player.inventory.setInventorySlotContents(player.inventory.currentItem, null);
        player.openContainer.detectAndSendChanges();
    }

    @Override
    public double getMaxRenderDistanceSquared() {
        return GeneralConfiguration.maxRenderDistanceSquared;
    }
}