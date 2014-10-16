package com.example.mobilesecuritylab_tnm031;

import com.example.mobilesecuritylab.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;

import java.security.KeyStore;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.SSLContext;
import java.net.URL;
import javax.net.ssl.HttpsURLConnection;
import java.io.InputStream;
public class MainActivity extends Activity {
	
	public ProgressDialog loadingdialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		Button button1 = (Button) findViewById(R.id.button1);
		button1.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				//showToast("Button 1 Clicked");
				buttonAction("https://www.liu.se/", 0);
			}
		});

		Button button2 = (Button) findViewById(R.id.button2);
		button2.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				//showToast("Button 2 Clicked");
				buttonAction("https://tal-front.itn.liu.se/", 0);
			}
		});
		
		Button button3 = (Button) findViewById(R.id.button3);
		button3.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				//showToast("Button 3 Clicked");
				buttonAction("https://tal-front.itn.liu.se:4008/", 1);
			}
		});
		
		Button button4 = (Button) findViewById(R.id.button4);
		button4.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				//showToast("Button 4 Clicked");
				buttonAction("https://tal-front.itn.liu.se:4018/", 1);
			}
		});
	}

	
	// Called when the user clicks button
	public void buttonAction(final String s_url, final int btnNr){
		loadingdialog = ProgressDialog.show(MainActivity.this, "","Loading...",true);

		Thread thread = new Thread(new Runnable(){
			@Override
			public void run() {
				try {
                    if(btnNr == 1) {
                        //Check with the certificate
                        setUpConnection(s_url);
                    }
                    else {
                        //Skapa Http-connection
                        HttpClient client = new DefaultHttpClient();
                        //HttpResponse utför en förfrågan att använda default context
                        HttpResponse response = client.execute(new HttpGet(s_url));

                        //Erhåll status från websidan
                        StatusLine sl = response.getStatusLine();

                        //Visa en alertruta med Http-status
                        showAlert(s_url, "HTTP Status: " + sl.getStatusCode());
                    }
				} catch (Exception e) {
					//Visa en alertruta med errormeddelande
					showAlert(s_url, "ERROR: " + e.getMessage());
					e.printStackTrace();
				}
				
				loadingdialog.dismiss();
			}
			});
		thread.start();
		
	}

    public void setUpConnection(String urlString){
        try {
            //Skapa truststore med vårt bouncy castle-certifikat
            KeyStore ts = KeyStore.getInstance("BKS");

            //Länkar in certifikatfilen och laddar upp den till truststore samt stänger strömmen
            InputStream is = getResources().openRawResource(R.raw.bccert);
            //Inget lösenord är satt, därav ""
            ts.load(is, "tnm031".toCharArray());
            is.close();

            //Skapa en trustmanager som litar på certifikatet
            String tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
            TrustManagerFactory tmf = TrustManagerFactory.getInstance(tmfAlgorithm);
            tmf.init(ts);

            //Skapa ett SSLContext som använder sig av trustmanager
            SSLContext context = SSLContext.getInstance("TLS");
            context.init(null, tmf.getTrustManagers(), null);

            //Skapa URL connection med socket factory som använder sig av vårt SSLContext
            URL url = new URL(urlString);
            HttpsURLConnection urlConnection = (HttpsURLConnection) url.openConnection();
            urlConnection.setSSLSocketFactory(context.getSocketFactory());

            //En liten alertbox som meddelar användaren responskoden samt responsmeddelande :)
            showAlert("Response", "Response code: " + urlConnection.getResponseCode() +
                      "\nResponse message: " + urlConnection.getResponseMessage());
        }
        catch (Exception ex) {
            showAlert(urlString, "Error: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
		
	public void showToast(final String toast)
	{
	    runOnUiThread(new Runnable() {
	        @Override
			public void run()
	        {
	            Toast.makeText(MainActivity.this, toast, Toast.LENGTH_SHORT).show();
	        }
	    });
	}
	
	public void showAlert(final String header, final String message)
	{
		runOnUiThread(new Runnable() {
			@Override
			public void run()
	        {
	        	AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
	    		alertDialog.setTitle(header);
	    		alertDialog.setMessage(message);
	    		alertDialog.setCancelable(false);
	    		alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
	                @Override
					public void onClick(DialogInterface dialog, int id) {
	                    dialog.cancel();
	                }
	            });
	    		alertDialog.show();
	        }
		});
	}
}
