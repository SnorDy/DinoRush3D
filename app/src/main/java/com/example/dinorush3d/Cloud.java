package com.example.dinorush3d;

import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;

public class Cloud extends GameObject{
    private Spatial spatial;
    private float Vx=0.02f;
    private boolean isActive=true;

    public Cloud(Spatial cloud,float x,float y){
        super(cloud,true);
        this.spatial = cloud;
        Vector3f pos  = spatial.getLocalTranslation();
        pos.setX(x);
        pos.setY(y);
        cloud.setLocalTranslation(pos);
    }
    public void update() {
        Vector3f pos = spatial.getLocalTranslation();
        float x = pos.getX();
        pos.setX(x-Vx);
        spatial.setLocalTranslation(pos);
        if (x< -6)this.isActive=false;

    }
    public void setActive(boolean f){
        this.isActive = f;
    }
    public boolean isActive() {
        return isActive;
    }
}