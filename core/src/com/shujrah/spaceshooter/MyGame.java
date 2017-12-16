package com.shujrah.spaceshooter;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.shujrah.spaceshooter.Screens.MyScreen;
import com.shujrah.spaceshooter.Sprites.Bullet;
import com.shujrah.spaceshooter.Sprites.Player;

import java.util.ArrayList;

public class MyGame extends Game {
	public SpriteBatch batch;
	//Texture img;

	public static final float H = 100;
	public static final float W = 50;
	public static final int PPM = 5;
	public static World world;
	public static Player player;
	public static ShapeRenderer sr;
	public static OrthographicCamera gameCam;

	public static Texture spriteSheet;

	public static Array<Bullet> bullets;

	public static boolean debugRendererOn;


	@Override
	public void create () {
	    MyLib.logging = false;

		Gdx.app.setLogLevel(Application.LOG_DEBUG);

        Gdx.app.log("MyGame", "World Area (meters): " + W + " x " + H);
		Gdx.app.log("MyGame", "World Area (px): " + W*PPM + " x " + H*PPM
        + " || PPM:" + PPM);

		debugRendererOn = false; //true;

		batch = new SpriteBatch();
        spriteSheet = new Texture( Gdx.files.internal("shooterSprites.png") );
        sr = new ShapeRenderer();
        gameCam = new OrthographicCamera(W/PPM, H/PPM);


		//img = new Texture("badlogic.jpg");
		setScreen(new MyScreen(this));



	}

	@Override
	public void render () {

		Gdx.gl.glClearColor(0, 0.0f, 0.1f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		super.render();

//		Gdx.gl.glClearColor(1, 0, 0, 1);
//		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
//		batch.begin();
//		batch.draw(img, 0, 0);
//		batch.end();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		//img.dispose();
        spriteSheet.dispose();
		sr.dispose();
	}
}
