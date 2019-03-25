package com.transpay;

import android.app.Application;
import android.content.Intent;

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        this.startKioskService();
    }

    private void startKioskService() { // ... and this method
        startService(new Intent(this, KioskService.class));
    }
}
