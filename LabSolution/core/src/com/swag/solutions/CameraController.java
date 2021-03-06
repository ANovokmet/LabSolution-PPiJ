package com.swag.solutions;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Vector2;
import com.swag.solutions.logic.Solution;

/**
 * Created by Ante on 17.4.2015..
 */
public class CameraController implements GestureDetector.GestureListener {
    float velX, velY;
    boolean flinging = false;
    float initialScale = 1;
    OrthographicCamera camera;
    private Solution solution;
    Vector2 lastGoodCamera;

    boolean disabled = false;

    public CameraController(OrthographicCamera camera, Solution w){
        this.camera = camera;   //glavna kamera
        lastGoodCamera = new Vector2(camera.position.x, camera.position.y);   //pozicija u slucaju pomicanja van okvira svijeta
        solution =w;
    }

    private boolean xInsideBounds(){
        if(camera.position.x-camera.viewportWidth/2 > solution.left_x && camera.position.x+camera.viewportWidth/2 < solution.right_x
                ){
            lastGoodCamera.x = camera.position.x;
            return true;
        }
        return false;
    }

    private boolean yInsideBounds(){
        if(camera.position.y+camera.viewportHeight/2 < solution.top_y && camera.position.y-camera.viewportHeight/2 > solution.bottom_y){

            lastGoodCamera.y = camera.position.y;
            return true;
        }
        return false;
    }

    public boolean touchDown (float x, float y, int pointer, int button) {
        flinging = false;
        initialScale = camera.zoom;
        return false;
    }

    @Override
    public boolean tap (float x, float y, int count, int button) {
        Gdx.app.log("GestureDetectorTest", "tap at " + x + ", " + y + ", count: " + count);
        return false;
    }

    @Override
    public boolean longPress (float x, float y) {
        Gdx.app.log("GestureDetectorTest", "long press at " + x + ", " + y);
        return false;
    }

    @Override
    public boolean fling (float velocityX, float velocityY, int button) {
        if(!disabled) {
            Gdx.app.log("GestureDetectorTest", "fling " + velocityX + ", " + velocityY);
            flinging = true;
            velX = camera.zoom * velocityX * 0.5f;
            velY = camera.zoom * velocityY * 0.5f;
        }
        return false;
    }

    @Override
    public boolean pan (float x, float y, float deltaX, float deltaY) {
        if(!disabled){
            camera.position.add(-deltaX * camera.zoom, deltaY * camera.zoom, 0);
        }
        return false;

    }

    public void disableCamera(boolean state){
        this.disabled = state;
    }

    @Override
    public boolean panStop (float x, float y, int pointer, int button) {
        Gdx.app.log("GestureDetectorTest", "pan stop at " + x + ", " + y);
        return false;
    }

    @Override
    public boolean zoom (float originalDistance, float currentDistance) {
        return false;
    }

    @Override
    public boolean pinch (Vector2 initialFirstPointer, Vector2 initialSecondPointer, Vector2 firstPointer, Vector2 secondPointer) {
        return false;
    }

    public void update () {


        if (flinging) {
            //Gdx.app.log("trenutno","flingam");
            velX *= 0.9f;
            velY *= 0.9f;
            camera.position.add(-velX * Gdx.graphics.getDeltaTime(), velY * Gdx.graphics.getDeltaTime(), 0);
            if (Math.abs(velX) < 0.01f) velX = 0;
            if (Math.abs(velY) < 0.01f) velY = 0;
            if(velX==0 && velY==0)
                flinging=false;
        }

        if(xInsideBounds() && yInsideBounds()){

        }
        else {
            if(!xInsideBounds()){
                camera.position.x = lastGoodCamera.x;
            }
            if(!yInsideBounds()){
                camera.position.y = lastGoodCamera.y;
            }
        }

    }
}
