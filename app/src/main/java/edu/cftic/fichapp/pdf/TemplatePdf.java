/*
 * Developed by Alfonso J. González
 * Made at 4/06/19 19:01
 * Copyright (c) alf8969 2019 All rights reserved
 */

package edu.cftic.fichapp.pdf;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Environment;
import android.os.StrictMode;
import android.support.annotation.RequiresApi;
import android.util.Log;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.Locale;
import edu.cftic.fichapp.bean.Empresa;

public class TemplatePdf extends PdfPageEventHelper {
  private Context context;
  private File pdfFile;
  private Document document;
  private Paragraph paragraph;
  private Font fTitle = new Font(Font.FontFamily.TIMES_ROMAN, 18, Font.BOLD);
  private Font fSubTitle = new Font(Font.FontFamily.TIMES_ROMAN, 15, Font.BOLD);
  private Font fText = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD);
  private Font fHighText = new Font(Font.FontFamily.TIMES_ROMAN, 20, Font.BOLD, BaseColor.RED);
  private PdfWriter pdfWriter;
  private PdfPTable pdfPTable;
  private Empresa emp;

  private static final String PREFIX_INFORM = "INFORME_FICHAJE_";
  private static final String SUFFIX_INFORM = ".pdf";
  static final String FILE_NAME = PREFIX_INFORM + SUFFIX_INFORM;

  TemplatePdf(Context context, Empresa emp) {
    this.context = context;
    StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
    StrictMode.setVmPolicy(builder.build());
    this.emp = emp;
  }


  @RequiresApi(api = Build.VERSION_CODES.O)
  void onStartPage() {
    String[] mainHeaders = new String[]{
        "INFORME FICHAPP MES " + LocalDate.now().getMonth().getDisplayName(TextStyle.SHORT, Locale.getDefault()) +
            "-" + LocalDate.now().getYear(),
        "Fecha: " + LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
    };
      openDocument();
      addMetaData("Informe fichaje", "Informe", "Ficha APP");
      setHeaders(mainHeaders, true);
      addImgName(emp.getRutalogo());
      addTitles("Nombre empresa", emp.getNombre_empresa(), emp.getCif(), emp.getResponsable());
  }

  private void openDocument() {
    createFile();
    try {
      document = new Document(PageSize.A4, 36, 36, 54, 36);
      pdfWriter = PdfWriter.getInstance(document, new FileOutputStream(pdfFile));
      document.open();
    } catch (Exception e) {
      Log.e("openDocument", e.toString());
    }
  }


  private void createFile() {
    boolean success = true;
    File folder = new File(Environment.getExternalStorageDirectory().toString(), "PDF");
    if (!folder.exists()) {
      try {
        success = folder.mkdir();
      } catch (SecurityException e) {
        Log.e("APP", "no se pudo crear la carpeta");
        Log.e("APP", e.getLocalizedMessage());
      }
    }
    if (success)
      pdfFile = new File(folder, TemplatePdf.FILE_NAME);
  }

  @Override
  public void onCloseDocument(PdfWriter writer, Document document) {
    ColumnText.showTextAligned(pdfWriter.getDirectContent(), Element.ALIGN_LEFT, new Phrase(String.valueOf(writer.getPageNumber() - 1)), 0, 0, 0);
  }

  void closeDocument() {
    document.close();
  }

  private void addImgName(String file_path) {
    try {
      Image image = Image.getInstance(file_path);
      image.setSpacingBefore(5);
      image.setSpacingAfter(5);
      image.scaleToFit(150, 150);
      image.setAlignment(Element.ALIGN_LEFT);
      document.add(image);
    } catch (Exception e) {
      Log.e("addImgName ", e.toString());
    }
  }

  private void addMetaData(String title, String subject, String author) {
    document.addTitle(title);
    document.addSubject(subject);
    document.addAuthor(author);
  }

  private void addTitles(String title, String subTitle, String cif, String res) {
    paragraph = new Paragraph();
    addChildP(new Paragraph(title, fHighText));
    addChildP(new Paragraph(subTitle, fTitle));
    addChildP(new Paragraph("Cif: " + cif, fSubTitle));
    addChildP(new Paragraph("Responsable: " + res, fSubTitle));
    paragraph.setSpacingAfter(30);
    try {
      document.add(paragraph);
    } catch (DocumentException e) {
      Log.e("addTitles ", e.toString());
    }
  }

  private void addChildP(Paragraph childParagraph) {
    childParagraph.setAlignment(Element.ALIGN_LEFT);
    paragraph.add(childParagraph);
  }

  /**
   * Método para añadir cualquier información al documento
   *
   * @param text El texto a introducir en el documento
   */
  private void addParagraph(String text) {
    try {
      paragraph = new Paragraph(text, fText);
      paragraph.setSpacingAfter(5);
      paragraph.setSpacingBefore(5);
      document.add(paragraph);
    } catch (DocumentException e) {
      Log.e("addParagraph ", e.toString());
    }
  }

  void createTable( ArrayList<String[]> data) {
    PdfPCell pdfPCell;

    for (int indexRow = 0; indexRow < data.size(); indexRow++) {
      String[] row = data.get(indexRow);
      pdfPTable = new PdfPTable(row.length);
      pdfPTable.setWidthPercentage(100);
      for (String s : row) {
        pdfPCell = new PdfPCell(new Phrase(s));
        pdfPCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        pdfPCell.setFixedHeight(40);
        pdfPTable.addCell(pdfPCell);
      }
      paragraph = new Paragraph();
      paragraph.setFont(fText);
      paragraph.add(pdfPTable);

      try {
        document.add(paragraph);
      } catch (DocumentException e) {
        Log.e("createTable ", e.toString());
      }
    }
  }

  void setHeaders(String[] headers, boolean main) {
    pdfPTable = new PdfPTable(headers.length);
    pdfPTable.setWidthPercentage(100);
    pdfPTable.setSpacingBefore(20);
    int indexC = 0;
    while (indexC < headers.length) {
      PdfPCell pdfPCell = new PdfPCell(new Phrase(headers[indexC++], fSubTitle));
      pdfPCell.setHorizontalAlignment(Element.ALIGN_CENTER);
      if (main)
        pdfPCell.setBackgroundColor(BaseColor.GRAY);
      else
        pdfPCell.setBackgroundColor(BaseColor.GREEN);
      pdfPTable.addCell(pdfPCell);
    }
    paragraph = new Paragraph();
    paragraph.setFont(fText);
    paragraph.add(pdfPTable);
    try {
      document.add(paragraph);
    } catch (DocumentException e) {
      Log.e("setMainHeaders ", e.toString());
    }
  }

  void onEndPage() {
    addParagraph(String.format(Locale.getDefault(),"Página %d de %d", pdfWriter.getCurrentPageNumber(), pdfWriter.getPageNumber()));
  }

  void viewPDF() {
    Intent intent = new Intent(context, ViewPDFActivity.class);
    String path = pdfFile.getAbsoluteFile().toString();
    intent.putExtra("path", path);
    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    context.startActivity(intent);
  }
}
