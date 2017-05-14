package fraiburgo.ifc.edu.br.rubble;

import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import fraiburgo.ifc.edu.br.controllers.CidadeController;
import fraiburgo.ifc.edu.br.controllers.EstadoController;
import fraiburgo.ifc.edu.br.controllers.RegisterController;
import fraiburgo.ifc.edu.br.model.Cidade;
import fraiburgo.ifc.edu.br.model.Estado;

import static fraiburgo.ifc.edu.br.utils.IntentUtils.startAlertDialog;
import static fraiburgo.ifc.edu.br.utils.SecurePassword.getSecurePassword;


public class Register extends Activity {

    private String url_cidade;
    private String url_register;
    private Spinner spn_estados;
    private Spinner spn_cidades;
    private EstadoController ec;
    private CidadeController cc;

    @TargetApi(Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_register);

        String url_estado = getString(R.string.server_ip) + "/" + getString(R.string.application_name) + "/EstadoServlet";
        url_cidade = getString(R.string.server_ip) + "/" + getString(R.string.application_name) + "/CidadeServlet";
        url_register = getString(R.string.server_ip) + "/" + getString(R.string.application_name) + "/RegisterServlet";

        spn_estados = (Spinner) findViewById(R.id.spn_estados);
        spn_cidades = (Spinner) findViewById(R.id.spn_cidades);

        ec = new EstadoController(Register.this, spn_estados);
        cc = new CidadeController(Register.this, spn_cidades, 1);
        ec.execute(url_estado);
        cc.execute(url_cidade);

        spn_estados.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                cc = new CidadeController(Register.this, spn_cidades, getSelectedEstado().getIdEstado().intValue());
                cc.execute(url_cidade);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        Button registrar = (Button) findViewById(R.id.btn_registrar);
        registrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int idCidade = getSelectedCidade().getIdCidade().intValue();
                EditText editTextName = (EditText) findViewById(R.id.et_nome);
                String name = editTextName.getText().toString();
                EditText editTextEmail = (EditText) findViewById(R.id.et_email);
                String email = editTextEmail.getText().toString();
                EditText editTextPassword = (EditText) findViewById(R.id.et_senha);
                String pass = editTextPassword.getText().toString();
                String securePassword = getSecurePassword(pass);
                if(!name.isEmpty() && !email.isEmpty() && !pass.isEmpty()){
                    if(email.contains("@") && isEmailValid(email)){
                        RegisterController rc = new RegisterController(Register.this, name, email, securePassword, idCidade);
                        rc.execute(url_register);
                        Register.this.finish();
                    }else{
                        startAlertDialog("Email inválido", "O email digitado é inválido, utilize um email existente!", Register.this);
                    }
                }else{
                    startAlertDialog("Campos vazios!","Preencha todos os campos para efetuar seu cadastro!",Register.this);
                }

            }
        });
    }

    boolean isEmailValid(CharSequence email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public Estado getSelectedEstado() {
        Estado e = new Estado();
        try {
            e = ec.getEstadosList().get(spn_estados.getSelectedItemPosition());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return e;
    }

    public Cidade getSelectedCidade() {
        Cidade c = new Cidade();
        try {
            c = cc.getCidadesList().get(spn_cidades.getSelectedItemPosition());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return c;
    }
}
