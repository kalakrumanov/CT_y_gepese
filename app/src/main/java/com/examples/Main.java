package com.examples;

import android.app.Activity;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class Main extends Activity
{
    private static final String tag = "Main";
    private MalibuCountDownTimer countDownTimer;
    private long timeElapsed;
    private boolean timerHasStarted = false;
    private Button startB;
    private TextView text;
    private TextView timeElapsedView;

    private long startTime = 50000;
    private long interval = 1000;

    private Spinner sCuantosSegundos;
    private Button btnSubmit;

    int x=0;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        InputOutputLocal util = new InputOutputLocal();
        util.writeConfiguration(this);
        util.readFileFromInternalStorage("config.txt", this);



        /** ELEGIR SEGUNDOS **/

        //addItemsOnSpinner2();
        addListenerOnButton();
        addListenerOnSpinnerItemSelection();


        /** CONTADOR **/

        startB = (Button) this.findViewById(R.id.empezarContador);
        startB.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!timerHasStarted)
                {
                    countDownTimer.start();
                    timerHasStarted = true;
                    startB.setText("Start");
                }
                else
                {

                    countDownTimer.cancel();
                    timerHasStarted = false;
                    startB.setText("RESET");
                }
            }
        });


        text = (TextView) this.findViewById(R.id.timer);
        timeElapsedView = (TextView) this.findViewById(R.id.timeElapsed);
        countDownTimer = new MalibuCountDownTimer(startTime, interval);
        text.setText(text.getText() + String.valueOf(startTime));



    }

    private void addListenerOnSpinnerItemSelection() {
        // Salta al elegir una opcion de "eligeCuantosSegundos"
        sCuantosSegundos = (Spinner) findViewById(R.id.eligeCuantosSegundos);
        sCuantosSegundos.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                Toast.makeText(parent.getContext(),
                        "OnItemSelectedListener : " + parent.getItemAtPosition(position).toString(),
                        Toast.LENGTH_SHORT).show();


                if (sCuantosSegundos.getSelectedItemPosition() == 0) {
                    Toast.makeText(getApplicationContext(), "1 segundo",
                            Toast.LENGTH_SHORT).show();
                }

                long posicion = sCuantosSegundos.getSelectedItemPosition();

                Log.i("TAG", "Posicion: " + sCuantosSegundos.getSelectedItemPosition());

                interval = (sCuantosSegundos.getSelectedItemPosition() + 1) * 1000;


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        //startTime = sCuantosSegundos.getSelectedItemId();




    }

    private void addListenerOnButton() {



        // Cuando hago click en "Guardar Cambios"

        sCuantosSegundos = (Spinner) findViewById(R.id.eligeCuantosSegundos);
        //spinner2 = (Spinner) findViewById(R.id.spinner2);
        btnSubmit = (Button) findViewById(R.id.verSegundosElegidos);

        btnSubmit.setEnabled(false);

        btnSubmit.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                Toast.makeText(getApplicationContext(),
                        "OnClickListener : " +
                                "\nSpinner 1 : " + String.valueOf(sCuantosSegundos.getSelectedItem()),
                        Toast.LENGTH_SHORT).show();
            }

        });

    }

    /** Ejemplo de "Meter items en vivo a SPINNER **/

    /*

    private void addItemsOnSpinner2() {

        spinner2 = (Spinner) findViewById(R.id.spinner2);
        List<String> list = new ArrayList<String>();
        list.add("list 1");
        list.add("list 2");
        list.add("list 3");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner2.setAdapter(dataAdapter);

    }

     */



    // CountDownTimer class
    public class MalibuCountDownTimer extends CountDownTimer
    {

        public MalibuCountDownTimer(long startTime, long interval)
        {
            super(startTime, interval);
        }

        @Override
        public void onFinish()
        {
            text.setText("Time's up!");
            timeElapsedView.setText("Time Elapsed: " + String.valueOf(startTime));
        }

        @Override
        public void onTick(long millisUntilFinished)
        {
            x++;
            if (x==(interval) / 1000) {

				/* Empieza GPS */

                GPSTracker servicio = new GPSTracker(getApplicationContext());
                servicio.getLocation();

                Toast.makeText(getApplicationContext(), "Cada " + x + " segundos actualizo esto" +
                        "\nCoords: "+servicio.getLatitud()+","+servicio.getLongitud(),
                        Toast.LENGTH_SHORT).show();
                Log.i("TAG", "Coords: " + servicio.getLatitud() + "," + servicio.getLongitud());


                //Toast.makeText(getApplicationContext(), "Cada 5 segundos veo GPS", Toast.LENGTH_LONG).show();

                x=0;
            }
            text.setText("Time remain:" + millisUntilFinished);
            timeElapsed = startTime - millisUntilFinished;
            timeElapsedView.setText("Time Elapsed: " + String.valueOf(timeElapsed));
        }
    }
}