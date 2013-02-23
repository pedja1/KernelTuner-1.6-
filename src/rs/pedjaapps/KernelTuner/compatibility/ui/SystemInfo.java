package rs.pedjaapps.KernelTuner.compatibility.ui;

import android.app.*;
import android.app.ActivityManager.*;
import android.content.*;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.graphics.*;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.*;
import android.preference.*;
import android.util.DisplayMetrics;
import android.view.*;
import android.widget.*;

import java.io.*;
import java.text.*;
import java.util.*;

import rs.pedjaapps.KernelTuner.compatibility.R;
import rs.pedjaapps.KernelTuner.compatibility.helpers.IOHelper;



public class SystemInfo extends Activity  {

	private Integer gpu2d;
	private Integer gpu3d;
	private Integer vsync;
	private Integer fastcharge;
	private Integer cdepth;
	private String kernel;
	private String schedulers;
	private String scheduler;
	private Integer mpdec;
	private Integer s2w;
	private String cpu_info;
	private ProgressDialog pd;

	private static Integer battperc;

	private static Double batttemp;

	private static String battcurrent;

	private SharedPreferences prefs;
	private static String tempPref;
	private List<IOHelper.FreqsEntry> freqEntries;
	private List<String> freqs = new ArrayList<String>();
	private List<IOHelper.VoltageList> voltEntries;
	private List<Integer> voltages = new ArrayList<Integer>();
	private List<String> voltFreq = new ArrayList<String>();
	private String governors;
	private String androidVersion;
	private Integer apiLevel;
	private String cpuAbi;
	private String manufacturer;
	private String hardware;
	private String board;
	private String brand;
	private String device;
	private String display;
	private String fingerprint;
	private String host;
	private String id;
	private String model;
	private String product;
	private String tags;
	private String type;
	private String user;
	private List<PackageInfo> userApps = new ArrayList<PackageInfo>();
	private List<PackageInfo> systemApps = new ArrayList<PackageInfo>();
	private Integer numberOfInstalledApps;
	private Integer numberOfSystemApps;
	private String screenRezolution;
	private String screenRefreshRate;
	private String screenDensity;
	private String screenPpi;
	TextView oriHead, accHead, magHead, ligHead, proxHead, presHead, tempHead,
			gyroHead, gravHead, humHead, oriAccu, accAccu, magAccu, ligAccu,
			proxAccu, presAccu, tempAccu, gyroAccu, gravAccu, humAccu,
			tv_orientationA, tv_orientationB, tv_orientationC, tv_accelA,
			tv_accelB, tv_accelC, tv_magneticA, tv_magneticB, tv_magneticC,
			tv_lightA, tv_proxA, tv_presA, tv_tempA, tv_gravityA, tv_gravityB,
			tv_gravityC, tv_gyroscopeA, tv_gyroscopeB, tv_gyroscopeC,
			tv_humidity_A;
	ProgressBar pb_orientationA, pb_orientationB, pb_orientationC, pb_accelA,
			pb_accelB, pb_accelC, pb_magneticA, pb_magneticB, pb_magneticC,
			pb_lightA, pb_proxA, pb_presA, pb_tempA, pb_gravityA, pb_gravityB,
			pb_gravityC, pb_gyroscopeA, pb_gyroscopeB, pb_gyroscopeC,
			pb_humidity_A;
	LinearLayout oriLayout, accLayout, magLayout, ligLayout, proxLayout,
			tempLayout, presLayout;
	SensorManager m_sensormgr;
	List<Sensor> m_sensorlist;
	static final int FLOATTOINTPRECISION = 100;

	Boolean isSDPresent;

	private class info extends AsyncTask<String, Void, Object> {

