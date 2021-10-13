package cz.kobul.saneweb;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;

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
	
}
