package com.shujrah.spaceshooter.Sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.TimeUtils;
import com.shujrah.spaceshooter.MyGame;
import com.shujrah.spaceshooter.MyLib;
import com.shujrah.spaceshooter.Screens.MyScreen;
import static com.shujrah.spaceshooter.MyGame.spriteSheet;
import static com.shujrah.spaceshooter.MyGame.world;
import static com.shujrah.spaceshooter.MyGame.bullets;

/**
 * Created by waqar on 29/10/2017.
 */

public class Player extends Sprite {

    MyScreen screen;
    public Body body;
    Texture texture;
    //Texture spriteSheet;
    TextureRegion textureRegion;
    public float x, y;
    int width, height, bulletDelay;
    enum State { MOVING_LEFT, MOVING_RIGHT, STATIONARY, DEAD };
    State current, previous;
    public boolean isFiring;

    FixtureDef fdef;

    Long created;
    long lastBulletTime;





    public Player(MyScreen screen) {
        created = TimeUtils.millis();

        this.screen = screen;
        world = screen.getWorld();

        this.width = 4;
        this.height = 10;

        defineBody();

        texture = new Texture( Gdx.files.internal("spaceship.png") );
        // moved to MyGame. So its loaded once when game is created.
        //spriteSheet = new Texture( Gdx.files.internal("shooterSprites.png") );

        textureRegion = new TextureRegion(spriteSheet, 41,0,40,40);


        bulletDelay = 1290; //millisoconds
        isFiring = true;


    }

    public void defineBody(){

        BodyDef bdef = new BodyDef();
        bdef.type = BodyDef.BodyType.DynamicBody;
        bdef.position.set(MyGame.W/2, MyGame.H/2);
        body = world.createBody(bdef);
        fdef = new FixtureDef();

        Vector2[] vertices = new Vector2[5];
        vertices[0] = new Vector2(0,  0);
        vertices[1] = new Vector2(-width,  -height);
        vertices[2] = new Vector2(0,  -height/2);
        vertices[3] = new Vector2(width,  -height);
        vertices[4] = new Vector2(0, 0);

        PolygonShape polygonShape = new PolygonShape();
        polygonShape.set(vertices);


        fdef.shape = polygonShape;

        body.createFixture(fdef).setUserData("player");


        //circle
        CircleShape cshape = new CircleShape();
        cshape.setRadius(width/2);
        cshape.setPosition(new Vector2(0,-height/2));
        fdef.shape = cshape;
        body.createFixture(fdef).setUserData("playerCircle");

        //Player box
        polygonShape.setAsBox(4,2);
        polygonShape.setAsBox(width,2, new Vector2(0,-height+height/4),0f);
        fdef.shape = polygonShape;
        body.createFixture(fdef).setUserData("playerBox");


        body.setUserData("player");

        cshape.dispose();



    }




    public void update(float delta){


        //set state//
        if(body.getLinearVelocity().x > 10)
            current = State.MOVING_RIGHT;

        if(body.getLinearVelocity().x < -10)
            current = State.MOVING_LEFT;

        if(body.getLinearVelocity().x < 10 && body.getLinearVelocity().x > -10)
            current = State.STATIONARY;

//        MyLib.llog(1f, "current state: " + current);
//        MyLib.llog(1f, "X Velocity: " + body.getLinearVelocity().x);


        //set texture region depending on state
        if(current == State.STATIONARY)
            textureRegion = new TextureRegion(spriteSheet, 41,40,40,48);
        if(current == State.MOVING_LEFT)
            textureRegion = new TextureRegion(spriteSheet, 0,40,40,48);
        if(current == State.MOVING_RIGHT)
            textureRegion = new TextureRegion(spriteSheet, 82,40,40,48);


//        if(body.getPosition().x < 0 || body.getPosition().x > MyGame.W) {
//            body.setLinearVelocity(0, body.getLinearVelocity().y);
//            body.setTransform(0,body.getPosition().y, body.getAngle());
//        }

        //Set Bounds for body to stay within defined world.
        if(body.getPosition().x < 0)
            body.setTransform(0, body.getPosition().y, body.getAngle());

        if(body.getPosition().x > MyGame.W)
            body.setTransform(MyGame.W, body.getPosition().y, body.getAngle());

        if(body.getPosition().y < 0)
            body.setTransform(body.getPosition().x, 0, body.getAngle());

        if(body.getPosition().y > MyGame.H)
            body.setTransform(body.getPosition().x, MyGame.H, body.getAngle());




//        if( (body.getPosition().y) < 150 || (body.getPosition().y) > MyGame.H - 150)
//            body.setLinearVelocity(body.getLinearVelocity().x, 0);


        setPosition(body.getPosition().x - getWidth() / 2, body.getPosition().y - getHeight() /2 );
        fire();

//        MyLib.llog(1f,"Player: " + body.getPosition().x +", " + body.getPosition().y);


    }


    @Override
    public void draw(Batch batch) {
        update(Gdx.graphics.getDeltaTime());
        batch.begin();
            //batch.draw(texture,body.getPosition().x - width, body.getPosition().y -height, width * 2, height);
            batch.draw(textureRegion,body.getPosition().x - width, body.getPosition().y -height, width * 2, height);
        batch.end();
    }

    public void dispose(){
        //body.getWorld().destroyBody(body);
    }

    public void fire(){


        if (TimeUtils.millis() - lastBulletTime > bulletDelay && isFiring) {

//            bullets.add( new Bullet( 1, 90, .01f,0));
            bullets.add( new Bullet( 1, 70, .01f,-0.001f));
//            bullets.add( new Bullet( 1, 110,.01f,0.01f));

//            bullets.add( new Bullet( 10, 150, 1.0f,1));
//            bullets.add( new Bullet( 10, 140, 1.0f,1));
//            bullets.add( new Bullet( 10, 180, 1.0f,-1));
//
//            bullets.add( new Bullet( 10, 210, 1.0f, 1));
//            bullets.add( new Bullet( 10, 330, 1.0f, 1));

            lastBulletTime = TimeUtils.millis();

        }

    }


    public void hit(){
        Gdx.app.log("player", " is hit");
    }




}
