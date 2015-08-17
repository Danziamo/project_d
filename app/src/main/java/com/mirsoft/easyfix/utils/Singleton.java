package com.mirsoft.easyfix.utils;

import android.content.Context;
import android.widget.LinearLayout;

import com.mirsoft.easyfix.models.Order;
import com.mirsoft.easyfix.models.Specialty;
import com.mirsoft.easyfix.models.User;

import java.util.ArrayList;

/**
 * Created by parviz on 7/27/15.
 */
public class Singleton {
    Context context;
    private static Singleton instance;

    public ArrayList<Specialty> specialtyList;

    public Boolean isClientMode = true;

    public Boolean fromCreateBasicOrderFragment = false;

    public Boolean isUserLogoUpdated = false;

    public  int currentSelectedTabPage = -1 ;

    public int indexSelectedOrder  = 0;

    public Order clientSelectedOrder;
    public User selectedMaster;
    public Specialty selectedSpecialty;

    public int activeOrdersCount   = -1;
    public int finishedOrdersCount = -1;

    public User currentUser;


    public static Singleton getInstance(Context context) {
        if(instance == null) instance = new Singleton(context);
        if(instance.context == null) instance.context = context;
        return instance;
    }

    private Singleton(Context context) {
        this.context = context;
        this.specialtyList = new ArrayList<>();
    }

    public int getPosition(int orderPosition){
        int position = -1;
        for(int i = 0; i < specialtyList.size(); i++){
            if(specialtyList.get(i).getId() == orderPosition){
                position = i;
            }
        }
        return position;
    }

    public double curLat;
    public double curLng;
}
