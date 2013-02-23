/*
* This file is part of the Kernel Tuner.
*
* Copyright Predrag Čokulov <predragcokulov@gmail.com>
*
* Kernel Tuner is free software: you can redistribute it and/or modify
* it under the terms of the GNU General Public License as published by
* the Free Software Foundation, either version 3 of the License, or
* (at your option) any later version.
*
* Kernel Tuner is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
* GNU General Public License for more details.
*
* You should have received a copy of the GNU General Public License
* along with Kernel Tuner. If not, see <http://www.gnu.org/licenses/>.
*/
package rs.pedjaapps.KernelTuner.compatibility.helpers;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.apache.commons.io.FileUtils;

import rs.pedjaapps.KernelTuner.compatibility.entry.TimesEntry;


import android.os.SystemClock;
import android.util.Log;

public class IOHelper
{

	public static String cpu0online = "/sys/devices/system/cpu/cpu0/online"; 
	public static String cpu1online = "/sys/devices/system/cpu/cpu1/online"; 
	public static String cpu2online = "/sys/devices/system/cpu/cpu2/online"; 
	public static String cpu3online = "/sys/devices/system/cpu/cpu3/online"; 


	public static String CPU0_FREQS = "/sys/devices/system/cpu/cpu0/cpufreq/scaling_available_frequencies";
	public static String SWAPS = "/proc/swaps";
	
	public static String CPU0_CURR_FREQ = "/sys/devices/system/cpu/cpu0/cpufreq/scaling_cur_freq";
	public static String CPU1_CURR_FREQ = "/sys/devices/system/cpu/cpu1/cpufreq/scaling_cur_freq";
	public static String CPU2_CURR_FREQ = "/sys/devices/system/cpu/cpu2/cpufreq/scaling_cur_freq";
	public static String CPU3_CURR_FREQ = "/sys/devices/system/cpu/cpu3/cpufreq/scaling_cur_freq";

	public static String CPU0_MAX_FREQ = "/sys/devices/system/cpu/cpu0/cpufreq/scaling_max_freq";
	public static String CPU1_MAX_FREQ = "/sys/devices/system/cpu/cpu1/cpufreq/scaling_max_freq";
	public static String CPU2_MAX_FREQ = "/sys/devices/system/cpu/cpu2/cpufreq/scaling_max_freq";
	public static String CPU3_MAX_FREQ = "/sys/devices/system/cpu/cpu3/cpufreq/scaling_max_freq";

	public static String CPU0_MIN_FREQ = "/sys/devices/system/cpu/cpu0/cpufreq/scaling_min_freq";
	public static String CPU1_MIN_FREQ = "/sys/devices/system/cpu/cpu1/cpufreq/scaling_min_freq";
	public static String CPU2_MIN_FREQ = "/sys/devices/system/cpu/cpu2/cpufreq/scaling_min_freq";
	public static String CPU3_MIN_FREQ = "/sys/devices/system/cpu/cpu3/cpufreq/scaling_min_freq";

	public static String CPU0_CURR_GOV = "/sys/devices/system/cpu/cpu0/cpufreq/scaling_governor";
	public static String CPU1_CURR_GOV = "/sys/devices/system/cpu/cpu1/cpufreq/scaling_governor";
	public static String CPU2_CURR_GOV = "/sys/devices/system/cpu/cpu2/cpufreq/scaling_governor";
	public static String CPU3_CURR_GOV = "/sys/devices/system/cpu/cpu3/cpufreq/scaling_governor";

	public static String CPU0_GOVS = "/sys/devices/system/cpu/cpu0/cpufreq/scaling_available_governors";
	public static String CPU1_GOVS = "/sys/devices/system/cpu/cpu1/cpufreq/scaling_available_governors";
	public static String CPU2_GOVS = "/sys/devices/system/cpu/cpu2/cpufreq/scaling_available_governors";
	public static String CPU3_GOVS = "/sys/devices/system/cpu/cpu3/cpufreq/scaling_available_governors";
	public static String TIMES_IN_STATE_CPU0 = "/sys/devices/system/cpu/cpu0/cpufreq/stats/time_in_state";
	public static String TIMES_IN_STATE_CPU1 = "/sys/devices/system/cpu/cpu1/cpufreq/stats/time_in_state";
	public static String TIMES_IN_STATE_CPU2 = "/sys/devices/system/cpu/cpu2/cpufreq/stats/time_in_state";
	public static String TIMES_IN_STATE_CPU3 = "/sys/devices/system/cpu/cpu3/cpufreq/stats/time_in_state";

