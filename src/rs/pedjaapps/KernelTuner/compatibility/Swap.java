package rs.pedjaapps.KernelTuner.compatibility;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;




import android.app.Activity;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

public class Swap extends Activity
{


	private ProgressDialog pd = null;
	private String[] swapSize = {"64","128","256","512","758","1024"};
	private String[] swapLocation = {"/data/",String.valueOf(Environment.getExternalStorageDirectory()) + "/"};

	private int swapSizeSelected;
	private String swapLocationSelected;
	private String swappinessSelected;
	private String swaps;
	private  String currentSwappiness;
	private String swapLocationCurrent;
    public SharedPreferences preferences;

    private List<String> swapsList = new ArrayList<String>();
    private List<String> temp = new ArrayList<String>();

	private class deactivateSwap extends AsyncTask<String, Void, Object>
	{


		@Override
		protected Object doInBackground(String... args)
		{
			
			try {
	            String line;
	            Process process = Runtime.getRuntime().exec("su");
	            OutputStream stdin = process.getOutputStream();
	            InputStream stderr = process.getErrorStream();
	            InputStream stdout = process.getInputStream();

	            stdin.write(("swapoff " + swapLocationCurrent.trim() + "\n").getBytes());
	            
	            stdin.flush();

	            stdin.close();
	            BufferedReader brCleanUp =
	                    new BufferedReader(new InputStreamReader(stdout));
	            while ((line = brCleanUp.readLine()) != null) {
	                Log.d("[KernelTuner Swap Output]", line);
	            }
	            brCleanUp.close();
	            brCleanUp =
	                    new BufferedReader(new InputStreamReader(stderr));
	            while ((line = brCleanUp.readLine()) != null) {
	            	Log.e("[KernelTuner Swap Error]", line);
	            }
	            brCleanUp.close();

	        } catch (IOException ex) {
	        }

			return "";
		}

		@Override
		protected void onPostExecute(Object result)
		{
			preferences = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
			SharedPreferences.Editor editor = preferences.edit();
			editor.putBoolean("swap", false);
			editor.putString("swap_location", swapLocationSelected + "swap");
			editor.commit();


			updateUI();
			Swap.this.pd.dismiss();

		}
	}

	private class activateSwap extends AsyncTask<String, Void, Object>
	{


		@Override
		protected Object doInBackground(String... args)
		{

			try {
	            String line;
	            Process process = Runtime.getRuntime().exec("su");
	            OutputStream stdin = process.getOutputStream();
	            InputStream stderr = process.getErrorStream();
	            InputStream stdout = process.getInputStream();

	            stdin.write(("swapon " + swapLocationSelected.trim() + "/swap" + "\n").getBytes());
	            
	            stdin.flush();

	            stdin.close();
	            BufferedReader brCleanUp =
	                    new BufferedReader(new InputStreamReader(stdout));
	            while ((line = brCleanUp.readLine()) != null) {
	                Log.d("[KernelTuner Swap Output]", line);
	            }
	            brCleanUp.close();
	            brCleanUp =
	                    new BufferedReader(new InputStreamReader(stderr));
	            while ((line = brCleanUp.readLine()) != null) {
	            	Log.e("[KernelTuner Swap Error]", line);
	            }
	            brCleanUp.close();

	        } catch (IOException ex) {
	        }

			return "";
		}

		@Override
		protected void onPostExecute(Object result)
		{
			preferences = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
			SharedPreferences.Editor editor = preferences.edit();
    	    editor.putBoolean("swap", true);
    	    editor.putString("swap_location", swapLocationSelected + "swap");
			editor.commit();
			updateUI();
			Swap.this.pd.dismiss();

		}
	}

	private class setSwappiness extends AsyncTask<String, Void, Object>
	{


		@Override
		protected Object doInBackground(String... args)
		{

			try {
	            String line;
	            Process process = Runtime.getRuntime().exec("su");
	            OutputStream stdin = process.getOutputStream();
	            InputStream stderr = process.getErrorStream();
	            InputStream stdout = process.getInputStream();

	            stdin.write(("echo " + swappinessSelected + " > /proc/sys/vm/swappiness\n").getBytes());
	            
	            stdin.flush();

	            stdin.close();
	            BufferedReader brCleanUp =
	                    new BufferedReader(new InputStreamReader(stdout));
	            while ((line = brCleanUp.readLine()) != null) {
	                Log.d("[KernelTuner Swap Output]", line);
	            }
	            brCleanUp.close();
	            brCleanUp =
	                    new BufferedReader(new InputStreamReader(stderr));
	            while ((line = brCleanUp.readLine()) != null) {
	            	Log.e("[KernelTuner Swap Error]", line);
	            }
	            brCleanUp.close();

	        } catch (IOException ex) {
	        }

			return "";
		}

		@Override
		protected void onPostExecute(Object result)
		{
			preferences = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
			SharedPreferences.Editor editor = preferences.edit();
    	    editor.putString("swappiness", swappinessSelected);
			editor.commit();
			updateUI();
			Swap.this.pd.dismiss();

		}
	}

