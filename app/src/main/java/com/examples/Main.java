package com.examples;

import android.app.Activity;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class Main extends Activity implements OnClickListener
{
	private static final String tag = "Main";
	private MalibuCountDownTimer countDownTimer;
	private long timeElapsed;
	private boolean timerHasStarted = false;
	private Button startB;
	private TextView text;
	private TextView timeElapsedView;

	private final long startTime = 50000;
	private final long interval = 1000;

	int x=0;

	GPSTracker gps;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		startB = (Button) this.findViewById(R.id.button);
		startB.setOnClickListener(this);

		text = (TextView) this.findViewById(R.id.timer);
		timeElapsedView = (TextView) this.findViewById(R.id.timeElapsed);
		countDownTimer = new MalibuCountDownTimer(startTime, interval);
		text.setText(text.getText() + String.valueOf(startTime));
	}

	@Override
	public void onClick(View v)
	{
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
			if (x==5) {

				/* Empieza GPS */

				GPSTracker servicio = new GPSTracker(getApplicationContext());

				servicio.getLocation();

				Toast.makeText(getApplicationContext(), "Coords: "+servicio.getLatitud()+","+servicio.getLongitud(),
						Toast.LENGTH_LONG).show();
				Log.i("TAG", "Coords: " + servicio.getLatitud() + "," + servicio.getLongitud());


				/*

				gps = new GPSTracker(Main.this);

				if(gps.canGetLocation()) {
					double latitude = gps.getLatitude();
					double longitude = gps.getLongitude();

					Toast.makeText(
							getApplicationContext(),
							"Tu situación GPS es \nLat: " + latitude + "\nLong: "
									+ longitude, Toast.LENGTH_LONG).show();



				} else {
					gps.showSettingsAlert();
				}

				*/

				/* FIN GPS */

				//Toast.makeText(getApplicationContext(), "Cada 5 segundos veo GPS", Toast.LENGTH_LONG).show();

				x=0;
			}
			text.setText("Time remain:" + millisUntilFinished);
			timeElapsed = startTime - millisUntilFinished;
			timeElapsedView.setText("Time Elapsed: " + String.valueOf(timeElapsed));
		}
	}
}