package com.paul.t29ideagarden2.views;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.Marker;
import com.amap.api.maps2d.model.MarkerOptions;
import com.paul.t29ideagarden2.MainActivity;
import com.paul.t29ideagarden2.MyApplication;
import com.paul.t29ideagarden2.R;
import com.paul.t29ideagarden2.atys.NewIdeaActivity;
import com.paul.t29ideagarden2.atys.SeeIdeaActivity;
import com.paul.t29ideagarden2.atys.ToolsActivity;
import com.paul.t29ideagarden2.bean.Idea;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobGeoPoint;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

import static com.paul.t29ideagarden2.R.layout.chose_dialog_fragment;

/**
 * Created by paul on 4/20/17.
 */

public class LoginDialogFragment extends DialogFragment {

    double clickLatitude=39.8965,clickLongitude=116.4074;
    private EditText et_latitude,et_longitude;
    MapView mapView;
    AMap aMap;

    Context context = getActivity();
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(chose_dialog_fragment, null);
        mapView = (MapView) view.findViewById(R.id.chose_map_in_fragment);
        mapView.onCreate(savedInstanceState);
        et_latitude = (EditText) view.findViewById(R.id.id_latitude);
        et_longitude = (EditText) view.findViewById(R.id.id_longitude);
        aMap = mapView.getMap();
        aMap.setOnMapClickListener(onMapClickListener);
        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        builder.setView(view)
                // Add action buttons
                .setPositiveButton("空中侦测",
                        new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialog, int id)
                            {
                                findNear(clickLatitude,clickLongitude);
                            }
                        }).setNegativeButton("空投思想",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent =new Intent(getActivity(), NewIdeaActivity.class);
                                intent.putExtra("latitude",clickLatitude);
                                intent.putExtra("longitude",clickLongitude);
                                startActivity(intent);
                    }
                }).setCancelable(false);
        return builder.create();
    }
    AMap.OnMapClickListener onMapClickListener = new AMap.OnMapClickListener() {
        @Override
        public void onMapClick(LatLng latLng) {
            clickLatitude= latLng.latitude;
            clickLongitude= latLng.longitude;
            aMap.clear();
            aMap.addMarker(new MarkerOptions().position(latLng));
            et_latitude.setText(clickLatitude+"");
            et_longitude.setText(clickLongitude+"");
        }
    };

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    public void findNear(double clickLatitude,double clickLongitude){
        BmobQuery<Idea> bmobQuery = new BmobQuery<>();
        bmobQuery.addWhereNear("gps", new BmobGeoPoint(clickLongitude,clickLatitude));
        bmobQuery.setLimit(20);
        Log.d("Almost Searching", ">>>>>>>>>>>>>>>>>ready for search");
        bmobQuery.findObjects(new FindListener<Idea>() {
            @Override
            public void done(List<Idea> list, BmobException e) {
                if (e == null){
                    if (list.size() == 0){
                        toast("刚才视野内没人投放花朵");
                    }else {
                        toast("刚才侦测到"+list.size()+"个思想花");
                    }

                }
            }
        });
    }
    public void toast(String msg){
        Toast.makeText(MyApplication.getContext(), msg, Toast.LENGTH_SHORT).show();
    }



}