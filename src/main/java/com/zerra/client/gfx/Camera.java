package com.zerra.client.gfx;

import org.joml.Vector3f;
import org.joml.Vector3i;

import com.zerra.client.ZerraClient;
import com.zerra.common.world.storage.plate.Plate;

/**
 * <em><b>Copyright (c) 2019 The Zerra Team.</b></em> <br>
 * </br>
 * A simple implementation of {@link ICamera}.
 * 
 * @author Ocelot5836
 */
public class Camera implements ICamera
{

	private Vector3i lastPlatePosition;
	private Vector3i platePosition;
	private Vector3f renderPosition;
	private Vector3f lastPosition;
	private Vector3f position;
	private Vector3f renderRotation;
	private Vector3f lastRotation;
	private Vector3f rotation;

	// TODO move all movement/input code to client player
	
	// private float speedAdjust = 0f;
	//
	// private boolean controllerIsOffset = false;
	// private double xOffset;
	// private double yOffset;

	public Camera()
	{
		this.lastPlatePosition = new Vector3i();
		this.platePosition = new Vector3i();
		this.renderPosition = new Vector3f();
		this.lastPosition = new Vector3f();
		this.position = new Vector3f();
		this.renderRotation = new Vector3f();
		this.lastRotation = new Vector3f();
		this.rotation = new Vector3f();
	}

	/**
	 * Updates the camera's position and rotation.
	 */
	public void update()
	{
		this.lastPlatePosition.set(this.platePosition);
		this.lastPosition.set(this.position);
		this.lastRotation.set(this.rotation);
		this.platePosition.set((int) (this.position.x / (float) (Plate.SIZE + 1)), (int) this.position.z, (int) (this.position.y / (float) (Plate.SIZE + 1)));

		// InputHandler inputHandler = ZerraClient.getInstance().getInputHandler();
		// if (inputHandler.isGamepadConnected(GLFW.GLFW_JOYSTICK_1) && !inputHandler.isReceivingMouseInput())
		// {
		// Gamepad gamepad = inputHandler.getGamepad(GLFW.GLFW_JOYSTICK_1);
		// Joystick joystick = gamepad.getJoystick(0);
		//
		// if (joystick != null)
		// {
		//
		// if (!this.controllerIsOffset && joystick.getX() != 0 && joystick.getY() != 0 && Display.isCreated())
		// {
		// xOffset = joystick.getX();
		// yOffset = joystick.getY();
		// this.controllerIsOffset = true;
		// }
		//
		// if (joystick.getX() < 0)
		// {
		// if (!(joystick.getX() > -xOffset - 0.01f))
		// {
		// this.position.x += joystick.getX();
		// }
		// }
		// else
		// {
		// if (!(joystick.getX() < xOffset + 0.01f))
		// {
		// this.position.x += joystick.getX();
		// }
		// }
		//
		// if (joystick.getY() < 0)
		// {
		// if (!(joystick.getY() > -yOffset - 0.01f))
		// {
		// this.position.y += joystick.getY();
		// }
		// }
		// else
		// {
		// if (!(joystick.getY() < yOffset + 0.01f))
		// {
		// this.position.y += joystick.getY();
		// }
		// }
		// }
		// }
		//
		// if (inputHandler.isKeyPressed(GLFW.GLFW_KEY_W) || inputHandler.isKeyPressed(GLFW.GLFW_KEY_UP))
		// {
		// this.position.y -= 1 + this.speedAdjust;
		// }
		// if (inputHandler.isKeyPressed(GLFW.GLFW_KEY_S) || inputHandler.isKeyPressed(GLFW.GLFW_KEY_DOWN))
		// {
		// this.position.y += 1 + this.speedAdjust;
		// }
		// if (inputHandler.isKeyPressed(GLFW.GLFW_KEY_A) || inputHandler.isKeyPressed(GLFW.GLFW_KEY_LEFT))
		// {
		// this.position.x -= 1 + this.speedAdjust;
		// }
		// if (inputHandler.isKeyPressed(GLFW.GLFW_KEY_D) || inputHandler.isKeyPressed(GLFW.GLFW_KEY_RIGHT))
		// {
		// this.position.x += 1 + this.speedAdjust;
		// }

		// Useful keys to adjust the movement speed of the camera when not locked to the player.

		// if (Reference.IS_DEVELOPMENT_BUILD)
		// {
		// if (inputHandler.isKeyPressed(GLFW.GLFW_KEY_EQUAL))
		// {
		// this.speedAdjust += (this.speedAdjust < 10) ? 0.07f : 0.0f;
		// }
		// if (inputHandler.isKeyPressed(GLFW.GLFW_KEY_MINUS))
		// {
		// this.speedAdjust -= (this.speedAdjust > -0.75f) ? 0.07f : 0.0f;
		// }
		// if (inputHandler.isKeyPressed(GLFW.GLFW_KEY_2))
		// {
		// ZerraClient.getInstance().getConnectionManager().sendToServer(new MessagePing());
		// }
		// }
	}

	@Override
	public Vector3f getPosition()
	{
		return this.renderPosition.set(this.lastPosition.x + (this.position.x - this.lastPosition.x) * ZerraClient.getInstance().getRenderPartialTicks(), this.lastPosition.y + (this.position.y - this.lastPosition.y) * ZerraClient.getInstance().getRenderPartialTicks(), this.lastPosition.z + (this.position.z - this.lastPosition.z) * ZerraClient.getInstance().getRenderPartialTicks());
	}

	@Override
	public Vector3f getRotation()
	{
		return this.renderRotation.set(this.renderRotation.x + (this.rotation.x - this.renderRotation.x) * ZerraClient.getInstance().getRenderPartialTicks(), this.renderRotation.y + (this.rotation.y - this.renderRotation.y) * ZerraClient.getInstance().getRenderPartialTicks(), this.renderRotation.z + (this.rotation.z - this.renderRotation.z) * ZerraClient.getInstance().getRenderPartialTicks());
	}
}