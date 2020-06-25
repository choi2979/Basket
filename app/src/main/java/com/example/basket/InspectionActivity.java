package com.example.basket;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.material.snackbar.Snackbar;

public class InspectionActivity extends AppCompatActivity {
    public static final String INSPECTION                           = "INSPECTION";
    //public String userNick                                        = "이정훈짱짱맨";
    public String userNick = null;

    public static final int PERMISSIONS_REQUEST_CODE = 2000;

    private Context applicationContext                              =   null;

    public static final String[] REQUIRED_PERMISSIONS = new String[]{   Manifest.permission.CAMERA,
                                                                        Manifest.permission.ACCESS_FINE_LOCATION
    };

    View mLayout = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(INSPECTION, "onCreate()");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inspection);
        mLayout = (View)findViewById(R.id.inspection);
        applicationContext = this.getApplicationContext();
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i(INSPECTION, "onStart()");
        if (getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
            // 1. 카메라 퍼미션과 외부 저장소 퍼미션을 가지고 있는지 체크합니다.
            int cameraPermission = ContextCompat.checkSelfPermission(applicationContext, Manifest.permission.CAMERA);
            int afLocationPermission = ContextCompat.checkSelfPermission(applicationContext, Manifest.permission.ACCESS_FINE_LOCATION);
            if ( cameraPermission == PackageManager.PERMISSION_GRANTED
                    && afLocationPermission == PackageManager.PERMISSION_GRANTED) {
                loginCheck();  // 3. 카메라 프리뷰 시작
            }else {  //2. 퍼미션 요청을 허용한 적이 없다면 퍼미션 요청이 필요합니다. 2가지 경우(3-1, 4-1)가 있습니다.
                // 3-1. 사용자가 퍼미션 거부를 한 적이 있는 경우에는
                if (ActivityCompat.shouldShowRequestPermissionRationale(InspectionActivity.this, REQUIRED_PERMISSIONS[0])
                        || ActivityCompat.shouldShowRequestPermissionRationale(InspectionActivity.this, REQUIRED_PERMISSIONS[1])) {
                    Snackbar.make(mLayout, "이 앱을 실행하려면 카메라와 외부 저장소 접근 권한이 필요합니다.",
                            Snackbar.LENGTH_INDEFINITE).setAction("확인", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            // 3-3. 사용자게에 퍼미션 요청을 합니다. 요청 결과는 onRequestPermissionResult에서 수신됩니다.
                            ActivityCompat.requestPermissions( InspectionActivity.this, REQUIRED_PERMISSIONS,
                                    PERMISSIONS_REQUEST_CODE);
                        }
                    }).show();
                } else {
                    // 4-1. 사용자가 퍼미션 거부를 한 적이 없는 경우에는 퍼미션 요청을 바로 합니다.
                    // 요청 결과는 onRequestPermissionResult에서 수신됩니다.
                    ActivityCompat.requestPermissions( InspectionActivity.this, REQUIRED_PERMISSIONS,
                            PERMISSIONS_REQUEST_CODE);
                }
            }
        } else {
            final Snackbar snackbar = Snackbar.make(mLayout, "디바이스가 카메라를 지원하지 않습니다.",
                    Snackbar.LENGTH_INDEFINITE);
            snackbar.setAction("확인", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    snackbar.dismiss();
                }
            });
            snackbar.show();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i(INSPECTION, "onPause()");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i(INSPECTION, "onDestroy()");
    }

    @Override
        public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grandResults) {
            if ( requestCode == PERMISSIONS_REQUEST_CODE && grandResults.length == REQUIRED_PERMISSIONS.length) {
                // 요청 코드가 PERMISSIONS_REQUEST_CODES[I] 이고, 요청한 퍼미션 개수만큼 수신되었다면
                boolean check_result = true;
                // 모든 퍼미션을 허용했는지 체크합니다.
                for (int result : grandResults) {
                    if (result != PackageManager.PERMISSION_GRANTED) {
                        check_result = false;
                        break;
                    }
                }
                if ( check_result ) {
                    loginCheck();
                    // 모든 퍼미션을 허용했다면 카메라 프리뷰를 시작합니다.
                } else {
                    // 거부한 퍼미션이 있다면 앱을 사용할 수 없는 이유를 설명해주고 앱을 종료합니다.2 가지 경우가 있습니다.
                    if (ActivityCompat.shouldShowRequestPermissionRationale(InspectionActivity.this, REQUIRED_PERMISSIONS[0])
                            || ActivityCompat.shouldShowRequestPermissionRationale(InspectionActivity.this, REQUIRED_PERMISSIONS[1])) {
                        // 사용자가 거부만 선택한 경우에는 앱을 다시 실행하여 허용을 선택하면 앱을 사용할 수 있습니다.
                        Snackbar.make(mLayout, "퍼미션이 거부되었습니다. 앱을 다시 실행하여 퍼미션을 허용해주세요. ",
                                Snackbar.LENGTH_INDEFINITE).setAction("확인", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                finish();
                            }
                        }).show();
                    }else {
                        // “다시 묻지 않음”을 사용자가 체크하고 거부를 선택한 경우에는 설정(앱 정보)에서 퍼미션을 허용해야 앱을 사용할 수 있습니다.
                        Snackbar.make(mLayout, "퍼미션이 거부되었습니다. 설정(앱 정보)에서 퍼미션을 허용해야 합니다. ",
                                Snackbar.LENGTH_INDEFINITE).setAction("설정", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                AlertDialog.Builder localBuilder = new AlertDialog.Builder(InspectionActivity.this);
                                localBuilder.setTitle("권한 설정")
                                        .setMessage("권한 거절로 인해 일부기능이 제한됩니다.")
                                        .setPositiveButton("권한 설정하러 가기", new DialogInterface.OnClickListener(){
                                            public void onClick(DialogInterface paramAnonymousDialogInterface, int paramAnonymousInt){
                                                try {
                                                    Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                                                            .setData(Uri.parse("package:" + getPackageName()));
                                                    startActivity(intent);
                                                } catch (ActivityNotFoundException e) {
                                                    e.printStackTrace();
                                                    Intent intent = new Intent(Settings.ACTION_MANAGE_APPLICATIONS_SETTINGS);
                                                    startActivity(intent);
                                                }
                                            }})
                                        .setNegativeButton("취소하기", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface paramAnonymousDialogInterface, int paramAnonymousInt) {
                                                Toast.makeText(getApplication(), "권한습gfㄴ다" ,Toast.LENGTH_SHORT).show();
                                                //Toast.makeText(getApplication(),getString(R.string.limit),Toast.LENGTH_SHORT).show();
                                            }})
                                        .create()
                                        .show();
                            }
                        }).show();
                    }
                }
            }
        }

    private void loginCheck() {
        if(userNick!=null&&userNick.length()>0){
            Intent intent = new Intent(InspectionActivity.this, MainActivity.class);
            startActivity(intent);
        } else {
            Intent intent = new Intent(InspectionActivity.this, LoginActivity.class);
            startActivity(intent);
        }
    }

}