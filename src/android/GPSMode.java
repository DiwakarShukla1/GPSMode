package org.devgirl.gpsmode;
 
import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.json.JSONObject;
import org.json.JSONArray;
import org.json.JSONException;

import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.provider.Settings;
import android.net.Uri;
import android.widget.Toast;

public class GPSMode extends CordovaPlugin {
	public static final String ACTION_ON = "on";
	public static final String ACTION_OFF = "off"; 
	public static final String ACTION_ISGPSON = "isGPSOn"; 
	Context context=null;

	@Override
	public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
		context=this.cordova.getActivity().getApplicationContext();
		try{
			if(ACTION_ON.equalsIgnoreCase(action)){
				turnGPSOn();
			}else if(ACTION_OFF.equalsIgnoreCase(action)){
				turnGPSOff();
			}else if(ACTION_ISGPSON.equalsIgnoreCase(action)){
				if(!isGPSOn()){
					callbackContext.error("GPS is Off");
					return false;		
				}else{
					callbackContext.success("GPS is On");
	 	 			return true;
				}
			}
			callbackContext.success();
	 	 	return true;
		}catch(Exception e){
			callbackContext.error("Error " + e.getMessage());
			return false;
		}
	}

	public boolean isGPSOn(){
	    String provider = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
	    if(!provider.contains("gps")){ //if gps is disabled
	        return false;
	    }
	    return true;
	}

	public void turnGPSOn()
	{
	     Intent intent = new Intent("android.location.GPS_ENABLED_CHANGE");
	     intent.putExtra("enabled", true);
	     context.sendBroadcast(intent);

	    String provider = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
	    if(!provider.contains("gps")){ //if gps is disabled
	        final Intent poke = new Intent();
	        poke.setClassName("com.android.settings", "com.android.settings.widget.SettingsAppWidgetProvider"); 
	        poke.addCategory(Intent.CATEGORY_ALTERNATIVE);
	        poke.setData(Uri.parse("3")); 
	        context.sendBroadcast(poke);
	    }
	}

	// automatic turn off the gps
    public void turnGPSOff()
    {
        String provider = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
        if(provider.contains("gps")){ //if gps is enabled
            final Intent poke = new Intent();
            poke.setClassName("com.android.settings", "com.android.settings.widget.SettingsAppWidgetProvider");
            poke.addCategory(Intent.CATEGORY_ALTERNATIVE);
            poke.setData(Uri.parse("3")); 
            context.sendBroadcast(poke);
        }
    }
}