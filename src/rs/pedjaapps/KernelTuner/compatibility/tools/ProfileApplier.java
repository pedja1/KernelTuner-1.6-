package rs.pedjaapps.KernelTuner.compatibility.tools;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import rs.pedjaapps.KernelTuner.compatibility.R;
import rs.pedjaapps.KernelTuner.compatibility.entry.Profile;
import rs.pedjaapps.KernelTuner.compatibility.entry.Voltage;
import rs.pedjaapps.KernelTuner.compatibility.helpers.DatabaseHandler;
import rs.pedjaapps.KernelTuner.compatibility.helpers.IOHelper;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;


public class ProfileApplier extends AsyncTask<String, Void, String>
{

	Context context;
	DatabaseHandler db;
	Profile profile;

	public ProfileApplier(Context context)
	{
		this.context = context;
		preferences = PreferenceManager.getDefaultSharedPreferences(context);
		db = new DatabaseHandler(context);
	}

	

	SharedPreferences preferences;
	
	@Override
	protected String doInBackground(String... args)
	{

		profile = db.getProfileByName(args[0]);
		String cpu0min = profile.getCpu0min();
		String cpu0max = profile.getCpu0max();
		String cpu1min = profile.getCpu1min();
		String cpu1max = profile.getCpu1max();
		
		 String cpu0gov = profile.getCpu0gov();
		 String cpu1gov = profile.getCpu1gov();
		 
		 String voltage = profile.getVoltage();

		 String buttons = profile.getButtonsLight();
		 int vsync = profile.getVsync();
		 int fcharge = profile.getFcharge();
		 String cdepth = profile.getCdepth();
		 String io = profile.getIoScheduler();
		 Integer sdcache = profile.getSdcache();

		 Integer s2w = profile.getSweep2wake();
		 List<IOHelper.VoltageList> voltageList = IOHelper.voltages();
			
			List<String> voltageFreqs =  new ArrayList<String>();
			
			for(IOHelper.VoltageList v: voltageList){
				voltageFreqs.add((v.getFreq()));
			}
		
			try {
	            String line;
	            Process process = Runtime.getRuntime().exec("su");
	            OutputStream stdin = process.getOutputStream();
	            InputStream stderr = process.getErrorStream();
	            InputStream stdout = process.getInputStream();

	            if (IOHelper.cpu1Online() == true)
				{
					stdin.write(("echo 0 > /sys/kernel/msm_mpdecision/conf/enabled\n").getBytes());
					stdin.write(("chmod 666 /sys/devices/system/cpu/cpu1/online\n").getBytes());
					stdin.write(("echo 1 > /sys/devices/system/cpu/cpu1/online\n").getBytes());
					stdin.write(("chmod 444 /sys/devices/system/cpu/cpu1/online\n").getBytes());
					stdin.write(("chown system /sys/devices/system/cpu/cpu1/online\n").getBytes());
				}
				

				stdin.write(("mount -t debugfs debugfs /sys/kernel/debug\n").getBytes());
		
		//cpu0 min
		if( cpu0min != null && !cpu0min.equals("Unchanged") && !cpu0min.equals("") ){
			stdin.write(("chmod 777 /sys/devices/system/cpu/cpu0/cpufreq/scaling_min_freq\n").getBytes());
			stdin.write(("echo " + cpu0min + " > /sys/devices/system/cpu/cpu0/cpufreq/scaling_min_freq\n").getBytes());
			SharedPreferences.Editor editor = preferences.edit();
			editor.putString("cpu0min", cpu0min);
			editor.commit();
		}
		
		//cpu0 max
		if( cpu0max != null && !cpu0max.equals("Unchanged") && !cpu0max.equals("") ){
			stdin.write(("chmod 777 /sys/devices/system/cpu/cpu0/cpufreq/scaling_max_freq\n").getBytes());
			stdin.write(("echo " + cpu0max + " > /sys/devices/system/cpu/cpu0/cpufreq/scaling_max_freq\n").getBytes());
			SharedPreferences.Editor editor = preferences.edit();
			editor.putString("cpu0max", cpu0max);
			editor.commit();
		}
		
		//cpu1min
		if( cpu1min != null && !cpu1min.equals("Unchanged") && !cpu1min.equals("") ){
			stdin.write(("chmod 777 /sys/devices/system/cpu/cpu1/cpufreq/scaling_min_freq\n").getBytes());
			stdin.write(("echo " + cpu1min + " > /sys/devices/system/cpu/cpu1/cpufreq/scaling_min_freq\n").getBytes());
			SharedPreferences.Editor editor = preferences.edit();
			editor.putString("cpu1min", cpu1min);
			editor.commit();
		}
		//cpu1max
		if( cpu1max != null && !cpu1max.equals("Unchanged") && !cpu1max.equals("") ){
			stdin.write(("chmod 777 /sys/devices/system/cpu/cpu1/cpufreq/scaling_max_freq\n").getBytes());
			stdin.write(("echo " + cpu1max + " > /sys/devices/system/cpu/cpu1/cpufreq/scaling_max_freq\n").getBytes());
			SharedPreferences.Editor editor = preferences.edit();
			editor.putString("cpu1max", cpu1max);
			editor.commit();
		}

		
		//cpu0governor
		if( cpu0gov != null && !cpu0gov.equals("Unchanged") && !cpu0gov.equals("") ){
			stdin.write(("echo " + cpu0gov + " > /sys/devices/system/cpu/cpu0/cpufreq/scaling_governor\n").getBytes());
			SharedPreferences.Editor editor = preferences.edit();
			editor.putString("cpu0gov", cpu0gov);
			editor.commit();
		}
		
		//cpu1governor
		if( cpu1gov != null && !cpu1gov.equals("Unchanged") && !cpu1gov.equals("") ){
			stdin.write(("echo " + cpu1gov + " > /sys/devices/system/cpu/cpu1/cpufreq/scaling_governor\n").getBytes());
			SharedPreferences.Editor editor = preferences.edit();
			editor.putString("cpu1gov", cpu1gov);
			editor.commit();
		}
		
		
		
		//voltage
		if( voltage != null && !voltage.equals("Unchanged") && !voltage.equals("") ){
			Voltage volt = db.getVoltageByName(profile.getVoltage());
			String[] values = volt.getValue().split("\\s");
			
		
		stdin.write(("chmod 777 /sys/devices/system/cpu/cpufreq/vdd_table/vdd_levels\n").getBytes());

		for (int i = 0; i < voltageFreqs.size(); i++)
		{
			//int volt = voltages.get(i) + 12500;
			
				stdin.write(("echo "
								+ voltageFreqs.get(i)
								+ " "
								+ values[i]
								+ " > /sys/devices/system/cpu/cpufreq/vdd_table/vdd_levels\n").getBytes());
				
				SharedPreferences.Editor editor = preferences.edit();
				editor.putString("voltage_" + voltageFreqs.get(i), voltageFreqs.get(i) + " " + values[i]);
				editor.commit();
		}
		}
		
		
		//buttons
		if( buttons != null && !buttons.equals("") ){
			stdin.write(("chmod 777 /sys/devices/platform/leds-pm8058/leds/button-backlight/currents\n").getBytes());
		stdin.write(("echo "+ buttons + " > /sys/devices/platform/leds-pm8058/leds/button-backlight/currents\n").getBytes());
		SharedPreferences.Editor editor = preferences.edit();
  	    editor.putString("led", buttons);
  	    editor.commit();
		}
		
		//vsync
		if( vsync==0 ){
			stdin.write(("chmod 777 /sys/kernel/debug/msm_fb/0/vsync_enable\n").getBytes());
			stdin.write(("chmod 777 /sys/kernel/debug/msm_fb/0/hw_vsync_mode\n").getBytes());
			stdin.write(("chmod 777 /sys/kernel/debug/msm_fb/0/backbuff\n").getBytes());
			stdin.write(("echo " + 0 + " > /sys/kernel/debug/msm_fb/0/vsync_enable\n").getBytes());
			stdin.write(("echo " + 0 + " > /sys/kernel/debug/msm_fb/0/hw_vsync_mode\n").getBytes());
			stdin.write(("echo " + 4 + " > /sys/kernel/debug/msm_fb/0/backbuff\n").getBytes());
			SharedPreferences.Editor editor = preferences.edit();
			editor.putString("vsync", "0");
			editor.putString("hw", "0");
			editor.putString("backbuf", "0");
			editor.commit();
		}
		else if(vsync==1){
			stdin.write(("chmod 777 /sys/kernel/debug/msm_fb/0/vsync_enable\n").getBytes());
			stdin.write(("chmod 777 /sys/kernel/debug/msm_fb/0/hw_vsync_mode\n").getBytes());
			stdin.write(("chmod 777 /sys/kernel/debug/msm_fb/0/backbuff\n").getBytes());
			stdin.write(("echo " + 1 + " > /sys/kernel/debug/msm_fb/0/vsync_enable\n").getBytes());
			stdin.write(("echo " + 1 + " > /sys/kernel/debug/msm_fb/0/hw_vsync_mode\n").getBytes());
			stdin.write(("echo " + 3 + " > /sys/kernel/debug/msm_fb/0/backbuff\n").getBytes());
			SharedPreferences.Editor editor = preferences.edit();
			editor.putString("vsync", "1");
			editor.putString("hw", "1");
			editor.putString("backbuf", "3");
			editor.commit();
		}
		
		//fcharge
		stdin.write(("chmod 777 /sys/kernel/fast_charge/force_fast_charge\n").getBytes());
	stdin.write(("echo " + fcharge+ " > /sys/kernel/fast_charge/force_fast_charge\n").getBytes());
	SharedPreferences.Editor editor = preferences.edit();
	    editor.putString("fastcharge", String.valueOf(fcharge));
	    editor.commit();
		
		//cdepth
		if( cdepth != null && !cdepth.equals("Unchanged") && !cdepth.equals("") ){
			stdin.write(("chmod 777 /sys/kernel/debug/msm_fb/0/bpp\n").getBytes());
		stdin.write(("echo " + cdepth + " > /sys/kernel/debug/msm_fb/0/bpp\n").getBytes());
		
  	    editor.putString("cdepth", cdepth);
  	    editor.commit();
		}
		
		//io
		if( io != null && !io.equals("Unchanged") && !io.equals("") ){
			stdin.write(("chmod 777 /sys/block/mmcblk0/queue/scheduler\n").getBytes());
		stdin.write(("chmod 777 /sys/block/mmcblk1/queue/scheduler\n").getBytes());
		stdin.write(("echo " + io + " > /sys/block/mmcblk0/queue/scheduler\n").getBytes());
		stdin.write(("echo " + io+ " > /sys/block/mmcblk1/queue/scheduler\n").getBytes());
		
  	    editor.putString("io", io);
  	    editor.commit();
		}
		
		//sd
		if( sdcache != null && sdcache!=0){
			stdin.write(("chmod 777 /sys/block/mmcblk1/queue/read_ahead_kb\n").getBytes());
		stdin.write(("chmod 777 /sys/block/mmcblk2/queue/read_ahead_kb\n").getBytes());
		stdin.write(("chmod 777 /sys/devices/virtual/bdi/179:0/read_ahead_kb\n").getBytes());
		stdin.write(("echo " + sdcache+ " > /sys/block/mmcblk1/queue/read_ahead_kb\n").getBytes());
		stdin.write(("echo " + sdcache+ " > /sys/block/mmcblk0/queue/read_ahead_kb\n").getBytes());
		stdin.write(("echo " + sdcache+ " > /sys/devices/virtual/bdi/179:0/read_ahead_kb\n").getBytes());
		editor.putString("sdcache", String.valueOf(sdcache));
  	    editor.commit();
		}
		
		//s2w
		if( s2w!=null ){
			stdin.write(("chmod 777 /sys/android_touch/sweep2wake\n").getBytes());
			stdin.write(("echo " + s2w + " > /sys/android_touch/sweep2wake\n").getBytes());
			stdin.write(("chmod 777 /sys/android_touch/sweep2wake/s2w_switch\n").getBytes());
			stdin.write(("echo " + s2w + " > /sys/android_touch/sweep2wake/s2w_switch\n").getBytes());
			editor.putString("s2w", String.valueOf(s2w));
	  	    editor.commit();
		}
		
		if (IOHelper.cpu1Online() == true)
		{
			stdin.write(("echo 1 > /sys/kernel/msm_mpdecision/conf/enabled\n").getBytes());
			stdin.write(("chmod 777 /sys/devices/system/cpu/cpu1/online\n").getBytes());
			stdin.write(("echo 0 > /sys/devices/system/cpu/cpu1/online\n").getBytes());
			stdin.write(("chown system /sys/devices/system/cpu/cpu1/online\n").getBytes());
		}
		if (IOHelper.cpu2Online() == true)
		{
			stdin.write(("chmod 777 /sys/devices/system/cpu/cpu2/online\n").getBytes());
			stdin.write(("echo 0 > /sys/devices/system/cpu/cpu2/online\n").getBytes());
			stdin.write(("chown system /sys/devices/system/cpu/cpu2/online\n").getBytes());
		}
		if (IOHelper.cpu3Online() == true)
		{
			stdin.write(("chmod 777 /sys/devices/system/cpu/cpu3/online\n").getBytes());
			stdin.write(("echo 0 > /sys/devices/system/cpu/cpu3/online\n").getBytes());
			stdin.write(("chown system /sys/devices/system/cpu/cpu3/online\n").getBytes());
		}
	            
	            stdin.flush();

	            stdin.close();
	            BufferedReader brCleanUp =
	                    new BufferedReader(new InputStreamReader(stdout));
	            while ((line = brCleanUp.readLine()) != null) {
	                Log.d("[KernelTuner ChangeGovernor Output]", line);
	            }
	            brCleanUp.close();
	            brCleanUp =
	                    new BufferedReader(new InputStreamReader(stderr));
	            while ((line = brCleanUp.readLine()) != null) {
	            	Log.e("[KernelTuner ChangeGovernor Error]", line);
	            }
	            brCleanUp.close();

	        } catch (IOException ex) {
	        }

		return "";
	}
	@Override
	protected void onPostExecute(String result){
		
		Toast.makeText(context, context.getResources().getString(R.string.profile)+"\""+ profile.getName()+"\""+" " + context.getResources().getString(R.string.applied)  , Toast.LENGTH_LONG).show();
		
	}
}	

