package com.example.dinorush3d;

import com.jme3.bounding.BoundingBox;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;

public class Cactus extends GameObject{
    private Spatial spatial;
    private float Vx=0.15f;
    private boolean isActive=true;
    Node node_cactus;
    public Cactus(Spatial cactus,float x){
        super(cactus,true);
        this.spatial = cactus;

        this.node_cactus=(Node) cactus;
        node_cactus.setModelBound(new BoundingBox());
        node_cactus.updateModelBound();
        Vector3f pos  = cactus.getLocalTranslation();
        pos.setX(x);
        cactus.setLocalTranslation(pos);

    }
    public void update() {
        Vector3f pos = spatial.getLocalTranslation();
        float x = pos.getX();
        pos.setX(x-Vx);
        spatial.setLocalTranslation(pos);
        if (x< -10)this.isActive=false;

    }
    public void setVx(float n){this.Vx=n;}

    public Node getNode(){return  node_cactus;}
    public boolean isActive() {
        return isActive;
    }
    public void setActive(boolean f){
        this.isActive = f;
    }
}