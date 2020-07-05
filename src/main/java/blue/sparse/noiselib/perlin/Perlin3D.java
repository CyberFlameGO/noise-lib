package blue.sparse.noiselib.perlin;

import blue.sparse.noiselib.math.FloatArrayVectorUtil;

import java.util.Random;

public final class Perlin3D implements Perlin {

	private static final int WRAPAROUND = 256;

	private float[][][][] gradientVectors;

	/*
	 * "In order to negate the expensive process of computing new gradients for each grid node, some implementations use
	 * a hash and lookup table for a finite number of precomputed gradient vectors. The use of a hash also permits the
	 * inclusion of a random seed where multiple instances of Perlin noise are required." -
	 * https://en.wikipedia.org/wiki/Perlin_noise#Algorithm_detail
	 */

	public Perlin3D(long seed) {
		Random random = new Random(seed);
		gradientVectors = new float[WRAPAROUND][WRAPAROUND][WRAPAROUND][3];

		for (int x = 0; x < gradientVectors.length; x++) {
			for (int y = 0; y < gradientVectors[x].length; y++) {
				for (int z = 0; z < gradientVectors[y].length; z++) {

					float vx = random.nextFloat() * 2f - 1f;
					float vy = random.nextFloat() * 2f - 1f;
					float vz = random.nextFloat() * 2f - 1f;

					gradientVectors[x][y][z] = FloatArrayVectorUtil.normalize(new float[]{vx, vy, vz});
				}
			}
		}
	}

	private float gradient(int ix, int iy, int iz, float x, float y, float z) {
		float dx = x - (float) ix;
		float dy = y - (float) iy;
		float dz = z - (float) iz;
		float[] gradient = gradientVectors[ix & (WRAPAROUND - 1)][iy & (WRAPAROUND - 1)][iz & (WRAPAROUND - 1)];
		return dx * gradient[0] + dy * gradient[1] + dz * gradient[2];
	}

	private float wrap(float f) {
		f = (Math.round(f * 10000f) / 10000f) % WRAPAROUND;
		if (f < 0f) {
			f += WRAPAROUND;
		}

		return f;
	}

	public float noise(float x, float y, float z) {
		x = wrap(x);
		y = wrap(y);
		z = wrap(z);

		int x0 = (int) x;
		int x1 = x0 + 1;

		int y0 = (int) y;
		int y1 = y0 + 1;

		int z0 = (int) z;
		int z1 = (z0 + 1);


		float weightX = fade(x - (float) x0);
		float weightY = fade(y - (float) y0);
		float weightZ = fade(z - (float) z0);

		float a = gradient(x0, y0, z0, x, y, z);
		float b = gradient(x1, y0, z0, x, y, z);
		float ab = lerp(a, b, weightX);

		float c = gradient(x0, y1, z0, x, y, z);
		float d = gradient(x1, y1, z0, x, y, z);
		float cd = lerp(c, d, weightX);

		float abcd = lerp(ab, cd, weightY);

		float e = gradient(x0, y0, z1, x, y, z);
		float f = gradient(x1, y0, z1, x, y, z);
		float ef = lerp(e, f, weightX);

		float g = gradient(x0, y1, z1, x, y, z);
		float h = gradient(x1, y1, z1, x, y, z);
		float gh = lerp(g, h, weightX);

		float efgh = lerp(ef, gh, weightY);

		return lerp(abcd, efgh, weightZ);
	}

	public float noise(float x, float y, float z, int octaves) {
		return noise(x, y, z, octaves, 1.0f, 0.5f);
	}

	public float noise(float x, float y, float z, int octaves, float frequency) {
		return noise(x, y, z, octaves, frequency, 0.5f);
	}

	public float noise(float x, float y, float z, int octaves, float frequency, float persistence) {
		float value = 0f;

		float amplitude = 1f;
		float amplitudeAccumulation = 0f;

		for (int octave = 0; octave < octaves; octave++) {
			value += noise(x * frequency, y * frequency, z * frequency) * amplitude;

			frequency *= 2;

			amplitudeAccumulation += amplitude;
			amplitude *= persistence;
		}

		return value / amplitudeAccumulation;
	}
}