		private boolean isSystemPackage(PackageInfo pkgInfo) {
			return ((pkgInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0) ? true
					: false;
		}

		@Override
		protected Object doInBackground(String... args) {
			isSDPresent = android.os.Environment.getExternalStorageState()
					.equals(android.os.Environment.MEDIA_MOUNTED);
			freqEntries = IOHelper.frequencies();
			voltEntries = IOHelper.voltages();
			for (IOHelper.FreqsEntry f : freqEntries) {
				freqs.add(f.getFreqName());
			}
			for (IOHelper.VoltageList v : voltEntries) {
				voltFreq.add(v.getFreqName());
			}
			for (IOHelper.VoltageList v : voltEntries) {
				voltages.add(v.getVoltage());
			}

			List<String> govs = IOHelper.governors();
			StringBuilder builder = new StringBuilder();
			for (String s : govs) {
				builder.append(s + ", ");
			}
			governors = builder.toString();
			androidVersion = Build.VERSION.RELEASE;
			apiLevel = Build.VERSION.SDK_INT;
			cpuAbi = android.os.Build.CPU_ABI;
			manufacturer = android.os.Build.MANUFACTURER;
			
			
			board = android.os.Build.BOARD;
			brand = android.os.Build.BRAND;
			device = android.os.Build.DEVICE;
			fingerprint = android.os.Build.FINGERPRINT;
			host = android.os.Build.HOST;
			id = android.os.Build.ID;
			model = android.os.Build.MODEL;
			product = android.os.Build.PRODUCT;
			tags = android.os.Build.TAGS;
			type = android.os.Build.TYPE;
			user = android.os.Build.USER;

			List<PackageInfo> apps = getPackageManager()
					.getInstalledPackages(0);
			for (PackageInfo packageInfo : apps) {
				if (isSystemPackage(packageInfo)) {
					systemApps.add(packageInfo);
				} else {
					userApps.add(packageInfo);
				}
			}
			numberOfInstalledApps = userApps.size();
			numberOfSystemApps = systemApps.size();
			Display display = getWindowManager().getDefaultDisplay();
			

			
				screenRezolution = String.valueOf(display.getWidth()) + "x"
						+ String.valueOf(display.getHeight());

			
			screenRefreshRate = String.valueOf(display.getRefreshRate())
					+ "fps";

			DisplayMetrics dm = SystemInfo.this.getResources()
					.getDisplayMetrics();
			screenDensity = String.valueOf(dm.densityDpi) + "dpi";
			screenPpi = "X: " + String.valueOf(dm.xdpi) + ", Y "
					+ String.valueOf(dm.ydpi);

			try {

				File myFile = new File(
						"/sys/class/power_supply/battery/capacity");
				FileInputStream fIn = new FileInputStream(myFile);

				BufferedReader myReader = new BufferedReader(
						new InputStreamReader(fIn));
				String aDataRow = "";
				String aBuffer = "";
				while ((aDataRow = myReader.readLine()) != null) {
					aBuffer += aDataRow + "\n";
				}

				battperc = Integer.parseInt(aBuffer.trim());
				myReader.close();

			} catch (Exception e) {

			}

			try {

				File myFile = new File(
						"/sys/class/power_supply/battery/batt_temp");
				FileInputStream fIn = new FileInputStream(myFile);

				BufferedReader myReader = new BufferedReader(
						new InputStreamReader(fIn));
				String aDataRow = "";
				String aBuffer = "";
				while ((aDataRow = myReader.readLine()) != null) {
					aBuffer += aDataRow + "\n";
				}

				batttemp = Double.parseDouble(aBuffer.trim()) / 10;
				myReader.close();

			} catch (Exception e) {

			}

			try {

				File myFile = new File(
						"/sys/class/power_supply/battery/batt_current");
				FileInputStream fIn = new FileInputStream(myFile);

				BufferedReader myReader = new BufferedReader(
						new InputStreamReader(fIn));
				String aDataRow = "";
				String aBuffer = "";
				while ((aDataRow = myReader.readLine()) != null) {
					aBuffer += aDataRow + "\n";
				}

				battcurrent = aBuffer.trim();
				myReader.close();

			} catch (Exception e) {
				battcurrent = "err";
			}

			try {

				File myFile = new File("/proc/cpuinfo");
				FileInputStream fIn = new FileInputStream(myFile);
				BufferedReader myReader = new BufferedReader(
						new InputStreamReader(fIn));
				String aDataRow = "";
				String aBuffer = "";
				while ((aDataRow = myReader.readLine()) != null) {
					aBuffer += aDataRow + "\n";
				}

				cpu_info = aBuffer.trim();
				myReader.close();

			} catch (Exception e) {
				cpu_info = "err";
			}

			

			try {

				File myFile = new File(
						"/sys/devices/platform/kgsl-3d0.0/kgsl/kgsl-3d0/max_gpuclk");
				FileInputStream fIn = new FileInputStream(myFile);

				BufferedReader myReader = new BufferedReader(
						new InputStreamReader(fIn));
				String aDataRow = "";
				String aBuffer = "";
				while ((aDataRow = myReader.readLine()) != null) {
					aBuffer += aDataRow + "\n";
				}

				gpu3d = Integer.parseInt(aBuffer.trim());
				myReader.close();

			} catch (Exception e) {

			}

			try {

				File myFile = new File(
						"/sys/devices/platform/kgsl-2d0.0/kgsl/kgsl-2d0/max_gpuclk");
				FileInputStream fIn = new FileInputStream(myFile);
				BufferedReader myReader = new BufferedReader(
						new InputStreamReader(fIn));
				String aDataRow = "";
				String aBuffer = "";
				while ((aDataRow = myReader.readLine()) != null) {
					aBuffer += aDataRow + "\n";
				}

				gpu2d = Integer.parseInt(aBuffer.trim());

				myReader.close();

			} catch (Exception e) {

			}

			try {
				String aBuffer = "";
				File myFile = new File(
						"/sys/kernel/fast_charge/force_fast_charge");
				FileInputStream fIn = new FileInputStream(myFile);
				BufferedReader myReader = new BufferedReader(
						new InputStreamReader(fIn));
				String aDataRow = "";
				while ((aDataRow = myReader.readLine()) != null) {
					aBuffer += aDataRow + "\n";
				}

				fastcharge = Integer.parseInt(aBuffer.trim());
				myReader.close();

			} catch (Exception e) {
				
			}

			try {
				String aBuffer = "";
				File myFile = new File(
						"/sys/kernel/debug/msm_fb/0/vsync_enable");
				FileInputStream fIn = new FileInputStream(myFile);
				BufferedReader myReader = new BufferedReader(
						new InputStreamReader(fIn));
				String aDataRow = "";
				while ((aDataRow = myReader.readLine()) != null) {
					aBuffer += aDataRow + "\n";
				}

				vsync = Integer.parseInt(aBuffer.trim());
				myReader.close();

			} catch (Exception e) {
				
			}

			try {

				File myFile = new File("/sys/kernel/debug/msm_fb/0/bpp");
				FileInputStream fIn = new FileInputStream(myFile);

				BufferedReader myReader = new BufferedReader(
						new InputStreamReader(fIn));
				String aDataRow = "";
				String aBuffer = "";
				while ((aDataRow = myReader.readLine()) != null) {
					aBuffer += aDataRow + "\n";
				}

				cdepth = Integer.parseInt(aBuffer.trim());
				myReader.close();
				

			} catch (IOException e) {
				
			}

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
				kernel = "Kernel version file not found";

			}

			try {

				File myFile = new File("/sys/block/mmcblk0/queue/scheduler");
				FileInputStream fIn = new FileInputStream(myFile);

				BufferedReader myReader = new BufferedReader(
						new InputStreamReader(fIn));
				String aDataRow = "";
				String aBuffer = "";
				while ((aDataRow = myReader.readLine()) != null) {
					aBuffer += aDataRow + "\n";
				}

				schedulers = aBuffer;
				myReader.close();

				scheduler = schedulers.substring(schedulers.indexOf("[") + 1,
						schedulers.indexOf("]"));
				scheduler.trim();
				schedulers = schedulers.replace("[", "");
				schedulers = schedulers.replace("]", "");

			} catch (Exception e) {
				schedulers = "err";
				scheduler = "err";
			}

			

			try {

				File myFile = new File(
						"/sys/kernel/msm_mpdecision/conf/enabled");
				FileInputStream fIn = new FileInputStream(myFile);

				BufferedReader myReader = new BufferedReader(
						new InputStreamReader(fIn));
				String aDataRow = "";
				String aBuffer = "";
				while ((aDataRow = myReader.readLine()) != null) {
					aBuffer += aDataRow + "\n";
				}

				mpdec = Integer.parseInt(aBuffer.trim());
				myReader.close();

			} catch (Exception e) {

			}

			try {

				File myFile = new File("/sys/android_touch/sweep2wake");
				FileInputStream fIn = new FileInputStream(myFile);

				BufferedReader myReader = new BufferedReader(
						new InputStreamReader(fIn));
				String aDataRow = "";
				String aBuffer = "";
				while ((aDataRow = myReader.readLine()) != null) {
					aBuffer += aDataRow + "\n";
				}

				s2w = Integer.parseInt(aBuffer.trim());

				myReader.close();

			} catch (Exception e) {

				try {

					File myFile = new File(
							"/sys/android_touch/sweep2wake/s2w_switch");
					FileInputStream fIn = new FileInputStream(myFile);

					BufferedReader myReader = new BufferedReader(
							new InputStreamReader(fIn));
					String aDataRow = "";
					String aBuffer = "";
					while ((aDataRow = myReader.readLine()) != null) {
						aBuffer += aDataRow + "\n";
					}

					s2w = Integer.parseInt(aBuffer.trim());

					myReader.close();

				} catch (Exception e2) {

					
				}
			}

			return "";
		}