	public static String VOLTAGE_PATH = "/sys/devices/system/cpu/cpufreq/vdd_table/vdd_levels";
	public static String VOLTAGE_PATH_TEGRA_3 = "/sys/devices/system/cpu/cpu0/cpufreq/UV_mV_table";
	public static String GPU = "/sys/devices/platform/kgsl-3d0.0/kgsl/kgsl-3d0/max_gpuclk";
	public static String CDEPTH = "/sys/kernel/debug/msm_fb/0/bpp";
	public static String S2W = "/sys/android_touch/sweep2wake";
	public static String S2W_ALT = "/sys/android_touch/sweep2wake/s2w_switch";
	public static String MPDECISION = "/sys/kernel/msm_mpdecision/conf/enabled";
	public static String BUTTONS_LIGHT = "/sys/devices/platform/leds-pm8058/leds/button-backlight/currents";
	public static String BUTTONS_LIGHT_2 = "/sys/devices/platform/msm_ssbi.0/pm8921-core/pm8xxx-led/leds/button-backlight/currents";
	public static String SD_CACHE = "/sys/devices/virtual/bdi/179:0/read_ahead_kb";
	public static String VSYNC = "/sys/kernel/debug/msm_fb/0/vsync_enable";
	public static String FCHARGE = "/sys/kernel/fast_charge/force_fast_charge";
	public static String OOM = "/sys/module/lowmemorykiller/parameters/minfree";
	public static String THERMALD = "/sys/kernel/msm_thermal/conf/allowed_low_freq";
	public static String SCHEDULER = "/sys/block/mmcblk0/queue/scheduler";
	public static String OTG = "/sys/kernel/debug/msm_otg/mode";
	public static String OTG_2= "/sys/kernel/debug/otg/mode";
	public static boolean freqsExists()
	{
		boolean i = false;
		if (new File(CPU0_FREQS).exists())
		{
			i = true;
		}
		return i;

	}
	
	public static boolean oomExists()
	{
		boolean i = false;
		if (new File(OOM).exists())
		{
			i = true;
		}
		return i;

	}
	
	public static boolean thermaldExists()
	{
		boolean i = false;
		if (new File(THERMALD).exists())
		{
			i = true;
		}
		return i;

	}
	
	public static boolean swapsExists()
	{
		boolean i = false;
		if (new File(SWAPS).exists())
		{
			i = true;
		}
		return i;

	}
	
	public static boolean cpu0Online()
	{
		boolean i = false;
		if (new File(cpu0online).exists())
		{
			i = true;
		}
		return i;

	}

	public static boolean cpu1Online()
	{
		boolean i = false;
		if (new File(cpu1online).exists())
		{
			i = true;
		}
		return i;

	}

	public static boolean cpu2Online()
	{
		boolean i = false;
		if (new File(cpu2online).exists())
		{
			i = true;
		}
		return i;

	}

	public static boolean cpu3Online()
	{
		boolean i = false;
		if (new File(cpu3online).exists())
		{
			i = true;
		}
		return i;

	}
	
	public static boolean gpuExists()
	{
		boolean i = false;
		if (new File(GPU).exists())
		{
			i = true;
		}
		return i;

	}
	
	public static boolean cdExists()
	{
		boolean i = false;
		if (new File(CDEPTH).exists())
		{
			i = true;
		}
		return i;

	}

	public static boolean voltageExists()
	{
		boolean i = false;
		if (new File(VOLTAGE_PATH).exists())
		{
			i = true;
		}
		else if (new File(VOLTAGE_PATH_TEGRA_3).exists())
		{
			i = true;
		}
		return i;

	}
	
	public static boolean otgExists()
	{
		boolean i = false;
		if (new File(OTG).exists())
		{
			i = true;
		}
		else if (new File(OTG_2).exists())
		{
			i = true;
		}
		return i;

	}

	public static boolean s2wExists()
	{
		boolean i = false;
		if (new File(S2W).exists())
		{
			i = true;
		}
		else if (new File(S2W_ALT).exists())
		{
			i = true;
		}
		return i;

	}
	
