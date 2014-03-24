package name.leiqin.qrcode;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;
import javax.swing.JComponent;
import javax.swing.JFrame;

import com.google.zxing.WriterException;

public class Cmd extends JComponent {

	private BufferedImage bi = null;
	private int width = 0;
	private int height = 0;

	@Override
	public Dimension getPreferredSize() {
		return new Dimension(width, height);
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawImage(bi, 0, 0, this);
	}

	public static BufferedImage getQrCodeImg(String context, int size,
			BufferedImage logo) throws WriterException {
		QRCodeHelper qh = new QRCodeHelper();
		qh.setContext(context);
		qh.setSize(size);
		qh.setLogoImage(logo);
		return qh.qrcodeImg();
	}

	public static void writeToFile(BufferedImage img, String path)
			throws IOException {
		ImageIO.write(img, "png", new File(path));
	}

	public static void showWithSwing(BufferedImage img) {
		Cmd t = new Cmd();
		t.width = img.getWidth();
		t.height = img.getHeight();
		t.bi = img;
		JFrame frame = new JFrame("HelloWorldQrcode");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		frame.add(t);

		//Display the window.
		frame.pack();
		frame.setVisible(true);
	}

	public static BufferedImage getImageFromPath(String path)
			throws IOException {
		if (path == null || path.trim().length() == 0)
			return null;
		if (path.startsWith("http://") || path.startsWith("https://"))
			return getImageFromHttp(path);
		BufferedImage img = ImageIO.read(new File(path));
		return img;
	}

	public static BufferedImage getImageFromHttp(String url)
			throws IOException {
		URL u = new URL(url);
		URLConnection c = u.openConnection();
		c.connect();
		InputStream is = c.getInputStream();
		BufferedImage img = ImageIO.read(is);
		return img;
	}

	public static void printUsage() {
		System.out.println("Usage:");
		System.out.println("    mvn exec:java -Dexec.args='[[[output] size] logopath] context'");
		System.out.println();
		System.out.println("Args:");
		System.out.println("    output     输出文件，png 格式，省略则会使用 swing 显示");
		System.out.println("    size       图片大小，缺省为 500");
		System.out.println("    logopath   logo 文件的地址，可以是 url");
		System.out.println("    context    二维码中所要包含的内容");
	}

	public static void main(String[] args) throws Exception {
		if (args.length == 0 || args.length > 4) {
			printUsage();
			System.exit(0);
		}
		String context = null;
		int size = 500;
		String logopath = null;
		String output = null;

		if (args.length == 1) {
			context = args[0];
		} else if (args.length == 2) {
			if (Pattern.matches("\\d+", args[0]))
				size = Integer.valueOf(args[0]);
			else
				logopath = args[0];
			context = args[1];
		} else if (args.length == 3) {
			if (Pattern.matches("\\d+", args[0]))
				size = Integer.valueOf(args[0]);
			else
				output = args[0];
			logopath = args[1];
			context = args[2];
		} else if (args.length == 4) {
			output = args[0];
			if (args[1].trim().length() != 0)
				size = Integer.valueOf(args[1]);
			logopath = args[2];
			context = args[3];
		}

		BufferedImage logoImg = getImageFromPath(logopath);
		BufferedImage result = getQrCodeImg(context, size, logoImg);

		if (output == null || output.trim().length() == 0)
			showWithSwing(result);
		else
			writeToFile(result, output);
	}

}
