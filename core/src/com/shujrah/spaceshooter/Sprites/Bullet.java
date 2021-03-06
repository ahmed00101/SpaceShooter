package com.shujrah.spaceshooter.Sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.utils.TimeUtils;
import com.shujrah.spaceshooter.MyGame;
import com.shujrah.spaceshooter.MyLib;

import static com.shujrah.spaceshooter.MyGame.PPM;
import static com.shujrah.spaceshooter.MyGame.player;
import static com.shujrah.spaceshooter.MyGame.spriteSheet;
import static com.shujrah.spaceshooter.MyGame.sr;
import static com.shujrah.spaceshooter.MyGame.world;

/**
 * Created by waqar on 30/10/2017.
 */

public class Bullet extends gameObject {

    float x, y, width, height, speed, angle, acceleration, angleDelta;
    public Body body;
    public final String typeStr = "Bullet";
    Long created;
    public Boolean destroyFlag = false;


    TextureRegion textureRegion;


    public Bullet( float speed, float angle, float acceleration, float angleDelta) {
        created = TimeUtils.millis();
        this.speed = speed;
        this.angle = angle;
        this.acceleration = acceleration;
        this.angleDelta = angleDelta;
        defineBody();
        updateFrames();
        textureRegion = new TextureRegion(spriteSheet, 25,134, 8,  12);
        //Gdx.app.log("Bullet", "" + speed);

    }



    public void update(float delta){

        x = body.getPosition().x;
        y = body.getPosition().y;

        angle += angleDelta;
        //body.setTransform(body.getPosition().x, body.getPosition().y, angle);
        //body.setBullet(true);
        speed += acceleration;

        //rotate(angle);
        //setRotation(angle);


        //body.setLinearVelocity(speed * MathUtils.cosDeg(angle), speed * MathUtils.sinDeg(angle));
        body.setLinearVelocity(speed * MathUtils.cosDeg(angle), speed * MathUtils.sinDeg(angle));


        if( (TimeUtils.millis() - created) > 9000)
            destroyFlag = true;

        if(     body.getPosition().y > MyGame.H +1 ||
                body.getPosition().y < -1          ||
                body.getPosition().x < -1          ||
                body.getPosition().x > MyGame.W +1 )
            destroyFlag = true;

    }





    public void draw(Batch batch){

        batch.begin();
            batch.draw(textureRegion, x - width/2, y - height/2, width, height );
//            batch.draw(textureRegion, x - width/2, y - height/2,
//                    x, y, width, height, 1, 1, 3, false);

//SpriteBatch.draw(textureRegion, x, y, originX, originY, width, height, scaleX, scaleY, rotation);
        batch.end();


//        sr.begin(ShapeRenderer.ShapeType.Filled);
//        sr.rect(body.getPosition().x - width/2,
//                body.getPosition().y - height/2,
//                width, height,
//                Color.BLUE, Color.SKY, Color.BLUE, Color.NAVY);

//        sr.rect(body.getPosition().x - width/2,
//                body.getPosition().y - height/2,
//                width, height,
//                Color.BLUE, Color.NAVY, Color.BLUE, Color.NAVY);

//        sr.rect(body.getPosition().x + width/2,
//                body.getPosition().y + height/2,
//                width/2, height,
//                Color.BLUE, Color.WHITE, Color.WHITE, Color.NAVY);

//        sr.rect(body.getPosition().x,
//                body.getPosition().y,
//                body.getPosition().x, //body.getPosition().x,
//                body.getPosition().y, //body.getPosition().y,
//                width,
//                height,
//                1f ,1f,
//                angle,
//                Color.BLUE, Color.BLUE,Color.CORAL,Color.CYAN
//                );
//        sr.end();

    }



    public void defineBody(){

        BodyDef bdef = new BodyDef();
        bdef.type = BodyDef.BodyType.DynamicBody;

        bdef.position.set(player.body.getPosition().x, player.body.getPosition().y  + 0 + 0*height/2);
        body = world.createBody(bdef);
        bdef = null;

        width = 10/PPM;
        height = 20/PPM;

        FixtureDef fdef = new FixtureDef();

        Vector2[] vertices = new Vector2[4];
        vertices[0] = new Vector2(-width/2,  -height/2);
        vertices[1] = new Vector2(-width/2,  height/2);
        vertices[2] = new Vector2(width/2,  height/2);
        vertices[3] = new Vector2(width/2,  -height/2);

        PolygonShape polygonShape = new PolygonShape();
        polygonShape.set(vertices);


        fdef.shape = polygonShape;
        fdef.isSensor = true;
        body.createFixture(fdef).setUserData("playerBullet");
        body.setUserData(this);
        body.setAwake(true);
        polygonShape.dispose();
        fdef = null;

    }

    public void dispose(){
        destroyFlag = true;

    }



    public void updateFrames(){



    }



}
