package network.ubic.ubic;

import android.net.Uri;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import network.ubic.ubic.Fragments.BalanceFragment;
import network.ubic.ubic.Fragments.MyUBIFragment;
import network.ubic.ubic.Fragments.ReceiveFragment;
import network.ubic.ubic.Fragments.RegisterPassportFragment;
import network.ubic.ubic.Fragments.SendFragment;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
                   BalanceFragment.OnFragmentInteractionListener,
                   SendFragment.OnFragmentInteractionListener,
                   ReceiveFragment.OnFragmentInteractionListener,
                   MyUBIFragment.OnFragmentInteractionListener,
                   RegisterPassportFragment.OnFragmentInteractionListener,
                   OnGetBalanceCompleted {

    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("native-lib");
    }

    @Override
    public void onFragmentInteraction(Uri uri) {
        //
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        PrivateKeyStore privateKeyStore = new PrivateKeyStore();

        try {
            String privateKeyStr = new String(privateKeyStore.getPrivateKey(getBaseContext()), "ASCII");

            System.out.println("privateKeyStore.getPrivateKey(getBaseContext())");
            System.out.println(privateKeyStr);
        } catch (Exception e) {
        }

        this.goToNavBalance();

        //new GetBalance(this).execute();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
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

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_balance) {
            goToNavBalance();
        } else if (id == R.id.nav_my_ubi) {
            goToNavMyUbi();
        } else if (id == R.id.nav_receive) {
            goToNavReceive();
        } else if (id == R.id.nav_send) {
            goToNavSend();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void goToNavRegisterPassport() {
        RegisterPassportFragment rpFragment = new RegisterPassportFragment();

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.contentContainer, rpFragment);
        transaction.commit();
    }

    public void goToNavBalance() {
        BalanceFragment bf = new BalanceFragment();

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.contentContainer, bf);
        transaction.commit();

        new GetBalance(this).execute();
    }

    public void goToNavMyUbi() {
        MyUBIFragment myUbiF = new MyUBIFragment();

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.contentContainer, myUbiF);
        transaction.commit();
    }

    public void goToNavSend() {
        SendFragment sendF = new SendFragment();

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.contentContainer, sendF);
        transaction.commit();
    }

    public void goToNavReceive() {
        ReceiveFragment receiveF = new ReceiveFragment();

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.contentContainer, receiveF);
        transaction.commit();
    }


    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
    public native String stringFromJNI();

    public void OnGetBalanceCompleted(HashMap<Integer, BigInteger> balanceMap) {

        ListView balanceListView = findViewById(R.id.balance_list_view);
        List<String> balanceList = new ArrayList<String>();

        //System.out.println(key + " : " + value);
        for (HashMap.Entry<Integer, BigInteger> entry : balanceMap.entrySet())
        {
            balanceList.add(entry.getKey() + " : " + entry.getValue());
        }

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_list_item_1,
                balanceList );

        balanceListView.setAdapter(arrayAdapter);
    }
}