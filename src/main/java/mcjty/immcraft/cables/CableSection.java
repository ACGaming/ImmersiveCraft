package mcjty.immcraft.cables;

import mcjty.immcraft.api.cable.*;
import mcjty.immcraft.varia.BlockPosTools;
import mcjty.immcraft.varia.NBTHelper;
import mcjty.immcraft.api.util.Vector;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public class CableSection implements ICableSection {
    private final ICableType type;
    private final ICableSubType subType;

    // The multiblock network id for this cable.
    private int id;

    private ConnectionInfo[] info = new ConnectionInfo[] { new ConnectionInfo(), new ConnectionInfo() };

    private final Vector vector;

    public CableSection(NBTTagCompound tagCompound) {
        type = CableRegistry.getTypeByID(tagCompound.getString("type"));
        subType = type.getSubTypeByID(tagCompound.getString("subtype"));
        id = tagCompound.getInteger("id");
        vector = new Vector(tagCompound.getFloat("vx"), tagCompound.getFloat("vy"), tagCompound.getFloat("vz"));
        info[0].readFromNBT(tagCompound, "0");
        info[1].readFromNBT(tagCompound, "1");
    }

    public CableSection(ICableType type, ICableSubType subType, int id, Vector vector) {
        this.id = id;
        this.type = type;
        this.subType = subType;
        this.vector = vector;
    }

    public CableSectionRender getRenderer(BlockPos pos) {
        Vector v1 = getVector(0);
        Vector v2 = getVector(1);

        return new CableSectionRender(subType, vector.subtract(pos), v1 == null ? null : v1.subtract(pos), v2 == null ? null : v2.subtract(pos));
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public ICableType getType() {
        return type;
    }

    @Override
    public ICableSubType getSubType() {
        return subType;
    }

    public void setConnection(int directionId, BlockPos connection, Vector vector, int connectorId) {
        info[directionId].setConnection(connection);
        info[directionId].setVector(vector);
        info[directionId].setConnectorId(connectorId);
    }

    public BlockPos getConnection(int directionId) {
        return info[directionId].getConnection();
    }

    public Vector getVector() {
        return vector;
    }

    public Vector getVector(int directionId) {
        return info[directionId].getVector();
    }

    public ICable getCable(World world) {
        return type.getCableHandler().getCable(world, subType, id);
    }

    public int getConnectorID(int idx) {
        return info[idx].getConnectorId();
    }

    public void releaseConnector(World world, int directionId) {
        info[directionId].releaseConnector(world);
    }

    @Override
    public ICableConnector getConnector(World worldObj, int directionId) {
        return info[directionId].getConnector(worldObj);
    }

    public NBTHelper writeToNBT(NBTHelper helper) {
        helper
                .set("type", type.getTypeID())
                .set("subtype", subType.getTypeID())
                .set("id", id)
                .set("v", vector);
        info[0].writeToNBT(helper, "0");
        info[1].writeToNBT(helper, "1");
        return helper;
    }

    private static class ConnectionInfo {
        private Vector vector;
        private BlockPos connection;
        private int connectorId;

        public Vector getVector() {
            return vector;
        }

        public void setVector(Vector vector) {
            this.vector = vector;
        }

        public BlockPos getConnection() {
            return connection;
        }

        public void setConnection(BlockPos connection) {
            this.connection = connection;
        }

        public int getConnectorId() {
            return connectorId;
        }

        public void setConnectorId(int connectorId) {
            this.connectorId = connectorId;
        }

        public void releaseConnector(World world) {
            ICableConnector c = getConnector(world);
            if (c != null && connectorId != -1) {
                c.disconnect(connectorId);
            }
        }

        public ICableConnector getConnector(World worldObj) {
            if (connection == null) {
                return null;
            }
            TileEntity te = worldObj.getTileEntity(connection);
            if (te instanceof ICableConnector) {
                return (ICableConnector) te;
            } else {
                return null;
            }
        }

        public void readFromNBT(NBTTagCompound tagCompound, String suffix) {
            String vk = "v"+suffix;
            if (tagCompound.hasKey(vk+"x")) {
                vector = new Vector(tagCompound.getFloat(vk + "x"), tagCompound.getFloat(vk + "y"), tagCompound.getFloat(vk + "z"));
            } else {
                vector = null;
            }
            connection = BlockPosTools.readFromNBT(tagCompound, "c" + suffix);
            connectorId = tagCompound.getInteger("i"+suffix);
        }

        public NBTHelper writeToNBT(NBTHelper helper, String suffix) {
            return helper
                    .set("i"+suffix, connectorId)
                    .set("c" + suffix, connection)
                    .set("v"+suffix, vector);
        }
    }
}
