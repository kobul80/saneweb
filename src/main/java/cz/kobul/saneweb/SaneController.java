package cz.kobul.saneweb;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import au.com.southsky.jfreesane.SaneException;
import cz.kobul.saneweb.SaneTools.Mode;

@RestController
public class SaneController {
	
	private static final String URL = "192.168.0.120";
	
	@GetMapping(value = "/scan/bw/{resolution}", produces = MediaType.IMAGE_PNG_VALUE)
	public @ResponseBody byte[] scanBwImage(@PathVariable int resolution) throws IOException, SaneException {
		return SaneTools.scanToPng(URL, Mode.BW, resolution);
	}

	@GetMapping(value = "/scan/gray/{resolution}", produces = MediaType.IMAGE_JPEG_VALUE)
	public @ResponseBody byte[] scanGrayImage(@PathVariable int resolution) throws IOException, SaneException {
		return SaneTools.scanToJpeg(URL, Mode.GRAY, resolution);
	}
	
	@GetMapping(value = "/scan/color/{resolution}", produces = MediaType.IMAGE_JPEG_VALUE)
	public @ResponseBody byte[] scanColorImage(@PathVariable int resolution) throws IOException, SaneException {
		return SaneTools.scanToJpeg(URL, Mode.COLOR, resolution);
	}

	@GetMapping(value = "/scan/pdf/bw/{resolution}", produces = MediaType.APPLICATION_PDF_VALUE)
	public @ResponseBody byte[] scanBwPdf(@PathVariable int resolution) throws Exception {
		byte[] png = SaneTools.scanToPng(URL, Mode.BW, resolution);
		return PdfTools.writeAsPdf(png, resolution);
	}

	@GetMapping(value = "/scan/pdf/gray/{resolution}", produces = MediaType.APPLICATION_PDF_VALUE)
	public @ResponseBody byte[] scanGrayPdf(@PathVariable int resolution) throws Exception {
		byte[] jpeg = SaneTools.scanToJpeg(URL, Mode.GRAY, resolution);
		return PdfTools.writeAsPdf(jpeg, resolution);
	}
	
	@GetMapping(value = "/scan/pdf/color/{resolution}", produces = MediaType.APPLICATION_PDF_VALUE)
	public @ResponseBody byte[] scanColorPdf(@PathVariable int resolution) throws Exception {
		byte[] jpeg = SaneTools.scanToJpeg(URL, Mode.COLOR, resolution);
		return PdfTools.writeAsPdf(jpeg, resolution);
	}
	

	@GetMapping(value = "/scan/multiple", produces = MediaType.TEXT_HTML_VALUE)
	public String index() throws IOException {
		InputStream in = new ClassPathResource("static/scan/multiple/index.html").getInputStream();
		ByteArrayOutputStream result = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		for (int length; (length = in.read(buffer)) != -1; ) {
			result.write(buffer, 0, length);
		}
		return result.toString(StandardCharsets.UTF_8.name());
	}
	
	@GetMapping("/clear")
	public ClearResult clear() {
		return new ClearResult("ok");
	}
	
}
