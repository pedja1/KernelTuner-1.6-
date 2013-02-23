package rs.pedjaapps.KernelTuner.compatibility.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.*;
import android.content.pm.*;
import android.content.res.*;
import android.os.*;
import android.preference.*;
import android.util.*;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.*;
import android.widget.*;

import com.google.ads.*;


import java.io.*;
import java.util.*;

import java.lang.Process;

import rs.pedjaapps.KernelTuner.compatibility.R;
import rs.pedjaapps.KernelTuner.compatibility.helpers.IOHelper;
import rs.pedjaapps.KernelTuner.compatibility.tools.Initd;







//EndImports 


public class KernelTuner extends Activity 
{
	private List<IOHelper.FreqsEntry> freqEntries;
	private List<IOHelper.VoltageList> voltageFreqs;
	private List<String> voltages = new ArrayList<String>();


	private AlertDialog alert;
	
	




	private boolean thread = true;
	private String freqcpu0 = "offline";
	private String freqcpu1= "offline";

	
	private String cpu0max = "       ";
	private String cpu1max = "       ";



	private float fLoad;
	
 



	
	private TextView cpu0prog;
	private TextView cpu1prog;

	private ProgressBar cpu0progbar;
	private ProgressBar cpu1progbar;

	private List<String> freqlist = new ArrayList<String>();
	private SharedPreferences preferences;
	private ProgressDialog pd = null;
	
	private int load;

	private Handler mHandler;

	private SharedPreferences.Editor editor;
	
	private class CPUToggle extends AsyncTask<String, Void, Object> {
		@Override
		protected Object doInBackground(String... args) {
			Process process;
			File file = new File("/sys/devices/system/cpu/cpu"+args[0]+"/cpufreq/scaling_governor");
			try {

				InputStream fIn = new FileInputStream(file);

				try {
					String line;
					process = Runtime.getRuntime().exec("su");
					OutputStream stdin = process.getOutputStream();
					InputStream stderr = process.getErrorStream();
					InputStream stdout = process.getInputStream();

					stdin.write(("echo 1 > /sys/kernel/msm_mpdecision/conf/enabled\n")
							.getBytes());
					stdin.write(("chmod 777 /sys/devices/system/cpu/cpu"+args[0]+"/online\n")
							.getBytes());
					stdin.write(("echo 0 > /sys/devices/system/cpu/cpu"+args[0]+"/online\n")
							.getBytes());
					stdin.write(("chown system /sys/devices/system/cpu/cpu"+args[0]+"/online\n")
							.getBytes());

					stdin.flush();

					stdin.close();
					BufferedReader brCleanUp = new BufferedReader(
							new InputStreamReader(stdout));
					while ((line = brCleanUp.readLine()) != null) {
						Log.d("[KernelTuner ToggleCPU Output]", line);
					}
					brCleanUp.close();
					brCleanUp = new BufferedReader(
							new InputStreamReader(stderr));
					while ((line = brCleanUp.readLine()) != null) {
						Log.e("[KernelTuner ToggleCPU Error]", line);
					}
					brCleanUp.close();
					if (process != null) {
						process.getErrorStream().close();
						process.getInputStream().close();
						process.getOutputStream().close();
					}
				} catch (IOException e) {

				}
				fIn.close();
			}

			catch (FileNotFoundException e) {

				try {
					String line;
					process = Runtime.getRuntime().exec("su");
					OutputStream stdin = process.getOutputStream();
					InputStream stderr = process.getErrorStream();
					InputStream stdout = process.getInputStream();

					stdin.write(("echo 0 > /sys/kernel/msm_mpdecision/conf/enabled\n")
							.getBytes());
					stdin.write(("chmod 666 /sys/devices/system/cpu/cpu"+args[0]+"/online\n")
							.getBytes());
					stdin.write(("echo 1 > /sys/devices/system/cpu/cpu"+args[0]+"/online\n")
							.getBytes());
					stdin.write(("chmod 444 /sys/devices/system/cpu/cpu"+args[0]+"/online\n")
							.getBytes());
					stdin.write(("chown system /sys/devices/system/cpu/cpu"+args[0]+"/online\n")
							.getBytes());

					stdin.flush();

					stdin.close();
					BufferedReader brCleanUp = new BufferedReader(
							new InputStreamReader(stdout));
					while ((line = brCleanUp.readLine()) != null) {
						Log.d("[KernelTuner ToggleCPU Output]", line);
					}
					brCleanUp.close();
					brCleanUp = new BufferedReader(
							new InputStreamReader(stderr));
					while ((line = brCleanUp.readLine()) != null) {
						Log.e("[KernelTuner ToggleCPU Error]", line);
					}
					brCleanUp.close();

				} catch (IOException ex) {
				}
			} catch (IOException e) {

				e.printStackTrace();
			}

			return "";
		}

