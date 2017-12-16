package com.shujrah.spaceshooter.Sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;
import com.shujrah.spaceshooter.MyGame;
import com.shujrah.spaceshooter.MyLib;
import com.shujrah.spaceshooter.Screens.MyScreen;

import static com.shujrah.spaceshooter.MyGame.PPM;
import static com.shujrah.spaceshooter.MyGame.player;
import static com.shujrah.spaceshooter.MyGame.spriteSheet;
import static com.shujrah.spaceshooter.MyGame.world;
import static com.shujrah.spaceshooter.MyGame.bullets;

/**
 * Created by waqar on 29/10/2017.
 */

public class Player extends Sprite {
    final String TAG = "Player";

    MyScreen screen;
    public Body body;
    Texture texture;
    TextureRegion textureRegion, region2;
    public float x, y;
    int width, height, bulletDelay;
    enum State { MOVING_LEFT, MOVING_RIGHT, STATIONARY, DEAD };
    State current, previous;
    public boolean isFiring;
    float stateTimer;
    public int lives;
    float forceDefault, velocityT;
    Vector2 velocity, position;
    FixtureDef fdef;
    Long created;
    long lastBulletTime;
    public Animation<TextureRegion> stationary, moving_left, moving_right, dead;



    public Player(MyScreen screen) {
        lives = 5;
        created = TimeUtils.millis();
        this.screen = screen;
        world = screen.getWorld();
        this.width = 20/PPM;
        this.height = 50/PPM;
        forceDefault = 25/PPM; // 25/PPM = 5(PPM=5)
        velocityT = 25; // meters per second; world is 50 meters wide
        stateTimer = 0;

        defineBody();
        //spriteSheet = new Texture( Gdx.files.internal("shooterSprites.png") );

        MyLib.llog(1f, "mass: " + body.getMass());
        Gdx.app.log(TAG, "Mass: "+ body.getMass());

        textureRegion = new TextureRegion(spriteSheet, 41,0,40,40);

        bulletDelay = 100; //millisoconds
        isFiring = true;

        updateFrames();

    }


    private void updateFrames(){

        Array<TextureRegion> frames = new Array<TextureRegion>();

        //get run animation frames and add them to spaceship Animations
        for(int i = 0; i < 5; i++)
            frames.add(new TextureRegion(spriteSheet, 500, i*50, 45, 50));
        stationary = new Animation<TextureRegion>(0.2f, frames);
        frames.clear();

        for(int i = 0; i < 5; i++)
            frames.add(new TextureRegion(spriteSheet, 450, i*50, 45, 50));
        moving_left = new Animation<TextureRegion>(0.2f, frames);
        frames.clear();

        for(int i = 0; i < 5; i++)
            frames.add(new TextureRegion(spriteSheet, 550, i*50, 45, 50));
        moving_right = new Animation<TextureRegion>(0.2f, frames);
        frames.clear();

        for(int i = 0; i < 9; i++)
            frames.add(new TextureRegion(spriteSheet, 400, i*50, 45, 50));
        dead = new Animation<TextureRegion>(0.1f, frames);
        frames.clear();

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
        body.createFixture(fdef).setUserData("player");

        //Player box
        polygonShape.setAsBox(4,2);
        polygonShape.setAsBox(width,2, new Vector2(0,-height+height/4),0f);
        fdef.shape = polygonShape;
        //body.createFixture(fdef).setUserData("player");


        body.setUserData("player");

        cshape.dispose();



    }



    public void update(float delta){

        handleInput(delta);

        updateState();

        updateTextureRegion();

        updatePhysics();

        stateTimer = current == previous ? stateTimer + delta : 0;
        previous = current;
        if (current == State.DEAD && stateTimer > 1.5)
            current = State.STATIONARY;

        region2 = getFrame(delta);

        setPosition(body.getPosition().x - getWidth() / 2, body.getPosition().y - getHeight() /2 );
        fire();



    }

    public void updateTextureRegion(){

        //set texture region depending on state
        if(current == State.STATIONARY)
            textureRegion = new TextureRegion(spriteSheet, 41,40,40,48);
        if(current == State.MOVING_LEFT)
            textureRegion = new TextureRegion(spriteSheet, 0,40,40,48);
        if(current == State.MOVING_RIGHT)
            textureRegion = new TextureRegion(spriteSheet, 82,40,40,48);


    }


