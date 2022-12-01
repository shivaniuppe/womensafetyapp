/*
 * Copyright (c) 2022. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.pm.wosafe.Class;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;

import androidx.core.app.ActivityCompat;

import java.util.ArrayList;
import java.util.List;

public class PermissionUtility {
    private Context context;
    private String[] PERMISSIONS;public PermissionUtility(Context context, String... permissions) {
        this.context = context;
        this.PERMISSIONS = permissions;
    }public boolean arePermissionsEnabled(){
        for(String permission : PERMISSIONS){
            if(ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED)
                return false;
        }
        return true;
    }public void requestMultiplePermissions(){
        List<String> remainingPermissions = new ArrayList<>();
        for (String permission : PERMISSIONS) {
            if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                remainingPermissions.add(permission);
            }
        }
        ActivityCompat.requestPermissions((Activity) context, remainingPermissions.toArray(new String[remainingPermissions.size()]), 101);
    }public boolean onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if(requestCode == 101){
            for(int i=0;i<grantResults.length;i++){
                if(grantResults[i] != PackageManager.PERMISSION_GRANTED){
                    if(ActivityCompat.shouldShowRequestPermissionRationale((Activity) context, permissions[i])){
                        requestMultiplePermissions();
                    }
                    return false;
                }
            }
        }
        return true;
    }}