	public static boolean TISExists()
	{
		boolean i = false;
		if (new File(TIMES_IN_STATE_CPU0).exists())
		{
			i = true;
		}
		return i;

	}
	
	public static boolean mpdecisionExists()
	{
		boolean i = false;
		if (new File(MPDECISION).exists())
		{
			i = true;
		}
		return i;

	}
	
	public static boolean buttonsExists()
	{
		boolean i = false;
		if (new File(BUTTONS_LIGHT).exists())
		{
			i = true;
		}
		else if (new File(BUTTONS_LIGHT_2).exists())
		{
			i = true;
		}
		return i;

	}
	
	public static boolean sdcacheExists()
	{
		boolean i = false;
		if (new File(SD_CACHE).exists())
		{
			i = true;
		}
		return i;

	}
	
	public static boolean vsyncExists()
	{
		boolean i = false;
		if (new File(VSYNC).exists())
		{
			i = true;
		}
		return i;

	}
	
	public static boolean fchargeExists()
	{
		boolean i = false;
		if (new File(FCHARGE).exists())
		{
			i = true;
		}
		return i;

	}

	public static List<FreqsEntry> frequencies()
	{
		
		List<FreqsEntry> entries = new ArrayList<FreqsEntry>();
		List<String> frequencies = new ArrayList<String>();
		try
		{

			File myFile = new File(CPU0_FREQS);
			FileInputStream fIn = new FileInputStream(myFile);

			BufferedReader myReader = new BufferedReader(
				new InputStreamReader(fIn));
			String aDataRow = "";
			String aBuffer = "";
			while ((aDataRow = myReader.readLine()) != null)
			{
				aBuffer += aDataRow + "\n";
			}
			frequencies = Arrays.asList(aBuffer.split("\\s"));
			for(String s: frequencies){
				entries.add(new FreqsEntry(s.trim().substring(0, s.trim().length()-3)+"MHz", Integer.parseInt(s.trim())));
 				
			}
			myReader.close();
			fIn.close();
		}
		catch (Exception e)
		{
			try
			{

	 			FileInputStream fstream = new FileInputStream(TIMES_IN_STATE_CPU0);

	 			DataInputStream in = new DataInputStream(fstream);
	 			BufferedReader br = new BufferedReader(new InputStreamReader(in));
	 			String strLine;


	 			while ((strLine = br.readLine()) != null)
				{

	 				String[] delims = strLine.split(" ");
	 				String freq = delims[0];
	 				//frequencies.add(freq);
	 				entries.add(new FreqsEntry(freq.trim().substring(0, freq.trim().length()-3)+"MHz", Integer.parseInt(freq.trim())));
	 				

	 			}

	 			
	 				Collections.sort(entries,new MyComparator());
	 			


	 			in.close();
	 			fstream.close();
	 			br.close();
			}
			catch (Exception ee)
			{
				entries.add(new FreqsEntry("", 0));
 				
			}
		}
		return entries;

	}
	
	public static final List<String> oom()
	{
		try
		{
			return Arrays.asList(FileUtils.readFileToString(new File(OOM)).split(","));
		}
		catch (Exception e)
		{
			return new ArrayList<String>();
		}

	}
	
	public static final String leds()
	{
		try
		{
			return FileUtils.readFileToString(new File(BUTTONS_LIGHT_2)).trim();	
		}
		catch (Exception e)
		{
			try
			{
				return FileUtils.readFileToString(new File(BUTTONS_LIGHT)).trim();
			}
			catch (Exception ee)
			{
				return "";
			}
		}
		
	}

	public static final List<String> governors()
	{
		try
		{
			return Arrays.asList(FileUtils.readFileToString(new File(CPU0_GOVS)).split("\\s"));
		}
		catch (Exception e)
		{
			return new ArrayList<String>();
		}

	}

	public static final String cpu0MinFreq()
	{
		try
		{
			return FileUtils.readFileToString(new File(CPU0_MIN_FREQ)).trim();
		}
		catch (Exception e)
		{
			return "offline";
		}
		
	}
	
	

	public static final String cpu0MaxFreq()
	{
		try
		{
			return FileUtils.readFileToString(new File(CPU0_MAX_FREQ)).trim();
		}
		catch (Exception e)
		{
			return "offline";
		}

	}

	public static final String cpu1MinFreq()
	{
		try
		{
			return FileUtils.readFileToString(new File(CPU1_MIN_FREQ)).trim();
		}
		catch (Exception e)
		{
			return "offline";
		}
	}

