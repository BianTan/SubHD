package org.biantan.subhd;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;

import de.psdev.licensesdialog.LicensesDialog;

public class AboutActivity extends AppCompatActivity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Binding();
    }

    private void Binding(){
        LinearLayout developersView = (LinearLayout) findViewById(R.id.blog);
        developersView.setOnClickListener(this);
        LinearLayout licensesView = (LinearLayout) findViewById(R.id.ku);
        licensesView.setOnClickListener(this);
        LinearLayout sourceCodeView = (LinearLayout) findViewById(R.id.github);
        sourceCodeView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.blog:
                Intent intent1 = new Intent(Intent.ACTION_VIEW);
                intent1.setData(Uri.parse("https://biantan.org"));
                startActivity(intent1);
                break;
            case R.id.ku:
                new LicensesDialog.Builder(this)
                        .setNotices(R.raw.raw)
                        .build()
                        .show();
                break;
            case R.id.github:
                Intent intent2 = new Intent(Intent.ACTION_VIEW);
                intent2.setData(Uri.parse("https://github.com/BianTan/SubHD"));
                startActivity(intent2);
                break;
            default:
                break;
        }
    }

}
