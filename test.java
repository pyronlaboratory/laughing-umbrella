package com.ch;

import com.ch.math.Matrix4f;
import com.ch.math.Vector3f;

/**
 * Defines an abstract base class for cameras, encapsulating projection, view, and
 * transformation matrices, as well as viewport adjustments. It provides methods for
 * calculating view and projection matrices, adjusting to viewport dimensions, and
 * accessing transformation data.
 */
public abstract class Camera {

	protected Matrix4f projection;
	protected Matrix4f viewProjectionMat4;
	protected CameraStruct values;
	protected Transform transform;

	protected Camera(Matrix4f projection) {
		this.projection = projection;
		transform = new Transform();
	}

	/**
	 * Returns a 4x4 matrix representing the combined view and projection transformations,
	 * or recalculates it if the view projection matrix is null or the transformation has
	 * changed.
	 *
	 * @returns a 4x4 matrix representing the combined view and projection transformations.
	 */
	public Matrix4f getViewProjection() {

		if (viewProjectionMat4 == null || transform.hasChanged()) {
			calculateViewMatrix();
		}

		return viewProjectionMat4;
	}

	/**
	 * Calculates the view matrix by multiplying the camera's rotation and translation
	 * matrices with the projection matrix. The result is stored in the `viewProjectionMat4`
	 * variable. The function returns the transformed view matrix.
	 *
	 * @returns a 4x4 matrix representing the combined view-projection transformation of
	 * the camera.
	 */
	public Matrix4f calculateViewMatrix() {

		Matrix4f cameraRotation = transform.getTransformedRot().conjugate().toRotationMatrix();
		Matrix4f cameraTranslation = getTranslationMatrix();

		return (viewProjectionMat4 = projection.mul(cameraRotation.mul(cameraTranslation)));

	}


	/**
	 * Calculates a translation matrix based on the negative transformed position of a camera.
	 *
	 * @returns a 4x4 translation matrix representing the camera's position in 3D space.
	 */
	public Matrix4f getTranslationMatrix() {
		Vector3f cameraPos = transform.getTransformedPos().mul(-1);
		return new Matrix4f().initTranslation(cameraPos.getX(), cameraPos.getY(), cameraPos.getZ());
	}

	/**
	 * Returns the value of the `transform` field.
	 * It provides read-only access to the `transform` object.
	 *
	 * @returns an object of type `Transform`, referencing the `transform` variable.
	 */
	public Transform getTransform() {
		return transform;
	}
	
	public abstract Matrix4f calculateProjectionMatrix(CameraStruct data);

	public abstract void adjustToViewport(int width, int height);

	/**
	 * Defines an abstract structure for camera-related data,
	 * with a method to convert it into a Matrix4f representation.
	 */
	protected abstract class CameraStruct {

		protected abstract Matrix4f getAsMatrix4();

	}

}
