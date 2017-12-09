package com.shujrah.spaceshooter.Sprites;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.World;
import com.shujrah.spaceshooter.MyGame;
import com.shujrah.spaceshooter.MyLib;
import com.shujrah.spaceshooter.Screens.MyScreen;

import static com.shujrah.spaceshooter.MyGame.player;
import static com.shujrah.spaceshooter.MyLib.*;


/**
 * Created by waqar on 12/11/2017.
 */

public class gameCollisions implements ContactListener {
    private final String TAG = "Contact Listener";
    World world;
    Body bodyA, bodyB;

    @Override
    public void beginContact(Contact contact) {
        //Gdx.app.log(TAG, "Collision!");

        Fixture fixtureA = contact.getFixtureA();
        Fixture fixtureB = contact.getFixtureB();

        String colliders = fixtureA.getUserData() +"-" + fixtureB.getUserData();
//        Gdx.app.log(TAG, colliders);

        if(colliders.equals("player-Enemy") || colliders.equals("Enemy-player") )
        {
            //Gdx.app.log(TAG, colliders);
            player.hit();

        }



        //Gdx.app.log(TAG, "between " +  fixtureA.getBody().getUserData()  +" and " + fixtureB.getBody().getUserData());

        world = fixtureA.getBody().getWorld();
        bodyA = fixtureA.getBody();
        bodyB = fixtureB.getBody();



//        MyLib.llog(1f, "Collision! " + fixtureA.getUserData().getClass().getName() +" - "+ fixtureB.getUserData().getClass().getName());
//        MyLib.llog(1f, "Collision! " + bodyA.getUserData().typeStr );

        if(!bodyA.equals(null))
            ;//llog(1f, "fixA is null");

        //MyLib.llog(1f, "Collision! " + fixtureA.getUserData().toString() );



        if(bodyA.getUserData() instanceof Bullet && bodyB.getUserData() instanceof Enemy) {
            //Gdx.app.log(TAG, "Bullet - Enemy");
            ((Bullet) bodyA.getUserData()).dispose();
            ((Enemy) bodyB.getUserData()).dispose();
        }

        if(bodyB.getUserData() instanceof Bullet && bodyA.getUserData() instanceof Enemy) {
            //Gdx.app.log(TAG, "Bullet - Enemy");
            ((Bullet)bodyB.getUserData() ).dispose();
            ((Enemy)bodyA.getUserData()  ).dispose();

        }


        if(bodyA.getUserData() instanceof Player && bodyB.getUserData() instanceof Enemy) {
            //Gdx.app.log(TAG, "Bullet - Enemy");
            ((Player) bodyA.getUserData()).hit();
            ((Enemy) bodyB.getUserData()).dispose();
        }

        if(bodyB.getUserData() instanceof Player && bodyA.getUserData() instanceof Enemy) {
            //Gdx.app.log(TAG, "Bullet - Enemy");
            ((Player)bodyB.getUserData() ).hit();
            ((Enemy)bodyA.getUserData()  ).dispose();

        }

//        if(fixtureA.getUserData() == "")

//
//        if(fixtureA.getUserData() instanceof Bullet && fixtureB.getUserData() instanceof Enemy){
//            ((Bullet) fixtureA.getUserData()).dispose();
//            ((Enemy) fixtureB.getUserData()).dispose();
//
//            llog(1, "Bullet Collided with Enemy");
//        }
//
//        if(fixtureB.getUserData() instanceof Bullet && fixtureA.getUserData() instanceof Enemy){
//            ((Bullet) fixtureA.getUserData()).dispose();
//            ((Enemy) fixtureB.getUserData()).dispose();
//
//            llog(1, "Bullet Collided with Enemy");
//        }

    }

    @Override
    public void endContact(Contact contact) {

    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {
        //world.destroyBody( bodyA );
        //world.destroyBody( bodyB );

//        MyScreen.bodyDeletionList.add(bodyA);
//        MyScreen.bodyDeletionList.add(bodyB);

        bodyA = null;
        bodyB = null;



    }
}
