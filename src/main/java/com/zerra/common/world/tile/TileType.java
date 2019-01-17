package com.zerra.common.world.tile;

import org.apache.commons.lang3.builder.HashCodeBuilder;

import com.zerra.common.registry.RegistryNameable;

public class TileType implements RegistryNameable {

    private String registryName;

    private final float hardness;

    private final String toolType;

    private final float harvestLevel;

    /**
     * Constructs an tileType
     * @see ZerraTileTypes
     *
     * @param registryName unique identifier in text for this type
     * @param hardness the hardness of the tile (how long it takes to be broken) unless specified by
     *                 the tool/item used the breaktime will be ((hardness * harvestLevel) / toolLevel) * 0.5 seconds
     * @param harvestLevel the minimum level of tool you need to be able to 'harvest' (collect drops) by breaking this tile
     * @param toolType a String specifying the type of tool that can break this Tile. See DefaultToolTypes.class
     */
    public TileType(String registryName, float hardness, float harvestLevel, String toolType) {
        this.registryName = registryName;
      this.hardness = hardness;
      this.harvestLevel = harvestLevel;
      this.toolType = toolType;
    }

    @Override
    public boolean equals(Object o) {
        return o == this || (o instanceof TileType && getRegistryName().equals(((TileType) o).getRegistryName()));
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(getRegistryName())
                .append(getHardness())
                .append(getToolType())
                .append(getHarvestLevel())
                .toHashCode();
    }

    @Override
    public void setDomain(String domain) {
        if (!registryName.contains(":")) {
            registryName = domain + ":" + registryName;
        }
    }

    @Override
    public String getRegistryName() {
        return registryName;
    }

    public float getHardness() {
        return hardness;
    }

    public String getToolType() {
        return toolType;
    }

    public float getHarvestLevel() {
        return harvestLevel;
    }
}
