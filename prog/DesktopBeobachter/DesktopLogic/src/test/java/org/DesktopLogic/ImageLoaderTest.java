package org.DesktopLogic;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.awt.image.BufferedImage;
import java.util.zip.ZipInputStream;

import org.junit.Test;

import main.App;
import model.ImageLoader;

public class ImageLoaderTest {

	@Test
	public void testImageLoader() {

		if (new ZipInputStream(App.class.getClassLoader().getResourceAsStream("tileimages/texture.zip")) != null) {

			ImageLoader imageLoader = new ImageLoader("tileimages/texture.zip");

			BufferedImage[][] images = imageLoader.getImages();

			assertFalse("Make Color is null", images == null);
			assertFalse("There are less than 12 Images loaded", images.length < 12);
			assertFalse("There are more than 12 Images loaded", images.length > 12);

			for (BufferedImage[] bufferedImages : images) {
				for (BufferedImage bufferedImage : bufferedImages) {
					assertFalse("Image is not ARGB", bufferedImage.getType() != BufferedImage.TYPE_INT_ARGB);
				}
			}

			imageLoader = new ImageLoader("");

			images = imageLoader.getImages();
			assertTrue(images == null);

		}
	}

}
