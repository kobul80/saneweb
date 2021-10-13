package cz.kobul.saneweb;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Arrays;
import java.util.Comparator;
import java.util.stream.Collectors;

import com.itextpdf.text.Document;
import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.PdfWriter;

/**
 * Tooly pro praci s PDF pres iText
 */
public class PdfTools {

    /**
     * Prevede centimetry na body [points (72 bodu na palec)]
     * @param x
     * @return delka x v bodech
     */
	public static float cmToPt(final double x) {
        return (float)(x / 2.54 * 72);
    }
    
	/**
	 * Ulozi obrazek jako jednu stranku PDF souboru.
	 * @param imageData
	 * @param dpi
	 * @param filename
	 * @throws Exception
	 */
	public static final byte[] writeAsPdf(byte[] imageData, int dpi) throws Exception {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		Document document = new Document();
		PdfWriter.getInstance(document, bos);
		document.open();

		Image image = Image.getInstance(imageData);
		image.setAbsolutePosition(0, 0);
		image.scaleAbsolute(cmToPt(21), cmToPt(29.7));
		// Adding image to the document       
		document.add(image);              

		// Closing the document       
		document.close();              
		
		return bos.toByteArray();
	}
	
	/**
	 * Provede konverzi slozky se soubory na jedno spojene pdf.
	 * @param dirName
	 * @param resultFileName
	 * @throws Exception
	 */
	public static void convertToPdfDir(String dirName, String resultFileName) throws Exception {
		File dir = new File(dirName);
		
		Document document = new Document();
		PdfWriter.getInstance(document, new FileOutputStream(resultFileName));
		document.open();
		boolean first = true;
		for (File f : Arrays.stream(dir.listFiles()).sorted(Comparator.comparing(File::getName)).collect(Collectors.toList())) {
			String lcname = f.getName().toLowerCase();
			if (lcname.endsWith(".png") || lcname.endsWith(".jpg") || lcname.endsWith(".jpeg")) {
				// konverze do png
//				BufferedImage imageIO = ImageIO.read(f);
//				
//				ByteArrayOutputStream out = new ByteArrayOutputStream(100000);
//				ImageIO.write(imageIO, "png", out);
//				
//				byte[] buf = out.toByteArray();
				
				if (!first) {
					document.newPage();
				} else {
					first = false;
				}
				byte[] buf = new byte[(int) f.length()];
				try (FileInputStream fin = new FileInputStream(f)) {
					fin.read(buf);			
				}
				Image image = Image.getInstance(buf);
				image.setAbsolutePosition(0, 0);
				image.scaleAbsolute(cmToPt(21), cmToPt(29.7));
				// Adding image to the document       
				document.add(image);              				
			}
		}
		// Closing the document       
		document.close();              
	}

	
}
