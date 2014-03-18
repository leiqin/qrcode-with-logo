package name.leiqin.qrcode;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

public class Test extends JComponent {

	private BufferedImage bi = null;
	private int width = 0;
	private int height = 0;

	@Override
    public Dimension getMinimumSize() {
        return new Dimension(150, 150);
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(width, height);
    }

	@Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(bi,0,0,this);
    }

	public static void main(String[] args) throws WriterException, IOException {
		String context = "我们是害虫，我们是益虫！";
		int width = 370;
		int height = 370;
		String path = "/tmp/a.png";
		BufferedImage result = null;

		Map<EncodeHintType, Object> hints = new HashMap<>();
		hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
		hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
		hints.put(EncodeHintType.MARGIN, 0);

		MultiFormatWriter mfw = new MultiFormatWriter();
		BitMatrix bm = mfw.encode(context, BarcodeFormat.QR_CODE, width, height, hints);
		
		BufferedImage bi = MatrixToImageWriter.toBufferedImage(bm);

		BufferedImage abi = new BufferedImage(430, 430, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = abi.createGraphics();
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, 430, 430);
		g.drawImage(bi,30, 30, null);

		String logoPath = Test.class.getClassLoader().getResource("favicon.jpg").getFile();
		BufferedImage logo = ImageIO.read(new File(logoPath));
		BufferedImage scLogo = ImageUtils.cutAndScaleToRoundSquare(logo, 88);
		g.fillRoundRect(166, 166, 99, 99, 15, 15);
		g.drawImage(scLogo, 171, 171, null);

		result = abi;

		Test t = new Test();
		t.width = 430;
		t.height = 430;
		t.bi = result;
        JFrame frame = new JFrame("HelloWorldQrcode");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
 
		frame.add(t);
 
        //Display the window.
        frame.pack();
        frame.setVisible(true);
		
	}

}
