package cz.kobul.saneweb;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;
import java.util.Optional;

import javax.imageio.ImageIO;

import au.com.southsky.jfreesane.SaneDevice;
import au.com.southsky.jfreesane.SaneException;
import au.com.southsky.jfreesane.SaneOption;
import au.com.southsky.jfreesane.SaneSession;
import cz.kobul.saneweb.SaneTools.Mode;

/**
 * Tools for jfreesane 
 */
public final class SaneTools {

	public static enum Mode {
		BW("Lineart"),
		GRAY("Gray"),
		COLOR("Color");
		
		private String saneValue;
		
		private Mode(String saneValue) {
			this.saneValue = saneValue;
		}
		
		public String getSaneValue() {
			return saneValue;
		}
		
	}
	
	private SaneTools() {
	}
	
	/**
	 * Nastavi rozliseni pro skenovani na 'resolution', pokud je to mozne
	 * @param device
	 * @param resolution
	 * @throws IOException
	 * @throws SaneException
	 */
	public static void setResolution(SaneDevice device, int resolution) throws IOException, SaneException {
		SaneOption option = device.getOption("resolution");
		if (option != null) {
			option.setIntegerValue(resolution);
		}
	}
	
	/**
	 * Nastavi rezim skenovani (cernobile, odstiny sede, barevne)
	 * @param device
	 * @param mode
	 * @throws IOException
	 * @throws SaneException
	 */
	public static void setMode(SaneDevice device, Mode mode) throws IOException, SaneException {
		SaneOption option = device.getOption("mode");
		if (option != null) {
//			System.out.println(option.getStringConstraints());
			option.setStringValue(mode.getSaneValue());
		}
	}	

	public static Optional<BufferedImage> scanFromUrl(String url, Mode mode, int resolution) throws UnknownHostException, IOException, SaneException {
		SaneSession session = SaneSession.withRemoteSane(
				InetAddress.getByName(url));
		List<SaneDevice> devices = session.listDevices();
		if (!devices.isEmpty()) {
			SaneDevice device = devices.iterator().next();
			device.open();

			setResolution(device, resolution);
			setMode(device, mode);
			
			BufferedImage image = device.acquireImage();  // scan an image
			device.close();
			
			return Optional.of(image);
		}
		return Optional.empty();
	}
	
	public static byte[] scanToPng(String url, Mode mode, int resolution) throws IOException, UnknownHostException, SaneException {
		Optional<BufferedImage> image = scanFromUrl(url, mode, resolution);
		return image.isPresent() ? convertToPng(image.get()) : null;
	}

	public static byte[] scanToJpeg(String url, Mode mode, int resolution) throws IOException, UnknownHostException, SaneException {
		Optional<BufferedImage> image = scanFromUrl(url, mode, resolution);
		return image.isPresent() ? convertToJpeg(image.get()) : null;
	}

	public static byte[] convertToPng(BufferedImage image) throws IOException {
		ByteArrayOutputStream out = new ByteArrayOutputStream(10000);

		ImageIO.write(image, "png", out);
		
		return out.toByteArray();
	}

	public static byte[] convertToJpeg(BufferedImage image) throws IOException {
		ByteArrayOutputStream out = new ByteArrayOutputStream(10000);

		ImageIO.write(image, "jpeg", out);
		
		return out.toByteArray();
	}
	
}
