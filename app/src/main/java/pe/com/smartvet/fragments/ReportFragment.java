package pe.com.smartvet.fragments;


import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.Chart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LegendEntry;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.DataSet;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormatSymbols;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import pe.com.smartvet.R;
import pe.com.smartvet.SmartVetApp;
import pe.com.smartvet.models.Owner;
import pe.com.smartvet.models.Pet;
import pe.com.smartvet.network.SmartVetService;
import pe.com.smartvet.templates.TemplatePDF;

/**
 * A simple {@link Fragment} subclass.
 */
public class ReportFragment extends Fragment {

    private TemplatePDF templatePDF;
    private Spinner monthSpinner;
    private Button pdfButton;
    private String[]headerOwner={"Number","Owner","mail","Phone","Date","Status"};
    private String[]headerPet={"Number","Pet","Owner","Date","Status"};
    private List<Owner> owners;
    private List<Pet> pets;
    List<String> monthList = new ArrayList<String>();
    List<Owner> monthOwners = new ArrayList<>();
    List<Pet> monthPets = new ArrayList<>();
    private BarChart ownerBarChart;
    private BarChart petBarChart;
    private String[] months;
    private int[] ownerByMonth = new int[12];
    private int[] petByMonth = new int[12];
    private int[] colors = new int[]{Color.BLACK,Color.GREEN,Color.RED,Color.BLACK,Color.GREEN,Color.RED,Color.BLACK,Color.GREEN,Color.RED,Color.BLACK,Color.GREEN,Color.RED};

    private static final int MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 1 ;


