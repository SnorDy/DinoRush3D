package com.example.dinorush3d;

import com.jme3.math.Vector3f;
import com.jme3.scene.Spatial;

public class GameObject {
    private Spatial spatial;
    private boolean isActive=true;
    public GameObject(Spatial spatial,boolean isActive){
        this.isActive=isActive;
        this.spatial = spatial;

    }
    public boolean isActive() {
        return isActive;
    }
    public void setX(float x){
        Vector3f pos = spatial.getLocalTranslation();
        pos.setX(x);
    }
    public void setY(float y){
        Vector3f pos = spatial.getLocalTranslation();
        pos.setX(y);
    }
    public void setActive(boolean f){this.isActive=f;}
    public Spatial getSpatial(){return spatial;}
}