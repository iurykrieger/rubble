package fraiburgo.ifc.edu.br.rubble;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import fraiburgo.ifc.edu.br.controllers.LoginController;

import static fraiburgo.ifc.edu.br.utils.IntentUtils.startAlertDialog;
import static fraiburgo.ifc.edu.br.utils.SecurePassword.getSecurePassword;


public class Login extends Activity {

    private Login login;
    private String url_login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);
        login = this;
        url_login = getString(R.string.server_ip) + "/" + getString(R.string.application_name) + "/LoginServlet";
        Button btnLogin = (Button) findViewById(R.id.btn_login);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText et_email = (EditText) findViewById(R.id.et_email);
                EditText et_password = (EditText) findViewById(R.id.et_senha);
                String email = et_email.getText().toString();
                String pass = et_password.getText().toString();
                String securePass = getSecurePassword(pass);
                if(!email.isEmpty() && !pass.isEmpty()){
                    LoginController lc = new LoginController(Login.this, login, email, securePass);
                    lc.execute(url_login);
                }else{
                    startAlertDialog("Campos vazios!","Preencha o usuário e senha para efetuar seu login",Login.this);
                }
            }
        });
        Button btnRegister = (Button) findViewById(R.id.btn_registre_se);
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Start the register activity
                Intent intent = new Intent(Login.this, Register.class);
                Login.this.startActivity(intent);
            }
        });
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