	public static final String cpu1MaxFreq()
	{
		try
		{
			return FileUtils.readFileToString(new File(CPU1_MAX_FREQ)).trim();
		}
		catch (Exception e)
		{
			return "offline";
		}
	}

	public static final String cpu2MinFreq()
	{
		try
		{
			return FileUtils.readFileToString(new File(CPU2_MIN_FREQ)).trim();
		}
		catch (Exception e)
		{
			return "offline";
		}

	}

	public static final String cpu2MaxFreq()
	{
		try
		{
			return FileUtils.readFileToString(new File(CPU2_MAX_FREQ)).trim();
		}
		catch (Exception e)
		{
			return "offline";
		}
	}

	public static final String cpu3MinFreq()
	{
		try
		{
			return FileUtils.readFileToString(new File(CPU3_MIN_FREQ)).trim();
		}
		catch (Exception e)
		{
			return "offline";
		}

	}

	public static final String cpu3MaxFreq()
	{
		try
		{
			return FileUtils.readFileToString(new File(CPU3_MAX_FREQ)).trim();
		}
		catch (Exception e)
		{
			return "offline";
		}

	}

	public static final String cpu0CurFreq()
	{
		try
		{
			return FileUtils.readFileToString(new File(CPU0_CURR_FREQ)).trim();
		}
		catch (Exception e)
		{
			return "offline";
		}
	}

	public static final String cpu1CurFreq()
	{
		try
		{
			return FileUtils.readFileToString(new File(CPU1_CURR_FREQ)).trim();
		}
		catch (Exception e)
		{
			return "offline";
		}
	}

	public static final String cpu2CurFreq()
	{
		try
		{
			return FileUtils.readFileToString(new File(CPU2_CURR_FREQ)).trim();
		}
		catch (Exception e)
		{
			return "offline";
		}

	}

	public static final String cpu3CurFreq()
	{
		try
		{
			return FileUtils.readFileToString(new File(CPU3_CURR_FREQ)).trim();
		}
		catch (Exception e)
		{
			return "offline";
		}

	}

	public static final String cpu0CurGov()
	{
		try
		{
			return FileUtils.readFileToString(new File(CPU0_CURR_GOV)).trim();
		}
		catch (Exception e)
		{
			return "offline";
		}

	}

	public static final String cpu1CurGov()
	{
		try
		{
			return FileUtils.readFileToString(new File(CPU1_CURR_GOV)).trim();
		}
		catch (Exception e)
		{
			return "offline";
		}

	}

	public static final String cpu2CurGov()
	{
		try
		{
			return FileUtils.readFileToString(new File(CPU2_CURR_GOV)).trim();
		}
		catch (Exception e)
		{
			return "offline";
		}

	}

	public static final String cpu3CurGov()
	{
		try
		{
			return FileUtils.readFileToString(new File(CPU3_CURR_GOV)).trim();
		}
		catch (Exception e)
		{
			return "offline";
		}

	}


	public static  List<TimesEntry> getTis()
	{
		List<TimesEntry> times = new ArrayList<TimesEntry>();

		try
		{

			FileInputStream fstream = new FileInputStream(TIMES_IN_STATE_CPU0);

			DataInputStream in = new DataInputStream(fstream);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			String strLine;

			while ((strLine = br.readLine()) != null)
			{	
				String[] delims = strLine.split(" ");
				times.add(new TimesEntry(Integer.parseInt(delims[0]), Long.parseLong(delims[1])));
				System.out.println(strLine);
			}

			in.close();
			fstream.close();
			br.close();
		}
		catch (Exception e)
		{
			Log.e("Error: " , e.getMessage());
		}
		
		return times;

	}

	

