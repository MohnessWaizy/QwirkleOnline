package model;

import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.zip.ZipInputStream;

import javax.imageio.ImageIO;

import main.App;


/**
 * The class that loads the Images for the Game from a zip file
 * 
 *
 */
public class ImageLoader {

	private BufferedImage[][] images;
	private static int[][] colorMapping;

	/**
	 * Konstruktor for the ImageLoader class
	 * In which all images get loaded
	 * 
	 * @param url the url from the zip file 
	 */
	public ImageLoader(String url) {
		colorMapping = makeColor();
		images = loadToColor(colorMapping, url);
	}

	/**
	 * Getter for the Buffered Image Array
	 * BufferedImage[indexOfColor][indexOfShape] 
	 * 
	 * @return BufferedImage[][]
	 */
	public BufferedImage[][] getImages() {
		return this.images;
	}

	
	/**
	 * Loads the Images to the Colored Images
	 * 
	 * @param colorMapping
	 * @return BufferedImage[][] The Colorised Images
	 */
	private BufferedImage[][] loadToColor(int[][] colorMapping, String url) {

		try {
			BufferedImage[] img = loadImages(url);
			BufferedImage[][] colorImages = new BufferedImage[12][12];

			for (int i = 0; i < 12; i++) {
				int r = colorMapping[i][0];
				int g = colorMapping[i][1];
				int b = colorMapping[i][2];
				for (int j = 0; j < 12; j++) {
					colorImages[i][j] = colorTile(img[j], r, g, b);
				}
			}
			return colorImages;
		} catch (IOException e) {
			return null;
		} 

	}

	/**
	 * Loads the images and checks if there are 12 images inside the File
	 * 
	 * @return
	 * @throws IOException
	 * @throws URISyntaxException
	 */
	private BufferedImage[] loadImages(String url) throws IOException {
		ArrayList<BufferedImage> textures = loadZip(
				 App.class.getClassLoader().getResourceAsStream(url));
		if (textures.size() == 12) {
			return textures.toArray(new BufferedImage[12]);
		} else {
			throw new IOException("There are more or less than 12 pictures");
		}

	}

	
	/**
	 * Loads the ZipFile and Returns a ArrayList of Buffered Images
	 * 
	 * @param Input Stream file
	 * @return ArrayList<BufferedImage> The Images inside of the zip
	 * @throws IOException
	 */
	private ArrayList<BufferedImage> loadZip(InputStream file) throws IOException {
		ZipInputStream zip = new ZipInputStream(file);

			ArrayList<BufferedImage> textures = new ArrayList<BufferedImage>();
			while (zip.getNextEntry() != null) {
				if (zip.available() > 0) {
					textures.add(convertToRgb(ImageIO.read(zip)));
				}
			}

			return textures;
		
	}

	/**
	 * Converts a BufferedImage to a Rgb BufferedImage
	 * 
	 * @param BufferedImage img
	 * @return BufferedImage
	 */
	private BufferedImage convertToRgb(BufferedImage img) {
		BufferedImage newImg = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_ARGB);
		newImg.getGraphics().drawImage(img, 0, 0, null);

		return newImg;
	}

	
	/**
	 * Colors the given BufferedImage with the RGB value
	 * 
	 * @param img
	 * @param R Red Value
	 * @param G Yellow Value
	 * @param B Blue Value
	 * @return
	 */
	private BufferedImage colorTile(BufferedImage img, int R, int G, int B) {
		BufferedImage newImage = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_ARGB);

		int width = img.getWidth();
		int height = img.getHeight();

		WritableRaster raster = img.getRaster();
		WritableRaster raster2 = newImage.getRaster();

		int[] color = { R, G, B, 255 };

		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {

				int[] pixels = raster.getPixel(x, y, new int[4]);
				if (pixels[0] != 255 || pixels[1] != 255 || pixels[2] != 255) {
					raster2.setPixel(x, y, color);
				} else {
					raster2.setPixel(x, y, pixels);
				}

			}

		}
		return newImage;

	}

	/**
	 * Makes the Color mapping Array
	 * @return int[][]
	 */
	private static int[][] makeColor() {
		int[][] colors = new int[12][3];

		colors[0][0] = 204;
		colors[0][1] = 0;
		colors[0][2] = 0;

		colors[1][0] = 153;
		colors[1][1] = 102;
		colors[1][2] = 0;

		colors[2][0] = 204;
		colors[2][1] = 0;
		colors[2][2] = 102;

		colors[3][0] = 204;
		colors[3][1] = 0;
		colors[3][2] = 204;

		colors[4][0] = 102;
		colors[4][1] = 0;
		colors[4][2] = 204;

		colors[5][0] = 178;
		colors[5][1] = 178;
		colors[5][2] = 178;

		colors[6][0] = 0;
		colors[6][1] = 102;
		colors[6][2] = 204;

		colors[7][0] = 0;
		colors[7][1] = 153;
		colors[7][2] = 153;

		colors[8][0] = 0;
		colors[8][1] = 153;
		colors[8][2] = 51;

		colors[9][0] = 102;
		colors[9][1] = 204;
		colors[9][2] = 0;

		colors[10][0] = 102;
		colors[10][1] = 51;
		colors[10][2] = 0;

		colors[11][0] = 204;
		colors[11][1] = 153;
		colors[11][2] = 0;

		return colors;

	}
}