package com.swag.solutions;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class MainGame extends ApplicationAdapter implements ApplicationListener, InputProcessor {
	SpriteBatch batch;
    Texture img;
    int imgX=0, imgY=0;
    float omjerY,omjerX,omjer;
    private OrthographicCamera camera;
	
	@Override
	public void create () {
		batch = new SpriteBatch();
		img = new Texture("badlogic.jpg");
        omjerY=(float)Gdx.graphics.getHeight()/600;
        omjerX=(float)Gdx.graphics.getWidth()/1024;
        omjer=(omjerX+omjerY)/2;
        camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        Gdx.input.setInputProcessor(this);
        Gdx.app.log("omjer", "" + omjer);
	}

    @Override
    public void dispose() {
        batch.dispose();
        img.dispose();
    }

	@Override
	public void render () {
		Gdx.gl.glClearColor(1, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.begin();
		batch.draw(img, imgX, imgY);
		batch.end();
	}

    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        Vector3 koord;
        //koord = camera.unproject(new Vector3(screenX,screenY,0));
        koord=new Vector3(screenX, Gdx.graphics.getHeight()-screenY, 0);  //placeholder
        if(u_slici(img,(int) koord.x, (int) koord.y)){
            imgX+=((int) koord.x-img.getWidth()/2-imgX);
            imgY+=((int) koord.y-img.getHeight()/2-imgY);
        }
        return true;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }

    boolean u_slici(Texture img, int coordX, int coordY){
        if(imgX < coordX && (img.getWidth()+imgX) > coordX && imgY < coordY && (img.getHeight()+imgY)> coordY)
            return true;
        return false;
    }
}
