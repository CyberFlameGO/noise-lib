package blue.sparse.noiselib.math;

import java.util.Arrays;

public final class FlatVectorArray {

	private final float[] array;
	private final int[] pCount;
	private final int dimensions;
	private final int count;


	public FlatVectorArray(int dimensions, int count) {
		this.dimensions = dimensions;
		this.count = count;
		array = new float[getFloatArraySize(dimensions, count)];


		pCount = new int[dimensions + 1];
		pCount[0] = 1;
		for (int i = 1; i < pCount.length; i++) {
			pCount[i] = pCount[i - 1] * count;
		}
	}

	private static int getFloatArraySize(int dimensions, int count) {
		return (int) (Math.pow(count, dimensions) * dimensions);
	}

	public float getComponent(int axisIndex, int... coordinates) {
		return array[getIndex(axisIndex, coordinates)];
	}

	public float[] getVector(int... coordinates) {
		float[] floats = new float[dimensions];

		for (int i = 0; i < dimensions; i++) {
			floats[i] = getComponent(i, coordinates);
		}

		return floats;
	}

	public void setComponent(float value, int axisIndex, int... coordinates) {
		array[getIndex(axisIndex, coordinates)] = value;
	}

	public void setVector(float[] value, int... coordinates) {
		if (value.length != dimensions) {
			throw new IllegalArgumentException("Vector length " + value.length + " is not equal to dimensions " + dimensions + ".");
		}

		for (int i = 0; i < value.length; i++) {
			float f = value[i];
			setComponent(f, i, coordinates);
		}
	}

	public int[] getCoordinatesAt(int index) {
		int[] coordinates = new int[dimensions];

		for (int i = 0; i < coordinates.length; i++) {
			coordinates[coordinates.length - i - 1] = index % count;
			index /= count;
		}

		return coordinates;
	}

	public int length() {
		return pCount[pCount.length - 1];
	}

	private int getIndex(int axisIndex, int... coordinates) {
		int length = coordinates.length;

		if (length != dimensions) {
			throw new IllegalArgumentException("Coordinates [" + Arrays.toString(coordinates) + "] does not reflect dimension count " + dimensions + ".");
		}

		int index = 0;
		for (int i = 0; i < coordinates.length; i++) {
			index += coordinates[i] * pCount[i];
		}

		return index * dimensions + axisIndex;
	}

	public int getDimensions() {
		return dimensions;
	}

	public int getCount() {
		return count;
	}
}
