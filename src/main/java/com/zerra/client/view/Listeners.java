package com.zerra.client.view;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWJoystickCallback;
import org.lwjgl.glfw.GLFWKeyCallback;
import org.lwjgl.glfw.GLFWMouseButtonCallback;
import org.lwjgl.glfw.GLFWScrollCallback;

import com.zerra.common.ZerraClient;

/**
 * <em><b>Copyright (c) 2019 The Zerra Team.</b></em>
 * 
 * <br>
 * </br>
 * 
 * This should not be used by modders. Ever.
 * 
 * @author Ocelot5836
 */
public class Listeners {

	protected static final GLFWKeyCallback KEY_CALLBACK = new KeyCallback();
	protected static final GLFWMouseButtonCallback MOUSE_CALLBACK = new MouseCallback();
	protected static final GLFWScrollCallback SCROLL_CALLBACK = new ScrollCallback();
	protected static final GLFWJoystickCallback JOYSTICK_CALLBACK = new JoystickCallback();

	private static class KeyCallback extends GLFWKeyCallback {
		@Override
		public void invoke(long window, int key, int scancode, int action, int mods) {
			if (action == GLFW.GLFW_PRESS) {
				ZerraClient.getInstance().onKeyPressed(key);
			}

			if (action == GLFW.GLFW_RELEASE) {
				ZerraClient.getInstance().onKeyReleased(key);
			}
		}
	}

	private static class MouseCallback extends GLFWMouseButtonCallback {
		@Override
		public void invoke(long window, int button, int action, int mods) {
			if (action == GLFW.GLFW_PRESS) {
				ZerraClient.getInstance().onMousePressed(Display.getMouseX(), Display.getMouseY(), button);
			}

			if (action == GLFW.GLFW_RELEASE) {
				ZerraClient.getInstance().onMouseReleased(Display.getMouseX(), Display.getMouseY(), button);
			}
		}
	}

	private static class ScrollCallback extends GLFWScrollCallback {
		@Override
		public void invoke(long window, double xoffset, double yoffset) {
			ZerraClient.getInstance().onMouseScrolled(Display.getMouseX(), Display.getMouseY(), yoffset);
		}
	}

	private static class JoystickCallback extends GLFWJoystickCallback {
		@Override
		public void invoke(int jid, int event) {
			Display.joysticksPresent[jid] = (byte) (GLFW.glfwJoystickPresent(jid) ? 1 : 0);
			if (event == GLFW.GLFW_CONNECTED) {
				ZerraClient.getInstance().onJoystickConnected(jid);
				ZerraClient.logger().info("Controller " + jid + " was connected");
			} else {
				ZerraClient.getInstance().onJoystickDisconnected(jid);
				ZerraClient.logger().info("Controller " + jid + " was disconnected");
			}
		}
	}
}