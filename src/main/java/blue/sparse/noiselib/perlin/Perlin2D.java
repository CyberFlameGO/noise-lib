package blue.sparse.noiselib.perlin;

import blue.sparse.noiselib.math.FloatArrayVectorUtil;

import java.util.Random;

public final class Perlin2D implements Perlin {

	private static final int WRAPAROUND = 256;

	private float[][][] gradientVectors;

	/*
	 * "In order to negate the expensive process of computing new gradients for each grid node, some implementations use
	 * a hash and lookup table for a finite number of precomputed gradient vectors. The use of a hash also permits the
	 * inclusion of a random seed where multiple instances of Perlin noise are required." -
	 * https://en.wikipedia.org/wiki/Perlin_noise#Algorithm_detail
	 */

	public Perlin2D(long seed) {
		Random random = new Random(seed);
		gradientVectors = new float[WRAPAROUND][WRAPAROUND][2];

		for (int x = 0; x < gradientVectors.length; x++) {
			for (int y = 0; y < gradientVectors[x].length; y++) {
				float vx = random.nextFloat() * 2f - 1f;
				float vy = random.nextFloat() * 2f - 1f;

				gradientVectors[x][y] = FloatArrayVectorUtil.normalize(new float[]{vx, vy});
			}
		}
	}

	private float gradient(int ix, int iy, float x, float y) {
		float dx = x - (float) ix;
		float dy = y - (float) iy;
		float[] gradient = gradientVectors[ix & (WRAPAROUND - 1)][iy & (WRAPAROUND - 1)];
		return dx * gradient[0] + dy * gradient[1];
	}

	private float wrap(float f) {
		f = (Math.round(f * 10000f) / 10000f) % WRAPAROUND;
		if (f < 0f) {
			f += WRAPAROUND;
		}

		return f;
	}

	public float noise(float x, float y) {
		x = wrap(x);
		y = wrap(y);

		int x0 = (int) x;
		int x1 = x0 + 1;

		int y0 = (int) y;
		int y1 = y0 + 1;


		float weightX = fade(x - (float) x0);
		float weightY = fade(y - (float) y0);

		float a = gradient(x0, y0, x, y);
		float b = gradient(x1, y0, x, y);
		float ab = lerp(a, b, weightX);

		float c = gradient(x0, y1, x, y);
		float d = gradient(x1, y1, x, y);
		float cd = lerp(c, d, weightX);

		return lerp(ab, cd, weightY);
	}

	public float noise(float x, float y, int octaves) {
		return noise(x, y, octaves, 1.0f, 0.5f);
	}

	public float noise(float x, float y, int octaves, float frequency) {
		return noise(x, y, octaves, frequency, 0.5f);
	}

	public float noise(float x, float y, int octaves, float frequency, float persistence) {
		float value = 0f;

		float amplitude = 1f;
		float amplitudeAccumulation = 0f;

		for (int octave = 0; octave < octaves; octave++) {
			value += noise(x * frequency, y * frequency) * amplitude;

			frequency *= 2;

			amplitudeAccumulation += amplitude;
			amplitude *= persistence;
		}

		return value / amplitudeAccumulation;
	}
}
