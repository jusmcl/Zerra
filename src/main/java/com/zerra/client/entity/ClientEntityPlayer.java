package com.zerra.client.entity;

import org.lwjgl.glfw.GLFW;

import com.zerra.client.ZerraClient;
import com.zerra.client.gfx.Camera;
import com.zerra.client.gfx.Display;
import com.zerra.client.gfx.renderer.Renderer;
import com.zerra.client.input.InputHandler;
import com.zerra.client.input.gamepad.Gamepad;
import com.zerra.client.input.gamepad.Joystick;
import com.zerra.common.world.World;
import com.zerra.common.world.entity.EntityPlayer;

public class ClientEntityPlayer extends EntityPlayer
{
	private InputHandler inputHandler;
	private boolean controllerIsOffset = false;
	private double xOffset;
	private double yOffset;

	public ClientEntityPlayer(World world, InputHandler inputHandler)
	{
		super(world);
		this.inputHandler = inputHandler;
	}

	@Override
	public void update()
	{
		super.update();

		this.setVelocity(0, 0, 0);

		this.handleInput();

		Camera camera = ZerraClient.getInstance().getRenderingManager().getCamera();
		camera.setPosition(this.getPosition().x() - (Display.getWidth() / Renderer.SCALE - 32) / 32, this.getPosition().y(), this.getPosition().z() - (Display.getHeight() / Renderer.SCALE - 32) / 32);
	}

	private void handleInput()
	{
		if (inputHandler != null)
		{
			if (this.inputHandler.isGamepadConnected(GLFW.GLFW_JOYSTICK_1) && !inputHandler.isReceivingMouseInput())
			{
				Gamepad gamepad = inputHandler.getGamepad(GLFW.GLFW_JOYSTICK_1);
				Joystick joystick = gamepad.getJoystick(0);

				if (joystick != null)
				{

					if (!this.controllerIsOffset && joystick.getX() != 0 && joystick.getY() != 0 && Display.isCreated())
					{
						xOffset = joystick.getX();
						yOffset = joystick.getY();
						this.controllerIsOffset = true;
					}

					if (joystick.getX() < 0)
					{
						if (!(joystick.getX() > -xOffset - 0.01f))
						{
							this.move(joystick.getX(), 0, 0);
						}
					}
					else
					{
						if (!(joystick.getX() < xOffset + 0.01f))
						{
							this.move(joystick.getX(), 0, 0);
						}
					}

					if (joystick.getY() < 0)
					{
						if (!(joystick.getY() > -yOffset - 0.01f))
						{
							this.move(joystick.getY(), 0, 0);
						}
					}
					else
					{
						if (!(joystick.getY() < yOffset + 0.01f))
						{
							this.move(joystick.getY(), 0, 0);
						}
					}
				}
			}

			if (inputHandler.isKeyPressed(GLFW.GLFW_KEY_W) || inputHandler.isKeyPressed(GLFW.GLFW_KEY_UP))
			{
				this.move(0, 0, -this.getAttribute(SPEED).getValue());
			}
			if (inputHandler.isKeyPressed(GLFW.GLFW_KEY_S) || inputHandler.isKeyPressed(GLFW.GLFW_KEY_DOWN))
			{
				this.move(0, 0, this.getAttribute(SPEED).getValue());
			}
			if (inputHandler.isKeyPressed(GLFW.GLFW_KEY_A) || inputHandler.isKeyPressed(GLFW.GLFW_KEY_LEFT))
			{
				this.move(-this.getAttribute(SPEED).getValue(), 0, 0);
			}
			if (inputHandler.isKeyPressed(GLFW.GLFW_KEY_D) || inputHandler.isKeyPressed(GLFW.GLFW_KEY_RIGHT))
			{
				this.move(this.getAttribute(SPEED).getValue(), 0, 0);
			}
		}
	}
}