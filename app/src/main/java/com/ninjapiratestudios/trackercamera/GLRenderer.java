package com.ninjapiratestudios.trackercamera;

import android.content.res.AssetManager;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.opengl.GLES11Ext;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;
import java.util.List;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Created by jjdixie on 2/1/16.
 */
public class GLRenderer implements GLSurfaceView.Renderer, SurfaceTexture.OnFrameAvailableListener {

    GLCamView glCamView;
    Camera camera;
    private SurfaceTexture surfaceTexture;

    float[] transformMatrix;

    private static short indices[] = { 0, 1, 2, 2, 1, 3 };
    private ShortBuffer indexBuffer;
    private FloatBuffer vertexBuffer;
    private FloatBuffer textureBuffer;
    private float rectCoords[] = { -1.0f, -1.0f, 0.0f,
            1.0f, -1.0f, 0.0f,
            -1.0f,  1.0f, 0.0f,
            1.0f,  1.0f, 0.0f};
    private float texCoords[] = { 0.0f, 1.0f,
            1.0f, 1.0f,
            0.0f, 0.0f,
            1.0f, 0.0f};

    //handles
    int[] textureHandle = new int[1];
    private int vertexShaderHandle;
    private int fragmentShaderHandle;
    private int shaderProgram;

    boolean updateSurfaceTexture;

    GLRenderer(GLCamView glcv){
        glCamView = glcv;

        loadShaders();

        //ready index, vertex, and texture buffers
        ByteBuffer ib = ByteBuffer.allocateDirect(indices. length * 2);
        ib.order(ByteOrder.nativeOrder());
        indexBuffer = ib.asShortBuffer();
        indexBuffer.put(indices);
        indexBuffer.position(0);

        ByteBuffer bb = ByteBuffer.allocateDirect(rectCoords.length * 4);
        bb.order(ByteOrder.nativeOrder());

        vertexBuffer = bb.asFloatBuffer();
        vertexBuffer.put(texCoords);
        vertexBuffer.position(0);

        ByteBuffer texturebb = ByteBuffer.allocateDirect(texCoords.length * 4);
        texturebb.order(ByteOrder.nativeOrder());

        textureBuffer = texturebb.asFloatBuffer();
        textureBuffer.put(texCoords);
        textureBuffer.position(0);
    }

    public void release(){
        surfaceTexture.release();
        camera.stopPreview();
        GLES20.glDeleteTextures(1, textureHandle, 0);
    }

    @Override
    public void onSurfaceCreated ( GL10 unused, EGLConfig config ) {
        textureHandle = new int[1];
        GLES20.glGenTextures (1, textureHandle, 0);
        GLES20.glBindTexture(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, textureHandle[0]);
        GLES20.glTexParameteri(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_CLAMP_TO_EDGE);
        GLES20.glTexParameteri(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_CLAMP_TO_EDGE);
        GLES20.glTexParameteri(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_NEAREST);
        GLES20.glTexParameteri(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_NEAREST);
        surfaceTexture = new SurfaceTexture(textureHandle[0]);
        surfaceTexture.setOnFrameAvailableListener(this);

        camera = Camera.open();
        try {
            camera.setPreviewTexture(surfaceTexture);
        } catch(IOException e){

        }

        GLES20.glClearColor(1.0f, 1.0f, 0.0f, 1.0f);

        loadShaders();
    }

    @Override
    public void onSurfaceChanged ( GL10 unused, int width, int height ) {
        GLES20.glViewport( 0, 0, width, height );
        Camera.Size previewSize = camera.getParameters().getPreviewSize();
        Camera.Parameters param = camera.getParameters();
        param.setPictureSize(previewSize.width, previewSize.height);
        param.set("orientation", "landscape");
        camera.setParameters(param);
        camera.startPreview();
    }

    @Override
    public void onDrawFrame ( GL10 unused ) {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);

        synchronized(this) {
            if (updateSurfaceTexture) {
                surfaceTexture.updateTexImage();
                updateSurfaceTexture = false;
            }
        }

        GLES20.glUseProgram(shaderProgram);

        int positionHandle = GLES20.glGetAttribLocation(shaderProgram, "position");
        int textureCoordinateHandle = GLES20.glGetAttribLocation (shaderProgram, "inputTextureCoordinate");
        int textureHandle = GLES20.glGetUniformLocation (shaderProgram, "videoFrame");

        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        GLES20.glBindTexture(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, textureHandle);
        GLES20.glUniform1i(textureHandle, 0);

