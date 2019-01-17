package com.zerra.client.gfx.shader;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.FloatBuffer;
import java.util.HashMap;
import java.util.Map;

import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;

import com.zerra.client.util.ResourceLocation;
import com.zerra.common.ZerraClient;

/**
 * <em><b>Copyright (c) 2018 The Zerra Team.</b></em>
 * 
 * <br>
 * </br>
 * 
 * The root for any kind of shader. Handles all of the GL stuff and allows the user to load objects to the shaders from java code.
 * 
 * @author Ocelot5836
 */
public abstract class ShaderProgram {

	private Map<String, String> compileParameters;
	private int programID;
	private int vertexShaderID;
	private int fragmentShaderID;

	private FloatBuffer matrix3Buffer = BufferUtils.createFloatBuffer(3 * 3);
	private FloatBuffer matrix4Buffer = BufferUtils.createFloatBuffer(4 * 4);

	/**
	 * The default implementation of a shader program containing what any shader program needs.
	 * 
	 * @param domain
	 *            The domain the shader is in
	 * @param baseName
	 *            Takes the name and looks for files named baseName_vertex.glsl and baseName_fragment.glsl. The folder these are also searched for is res/assets/domain/shaders.
	 */
	public ShaderProgram(String domain, String baseName) {
		this(new ResourceLocation(domain, "shaders/" + baseName + "_vert.glsl"), new ResourceLocation(domain, "shaders/" + baseName + "_frag.glsl"));
	}

	/**
	 * The default implementation of a shader program containing what any shader program needs.
	 * 
	 * @param vertexFile
	 *            The vertex shader file location found as /vertexFile.glsl
	 * @param fragmentFile
	 *            The fragment shader file location found as /fragmentFile.glsl
	 */
	public ShaderProgram(ResourceLocation vertexFile, ResourceLocation fragmentFile) {
		this.compileParameters = new HashMap<String, String>();
		this.setCompileParameters();
		this.vertexShaderID = loadShader(vertexFile, GL20.GL_VERTEX_SHADER);
		this.fragmentShaderID = loadShader(fragmentFile, GL20.GL_FRAGMENT_SHADER);
		this.programID = GL20.glCreateProgram();
		GL20.glAttachShader(this.programID, this.vertexShaderID);
		GL20.glAttachShader(this.programID, this.fragmentShaderID);
		bindAttributes();
		GL20.glLinkProgram(this.programID);
		GL20.glValidateProgram(this.programID);
		getAllUniformLocations();
	}

	/**
	 * Binds all the attributes to the shader files.
	 */
	protected abstract void bindAttributes();

	/**
	 * Sets all the uniform location integers to the locations of the variables in the shaders.
	 */
	protected abstract void getAllUniformLocations();

	/**
	 * Sets all the parameters that will be used during shader compilation.
	 */
	protected void setCompileParameters() {
	}

	protected void setCompileParameter(String key, byte value) {
		this.compileParameters.put(key, Byte.toString(value));
	}

	protected void setCompileParameter(String key, short value) {
		this.compileParameters.put(key, Short.toString(value));
	}

	protected void setCompileParameter(String key, int value) {
		this.compileParameters.put(key, Integer.toString(value));
	}

	protected void setCompileParameter(String key, float value) {
		this.compileParameters.put(key, Float.toString(value));
	}

	protected void setCompileParameter(String key, double value) {
		this.compileParameters.put(key, Double.toString(value));
	}

	protected void setCompileParameter(String key, boolean value) {
		this.compileParameters.put(key, Boolean.toString(value));
	}

	protected void setCompileParameter(String key, String value) {
		this.compileParameters.put(key, value);
	}

	protected void bindAttribute(int attribute, String variableName) {
		GL20.glBindAttribLocation(this.programID, attribute, variableName);
	}

	protected int getUniformLocation(String uniformName) {
		int location = GL20.glGetUniformLocation(programID, uniformName);
		if (location < 0) {
			ZerraClient.logger().warn("Could not find uniform \'" + uniformName + "\' in shader \'" + this.getClass().getName() + "\'");
		}
		return GL20.glGetUniformLocation(programID, uniformName);
	}

	protected void loadFloat(int location, float value) {
		if (GL20.glGetUniformf(this.programID, location) != value) {
			GL20.glUniform1f(location, value);
		}
	}

	protected void loadInt(int location, int value) {
		if (GL20.glGetUniformi(this.programID, location) != value) {
			GL20.glUniform1i(location, value);
		}
	}

