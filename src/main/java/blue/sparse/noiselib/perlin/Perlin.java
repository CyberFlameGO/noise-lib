package blue.sparse.noiselib.perlin;

public interface Perlin {

	default float fade(float t) {
		return ((6 * t - 15) * t + 10) * t * t * t;
	}

	default float lerp(float a, float b, float t) {
		return (1.0f - t) * a + t * b;
	}
}
