package com.paul.t29ideagarden2;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.LocationSource;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.UiSettings;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.Marker;
import com.amap.api.maps2d.model.MarkerOptions;
import com.paul.t29ideagarden2.atys.EditUserInfoActivity;
import com.paul.t29ideagarden2.atys.HelpActivity;
import com.paul.t29ideagarden2.atys.LoginActivity;
import com.paul.t29ideagarden2.atys.MyFieldActivity;
import com.paul.t29ideagarden2.atys.NewIdeaActivity;
import com.paul.t29ideagarden2.atys.SeeIdeaActivity;
import com.paul.t29ideagarden2.atys.ToolsActivity;
import com.paul.t29ideagarden2.atys.TrendingActivity;
import com.paul.t29ideagarden2.bean.Idea;
import com.paul.t29ideagarden2.bean.User;
import com.paul.t29ideagarden2.util.Constants;
import com.paul.t29ideagarden2.util.PermissionUtil;

import java.io.FileNotFoundException;
import java.util.List;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobGeoPoint;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

import static com.paul.t29ideagarden2.util.Constants.*;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, LocationSource, AMapLocationListener {

    public static final String TAG = "MainActivity Test";
    private static AMap aMap;
    OnLocationChangedListener mListener;
    AMapLocationClient mlocationClient;
    AMapLocationClientOption mLocationOption;
    MapView mMapView = null;
    double latitude, longitude;
    Dialog dialog;
    AMap.OnInfoWindowClickListener listener = new AMap.OnInfoWindowClickListener() {

        @Override
        public void onInfoWindowClick(Marker marker) {

            String ideaObjectId = marker.getSnippet();
            Intent intent = new Intent(MainActivity.this, SeeIdeaActivity.class);
            intent.putExtra("ideaObjectId", ideaObjectId);
            Log.d(TAG, ">>>>>>>>>>>>onInfoWindowClick,ready for see idea,and the Obj id is:" + ideaObjectId);
            startActivity(intent);

        }
    };
    private UiSettings mUiSettings;//定义一个UiSettings对象
    private AMap.OnMapClickListener onMapClickListener;
    private ImageButton userImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Bmob.initialize(this, "6b5c101fa6f907f9f68e35d24b2344d4");


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int i = ContextCompat.checkSelfPermission(this, permissions[0]);
            if (i != PackageManager.PERMISSION_GRANTED) {
                PermissionUtil.showDialogTipUserRequestPermission(this);
            }
        }


        //在activity执行onCreate时执行mMapView.onCreate(savedInstanceState)，创建地图
        mMapView = (MapView) findViewById(R.id.map);
        mMapView.onCreate(savedInstanceState);
        Toast.makeText(this, "侦测花儿开始啦", Toast.LENGTH_SHORT).show();
        init();
    }

    private void init() {
       /*获取当前用户*/
        BmobUser user = BmobUser.getCurrentUser(User.class);
        if (user != null) {
            // 允许用户使用应用
            aMap = mMapView.getMap();
            mUiSettings = aMap.getUiSettings();
            mUiSettings.setMyLocationButtonEnabled(true); //显示默认的定位按钮
            aMap.setMyLocationEnabled(true);// 可触发定位并显示当前位置
            NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
            navigationView.setNavigationItemSelectedListener(this);
            View hView = navigationView.getHeaderView(0);
            TextView nav_user = (TextView) hView.findViewById(R.id.user_name);
            nav_user.setText(user.getUsername());
            userImage = hView.findViewById(R.id.user_image);

            // 设置定位监听
            aMap.setLocationSource(this);
            aMap.setMyLocationEnabled(true);
            aMap.moveCamera(CameraUpdateFactory.zoomTo(17));

        } else {
            //缓存用户对象为空时， 可打开用户注册界面…
            Intent intent = new Intent(this, LoginActivity.class);
            startActivityForResult(intent, ACTIVITY_REQUEST_FOR_REGISTER_USER);

        }
        Log.i(TAG, "init: ");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，销毁地图
        mMapView.onDestroy();
        if (null != mlocationClient) {
            mlocationClient.onDestroy();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView.onResume ()，重新绘制加载地图
        mMapView.onResume();
        findNear();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView.onPause ()，暂停地图的绘制
        mMapView.onPause();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //在activity执行onSaveInstanceState时执行mMapView.onSaveInstanceState (outState)，保存地图当前的状态
        mMapView.onSaveInstanceState(outState);
    }

    //tt: deal with 返回按钮事件，保证首先关闭抽屉
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

    public void toast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        final int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_search) {
            Log.d("Start Searching", ">>>>>>>>>>>>>>>>>enter search");
            toast("正在找你附近的思想");
            findNear();
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        Intent intent = null;
        if (id == R.id.nav_field) {
            intent = new Intent(this, MyFieldActivity.class);
            startActivity(intent);

        } else if (id == R.id.nav_add_new) {
            intent = new Intent(this, NewIdeaActivity.class);
            intent.putExtra("latitude", latitude);
            intent.putExtra("longitude", longitude);
            startActivity(intent);
        } else if (id == R.id.nav_trending) {
            intent = new Intent(this, TrendingActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_tools) {
            intent = new Intent(this, ToolsActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_help) {
            intent = new Intent(this, HelpActivity.class);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void activate(OnLocationChangedListener listener) {
        mListener = listener;
        Toast.makeText(this, "正在定位中", Toast.LENGTH_SHORT).show();
        if (mlocationClient == null) {
            //初始化定位
            mlocationClient = new AMapLocationClient(this);
            //初始化定位参数
            mLocationOption = new AMapLocationClientOption();
            //设置定位回调监听
            mlocationClient.setLocationListener(this);
            //设置为高精度定位模式
            mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
            //设置定位参数
            mlocationClient.setLocationOption(mLocationOption);
            // 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
            // 注意设置合适的定位时间的间隔（最小间隔支持为2000ms），并且在合适时间调用stopLocation()方法来取消定位请求
            // 在定位结束后，在合适的生命周期调用onDestroy()方法
            // 在单次定位情况下，定位无论成功与否，都无需调用stopLocation()方法移除请求，定位sdk内部会移除
            mlocationClient.startLocation();//启动定位
        }
    }

    /**
     * 停止定位
     */
    @Override
    public void deactivate() {
        mListener = null;
        if (mlocationClient != null) {
            mlocationClient.stopLocation();
            mlocationClient.onDestroy();
        }
        mlocationClient = null;
    }

    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        if (mListener != null && aMapLocation != null) {
            if (aMapLocation != null
                    && aMapLocation.getErrorCode() == 0) {
                mListener.onLocationChanged(aMapLocation);// 显示系统小蓝点
                latitude = aMapLocation.getLatitude();
                longitude = aMapLocation.getLongitude();
            } else {
                String errText = "定位失败," + aMapLocation.getErrorCode() + ": " + aMapLocation.getErrorInfo();
                Log.e("AmapErr", errText);
                Toast.makeText(this, "定位失败" + aMapLocation.getErrorInfo(), Toast.LENGTH_SHORT).show();

            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ACTIVITY_REQUEST_FOR_REGISTER_USER) {
            init();
        } else if (requestCode == ACTIVITY_REQUEST_FOR_SETTING_CODE) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                int permission_index = 0;
                for (String permission : permissions) {
                    int i = ContextCompat.checkSelfPermission(this, permissions[permission_index]);
                    if (i != PackageManager.PERMISSION_GRANTED) {
                        //tt: 未获得权限，再次从头申请
                        PermissionUtil.showDialogTipUserRequestPermission(this);
                    } else {
                        //tt:权限已经获得
                        if (dialog != null && dialog.isShowing()) {
                            dialog.dismiss();
                        }
                        Toast.makeText(this, "权限已经获得", Toast.LENGTH_SHORT).show();
                    }
                }

            }
        }

    }

    public void editUserInfo(View view) {
        /*获取当前用户*/
        BmobUser user = User.getCurrentUser();
        if (user != null) {
            String userId = user.getObjectId();
            Intent intent = new Intent(this, EditUserInfoActivity.class);
            intent.putExtra("userId", userId);
            startActivity(intent);
        } else {
            toast("诶?看不了个人信息，不会吧？bug啊！");
        }
    }

    public void findNear() {
        BmobQuery<Idea> bmobQuery = new BmobQuery<>();
        bmobQuery.addWhereNear("gps", new BmobGeoPoint(longitude, latitude));
        bmobQuery.setLimit(20);
        Log.d("Almost Searching", ">>>>>>>>>>>>>>>>>ready for search");
        bmobQuery.findObjects(new FindListener<Idea>() {
            @Override
            public void done(List<Idea> list, BmobException e) {
                if (e == null) {
                    double tempLatitude, tempLongitude;
                    LatLng latLng;
                    aMap = mMapView.getMap();
                    Idea tempIdea;
                    for (int i = 0; i < list.size(); i++) {
                        tempIdea = list.get(i);
                        tempLatitude = list.get(i).getGps().getLatitude();
                        tempLongitude = list.get(i).getGps().getLongitude();
                        latLng = new LatLng(tempLatitude, tempLongitude);
                        aMap.addMarker(new MarkerOptions().position(latLng).title(list.get(i).getTitle()).snippet(list.get(i).getObjectId()));
                    }
                    if (list.size() == 0) {
                        toast("没找到呢，你附近没人投放花朵，或者再用放大镜侦查吧");
                    } else {
                        toast("找到了一些附近人种的" + list.size() + "个思想花，用右下角+-缩放地图看看吧");
                    }
                    aMap.setOnInfoWindowClickListener(listener);
                }
            }
        });
    }

    //tt: 获取权限的一些代码
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                int permission_index = 0;
                for (int permissionResult : grantResults) {
                    if (permissionResult != PackageManager.PERMISSION_GRANTED) {
                        boolean b = shouldShowRequestPermissionRationale(permissions[permission_index]);
                        if (b) {
                            showTipToUserToAppSetting();
                        } else {
                            finish();
                        }
                    } else {
                        Toast.makeText(this, "获取权限成功", Toast.LENGTH_SHORT).show();
                    }
                    permission_index++;
                }
            }
        }
    }

    private void showTipToUserToAppSetting() {
        dialog = new AlertDialog.Builder(this)
                .setTitle("一些权限还没获取")
                .setMessage("即将前往设置页面，请在那里开启必要的权限")
                .setPositiveButton("前往", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        goToAppSetting();
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                })
                .setCancelable(false)
                .show();
    }

    private void goToAppSetting() {
        Intent intent = new Intent();
        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", getPackageName(), null);
        intent.setData(uri);
        startActivityForResult(intent, ACTIVITY_REQUEST_FOR_SETTING_CODE);
    }


}
