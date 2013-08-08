package com.example.SignatureVerify;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.widget.*;
import com.example.SignatureVerify.R;
import android.hardware.Camera;
import android.graphics.Canvas;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.PrivateKey;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.view.View;


/**
 * Created with IntelliJ IDEA.
 * User: sarthak
 * Date: 18/2/13
 * Time: 1:40 AM
 * To change this template use File | Settings | File Templates.
 */
public class CamPrv extends Activity {

    protected ImageView imageView;
    protected FrameLayout frameLayout;
    protected CameraSurfaceView cameraSurfaceView;
    private Button captureButton;
    private Button keepButton;
    private Button continueButton;
    private LinearLayout buttonsLinearLayout;
    public Camera.PictureCallback mPicture;
    public static final int MEDIA_TYPE_IMAGE = 1;
    public static final int MEDIA_TYPE_VIDEO = 2;
    private static final String APP_NAME = "VerifyPalm";

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.campy_ly);
      //  canvasDrawing= (CanvasDrawing) this.findViewById(R.id.cmr_frame);
        setUpCamera();
    }
    public void setUpCamera()
    {
        cameraSurfaceView=new CameraSurfaceView(this);
        imageView= (ImageView) findViewById(R.id.image);
        mPicture = new Camera.PictureCallback() {

            @Override
            public void onPictureTaken(byte[] data, Camera camera) {

                File pictureFile = getOutputMediaFile(MEDIA_TYPE_IMAGE);
                if (pictureFile == null){
                    Log.d("********", "Error creating media file, check storage permissions: ");

                    return;
                }

                try {
                    FileOutputStream fos = new FileOutputStream(pictureFile);
                    fos.write(data);
                    fos.close();
                    Log.i("$$$$$$$$$", "file saved successfully....");
                } catch (FileNotFoundException e) {
                    Log.e("********", "File not found: " + e.getMessage());
                } catch (IOException e) {
                    Log.e("********", "Error accessing file: " + e.getMessage());
                }
                sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED,
                        Uri.parse("file://" + Environment.getExternalStorageDirectory())));
            }
        };

       // imageView= new ImageView(getApplicationContext());
      //  imageView.setBackgroundColor(Color.GRAY);
        frameLayout =  (FrameLayout) findViewById(R.id.cmr_frame);
        captureButton = (Button) findViewById(R.id.button);
        //frameLayout.addView(imageView);
        frameLayout.addView(cameraSurfaceView);
        frameLayout.bringChildToFront(imageView);
        frameLayout.bringChildToFront(captureButton);

        buttonsLinearLayout = (LinearLayout) findViewById(R.id.LL_buttons);
        frameLayout.bringChildToFront(buttonsLinearLayout);

        captureButton.setOnClickListener(
                new View.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        // get an image from the camera

                        captureButton.setVisibility(View.GONE);

                        buttonsLinearLayout.setVisibility(View.VISIBLE);

                        cameraSurfaceView.capture(mPicture);

                    }
                }
        );
        keepButton = (Button) findViewById(R.id.keep_button);
        continueButton = (Button) findViewById(R.id.continue_button);

        continueButton.setOnClickListener(
                new View.OnClickListener(){
                    @Override
                    public void onClick(View v) {


                           // Intent intent = new Intent(CamPrv.this, MyActivity.class);
                           // startActivity(intent);
                           recreate();

                    }
                }
        );

    }
    @Override
    protected void onDestroy(){

        super.onDestroy();
        cameraSurfaceView.surfaceDestroyed(cameraSurfaceView.cameraSurfaceHolder);
    }

    private static File getOutputMediaFile(int type){
        // To be safe, you should check that the SDCard is mounted
        // using Environment.getExternalStorageState() before doing this.

        File mediaStorageDir = new File(Environment.getExternalStorageDirectory().getPath()+"/"+APP_NAME);


        Log.d("dir: ",  mediaStorageDir.getAbsolutePath());
        Log.d("debug2: ",  mediaStorageDir.getPath());
        Log.d("sdsd",Environment.getExternalStorageDirectory().getPath());

        // This location works best if you want the created images to be shared
        // between applications and persist after your app has been uninstalled.

        // Create the storage directory if it does not exist
        if (! mediaStorageDir.exists()){
            if (! mediaStorageDir.mkdirs()){
                Log.e("VerifyPalm", "failed to create directory");
                return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File mediaFile;
        if (type == MEDIA_TYPE_IMAGE){
            mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                    "IMG_"+ timeStamp + ".jpg");
            Log.d("saved",mediaStorageDir.getPath() + File.separator +
                    "IMG_"+ timeStamp + ".jpg");
        } else if(type == MEDIA_TYPE_VIDEO) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                    "VID_"+ timeStamp + ".mp4");
        } else {
            return null;
        }

        return mediaFile;
    }

}