	private class createSwap extends AsyncTask<String, Void, Object>
	{


		@Override
		protected Object doInBackground(String... args)
		{

			try {
	            String line;
	            Process process = Runtime.getRuntime().exec("su");
	            OutputStream stdin = process.getOutputStream();
	            InputStream stderr = process.getErrorStream();
	            InputStream stdout = process.getInputStream();

	            stdin.write(("busybox dd if=/dev/zero of=" + swapLocationSelected.trim() + "/swap bs=1k count=" + swapSizeSelected + "\n").getBytes());
	            stdin.write(("mkswap " + swapLocationSelected.trim() + "swap\n").getBytes());
	            
	            stdin.flush();

	            stdin.close();
	            BufferedReader brCleanUp =
	                    new BufferedReader(new InputStreamReader(stdout));
	            while ((line = brCleanUp.readLine()) != null) {
	                Log.d("[KernelTuner Swap Output]", line);
	            }
	            brCleanUp.close();
	            brCleanUp =
	                    new BufferedReader(new InputStreamReader(stderr));
	            while ((line = brCleanUp.readLine()) != null) {
	            	Log.e("[KernelTuner Swap Error]", line);
	            }
	            brCleanUp.close();

	        } catch (IOException ex) {
	        }

			return "";
		}

		@Override
		protected void onPreExecute()
		{
			Swap.this.pd = ProgressDialog.show(Swap.this, "Creating swap file...", "This can take a while...\nPlease be patient...", true, false);

		}

		@Override
		protected void onPostExecute(Object result)
		{

			updateUI();
			Swap.this.pd.dismiss();

		}
	}

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		setContentView(R.layout.swap);
		
