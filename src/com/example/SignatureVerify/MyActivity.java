package com.example.SignatureVerify;

import android.app.Activity;
import android.content.Intent;
import android.hardware.Camera;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class    MyActivity extends Activity
{
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

    final Button cam_fire_button = (Button) findViewById(R.id.close);
    cam_fire_button.setOnClickListener(new View.OnClickListener() {

    public void onClick(View v) {
        // Perform action on click
                Intent intent = new Intent(MyActivity.this,CamPrv.class);

          EditText nameInput = (EditText) findViewById(R.id.input_name);
                String name = nameInput.getText().toString();
                intent.putExtra("name",name);

                 startActivity(intent);

              finish();

         //   c = Camera.open(); // attempt to get a Camera instance

    }
});
    }

        // Perform action on click

 }


