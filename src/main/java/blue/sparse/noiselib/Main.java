package blue.sparse.noiselib;

import blue.sparse.noiselib.math.FlatVectorArray;
import blue.sparse.noiselib.perlin.experimental.PerlinND;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.concurrent.ThreadLocalRandom;

public class Main {
	public static void main(String[] args) {
		ThreadLocalRandom current = ThreadLocalRandom.current();
		FlatVectorArray array = new FlatVectorArray(2, 3);

		for (int x = 0; x < 3; x++) {
			for (int z = 0; z < 3; z++) {
				float[] vector = new float[]{current.nextFloat() * 100f, current.nextFloat() * 100f};
				array.setVector(vector, x, z);
				System.out.println("ADDED " + Arrays.toString(vector));
			}
		}
		System.out.println("-----------");
		System.out.println("-----------");
		System.out.println("-----------");

		for (int x = 0; x < 3; x++) {
			for (int z = 0; z < 3; z++) {
				float[] vector = array.getVector(x, z);
				System.out.println("RETRIEVED " + Arrays.toString(vector));

			}
		}

		System.out.println("-----------");
		System.out.println("-----------");
		System.out.println("-----------");
		for (int i = 0; i < array.length(); i++) {
			float[] vector = array.getVector(array.getCoordinatesAt(i));
			System.out.println("RETRIEVED BY COORD " + Arrays.toString(vector));

		}
		PerlinND perlin = new PerlinND(69L, 2);
		BufferedImage im = new BufferedImage(1024, 1024, BufferedImage.TYPE_BYTE_GRAY);

		float min = Float.MAX_VALUE;
		float max = Float.MIN_VALUE;

		for (int x = 0; x < 1024; x++) {
			for (int z = 0; z < 1024; z++) {
				float noise = perlin.noise(x * 0.025f, z * 0.025f);

				if (noise > max) {
					max = noise;
				}

				if (noise < min) {
					min = noise;
				}

				noise = noise / 2f + 0.5f;
				im.setRGB(x, z, new Color(noise, noise, noise).getRGB());
			}
		}

		System.out.println("min = " + min);
		System.out.println("max = " + max);

		try {
			ImageIO.write(im, "PNG", new File("output.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
