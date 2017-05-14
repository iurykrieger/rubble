package fraiburgo.ifc.edu.br.initalizers;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.google.android.gms.tagmanager.Container;

import fraiburgo.ifc.edu.br.rubble.R;

/**
 * Created by iuryk on 16/07/2015.
 */
public class ReloadInitializer {

    private Activity activity;
    private View view;
    private ViewGroup viewGroup;
    private Container container;
    private Context context;

    public ReloadInitializer(Activity activity) {
        this.activity = activity;
    }

    public ReloadInitializer(View view, ViewGroup viewGroup, Context context) {
        this.view = view;
        this.viewGroup = viewGroup;
        this.context = context;
    }

    public void initializeViewReload() {

        /*LayoutInflater inflater = LayoutInflater.from(context);
        View inflatedLayout = inflater.inflate(R.layout.layout_load, viewGroup, false);
        int index = viewGroup.indexOfChild(view);
        viewGroup.removeView(view);
        viewGroup.addView(inflatedLayout,index);*/
        /*RelativeLayout rl = (RelativeLayout) view.findViewById(R.id.rl_reload);
        rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                view.invalidate();
            }
        });*/
    }

    public void initializeReload() {
        activity.setContentView(R.layout.layout_reload);

        RelativeLayout rl = (RelativeLayout) activity.findViewById(R.id.rl_reload);
        rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.startActivity(activity.getIntent());
                activity.finish();
            }
        });
    }

}
