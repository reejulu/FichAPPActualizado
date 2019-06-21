/*
 * Developed by Alfonso J. González
 * Made at 6/6/19 1:29 PM
 * Copyright (c) alf8969 2019 All rights reserved
 */

package edu.cftic.fichapp.pdf;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.Toast;
import com.google.gson.Gson;

import java.io.File;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.EnumSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import edu.cftic.fichapp.R;

import edu.cftic.fichapp.actividades.MenuGestorActivity;
import edu.cftic.fichapp.actividades.SeleccionarInforme;
import edu.cftic.fichapp.bean.Empresa;
import edu.cftic.fichapp.persistencia.DB;
import edu.cftic.fichapp.persistencia.esquemas.IEmpleadoEsquema;
import edu.cftic.fichapp.persistencia.esquemas.IFichajeEsquema;
import edu.cftic.fichapp.util.Fecha;

import static java.util.concurrent.TimeUnit.HOURS;
import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static java.util.concurrent.TimeUnit.MINUTES;

public class CreatePdfActivity extends AppCompatActivity implements IEmpleadoEsquema, IFichajeEsquema {

  private ProgressDialog progressDialog;
  private ArrayList<String[]> rows;
  private TemplatePdf templatePdf;
  private Empresa emp;
  private Spinner spnMonth;
  private CheckBox checkAll;
  private boolean automatic;


  @RequiresApi(api = Build.VERSION_CODES.N)
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    Bundle bundle = getIntent().getExtras();
    if (bundle != null) {
      emp = new Gson().fromJson(bundle.getString("empresa"), Empresa.class);
      automatic = bundle.getBoolean("CREARPDF", false);
    }

