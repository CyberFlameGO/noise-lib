package blue.sparse.noiselib.fbm;

public abstract class FBM {
	protected final int octaves;
	protected final float frequency;
	protected final float persistence;
	protected final float lacunarity;

	public FBM(int octaves, float frequency) {
		this(octaves, frequency, 0.5f, 2f);
	}

	public FBM(int octaves, float frequency, float persistence, float lacunarity) {
		this.octaves = octaves;
		this.frequency = frequency;
		this.persistence = persistence;
		this.lacunarity = lacunarity;
	}

	public int getOctaves() {
		return octaves;
	}

	public float getFrequency() {
		return frequency;
	}

	public float getPersistence() {
		return persistence;
	}

	public float getLacunarity() {
		return lacunarity;
	}
}
