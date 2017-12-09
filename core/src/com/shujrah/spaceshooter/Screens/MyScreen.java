package com.shujrah.spaceshooter.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.shujrah.spaceshooter.MyGame;
import com.shujrah.spaceshooter.MyLib;
import com.shujrah.spaceshooter.Sprites.Bullet;
import com.shujrah.spaceshooter.Sprites.Enemy;
import com.shujrah.spaceshooter.Sprites.Player;
import com.shujrah.spaceshooter.Sprites.gameCollisions;
import java.util.Iterator;
import static com.shujrah.spaceshooter.MyGame.PPM;
import static com.shujrah.spaceshooter.MyGame.debugRendererOn;
import static com.shujrah.spaceshooter.Sprites.Enemy.enemies;
import static com.shujrah.spaceshooter.MyGame.world;
import static com.shujrah.spaceshooter.MyGame.player;
import static com.shujrah.spaceshooter.MyGame.sr;
import static com.shujrah.spaceshooter.MyGame.bullets;

/**
 * Created by waqar on 29/10/2017.
 */



public class MyScreen implements Screen {
    final String TAG = "MyScreen ";
    public MyGame game;


    FitViewport gamePort;
    OrthographicCamera cam;

    Box2DDebugRenderer debugRenderer;
    Texture texture;



    public MyScreen(MyGame game) {



        float w = Gdx.graphics.getWidth();
        float h = Gdx.graphics.getHeight();
        cam = new OrthographicCamera(game.H/MyGame.PPM, game.H / MyGame.PPM * (h / w));

        cam.translate(game.W/2, game.H/2);


        gamePort = new FitViewport(game.W,game.H, cam);
        gamePort.apply();

        this.game = game;
        //sr = new ShapeRenderer();
        world = new World(new Vector2(0, 0f),true);
        world.setContactListener(new gameCollisions());
        debugRenderer = new Box2DDebugRenderer();

        player = new Player(this);
        player.setPosition(game.W/2, game.H/2);

        texture = new Texture( Gdx.files.internal("spaceship.png"));

        //Initialize arrays
        bullets = new Array<Bullet>();
        enemies = new Array<Enemy>();

        //setGround();


    }

    public World getWorld(){
        return world;
    }

    @Override
    public void show() {

    }

