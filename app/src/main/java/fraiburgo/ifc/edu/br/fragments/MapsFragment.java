package fraiburgo.ifc.edu.br.fragments;

import android.app.Activity;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ToggleButton;

import com.androidmapsextensions.ClusteringSettings;
import com.androidmapsextensions.GoogleMap;
import com.androidmapsextensions.Marker;
import com.androidmapsextensions.MarkerOptions;
import com.androidmapsextensions.SupportMapFragment;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;

import fraiburgo.ifc.edu.br.rubble.R;
import fraiburgo.ifc.edu.br.utils.BuildMapMarkers;
import fraiburgo.ifc.edu.br.utils.MyLocation;

public class MapsFragment extends Fragment {

    private final int requestCode = 1;
    private SupportMapFragment fragment;
    private GoogleMap map;
    private View view;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (view != null) {
            ViewGroup parent = (ViewGroup) view.getParent();
            if (parent != null)
                parent.removeView(view);
        }
        try {
            view = inflater.inflate(R.layout.fragment_maps, container, false);
        } catch (InflateException e) {
            e.printStackTrace();
        }
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        FragmentManager fm = getChildFragmentManager();
        fragment = (SupportMapFragment) fm.findFragmentById(R.id.map);
        if (fragment == null) {
            fragment = SupportMapFragment.newInstance();
            fm.beginTransaction().replace(R.id.map, fragment).commit();
        }
        setUpMap();
    }

    @Override
    public void onResume() {
        super.onResume();
        setUpMap();
        if (map == null) {
            map = fragment.getExtendedMap();
        }
    }

    private void setUpMap() {
        if (map == null) {
            map = fragment.getExtendedMap();
            final ToggleButton tb_maptype = (ToggleButton) view.findViewById(R.id.btn_maptype);
            tb_maptype.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (tb_maptype.isChecked()) {
                        map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                    } else {
                        map.setMapType(GoogleMap.MAP_TYPE_HYBRID);
                    }
                }
            });
            tb_maptype.refreshDrawableState();
            //CONFIGURAÇÕES DE INTERFACE
            map.setMyLocationEnabled(true);
            map.setMapType(GoogleMap.MAP_TYPE_HYBRID);
            map.getUiSettings().setZoomControlsEnabled(true);
            map.getUiSettings().setMapToolbarEnabled(true);
            map.setClustering(new ClusteringSettings()
                    .enabled(true)
                    .addMarkersDynamically(true));
            map.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
                @Override
                public void onMapLongClick(LatLng latLng) {
                    startPostActivity(latLng);
                }
            });
            map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                @Override
                public boolean onMarkerClick(Marker marker) {
                    if (!marker.isCluster()) {
                        startBuildPostActivity(Integer.parseInt(marker.getTitle()));
                        if (marker.isInfoWindowShown()) {
                            marker.hideInfoWindow();
                        }
                    }
                    return true;
                }
            });
            map.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {
                @Override
                public void onCameraChange(CameraPosition cameraPosition) {
                    if (map.getCameraPosition().zoom > 10) {
                        String url_post = getString(R.string.server_ip) + "/" + getString(R.string.application_name) + "/PostagemServlet";
                        BuildMapMarkers bmm = new BuildMapMarkers(map);
                        bmm.execute(url_post);
                    }
                }
            });

            //CONFIGURAÇÕES DE POSIÇÃO
            MyLocation.LocationResult locationResult = new MyLocation.LocationResult() {
                @Override
                public void gotLocation(Location location) {
                    LatLng latLang = new LatLng(location.getLatitude(), location.getLongitude());
                    CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLang, 17);
                    map.animateCamera(cameraUpdate);
                }
            };
            MyLocation myLocation = new MyLocation();
            myLocation.getLocation(this.getActivity(), locationResult);
        }
    }

    private void startPostActivity(LatLng latLng) {
        Intent intent = new Intent(this.getActivity(), PostActivity.class);
        intent.putExtra("LATITUDE", latLng.latitude);
        intent.putExtra("LONGITUDE", latLng.longitude);
        this.startActivityForResult(intent, requestCode);
    }

    private void startBuildPostActivity(int idPostagem) {
        Intent intent = new Intent(this.getActivity(), BuildPostActivity.class);
        intent.putExtra("idPostagem", idPostagem);
        this.startActivity(intent);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == this.requestCode) {
            if (resultCode == Activity.RESULT_OK) {
                double latitude = data.getDoubleExtra("LATITUDE", 0);
                double longitude = data.getDoubleExtra("LONGITUDE", 0);
                int idPostagem = data.getIntExtra("idPostagem", 0);
                MarkerOptions m = new MarkerOptions().position(new LatLng(latitude, longitude)).title(String.valueOf(idPostagem))
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
                map.addMarker(m).hideInfoWindow();
            }
        }
    }
}
