package mcjty.immcraft.blocks.furnace;

import mcjty.immcraft.blocks.generic.GenericBlockWithTE;
import mcjty.immcraft.varia.BlockTools;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class FurnaceBlock extends GenericBlockWithTE<FurnaceTE> {

    public static final PropertyBool BURNING = PropertyBool.create("burning");

    public FurnaceBlock() {
        super(Material.rock, "furnace", FurnaceTE.class);
        setHardness(2.0f);
        setStepSound(soundTypeStone);
        setHarvestLevel("pickaxe", 0);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void initModel() {
        super.initModel();
        ClientRegistry.bindTileEntitySpecialRenderer(FurnaceTE.class, new FurnaceTESR());
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean shouldSideBeRendered(IBlockAccess worldIn, BlockPos pos, EnumFacing side) {
        return false;
    }

    @Override
    public boolean isBlockNormalCube() {
        return false;
    }

    @Override
    public boolean isOpaqueCube() {
        return false;
    }

    @Override
    public boolean canRenderInLayer(EnumWorldBlockLayer layer) {
        return layer == EnumWorldBlockLayer.SOLID || layer == EnumWorldBlockLayer.CUTOUT;
    }

    @Override
    public int getLightValue(IBlockAccess world, BlockPos pos) {
        if (getTE(world, pos).getBurnTime() > 0) {
            return 13;
        } else {
            return 0;
        }
    }

    @Override
    public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
        FurnaceTE furnaceTE = BlockTools.getTE(FurnaceTE.class, worldIn, pos).get();
        Boolean burning = furnaceTE.getBurnTime() > 0;
        return state.withProperty(BURNING, burning);
    }

    @Override
    protected BlockState createBlockState() {
        return new BlockState(this, FACING_HORIZ, BURNING);
    }

}
