qrcode-with-logo
===============

生成带 Logo 的二维码图片

使用方法，运行：

	mvn jetty:run

然后访问： http://localhost:8080

在命令行中使用：

	mvn exec:java -Dexec.args='[[[output] size] logopath] content'

参数：

	output     输出文件，png 格式，省略则会使用 swing 显示
	size       图片大小，缺省为 500
	logopath   logo 文件的地址，可以是 url
	content    二维码中所要包含的内容

在代码中使用：

	BufferedImage logo = ImageIO.read(new File(logopath));

	QRCodeHelper qh = new QRCodeHelper();
	qh.setContext(context);
	qh.setSize(size);
	qh.setLogoImage(logo);
	BufferedImage img = qh.qrcodeImg();

	ImageIO.write(img, "png", new File(output));

下图使用这个命令生成的：
	
	mvn exec:java -Dexec.args='qrcode.png https://avatars1.githubusercontent.com/u/1247384 https://github.com/leiqin/qrcode-with-logo'

<img src="http://blog.leiqin.name/qrcode-with-logo/images/qrcode.png"/>