	public static List<VoltageList> voltages()
	{
		List<VoltageList> voltages = new ArrayList<VoltageList>();
		if(voltages.isEmpty()==false){
			voltages.clear();
		}
		try
		{

			FileInputStream fstream = new FileInputStream(VOLTAGE_PATH);

			DataInputStream in = new DataInputStream(fstream);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			String strLine;

			while ((strLine = br.readLine()) != null)
			{

				voltages.add(new VoltageList(strLine.substring(0,strLine.length() - 10).trim(),
											 strLine.substring(0,strLine.length() - 13).trim()+"MHz", 
											 Integer.parseInt(strLine.substring(9,strLine.length() - 0).trim())));
			}

			in.close();
			fstream.close();
			br.close();
		}
		catch (Exception e)
		{
			try
			{

				FileInputStream fstream = new FileInputStream(VOLTAGE_PATH_TEGRA_3);

				DataInputStream in = new DataInputStream(fstream);
				BufferedReader br = new BufferedReader(new InputStreamReader(in));
				String strLine;

				while ((strLine = br.readLine()) != null)
				{	
					String[] delims = strLine.split(" ");
					//voltages.add(Integer.parseInt(delims[1]));
					voltages.add(new VoltageList(delims[0],
							 					 delims[0].substring(0,delims[0].length() - 4).trim()+"MHz", 
							 					 Integer.parseInt(delims[1])));

				}

				in.close();
				fstream.close();
				br.close();
			}
			catch (Exception ex)
			{

			}
		}
		System.out.println(voltages);
		return voltages;

	}

	

	public static String uptime()
	{
		String uptime;

		int time =(int) SystemClock.elapsedRealtime();


		String s = String.valueOf((int)((time / 1000) % 60));
		String m = String.valueOf((int)((time / (1000 * 60)) % 60));
		String h = String.valueOf((int)((time / (1000 * 3600)) % 24));
		String d = String.valueOf((int)(time / (1000 * 60 * 60 * 24)));
		StringBuilder builder = new StringBuilder();
		if (!d.equals("0"))
		{
			builder.append(d + "d:");

		}
		if (!h.equals("0"))
		{
			builder.append(h + "h:");

		}
		if (!m.equals("0"))
		{
			builder.append(m + "m:");

		}

		builder.append(s + "s");


		uptime = builder.toString();

		return uptime;


	}
	public static String deepSleep()
	{
		String deepSleep;

		int time =(int) (SystemClock.elapsedRealtime() - SystemClock.uptimeMillis());

		String s = String.valueOf((int)((time / 1000) % 60));
	    String m = String.valueOf((int)((time / (1000 * 60)) % 60));
		String h = String.valueOf((int)((time / (1000 * 3600)) % 24));
		String d = String.valueOf((int)(time / (1000 * 60 * 60 * 24)));
		StringBuilder builder = new StringBuilder();
		if (!d.equals("0"))
		{
			builder.append(d + "d:");

		}
		if (!h.equals("0"))
		{
			builder.append(h + "h:");

		}
		if (!m.equals("0"))
		{
			builder.append(m + "m:");

		}

		builder.append(s + "s");


		deepSleep = builder.toString();

		return deepSleep;
	}

	public static final String cpuTemp()
	{
		try
		{
			return FileUtils.readFileToString(new File("/sys/class/thermal/thermal_zone1/temp")).trim();
		}
		catch (Exception e2)
		{
			return "0";
		}
	}

	public static final String cpuInfo()
	{
		try
		{
			return FileUtils.readFileToString(new File("/proc/cpuinfo")).trim();
		}
		catch (Exception e2)
		{
			return "";
		}
	}

	public static List<String> availableGovs()
	{
		File govs = new File("/sys/devices/system/cpu/cpufreq/");
		List<String> availableGovs = new ArrayList<String>();


		if (govs.exists())
		{
			File[] files = govs.listFiles();

			for (File file : files)
			{
				availableGovs.add(file.getName());


			}
		}

		availableGovs.removeAll(Arrays.asList("vdd_table"));
		return availableGovs;

	}

	public static List<String> govSettings()
	{

		List<String> govSettings = new ArrayList<String>();

		for (String s : availableGovs())
		{
			File gov = new File("/sys/devices/system/cpu/cpufreq/" + s + "/");

			if (gov.exists())
			{
				File[] files = gov.listFiles();
				if(files!=null){
				for (File file : files)
				{

					govSettings.add(file.getName());

				}
				}
			}}
		return govSettings;
		}

	

