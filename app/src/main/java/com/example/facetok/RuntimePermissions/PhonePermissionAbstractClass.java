package com.example.facetok.RuntimePermissions;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.SparseArray;
import android.view.View;
import android.widget.Toast;

import com.example.facetok.R;


public abstract class PhonePermissionAbstractClass extends AppCompatActivity {
    private SparseArray mErrorString;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mErrorString = new SparseArray();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        int permissionCheck = PackageManager.PERMISSION_GRANTED;
        for (int permission : grantResults) {
            permissionCheck = permissionCheck + permission;
        }
        if ((grantResults.length > 0) && permissionCheck == PackageManager.PERMISSION_GRANTED) {
            onPermissionGrantedCheck(requestCode);
        } else {
            Snackbar.make(findViewById(android.R.id.content), R.string.snakebarPhonePermissionsEnableMessage,
                    Snackbar.LENGTH_INDEFINITE).setAction("ENABLE",
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent();
                            intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                            intent.addCategory(Intent.CATEGORY_DEFAULT);
                            intent.setData(Uri.parse("package:" + getPackageName()));
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                            intent.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
                            startActivity(intent);
                        }
                    }).show();
        }
    }

    public void requestAppPermissions(final String[] requestedPermissions, final int stringId, final int requestCode) {
        mErrorString.put(stringId, requestCode);
        int checkPermission = PackageManager.PERMISSION_GRANTED;
        boolean isPermissionDenied = false;
        for (String permissions : requestedPermissions) {
            checkPermission = checkPermission + ContextCompat.checkSelfPermission(this, permissions);
            isPermissionDenied = isPermissionDenied || ActivityCompat.shouldShowRequestPermissionRationale(this, permissions);
        }
        if (checkPermission != PackageManager.PERMISSION_GRANTED) {
            if (isPermissionDenied) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage(R.string.phonePermissionMainBody);
                builder.setTitle(R.string.phonePermissionTitle);
                builder.setCancelable(false);
                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ActivityCompat.requestPermissions(PhonePermissionAbstractClass.this, requestedPermissions, requestCode);
                    }
                }).show();
            } else {
                ActivityCompat.requestPermissions(this, requestedPermissions, requestCode);
            }
        } else {
            onPermissionGrantedCheck(requestCode);
        }
    }

    public abstract void onPermissionGrantedCheck(int requestCode);

    @Override
    public void onBackPressed() {
        Toast.makeText(this, "This Option is not available here ", Toast.LENGTH_SHORT).show();
    }
}
