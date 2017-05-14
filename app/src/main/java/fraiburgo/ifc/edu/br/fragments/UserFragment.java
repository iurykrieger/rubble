package fraiburgo.ifc.edu.br.fragments;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import fraiburgo.ifc.edu.br.controllers.UserPostagemController;
import fraiburgo.ifc.edu.br.controllers.UsuarioController;
import fraiburgo.ifc.edu.br.rubble.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class UserFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_user, container, false);

        String url_user = getString(R.string.server_ip) + "/" + getString(R.string.application_name) + "/UsuarioServlet";
        String url_posts = getString(R.string.server_ip) + "/" + getString(R.string.application_name) + "/PostagemServlet";
        SharedPreferences sharedpreferences = this.getActivity().getSharedPreferences("session", Context.MODE_PRIVATE);
        int id = sharedpreferences.getInt("idUsuario", 0);
        UsuarioController uc = new UsuarioController(this.getActivity(), view, id);
        uc.execute(url_user);

        UserPostagemController upc = new UserPostagemController(this.getActivity(), view, id);
        upc.execute(url_posts);
        return view;
    }

}
