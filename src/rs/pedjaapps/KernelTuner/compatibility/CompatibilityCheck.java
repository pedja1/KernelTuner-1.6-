package rs.pedjaapps.KernelTuner.compatibility;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;


import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;


public class CompatibilityCheck extends Activity
{

	private int count = 0;
	private boolean saf;
	private boolean vs;
	private boolean fs;
	private boolean fc;
	private boolean tis;
	private boolean uv;
	private boolean cd;
	private boolean lt;
	private boolean bl;
	private boolean s2w;
	private boolean sdc;
	private boolean sh;

	SharedPreferences sharedPrefs;

	private class exec extends AsyncTask<Void, Integer, Void>
	{


		@Override
		protected Void doInBackground(Void... args)
		{
			
			try
			{

				File myFile = new File(
					"/sys/devices/system/cpu/cpu0/cpufreq/scaling_available_frequencies");
				FileInputStream fIn = new FileInputStream(myFile);

				count = count + 1;
				saf = true;
				fIn.close();
			}
			catch (IOException e)
			{
				saf = false;
			}

			publishProgress(3);
			try
			{

				File myFile = new File(
					"/sys/devices/system/cpu/cpufreq/vdd_table/vdd_levels");
				FileInputStream fIn = new FileInputStream(myFile);

				count = count + 1;
				uv = true;
				fIn.close();
			}
			catch (IOException e)
			{
				uv = false;
			}
			publishProgress(4);
			try
			{

				File myFile = new File(
					"/sys/devices/system/cpu/cpu0/cpufreq/stats/time_in_state");
				FileInputStream fIn = new FileInputStream(myFile);

				count = count + 1;
				tis = true;
				fIn.close();
			}
			catch (IOException e)
			{
				tis = false;
			}
			publishProgress(5);
			try
			{

				File myFile = new File(
					"/sys/kernel/notification_leds/off_timer_multiplier");
				FileInputStream fIn = new FileInputStream(myFile);

				count = count + 1;
				lt = true;
				fIn.close();
			}
			catch (IOException e)
			{
				lt = false;
			}
			publishProgress(6);
			try
			{

				File myFile = new File(
					"/sys/devices/platform/leds-pm8058/leds/button-backlight/currents");
				FileInputStream fIn = new FileInputStream(myFile);

				count = count + 1;
				bl = true;
				fIn.close();
			}
			catch (IOException e)
			{
				try
				{

					File myFile = new File(
						"/sys/devices/platform/msm_ssbi.0/pm8921-core/pm8xxx-led/leds/button-backlight/currents");
					FileInputStream fIn = new FileInputStream(myFile);

					count = count + 1;
					bl = true;
					fIn.close();
				}
				catch (IOException e1)
				{
					bl = false;
					
				}
			}
			publishProgress(7);
			
			
			try
			{

				File myFile = new File(
					"/sys/kernel/fast_charge/force_fast_charge");
				FileInputStream fIn = new FileInputStream(myFile);

				count = count + 1;
				fc = true;
				fIn.close();

			}
			catch (IOException e)
			{
				fc = false;
			}
			publishProgress(8);
			try
			{

				File myFile = new File(
					"/sys/kernel/debug/msm_fb/0/vsync_enable");
				FileInputStream fIn = new FileInputStream(myFile);

				count = count + 1;
				vs = true;
				fIn.close();
			}
			catch (IOException e)
			{
				vs = false;
			}
			publishProgress(9);

			try
			{

				File myFile = new File("/sys/kernel/debug/msm_fb/0/bpp");
				FileInputStream fIn = new FileInputStream(myFile);

				count = count + 1;
				cd = true;
				fIn.close();

			}
			catch (IOException e)
			{
				cd = false;

			}
			publishProgress(10);
			try
			{

				File myFile = new File(
					"/sys/kernel/dyn_fsync/Dyn_fsync_version");
				FileInputStream fIn = new FileInputStream(myFile);
				count = count + 1;
				fs = true;
				fIn.close();
			}
			catch (IOException e)
			{
				fs = false;
			}
			publishProgress(11);
			try
			{

				File myFile = new File("/sys/android_touch/sweep2wake");
				FileInputStream fIn = new FileInputStream(myFile);

				count = count + 1;
				s2w = true;
				fIn.close();
			}
			catch (IOException e)
			{
				s2w = false;
			}
			
			publishProgress(12);
			try
			{

				File myFile = new File(
					"/sys/devices/virtual/bdi/179:0/read_ahead_kb");
				FileInputStream fIn = new FileInputStream(myFile);
				count = count + 1;
				sdc = true;
				fIn.close();

			}
			catch (Exception e)
			{

				sdc = false;
			}
			publishProgress(13);

			try
			{

				File myFile = new File("/sys/block/mmcblk0/queue/scheduler");
				FileInputStream fIn = new FileInputStream(myFile);
				count = count + 1;
				sh = true;
				fIn.close();

			}
			catch (Exception e)
			{
				sh = false;
			}

			publishProgress(14);

			return null;
		}

