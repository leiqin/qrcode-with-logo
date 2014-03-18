package name.leiqin.qrcode;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

public class QrCodeHelper {

	public static double DEFAULT_QRCODE_OVER_WHOLE = 1.0 / 1.162;
	public static double DEFAULT_LOGO_OVER_WHOLE = 1.0 / 5;
	public static double DEFAULT_LOGO_MARGIN_OVER_LOGO = 1.0 / 16;
	public static double DEFAULT_LOGO_ROUND_RADIUS_OVER_LOGO = 1.0 / 10;

	String context;
	BufferedImage logoImage;
	int size;
	double qrCodeOverWhole = DEFAULT_QRCODE_OVER_WHOLE;
	double logoOverWhole = DEFAULT_LOGO_OVER_WHOLE;
	double logoMarginOverLogo = DEFAULT_LOGO_MARGIN_OVER_LOGO;
	double logoRoundRadiusOverLogo = DEFAULT_LOGO_ROUND_RADIUS_OVER_LOGO;

	public BufferedImage qrcodeImg() throws WriterException {
		int qrCodeSize = getQrCodeSize();
		int qrCodeOffset = getQrCodeOffset();

		int logoSize = getLogoSize();
		int logoOffset = getLogoOffset();
		int logoRoundRadius = getLogoRoundRadius();

		int logoMarginRectSize = getLogoMarginRectSize();
		int logoMarginRectOffset = getLogoMarginRectOffset();
		int logoMarginRectRoundRadius = getLogoMarginRectRoundRadius();

		Map<EncodeHintType, Object> hints = new HashMap<>();
		hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
		hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
		hints.put(EncodeHintType.MARGIN, 0);

		MultiFormatWriter mfw = new MultiFormatWriter();
		BitMatrix bm = mfw.encode(context, BarcodeFormat.QR_CODE, qrCodeSize, qrCodeSize, hints);
		
		BufferedImage qrImg = MatrixToImageWriter.toBufferedImage(bm);

		// the result img
		BufferedImage result = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = result.createGraphics();
		// fill all with white color
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, size, size);

		// draw the qrCode img
		g.drawImage(qrImg, qrCodeOffset, qrCodeOffset, null);

		if (logoImage != null) {
			// fill logo margin rect
			g.setComposite(AlphaComposite.Src);
			g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, 
					RenderingHints.VALUE_ANTIALIAS_ON);
			g.setColor(Color.WHITE);
			g.fill(new RoundRectangle2D.Float(logoMarginRectOffset, logoMarginRectOffset, 
					logoMarginRectSize, logoMarginRectSize, 
					logoMarginRectRoundRadius, logoMarginRectRoundRadius));

			// scale the logo img
			BufferedImage scLogo = ImageUtils.cutAndScaleToRoundSquare(logoImage, 
					logoSize, logoRoundRadius);
			// draw the scaled logo img
			g.drawImage(scLogo, logoOffset, logoOffset, null);
		}

		return result;
	}

	public int getQrCodeSize() {
		return (int)Math.round(size * qrCodeOverWhole);
	}

	public int getQrCodeOffset() {
		return (size - getQrCodeSize()) / 2;
	}

	public int getLogoMarginRectSize() {
		int logoSize = getLogoSize();
		int marginSize = (int)Math.round(logoSize * logoMarginOverLogo);
		return logoSize + 2 * marginSize;
	}

	public int getLogoMarginRectOffset() {
		int logoSize = getLogoSize();
		int marginSize = (int)Math.round(logoSize * logoMarginOverLogo);
		int logoOffset = getLogoOffset();
		return logoOffset - marginSize;
	}

	public int getLogoMarginRectRoundRadius() {
		int logoSize = getLogoSize();
		int marginSize = (int)Math.round(logoSize * logoMarginOverLogo);
		int logoRoundRadius = getLogoRoundRadius();
		return logoRoundRadius + marginSize;
	}

	public int getLogoSize() {
		return (int)Math.round(size * logoOverWhole);
	}

	public int getLogoOffset() {
		return (size - getLogoSize()) / 2;
	}

	public int getLogoRoundRadius() {
		return (int)Math.round(getLogoSize() * logoRoundRadiusOverLogo);
	}

	/**
	 * @return the context
	 */
	public String getContext() {
		return context;
	}

	/**
	 * @param context the context to set
	 */
	public void setContext(String context) {
		this.context = context;
	}

	/**
	 * @return the logoImage
	 */
	public BufferedImage getLogoImage() {
		return logoImage;
	}

	/**
	 * @param logoImage the logoImage to set
	 */
	public void setLogoImage(BufferedImage logoImage) {
		this.logoImage = logoImage;
	}

	/**
	 * @return the size
	 */
	public int getSize() {
		return size;
	}

	/**
	 * @param size the size to set
	 */
	public void setSize(int size) {
		this.size = size;
	}

	/**
	 * @return the qrCodeOverWhole
	 */
	public double getQrCodeOverWhole() {
		return qrCodeOverWhole;
	}

	/**
	 * @param qrCodeOverWhole the qrCodeOverWhole to set
	 */
	public void setQrCodeOverWhole(double qrCodeOverWhole) {
		this.qrCodeOverWhole = qrCodeOverWhole;
	}

	/**
	 * @return the logoOverWhole
	 */
	public double getLogoOverWhole() {
		return logoOverWhole;
	}

	/**
	 * @param logoOverWhole the logoOverWhole to set
	 */
	public void setLogoOverWhole(double logoOverWhole) {
		this.logoOverWhole = logoOverWhole;
	}

}
