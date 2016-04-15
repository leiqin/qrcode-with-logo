package name.leiqin.qrcode;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

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
		if (logourl.startsWith("uploadfile://")) {
			logo = (BufferedImage) req.getSession().getAttribute("uploadfile");
		} else if (logourl != null && logourl.trim().length() != 0) {
			logo = Cmd.getImageFromHttp(logourl);
		}
		try {
			BufferedImage result = Cmd.getQrCodeImg(content, size, logo);
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ImageIO.write(result, "png", baos);
			byte[] bs = baos.toByteArray();

			resp.setHeader("Cache-Control", "max-age=2592000");
			resp.setHeader("ETag", "immutable");
			resp.setContentType("image/png");
			resp.setContentLength(bs.length);
			resp.getOutputStream().write(bs);
		} catch (WriterException e) {
			throw new IOException(e);
		}
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		BufferedImage img = ImageIO.read(req.getInputStream());
		HttpSession session = req.getSession();
		DateFormat df = new SimpleDateFormat("YYYY-MM-DD'T'HH-mm-ss.SSSZZZ");
		String key = "uploadfile://" + df.format(new Date());
		session.setAttribute("uploadfile", img);
		byte[] bs = key.getBytes("utf-8");
		resp.setContentType("text/plain;charset=utf-8");
		resp.setContentLength(bs.length);
		resp.getOutputStream().write(bs);
	}
}
