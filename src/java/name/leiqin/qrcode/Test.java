package name.leiqin.qrcode;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

public class Test {

	public static void main(String[] args) throws WriterException, IOException {
		String context = "我们是害虫";
		for (int i = 0; i < 0; i++)
			context += context;
		int width = 370;
		int height = 370;
		String path = "/tmp/a.png";
		BufferedImage result = null;

		Map<EncodeHintType, Object> hints = new HashMap<>();
		hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
		hints.put(EncodeHintType.MARGIN, 0);

		MultiFormatWriter mfw = new MultiFormatWriter();
		BitMatrix bm = mfw.encode(context, BarcodeFormat.QR_CODE, width, height, hints);
		
		BufferedImage bi = MatrixToImageWriter.toBufferedImage(bm);

		BufferedImage abi = new BufferedImage(430, 430, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = abi.createGraphics();
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, 430, 430);
		g.drawImage(bi,30, 30, null);

		AffineTransform at = new AffineTransform();
		at.setToScale(2.0, 2.0);
		AffineTransformOp ato = new AffineTransformOp(at, AffineTransformOp.TYPE_BILINEAR);
		BufferedImage sbi = new BufferedImage(1000, 1000, BufferedImage.TYPE_INT_ARGB);
		sbi = ato.filter(abi, sbi);

		result = sbi;
		OutputStream os = new FileOutputStream(path);
		ImageIO.write(result, "png", os);
		//MatrixToImageWriter.writeToPath(bm, "png", Paths.get(path));
	}
}
