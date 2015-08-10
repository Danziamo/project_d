package com.mirsoft.easyfix.adapters;

import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.mirsoft.easyfix.R;
import com.mirsoft.easyfix.models.Specialty;
import com.mirsoft.easyfix.models.UserSpecialty;

import java.util.ArrayList;

/**
 * Created by mbt on 7/29/15.
 */
public class MySpecialityAdapter extends RecyclerView.Adapter<MySpecialityAdapter.ViewHolder>{

    private ArrayList<UserSpecialty> items;
    private ArrayList<Specialty> specialties;
    private int itemLayout;
    private boolean isEditable = false;

    public MySpecialityAdapter(ArrayList<UserSpecialty> items, ArrayList<Specialty> specialties, int layout) {
        this.items = items;
        this.specialties = specialties;
        this.itemLayout = layout;
    }

    private int getSpecialtyIndexById(AppCompatSpinner spinner, int id){
        int index = 0;
        for(int i = 0; i < spinner.getCount(); i++){
            Specialty specialty = (Specialty) spinner.getItemAtPosition(i);
            if(specialty.getId() == id){
                index = i;
                break;
            }
        }
        return index;
    }

    public void addItem(UserSpecialty item){
        items.add(item);
        notifyDataSetChanged();
    }

    public void setEditable(boolean isEditable) {
        this.isEditable = isEditable;
        notifyDataSetChanged();
    }

    public ArrayList<UserSpecialty> getItems(){
        return items;
    }

    public void setItems(ArrayList<UserSpecialty> items) {
        this.items = items;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(itemLayout, parent, false);

        return new ViewHolder(v, specialties);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final UserSpecialty item = items.get(position);
        int itemPositionSpecialtyIndex = getSpecialtyIndexById(holder.spProfession, item.getSpecialtyId());
        holder.spProfession.setSelection(itemPositionSpecialtyIndex); //@TODO item.getSpecialtyId()

        holder.cbLicense.setChecked(item.isCertified());

        if(item.getId() == 0 || isEditable){
            holder.spProfession.setClickable(true);
            holder.cbLicense.setEnabled(true);
        }

        holder.spProfession.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Specialty currentSpeciality = specialties.get(position);
                item.setSpecialtyId(currentSpeciality.getId());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        holder.cbLicense.setOnCheckedChangeListener(new CheckBox.OnCheckedChangeListener(){

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                item.setIsCertified(isChecked);
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public AppCompatSpinner spProfession;
        public AppCompatCheckBox cbLicense;

        public ViewHolder(View v, ArrayList<Specialty> specialties) {
            super(v);
            spProfession = (AppCompatSpinner) v.findViewById(R.id.spProfession);
            cbLicense = (AppCompatCheckBox) v.findViewById(R.id.cbLicense);

            ArrayAdapter<Specialty> adapter = new ArrayAdapter<>(v.getContext(), android.R.layout.simple_spinner_item, specialties);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spProfession.setAdapter(adapter);
        }
    }
}
