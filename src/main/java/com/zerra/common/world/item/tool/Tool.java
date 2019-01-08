package com.zerra.common.world.item.tool;

/**
 * An interface to be used by tools.
 * 
 * @author Arpaesis
 */
public interface Tool
{

	/**
	 * @return The swing speed of the tool (calculated as swings per second).
	 */
	public float getSwingSpeed();

	public void setSwingSpeed(float swingSpeed);

	/**
	 * @return The reach of the tool.
	 */
	public float getReach();

	public void setReach(float reach);

	/**
	 * @return The durability of the tool.
	 */
	public int getDurability();

	public void setDurability(int durability);

	/**
	 * @return The type of tool this is.
	 */
	public String getToolType();

	public void setToolType(String toolType);
}
