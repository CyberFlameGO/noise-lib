package blue.sparse.noiselib.math;

public final class FloatArrayVectorUtil {
	private FloatArrayVectorUtil() {
	}

	public static float lengthSquared(float[] vector) {
		float length = 0f;

		for (float f : vector) {
			length += f * f;
		}

		return length;
	}

	public static float length(float[] vector) {
		return (float) Math.sqrt(lengthSquared(vector));
	}

	public static float[] normalize(float[] vector) {
		float length = length(vector);

		for (int i = 0; i < vector.length; i++) {
			vector[i] = vector[i] / length;
		}

		return vector;
	}

	public static float dot(float[] aVector, float[] bVector) {
		float dot = 0f;

		for (int i = 0; i < aVector.length; i++) {
			dot += aVector[i] * bVector[i];
		}

		return dot;
	}

	public static float lerp(float a, float b, float t) {
		return (1.0f - t) * a + t * b;
//		return a + t * (b - a);
	}
}
