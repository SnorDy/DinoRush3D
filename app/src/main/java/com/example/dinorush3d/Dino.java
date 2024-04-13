package com.example.dinorush3d;


import android.util.Log;

import com.jme3.anim.AnimComposer;
import com.jme3.anim.Armature;
import com.jme3.anim.tween.Tween;
import com.jme3.anim.tween.Tweens;
import com.jme3.anim.tween.action.Action;
import com.jme3.bounding.BoundingBox;
import com.jme3.bounding.BoundingVolume;
import com.jme3.bullet.collision.shapes.CollisionShape;
import com.jme3.bullet.control.CharacterControl;
import com.jme3.bullet.util.CollisionShapeFactory;
import com.jme3.collision.CollisionResults;
import com.jme3.math.Transform;
import com.jme3.math.Vector3f;
import com.jme3.scene.Mesh;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;

public class Dino {
    private boolean isAlive=true,isBent = false;;
    private boolean up = false,down=false;
    private float jump_y,start_y;
    public float jump_speed = 0.135f;
    private AnimComposer composer;
    private Node dino,init_dino;
    private Node initDino;




    public Dino(Node dino, AnimComposer composer,Node dino1){

        this.composer=composer;

        this.dino = dino;
        this.initDino = dino1;


//        composer = dino.getChild("_31").getControl(AnimComposer.class);
//        composer.setGlobalSpeed(1.5f);
//        composer.setCurrentAction("chrome dino run");
        start_y=dino.getLocalTranslation().getY();
        jump_y=start_y+jump_speed*20;

        composer.setGlobalSpeed(1.6f);
        composer.setCurrentAction("chrome dino run");



    }
    public void  setAnim(String s){
        if (isAlive()){
            if (s=="chrome dino jump"){
                Action jump = composer.action(s);
                Action land = composer.action("chrome dino land");
                Tween doneTween = Tweens.callMethod(composer, "setCurrentAction", "chrome dino land");
                Action landOnce = composer.actionSequence("LandOnce", land, doneTween);
                Tween doneTween2 = Tweens.callMethod(composer, "setCurrentAction", "LandOnce");
                Action jumpOnce = composer.actionSequence("JumpOnce", jump, doneTween2);
                composer.setCurrentAction("JumpOnce");}
            else composer.setCurrentAction(s);}
        else if (s=="chrome dino death"&&!this.isBent){
//            Vector3f pos = dino.getLocalTranslation();
//            pos.setY(pos.getY()+0.3f);
//            dino.setLocalTranslation(pos);
            Action death = composer.action(s);
            Tween doneTween = Tweens.callMethod(composer, "setCurrentAction", "chrome dino idle");
            Action DeathOnce = composer.actionSequence("DeathOnce", death, doneTween);
            composer.setCurrentAction("DeathOnce");

        }
        else composer.setCurrentAction(s);
    }
    public boolean IsDown() {
        return down;
    }
    public void update_model(Node model,AnimComposer composer){this.dino = model; this.composer = composer;}

    public int intersect(Spatial s){
        BoundingVolume bv;
        CollisionResults results = new CollisionResults();
        Vector3f v = dino.getWorldBound().getCenter();

        if (this.isBent) bv = new BoundingBox(v,0.1f,0.35f,0.6f);
        else bv = new BoundingBox(v,0.1f,0.6f,0.6f);

        return bv.collideWith(s.getWorldBound(),results);}

    public void SetDown(boolean down) {
        this.down = down;
    }

    public boolean IsUp() {
        return up;
    }

    public void SetUp(boolean up) {
        this.up = up;
    }

    public boolean isAlive() {
        return isAlive;
    }

    public void setAlive(boolean alive) {
        isAlive = alive;
    }

    public void setBent(boolean f){this.isBent= f;}
    public boolean isBent(){return this.isBent;}

    public void update()  {

        if (!isAlive()){

            Log.d("DINO","ISALIVE "+isAlive());
            if (composer.getCurrentAction()!=null){
                if (composer.getCurrentAction().toString().contains("chrome dino idle"))
                    composer.reset();}
            Vector3f pos = dino.getLocalTranslation();
            if (pos.getY()>start_y){
                pos.setY(pos.getY()-jump_speed);}
            else if (pos.getY()<start_y){
                pos.setY(start_y);}
            dino.setLocalTranslation(pos);
        }
        else{

            Vector3f pos = dino.getLocalTranslation();

            float y = pos.getY();
            if (y>=jump_y){SetDown(true);SetUp(false);y=jump_y;}
            if (y<=start_y){SetDown(false);y=start_y; }

            pos.setY(y);

            if ((IsUp())&&(y!=jump_y)) { pos.setY( (y+jump_speed-0.025f));}
            else {SetDown(true);SetUp(false);}

            if ((y!=this.start_y)&&(IsDown())){pos.setY( (y-jump_speed));}
            else {SetDown(false);}
            if (composer.getCurrentAction().toString().contains("chrome dino land")&&(y==start_y)&&(!IsUp())) {
                composer.setCurrentAction("chrome dino run");


//            composer.setCurrentAction("chrome dino run");
//            Log.d("ISJUMP","NOJUMP "+ composer.getCurrentAction().toString());
//            composer.setCurrentAction("chrome dino run");


//            dino.rotate(-0.2f,0,0);



            }
            dino.setLocalTranslation(pos);}



    }





}