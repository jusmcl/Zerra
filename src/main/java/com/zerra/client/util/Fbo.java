package com.zerra.client.util;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.lwjgl.opengl.GL14;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import com.zerra.client.view.Display;

/**
 * <em><b>Copyright (c) 2019 The Zerra Team.</b></em> <br>
 * </br>
 * A render target that can be rendered to and sampled from.
 * 
 * @author Ocelot5836
 */
public class Fbo
{

	public static final int NONE = 0;
	public static final int DEPTH_TEXTURE = 1;
	public static final int DEPTH_RENDER_BUFFER = 2;

	private final int width;
	private final int height;

	private int frameBuffer;

	private int[] colorTextures;
	private int depthTexture;

	private int depthBuffer;
	private int[] colourBuffers;
	private int numAttachments;

	/**
	 * Creates an FBO of a specified width and height, with the desired type of
	 * depth buffer attachment.
	 * 
	 * @param width The width of the FBO.
	 * @param height The height of the FBO.
	 * @param depthBufferType An int indicating the type of depth buffer attachment
	 *        that this FBO should use.
	 */
	public Fbo(int width, int height, int depthBufferType)
	{
		this(width, height, depthBufferType, 1);
	}

	/**
	 * Creates an FBO of a specified width and height, with the desired type of
	 * depth buffer attachment.
	 * 
	 * @param width The width of the FBO.
	 * @param height The height of the FBO.
	 * @param depthBufferType An int indicating the type of depth buffer attachment
	 *        that this FBO should use.
	 * @param numAttachments The number of color buffers to attach to this FBO
	 */
	public Fbo(int width, int height, int depthBufferType, int numAttachments)
	{
		if (numAttachments <= 0 || numAttachments >= 32)
			throw new RuntimeException("An FBO must have at least 1 color attachment and at most 32!");
		this.width = width;
		this.height = height;
		this.colorTextures = new int[numAttachments];
		this.colourBuffers = new int[numAttachments];
		this.numAttachments = numAttachments;
		initialiseFrameBuffer(depthBufferType);
	}

	/**
	 * Deletes the frame buffer and its attachments when the game closes.
	 */
	public void dispose()
	{
		GL30.glDeleteFramebuffers(frameBuffer);
		GL11.glDeleteTextures(colorTextures);
		GL11.glDeleteTextures(depthTexture);
		GL30.glDeleteRenderbuffers(depthBuffer);
		GL30.glDeleteRenderbuffers(colourBuffers);
	}

	/**
	 * Binds the frame buffer, setting it as the current render target. Anything
	 * rendered after this will be rendered to this FBO, and not to the screen.
	 */
	public void bindFrameBuffer()
	{
		GL30.glBindFramebuffer(GL30.GL_DRAW_FRAMEBUFFER, frameBuffer);
		GL11.glViewport(0, 0, width, height);
	}

	/**
	 * Unbinds the frame buffer, setting the default frame buffer as the current
	 * render target. Anything rendered after this will be rendered to the screen,
	 * and not this FBO.
	 */
	public void unbindFrameBuffer()
	{
		GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, 0);
		GL11.glViewport(0, 0, Display.getWidth(), Display.getHeight());
	}

	/**
	 * Binds the current FBO to be read from (not used in tutorial 43).
	 */
	public void bindToRead()
	{
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
		GL30.glBindFramebuffer(GL30.GL_READ_FRAMEBUFFER, frameBuffer);
		GL11.glReadBuffer(GL30.GL_COLOR_ATTACHMENT0);
	}

	/**
	 * @return The ID of the texture containing the color buffer of the FBO.
	 */
	public int getColorTexture()
	{
		return colorTextures[0];
	}

	/**
	 * @return The ID of the texture containing the color buffer of the FBO.
	 */
	public int getColorTexture(int buffer)
	{
		return colorTextures[buffer];
	}

	/**
	 * @return The texture containing the FBOs depth buffer.
	 */
	public int getDepthTexture()
	{
		return depthTexture;
	}

	/**
	 * Creates the FBO along with a color buffer texture attachment, and possibly a
	 * depth buffer.
	 * 
	 * @param type The type of depth buffer attachment to be attached to the FBO.
	 */
	private void initialiseFrameBuffer(int type)
	{
		createFrameBuffer();
		for (int i = 0; i < numAttachments; i++)
		{
			colorTextures[i] = createTextureAttachment(GL30.GL_COLOR_ATTACHMENT0 + i);
		}
		if (type == DEPTH_RENDER_BUFFER)
		{
			createDepthBufferAttachment();
		} else if (type == DEPTH_TEXTURE)
		{
			createDepthTextureAttachment();
		}
		unbindFrameBuffer();
	}

	/**
	 * Creates a new frame buffer object and sets the buffer to which drawing will
	 * occur - color attachment 0. This is the attachment where the color buffer
	 * texture is.
	 */
	private void createFrameBuffer()
	{
		frameBuffer = GL30.glGenFramebuffers();
		GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, frameBuffer);
		determineDrawBuffers();
	}

	private void determineDrawBuffers()
	{
		IntBuffer drawBuffers = BufferUtils.createIntBuffer(numAttachments);
		for (int i = 0; i < numAttachments; i++)
		{
			drawBuffers.put(GL30.GL_COLOR_ATTACHMENT0 + i);
		}
		drawBuffers.flip();
		GL20.glDrawBuffers(drawBuffers);
	}

	/**
	 * Creates a texture and sets it as the color buffer attachment for this FBO.
	 */
	private int createTextureAttachment(int attachment)
	{
		int colorTexture = GL11.glGenTextures();
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, colorTexture);
		GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA8, width, height, 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, (ByteBuffer) null);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL12.GL_CLAMP_TO_EDGE);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL12.GL_CLAMP_TO_EDGE);
		GL30.glFramebufferTexture2D(GL30.GL_FRAMEBUFFER, attachment, GL11.GL_TEXTURE_2D, colorTexture, 0);
		return colorTexture;
	}

	/**
	 * Adds a depth buffer to the FBO in the form of a texture, which can later be
	 * sampled.
	 */
	private void createDepthTextureAttachment()
	{
		depthTexture = GL11.glGenTextures();
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, depthTexture);
		GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL14.GL_DEPTH_COMPONENT24, width, height, 0, GL11.GL_DEPTH_COMPONENT, GL11.GL_FLOAT, (ByteBuffer) null);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
		GL30.glFramebufferTexture2D(GL30.GL_FRAMEBUFFER, GL30.GL_DEPTH_ATTACHMENT, GL11.GL_TEXTURE_2D, depthTexture, 0);
	}

	/**
	 * Adds a depth buffer to the FBO in the form of a render buffer. This can't be
	 * used for sampling in the shaders.
	 */
	private void createDepthBufferAttachment()
	{
		depthBuffer = GL30.glGenRenderbuffers();
		GL30.glBindRenderbuffer(GL30.GL_RENDERBUFFER, depthBuffer);
		GL30.glRenderbufferStorage(GL30.GL_RENDERBUFFER, GL14.GL_DEPTH_COMPONENT24, width, height);
		GL30.glFramebufferRenderbuffer(GL30.GL_FRAMEBUFFER, GL30.GL_DEPTH_ATTACHMENT, GL30.GL_RENDERBUFFER, depthBuffer);
	}
}