		@Override
		protected void onPostExecute(Object result) {
			setUI();
			pd.dismiss();
			
		}

	}



	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.system_info);
		System.out.println(getTotalRAM());
		pd = ProgressDialog.show(this, null,
				"Gathering system information\nPlease wait...");
		new info().execute();
		prefs = PreferenceManager.getDefaultSharedPreferences(this);
		tempPref = prefs.getString("temp", "celsius");
		

	}


	public static String tempConverter(String tempPref, double cTemp) {
		String tempNew = "";
		/**
		 * cTemp = temperature in celsius tempPreff = string from shared
		 * preferences with value fahrenheit, celsius or kelvin
		 */
		if (tempPref.equals("fahrenheit")) {
			tempNew = String.valueOf((cTemp * 1.8) + 32) + "°F";

		} else if (tempPref.equals("celsius")) {
			tempNew = String.valueOf(cTemp) + "°C";

		} else if (tempPref.equals("kelvin")) {

			tempNew = String.valueOf(cTemp + 273.15) + "°C";

		}
		return tempNew;
	}

	public static Integer getTotalRAM() {
		RandomAccessFile reader = null;
		String load = null;
		Integer mem = null;
		try {
			reader = new RandomAccessFile("/proc/meminfo", "r");
			load = reader.readLine();
			mem = Integer.parseInt(load.substring(load.indexOf(":") + 1,
					load.lastIndexOf(" ")).trim()) / 1024;
		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			try {
				reader.close();
			} catch (IOException e) {

				e.printStackTrace();
			}
		}
		return mem;
	}

	public Integer getFreeRAM() {
		MemoryInfo mi = new MemoryInfo();
		ActivityManager activityManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
		activityManager.getMemoryInfo(mi);
		Integer mem = (int) (mi.availMem / 1048576L);
		return mem;

	}

	public static long getAvailableSpaceInBytesOnInternalStorage() {
		long availableSpace = -1L;
		StatFs stat = new StatFs(Environment.getDataDirectory().getPath());
		availableSpace = (long) stat.getAvailableBlocks()
				* (long) stat.getBlockSize();

		return availableSpace;
	}

	public static long getUsedSpaceInBytesOnInternalStorage() {
		long usedSpace = -1L;
		StatFs stat = new StatFs(Environment.getDataDirectory().getPath());
		usedSpace = ((long) stat.getBlockCount() - stat.getAvailableBlocks())
				* (long) stat.getBlockSize();

		return usedSpace;
	}

	public static long getTotalSpaceInBytesOnInternalStorage() {
		long usedSpace = -1L;
		StatFs stat = new StatFs(Environment.getDataDirectory().getPath());
		usedSpace = ((long) stat.getBlockCount()) * (long) stat.getBlockSize();

		return usedSpace;
	}

	public static long getAvailableSpaceInBytesOnExternalStorage() {
		long availableSpace = -1L;
		StatFs stat = new StatFs(Environment.getExternalStorageDirectory()
				.getPath());
		availableSpace = (long) stat.getAvailableBlocks()
				* (long) stat.getBlockSize();

		return availableSpace;
	}

	public static long getUsedSpaceInBytesOnExternalStorage() {
		long usedSpace = -1L;
		StatFs stat = new StatFs(Environment.getExternalStorageDirectory()
				.getPath());
		usedSpace = ((long) stat.getBlockCount() - stat.getAvailableBlocks())
				* (long) stat.getBlockSize();

		return usedSpace;
	}

	public static long getTotalSpaceInBytesOnExternalStorage() {
		long usedSpace = -1L;
		StatFs stat = new StatFs(Environment.getExternalStorageDirectory()
				.getPath());
		usedSpace = ((long) stat.getBlockCount()) * (long) stat.getBlockSize();

		return usedSpace;
	}

	public String humanReadableSize(long size) {
		String hrSize = "";

		long b = size;
		double k = size / 1024.0;
		double m = size / 1048576.0;
		double g = size / 1073741824.0;
		double t = size / 1099511627776.0;

		DecimalFormat dec = new DecimalFormat("0.00");

		if (t > 1) {

			hrSize = dec.format(t).concat("TB");
		} else if (g > 1) {

			hrSize = dec.format(g).concat("GB");
		} else if (m > 1) {

			hrSize = dec.format(m).concat("MB");
		} else if (k > 1) {

			hrSize = dec.format(k).concat("KB");

		} else if (b > 1) {
			hrSize = dec.format(b).concat("B");
		}

		return hrSize;

	}

	public void setUI(){
		Integer freeRAM = getFreeRAM();
		Integer totalRAM = getTotalRAM();
		Integer usedRAM = getTotalRAM() - getFreeRAM();
		long freeInternal = getAvailableSpaceInBytesOnInternalStorage();
		long usedInternal = getUsedSpaceInBytesOnInternalStorage();
		long totalInternal = getTotalSpaceInBytesOnInternalStorage();
		long freeExternal = getAvailableSpaceInBytesOnExternalStorage();
		long usedExternal = getUsedSpaceInBytesOnExternalStorage();
		long totalExternal = getTotalSpaceInBytesOnExternalStorage();

		
		TextView level = (TextView) findViewById(R.id.textView1);
		ProgressBar levelProgress = (ProgressBar) findViewById(R.id.progressBar1);
		TextView temp = (TextView) findViewById(R.id.textView3);
		TextView drain = (TextView) findViewById(R.id.textView5);
		TextView totalRAMtxt = (TextView) findViewById(R.id.textView7);
		TextView freeRAMtxt = (TextView) findViewById(R.id.textView8);
		ProgressBar ramProgress = (ProgressBar) findViewById(R.id.progressBar2);
		TextView totalInternaltxt = (TextView) findViewById(R.id.textView10);
		TextView freeInternaltxt = (TextView) findViewById(R.id.textView11);
		ProgressBar internalProgress = (ProgressBar) findViewById(R.id.progressBar3);
		TextView totalExternaltxt = (TextView)findViewById(R.id.textView13);
		TextView freeExternaltxt = (TextView) findViewById(R.id.textView14);
		ProgressBar externalProgress = (ProgressBar)findViewById(R.id.progressBar4);
		TextView Externaltxt = (TextView)findViewById(R.id.textView12);

		if (battperc != null) {
			level.setText("Level: " + String.valueOf(battperc) + "%");
			levelProgress.setProgress(battperc);
		} else {
			level.setText("Unknown");
		}
		if (batttemp != null) {
			temp.setText(tempConverter(tempPref, batttemp));
		} else {
			temp.setText("Unknown");
		}
		if (!battcurrent.equals("err")) {
			drain.setText(battcurrent + "mAh");
		} else {
			drain.setText("Unknown");
		}
		totalRAMtxt.setText("Total: " + String.valueOf(totalRAM) + "MB");
		freeRAMtxt.setText("Free: " + String.valueOf(freeRAM) + "MB");
		ramProgress.setProgress(usedRAM * 100 / totalRAM);

		totalInternaltxt.setText("Total: " + humanReadableSize(totalInternal));
		freeInternaltxt.setText("Free: " + humanReadableSize(freeInternal));
		internalProgress
				.setProgress((int) (usedInternal * 100 / totalInternal));
		if (isSDPresent) {
			totalExternaltxt.setText("Total: "
					+ humanReadableSize(totalExternal));
			freeExternaltxt.setText("Free: " + humanReadableSize(freeExternal));
			externalProgress
					.setProgress((int) (usedExternal * 100 / totalExternal));
		} else {
			Externaltxt.setText("External Storage not present");
			totalExternaltxt.setVisibility(View.GONE);
			freeExternaltxt.setVisibility(View.GONE);
			externalProgress.setVisibility(View.GONE);
		}
		
		TextView androidVersiontxt = (TextView) 
				findViewById(R.id.androidVersion);
		TextView apitxt = (TextView) findViewById(R.id.api);
		TextView cpuAbitxt = (TextView) findViewById(R.id.cpuAbi);
		TextView manufacturertxt = (TextView) 
				findViewById(R.id.manufacturer);
	
		TextView hardwaretxt = (TextView) findViewById(R.id.hardware);
		
		TextView boardtxt = (TextView) findViewById(R.id.board);
		TextView brandtxt = (TextView) findViewById(R.id.brand);
		TextView devicetxt = (TextView) findViewById(R.id.device);
		TextView displaytxt = (TextView) findViewById(R.id.display);
		TextView fingerprinttxt = (TextView) 
				findViewById(R.id.fingerprint);
		TextView hosttxt = (TextView) findViewById(R.id.host);
		TextView idtxt = (TextView) findViewById(R.id.id);
		TextView modeltxt = (TextView) findViewById(R.id.model);
		TextView producttxt = (TextView) findViewById(R.id.product);
		TextView tagstxt = (TextView) findViewById(R.id.tags);
		TextView typetxt = (TextView) findViewById(R.id.type);
		TextView usertxt = (TextView) findViewById(R.id.user);
		TextView userAppstxt = (TextView) findViewById(R.id.userApps);
		TextView systemAppstxt = (TextView) 
				findViewById(R.id.systemApps);
		TextView screenRestxt = (TextView) 
				findViewById(R.id.screenResolution);
		TextView screenRefreshratetxt = (TextView)
				findViewById(R.id.screenRefreshRate);
		TextView screenDensitytxt = (TextView) 
				findViewById(R.id.screenDensity);
		TextView screenPPItxt = (TextView) 
				findViewById(R.id.screenPPI);
		TextView kerneltxt = (TextView) findViewById(R.id.kernel);
		TextView gpu2dtxt = (TextView) findViewById(R.id.gpu2d);
		TextView gpu3dtxt = (TextView) findViewById(R.id.gpu3d);
		TextView vsynctxt = (TextView) findViewById(R.id.vsync);
		TextView fastchargetxt = (TextView) findViewById(R.id.fastcharge);
		TextView colorDepthtxt = (TextView) findViewById(R.id.cdepth);
		TextView schedulerstxt = (TextView) findViewById(R.id.schedulers);
		TextView s2wtxt = (TextView)findViewById(R.id.s2w);
		if(gpu2d!=null){
			gpu2dtxt.setText(String.valueOf(gpu2d/1000000)+"MHz");
		}
		else{
			gpu2dtxt.setText("Unknown");
		}
		if(gpu3d!=null){
			gpu3dtxt.setText(String.valueOf(gpu3d/1000000)+"MHz");
		}
		else{
			gpu3dtxt.setText("Unknown");
		}
		if(vsync!=null){
			if(vsync==0){
			vsynctxt.setText("OFF");
			vsynctxt.setTextColor(Color.RED);
			}
			else if(vsync==1){
				vsynctxt.setText("ON");
				vsynctxt.setTextColor(Color.GREEN);
			}
			else{
				vsynctxt.setText("Unknown");
			}
		}
		else{
			vsynctxt.setText("Unknown");
		}
		if(fastcharge!=null){
			if(fastcharge==0){
				fastchargetxt.setText("OFF");
				fastchargetxt.setTextColor(Color.RED);
			}
			else if(fastcharge==1){
				fastchargetxt.setText("ON");
				fastchargetxt.setTextColor(Color.GREEN);
			}
			else{
				fastchargetxt.setText("Unknown");
			}
		}
		else{
			fastchargetxt.setText("Unknown");
		}
		if(cdepth!=null){
			colorDepthtxt.setText(String.valueOf(cdepth)+"-bit");
		}
		else{
			colorDepthtxt.setText("Unknown");
		}
		schedulerstxt.setText(schedulers);
		if(s2w!=null){
			if(s2w==0){
			s2wtxt.setText("OFF");
			}
			else{
				s2wtxt.setText("ON");
			}
		}
		else{
			s2wtxt.setText("Unknown");
		}
		androidVersiontxt.setText(androidVersion);
		apitxt.setText(String.valueOf(apiLevel));
		if (apiLevel >= 14) {
			androidVersiontxt.setTextColor(Color.GREEN);
			apitxt.setTextColor(Color.GREEN);
		} else {
			androidVersiontxt.setTextColor(Color.RED);
			apitxt.setTextColor(Color.RED);
		}
		cpuAbitxt.setText(cpuAbi);
		manufacturertxt.setText(manufacturer);
		hardwaretxt.setText(hardware);
		boardtxt.setText(board);
		brandtxt.setText(brand);
		devicetxt.setText(device);
		displaytxt.setText(display);
		fingerprinttxt.setText(fingerprint);
		hosttxt.setText(host);
		idtxt.setText(id);
		modeltxt.setText(model);
		producttxt.setText(product);
		tagstxt.setText(tags);
		typetxt.setText(type);
		usertxt.setText(user);
		userAppstxt.setText(String.valueOf(numberOfInstalledApps));
		systemAppstxt.setText(String.valueOf(numberOfSystemApps));
		screenRestxt.setText(screenRezolution);
		screenRefreshratetxt.setText(screenRefreshRate);
		screenDensitytxt.setText(screenDensity);
		screenPPItxt.setText(screenPpi);
		kerneltxt.setText(kernel);
		
		TextView cpu = (TextView) findViewById(R.id.tv);
		TextView freqRange = (TextView) findViewById(R.id.freqRange);
		TextView mpdectxt = (TextView) findViewById(R.id.mpdec);
		TextView thermal = (TextView) findViewById(R.id.thermal);
		TextView governorstxt = (TextView)findViewById(R.id.governors);
		TextView voltRange = (TextView) findViewById(R.id.voltRange);
		LinearLayout mpdecLayout = (LinearLayout) findViewById(R.id.mpdecLayout);
		LinearLayout thermalLayout = (LinearLayout)findViewById(R.id.thermalLayout);
		LinearLayout voltageLayout = (LinearLayout) findViewById(R.id.voltageLayout);

		cpu.setText(cpu_info);
		if (freqs.isEmpty() == false) {
			freqRange.setText(freqs.get(0) + " - "
					+ freqs.get(freqs.size() - 1));
		} else {
			freqRange.setText("Unknown");
		}
		if (mpdec != null) {
			if (mpdec == 0) {
				mpdectxt.setText("OFF");
				mpdectxt.setTextColor(Color.RED);
			} else if (mpdec == 1) {
				mpdectxt.setText("ON");
				mpdectxt.setTextColor(Color.GREEN);
			} else {
				mpdectxt.setText("Unknown");
				mpdectxt.setTextColor(Color.RED);
			}
		} else {
			mpdecLayout.setVisibility(View.GONE);
		}
		if (new File("/sys/kernel/msm_thermal/conf").exists()) {
			thermal.setText("ON");
			thermal.setTextColor(Color.GREEN);
		} else {
			thermalLayout.setVisibility(View.GONE);
		}
		if (governors.equals("")) {
			governorstxt.setText("Unknown");
		} else {
			governorstxt.setText(governors);
		}
		if (voltages.isEmpty() == false) {
			voltRange.setText(String.valueOf(voltages.get(0) / 1000) + "mV("
					+ voltFreq.get(0) + ") - "
					+ String.valueOf(voltages.get(voltages.size() - 1) / 1000)
					+ "mV(" + voltFreq.get(voltFreq.size() - 1) + ")");
		} else {
			voltageLayout.setVisibility(View.GONE);
		}
	}

}
