package com.zerra.client.util;

import java.util.HashMap;
import java.util.Map;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;

import com.zerra.client.view.ICamera;

/**
 * <em><b>Copyright (c) 2018 The Zerra Team.</b></em>
 * 
 * <br>
 * </br>
 * 
 * Contains math helpers to help when using the game.
 * 
 * @author Ocelot5836
 */
public class Maths {

	private static final Map<String, Matrix4f> TRANSFORMATION_MATRICES = new HashMap<String, Matrix4f>();
	private static final Matrix4f VIEW_MATRIX = new Matrix4f();

	/**
	 * Creates a 2d transformation matrix.
	 * 
	 * @param position
	 *            The translation in the x axis
	 * @param rotation
	 *            The rotation
	 * @param scale
	 *            The scale
	 * @return The matrix created with all the data stored inside
	 */
	public static Matrix4f createTransformationMatrix(Vector2f position, Vector2f rotation, Vector2f scale) {
		return createTransformationMatrix(position.x, position.y, rotation.x, rotation.y, scale.x, scale.y);
	}

	/**
	 * Creates a 2d transformation matrix.
	 * 
	 * @param x
	 *            The translation in the x axis
	 * @param y
	 *            The translation in the y axis
	 * @param rotationX
	 *            The rotation in the x axis
	 * @param rotationY
	 *            The rotation in the y axis
	 * @param scaleX
	 *            The scale in the x axis
	 * @param scaleY
	 *            The scale in the y axis
	 * @return The matrix created with all the data stored inside
	 */
	public static Matrix4f createTransformationMatrix(float x, float y, float rotationX, float rotationY, float scaleX, float scaleY) {
		Matrix4f matrix = TRANSFORMATION_MATRICES.get(x + "," + y + "," + rotationX + "," + rotationY + "," + scaleX + "," + scaleY);
		if (matrix == null) {
			matrix = new Matrix4f();
			matrix.identity();
			matrix.translate(x, y, -1);
			matrix.rotate((float) Math.toRadians(rotationX), 1, 0, 0);
			matrix.rotate((float) Math.toRadians(rotationY), 0, 1, 0);
			matrix.scale(scaleX, scaleY, 1);
			TRANSFORMATION_MATRICES.put(x + "," + y + "," + rotationX + "," + rotationY + "," + scaleX + "," + scaleY, matrix);
		}
		return matrix;
	}

	/**
	 * Creates a 3d transformation matrix.
	 * 
	 * @param position
	 *            The translation in the x axis
	 * @param rotation
	 *            The rotation
	 * @param scale
	 *            The scale
	 * @return The matrix created with all the data stored inside
	 */
	public static Matrix4f createTransformationMatrix(Vector3f position, Vector3f rotation, Vector3f scale) {
		return createTransformationMatrix(position.x, position.y, position.z, rotation.x, rotation.y, rotation.z, scale.x, scale.y, scale.z);
	}

	/**
	 * Creates a 3d transformation matrix.
	 * 
	 * @param x
	 *            The translation in the x axis
	 * @param y
	 *            The translation in the y axis
	 * @param z
	 *            The translation in the z axis
	 * @param rotationX
	 *            The rotation in the x axis
	 * @param rotationY
	 *            The rotation in the y axis
	 * @param rotationZ
	 *            The rotation in the z axis
	 * @param scaleX
	 *            The scale in the x axis
	 * @param scaleY
	 *            The scale in the y axis
	 * @param scaleZ
	 *            The scale in the z axis
	 * @return The matrix created with all the data stored inside
	 */
	public static Matrix4f createTransformationMatrix(float x, float y, float z, float rotationX, float rotationY, float rotationZ, float scaleX, float scaleY, float scaleZ) {
		Matrix4f matrix = TRANSFORMATION_MATRICES.get(x + "," + y + "," + z + "," + rotationX + "," + rotationY + "," + rotationZ + "," + scaleX + "," + scaleY + "," + scaleZ);
		if (matrix == null) {
			matrix = new Matrix4f();
			matrix.identity();
			matrix.translate(x, y, z);
			matrix.rotate((float) Math.toRadians(rotationX), 1, 0, 0);
			matrix.rotate((float) Math.toRadians(rotationY), 0, 1, 0);
			matrix.rotate((float) Math.toRadians(rotationZ), 0, 0, 1);
			matrix.scale(scaleX, scaleY, scaleZ);
			TRANSFORMATION_MATRICES.put(x + "," + y + "," + z + "," + rotationX + "," + rotationY + "," + rotationZ + "," + scaleX + "," + scaleY + "," + scaleZ, matrix);
		}
		return matrix;
	}

	/**
	 * Creates the matrix used to move the world around based on the camera position.
	 *
	 * @param camera
	 *            The camera to revolve around
	 * @param output
	 *            The output matrix that the camera info will go into
	 * @return The matrix returned or created if null was passed for the output
	 */
	public static Matrix4f createViewMatrix(ICamera camera) {
		VIEW_MATRIX.identity();
		VIEW_MATRIX.rotate((float) Math.toRadians(camera.getRotation().x), 1, 0, 0);
		VIEW_MATRIX.rotate((float) Math.toRadians(camera.getRotation().y), 0, 1, 0);
		VIEW_MATRIX.rotate((float) Math.toRadians(camera.getRotation().z), 0, 0, 1);
		VIEW_MATRIX.translate(-camera.getPosition().x, -camera.getPosition().y, -camera.getPosition().z);
		return VIEW_MATRIX;
	}

	/**
	 * A method for clamping a variable between two values.
	 * 
	 * @param value
	 *            The value to be clamped.
	 * @param min
	 *            The lowest the value should go.
	 * @param max
	 *            The highest the value should go.
	 * @return The clamped value.
	 */
	public static double clamp(double value, double min, double max) {
		if (value < min) {
			value = min;
		}
		if (value > max) {
			value = max;
		}
		return value;
	}

	/**
	 * Interpolates between point a and b
	 * 
	 * @param a
	 *            The first position
	 * @param b
	 *            The second position
	 * @param blend
	 *            The amount to interpolate
	 * @return The interpolated value
	 */
	public static float interpolate(float a, float b, float blend) {
		double theta = blend * Math.PI;
		float f = (float) (1f - Math.cos(theta)) * 0.5f;
		return a * (1f - f) + b * f;
	}
}