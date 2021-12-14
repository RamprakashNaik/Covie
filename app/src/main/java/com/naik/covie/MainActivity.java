package com.naik.covie;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.naik.covie.api.ApiUtilities;
import com.naik.covie.api.CountryData;

import org.eazegraph.lib.charts.PieChart;
import org.eazegraph.lib.models.PieModel;
import org.w3c.dom.Text;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MainActivity extends AppCompatActivity {

    private TextView totalconfirm, totalrecovered, totaldeath, totalactive, totaltest;
    private TextView todayconfirm, todaydeath, todayrecovered, dateTV;
    private List<CountryData> list;
    Button callnow;
    String country = "India";
    private PieChart piechart;
    private int number ;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        list = new ArrayList<>();
        if(getIntent().getStringExtra("country") != null)
            country = getIntent().getStringExtra("country");



        init();
        callnow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                call();
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + number));
                startActivity(intent);
            }
        });


        TextView cname = findViewById(R.id.cname);
        cname.setText(country);

        cname.setOnClickListener(v ->
                startActivity(new Intent(MainActivity.this, CountryActivity.class)));

        ApiUtilities.getApiInterface().getCountryData()
                .enqueue(new Callback<List<CountryData>>() {
                    @Override
                    public void onResponse(Call<List<CountryData>> call, Response<List<CountryData>> response) {
                        list.addAll(response.body());

                        for(int i =0; i<list.size(); i++){
                            if (list.get(i).getCountry().equals(country)){
                                int confirm = Integer.parseInt(list.get(i).getCases());
                                int active = Integer.parseInt(list.get(i).getActive());
                                int recovered = Integer.parseInt(list.get(i).getRecovered());
                                int death = Integer.parseInt(list.get(i).getDeaths());

                                totalactive.setText(NumberFormat.getInstance().format(active));
                                totalconfirm.setText(NumberFormat.getInstance().format(confirm));
                                totalrecovered.setText(NumberFormat.getInstance().format(recovered));
                                totaldeath.setText(NumberFormat.getInstance().format(death));

                                todaydeath.setText("(+"+NumberFormat.getInstance().format(Integer.parseInt(list.get(i).getTodayDeaths()))+")");
                                todayconfirm.setText("(+"+NumberFormat.getInstance().format(Integer.parseInt(list.get(i).getTodayCases()))+")");
                                todayrecovered.setText("(+"+NumberFormat.getInstance().format(Integer.parseInt(list.get(i).getTodayRecovered()))+")");

                                totaltest.setText(NumberFormat.getInstance().format(Integer.parseInt(list.get(i).getTests())));

                                setText(list.get(i).getUpdated());

                                piechart.addPieSlice(new PieModel("Confirm", confirm, getResources().getColor(R.color.yellow)));
                                piechart.addPieSlice(new PieModel("Active", active, getResources().getColor(R.color.blue_pie)));
                                piechart.addPieSlice(new PieModel("Recovered", recovered, getResources().getColor(R.color.green_pie)));
                                piechart.addPieSlice(new PieModel("Death", death, getResources().getColor(R.color.red_pie)));

                                piechart.startAnimation();
                               // call();


                            }
                        }


                    }


                    @Override
                    public void onFailure(Call<List<CountryData>> call, Throwable t) {
                        Toast.makeText(MainActivity.this,"Error" + t.getMessage(),Toast.LENGTH_LONG);

                    }
                });


    }

    private void setText(String updated) {
        DateFormat format = new SimpleDateFormat("MMM dd, yyyy");

        long milliseconds = Long.parseLong(updated);

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliseconds);

        dateTV.setText("Updated at " + format.format(calendar.getTime()));
    }

    private void init(){

        totalconfirm = findViewById(R.id.totalconfirm);
        totalrecovered = findViewById(R.id.totalrecovered);
        totaldeath = findViewById(R.id.totaldeath);
        totalactive = findViewById(R.id.totalactive);
        todayconfirm = findViewById(R.id.todayconfirm);
        todaydeath = findViewById(R.id.todaydeath);
        todayrecovered = findViewById(R.id.todayrecovered);
        piechart = findViewById(R.id.piechart);
        totaltest = findViewById(R.id.totaltest);
        dateTV = findViewById(R.id.date);
        callnow = findViewById(R.id.callnow);


    }

    private void call(){
        if(country.equals("India")){
            number = 1075;
        }
        else if(country.equals("Afghanistan")){
            number = 11043;
        }
        else if(country.equals("Australia")){
            number = 1800020080;
        }
        else if(country.equals("Albania")){
            number = 116111;
        }
        else if(country.equals("Argentina")){
            number = 18002004;
        }
        else if(country.equals("Brazil")){
            number = 110824;
        }
        else if(country.equals("USA")){
            number = 911;
        }
        else if(country.equals("Germany")){
            number = 030346465100;
        }
        else if(country.equals("UK")){
            number = 808800002;
        }
        else{
            number = 911;
        }
    }




}