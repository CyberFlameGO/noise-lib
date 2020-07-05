package blue.sparse.noiselib.fbm;


import blue.sparse.noiselib.Noise3D;

public final class FBM3D extends FBM implements Noise3D {

	public FBM3D(int octaves, float frequency) {
		super(octaves, frequency);
	}

	public FBM3D(int octaves, float frequency, float persistence, float lacunarity) {
		super(octaves, frequency, persistence, lacunarity);
	}

	@Override
	public float noise(float x, float y, float z) {
		float value = 0f;

		float amplitude = 1f;
		float amplitudeAccumulation = 0f;
		float frequency = this.frequency;

		for (int octave = 0; octave < octaves; octave++) {
			value += noise(x * frequency, y * frequency, z * frequency) * amplitude;

			frequency *= lacunarity;

			amplitudeAccumulation += amplitude;
			amplitude *= persistence;
		}

		return value / amplitudeAccumulation;
	}
}
