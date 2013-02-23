package rs.pedjaapps.KernelTuner.compatibility.ui;

import java.util.ArrayList;
import java.util.List;

import rs.pedjaapps.KernelTuner.compatibility.R;
import rs.pedjaapps.KernelTuner.compatibility.entry.Profile;
import rs.pedjaapps.KernelTuner.compatibility.entry.Voltage;
import rs.pedjaapps.KernelTuner.compatibility.helpers.DatabaseHandler;
import rs.pedjaapps.KernelTuner.compatibility.helpers.IOHelper;



import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class ProfileEditor extends Activity
{
	
	
	private List<IOHelper.FreqsEntry> freqEntries = IOHelper.frequencies();
	private List<String> freqNames = new ArrayList<String>();
	private String cpu0min;
	private String cpu0max; 
	private String cpu1min;
	private String cpu1max;

	private String cpu0gov;
	private String cpu1gov;

	private String Name;
	private String voltage;
	private String scheduler;
	private String cdepth;
	
	private String profileName;
	private Profile profile;
	

	private int s2w;

	private EditText ed3;
	private EditText ed4;
	private EditText name;
	
	private RadioGroup vsyncGroup;
	private RadioGroup fchargeGroup;
	private RadioButton vsyncUnchanged;
	private RadioButton vsyncOff;
	private RadioButton vsyncOn;
	private RadioButton fchargeUnchanged;
	private RadioButton fchargeOff;
	private RadioButton fchargeOn;
	private DatabaseHandler db;
	private TextView vsyncText;
	private TextView fchargetext;
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		setContentView(R.layout.profile_editor);
		Intent intent = getIntent();
		db = new DatabaseHandler(this);
		profileName = intent.getExtras().getString("profileName");
		
		if(profileName!=null && !profileName.equals("")){
		profile = db.getProfileByName(profileName);
		}
		RelativeLayout cpu = (RelativeLayout)findViewById(R.id.cpu);
		final LinearLayout cpuInfo = (LinearLayout)findViewById(R.id.cpu_settings);
		final ImageView cpuImg = (ImageView)findViewById(R.id.cpu_img);

		RelativeLayout other = (RelativeLayout)findViewById(R.id.other);
		final LinearLayout otherInfo = (LinearLayout)findViewById(R.id.other_settings);
		final ImageView otherImg = (ImageView)findViewById(R.id.other_img);

		name = (EditText)findViewById(R.id.editText3);//profile name
		ed3  = (EditText)findViewById(R.id.editText4);//capacitive lights
		ed4  = (EditText)findViewById(R.id.editText5);//sd cache
		vsyncGroup = (RadioGroup)findViewById(R.id.vsyncGroup);
		fchargeGroup = (RadioGroup)findViewById(R.id.fchargeGroup);
		vsyncUnchanged = (RadioButton)findViewById(R.id.vsyncUnchanged);
		vsyncOff = (RadioButton)findViewById(R.id.vsyncOff);
		vsyncOn = (RadioButton)findViewById(R.id.vsyncOn);
		fchargeUnchanged = (RadioButton)findViewById(R.id.fchargeUnchanged);
		fchargeOff = (RadioButton)findViewById(R.id.fchargeOff);
		fchargeOn = (RadioButton)findViewById(R.id.fchargeOn);
		
		vsyncText = (TextView)findViewById(R.id.vsync);
		fchargetext = (TextView)findViewById(R.id.fcharge);
	
		
		cpu.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0)
			{
				if (cpuInfo.getVisibility() == View.VISIBLE)
				{
					cpuInfo.setVisibility(View.GONE);
					cpuImg.setImageResource(R.drawable.arrow_right);
				}
				else if (cpuInfo.getVisibility() == View.GONE)
				{
					cpuInfo.setVisibility(View.VISIBLE);
					cpuImg.setImageResource(R.drawable.arrow_down);
				}
			}

		});

	
	other.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0)
			{
				if (otherInfo.getVisibility() == View.VISIBLE)
				{
					otherInfo.setVisibility(View.GONE);
					otherImg.setImageResource(R.drawable.arrow_right);
				}
				else if (otherInfo.getVisibility() == View.GONE)
				{
					otherInfo.setVisibility(View.VISIBLE);
					otherImg.setImageResource(R.drawable.arrow_down);
				}
			}

		});
	
		final Spinner spinner1 = (Spinner)findViewById(R.id.spinner1);
		Spinner spinner2 = (Spinner)findViewById(R.id.spinner2);
		Spinner spinner3 = (Spinner)findViewById(R.id.spinner3);
		Spinner spinner4 = (Spinner)findViewById(R.id.spinner4);
		Spinner spinner9 = (Spinner)findViewById(R.id.spinner13);
		Spinner spinner12 = (Spinner)findViewById(R.id.spinner9);
		Spinner spinner13 = (Spinner)findViewById(R.id.spinner12);
		Spinner spinner16 = (Spinner)findViewById(R.id.spinner16);
		Spinner spinner17 = (Spinner)findViewById(R.id.spinner17);
		Spinner spinner18 = (Spinner)findViewById(R.id.spinner19);

		List<String> freqs = new ArrayList<String>();
		freqs.add(getResources().getString(R.string.unchanged));
		for(IOHelper.FreqsEntry f: freqEntries){
			freqs.add(String.valueOf(f.getFreq()));
		}
		for(IOHelper.FreqsEntry f: freqEntries){
			freqNames.add(f.getFreqName());
		}


		List<String> govs = new ArrayList<String>();
		govs.add(getResources().getString(R.string.unchanged));
		govs.addAll(IOHelper.governors());

		List<String> schedulers = new ArrayList<String>();
		schedulers.add(getResources().getString(R.string.unchanged));
		schedulers.addAll(IOHelper.schedulers());


		List<String> voltageProfiles = new ArrayList<String>();
		List<Voltage> voltages = db.getAllVoltages();
		voltageProfiles.add(getResources().getString(R.string.unchanged));
		for(Voltage v : voltages){
			voltageProfiles.add(v.getName());
		}
		LinearLayout buttons  = (LinearLayout)findViewById(R.id.buttons);//capacitive lights
		LinearLayout sd  = (LinearLayout)findViewById(R.id.sd);//sd cache


		if(profileName!=null && !profileName.equals("")){
			name.setText(profileName.toString());
		}
		
		if(profileName!=null && !profileName.equals("")){
			ed3.setText(profile.getButtonsLight());
		}
		if(profileName!=null && !profileName.equals("")){
			ed4.setText(String.valueOf(profile.getSdcache()));
		}
		if(profileName!=null && !profileName.equals("")){
			if(profile.getVsync()==0){
				vsyncOff.setChecked(true);
			}
			else if(profile.getVsync()==1){
				vsyncOn.setChecked(true);
			}
			else{
				vsyncUnchanged.setChecked(true);
			}
		}
		if(profileName!=null && !profileName.equals("")){
			if(profile.getFcharge()==0){
				fchargeOff.setChecked(true);
			}
			else if(profile.getFcharge()==1){
				fchargeOn.setChecked(true);
			}
			else{
				fchargeUnchanged.setChecked(true);
			}
		}

		
		if(IOHelper.buttonsExists()==false){
			buttons.setVisibility(View.GONE);
		}
		if(IOHelper.sdcacheExists()==false){
			sd.setVisibility(View.GONE);
		}

		if(IOHelper.vsyncExists()==false){
			vsyncGroup.setVisibility(View.GONE);
			vsyncText.setVisibility(View.GONE);
		}
		if(IOHelper.fchargeExists()==false){
			fchargeGroup.setVisibility(View.GONE);
			fchargetext.setVisibility(View.GONE);
		}
		String[] cd = {getResources().getString(R.string.unchanged),"16","24","32"};
		String[] sweep2wake = {getResources().getString(R.string.unchanged),"OFF","ON with no backlight","ON with backlight"};


		/*if(CPUInfo.cpu0Online()==true)
		 {*/
		/**spinner1*/
		ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this,   android.R.layout.simple_spinner_item, freqs);
		spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down vieww
		spinner1.setAdapter(spinnerArrayAdapter);

		if(profileName!=null && !profileName.equals("")){
			int spinner1Position = spinnerArrayAdapter.getPosition(profile.getCpu0min());
			spinner1.setSelection(spinner1Position);
		}
		spinner1.setOnItemSelectedListener(new OnItemSelectedListener() {
        		@Override 
        	    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id)
				{
        	    	cpu0min = parent.getItemAtPosition(pos).toString();

        	    }

        		@Override
        	    public void onNothingSelected(AdapterView<?> parent)
				{
        	        //do nothing
        	    }



        	});


		/**spinner2*/
		ArrayAdapter<String> spinner2ArrayAdapter = new ArrayAdapter<String>(this,   android.R.layout.simple_spinner_item, freqs);
		spinner2ArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down vieww
		spinner2.setAdapter(spinner2ArrayAdapter);

		if(profileName!=null && !profileName.equals("")){
			int spinner1Position = spinner2ArrayAdapter.getPosition(profile.getCpu0max());
			spinner2.setSelection(spinner1Position);
		}

		spinner2.setOnItemSelectedListener(new OnItemSelectedListener() {
        		@Override
        	    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id)
				{
        	    	cpu0max = parent.getItemAtPosition(pos).toString();

        	    }

        		@Override
        	    public void onNothingSelected(AdapterView<?> parent)
				{
        	        //do nothing
        	    }
        	});

		/**spinner9*/
		ArrayAdapter<String> spinner9ArrayAdapter = new ArrayAdapter<String>(this,   android.R.layout.simple_spinner_item, govs);
		spinner9ArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down vieww
		spinner9.setAdapter(spinner9ArrayAdapter);

		if(profileName!=null && !profileName.equals("")){
			int spinner1Position = spinner9ArrayAdapter.getPosition(profile.getCpu0gov());
			spinner9.setSelection(spinner1Position);
		}

		spinner9.setOnItemSelectedListener(new OnItemSelectedListener() {
        		@Override
        	    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id)
				{
        	    	cpu0gov = parent.getItemAtPosition(pos).toString();

        	    }

        		@Override
        	    public void onNothingSelected(AdapterView<?> parent)
				{
        	        //do nothing
        	    }
        	});
		
		if(IOHelper.cpu1Online()==true)
		{
			/**spinner3*/
			ArrayAdapter<String> spinner3ArrayAdapter = new ArrayAdapter<String>(this,   android.R.layout.simple_spinner_item, freqs);
			spinner3ArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down vieww
			spinner3.setAdapter(spinner3ArrayAdapter);

			if(profileName!=null && !profileName.equals("")){
				int spinner1Position = spinner3ArrayAdapter.getPosition(profile.getCpu1min());
				spinner3.setSelection(spinner1Position);
			}

			spinner3.setOnItemSelectedListener(new OnItemSelectedListener() {
					@Override
					public void onItemSelected(AdapterView<?> parent, View view, int pos, long id)
					{
						cpu1min = parent.getItemAtPosition(pos).toString();

					}

					@Override
					public void onNothingSelected(AdapterView<?> parent)
					{
						//do nothing
					}
				});

			/**spinner4*/
			ArrayAdapter<String> spinner4ArrayAdapter = new ArrayAdapter<String>(this,   android.R.layout.simple_spinner_item, freqs);
			spinner4ArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down vieww
			spinner4.setAdapter(spinner4ArrayAdapter);

			if(profileName!=null && !profileName.equals("")){
				int spinner1Position = spinner4ArrayAdapter.getPosition(profile.getCpu1max());
				spinner4.setSelection(spinner1Position);
			}

			spinner4.setOnItemSelectedListener(new OnItemSelectedListener() {
					@Override
					public void onItemSelected(AdapterView<?> parent, View view, int pos, long id)
					{
						cpu1max = parent.getItemAtPosition(pos).toString();

					}

					@Override
					public void onNothingSelected(AdapterView<?> parent)
					{
						//do nothing
					}
				});

			/**spinner10*/
			ArrayAdapter<String> spinner12ArrayAdapter = new ArrayAdapter<String>(this,   android.R.layout.simple_spinner_item, govs);
			spinner12ArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down vieww
			spinner12.setAdapter(spinner12ArrayAdapter);

			if(profileName!=null && !profileName.equals("")){
				int spinner1Position = spinner12ArrayAdapter.getPosition(profile.getCpu1gov());
				spinner12.setSelection(spinner1Position);
			}

			spinner12.setOnItemSelectedListener(new OnItemSelectedListener() {
					@Override
					public void onItemSelected(AdapterView<?> parent, View view, int pos, long id)
					{
						cpu1gov = parent.getItemAtPosition(pos).toString();

					}

					@Override
					public void onNothingSelected(AdapterView<?> parent)
					{
						//do nothing
					}
				});
		}
		else{
			LinearLayout cpu1minll = (LinearLayout)findViewById(R.id.cpu1min);
			LinearLayout cpu1maxll = (LinearLayout)findViewById(R.id.cpu1max);
			LinearLayout cpu1govll = (LinearLayout)findViewById(R.id.cpu1gov);

			cpu1minll.setVisibility(View.GONE);
			cpu1maxll.setVisibility(View.GONE);
			cpu1govll.setVisibility(View.GONE);
		}
		
		if(IOHelper.voltageExists())
		{
			/**spinner13*/
			ArrayAdapter<String> spinner13ArrayAdapter = new ArrayAdapter<String>(this,   android.R.layout.simple_spinner_item, voltageProfiles);
			spinner13ArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down vieww
			spinner13.setAdapter(spinner13ArrayAdapter);

			if(profileName!=null && !profileName.equals("")){
				int spinner1Position = spinner13ArrayAdapter.getPosition(profile.getVoltage());
				spinner13.setSelection(spinner1Position);
			}

			spinner13.setOnItemSelectedListener(new OnItemSelectedListener() {
					@Override
					public void onItemSelected(AdapterView<?> parent, View view, int pos, long id)
					{
						voltage = parent.getItemAtPosition(pos).toString();

					}

					@Override
					public void onNothingSelected(AdapterView<?> parent)
					{
						//do nothing
					}
				});
		}
		else{
			LinearLayout voltage = (LinearLayout)findViewById(R.id.voltage);

			voltage.setVisibility(View.GONE);
		}
		
		if(IOHelper.cdExists())
		{
			/**spinner16*/
			ArrayAdapter<String> spinner16ArrayAdapter = new ArrayAdapter<String>(this,   android.R.layout.simple_spinner_item, cd);
			spinner16ArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down vieww
			spinner16.setAdapter(spinner16ArrayAdapter);

			if(profileName!=null && !profileName.equals("")){
				int spinner1Position = spinner16ArrayAdapter.getPosition(profile.getCdepth());
				spinner16.setSelection(spinner1Position);
			}

			spinner16.setOnItemSelectedListener(new OnItemSelectedListener() {
					@Override
					public void onItemSelected(AdapterView<?> parent, View view, int pos, long id)
					{
						cdepth = parent.getItemAtPosition(pos).toString();

					}

					@Override
					public void onNothingSelected(AdapterView<?> parent)
					{
						//do nothing
					}
				});
		}
		else{
			LinearLayout cdepthll = (LinearLayout)findViewById(R.id.cdepth);

			cdepthll.setVisibility(View.GONE);
		}
		/**spinner17*/ 
		ArrayAdapter<String> spinner17ArrayAdapter = new ArrayAdapter<String>(this,   android.R.layout.simple_spinner_item, schedulers);
		spinner17ArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down vieww
		spinner17.setAdapter(spinner17ArrayAdapter);

		if(profileName!=null && !profileName.equals("")){
			int spinner1Position = spinner17ArrayAdapter.getPosition(profile.getIoScheduler());
			spinner17.setSelection(spinner1Position);
		}

		spinner17.setOnItemSelectedListener(new OnItemSelectedListener() {
        		@Override
        	    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id)
				{
        	    	scheduler = parent.getItemAtPosition(pos).toString();

        	    }

        		@Override
        	    public void onNothingSelected(AdapterView<?> parent)
				{
        	        //do nothing
        	    }
        	});
		if(IOHelper.s2wExists())
		{
			/**spinner18*/
			ArrayAdapter<String> spinner18ArrayAdapter = new ArrayAdapter<String>(this,   android.R.layout.simple_spinner_item, sweep2wake);
			spinner18ArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down vieww
			spinner18.setAdapter(spinner18ArrayAdapter);

			if(profileName!=null && !profileName.equals("")){
				if(profile.getSweep2wake()==0){
					int spinner1Position = spinner18ArrayAdapter.getPosition("OFF");
					spinner18.setSelection(spinner1Position);
				}
				else if(profile.getSweep2wake()==1){
					int spinner1Position = spinner18ArrayAdapter.getPosition("ON with no backlight");
					spinner18.setSelection(spinner1Position);
				}
				else if(profile.getSweep2wake()==1){
					int spinner1Position = spinner18ArrayAdapter.getPosition("ON with backlight");
					spinner18.setSelection(spinner1Position);
				}

			}

			spinner18.setOnItemSelectedListener(new OnItemSelectedListener() {
					@Override
					public void onItemSelected(AdapterView<?> parent, View view, int pos, long id)
					{
						if(parent.getItemAtPosition(pos).toString().equals("ON with no backlight"))
						{
							s2w = 1;
						}
						else if(parent.getItemAtPosition(pos).toString().equals("OFF"))
						{
							s2w = 0;
						}
						else if(parent.getItemAtPosition(pos).toString().equals("ON with backlight"))
						{
							s2w = 2;
						}
						else if(parent.getItemAtPosition(pos).toString().equals("Unchanged"))
						{
							s2w = 3;
						}


					}

					@Override
					public void onNothingSelected(AdapterView<?> parent)
					{
						//do nothing
					}
				});
		}
		else{
			LinearLayout s2wll = (LinearLayout)findViewById(R.id.s2w);

			s2wll.setVisibility(View.GONE);
		}
			
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.profile_editor_options_menu, menu);
		return super.onCreateOptionsMenu(menu);
}
@Override
public boolean onPrepareOptionsMenu (Menu menu) {

return true;
}

