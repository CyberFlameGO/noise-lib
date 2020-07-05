package blue.sparse.noiselib.perlin.experimental;

import blue.sparse.noiselib.math.FlatVectorArray;
import blue.sparse.noiselib.math.FloatArrayVectorUtil;
import jdk.jfr.Experimental;

import java.util.Random;

@Deprecated
@Experimental
public final class PerlinND {
	private static final int WRAPAROUND = 256;

	private FlatVectorArray gradientVectors;

	private long seed;
	private int dimensions;

	public PerlinND(long seed, int dimensions) {
		this.seed = seed;
		this.dimensions = dimensions;
		this.gradientVectors = new FlatVectorArray(dimensions, WRAPAROUND);

		Random random = new Random(seed);
		for (int i = 0; i < gradientVectors.length(); i++) {
			float[] vector = new float[dimensions];
			for (int j = 0; j < dimensions; j++) {
				vector[j] = random.nextFloat() * 2f - 1f;
			}

			gradientVectors.setVector(FloatArrayVectorUtil.normalize(vector), gradientVectors.getCoordinatesAt(i));
		}
	}

	private float gradient(int[] pos, float[] vector) {
		int[] wrappedCoords = new int[dimensions];
		float[] other = new float[dimensions];

		for (int i = 0; i < pos.length; i++) {
			float f = vector[i];
			int d = pos[i];

			float a = f - (float) d;
			other[i] = a;
			wrappedCoords[i] = d & (WRAPAROUND - 1);
		}

		return FloatArrayVectorUtil.dot(other, gradientVectors.getVector(wrappedCoords));
	}

	private float fade(float t) {
		return ((6 * t - 15) * t + 10) * t * t * t;
	}

	public float noise(float... vector) {
		if (vector.length != dimensions) {
			throw new IllegalArgumentException();
		}

		for (int i = 0; i < vector.length; i++) {
			float f = vector[i];
			f = (Math.round(f * 10000f) / 10000f) % WRAPAROUND;
			if (f < 0f) {
				f += WRAPAROUND;
			}
			vector[i] = f;
		}

		int[] cellMin = new int[dimensions];
		int[] cellMax = new int[dimensions];
		float[] weights = new float[dimensions];

		for (int i = 0; i < vector.length; i++) {
			float v = vector[i];
			int v0 = (int) v;
			cellMin[i] = v0;
			cellMax[i] = v0 + 1;
			weights[i] = fade(v - (float) v0);
		}

		int cornerCount = (int) Math.pow(2, dimensions);
		int[][] corners = new int[cornerCount][dimensions];
		float[] alphabet = new float[cornerCount];

		for (int i = 0; i < cornerCount; i++) {
			int[] corner = corners[i];
			for (int j = 0; j < corner.length; j++) {
				corner[j] = (i & (1 << j)) == 0 ? cellMin[j] : cellMax[j];
			}

			alphabet[i] = gradient(corner, vector);
		}

		int iterateCount = 0;
		while (alphabet.length != 1) {
			float[] newAlphabet = new float[alphabet.length / 2];
			for (int i = 0; i < newAlphabet.length; i++) {
				newAlphabet[i] = FloatArrayVectorUtil.lerp(
						alphabet[i * 2],
						alphabet[i * 2 + 1],
						weights[iterateCount]
				);
			}

			alphabet = newAlphabet;
			iterateCount++;
		}

		float range = (float) Math.sqrt(dimensions) / 2;
		return alphabet[0] / range;
	}

	public long getSeed() {
		return seed;
	}

	public int getDimensions() {
		return dimensions;
	}
}