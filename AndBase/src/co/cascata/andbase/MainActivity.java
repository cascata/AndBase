package co.cascata.andbase;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.Fragment;
import android.app.WallpaperManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.location.Location;
import android.location.LocationListener;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends Activity implements SensorEventListener, View.OnClickListener, LocationListener{

	/* Globals */
	String BingURL = "http://www.bing.com/HPImageArchive.aspx?format=js&idx=0&n=1&nc=1397809837851&pid=hp";
	String BingImage ="";
	Camera cam = null;
	Parameters p = null;
	boolean on=false;
	int delay = 100;
	int times = 2;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		if (savedInstanceState == null) {
			getFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
		}
	
		 cam = Camera.open();
		 p = cam.getParameters();
		
		
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy);
		
		PostData();

	}

	
	public boolean PostData() {

		if(true) {
			new HttpAsyncTask()
				.execute(BingURL);
		}
		return true;
		
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_main, container,
					false);
			return rootView;
		}
	}

	@Override
	public void onLocationChanged(Location arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onProviderDisabled(String arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onProviderEnabled(String arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onAccuracyChanged(Sensor arg0, int arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onSensorChanged(SensorEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	
	private static String convertInputStreamToString(InputStream inputStream)
			throws IOException {
		BufferedReader bufferedReader = new BufferedReader(
				new InputStreamReader(inputStream));
		String line = "";
		String result = "";
		while ((line = bufferedReader.readLine()) != null)
			result += line;

		inputStream.close();
		return result;

	}
	public static String GET(String url) {
		InputStream inputStream = null;
		String result = "";
		try {

			// create HttpClient
			HttpClient httpclient = new DefaultHttpClient();

			// make GET request to the given URL
			HttpResponse httpResponse = httpclient.execute(new HttpGet(url));

			// receive response as inputStream
			inputStream = httpResponse.getEntity().getContent();

			// convert inputstream to string
			if (inputStream != null)
				result = convertInputStreamToString(inputStream);
			else
				result = "Did not work!";

		} catch (Exception e) {
			Log.d("InputStream", e.getLocalizedMessage());
		}

		return result;
	}

	private class HttpAsyncTask extends AsyncTask<String, Void, String> {
		
		
		
		@Override
		protected String doInBackground(String... urls) {

			return GET(urls[0]);
		}

		// onPostExecute displays the results of the AsyncTask.
		@Override
		protected void onPostExecute(String result) {
			Toast.makeText(getBaseContext(), "Received!", Toast.LENGTH_LONG)
					.show();

			JSONObject jsobj = null;
			try {
				jsobj = new JSONObject(result);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				//JSONObject DataObject = jsobj.getJSONObject("images");
				JSONArray ccond = jsobj.getJSONArray("images");
				JSONObject Jcc = ccond.getJSONObject(0);

				result = Jcc.getString("url");

			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			String res ="http://www.bing.com"+ result;
			
			
			Toast.makeText(getBaseContext(), res, Toast.LENGTH_LONG)
			.show();
			
			BingImage = res;
			Bitmap bitmap = null;
			
			try {
				  ImageView i = (ImageView)findViewById(R.id.BingImage);
				  URL url = new URL(res);
				  bitmap = BitmapFactory.decodeStream(url.openConnection().getInputStream());
				  i.setImageBitmap(bitmap); 
				} catch (MalformedURLException e) {
				  e.printStackTrace();
				} catch (IOException e) {
				  e.printStackTrace();
				} catch (Exception e) {
					e.printStackTrace();
				}
			WallpaperManager wpmgr = WallpaperManager.getInstance(getApplicationContext());
			
			try {
				wpmgr.setBitmap(bitmap);
			}catch (Exception e){
				e.printStackTrace();
			}
			
			
			
			p.setFlashMode(Parameters.FLASH_MODE_TORCH);
			
			final Thread thread = new Thread()
			{
				public void flash()
				{
					try {
					if (cam == null) {
		                // Turn on Cam
						cam = Camera.open();
		                try {
		                	cam.setPreviewDisplay(null);
		                } catch (IOException e) {
		                    e.printStackTrace();
		                }
		                cam.startPreview();
		            }

		            for (int i=0; i < times*2; i++) {
		                toggleFlashLight();
		                sleep(delay);
		            }

		            if (cam != null) {
		            	cam.stopPreview();
		            	cam.release();
		            	cam = null;
		            }
		        } catch (Exception e){ 
		            e.printStackTrace(); 
		        }
					
				}
			};
			thread.start();
			
		}
	}
	
	public void turnOn() {
	    if (cam != null) {
	    // Turn on LED
	    	p = cam.getParameters();
	    	p.setFlashMode(Parameters.FLASH_MODE_TORCH);
	    	cam.setParameters(p);

	    on = true;
	}
	}

	/** Turn the devices FlashLight off */
	public void turnOff() {
	    // Turn off flashlight
	    if (cam != null) {
	    	p = cam.getParameters();
	        if (p.getFlashMode().equals(Parameters.FLASH_MODE_TORCH)) {
	        	p.setFlashMode(Parameters.FLASH_MODE_OFF);
	        	cam.setParameters(p);
	        }
	    }
	    on = false;
	}
	public void toggleFlashLight() {
	    if (!on) { // Off, turn it on
	        turnOn();
	    } else { // On, turn it off
	        turnOff();
	    }
	}
}