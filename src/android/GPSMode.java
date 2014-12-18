package org.devgirl.gpsmode;
 
import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.json.JSONObject;
import org.json.JSONArray;
import org.json.JSONException;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.provider.Settings;
import android.net.Uri;
import android.widget.Toast;

public class GPSMode extends CordovaPlugin {
	public static final String ACTION_ON = "on";
	public static final String ACTION_OFF = "off"; 
	public static final String ACTION_ISGPSON = "isGPSOn";
	public static final String ACTION_GETLOCATION="getLocation";

	LocationManager locationManager=null;
    Location location=null;

	Context context=null;

	@Override
	public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
		try{
			context=this.cordova.getActivity().getApplicationContext();
			Toast.makeText(context,"GPSMODE",Toast.LENGTH_SHORT).show();
			if(ACTION_ON.equalsIgnoreCase(action)){
				turnGPSOn();
			}else if(ACTION_OFF.equalsIgnoreCase(action)){
				turnGPSOff();
			}else if(ACTION_ISGPSON.equalsIgnoreCase(action)){
				Toast.makeText(context,"isGPSOn",Toast.LENGTH_SHORT).show();
				if(!isGPSOn()){
					Toast.makeText(context,"GPS OFF",Toast.LENGTH_SHORT).show();
					callbackContext.error("GPS is Off");
					return false;		
				}else{
					Toast.makeText(context,"GPS ON",Toast.LENGTH_SHORT).show();
					callbackContext.success("GPS is On");
	 	 			return true;
				}
			}else if(ACTION_GETLOCATION.equalsIgnoreCase(action)){
				 Location location1=getLocation();
	            JSONObject locationObj=new JSONObject();
	            locationObj.put("latitude",location1.getLatitude());
	            locationObj.put("longitude",location1.getLongitude());
	            callbackContext.success(locationObj.toString());
	            return true;
			}
			callbackContext.success();
	 	 	return true;
		}catch(Exception e){
			callbackContext.error("Error " + e.getMessage());
			return false;
		}
	}

	public Location getLocation() {
        try {
            locationManager = (LocationManager) context
                    .getSystemService(LocationManager.GPS_PROVIDER);
            location=locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return location;
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