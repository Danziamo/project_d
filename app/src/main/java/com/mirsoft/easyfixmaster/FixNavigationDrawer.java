package com.mirsoft.easyfixmaster;

import android.content.Intent;
import android.os.Bundle;

import it.neokree.materialnavigationdrawer.MaterialNavigationDrawer;
import it.neokree.materialnavigationdrawer.elements.MaterialAccount;
import it.neokree.materialnavigationdrawer.elements.listeners.MaterialAccountListener;

public class FixNavigationDrawer extends MaterialNavigationDrawer implements MaterialAccountListener{
    @Override
    public void init(Bundle bundle) {
        MaterialAccount account = new MaterialAccount(this.getResources(), "Title", "Subtitle", 0, 0);
        this.addAccount(account);

        this.setAccountListener(this);
        this.addSection(newSection("Заказы", new MainActivityFragment()));
        this.addSection(newSection("Правила", new MainActivityFragment()));
        this.addSection(newSection("О программе", new MainActivityFragment()));
        this.addSection(newSection("Рекомендовать другу", new MainActivityFragment()));

        this.addBottomSection(newSection("Выход", R.mipmap.ic_launcher, new Intent(this, MainActivity.class)));

    }

    @Override
    public void onAccountOpening(MaterialAccount materialAccount) {

    }

    @Override
    public void onChangeAccount(MaterialAccount materialAccount) {

    }
}
