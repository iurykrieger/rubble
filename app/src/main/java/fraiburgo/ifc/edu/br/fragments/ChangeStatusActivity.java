package fraiburgo.ifc.edu.br.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import fraiburgo.ifc.edu.br.controllers.StatusController;
import fraiburgo.ifc.edu.br.rubble.R;

public class ChangeStatusActivity extends Activity {

    private Spinner spn_status;
    private int idPostagem;
    private int idUsuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_status);
        spn_status = (Spinner) findViewById(R.id.spn_change_status);
        String[] arrayStatus = {"Não Resolvido", "Em Andamento", "Resolvido"};
        ArrayAdapter<String> adapterStatus = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, arrayStatus);
        spn_status.setAdapter(adapterStatus);

        SharedPreferences sharedpreferences = getSharedPreferences("session", Context.MODE_PRIVATE);
        idUsuario = sharedpreferences.getInt("idUsuario", 0);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            idPostagem = extras.getInt("idPostagem");
        }

        Button btn_change = (Button) findViewById(R.id.btn_send_bugreport);
        btn_change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String status = spn_status.getSelectedItem().toString();
                StatusController sc = new StatusController(ChangeStatusActivity.this,status,idPostagem,idUsuario);
                String url_status = getString(R.string.server_ip) + "/" + getString(R.string.application_name) + "/StatusServlet";
                sc.execute(url_status);
            }
        });
    }

}