		@Override
		protected void onProgressUpdate(Integer... values)
		{

			super.onProgressUpdate();
			
			LinearLayout ll1 = (LinearLayout) findViewById(R.id.ll1);
			LinearLayout ll2 = (LinearLayout) findViewById(R.id.ll2);
			LinearLayout ll3 = (LinearLayout) findViewById(R.id.ll3);
			LinearLayout ll4 = (LinearLayout) findViewById(R.id.ll4);
			LinearLayout ll5 = (LinearLayout) findViewById(R.id.ll5);
			LinearLayout ll8 = (LinearLayout) findViewById(R.id.ll8);
			LinearLayout ll9 = (LinearLayout) findViewById(R.id.ll9);
			LinearLayout ll10 = (LinearLayout) findViewById(R.id.ll10);
			LinearLayout ll11 = (LinearLayout) findViewById(R.id.ll11);
			LinearLayout ll12 = (LinearLayout) findViewById(R.id.ll12);
			LinearLayout ll15 = (LinearLayout) findViewById(R.id.ll15);
			LinearLayout ll16 = (LinearLayout) findViewById(R.id.ll16);
			TextView saftv = (TextView) findViewById(R.id.textView2);
			TextView uvtv = (TextView) findViewById(R.id.textView4);
			TextView tistv = (TextView) findViewById(R.id.textView6);
			TextView lttv = (TextView) findViewById(R.id.textView8);
			TextView bltv = (TextView) findViewById(R.id.textView10);
			TextView fctv = (TextView) findViewById(R.id.textView16);
			TextView vstv = (TextView) findViewById(R.id.textView18);
			TextView cdtv = (TextView) findViewById(R.id.textView20);
			TextView fstv = (TextView) findViewById(R.id.textView22);
			TextView s2wtv = (TextView) findViewById(R.id.textView24);
			TextView sdctv = (TextView) findViewById(R.id.textView30);
			TextView shtv = (TextView) findViewById(R.id.textView32);

			if (values[0] == 3)
			{
				ll1.setVisibility(View.VISIBLE);
				if (saf == true)
				{
					saftv.setText("[OK]");
					saftv.setTextColor(Color.GREEN);
				}
				else
				{
					saftv.setText("[Not Found]");
					saftv.setTextColor(Color.RED);
				}
			}
			if (values[0] == 4)
			{
				ll2.setVisibility(View.VISIBLE);
				if (uv == true)
				{
					uvtv.setText("[OK]");
					uvtv.setTextColor(Color.GREEN);
				}
				else
				{
					uvtv.setText("[Not Found]");
					uvtv.setTextColor(Color.RED);
				}
			}
			if (values[0] == 5)
			{
				ll3.setVisibility(View.VISIBLE);
				if (tis == true)
				{
					tistv.setText("[OK]");
					tistv.setTextColor(Color.GREEN);
				}
				else
				{
					tistv.setText("[Not Found]");
					tistv.setTextColor(Color.RED);
				}
			}
			if (values[0] == 6)
			{
				ll4.setVisibility(View.VISIBLE);
				if (lt == true)
				{
					lttv.setText("[OK]");
					lttv.setTextColor(Color.GREEN);
				}
				else
				{
					lttv.setText("[Not Found]");
					lttv.setTextColor(Color.RED);
				}
			}
			if (values[0] == 7)
			{
				ll5.setVisibility(View.VISIBLE);
				if (bl == true)
				{
					bltv.setText("[OK]");
					bltv.setTextColor(Color.GREEN);
				}
				else
				{
					bltv.setText("[Not Found]");
					bltv.setTextColor(Color.RED);
				}
			}
			
			if (values[0] == 8)
			{
				ll8.setVisibility(View.VISIBLE);
				if (fc == true)
				{
					fctv.setText("[OK]");
					fctv.setTextColor(Color.GREEN);
				}
				else
				{
					fctv.setText("[Not Found]");
					fctv.setTextColor(Color.RED);
				}
			}
			if (values[0] == 9)
			{
				ll9.setVisibility(View.VISIBLE);
				if (vs == true)
				{
					vstv.setText("[OK]");
					vstv.setTextColor(Color.GREEN);
				}
				else
				{
					vstv.setText("[Not Found]");
					vstv.setTextColor(Color.RED);
				}
			}
			if (values[0] == 10)
			{
				ll10.setVisibility(View.VISIBLE);
				if (cd == true)
				{
					cdtv.setText("[OK]");
					cdtv.setTextColor(Color.GREEN);
				}
				else
				{
					cdtv.setText("[Not Found]");
					cdtv.setTextColor(Color.RED);
				}
			}
			if (values[0] == 11)
			{
				ll11.setVisibility(View.VISIBLE);
				if (fs == true)
				{
					fstv.setText("[OK]");
					fstv.setTextColor(Color.GREEN);
				}
				else
				{
					fstv.setText("[Not Found]");
					fstv.setTextColor(Color.RED);
				}
			}
			if (values[0] == 12)
			{
				ll12.setVisibility(View.VISIBLE);
				if (s2w == true)
				{
					s2wtv.setText("[OK]");
					s2wtv.setTextColor(Color.GREEN);
				}
				else
				{
					s2wtv.setText("[Not Found]");
					s2wtv.setTextColor(Color.RED);
				}
			}
			

			if (values[0] == 13)
			{
				ll15.setVisibility(View.VISIBLE);
				if (sdc == true)
				{
					sdctv.setText("[OK]");
					sdctv.setTextColor(Color.GREEN);
				}
				else
				{
					sdctv.setText("[Not Found]");
					sdctv.setTextColor(Color.RED);
				}
			}
			if (values[0] == 14)
			{
				ll16.setVisibility(View.VISIBLE);
				if (sh == true)
				{
					shtv.setText("[OK]");
					shtv.setTextColor(Color.GREEN);
				}
				else
				{
					shtv.setText("[Not Found]");
					shtv.setTextColor(Color.RED);
				}
			}

		}

		@Override
		protected void onPostExecute(Void result)
		{
			TextView res = (TextView) findViewById(R.id.textView34);
			int cn = count * 100 / 16;
			res.setText(String.valueOf(cn) + "%");
			if (cn < 30)
			{
				res.setTextColor(Color.RED);
			}
			else if (cn > 30 && cn < 50)
			{
				res.setTextColor(Color.MAGENTA);
			}
			else if (cn > 50 && cn < 70)
			{
				res.setTextColor(Color.YELLOW);
			}
			else if (cn > 70 && cn < 90)
			{
				res.setTextColor(Color.BLUE);
			}
			else if (cn > 90)
			{
				res.setTextColor(Color.GREEN);
			}
			ProgressBar pb = (ProgressBar) findViewById(R.id.progressBar1);
			LinearLayout resultll = (LinearLayout) findViewById(R.id.resultll);
			resultll.setVisibility(View.VISIBLE);
			pb.setVisibility(View.GONE);
			Button ok = (Button) findViewById(R.id.button1);
			ok.setVisibility(View.VISIBLE);


		}

	}

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		setContentView(R.layout.check);
		new exec().execute();
		Button ok = (Button) findViewById(R.id.button1);
		ok.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0)
				{
					
					finish();

				}
			});

	}
}