		@Override
		protected void onPostExecute(Object result) {

			KernelTuner.this.pd.dismiss();
		}

	}

	
	private class mountDebugFs extends AsyncTask<String, Void, Object>
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

	            stdin.write(("mount -t debugfs debugfs /sys/kernel/debug\n").getBytes());
	           
	            stdin.flush();

	            stdin.close();
	            BufferedReader brCleanUp =
	                    new BufferedReader(new InputStreamReader(stdout));
	            while ((line = brCleanUp.readLine()) != null) {
	                Log.d("[KernelTuner MountDebugFs Output]", line);
	            }
	            brCleanUp.close();
	            brCleanUp =
	                    new BufferedReader(new InputStreamReader(stderr));
	            while ((line = brCleanUp.readLine()) != null) {
	            	Log.e("[KernelTuner MountDebugFs Error]", line);
	            }
	            brCleanUp.close();
				stderr.close();
				stdout.close();
				process.waitFor();

	        } catch (IOException ex) {
	        } catch (InterruptedException e) {
				e.printStackTrace();
			}
			return "";
		}

		@Override
		protected void onPostExecute(Object result)
		{
			

		}

	}
	

	
	boolean first;
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		freqEntries = IOHelper.frequencies();
		voltageFreqs = IOHelper.voltages();
		preferences = PreferenceManager.getDefaultSharedPreferences(this);
		editor = preferences.edit();
		setContentView(R.layout.main);
		super.onCreate(savedInstanceState);
		mHandler = new Handler();
		cpu0prog = (TextView)this.findViewById(R.id.ptextView3);
		cpu1prog = (TextView)this.findViewById(R.id.ptextView4);
		
		cpu0progbar = (ProgressBar)findViewById(R.id.progressBar1);
		cpu1progbar = (ProgressBar)findViewById(R.id.progressBar2);
		 
		
		 /**
		  * Extract assets if first launch*/
		first = preferences.getBoolean(
				"first_launch", false);
		if (first == false) {
			CopyAssets();
		}
		
		
		
			
        
		/**
		If auto update check is enabled check for updates
		*/
		File file = new File("/sys/kernel/debug");
		
		if(file.exists() && file.list().length>0){
			
			System.out.println("Debug fs already mounted");
		}
		else{
			
			System.out.println("Mounting debug fs");
			new mountDebugFs().execute();
		}
		



		/*
		Load ads if not disabled
		*/
		boolean ads = preferences.getBoolean("ads", true);
		if (ads == true)
		{AdView adView = (AdView)this.findViewById(R.id.ad);
			adView.loadAd(new AdRequest());}
		
		//SharedPreferences.Editor editor = sharedPrefs.edit(); 
		editor.putString("kernel", IOHelper.kernel());
		editor.commit();
		
        /**
		Show changelog if application updated
		*/
		changelog();

		/**
		Read all available frequency steps
		*/
		
		for(IOHelper.FreqsEntry f: freqEntries){
			freqlist.add(String.valueOf(f.getFreq()));
		}
		
		
		for(IOHelper.VoltageList v: voltageFreqs){
			voltages.add(String.valueOf(v.getFreq()));
		}
		
		initialCheck();

		/***
		Create new thread that will loop and show current frequency for each core
		*/
		new Thread(new Runnable() {
			@Override
			public void run() {
				while (thread) {
					try {
						Thread.sleep(1000);
						freqcpu0 = IOHelper.cpu0CurFreq();
						cpu0max = IOHelper.cpu0MaxFreq();
						
						
						if (IOHelper.cpu1Online() == true)
						{
							freqcpu1 = IOHelper.cpu1CurFreq();
							cpu1max = IOHelper.cpu1MaxFreq();
						}
						
						mHandler.post(new Runnable() {

							@Override
							public void run() {

										
										
										cpu0update();
										
										if (IOHelper.cpu1Online())
										{
											cpu1update();

										}
										


									}
						});
					} catch (Exception e) {

					}
				}
			}
		}).start();

			
			
			/**
			Declare buttons and set onClickListener for each
			*/
		
			
		
		

		Button voltage = (Button)findViewById(R.id.button6);
		voltage.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View v)
				{

					Intent myIntent = new Intent(KernelTuner.this, VoltageActivity.class);
					KernelTuner.this.startActivity(myIntent);
				}

			});

		Button cpu = (Button)this.findViewById(R.id.button2);
		cpu.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View v)
				{

					Intent myIntent = new Intent(KernelTuner.this, CPUActivityOld.class);
					KernelTuner.this.startActivity(myIntent);
				}
			});

		Button tis = (Button)this.findViewById(R.id.button5);
		tis.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View v)
				{
                    String tisChoice = preferences.getString("tis_open_as", "ask");
				   if(tisChoice.equals("ask")){
					AlertDialog.Builder builder = new AlertDialog.Builder(
		                    KernelTuner.this);

						builder.setTitle("Display As");
						LayoutInflater inflater = (LayoutInflater)KernelTuner.this.getSystemService
						(Context.LAYOUT_INFLATER_SERVICE);
						View view = inflater.inflate(R.layout.tis_dialog, null);
						ImageView list = (ImageView)view.findViewById(R.id.imageView1);
						ImageView chart = (ImageView)view.findViewById(R.id.imageView2);
						final CheckBox remember =(CheckBox)view.findViewById(R.id.checkBox1);
					 
						list.setOnClickListener(new OnClickListener(){

							@Override
							public void onClick(View arg0) {
								
								Intent myIntent = new Intent(KernelTuner.this, TISActivity.class);
								KernelTuner.this.startActivity(myIntent);
								if(remember.isChecked()){
								editor.putString("tis_open_as","list");
								editor.commit();
								}
								alert.dismiss();
								
							}
							
						});
						
						chart.setOnClickListener(new OnClickListener(){

							@Override
							public void onClick(View arg0) {
								
								Intent myIntent = new Intent(KernelTuner.this, TISActivityChart.class);
								KernelTuner.this.startActivity(myIntent);
								if(remember.isChecked()){
								editor.putString("tis_open_as","chart");
								editor.commit();
								}
								alert.dismiss();
								
							}
							
						});
						
						
						builder.setView(view);
						alert = builder.create();

						alert.show();
						}
						else if(tisChoice.equals("list")){
								Intent myIntent = new Intent(KernelTuner.this, TISActivity.class);
								KernelTuner.this.startActivity(myIntent);
						}
						else if(tisChoice.equals("chart")){
								Intent myIntent = new Intent(KernelTuner.this, TISActivityChart.class);
								KernelTuner.this.startActivity(myIntent);
						}
						
						

				}
			});

		
		


		Button buttongpu = (Button)this.findViewById(R.id.button4);
		buttongpu.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View v)
				{


					Intent myIntent = new Intent(KernelTuner.this, MiscTweaks.class);
					KernelTuner.this.startActivity(myIntent);

				}
			});


		Button cpu1toggle = (Button)this.findViewById(R.id.button1);
		cpu1toggle.setOnClickListener(new OnClickListener()
			{

				@Override
				public void onClick(View v)
				{


					KernelTuner.this.pd = ProgressDialog.show(KernelTuner.this, null, getResources().getString(R.string.applying_settings), true, true);
					new CPUToggle().execute(new String[] {"1"});


				}});

		
		Button governor = (Button)findViewById(R.id.button10);
		governor.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View v)
				{
					Intent myIntent = new Intent(KernelTuner.this, GovernorActivity.class);
					KernelTuner.this.startActivity(myIntent);

				}

			});

		Button oom = (Button)this.findViewById(R.id.button13);
		oom.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View v)
				{


					Intent myIntent = new Intent(KernelTuner.this, OOM.class);
					KernelTuner.this.startActivity(myIntent);

				}
			});

		Button profiles = (Button)this.findViewById(R.id.button12);
		profiles.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View v)
				{


					Intent myIntent = new Intent(KernelTuner.this, Profiles.class);
					KernelTuner.this.startActivity(myIntent);

				}
			});

		
		Button about = (Button)this.findViewById(R.id.button15);
		about.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View v)
				{


					Intent myIntent = new Intent(KernelTuner.this, About.class);
					KernelTuner.this.startActivity(myIntent);

				}
			});

		Button sys = (Button)this.findViewById(R.id.button14);
		sys.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View v)
				{


					Intent myIntent = new Intent(KernelTuner.this, SystemInfo.class);
					KernelTuner.this.startActivity(myIntent);
					

				}
			});
		
		Button swap = (Button)this.findViewById(R.id.swap);
		swap.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View v)
				{


					Intent myIntent = new Intent(KernelTuner.this, Swap.class);
					KernelTuner.this.startActivity(myIntent);
					

				}
			});
		Button sd = (Button)this.findViewById(R.id.sd);
		sd.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View v)
				{


					Intent myIntent = new Intent(KernelTuner.this, SDScannerConfigActivity.class);
					KernelTuner.this.startActivity(myIntent);
					

				}
			});
		Button ch = (Button)this.findViewById(R.id.changelog);
		ch.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View v)
				{


					Intent myIntent = new Intent(KernelTuner.this, Changelog.class);
					KernelTuner.this.startActivity(myIntent);
					

				}
			});

