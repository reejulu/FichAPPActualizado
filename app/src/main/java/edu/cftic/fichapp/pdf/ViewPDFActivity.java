/*
 * Developed by Alfonso J. González
 * Made at 4/06/19 20:00
 * Copyright (c) alf8969 2019 All rights reserved
 */

package edu.cftic.fichapp.pdf;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;
import com.github.barteksc.pdfviewer.PDFView;
import java.io.File;
import edu.cftic.fichapp.R;

public class ViewPDFActivity extends AppCompatActivity {

  private File file;
  String path;

  @RequiresApi(api = Build.VERSION_CODES.N)
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_view_pdf);

    PDFView pdfView = findViewById(R.id.pdfView);

    Bundle bundle = getIntent().getExtras();
    if (bundle != null) {
      path = bundle.getString("path", "");
      file = new File(path);
    }

    if (file.exists())
      pdfView.fromFile(file)
              .enableSwipe(true)
              .swipeHorizontal(true)
              .enableDoubletap(true)
              .enableAnnotationRendering(true)
              .load();
    else
      Toast.makeText(this,"File not found", Toast.LENGTH_LONG).show();

  }
}
