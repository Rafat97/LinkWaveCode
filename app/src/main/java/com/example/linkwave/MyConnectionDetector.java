package com.example.linkwave;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.Settings;
import android.widget.Toast;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;

public class MyConnectionDetector extends AsyncTask<String,Void,Integer> {
    private Context _context;


    public MyConnectionDetector(Context context){
        this._context = context;
    }

    /**
     * Checking for all possible internet providers
     * **/
    public boolean isConnectingToInternet(){
        ConnectivityManager connectivity = (ConnectivityManager) _context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null)
        {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null)
                for (int i = 0; i < info.length; i++)
                    if (info[i].getState() == NetworkInfo.State.CONNECTED)
                    {
                        return true;
                    }

        }
        return false;
    }

    public boolean internet(){
        boolean value = false;
        try {
            Socket soc = new Socket();
            SocketAddress adds = new InetSocketAddress("8.8.8.8",53);
            soc.connect(adds,1500);
            soc.close();
            value = true;
        }catch (Exception e){
            value = false;
        }
        return value;
    }

    @Override
    protected Integer doInBackground(String... strings) {
        Integer result = 0;
        try {
            Socket socket=new Socket();
            SocketAddress socketAddress=new InetSocketAddress("8.8.8.8",53);
            socket.connect(socketAddress,1500);
            socket.close();
            result=1;
        } catch (IOException e) {
            e.printStackTrace();
            result=0;
        }

        return result;
    }

    @Override
    protected void onPostExecute(Integer integer) {
        if (isConnectingToInternet())
        {
            if (integer==1)
            {
                Toast.makeText(_context, "  internet available ", Toast.LENGTH_SHORT).show();
                //MainActivity.txtLat.setText("Internet Have");

            }

            if(integer==0)
            {
                Toast.makeText(_context, " No internet available ", Toast.LENGTH_SHORT).show();
                //MainActivity.txtLat.setText("Internet Not Have");


                AlertDialog alertDialog = new AlertDialog.Builder(_context).create();
                alertDialog.setTitle("Internet Check ");
                alertDialog.setMessage("Please Check Internet in Your Setting !!!");
                alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();


                                Intent intent = new Intent();
                                intent.setAction(Settings.ACTION_WIFI_SETTINGS);
                                Uri uri = Uri.fromParts("package",BuildConfig.APPLICATION_ID, null);
                                intent.setData(uri);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                _context.startActivity(intent);
                            }
                        });
                alertDialog.show();

            }
        }
        else
        {
            Toast.makeText(_context, " No internet available ", Toast.LENGTH_SHORT).show();


            AlertDialog alertDialog = new AlertDialog.Builder(_context).create();
            alertDialog.setTitle("Internet Check ");
            alertDialog.setMessage("Please Check Internet in Your Setting !!!");
            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();


                            Intent intent = new Intent();
                            intent.setAction(Settings.ACTION_WIFI_SETTINGS);
                            Uri uri = Uri.fromParts("package",BuildConfig.APPLICATION_ID, null);
                            intent.setData(uri);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            _context.startActivity(intent);
                        }
                    });
            alertDialog.show();
        }
        super.onPostExecute(integer);
    }
}
