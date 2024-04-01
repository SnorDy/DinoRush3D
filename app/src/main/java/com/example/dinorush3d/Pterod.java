package com.example.dinorush3d;

import com.jme3.anim.AnimComposer;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;

public class Pterod extends GameObject {
    private boolean isActive;
    private float Vx=0.1f;
    private AnimComposer composer;
    private Node pterod;
    private Spatial spatial;

    public Pterod(Node pterod, AnimComposer composer,Spatial pterod_spatial,float x){
        super(pterod_spatial,true);
        this.pterod=pterod;
        this.spatial=pterod_spatial;
        this.composer = composer;
        Vector3f pos  = pterod.getLocalTranslation();
        pos.setX(x);
        pterod.setLocalTranslation(pos);

    }
    public void update(){
        Vector3f pos = this.spatial.getLocalTranslation();
        float x = pos.getX();
        pos.setX(x-Vx);
        this.spatial.setLocalTranslation(pos);
        if (pos.x<-5)this.setActive(false);
    }
    public boolean isActive() {
        return isActive;
    }
    public void setActive(boolean f){this.isActive=f;}
    public void setX(float x){
        Vector3f pos = this.spatial.getLocalTranslation();
        pos.setX(x);
        this.spatial.setLocalTranslation(pos);
    }
    public  void setY(float y){
        Vector3f pos = this.spatial.getLocalTranslation();
        pos.setY(y);
        this.spatial.setLocalTranslation(pos);
    }
}
