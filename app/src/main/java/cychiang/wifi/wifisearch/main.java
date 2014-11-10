package cychiang.wifi.wifisearch;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;


import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;
import java.util.Collections;
import java.util.Comparator;
import java.lang.String;
import java.lang.Object;

public class main extends Activity {
    WifiManager wifi;
    ListView listView;
    TextView textStatus;
    Button buttonScan;
    int size = 0;
    List<ScanResult> results;
    ArrayList arraylist = new ArrayList();
    SimpleAdapter adapter;

    private static final Map wifichannel = new HashMap();
    static {
        wifichannel.put("2412", "2.4G Ch01");
        wifichannel.put("2417", "2.4G Ch02");
        wifichannel.put("2422", "2.4G Ch03");
        wifichannel.put("2427", "2.4G Ch04");
        wifichannel.put("2432", "2.4G Ch05");
        wifichannel.put("2437", "2.4G Ch06");
        wifichannel.put("2442", "2.4G Ch07");
        wifichannel.put("2447", "2.4G Ch08");
        wifichannel.put("2452", "2.4G Ch09");
        wifichannel.put("2457", "2.4G Ch10");
        wifichannel.put("2462", "2.4G Ch11");
        wifichannel.put("2467", "2.4G Ch12");
        wifichannel.put("2472", "2.4G Ch13");
        wifichannel.put("2484", "2.4G Ch14");
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_layout);
        /* CYChiang */
        textStatus = (TextView) findViewById(R.id.textView2);
        buttonScan = (Button) findViewById(R.id.button1);
        listView = (ListView) findViewById(R.id.listView1);
        /* WiFi Status Check */
        wifi = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        if(wifi.isWifiEnabled() == false) {
            AlertDialog.Builder dialog = new AlertDialog.Builder(this);
            dialog.setTitle("Remind");
            dialog.setMessage("Your Wi-Fi is not enabled, enable?");
            dialog.setIcon(android.R.drawable.ic_dialog_info);
            dialog.setCancelable(false);
            dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    wifi.setWifiEnabled(true);
                    Toast.makeText(getApplicationContext(), "wifi is disabled..making it enabled", Toast.LENGTH_LONG).show();
                }
            });
            dialog.show();
        }

        /* Initial ListView */
        this.adapter = new SimpleAdapter(main.this, arraylist, R.layout.list, new String[] {"ssid","power","freq"}, new int[] {R.id.ssid, R.id.power, R.id.freq});
        listView.setAdapter(adapter);
        /* Used for scan */
        registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                results = wifi.getScanResults();
                size = results.size();

            }
        }, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
        /* Button Event for Scan WiFi */
        buttonScan.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                arraylist.clear();
                wifi.startScan();
                Toast.makeText(main.this, "Scanning..." + size, Toast.LENGTH_LONG).show();
                try {
                    size = size -1;
                    while(size >= 0) {
                        HashMap item = new HashMap();
                        item.put("ssid", results.get(size).SSID);
                        item.put("power", new String(results.get(size).level+" dBm"));
                        String wifichn = " ?G";
                        item.put("freq", wifichn);
                        arraylist.add(item);
                        size--;
                        adapter.notifyDataSetChanged();
                    }
                    Collections.sort(arraylist, new Comparator<HashMap>() {
                        @Override
                        public int compare(HashMap lhs, HashMap rhs) {
                            return ((String) lhs.get("power")).compareTo((String) rhs.get("power"));
                        }
                    });
                    textStatus.setText(arraylist.get(0).toString());
                } catch (Exception e) {

                }
            }
        });
        /* CYChiang */
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
}
