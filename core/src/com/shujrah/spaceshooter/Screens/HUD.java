package com.shujrah.spaceshooter.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.shujrah.spaceshooter.MyGame;

import static com.shujrah.spaceshooter.MyGame.gameCam;
import static com.shujrah.spaceshooter.MyGame.player;

/**
 * Created by waqar on 09/12/2017.
 */

public class HUD extends Stage implements Disposable{


    Stage stage;
    Skin skin, skin2;
    Table table;

    TextButton buttonLives;

    Viewport gameport;
    OrthographicCamera gamcam;



    public HUD(Viewport viewport, Batch batch) {



        gameport = new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), new OrthographicCamera());
        //setViewport( gameport);

        skin = new Skin();
//        skin2 = new Skin(Gdx.files.internal("data/uiskin.json"));
        skin2 = new Skin(Gdx.files.internal("data/flat-earth/flat-earth-ui.json"));


        stage = new Stage(gameport, batch);

        // Create a table that fills the screen. Everything else will go inside this table.
        table = new Table();
        table.setFillParent(true);
        table.top();
        stage.addActor(table);

        final TextButton button = new TextButton("#", skin2, "default");

        table.add(button).left().expand(true, false);

        button.addListener(new ChangeListener() {
            public void changed (ChangeEvent event, Actor actor) {
                System.out.println("Clicked! Is checked: " + button.isChecked());
            }
        });

        //table.add(new Image(skin.newDrawable("white", Color.RED))).size(14);

        buttonLives = new TextButton("" + player.lives, skin2, "default");

        table.add(buttonLives).right();

        Gdx.input.setInputProcessor(stage);





    }


    public void draw() {

        stage.draw();

    }


    public void act(float delta) {
        stage.act();
        table.setDebug(MyGame.debugRendererOn);
        buttonLives.setText("" + player.lives);

    }

}