    @Override
    public void draw(Batch batch) {
        update(Gdx.graphics.getDeltaTime());
        batch.begin();
            //batch.draw(texture,body.getPosition().x - width, body.getPosition().y -height, width * 2, height);

        //batch.draw(textureRegion,body.getPosition().x - width, body.getPosition().y -height, width * 2, height);

        batch.draw(region2,body.getPosition().x - width, body.getPosition().y - height, width * 2, height);

        batch.end();
    }

    public void dispose(){
        //body.getWorld().destroyBody(body);
    }

    public void fire(){

        if (TimeUtils.millis() - lastBulletTime > bulletDelay && isFiring) {
            bullets.add( new Bullet( 150f/PPM, 90, 1.2f,0f));
            bullets.add( new Bullet( 150f/PPM, 110, 1.1f,1f));
            bullets.add( new Bullet( 150f/PPM, 70, 1.1f,-1f));
            lastBulletTime = TimeUtils.millis();

        }
    }


    public void hit(){
        Gdx.app.log("player", " is hit");

        current = State.DEAD;
        isFiring = !isFiring;
        lives--;
    }


    private void updateState(){

        //set state//
        if(body.getLinearVelocity().x > 10 && current != State.DEAD)
            current = State.MOVING_RIGHT;

        if(body.getLinearVelocity().x < -10 && current != State.DEAD)
            current = State.MOVING_LEFT;

        if(body.getLinearVelocity().x < 10 && body.getLinearVelocity().x > -10  && current != State.DEAD)
            current = State.STATIONARY;

    }

    public TextureRegion getFrame(float dt) {


        TextureRegion region;

        //depending on the state, get corresponding animation keyFrame.
        switch (current) {
            case DEAD:
                region = dead.getKeyFrame(stateTimer, false);
                break;

            case STATIONARY:
                region = stationary.getKeyFrame(stateTimer,true);
                break;

            case MOVING_LEFT:
                region = moving_left.getKeyFrame(stateTimer,true);
                break;

            case MOVING_RIGHT:
                region = moving_right.getKeyFrame(stateTimer,true);
                break;

            default:
                region = stationary.getKeyFrame(stateTimer, true);
                break;
        }



        return region;


    }


    public float getStateTimer(){
        return stateTimer;
    }


    public void handleInput(float delta){



        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)){
            body.applyLinearImpulse( new Vector2(-forceDefault,0),player.body.getWorldCenter(), true);
        }

        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)){
            body.applyLinearImpulse( new Vector2(forceDefault,0),player.body.getWorldCenter(), true);
        }

        if (Gdx.input.isKeyPressed(Input.Keys.UP)){
            body.applyLinearImpulse( new Vector2(0, forceDefault),player.body.getWorldCenter(), true);
        }

        if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            body.applyLinearImpulse( new Vector2(0,-forceDefault),player.body.getWorldCenter(), true);
        }

        if (Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
            setPosition(MyGame.W/2, MyGame.H/2);
        }




    }


    public void updatePhysics(){
        position = body.getPosition();
        velocity = body.getLinearVelocity();

        //Set Bounds for body to stay within defined world.
        if(position.x < -10)
            body.setTransform(0, position.y, body.getAngle());

        if(position.x > MyGame.W)
            body.setTransform(MyGame.W, position.y, body.getAngle());

        if(position.y < -10)
            body.setTransform(position.x, 0, body.getAngle());

        if(position.y > MyGame.H)
            body.setTransform(position.x, MyGame.H, body.getAngle());


        //Restrict Terminal velocity

        if (velocity.x > velocityT)
            body.setLinearVelocity(velocityT, velocity.y);

        if (velocity.x < -velocityT)
            body.setLinearVelocity(-velocityT, velocity.y);

        if (velocity.y > velocityT)
            body.setLinearVelocity(velocity.x, velocityT);

        if (velocity.y < -velocityT)
            body.setLinearVelocity(velocity.x, -velocityT);
        /**/


    }



}
