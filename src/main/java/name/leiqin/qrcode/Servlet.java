package name.leiqin.qrcode;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.zxing.WriterException;

public class Servlet extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		String content = req.getParameter("content");
		if (content == null || content.trim().length() == 0)
			return;
		String sizeStr = req.getParameter("size");
		int size = 500;
		if (sizeStr != null && sizeStr.trim().length() != 0) {
			size = Integer.valueOf(sizeStr.trim());
		}
		String logourl = req.getParameter("logo");

		BufferedImage logo = null;
		if (logourl != null && logourl.trim().length() != 0) {
			logo = Cmd.getImageFromHttp(logourl);
		}
		try {
			BufferedImage result = Cmd.getQrCodeImg(content, size, logo);
			resp.setContentType("image/png");
			ImageIO.write(result, "png", resp.getOutputStream());
		} catch (WriterException e) {
			throw new IOException(e);
		}
	}

}
