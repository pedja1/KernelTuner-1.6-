package rs.pedjaapps.KernelTuner.compatibility.ui;





import rs.pedjaapps.KernelTuner.compatibility.R;
import android.app.*;
import android.content.*;
import android.os.*;
import android.preference.*;
import android.preference.Preference.*;



public class Preferences extends PreferenceActivity
{

	private ListPreference bootPrefList;
	private EditTextPreference widgetPref;
	private ListPreference tempPrefList;
	private ListPreference tisList;
	private CheckBoxPreference resetApp;


	@Override
	public void onCreate(Bundle savedInstanceState)
	{        
		super.onCreate(savedInstanceState);

		addPreferencesFromResource(R.xml.preferences); 
		
		
		
		bootPrefList = (ListPreference) findPreference("boot");
        bootPrefList.setDefaultValue(bootPrefList.getEntryValues()[0]);
        String boot = bootPrefList.getValue();
        if (boot == null) {
            bootPrefList.setValue((String)bootPrefList.getEntryValues()[0]);
            boot = bootPrefList.getValue();
        }
        bootPrefList.setSummary(bootPrefList.getEntries()[bootPrefList.findIndexOfValue(boot)]);


        bootPrefList.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                bootPrefList.setSummary(bootPrefList.getEntries()[bootPrefList.findIndexOfValue(newValue.toString())]);
                return true;
            }
        }); 
        
        widgetPref = (EditTextPreference) findPreference("widget_time");
        widgetPref.setDefaultValue(widgetPref.getText());
        String widget = widgetPref.getText();
        if (widget == null) {
        	widgetPref.setText((String)widgetPref.getText());
            widget = widgetPref.getText();
        }
        widgetPref.setSummary(widget+"min");


        widgetPref.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
            	widgetPref.setSummary(newValue.toString()+"min");
                return true;
            }
        }); 
        
        tempPrefList = (ListPreference) findPreference("temp");
        tempPrefList.setDefaultValue(tempPrefList.getEntryValues()[0]);
        String temp = tempPrefList.getValue();
        if (temp == null) {
        	tempPrefList.setValue((String)tempPrefList.getEntryValues()[0]);
        	temp = tempPrefList.getValue();
        }
        tempPrefList.setSummary(tempPrefList.getEntries()[tempPrefList.findIndexOfValue(temp)]);


        tempPrefList.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
            	tempPrefList.setSummary(tempPrefList.getEntries()[tempPrefList.findIndexOfValue(newValue.toString())]);
                return true;
            }
        }); 
        
       
        
        
        
        
        resetApp = (CheckBoxPreference) findPreference("reset");
        resetApp.setOnPreferenceClickListener(new OnPreferenceClickListener(){

			@Override
			public boolean onPreferenceClick(Preference arg0) {
				if(resetApp.isChecked()){
				AlertDialog.Builder builder = new AlertDialog.Builder(
	                    Preferences.this);

					builder.setMessage("This will not work if init.d is selected for restoring settings after reboot.\n\n init.d scripts are executed before this application can start");

					builder.setIcon(R.drawable.ic_menu_info_details);

					builder.setPositiveButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which)
							{
							
							}
						});
					
					
					AlertDialog alert = builder.create();

					alert.show();
				}
				return false;
			}
        	
        });
		
		tisList = (ListPreference) findPreference("tis_open_as");
	//	SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(Preferences.this);
       // final SharedPreferences.Editor editor = prefs.edit();
		 tisList.setDefaultValue(tisList.getEntryValues()[0]);
        String tis = tisList.getValue();
        if (tis == null) {
        	tisList.setValue((String)tisList.getEntryValues()[0]);
        	tis = tisList.getValue();
        }
        tisList.setSummary(tisList.getEntries()[tisList.findIndexOfValue(tis)]);

        tisList.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
				@Override
				public boolean onPreferenceChange(Preference preference, Object newValue) {
					tisList.setSummary(tisList.getEntries()[tisList.findIndexOfValue(newValue.toString())]);
	

					return true;
				}
			}); 
       
	}
	
	
	
	
	
}
