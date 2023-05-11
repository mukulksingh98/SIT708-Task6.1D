package com.example.task61;

import android.content.Intent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;

public class MenuActivity extends AppCompatActivity {

    // Activity code here

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.home_item:
                startActivity(new Intent(MenuActivity.this, MainActivity.class));
                finish();
                return true;
            case R.id.account_item:
                startActivity(new Intent(MenuActivity.this, AccountActivity.class));
                finish();
                return true;
            case R.id.myOrders_item:
                startActivity(new Intent(MenuActivity.this, MyOrdersActivity.class));
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}