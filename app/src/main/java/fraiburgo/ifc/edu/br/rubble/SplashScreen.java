package fraiburgo.ifc.edu.br.rubble;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;

import com.facebook.FacebookSdk;

import fraiburgo.ifc.edu.br.controllers.LoginController;


public class SplashScreen extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_splash_screen);
        FacebookSdk.sdkInitialize(getApplicationContext());

        //Start the activity
        SharedPreferences sharedpreferences = SplashScreen.this.getSharedPreferences("session", Context.MODE_PRIVATE);
        if (sharedpreferences.contains("email") && sharedpreferences.contains("password")) {
            String email = sharedpreferences.getString("email", "");
            String password = sharedpreferences.getString("password", "");
            String url_login = getString(R.string.server_ip) + "/" + getString(R.string.application_name) + "/LoginServlet";
            LoginController login = new LoginController(SplashScreen.this, this, email, password);
            login.execute(url_login);
        } else {
            Intent intent = new Intent(SplashScreen.this, Login.class);
            SplashScreen.this.startActivity(intent);
            SplashScreen.this.finish();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_splash_screen, menu);
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
}