@Override
public boolean onOptionsItemSelected(MenuItem item) {

	if (item.getItemId() == R.id.save)
	{
    	
		if(name.getText().toString().length()<1 || name.getText().toString().equals(""))
		{
			Toast.makeText(getApplicationContext(), getResources().getString(R.string.empty_profile_name), Toast.LENGTH_LONG).show();
			
		}
		else{
		int vsync = -1;
		int fcharge = -1;
		int sdcache = 0;
		try{
			sdcache = Integer.parseInt(ed4.getText().toString());
		}catch(NumberFormatException e){
			
		}
		if(vsyncOn.isChecked()){
			vsync=1;
		}
		else if(vsyncOff.isChecked()){
			vsync = 0;
		}
		
		if(fchargeOn.isChecked()){
			fcharge=1;
		}
		else if(fchargeOff.isChecked()){
			fcharge = 0;
		}
		
		if(profileName!=null && !profileName.equals("")){
			db.deleteProfileByName(profile);
		}
		Name = name.getText().toString();
		Intent intent = new Intent();
		intent.putExtra("Name", Name);
		intent.putExtra("cpu0min", cpu0min);
		intent.putExtra("cpu0max", cpu0max);
		intent.putExtra("cpu1min", cpu1min);
		intent.putExtra("cpu1max", cpu1max);
		intent.putExtra("cpu0gov", cpu0gov);
		intent.putExtra("cpu1gov", cpu1gov);
		intent.putExtra("voltageProfile", voltage);
		intent.putExtra("buttonsBacklight", ed3.getText().toString());
		intent.putExtra("vsync", vsync);
		intent.putExtra("fcharge", fcharge);
		intent.putExtra("cdepth", cdepth);
		intent.putExtra("io", scheduler);
		intent.putExtra("sdcache", sdcache);
		intent.putExtra("s2w", s2w);
		setResult(RESULT_OK, intent);
		finish();
		}
	}
		
	if (item.getItemId() == R.id.cancel)
	{
    	finish();
	}

return super.onOptionsItemSelected(item);

}

}