        GLES20.glVertexAttribPointer(positionHandle, 2, GLES20.GL_FLOAT, false, 4 * 2, vertexBuffer);
        GLES20.glVertexAttribPointer(textureCoordinateHandle, 2, GLES20.GL_FLOAT, false, 4 * 2, textureBuffer);
        GLES20.glEnableVertexAttribArray(positionHandle);
        GLES20.glEnableVertexAttribArray(textureCoordinateHandle);

        GLES20.glDrawElements(GLES20.GL_TRIANGLES, indices.length, GLES20.GL_UNSIGNED_SHORT, indexBuffer);
        GLES20.glDisableVertexAttribArray(positionHandle);
        GLES20.glDisableVertexAttribArray(textureCoordinateHandle);
    }

    @Override
    public synchronized void onFrameAvailable ( SurfaceTexture st ) {
        updateSurfaceTexture = true;
        glCamView.requestRender();
    }

    private void loadShaders(){
        AssetManager assetManager = glCamView.getContext().getAssets();
        String vertexShaderCode = "";
        String fragmentShaderCode = "";

        try {
            InputStream fis = assetManager.open("DirectDisplayShader.fsh");
            InputStreamReader fisr = new InputStreamReader(fis);
            BufferedReader fbr = new BufferedReader(fisr);
            StringBuilder fsb = new StringBuilder();
            String next;
            while((next = fbr.readLine()) != null){
                fsb.append(next);
                fsb.append('\n');
            }
            fragmentShaderCode = fsb.toString();

            InputStream vis = assetManager.open("DirectDisplayShader.vsh");
            InputStreamReader visr = new InputStreamReader(vis);
            BufferedReader vbr = new BufferedReader(visr);
            StringBuilder vsb = new StringBuilder();
            while((next = vbr.readLine()) != null){
                vsb.append(next);
                vsb.append('\n');
            }
            vertexShaderCode = vsb.toString();
        }
        catch(IOException e){
            Log.d("Shader loading error: ", e.getMessage());
            return;
        }
        IntBuffer intBuf=ByteBuffer.allocateDirect(4).order(ByteOrder.nativeOrder()).asIntBuffer();

        vertexShaderHandle = GLES20.glCreateShader(GLES20.GL_VERTEX_SHADER);
        GLES20.glShaderSource(vertexShaderHandle, vertexShaderCode);
        GLES20.glCompileShader(vertexShaderHandle);
        GLES20.glGetProgramiv(vertexShaderHandle, GLES20.GL_COMPILE_STATUS, intBuf);
        if(intBuf.get(0)==0){
            GLES20.glGetShaderiv(vertexShaderHandle, GLES20.GL_INFO_LOG_LENGTH,intBuf);
            Log.d("Shader","Vertex Shader: " + GLES20.glGetShaderInfoLog(vertexShaderHandle));
        }

        fragmentShaderHandle = GLES20.glCreateShader(GLES20.GL_FRAGMENT_SHADER);
        GLES20.glShaderSource(fragmentShaderHandle, fragmentShaderCode);
        GLES20.glCompileShader(fragmentShaderHandle);
        GLES20.glGetProgramiv(fragmentShaderHandle, GLES20.GL_COMPILE_STATUS, intBuf);
        if(intBuf.get(0)==0){
            GLES20.glGetShaderiv(fragmentShaderHandle, GLES20.GL_INFO_LOG_LENGTH,intBuf);
            Log.d("Shader","Fragment Shader: " + GLES20.glGetShaderInfoLog(fragmentShaderHandle));
        }

        shaderProgram = GLES20.glCreateProgram();
        GLES20.glAttachShader(shaderProgram, vertexShaderHandle);
        GLES20.glAttachShader(shaderProgram, fragmentShaderHandle);
        GLES20.glLinkProgram(shaderProgram);

        int[] status = new int[1];
        GLES20.glGetProgramiv(shaderProgram, GLES20.GL_LINK_STATUS, status, 0);
        if (status[0] != GLES20.GL_TRUE) {
            String error = GLES20.glGetProgramInfoLog(shaderProgram);
            //throw new RuntimeException("Shader program compilation failure: " + error);
        }
    }

    /*private void loadShaders(){

    }*/
}
