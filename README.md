qrcode-with-logo
===============

生成带 Logo 的二维码图片

使用方法：

	java -cp bin:lib/core-3.0.0.jar:lib/javase-3.0.0.jar name.leiqin.qrcode.Test [[[output] size] logopath] context

参数：
	output     输出文件，png 格式，省略则会使用 swing 显示
	size       图片大小，缺省为 500
	logopath   logo 文件的地址，可以是 url
	context    二维码中所要包含的内容

在代码中使用：

	BufferedImage logo = ImageIO.read(new File(logopath));

	QRCodeHelper qh = new QRCodeHelper();
	qh.setContext(context);
	qh.setSize(size);
	qh.setLogoImage(logo);
	BufferedImage img = qh.qrcodeImg();

	ImageIO.write(img, "png", new File(output));

下图使用这个命令生成的：
	
	java -cp bin:lib/core-3.0.0.jar:lib/javase-3.0.0.jar name.leiqin.qrcode.Test qrcode.png http://blog.leiqin.name/static/images/galaxy-ngc3370.jpg  https://github.com/leiqin/qrcode-with-logo

<img src="http://blog.leiqin.name/qrcode-with-logo/images/qrcode.png"/>