		this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN); 

		Button activate = (Button)findViewById(R.id.button1);
		Button create = (Button)findViewById(R.id.button2);
		Button swappiness = (Button)findViewById(R.id.button3);
		Button deactivate = (Button)findViewById(R.id.button4);

		final EditText ed = (EditText)findViewById(R.id.EditText1);
		updateUI();


		swappiness.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View arg0)
				{
					
					Swap.this.pd = ProgressDialog.show(Swap.this, "Please wait...", "Changing Swappiness...", true, false);
					swappinessSelected = ed.getText().toString();
					new setSwappiness().execute();
				}

			});



		deactivate.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View arg0)
				{
					
					Swap.this.pd = ProgressDialog.show(Swap.this, "Please wait...", "Deactivating swap...", true, false);

					new deactivateSwap().execute();
				}

			});

		activate.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View arg0)
				{
				
					Swap.this.pd = ProgressDialog.show(Swap.this, "Please wait...", "Activating swap...", true, false);

					new activateSwap().execute();
				}

			});

		create.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View arg0)
				{
				

					new createSwap().execute();
				}

			});

    }

	private  void updateUI()
	{
		currentSwappiness();
		spinnerSwapSize();
		spinnerSwapLocation();
		swaps();
		EditText ed = (EditText)findViewById(R.id.EditText1);
		Button activate = (Button)findViewById(R.id.button1);
		Button create = (Button)findViewById(R.id.button2);
		Button swappiness = (Button)findViewById(R.id.button3);

		Button deactivate = (Button)findViewById(R.id.button4);
		TextView status = (TextView)findViewById(R.id.textView2);
		LinearLayout ll = (LinearLayout)findViewById(R.id.LinearLayout1);
		LinearLayout ll1 = (LinearLayout)findViewById(R.id.ll1);
		LinearLayout ll2 = (LinearLayout)findViewById(R.id.ll2);


		TextView swappinesstxt = (TextView)findViewById(R.id.textView6);
		TextView total = (TextView)findViewById(R.id.textView7);
		TextView used = (TextView)findViewById(R.id.textView8);
		TextView free = (TextView)findViewById(R.id.textView9);
		TextView location = (TextView)findViewById(R.id.textView10);
		if (!currentSwappiness.equals("err"))
		{
			ed.setText(currentSwappiness);
		}
		else
		{
			ll.setVisibility(View.GONE);
		}

    	if (new File("/data/swap").exists())
		{
    		//System.out.println("swap file found on /data");
    		create.setVisibility(View.GONE);
    		ll1.setVisibility(View.GONE);
    		ll2.setVisibility(View.GONE);
    		activate.setVisibility(View.VISIBLE);
    		deactivate.setVisibility(View.VISIBLE);
    		swappiness.setVisibility(View.VISIBLE);
    	}	
    	else if (new File(String.valueOf(Environment.getExternalStorageDirectory()) + "/swap").exists())
		{
    		//System.out.println("swap file found on /sdcard");
    		create.setVisibility(View.GONE);
    		ll1.setVisibility(View.GONE);
    		ll2.setVisibility(View.GONE);
    		activate.setVisibility(View.VISIBLE);
    		deactivate.setVisibility(View.VISIBLE);
    		swappiness.setVisibility(View.VISIBLE);
    	}
    	else
		{
    		create.setVisibility(View.VISIBLE);
    		ll1.setVisibility(View.VISIBLE);
    		ll2.setVisibility(View.VISIBLE);
    		activate.setVisibility(View.GONE);
    		deactivate.setVisibility(View.GONE);
    		swappiness.setVisibility(View.GONE);

    	}

    	if (swaps.equals("") && new File(String.valueOf(Environment.getExternalStorageDirectory()) + "/swap").exists() || new File("/data/swap").exists())
		{
    		deactivate.setVisibility(View.GONE);
    		activate.setVisibility(View.VISIBLE);
    		ll2.setVisibility(View.VISIBLE);
    	}
    	else if (!swaps.equals("") && new File(String.valueOf(Environment.getExternalStorageDirectory()) + "/swap").exists() || new File("/data/swap").exists())
		{
    		activate.setVisibility(View.GONE);
    		deactivate.setVisibility(View.VISIBLE);
    		ll2.setVisibility(View.GONE);

    	}

    	if (!swaps.equals("") && !swaps.equals("err"))
		{
			String[] swapsArray = swaps.split("\\s");
			swapLocationCurrent = swapsArray[0];
			temp = Arrays.asList(swapsArray); 
			for (String s : temp)
			{
				if (s != null && s.length() > 0)
				{
					swapsList.add(s);
				}
    	    }
    	}
    	if (!swaps.equals("") && !swaps.equals("err"))
		{
    		status.setText("Swap Status: Activated");
    		swappinesstxt.setText("Swappiness: " + currentSwappiness + "%");
    		total.setText("Total Swap Memory: " + Integer.parseInt(swapsList.get(2)) / 1024 + "MB");
    		used.setText("Used Swap Memory: " + Integer.parseInt(swapsList.get(3)) / 1024 + "MB");
    		free.setText("Free Swap Memory: " + (Integer.parseInt(swapsList.get(2)) / 1024 - Integer.parseInt(swapsList.get(3)) / 1024) + "MB");
    		location.setText("Swap file location: " + swapsList.get(0));
    	}
    	else if (swaps.equals(""))
		{
    		status.setText("Swap Status: Deactivated");
    		swappinesstxt.setVisibility(View.GONE);
    		total.setVisibility(View.GONE);
    		used.setVisibility(View.GONE);
    		free.setVisibility(View.GONE);
    		location.setVisibility(View.GONE);
    	}
    	if (new File(String.valueOf(Environment.getExternalStorageDirectory()) + "/swap").exists())
		{
    		swapLocationCurrent = String.valueOf(Environment.getExternalStorageDirectory()) + "/swap";
    	}
    	else if (new File("/data/swap").exists())
		{
    		swapLocationCurrent = "/data/swap";
    	}
	} 

	private void currentSwappiness()
	{



		try
		{

    		File myFile = new File("/proc/sys/vm/swappiness");
    		FileInputStream fIn = new FileInputStream(myFile);

    		BufferedReader myReader = new BufferedReader(
				new InputStreamReader(fIn));
    		String aDataRow = "";
    		String aBuffer = "";
    		while ((aDataRow = myReader.readLine()) != null)
			{
    			aBuffer += aDataRow + "\n";
    		}

    		currentSwappiness = aBuffer.trim();
    		myReader.close();

    	}
		catch (Exception e)
		{
    		currentSwappiness = "err";
    	}


	}

	public void swaps()
	{



		try
		{

    		File myFile = new File("/proc/swaps");
    		FileInputStream fIn = new FileInputStream(myFile);

    		BufferedReader myReader = new BufferedReader(
				new InputStreamReader(fIn));
    		String aDataRow = "";
    		String aBuffer = "";
    		myReader.readLine();
    		while ((aDataRow = myReader.readLine()) != null)
			{

    			aBuffer += aDataRow + "\n";
    		}

    		swaps = aBuffer.trim();

    		myReader.close();

    	}
		catch (Exception e)
		{
    		swaps = "err";
    	}


	}

	private void spinnerSwapSize()
	{


    	Spinner spinner = (Spinner) findViewById(R.id.spinner1);
    	ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this,   android.R.layout.simple_spinner_item, swapSize);
    	spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down vieww
    	spinner.setAdapter(spinnerArrayAdapter);

    	spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
				@Override
				public void onItemSelected(AdapterView<?> parent, View view, int pos, long id)
				{
					swapSizeSelected = Integer.parseInt(parent.getItemAtPosition(pos).toString()) * 1024;

				}

				@Override
				public void onNothingSelected(AdapterView<?> parent)
				{
					//do nothing
				}
			});



    }

	private void spinnerSwapLocation()
	{


		Spinner spinner = (Spinner) findViewById(R.id.spinner2);

		// Application of the Array to the Spinner
		ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(Swap.this,   android.R.layout.simple_spinner_item, swapLocation);
		spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down vieww
		spinner.setAdapter(spinnerArrayAdapter);

		spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
				@Override
				public void onItemSelected(AdapterView<?> parent, View view, int pos, long id)
				{
					swapLocationSelected = parent.getItemAtPosition(pos).toString();

				}

				@Override
				public void onNothingSelected(AdapterView<?> parent)
				{
					//do nothing
				}
			});

	}
    

}
