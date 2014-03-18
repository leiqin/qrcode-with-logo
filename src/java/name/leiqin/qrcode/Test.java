package name.leiqin.qrcode;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JComponent;
import javax.swing.JFrame;

import com.google.zxing.WriterException;

public class Test extends JComponent {

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
        g.drawImage(bi,0,0,this);
    }

	public static void main(String[] args) throws WriterException, IOException {
		String context = "我们是害虫，我们是益虫！";
		int size = 500;
		BufferedImage result = null;
		String logopath = Test.class.getClassLoader().getResource("favicon.jpg").getFile();
		BufferedImage logoImg = ImageIO.read(new File(logopath));

		QrCodeHelper qh = new QrCodeHelper();
		qh.setContext(context);
		qh.setSize(size);
		qh.setLogoImage(logoImg);

		result = qh.qrcodeImg();

		Test t = new Test();
		t.width = result.getWidth();
		t.height = result.getHeight();
		t.bi = result;
        JFrame frame = new JFrame("HelloWorldQrcode");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
 
		frame.add(t);
 
        //Display the window.
        frame.pack();
        frame.setVisible(true);
		
	}

}
