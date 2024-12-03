package net.yc.race.track.serviceInf;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import net.yc.race.track.model.Result;
import org.springframework.stereotype.Service;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public interface PdfExportServiceInf {

     void exportResultsToPdf(List<Result> results, String outputPath)throws DocumentException, IOException;

}
 
