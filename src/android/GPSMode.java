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
				float lat,lon,acc;

				MyLocation.LocationResult locationResult = new MyLocation.LocationResult(){
           		@Override
            	public void gotLocation(Location location){
                //Got the location!
                // Log.e("Location", String.valueOf(location.getLatitude()));
                lat= (float) location.getLatitude();
                lon= (float) location.getLongitude();
                acc=location.getAccuracy();
		                Toast.makeText(context,"Latitude : "+lat+", Longitude : "+lon+", Accurancy : "+acc,Toast.LENGTH_LONG).show();
		            }
		        };
		        MyLocation myLocation = new MyLocation();
		        myLocation.getLocation(context, locationResult);
				// Toast.makeText(context,"Getting Location",Toast.LENGTH_SHORT).show();
				// Location location1=getLocation();
				// if(location1==null){
				// 	callbackContext.error("Loction Not Founded ");
				// 	return false;		
				// }
	   //          JSONObject locationObj=new JSONObject();
	   //          locationObj.put("latitude",location1.getLatitude());
	   //          locationObj.put("longitude",location1.getLongitude());
	   //          callbackContext.success(locationObj.toString());
	   //          Toast.makeText(context,locationObj.toString(),Toast.LENGTH_LONG).show();
	            // return true;
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
                    .getSystemService("location");
            location=locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(context,e.getMessage(),Toast.LENGTH_SHORT).show();
        }
        Toast.makeText(context,"Done",Toast.LENGTH_SHORT).show();
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