    public Vector3 getMousePosInGameWorld() {
        return cam.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));
    }

    public void handleInput(float delta){



        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)){
            player.body.applyLinearImpulse( new Vector2(-1f,0),player.body.getWorldCenter(), true);
        }

        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)){
            player.body.applyLinearImpulse( new Vector2(1f,0),player.body.getWorldCenter(), true);
        }

        if (Gdx.input.isKeyPressed(Input.Keys.UP)){
            player.body.applyLinearImpulse( new Vector2(0,1f),player.body.getWorldCenter(), true);
        }

        if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            player.body.applyLinearImpulse( new Vector2(0,-1f),player.body.getWorldCenter(), true);
        }

        if (Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
            player.setPosition(MyGame.W/2, MyGame.H/2);
        }



        if (Gdx.input.isKeyPressed(Input.Keys.X)) {
            player.body.setLinearVelocity(0,0);
            //player.setPosition(MyGame.W/2, MyGame.H/2);
        }

        if(Gdx.input.getAccelerometerX() != 0){
            player.body.setLinearVelocity(
                    Gdx.input.getAccelerometerX() *100,
                    Gdx.input.getAccelerometerX() *100);
            //player.x -= Gdx.input.getAccelerometerX();
        }

        if(Gdx.input.getAccelerometerY() != 0){
//            player.body.setLinearVelocity( Gdx.input.getAccelerometerX(), 0);
//            player.y -= Gdx.input.getAccelerometerY();
            player.body.setLinearVelocity(
                    Gdx.input.getAccelerometerX() *10,
                    Gdx.input.getAccelerometerY() *10);
        }


        if (Gdx.input.isKeyPressed(Input.Keys.W) )
            cam.zoom += (0.01f);
        if (Gdx.input.isKeyPressed(Input.Keys.S) )
            cam.zoom -= (0.01f);
        if (Gdx.input.isKeyJustPressed(Input.Keys.A) )
            cam.rotate(1f);
        if (Gdx.input.isKeyJustPressed(Input.Keys.D) )
            cam.rotate(-1f);
        if (Gdx.input.isKeyJustPressed(Input.Keys.Z) )
            debugRendererOn = !debugRendererOn;

        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE) )
            player.isFiring = !player.isFiring;

        if (Gdx.input.isKeyJustPressed(Input.Keys.V) ){

//                    while(player.body.getJointList().removeAll())
                        player.body.getJointList().clear();
//                        player.body.getJointList().
        }










    }

    public void update(float delta){
        handleInput(delta);
        //gameCam.update();

        //cam.rotate(delta*2);
        //cam.translate(delta,delta);

//        Vector2 vp = player.body.getPosition();
//        Vector2 vc = new Vector2(cam.position.x, cam.position.y);

        //vp.

//        Vector2 campos = new Vector2( vc.x + (vp.x-vc.x)/10, vc.y + (vp.y-vc.y)/10 );


        //cam.position.set(campos.x, campos.y,0);
        cam.update();

        //cam.zoom +=delta/100;



        Enemy.spawnEnemies(this, delta);
        world.step(Gdx.graphics.getDeltaTime(), 6, 2);
        cleanUp();

        //MyLib.llog(5f, "Bullets: " + bullets.size + ", Enemies: "+ enemies.size);

        //MyLib.llog(2f,"World Bodies: " + world.getBodyCount());

    }









    @Override
    public void render(float delta) {

        update(delta);

        game.batch.setProjectionMatrix(cam.combined);
        sr.setProjectionMatrix(cam.combined);


        printGrid();


        //Draw and Update Enemies
        for(Enemy enemy: enemies){
            enemy.update(delta);
            enemy.draw(game.batch);
        }

        //Draw and Update Bullets
        for(Bullet bullet: bullets){
            bullet.update(delta);
            bullet.draw(game.batch);
        }


        player.draw(game.batch);

        if(debugRendererOn)
        debugRenderer.render(world, cam.combined);


    }


    public void cleanUp() {

        Iterator<Bullet> iter = bullets.iterator();
        while (iter.hasNext()) {
            Bullet b = iter.next();

            if ( b.destroyFlag ){
                world.destroyBody(b.body);
                iter.remove();
            }
        }

        Iterator<Enemy> iterB = enemies.iterator();
        while (iterB.hasNext()) {
            Enemy e = iterB.next();

            if ( e.destroyFlag && !world.isLocked()){
                iterB.remove();
                world.destroyBody(e.body);
                 }
        }



    }







    @Override
    public void resize(int width, int height) {
        gamePort.update(width,height);

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        world.dispose();

        game.batch.dispose();
        sr.dispose();
        game.dispose();

    }

    public OrthographicCamera getCam(){
        return this.cam;
    }


    public void setGround(){

        BodyDef bdef = new BodyDef();
        bdef.type = BodyDef.BodyType.StaticBody; //BodyDef.BodyType.DynamicBody;
        //bdef.position.set(MyGame.H/2/MyGame.PPM,5);
        bdef.position.set(MyGame.W/2/PPM,5/PPM);


        Body ground;// = new Body();
        ground = world.createBody(bdef);


        FixtureDef fdef = new FixtureDef();
        PolygonShape polygonShape = new PolygonShape();
        polygonShape.setAsBox(MyGame.H/2/PPM,5);


        fdef.shape = polygonShape;
//        fdef.density = 1;

        ground.createFixture(fdef).setUserData("ground");




    }

    void printGrid(){

        sr.begin(ShapeRenderer.ShapeType.Filled);
            sr.rect(-10,-10, MyGame.W+20, MyGame.H+20, Color.GRAY, Color.CYAN, Color.BLUE, Color.GRAY);
        sr.end();

        sr.begin(ShapeRenderer.ShapeType.Line);
        for (int j = 0; j < MyGame.W; j += MyGame.W/10) {
            for (int i = 0; i < MyGame.H; i += MyGame.W/10) {
                sr.rect(j, i, MyGame.W/10, MyGame.W/10, Color.BLUE, Color.NAVY, Color.BLUE, Color.NAVY);
            }
        }
        sr.end();
    }
}