	protected void loadBoolean(int location, boolean value) {
		if (GL20.glGetUniformf(this.programID, location) != (value ? 1 : 0)) {
			GL20.glUniform1f(location, value ? 1 : 0);
		}
	}

	protected void loadVector(int location, Vector2f vector) {
		this.loadVector(location, vector.x, vector.y);
	}

	protected void loadVector(int location, float x, float y) {
		float[] uniformValues = new float[2];
		GL20.glGetUniformfv(this.programID, location, uniformValues);
		if (uniformValues[0] != x || uniformValues[1] != y) {
			GL20.glUniform2f(location, x, y);
		}
	}

	protected void loadVector(int location, Vector3f vector) {
		this.loadVector(location, vector.x, vector.y, vector.z);
	}

	protected void loadVector(int location, float x, float y, float z) {
		float[] uniformValues = new float[3];
		GL20.glGetUniformfv(this.programID, location, uniformValues);
		if (uniformValues[0] != x || uniformValues[1] != y || uniformValues[2] != z) {
			GL20.glUniform3f(location, x, y, z);
		}
	}

	protected void loadVector(int location, Vector4f vector) {
		this.loadVector(location, vector.x, vector.y, vector.z, vector.w);
	}

	protected void loadVector(int location, float x, float y, float z, float w) {
		float[] uniformValues = new float[4];
		GL20.glGetUniformfv(this.programID, location, uniformValues);
		if (uniformValues[0] != x || uniformValues[1] != y || uniformValues[2] != z || uniformValues[3] != w) {
			GL20.glUniform4f(location, x, y, z, w);
		}
	}

	protected void loadMatrix(int location, Matrix3f matrix) {
		GL20.glUniformMatrix3fv(location, false, matrix.get(this.matrix3Buffer));
	}

	// TODO use this later maybe
	// System.out.println(matrix4Buffer.get(0) + " " + matrix4Buffer.get(4) + " " + matrix4Buffer.get(8) + " " + matrix4Buffer.get(12) + "\n" + matrix4Buffer.get(1) + " " + matrix4Buffer.get(5) + " " + matrix4Buffer.get(9) + " " + matrix4Buffer.get(13) + "\n" + matrix4Buffer.get(2) + " " + matrix4Buffer.get(6) + " " + matrix4Buffer.get(10) + " " + matrix4Buffer.get(14) + "\n" + matrix4Buffer.get(3) + " " + matrix4Buffer.get(7) + " " + matrix4Buffer.get(11) + " " + matrix4Buffer.get(15) + "\n");
	protected void loadMatrix(int location, Matrix4f matrix) {
		GL20.glUniformMatrix4fv(location, false, matrix.get(this.matrix4Buffer));
	}

	public void start() {
		GL20.glUseProgram(this.programID);
	}

	public void stop() {
		GL20.glUseProgram(0);
	}

	public void cleanUp() {
		stop();
		GL20.glDetachShader(this.programID, this.vertexShaderID);
		GL20.glDetachShader(this.programID, this.fragmentShaderID);
		GL20.glDeleteShader(this.vertexShaderID);
		GL20.glDeleteShader(this.fragmentShaderID);
		GL20.glDeleteProgram(this.programID);
	}

	private int loadShader(ResourceLocation location, int type) {
		StringBuilder shaderSource = new StringBuilder();
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(location.getInputStream()));
			String line;
			while ((line = reader.readLine()) != null) {
				String compiledLine = line;
				for (String key : this.compileParameters.keySet()) {
					if (line.contains(key)) {
						compiledLine = line.replaceAll(key, this.compileParameters.get(key));
					}
				}
				shaderSource.append(compiledLine).append("\n");
			}
			reader.close();
		} catch (Exception e) {
			ZerraClient.logger().fatal("Could not load shader " + location, e);
			ZerraClient.getInstance().stop();
		}
		int shaderID = GL20.glCreateShader(type);
		GL20.glShaderSource(shaderID, shaderSource);
		GL20.glCompileShader(shaderID);
		if (GL20.glGetShaderi(shaderID, GL20.GL_COMPILE_STATUS) == GL11.GL_FALSE) {
			System.out.println();
			System.err.println("Could not compile \'" + this.getClass().getSimpleName() + "\' " + location + " shader!");
			System.out.println(GL20.glGetShaderInfoLog(shaderID, 500));
			ZerraClient.getInstance().stop();
		}
		return shaderID;
	}
}