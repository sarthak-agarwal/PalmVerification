package com.example.SignatureVerify;

import android.content.Context;
import android.content.res.Configuration;
import android.hardware.Camera;
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

    public SurfaceHolder mHolder;
    public Camera camera=null;
    @SuppressWarnings("deprecation")
    public CameraSurfaceView(Context context)
    {
        super(context);
         mHolder=getHolder();
        mHolder.addCallback(this);
        this.mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        camera= Camera.open();


    }



    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        //To change body of implemented methods use File | Settings | File Templates.

        try {
           // camera= Camera.open();
            camera.setPreviewDisplay(mHolder);
            camera.startPreview();
        }
        catch(Exception e)
        {
            Log.w("exception:",e.getMessage());
        }

    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        //To change body of implemented methods use File | Settings | File Templates.

       Camera.Parameters params=camera.getParameters();
      //  params.setPreviewSize(width,height);
       // camera.setParameters(params);
        // camera= Camera.open();
        if (Integer.parseInt(Build.VERSION.SDK) >= 8)
            setDisplayOrientation(camera, 90);
        else
        {
            if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT)
            {
                params.set("orientation", "portrait");
                params.set("rotation", 90);
            }
            if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE)
            {
                params.set("orientation", "landscape");
                params.set("rotation", 90);
            }
        }
        try {
            camera.setPreviewDisplay(mHolder);
        } catch (IOException e) {
            Log.w("ds", e.getMessage());  //To change body of catch statement use File | Settings | File Templates.
        }
        camera.startPreview();
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
         camera.takePicture(null,null,jpegHandler);
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
