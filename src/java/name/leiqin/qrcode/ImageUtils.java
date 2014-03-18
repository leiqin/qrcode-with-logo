package name.leiqin.qrcode;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;

public class ImageUtils {

	public static BufferedImage cutAndScaleToRoundSquare(BufferedImage src, int size, int radius) {
		BufferedImage bi = cutAndScaleToSquare(src, size);
		BufferedImage result = makeRoundedCorner(bi, radius);
		return result;
	}

	public static BufferedImage cutAndScaleToSquare(BufferedImage src, int size) {
		int width = src.getWidth();
		double scale = size * 1.0 / width;
		BufferedImage result = scale(src, scale);
		return result;
	}

	public static BufferedImage cutToSquare(BufferedImage src) {
		int width = src.getWidth();
		int height = src.getHeight();
		if (width == height)
			return src;
		BufferedImage result = null;
		if (width > height) {
			int cut = (width - height) / 2;
			result = src.getSubimage(cut, 0, height, height);
		} else {
			int cut = (height - width) / 2;
			result = src.getSubimage(0, cut, width, width);
		}
		return result;
	}

	/**
	 * Reference to http://stackoverflow.com/questions/4216123/how-to-scale-a-bufferedimage
	 */
	public static BufferedImage scale(BufferedImage src, double scale) {
		int width = src.getWidth();
		int height = src.getHeight();
		AffineTransform at = new AffineTransform();
		at.setToScale(scale, scale);
		AffineTransformOp ato = new AffineTransformOp(at, AffineTransformOp.TYPE_BILINEAR);
		BufferedImage dest = new BufferedImage(
				(int)Math.round(width * scale), 
				(int)Math.round(height * scale), 
				BufferedImage.TYPE_INT_ARGB);
		dest = ato.filter(src, dest);
		return dest;
	}

	/**
	 * Copy from http://stackoverflow.com/questions/7603400/how-to-make-a-rounded-corner-image-in-java
	 */
	public static BufferedImage makeRoundedCorner(BufferedImage image, int cornerRadius) {
		int w = image.getWidth();
		int h = image.getHeight();
		BufferedImage output = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);

		Graphics2D g2 = output.createGraphics();

		// This is what we want, but it only does hard-clipping, i.e. aliasing
		// g2.setClip(new RoundRectangle2D ...)

		// so instead fake soft-clipping by first drawing the desired clip shape
		// in fully opaque white with antialiasing enabled...
		g2.setComposite(AlphaComposite.Src);
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2.setColor(Color.WHITE);
		g2.fill(new RoundRectangle2D.Float(0, 0, w, h, cornerRadius, cornerRadius));

		// ... then compositing the image on top,
		// using the white shape from above as alpha source
		g2.setComposite(AlphaComposite.SrcAtop);
		g2.drawImage(image, 0, 0, null);

		g2.dispose();

		return output;
	}

	/**
	 * Copy from http://stackoverflow.com/questions/13605248/java-converting-image-to-bufferedimage
	 *
	 * Converts a given Image into a BufferedImage
	 *
	 * @param img The Image to be converted
	 * @return The converted BufferedImage
	 */
	public static BufferedImage toBufferedImage(Image img) {
		if (img instanceof BufferedImage)
		{
			return (BufferedImage) img;
		}

		// Create a buffered image with transparency
		BufferedImage bimage = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_ARGB);

		// Draw the image on to the buffered image
		Graphics2D bGr = bimage.createGraphics();
		bGr.drawImage(img, 0, 0, null);
		bGr.dispose();

		// Return the buffered image
		return bimage;
	}
}