	public static List<String> schedulers()
	{

		List<String> schedulers = new ArrayList<String>();

		try
		{

			File myFile = new File(SCHEDULER);
			FileInputStream fIn = new FileInputStream(myFile);

			BufferedReader myReader = new BufferedReader(new InputStreamReader(
															 fIn));
			String aDataRow = "";
			String aBuffer = "";
			while ((aDataRow = myReader.readLine()) != null)
			{
				aBuffer += aDataRow + "\n";
			}

			String schedulersTemp = aBuffer;
			myReader.close();
			fIn.close();
			
			schedulersTemp = schedulersTemp.replace("[", "");
			schedulersTemp = schedulersTemp.replace("]", "");
			String[] temp = schedulersTemp.split("\\s");
			for(String s : temp){
			schedulers.add(s);
			}
		}
		catch (Exception e)
		{

		}
		
		return schedulers;
		}
	
	
	public static final int fcharge(){
		try
		{
			return Integer.parseInt(FileUtils.readFileToString(new File(FCHARGE)).trim());
		}
		catch (Exception e)
		{
			return 0;
		}
	}
	
	public static final int vsync(){
		try
		{
			return Integer.parseInt(FileUtils.readFileToString(new File(VSYNC)).trim());
		}
		catch (Exception e)
		{
			return 0;
		}
	}
	
	public static final String cDepth(){
		try
		{
			return FileUtils.readFileToString(new File(CDEPTH)).trim();
		}
		catch (Exception e)
		{
			return "";
		}
	}
	
	public static String scheduler(){
		String scheduler = "";
		try
		{

			File myFile = new File(SCHEDULER);
			FileInputStream fIn = new FileInputStream(myFile);

			BufferedReader myReader = new BufferedReader(
					new InputStreamReader(fIn));
			String aDataRow = "";
			String aBuffer = "";
			while ((aDataRow = myReader.readLine()) != null)
			{
				aBuffer += aDataRow + "\n";
			}

			String schedulers = aBuffer;
			myReader.close();

			scheduler = schedulers.substring(schedulers.indexOf("[") + 1, schedulers.indexOf("]"));
			scheduler.trim();
			fIn.close();

		}
		catch (Exception e)
		{
			
		}
		return scheduler;
	}
	
	public static final int sdCache(){
		try
		{
			return Integer.parseInt(FileUtils.readFileToString(new File(SD_CACHE)).trim());
		}
		catch (Exception e)
		{
			return 0;
		}
	}
	
	public static final int s2w()
	{
		try
		{
			return Integer.parseInt(FileUtils.readFileToString(new File("/sys/android_touch/sweep2wake")).trim());
		}
		catch (Exception e)
		{
			try
			{
				return Integer.parseInt(FileUtils.readFileToString(new File("/sys/android_touch/sweep2wake/s2w_switch")).trim());
			}
			catch(Exception e2)
			{
				return 0;
			}
		}
	}
	
	public static final String readOTG(){
		try
		{
			return FileUtils.readFileToString(new File(OTG)).trim();
		}
		catch (Exception e)
		{
			try
			{
				return FileUtils.readFileToString(new File(OTG_2)).trim();
			}
			catch(Exception e2)
			{
				return "";
			}
		}
	}
	
	public static final class FreqsEntry
	{

		private final String freqName;
		private final int freq;
		


		public FreqsEntry(final String freqName, 
				final int freq)
		{
			this.freqName = freqName;
			this.freq = freq;
		}


		public String getFreqName()
		{
			return freqName;
		}


		public int getFreq(){
			return freq;
		}

	}
	
	public static final class VoltageList
	{

		private final String freq;
		private final String freqName;
		private final int voltage;
		


		public VoltageList(final String freq, final String freqName,
				final int voltage)
		{
			this.freq = freq;
			this.freqName = freqName;
			this.voltage = voltage;
		}


		public String getFreq()
		{
			return freq;
		}
		
		public String getFreqName()
		{
			return freqName;
		}


		public int getVoltage(){
			return voltage;
		}

	}
	
	public static String kernel(){
		String kernel = "";
		try {

			File myFile = new File("/proc/version");
			FileInputStream fIn = new FileInputStream(myFile);
			BufferedReader myReader = new BufferedReader(
					new InputStreamReader(fIn));
			String aDataRow = "";
			String aBuffer = "";
			while ((aDataRow = myReader.readLine()) != null) {
				aBuffer += aDataRow + "\n";
			}

			kernel = aBuffer.trim();
			myReader.close();

		} catch (Exception e) {

		}
		return kernel;
	}

	static class MyComparator implements Comparator<FreqsEntry>{
		  public int compare(FreqsEntry ob1, FreqsEntry ob2){
		   return ob1.getFreq() - ob2.getFreq() ;
		  }
		}
	
}