    public ReportFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_report, container, false);
        monthSpinner = view.findViewById(R.id.monthSpinner);
        pdfButton = view.findViewById(R.id.pdfButton);
        ownerBarChart = view.findViewById(R.id.ownerBarChart);
        petBarChart = view.findViewById(R.id.petBarChart);

        int permissionCheck = ContextCompat.checkSelfPermission(view.getContext(), android.Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (ContextCompat.checkSelfPermission(view.getContext(),
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);
            } else {
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);
            }
        }
        pdfButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pdfView(v);
            }
        });

        months = new DateFormatSymbols().getMonths();
        monthList.addAll(Arrays.asList(months));

        getOwners();
        getPets();

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(view.getContext(), android.R.layout.simple_spinner_item, monthList);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        monthSpinner.setAdapter(dataAdapter);

        return view;
    }

    private Chart getSameChart(Chart chart, String description, int textColor, int background, int animateY) {
        chart.getDescription().setText(description);
        chart.getDescription().setTextSize(15);
        chart.getDescription().setTextColor(textColor);
        //chart.setBackgroundColor(background);
        chart.animateY(animateY);
        legend(chart);
        return chart;
    }

    private void legend(Chart chart) {
        Legend legend = chart.getLegend();
        legend.setForm(Legend.LegendForm.CIRCLE);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);

        ArrayList<LegendEntry> entries = new ArrayList<>();

        for(int i=0; i<months.length;i++) {
            LegendEntry entry = new LegendEntry();
            entry.formColor = colors[i];
            entry.label = months[i];
            entries.add(entry);
        }
        legend.setCustom(entries);
    }

    private void ownersByMonth(){
        int count;
        for(int j=1;j<13;j++) {
            count = 0;
            for(int i=0;i<owners.size();i++) {
                Integer month = Integer.parseInt(owners.get(i).getSignUpDate().substring(5,7));
                if (month == j) {
                    count++;
                }
            }
            ownerByMonth[j-1] = count;
        }
        createCharts();
    }

    private void petsByMonth(){
        int count;
        for(int j=1;j<13;j++) {
            count = 0;
            for(int i=0;i<pets.size();i++) {
                Integer month = Integer.parseInt(pets.get(i).getCreateDate().substring(5,7));
                if (month == j) {
                    count++;
                }
            }
            petByMonth[j-1] = count;
        }
        createCharts();
    }

    private ArrayList<BarEntry> getBarEntriesOwner() {
        ArrayList<BarEntry> entries = new ArrayList<>();
        for(int i=0; i<months.length;i++) {
            entries.add(new BarEntry(i,ownerByMonth[i]));
        }
        return entries;
    }

    private ArrayList<BarEntry> getBarEntriesPet() {
        ArrayList<BarEntry> entries = new ArrayList<>();
        for(int i=0; i<months.length;i++) {
            entries.add(new BarEntry(i,petByMonth[i]));
        }
        return entries;
    }

    private void axisX(XAxis axis) {
        axis.setGranularityEnabled(true);
        axis.setPosition(XAxis.XAxisPosition.BOTTOM);
        axis.setValueFormatter(new IndexAxisValueFormatter(months));
        axis.setEnabled(false);
    }

    private void axisLeft(YAxis axis) {
        axis.setSpaceTop(30);
        axis.setAxisMinimum(0);
        axis.setGranularity(1);
    }

    private void axisRight(YAxis axis) {
        axis.setEnabled(false);
    }

    public void createCharts() {
        ownerBarChart = (BarChart) getSameChart(ownerBarChart,"", Color.RED, Color.CYAN, 3000);
        ownerBarChart.setDrawGridBackground(true);
        ownerBarChart.setDrawBarShadow(true);
        ownerBarChart.setData(getBarDataOwner());
        ownerBarChart.invalidate();
        axisX(ownerBarChart.getXAxis());
        axisLeft(ownerBarChart.getAxisLeft());
        axisRight(ownerBarChart.getAxisRight());

        petBarChart = (BarChart) getSameChart(petBarChart,"", Color.RED, Color.CYAN, 3000);
        petBarChart.setDrawGridBackground(true);
        petBarChart.setDrawBarShadow(true);
        petBarChart.setData(getBarDataPet());
        petBarChart.invalidate();
        axisX(petBarChart.getXAxis());
        axisLeft(petBarChart.getAxisLeft());
        axisRight(petBarChart.getAxisRight());
    }

    private DataSet getData(DataSet dataSet) {
        dataSet.setColors(colors);
        dataSet.setValueTextColor(Color.WHITE);
        dataSet.setValueTextSize(10);
        return dataSet;
    }

    private BarData getBarDataOwner() {
        BarDataSet barDataSet = (BarDataSet)getData(new BarDataSet(getBarEntriesOwner(), ""));
        barDataSet.setBarShadowColor(Color.GRAY);
        BarData barData = new BarData(barDataSet);
        barData.setBarWidth(0.4f);
        return barData;
    }

    private BarData getBarDataPet() {
        BarDataSet barDataSet = (BarDataSet)getData(new BarDataSet(getBarEntriesPet(), ""));
        barDataSet.setBarShadowColor(Color.GRAY);
        BarData barData = new BarData(barDataSet);
        barData.setBarWidth(0.4f);
        return barData;
    }

    public void pdfView(View view) {
        monthOwners.clear();
        monthPets.clear();
        templatePDF = new TemplatePDF(view.getContext());
        int year;
        int month;
        int position = Integer.parseInt(Long.toString(monthSpinner.getSelectedItemId())) + 1;
        if (owners.size() >= 1) {
            for(int i = 0;i<owners.size();i++) {
                year = Integer.parseInt(owners.get(i).getSignUpDate().substring(0, 4));
                month = Integer.parseInt(owners.get(i).getSignUpDate().substring(5,7));
                if (year == 2018 && month == position) {
                    monthOwners.add(owners.get(i));
                }
            }
        }
        if (pets.size() >= 1) {
            for(int i = 0;i<pets.size();i++) {
                year = Integer.parseInt(pets.get(i).getCreateDate().substring(0, 4));
                month = Integer.parseInt(pets.get(i).getCreateDate().substring(5,7));
                if (year == 2018 && month == position) {
                    monthPets.add(pets.get(i));
                }
            }
        }

        templatePDF.openDocument();
        templatePDF.addMetaData("Smart-Vet", "Reporte", "Guillermo Ramos");
        templatePDF.addTitles("Smart-Vet", "Reporte de " + String.valueOf(monthSpinner.getSelectedItem()), Calendar.getInstance().getTime().toString());
        templatePDF.addParagraph("Lista de Dueños");
        templatePDF.createTable(headerOwner, getOwner());
        templatePDF.addParagraph("Lista de Mascotas");
        templatePDF.createTable(headerPet, getPet());
        templatePDF.closeDocument();
        templatePDF.viewPDF();
    }

    private ArrayList<String[]>getOwner(){
        ArrayList<String[]>rows = new ArrayList<>();

        for(int i = 0; i < monthOwners.size(); i++) {
            rows.add(new String[]{String.valueOf(i+1), monthOwners.get(i).getName() + " " + monthOwners.get(i).getLastName(), monthOwners.get(i).getEmail(),
                    monthOwners.get(i).getMobilePhone().toString(), monthOwners.get(i).getSignUpDate().substring(0,10), monthOwners.get(i).getStatus()});
        }
        return rows;
    }

    private ArrayList<String[]>getPet(){
        ArrayList<String[]>rows = new ArrayList<>();

        for(int i = 0; i < monthPets.size(); i++) {
            rows.add(new String[]{String.valueOf(i+1), monthPets.get(i).getName(),
                    monthPets.get(i).getOwner().getName() + monthPets.get(i).getOwner().getLastName(),
                    monthPets.get(i).getCreateDate().substring(0,10),
                    monthPets.get(i).getStatus()});
        }
        return rows;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                } else {
                }
                return;
            }
        }
    }

    private void getOwners() {
        AndroidNetworking
                .get(SmartVetService.OWNER_URL)
                .addHeaders("Authorization", SmartVetApp.getInstance().getToken())
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            owners = Owner.build(response.getJSONArray("owners"));
                            ownersByMonth();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                    }
                });
    }

    private void getPets() {
        AndroidNetworking
                .get(SmartVetService.PET_URL)
                .addHeaders("Authorization", SmartVetApp.getInstance().getToken())
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            pets = Pet.build(response.getJSONArray("pets"));
                            petsByMonth();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onError(ANError anError) {
                    }
                });
    }
}
