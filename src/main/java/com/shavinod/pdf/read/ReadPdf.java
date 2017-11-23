package com.shavinod.pdf.read;

import java.io.File;
import java.io.IOException;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.encryption.InvalidPasswordException;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.pdfbox.text.PDFTextStripperByArea;

public class ReadPdf {
	
	public String parseLines(String line) {
		
		
		
		return line;
		
	}
	
	public static void main(String[] args) throws InvalidPasswordException, IOException {

		PDDocument document = PDDocument.load(new File("D:\\dev\\git\\hadoop-custom\\src\\main\\resources\\TRN57151082.pdf"));

		document.getClass();

		if (!document.isEncrypted()) {

			PDFTextStripperByArea stripper = new PDFTextStripperByArea();
			stripper.setSortByPosition(true);

			PDFTextStripper tStripper = new PDFTextStripper();
			String pdfFileInText = tStripper.getText(document);

			String lines[] = pdfFileInText.split("\\r?\\n");
			for (String line : lines) {
				System.out.println(line);
			}

		}
	}
}
