package com.zerra.common.world.item.tool;

/**
 * An interface to be used by tools.
 * 
 * @author Arpaesis
 */
public interface Tool
{

	/**
	 * @return The swing speed of the tool.
	 */
	public float swingSpeed();

	/**
	 * @return The cooldown in seconds for the tool to swing again.
	 */
	public int swingCooldown();

	/**
	 * @return The reach of the tool.
	 */
	public float reach();

	/**
	 * @return The durability of the tool. This also sets the max durability of the
	 *         tool.
	 */
	public int durability();

	/**
	 * @return The type of tool this is.
	 */
	public String getToolType();
}