startCpuLoadThread();


	}


	@Override
	public void onPause()
	{

		super.onPause();

	}

	@Override
	protected void onResume()
	{
		
		/**
		Register BroadcastReceiver that will listen for battery changes and update ui
		*/
		

		/**
		I init.d is selected for restore settings on boot make inid.d files else remove them
		*/

		SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
		String boot = sharedPrefs.getString("boot", "init.d");
		if (boot.equals("init.d"))
		{
			initdExport();
		}
		else
		{
			new Initd().execute(new String[] {"rm"});
		}



		super.onResume();

	}

	@Override
	public void onStop()
	{
		
		

		super.onStop();

	}
	@Override
	public void onDestroy()
	{

		/**
		set thread false so that cpu info thread stop repeating
		*/
		thread = false;
		

		super.onDestroy();

	}

	private void setCpuLoad(){
		TextView cpuLoadTxt = (TextView)findViewById(R.id.textView1);

		ProgressBar cpuLoad = (ProgressBar)findViewById(R.id.progressBar5);
		cpuLoad.setProgress(load);
		cpuLoadTxt.setText(String.valueOf(load) + "%");
		
	}
	
	
private void startCpuLoadThread() {
		// Do something long
		Runnable runnable = new Runnable() {
			@Override
			public void run() {
				while(thread) {
					try {
						RandomAccessFile reader = new RandomAccessFile("/proc/stat", "r");
						String load = reader.readLine();

						String[] toks = load.split(" ");

						long idle1 = Long.parseLong(toks[5]);
						long cpu1 = Long.parseLong(toks[2]) + Long.parseLong(toks[3]) + Long.parseLong(toks[4])
							+ Long.parseLong(toks[6]) + Long.parseLong(toks[7]) + Long.parseLong(toks[8]);

						try {
							Thread.sleep(360);
						} catch (Exception e) {}

						reader.seek(0);
						load = reader.readLine();
						reader.close();

						toks = load.split(" ");

						long idle2 = Long.parseLong(toks[5]);
						long cpu2 = Long.parseLong(toks[2]) + Long.parseLong(toks[3]) + Long.parseLong(toks[4])
							+ Long.parseLong(toks[6]) + Long.parseLong(toks[7]) + Long.parseLong(toks[8]);

						fLoad =	 (float)(cpu2 - cpu1) / ((cpu2 + idle2) - (cpu1 + idle1));

					} catch (IOException ex) {
						ex.printStackTrace();
					}
					load =(int) (fLoad*100);
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					mHandler.post(new Runnable() {
							@Override
							public void run() {
							
								setCpuLoad();
								//	progress.setProgress(value);
							}
						});
				}
			}
		};
		new Thread(runnable).start();
	}
	
	
	private void changelog()
	{
		preferences = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
		String versionpref = preferences.getString("version", "");

		try
		{
			PackageInfo pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
			String	version = pInfo.versionName;
			if (!versionpref.equals(version))
			{

				Intent myIntent = new Intent(KernelTuner.this, Changelog.class);
				KernelTuner.this.startActivity(myIntent);
					if (first == true) {
			            CopyAssets();
	             	}
				

			}	
			SharedPreferences.Editor editor = preferences.edit();
			editor.putString("version", version);
			editor.commit();
		}
		catch (PackageManager.NameNotFoundException e)
		{
			}


	}

	
	

	
	private void initialCheck()
	{

		/**
		Show/hide certain Views depending on number of cpus
		*/
		if (IOHelper.cpu1Online() == true)
		{
			Button b2 = (Button) findViewById(R.id.button1);
			b2.setVisibility(View.VISIBLE);
			ProgressBar cpu1progbar = (ProgressBar)findViewById(R.id.progressBar2);
			cpu1progbar.setVisibility(View.VISIBLE);
			TextView tv1 = (TextView) findViewById(R.id.ptextView2);
			tv1.setVisibility(View.VISIBLE);
			TextView tv4 = (TextView) findViewById(R.id.ptextView4);
			tv4.setVisibility(View.VISIBLE);
		}
		else
		{
			Button b2 = (Button) findViewById(R.id.button1);
			b2.setVisibility(View.GONE);
			ProgressBar cpu1progbar = (ProgressBar)findViewById(R.id.progressBar2);
			cpu1progbar.setVisibility(View.GONE);
			TextView tv1 = (TextView) findViewById(R.id.ptextView2);
			tv1.setVisibility(View.GONE);
			TextView tv4 = (TextView) findViewById(R.id.ptextView4);
			tv4.setVisibility(View.GONE);
		}
		
		/**
		Check for certain files in sysfs and if they doesnt exists hide depending views
		*/
		File file4 = new File(IOHelper.CPU0_FREQS);
		File file5 = new File(IOHelper.TIMES_IN_STATE_CPU0);
		try
		{
			InputStream fIn = new FileInputStream(file4);
			fIn.close();
		}
		catch (FileNotFoundException e)
		{ 
			try
			{
				InputStream fIn = new FileInputStream(file5);
				fIn.close();
			}
			catch (FileNotFoundException e2)
			{
				Button cpu = (Button)findViewById(R.id.button2);
				cpu.setVisibility(View.GONE);
			} catch (IOException e1) {
				
			}


		} catch (IOException e) {
			
		}

		File file = new File(IOHelper.VOLTAGE_PATH);
		try
		{
			InputStream fIn = new FileInputStream(file);
			fIn.close();
		}
		catch (FileNotFoundException e)
		{ 
		 	File file2 = new File(IOHelper.VOLTAGE_PATH_TEGRA_3);
			try
			{
				InputStream fIn = new FileInputStream(file2);
				fIn.close();
			}
			catch (FileNotFoundException ex)
			{ 
				Button voltage = (Button)findViewById(R.id.button6);
				voltage.setVisibility(View.GONE);

			} catch (IOException e1) {
				
			}

		} catch (IOException e) {
			
		}

		File file2 = new File(IOHelper.TIMES_IN_STATE_CPU0);
		try
		{
			InputStream fIn = new FileInputStream(file2);
			fIn.close();
		}
		catch (FileNotFoundException e)
		{ 
			Button times = (Button)findViewById(R.id.button5);
			times.setVisibility(View.GONE);

		} catch (IOException e) {
			
		}

		

		

		
		
		



	}

	
	/**
	Create init.d files and export them to private application folder
	*/

	private void initdExport()
	{
	
		SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
		
	
		String hw = sharedPrefs.getString("hw", "");
		String cdepth = sharedPrefs.getString("cdepth", "");
		String cpu1min = sharedPrefs.getString("cpu1min", "");
		String cpu1max = sharedPrefs.getString("cpu1max", "");
		String cpu0max = sharedPrefs.getString("cpu0max", "");
		String cpu0min = sharedPrefs.getString("cpu0min", "");
	
		String fastcharge = sharedPrefs.getString("fastcharge", "");
		String mpdecisionscroff = sharedPrefs.getString("mpdecisionscroff", "");
		String backbuff = sharedPrefs.getString("backbuf", "");
		String vsync = sharedPrefs.getString("vsync", "");
		String led = sharedPrefs.getString("led", "");
		String cpu0gov = sharedPrefs.getString("cpu0gov", "");
		String cpu1gov = sharedPrefs.getString("cpu1gov", "");
		String io = sharedPrefs.getString("io", "");
		String sdcache = sharedPrefs.getString("sdcache", "");
	
		String ldt = sharedPrefs.getString("ldt", "");
		String s2w = sharedPrefs.getString("s2w", "");
		String s2wStart = sharedPrefs.getString("s2wStart", "");
		String s2wEnd = sharedPrefs.getString("s2wEnd", "");
	
		boolean swap = sharedPrefs.getBoolean("swap", false);
		String swapLocation = sharedPrefs.getString("swap_location", "");
		String swappiness = sharedPrefs.getString("swappiness", "");
		String oom = sharedPrefs.getString("oom", "");
		String otg = sharedPrefs.getString("otg", "");
		
	
		StringBuilder cpubuilder = new StringBuilder();
	
		cpubuilder.append("#!/system/bin/sh");
		cpubuilder.append("\n");
		/**
		 * cpu0
		 * */
		if (!cpu0gov.equals(""))
		{
			cpubuilder.append("chmod 666 /sys/devices/system/cpu/cpu0/cpufreq/scaling_governor \n" +
							  "echo " + "\"" + cpu0gov + "\"" + " > /sys/devices/system/cpu/cpu0/cpufreq/scaling_governor\n"+
							  "chmod 444 /sys/devices/system/cpu/cpu0/cpufreq/scaling_governor \n");
		}
		if (!cpu0max.equals(""))
		{
			cpubuilder.append("chmod 666 /sys/devices/system/cpu/cpu0/cpufreq/scaling_max_freq \n" +
							  "echo " + "\"" + cpu0max + "\"" + " > /sys/devices/system/cpu/cpu0/cpufreq/scaling_max_freq \n"+
							  "chmod 444 /sys/devices/system/cpu/cpu0/cpufreq/scaling_max_freq\n" );
		}
		if (!cpu0min.equals(""))
		{
			cpubuilder.append("chmod 666 /sys/devices/system/cpu/cpu0/cpufreq/scaling_min_freq \n" +
							  "echo " + "\"" + cpu0min + "\"" + " > /sys/devices/system/cpu/cpu0/cpufreq/scaling_min_freq \n\n"+
							  "chmod 444 /sys/devices/system/cpu/cpu0/cpufreq/scaling_min_freq \n");
		}
		/**
		 * cpu1
		 * */
		if (!cpu1gov.equals(""))
		{
			cpubuilder.append("chmod 666 /sys/devices/system/cpu/cpu1/cpufreq/scaling_governor \n" +
							  "echo " + "\"" + cpu1gov + "\"" + " > /sys/devices/system/cpu/cpu1/cpufreq/scaling_governor\n"+
							 "chmod 444 /sys/devices/system/cpu/cpu1/cpufreq/scaling_governor \n" );
		}
		if (!cpu1max.equals(""))
		{
			cpubuilder.append("chmod 666 /sys/devices/system/cpu/cpu1/cpufreq/scaling_max_freq \n" +
							  "echo " + "\"" + cpu1max + "\"" + " > /sys/devices/system/cpu/cpu1/cpufreq/scaling_max_freq \n"+
							  "chmod 444 /sys/devices/system/cpu/cpu1/cpufreq/scaling_max_freq \n");
		}
		if (!cpu1min.equals(""))
		{
			cpubuilder.append("chmod 666 /sys/devices/system/cpu/cpu1/cpufreq/scaling_min_freq \n" +
							  "echo " + "\"" + cpu1min + "\"" + " > /sys/devices/system/cpu/cpu1/cpufreq/scaling_min_freq \n"+
							  "chmod 444 /sys/devices/system/cpu/cpu1/cpufreq/scaling_min_freq \n\n");
		}
	
		
		List<String> govSettings = IOHelper.govSettings();
		List<String> availableGovs = IOHelper.availableGovs();
	
		for (String s : availableGovs)
		{
			for (String st : govSettings)
			{
				String temp = sharedPrefs.getString(s + "_" + st, "");
	
	 		    if (!temp.equals(""))
				{
	 		    	cpubuilder.append("chmod 777 /sys/devices/system/cpu/cpufreq/" + s + "/" + st + "\n");
	 		    	cpubuilder.append("echo " + "\"" + temp + "\"" + " > /sys/devices/system/cpu/cpufreq/" + s + "/" + st + "\n");
	
	 		    }
			}
		}
		String cpu = cpubuilder.toString();
	
		StringBuilder miscbuilder = new StringBuilder();
	
		miscbuilder.append("#!/system/bin/sh \n\n" + "#mount debug filesystem\n" +
						   "mount -t debugfs debugfs /sys/kernel/debug \n\n");
		if (!vsync.equals(""))
		{
			miscbuilder.append("#vsync\n" +
							   "chmod 777 /sys/kernel/debug/msm_fb/0/vsync_enable \n" +
							   "chmod 777 /sys/kernel/debug/msm_fb/0/hw_vsync_mode \n" +
							   "chmod 777 /sys/kernel/debug/msm_fb/0/backbuff \n" +
							   "echo " + "\"" + vsync + "\"" + " > /sys/kernel/debug/msm_fb/0/vsync_enable \n" +
							   "echo " + "\"" + hw + "\"" + " > /sys/kernel/debug/msm_fb/0/hw_vsync_mode \n" +
							   "echo " + "\"" + backbuff + "\"" + " > /sys/kernel/debug/msm_fb/0/backbuff \n\n");
		}
		if (!led.equals(""))
		{
			miscbuilder.append("#capacitive buttons backlight\n" + "chmod 777 /sys/devices/platform/leds-pm8058/leds/button-backlight/currents \n" +
							   "echo " + "\"" + led + "\"" + " > /sys/devices/platform/leds-pm8058/leds/button-backlight/currents \n\n");
		}
		if (!fastcharge.equals(""))
		{
			miscbuilder.append("#fastcharge\n" + "chmod 777 /sys/kernel/fast_charge/force_fast_charge \n" +
							   "echo " + "\"" + fastcharge + "\"" + " > /sys/kernel/fast_charge/force_fast_charge \n\n");
		}
		if (!cdepth.equals(""))
		{
			miscbuilder.append("#color depth\n" + "chmod 777 /sys/kernel/debug/msm_fb/0/bpp \n" +
							   "echo " + "\"" + cdepth + "\"" + " > /sys/kernel/debug/msm_fb/0/bpp \n\n");
		}
	
		if (!mpdecisionscroff.equals(""))
		{
			miscbuilder.append("chmod 777 /sys/kernel/msm_mpdecision/conf/scroff_single_core \n" +
							   "echo " + "\"" + mpdecisionscroff + "\"" + " > /sys/kernel/msm_mpdecision/conf/scroff_single_core \n");
		}
		
		if (!sdcache.equals(""))
		{
			miscbuilder.append("#sd card cache size\n" +
							   "chmod 777 /sys/block/mmcblk1/queue/read_ahead_kb \n" +
							   "chmod 777 /sys/block/mmcblk0/queue/read_ahead_kb \n" +
							   "chmod 777 /sys/devices/virtual/bdi/179:0/read_ahead_kb \n" +
							   "echo " + "\"" + sdcache + "\"" + " > /sys/block/mmcblk1/queue/read_ahead_kb \n" +
							   "echo " + "\"" + sdcache + "\"" + " > /sys/block/mmcblk0/queue/read_ahead_kb \n" +
							   "echo " + "\"" + sdcache + "\"" + " > /sys/devices/virtual/bdi/179:0/read_ahead_kb \n\n");
		}
		if (!io.equals(""))
		{
			miscbuilder.append("#IO scheduler\n" +
							   "chmod 777 /sys/block/mmcblk0/queue/scheduler \n" +
							   "chmod 777 /sys/block/mmcblk1/queue/scheduler \n" +
							   "echo " + "\"" + io + "\"" + " > /sys/block/mmcblk0/queue/scheduler \n" +
							   "echo " + "\"" + io + "\"" + " > /sys/block/mmcblk1/queue/scheduler \n\n");
		}
		if (!ldt.equals(""))
		{
			miscbuilder.append("#Notification LED Timeout\n" +
							   "chmod 777 /sys/kernel/notification_leds/off_timer_multiplier\n" +
							   "echo " + "\"" + ldt + "\"" + " > /sys/kernel/notification_leds/off_timer_multiplier\n\n");
		}
		if (!s2w.equals(""))
		{
			miscbuilder.append("#Sweep2Wake\n" +
							   "chmod 777 /sys/android_touch/sweep2wake\n" +
							   "echo " + "\"" + s2w + "\"" + " > /sys/android_touch/sweep2wake\n\n");
		}
		if (!s2wStart.equals(""))
		{
			miscbuilder.append("chmod 777 /sys/android_touch/sweep2wake_startbutton\n" +
							   "echo " + s2wStart + " > /sys/android_touch/sweep2wake_startbutton\n" +
							   "chmod 777 /sys/android_touch/sweep2wake_endbutton\n" +
							   "echo " + s2wEnd + " > /sys/android_touch/sweep2wake_endbutton\n\n");
		}
	
		
		
		if (swap == true)
		{
			miscbuilder.append("echo " + swappiness + " > /proc/sys/vm/swappiness\n"
							   + "swapon " + swapLocation.trim() + "\n\n"
							   );
	
	
		}
		else if (swap == false)
		{
			miscbuilder.append("swapoff " + swapLocation.trim() + "\n\n");
	
		}
		if(!oom.equals(""))
		{
			miscbuilder.append("echo " + oom + " > /sys/module/lowmemorykiller/parameters/minfree\n");
	
		}
		if(!otg.equals(""))
		{
			miscbuilder.append("echo " + otg + " > /sys/kernel/debug/msm_otg/mode\n");
			miscbuilder.append("echo " + otg + " > /sys/kernel/debug/otg/mode\n");
		}
		miscbuilder.append("#Umount debug filesystem\n" +
						   "umount /sys/kernel/debug \n");
		String misc = miscbuilder.toString();
	
	
	
		StringBuilder voltagebuilder = new StringBuilder();
		voltagebuilder.append("#!/system/bin/sh \n");
		for (String s : voltages)
		{
			String temp = sharedPrefs.getString("voltage_" + s, "");
		    if (!temp.equals(""))
			{
				voltagebuilder.append("echo " + "\"" + temp + "\"" + " > /sys/devices/system/cpu/cpufreq/vdd_table/vdd_levels\n");
		    }
		}
		String voltage = voltagebuilder.toString();
		try
		{ 
	
			FileOutputStream fOut = openFileOutput("99ktcputweaks",
					MODE_PRIVATE);
			OutputStreamWriter osw = new OutputStreamWriter(fOut); 
			osw.write(cpu);        
			osw.flush();
			osw.close();
	
		}
		catch (IOException ioe)
		{
			}
		
		try
		{ 
	
			FileOutputStream fOut = openFileOutput("99ktmisctweaks",
					MODE_PRIVATE);
			OutputStreamWriter osw = new OutputStreamWriter(fOut); 
			osw.write(misc);        
			osw.flush();
			osw.close();
	
		}
		catch (IOException ioe)
		{
			}
	
		try
		{ 
	
			FileOutputStream fOut = openFileOutput("99ktvoltage",
												   MODE_PRIVATE);
			OutputStreamWriter osw = new OutputStreamWriter(fOut); 
			osw.write(voltage);        
			osw.flush();
			osw.close();
	
		}
		catch (IOException ioe)
		{
			} 
		new Initd().execute(new String[] {"apply"});
	}


	

	/**
	Update UI with current frequency
	*/
	private void cpu0update()
	{

		if(!freqcpu0.equals("offline") && freqcpu0.length()!=0){
		cpu0prog.setText(freqcpu0.trim().substring(0, freqcpu0.length()-3)+"MHz");
		}
		else{
			cpu0prog.setText("offline");
		}
			if (freqlist != null)
		{
		
			cpu0progbar.setMax(freqlist.indexOf(cpu0max.trim()) + 1);
			cpu0progbar.setProgress(freqlist.indexOf(freqcpu0.trim()) + 1);
		}
	}


	private void cpu1update()
	{

		if(!freqcpu1.equals("offline") && freqcpu1.length()!=0){
			cpu1prog.setText(freqcpu1.trim().substring(0, freqcpu1.length()-3)+"MHz");
			}
			else{
				cpu1prog.setText("offline");
			}
		if (freqlist != null)
		{

			cpu1progbar.setMax(freqlist.indexOf(cpu1max.trim()) + 1);
			cpu1progbar.setProgress(freqlist.indexOf(freqcpu1.trim()) + 1);
		}
	}

	



	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{

		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.options, menu);

		return true;
	}
	



	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{

		if (item.getItemId() == R.id.settings)
		{
			startActivity(new Intent(this, Preferences.class));

		}
		
		

		return super.onOptionsItemSelected(item);
	}

	
	private void CopyAssets() {
		AssetManager assetManager = getAssets();
		String[] files = null;
		File file;
		try {
			files = assetManager.list("");
		} catch (IOException e) {
			Log.e("tag", e.getMessage());
		}
		for (String filename : files) {
			InputStream in = null;
			OutputStream out = null;
			try {
				in = assetManager.open(filename);
				file = new File(this.getFilesDir().getAbsolutePath() + "/"
						+ filename);
				out = new FileOutputStream(file);
				copyFile(in, out);
				in.close();
				Runtime.getRuntime().exec("chmod 755 " + file);
				
				in = null;
				out.flush();
				out.close();
				out = null;
			} catch (Exception e) {
				Log.e("tag", e.getMessage());
			}
		}
		
		editor.putBoolean("first_launch", true);
		editor.commit();
	}

	private void copyFile(InputStream in, OutputStream out) throws IOException {
		byte[] buffer = new byte[1024];
		int read;
		while ((read = in.read(buffer)) != -1) {
			out.write(buffer, 0, read);
		}
	}

	
	
	
	
	
	
}
