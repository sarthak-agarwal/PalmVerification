package com.example.SignatureVerify;

import android.content.Context;
import android.content.res.Configuration;
import android.hardware.Camera;
import android.media.AudioManager;
import android.os.Build;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.io.IOException;
import java.lang.reflect.Method;

/**
 * Created with IntelliJ IDEA.
 * User: sarthak
 * Date: 18/2/13
 * Time: 2:27 AM
 * To change this template use File | Settings | File Templates.
 */
public class CameraSurfaceView extends SurfaceView implements SurfaceHolder.Callback{

    public SurfaceHolder cameraSurfaceHolder;
    public Camera camera = null;
    @SuppressWarnings("deprecation")
    public CameraSurfaceView(Context context){

        super(context);
        cameraSurfaceHolder = getHolder();
        cameraSurfaceHolder.addCallback(this);
        this.cameraSurfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        camera = Camera.open();

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {

        try {
           // camera= Camera.open();
            camera.setPreviewDisplay(cameraSurfaceHolder);
            camera.startPreview();
        }
        catch(Exception e) {
            Log.e("Error setting camera pane ",e.getMessage());
        }

    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        //To change body of implemented methods use File | Settings | File Templates.

       Camera.Parameters params=camera.getParameters();
      //  params.setPreviewSize(width,height);
       // camera.setParameters(params);
        // camera= Camera.open();

        if (cameraSurfaceHolder.getSurface() == null){
            // preview surface does not exist
            return;
        }

        try {
            camera.stopPreview();
        } catch (Exception e){
            // ignore: tried to stop a non-existent preview
        }

        if (Integer.parseInt(Build.VERSION.SDK) >= 8){
            setDisplayOrientation(camera, 90);
        }
        else{
            if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
                params.set("orientation", "portrait");
             //   params.set("rotation", 90);
            }
            if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
                params.set("orientation", "landscape");
             //   params.set("rotation", 90);
            }
        }
        try {
            camera.setPreviewDisplay(cameraSurfaceHolder);
            camera.startPreview();

        } catch (IOException e) {
            Log.e("Error: starting camera view", e.getMessage());  //To change body of catch statement use File | Settings | File Templates.
        }


    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        //To change body of implemented methods use File | Settings | File Templates.

        camera.stopPreview();
        camera.release();
        //camera=null;
    }
    public void capture(Camera.PictureCallback jpegHandler)
    {
            camera.autoFocus(new Camera.AutoFocusCallback(){

                @Override
                public void onAutoFocus(boolean a, Camera camera1){

                }
    });
            Camera.ShutterCallback shutterCallback = new Camera.ShutterCallback() {
            public void onShutter() {
                AudioManager mgr = (AudioManager) getContext().getSystemService(Context.AUDIO_SERVICE);
                mgr.playSoundEffect(AudioManager.FLAG_PLAY_SOUND);
            }
        };
         camera.takePicture(shutterCallback, null, jpegHandler);
    }
    protected void setDisplayOrientation(Camera camera, int angle){
        Method downPolymorphic;
        try
        {
            downPolymorphic = camera.getClass().getMethod("setDisplayOrientation", new Class[] { int.class });
            if (downPolymorphic != null)
                downPolymorphic.invoke(camera, new Object[] { angle });
        }
        catch (Exception e1)
        {
        }
    }
}