    if (automatic) {
      createPdf();
    } else {
      setContentView(R.layout.activity_create_pdf);
      checkAll = findViewById(R.id.checkAll);

      CardView btnCreatePdf = findViewById(R.id.btn_create);
      spnMonth = findViewById(R.id.spnSelectMonth);
      Cursor cursorAdapter = DB.empleados.getMonthsToSpinnerSelectMonth();
      SimpleCursorAdapter spnMonthsAdapter = new SimpleCursorAdapter(this,
              android.R.layout.simple_spinner_item,
              cursorAdapter,
              new String[]{"month"},
              new int[]{android.R.id.text1}, 0);
      spnMonthsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
      spnMonth.setAdapter(spnMonthsAdapter);

      checkAll.setOnCheckedChangeListener((buttonView, isChecked) -> {
        if (isChecked)
          spnMonth.setVisibility(View.GONE);
        else
          spnMonth.setVisibility(View.VISIBLE);
      });
      btnCreatePdf.setOnClickListener(v -> createPdf());
    }
  }

  @RequiresApi(api = Build.VERSION_CODES.N)
  private void createPdf() {
    progressDialog = new ProgressDialog(this);
    progressDialog.setTitle("Crear Pdf");
    progressDialog.setMessage("Creando Pdf");
    progressDialog.setCancelable(true);
    progressDialog.setIndeterminate(true);
    if (!automatic)
      progressDialog.show();
    if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
      ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
    } else {
      new Thread(() -> {
        createViewPdf();
        runOnUiThread(() -> {
          progressDialog.dismiss();
          //TODO hacer el salto a la actividad del email JUANLU MUÑOZ
          Intent intent = new Intent(this, SeleccionarInforme.class);
          File folder = new File(Environment.getExternalStorageDirectory().toString(), "PDF");
          File pdfFile = new File(folder, TemplatePdf.FILE_NAME);
          if (!pdfFile.exists()) {
            Toast.makeText(this, "Sin datos", Toast.LENGTH_LONG).show();
            intent.putExtra("YACREADO", false);
          } else {
            intent.putExtra("YACREADO", true);
            Toast.makeText(this, "Fichero " + pdfFile.getName() + " creado", Toast.LENGTH_LONG).show();
          }
          if (automatic){
            startActivity(intent);
            finish();
          }
        });
      }).start();
    }
  }

  @RequiresApi(api = Build.VERSION_CODES.N)
  @Override
  public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
    if (grantResults.length > 0) {
      if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
        createViewPdf();
      } else if (grantResults[0] == PackageManager.PERMISSION_DENIED) {
        new AlertDialog.Builder(this)
                .setTitle("Petición informe")
                .setMessage("Necesita permisoa")
                .setPositiveButton("Ok", (dialog, which) -> ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0))
                .show();
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
          requestWriteExternalStoragePermission();
        } else {
          new AlertDialog.Builder(this)
                  .setTitle("Petición informe")
                  .setMessage("Necesita permisoa")
                  .setPositiveButton("Ok", (dialog1, which1) -> dialog1.dismiss())
                  .show();
        }
      }
    }
  }

  private void requestWriteExternalStoragePermission() {
    if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
      new AlertDialog.Builder(this)
              .setTitle("Petición informe")
              .setMessage("Necesita permisoa")
              .setPositiveButton("OK", (dialog, which) -> ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0))
              .show();
    } else {
      ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
    }
  }

  private void createViewPdf() {
    templatePdf = new TemplatePdf(this, emp);
    templatePdf.onStartPage();
    rows = new ArrayList<>();
//    getDataToPdf();
    templatePdf.closeDocument();
    // Se creo el documento, ahora creamos la vista con los datos. (Class TemplatePdf)
    // siempre que la petición no venga del envio automático del informe.
    if (!automatic)
      templatePdf.viewPDF();
  }

  private void getDataToPdf() {
    Date date = new Date();
    Calendar calendar = Calendar.getInstance();
    calendar.setTime(date);

    String month;
    Cursor c;
    Cursor cursor;
    if (!automatic){
      c = (Cursor) spnMonth.getSelectedItem();
      month = c.getString(c.getColumnIndex("month"));
      if (checkAll.isChecked()) {
        cursor = DB.fichar.getALlDataToPdf(emp.getId_empresa());
      } else {
        cursor = DB.fichar.getDataOfMonthToPdf(emp.getId_empresa(), month);
      }
    }
    else {
      month = String.valueOf(calendar.get(Calendar.MONTH));
      if (Integer.valueOf(month) < 10) {
        month = "0"+month;
      }
      cursor = DB.fichar.getDataOfMonthToPdf(emp.getId_empresa(), month);
    }

    ArrayList<FichajeEmpleado> arrayFe = new ArrayList<>();
    if (cursor.getCount() > 0) {
      cursor.moveToFirst();
      do {
        FichajeEmpleado fe = new FichajeEmpleado(
                cursor.getString(cursor.getColumnIndex(E_COL_NOMBRE)),
                Fecha.inicio(new Timestamp(cursor.getLong(cursor.getColumnIndex(F_COL_INICIO)))),
                Fecha.fin(new Timestamp(cursor.getLong(cursor.getColumnIndex(F_COL_FIN))))
        );
        arrayFe.add(fe);
      } while (cursor.moveToNext());
    }

    String[] dataRows;
    String[] dataRowsTotalHours;
    String[] dataRowsEmployee;
    int totalHours = 0;
    int totalMinutes = 0;
    Date tempDate = new Date();
    int countDay = 0;
    String tempEmpleadoName ="";

    SimpleDateFormat formatDateMonth = new SimpleDateFormat("MM", Locale.getDefault());
    SimpleDateFormat formatDate = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
    SimpleDateFormat formatDateStart = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
    SimpleDateFormat formatDateEnd = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());

    for (int i =0; i< arrayFe.size(); i++) {
      rows = new ArrayList<>();

      String strDateStart = formatDateStart.format(arrayFe.get(i).getFechaInicio());
      String strDateMonth = formatDateMonth.format(arrayFe.get(i).getFechaInicio());
      String strDateEnd = formatDateEnd.format(arrayFe.get(i).getFechaFin());
      String strDate = formatDate.format(arrayFe.get(i).getFechaInicio());
      Map<TimeUnit, Long> timeUnitLongMap = computeDiff(arrayFe.get(i).getFechaInicio(), arrayFe.get(i).getFechaFin());
      String ne = arrayFe.get(i).getNombreEmpleado();
      if (!tempEmpleadoName.equals(ne)) {
        String[] headers = new String[]{
                "DIA",
                "ENTRADA",
                "SALIDA",
                "TIEMPO"
        };
        dataRowsEmployee = new String[]{
                "Empleado: ", "", "", arrayFe.get(i).getNombreEmpleado()
        };
        templatePdf.setHeaders(dataRowsEmployee, true);
        templatePdf.setHeaders(headers, false);
      }

      if (!tempDate.equals(arrayFe.get(i).getFechaInicio())) {
        countDay = 0;
      }

      if (countDay == 0) {
        totalHours += Integer.parseInt(Objects.requireNonNull(timeUnitLongMap.get(HOURS)).toString());
        totalMinutes += Integer.parseInt(Objects.requireNonNull(timeUnitLongMap.get(MINUTES)).toString());
        dataRows = new String[]{
                strDate,
                strDateStart,
                strDateEnd,
                timeUnitLongMap.get(HOURS) + " Horas " + timeUnitLongMap.get(MINUTES) + " minutos"
        };
      } else {
        totalHours += Integer.parseInt(Objects.requireNonNull(timeUnitLongMap.get(HOURS)).toString());
        totalMinutes += Integer.parseInt(Objects.requireNonNull(timeUnitLongMap.get(MINUTES)).toString());
        dataRows = new String[]{
                "",
                strDateStart,
                strDateEnd,
                timeUnitLongMap.get(HOURS) + " Horas " + timeUnitLongMap.get(MINUTES) + " minutos"
        };
      }

      rows.add(dataRows);

      if(i < arrayFe.size()-1) {
        if (!arrayFe.get(i).getFechaInicio().equals(arrayFe.get(i + 1).getFechaInicio())) {
          dataRowsTotalHours = setTotalTimes(strDate, totalHours, totalMinutes);
          rows.add(dataRowsTotalHours);
          totalHours = 0;
          totalMinutes = 0;
        }
      } else {
        dataRowsTotalHours = setTotalTimes(strDate, totalHours, totalMinutes);
        rows.add(dataRowsTotalHours);
        totalHours = 0;
        totalMinutes = 0;
      }

      tempDate = arrayFe.get(i).getFechaInicio();
      tempEmpleadoName = arrayFe.get(i).getNombreEmpleado();
      countDay++;

      templatePdf.createTable(rows);
    }
    templatePdf.onEndPage();
  }

  /**
   * Función para hayar las horas que resulten de la suma de minutos que excedan de 60
   * @param date Fecha del fichaje
   * @param totalHours Total de horas de la fecha
   * @param totalMinutes Total de minutos de la fecha
   * @return String[] formateado para la tabla
   */
  private String[] setTotalTimes(String date, int totalHours, int totalMinutes) {
    int res = totalMinutes / 60;
    if (res > 0) {
      for (int i =0; i < res; i++){
        totalHours += 1;
        totalMinutes -= 60;
      }
    }
    String hours = "";
    String minutes = "";
    if (totalHours > 0)
      hours = "Horas: " + totalHours;
    if (totalMinutes > 0)
      minutes = "Minutos: " + totalMinutes;
    return new String[]{
            "Total " + date, "", hours , minutes
    };
  }

  /**
   * Función que calcula la diferencia de dos fechas
   * @param dateStart Fecha inicio
   * @param dateEnd Fecha fin
   * @return Map con el resultado en Horas = h, Minutos = m, Segundos = s, etc..
   */
  public static Map<TimeUnit, Long> computeDiff(Date dateStart, Date dateEnd) {

    long diffInMillis = dateEnd.getTime() - dateStart.getTime();

    // Creamos la lista y la rellenamos con todas la unidades de tiempo Horas, minutos, etc..
    List<TimeUnit> units = new ArrayList<>(EnumSet.allOf(TimeUnit.class));
    Collections.reverse(units);

    // Creamos el Map de resultados de TimeUnit y la diferencia.
    Map<TimeUnit, Long> result = new LinkedHashMap<>();
    long millisRest = diffInMillis;

    for (TimeUnit unit : units) {
      // Calculamos la diferencía en milisegundos.
      long diff = unit.convert(millisRest, MILLISECONDS);
      long diffInMillisForUnit = unit.toMillis(diff);
      millisRest = millisRest - diffInMillisForUnit;
      //ponemos el resultado en el Map.
      result.put(unit, diff);
    }
    return result;
  }
}

class FichajeEmpleado {

  private String nombreEmpleado;
  private Date fechaInicio;
  private Date fechaFin;

  FichajeEmpleado(String nombreEmpleado, Date fechaInicio, Date fechaFin) {
    this.nombreEmpleado = nombreEmpleado;
    this.fechaInicio = fechaInicio;
    this.fechaFin = fechaFin;
  }

  String getNombreEmpleado() {
    return nombreEmpleado;
  }
  Date getFechaInicio() {
    return fechaInicio;
  }
  Date getFechaFin() {
    return fechaFin;
  }

}

