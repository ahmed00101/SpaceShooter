package com.shujrah.spaceshooter.Sprites;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;
import com.shujrah.spaceshooter.MyGame;
import com.shujrah.spaceshooter.Screens.MyScreen;

import static com.shujrah.spaceshooter.MyGame.PPM;
import static com.shujrah.spaceshooter.MyGame.spriteSheet;
import static com.shujrah.spaceshooter.MyGame.sr;

/**
 * Created by waqar on 30/10/2017.
 */

public class Enemy extends gameObject {

    public static Array<Enemy> enemies;

    public MyScreen screen;
    public Body body;
    World world;
    //Texture texture;
    public Boolean destroyFlag = false;
    Long created;
    public final String typeStr = "Enemy";
    FixtureDef fdef;
    public static final long spawnDelay = 500;
    TextureRegion textureRegion;

    enum State { MOVING, DEAD };
    private State current, previous;
    private float stateTimer;

    Animation<TextureRegion> moving, dead;

    public static long lastEnemyTime;
    public float x, y, width, height, vy, angle;



    public Enemy(MyScreen myScreen) {
        created = TimeUtils.millis();
        this.screen = myScreen;
        this.world = screen.getWorld();
        angle = 0;
        defineBody();
        //textureRegion = new TextureRegion(spriteSheet, 3,244, 55,  60);

        stateTimer = 0;
        current = State.MOVING;

        updateFrames();

    }


    public void update(float delta){
        vy -= delta * 10;
        //this.y -= vy;

        stateTimer = current == previous ? stateTimer + delta : 0;
        previous = current;


        textureRegion = getFrame(delta);

        this.body.setLinearVelocity(0, vy);


//        if( (TimeUtils.millis() - created) > spawnDelay)
//            destroyFlag = true;

        if(body.getPosition().y > MyGame.H || body.getPosition().y < 0)
            destroyFlag = true;
    }

    public void draw(Batch batch){




//        sr.begin(ShapeRenderer.ShapeType.Filled);
//        sr.rect(body.getPosition().x - width/2,
//               body.getPosition().y - height/2,
//               width,height, //width, height,
//               Color.RED,Color.PINK, Color.RED, Color.MAROON);
//
//        sr.end();


        batch.begin();
//            batch.draw(textureRegion,
//                    body.getPosition().x, body.getPosition().y, //x, y,
//                    0,0, //body.getPosition().x, body.getPosition().y, //originX, originY,
//                    width, height,
//                    1,1, //scaleX, scaleY,
//                    angle);



        batch.draw(textureRegion,
                body.getPosition().x - width/2, body.getPosition().y - height/2, //x, y,
                width, height
        );

        batch.end();





    }




    public void defineBody(){

        BodyDef bdef = new BodyDef();
        bdef.type = BodyDef.BodyType.DynamicBody;
        //bdef.position.set(MyGame.W/2, MyGame.H/2);

        bdef.position.set(MyGame.W/4 + MathUtils.random() * MyGame.W/2, MyGame.H/2 + MathUtils.random() * MyGame.H/2 );
        body = world.createBody(bdef);


        width = 50/PPM;
        height = 50/PPM;

        fdef = new FixtureDef();

        Vector2[] vertices = new Vector2[4];
        //left bottom
        vertices[0] = new Vector2(-width/2,  -height/2);
        //left top
        vertices[1] = new Vector2(-width/2,  height/2);
        //right top
        vertices[2] = new Vector2(width/2,  height/2);
        //right bottom
        vertices[3] = new Vector2(width/2,  -height/2);
//

        PolygonShape polygonShape = new PolygonShape();
        polygonShape.set(vertices);

        CircleShape shape = new CircleShape();
        shape.setRadius(width);

        //fdef.shape = shape;

        fdef.shape = polygonShape;

        body.createFixture(fdef).setUserData("Enemy");
        body.setUserData(this);
        shape.dispose();
        polygonShape.dispose();



    }

    public static void spawnEnemies(MyScreen myScreen, float delta){

        if (TimeUtils.millis() - lastEnemyTime > spawnDelay) {
            enemies.add(new Enemy(myScreen));
            lastEnemyTime = TimeUtils.millis();
        }

    }


    public void dispose(){
        destroyFlag = true;
        body.setUserData(null);
        screen = null;
        world = null;

    }

public void updateFrames(){

    Array<TextureRegion> frames = new Array<TextureRegion>();

    //get run animation frames and add them to spaceship Animations
    for(int i = 0; i < 5; i++)
        frames.add(new TextureRegion(spriteSheet, 300, i*50, 45, 50));
    moving = new Animation<TextureRegion>(0.2f, frames);
    frames.clear();

    for(int i = 0; i < 7; i++)
        frames.add(new TextureRegion(spriteSheet, 350, i*50, 45, 50));
    for(int i = 7; i > 0; i--)
        frames.add(new TextureRegion(spriteSheet, 350, i*50, 45, 50));


    dead = new Animation<TextureRegion>(0.1f, frames);
    frames.clear();

    frames = null;


}



    public void updateTextureRegion(){



    }





    public TextureRegion getFrame(float dt) {

        TextureRegion region;

        //depending on the state, get corresponding animation keyFrame.
        switch (current) {
            case DEAD:
                region = dead.getKeyFrame(stateTimer, false);
                break;

            case MOVING:
                region = moving.getKeyFrame(stateTimer, true);
                break;

            default:
                region = moving.getKeyFrame(stateTimer, true);
                break;
        }
        return region;
    }




}
