package com.zerra.client.entity;

import org.lwjgl.glfw.GLFW;

import com.zerra.client.gfx.Display;
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

		this.handleInput();
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
							this.setX(this.getXEntityPos() + joystick.getX());
						}
					}
					else
					{
						if (!(joystick.getX() < xOffset + 0.01f))
						{
							this.setX(this.getXEntityPos() + joystick.getX());
						}
					}

					if (joystick.getY() < 0)
					{
						if (!(joystick.getY() > -yOffset - 0.01f))
						{
							this.setX(this.getYEntityPos() + joystick.getY());
						}
					}
					else
					{
						if (!(joystick.getY() < yOffset + 0.01f))
						{
							this.setX(this.getYEntityPos() + joystick.getY());
						}
					}
				}
			}

			if (inputHandler.isKeyPressed(GLFW.GLFW_KEY_W) || inputHandler.isKeyPressed(GLFW.GLFW_KEY_UP))
			{
				this.setY(this.getYEntityPos() - this.getAttribute(SPEED).getValue());
			}
			if (inputHandler.isKeyPressed(GLFW.GLFW_KEY_S) || inputHandler.isKeyPressed(GLFW.GLFW_KEY_DOWN))
			{
				this.setY(this.getYEntityPos() + this.getAttribute(SPEED).getValue());
			}
			if (inputHandler.isKeyPressed(GLFW.GLFW_KEY_A) || inputHandler.isKeyPressed(GLFW.GLFW_KEY_LEFT))
			{
				this.setX(this.getYEntityPos() - this.getAttribute(SPEED).getValue());
			}
			if (inputHandler.isKeyPressed(GLFW.GLFW_KEY_D) || inputHandler.isKeyPressed(GLFW.GLFW_KEY_RIGHT))
			{
				this.setX(this.getYEntityPos() + this.getAttribute(SPEED).getValue());
			}
		}
	}
}