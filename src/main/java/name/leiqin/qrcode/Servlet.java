package name.leiqin.qrcode;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
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
		String etag = req.getHeader("If-None-Match");
		if (etag != null && etag.equals("immutable")) {
			resp.setStatus(304);
			return;
		}

		String content = req.getParameter("content");
		if (content == null || content.trim().length() == 0)
			return;
		String sizeStr = req.getParameter("size");
		int size = 460;
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
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ImageIO.write(result, "png", baos);
			byte[] bs = baos.toByteArray();

			resp.setHeader("Cache-Control", "max-age=3");
			resp.setHeader("ETag", "immutable");
			resp.setContentType("image/png");
			resp.setContentLength(bs.length);
			resp.getOutputStream().write(bs);
		} catch (WriterException e) {
			throw new IOException(e);
		}
	}

}
