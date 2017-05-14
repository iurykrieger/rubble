package fraiburgo.ifc.edu.br.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import fraiburgo.ifc.edu.br.controllers.BugreportController;
import fraiburgo.ifc.edu.br.rubble.R;

public class BugReport extends Activity {

    private int idUsuario;
    private String actualStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bug_report);

        SharedPreferences sharedpreferences = getSharedPreferences("session", Context.MODE_PRIVATE);
        idUsuario = sharedpreferences.getInt("idUsuario", 0);

        Button btn_report = (Button) findViewById(R.id.btn_send_bugreport);
        btn_report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText et = (EditText) findViewById(R.id.et_bugreport);
                String report = et.getText().toString();
                String url_bugreport = getString(R.string.server_ip) + "/" + getString(R.string.application_name) + "/BugreportServlet";
                BugreportController bc = new BugreportController(BugReport.this ,BugReport.this , report, idUsuario);
                bc.execute(url_bugreport);
            }
        });
    }

}
