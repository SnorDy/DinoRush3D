package com.example.dinorush3d;


import android.util.Log;
import android.widget.Button;


import com.jme3.anim.AnimComposer;
import com.jme3.anim.Armature;
import com.jme3.anim.SkinningControl;
import com.jme3.animation.AnimChannel;
import com.jme3.animation.AnimControl;
import com.jme3.animation.AnimEventListener;
import com.jme3.animation.Skeleton;
import com.jme3.animation.SkeletonControl;
import com.jme3.app.AndroidHarnessFragment;
import com.jme3.app.SimpleApplication;
import com.jme3.collision.CollisionResults;
import com.jme3.font.BitmapFont;
import com.jme3.font.BitmapText;
import com.jme3.font.Rectangle;
import com.jme3.input.InputManager;
import com.jme3.input.TouchInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.MouseButtonTrigger;
import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Quaternion;
import com.jme3.math.Ray;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.niftygui.NiftyJmeDisplay;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Box;
import com.jme3.system.JmeContext;
import com.jme3.system.JmeSystem;
import com.jme3.texture.Texture;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Random;

import de.lessvoid.nifty.Nifty;

public class Game extends SimpleApplication implements ActionListener, AnimEventListener {
    private Dino dino;
    private Material m;
    private Node player_model,player_model2,anim_model;
    private AnimComposer dino_composer, pterod_composer;
    private File file;
    private boolean game_over = false;
    private int max_score = 0;
    private String fileName = "max_score.save";
    private boolean new_game_flag = false;
    private double score;
    private int pterod_time = 2000;
    private long prevTime;
    private Spatial cactus_spat, cloud_spat,pterod_model;
    private Geometry background;
    private Cloud[] cloud_arr = new Cloud[3];
    private Cactus[] cactus_arr = new Cactus[2];
    private Pterod pterod;
    private boolean pterod_is_ready = false;


    @Override
    public void simpleInitApp() {

        viewPort.setBackgroundColor(ColorRGBA.fromRGBA255(50, 50, 50, 0));

        cam.setRotation(new Quaternion().fromAngleAxis(2.9151156f, new Vector3f(0, 1, 0)));
        cam.setLocation(cam.getLocation().add(new Vector3f(0, 0, 1.5f)));
        read_score();

        prevTime = System.currentTimeMillis();//Время на начало потока

        AmbientLight ambient = new AmbientLight();
        ambient.setColor(ColorRGBA.Gray);
        rootNode.addLight(ambient);

        Vector3f v = new Vector3f(3, -3f, 3);
        Box b1 = new Box(v, 14, 1f, 2.5f);
        background = new Geometry("Floor", b1);

        Material m1 = new Material(assetManager, "Common/MatDefs/Light/Lighting.j3md");
//        Texture sand_texture = assetManager.loadTexture("drawable/sand1.jpg");
//        m1.setTexture("DiffuseMap",sand_texture);
        m1.setBoolean("UseMaterialColors", true);
//        m1.setColor("Ambient",ColorRGBA.fromRGBA255(57,62,70,0));
////
//        m1.setColor("Diffuse",ColorRGBA.fromRGBA255(57,62,70,0));
        m1.setColor("Ambient", ColorRGBA.fromRGBA255(33, 33, 33, 0));
////
        m1.setColor("Diffuse", ColorRGBA.fromRGBA255(33, 33, 33, 0));
        background.setMaterial(m1);
        background.setShadowMode(RenderQueue.ShadowMode.CastAndReceive);
        rootNode.attachChild(background);


        m = new Material(assetManager, "Common/MatDefs/Light/Lighting.j3md");
        m.setBoolean("UseMaterialColors", true);
        m.setColor("Diffuse", ColorRGBA.fromRGBA255(0, 224, 206, 0));
        m.setColor("Ambient", ColorRGBA.fromRGBA255(0, 224, 206, 0));
//        green_texture = assetManager.loadTexture("drawable/green2.jpg");
//        green_texture.setWrap(Texture.WrapMode.Repeat);
//        m.setTexture("DiffuseMap",green_texture);

        player_model = (Node) assetManager.loadModel("assets/update_dino.j3o");
        player_model.rotate(0, -1.5f, 0);
        player_model.setLocalScale(0.6f);
        player_model.setLocalTranslation(-2f, -2f, 3.5f);
        rootNode.attachChild(player_model);
        player_model2= (Node) player_model.clone();
        anim_model = (Node) player_model.getChild("_31").clone();
//        Material dino_mat = new Material(assetManager, "Common/MatDefs/Light/Lighting.j3md");
//        dino_mat.setBoolean("UseMaterialColors", true);Material.002
//        dino_mat.setColor("Diffuse",ColorRGBA.fromRGBA255(19,124,92,0));
//        dino_mat.setColor("Ambient",ColorRGBA.fromRGBA255(19,124,92,0));
//        player_model.setMaterial(dino_mat);
        dino_composer = player_model.getChild("_31").getControl(AnimComposer.class);
        Node n = (Node) player_model.getChild("_31");
        Node n1 =  (Node) n.getChild("body_30");
        Log.d("SKELETON",((Node)n1).getChildren().toString());

//        dino = new Dino(player_model, dino_composer);
//        SkinningControl sk = player_model.getChild("_31").getControl(SkinningControl.class);
//
//        sk.setHardwareSkinningPreferred(false);
//        Armature armature =  sk.getArmature();
        dino = new Dino(player_model, dino_composer,player_model2);

        cactus_spat = assetManager.loadModel("assets/cactus2.j3o");//расположение кактуса
        cactus_spat.setMaterial(m);
        cactus_spat.setLocalScale(0.8f);
        cactus_spat.setLocalTranslation(0, -2f, 3.5f);
        cactus_spat.setShadowMode(RenderQueue.ShadowMode.CastAndReceive);
        GenerateCactus();

        cloud_spat = assetManager.loadModel("assets/cloud.j3o");//расположение кактуса
        cloud_spat.setMaterial(m);
        cloud_spat.setLocalScale(0.8f);
        cloud_spat.setLocalTranslation(0, 1f, 3.5f);
        cloud_spat.rotate(0.1f, 0f, 0.05f);
        GenerateClouds();

        pterod_model = assetManager.loadModel("assets/pterod.j3o");
        pterod = new Pterod((Node) pterod_model, pterod_composer, pterod_model, 5f);
        pterod_composer = ((Node) pterod_model).getChild("_18").getControl(AnimComposer.class);
        pterod_composer.setCurrentAction("pteranodon flying");
        pterod_model.setLocalTranslation(0f, 0f, 3.5f);
        pterod_model.rotate(0, 1.5f, 0);
        pterod_model.setLocalScale(1.5f);




//        dino = new Dino("box", dino_box);
//        dino.setShadowMode(RenderQueue.ShadowMode.Cast);

//        object.setMaterial(m1);

//        Texture t = assetManager.loadTexture("drawable/sand.png");
//        t.setWrap(Texture.WrapMode.Repeat);
//        m1.setTexture("DiffuseMap",t);
//        m1.setBoolean("UseMaterialColors", true);
//        m1.setColor("Ambient", ColorRGBA.White);
//        m1.setColor("Diffuse", ColorRGBA.White.clone());
//        dino.setMaterial(m1);
//        rootNode.attachChild(dino);

        DirectionalLight l = new DirectionalLight();
        l.setDirection(new Vector3f(-1, -0.9f, -2f));
        rootNode.addLight(l);

        l = new DirectionalLight();
        l.setDirection(new Vector3f(-1, -0.9f, 2f));
        rootNode.addLight(l);


        flyCam.setEnabled(false);
        inputManager.addMapping("TAP", new MouseButtonTrigger(TouchInput.ALL));
        inputManager.addListener(this, "TAP");


    }

    public void simpleUpdate(float f) {


        if (dino.isAlive()) {
            update_score();
            Random random = new Random();

            if (pterod.isActive()) {
                pterod.update();

                if (!pterod.isActive()){
                    pterod_is_ready=false;
                    rootNode.detachChild(pterod.getSpatial());
                    GenerateCactus();
                Log.d("PTEROD",""+pterod.isActive());}
            }

            if (!pterod_is_ready&&!pterod.isActive()){//если птеродактиль не летит и не собирается
            for (int i = 0; i < 2; i++) {//обновляем положение кактусов
                if (!cactus_arr[i].isActive()) {
                    rootNode.detachChild(cactus_arr[i].getSpatial());
                    float last_coords = cactus_arr[(i + 1) % cactus_arr.length].getSpatial().getLocalTranslation().getX();
                    float coords = last_coords + 4f + random.nextFloat() + 8;
                    cactus_arr[i] = new Cactus(cactus_spat.clone(), coords);
                    rootNode.attachChild(cactus_arr[i].getSpatial());
                } else {
                    cactus_arr[i].update();
                    if ((dino.intersect(cactus_arr[i].getSpatial()) > 0)) {
                        dino.setAlive(false);
                        dino.setAnim("chrome dino death");
                    }

                }}}
            else {for (Cactus cact :cactus_arr){if (cact.isActive())cact.update();//если собирается,
                // то ждем пока кактусы буду за пределами экрана
                else rootNode.detachChild(cact.getSpatial());
                }}

            if (!cactus_arr[1].isActive()&&!cactus_arr[0].isActive()&&pterod_is_ready&&!pterod.isActive()){
                // если созданы все условия для вылета, то спавним
                Log.d("PTEROD_START",""+pterod.getSpatial().getLocalTranslation().getX());

                pterod.setActive(true);
                pterod.setX(12);
                pterod.setY(-0.1f);
                rootNode.attachChild(pterod.getSpatial());
                }
            if (pterod.isActive()&&dino.intersect(pterod.getSpatial())>0){
                dino.setAlive(false);
                dino.setAnim("chrome dino death");
            }

            long now = System.currentTimeMillis();
            if (now%pterod_time==0 &&dino.isAlive()&&!(pterod.isActive())){
                pterod_is_ready=true;
                pterod_time = 1300+random.nextInt(500);
            }



            for (int i = 0; i < cloud_arr.length; i++) {
                if (!cloud_arr[i].isActive()) {
                    rootNode.detachChild(cloud_arr[i].getSpatial());
                    float last_coords = cloud_arr[(i + 1) % cloud_arr.length].getSpatial().getLocalTranslation().getX();
                    float coords_x = last_coords + 6f + random.nextFloat() + 8;
                    float coords_y = random.nextFloat() + 0.7f;
                    cloud_arr[i] = new Cloud(cloud_spat.clone(), coords_x, coords_y);
                    rootNode.attachChild(cloud_arr[i].getSpatial());
                } else {
                    cloud_arr[i].update();
                }
            }


            Log.d("CAMERA", "" + cam.getRotation().toAngleAxis(new Vector3f()));

        } else {
            if (!game_over) {
                game_over = true;
                if (score > max_score) max_score = (int) score;
                save();
                pterod.setActive(false);
                pterod_is_ready = false;
            }
        }
        dino.update();
        if (new_game_flag) {
            new_game();
            new_game_flag = false;
        }


    }


    @Override
    public void onAction(String name, boolean isPressed, float tpf) {
        Vector2f pos = inputManager.getCursorPosition();

        if (name == "TAP" && isPressed) {

            if (pos.x>= cam.getWidth()/2){
                if (!dino.IsUp() && (!dino.IsDown()) && dino.isAlive()) dino.setAnim("chrome dino jump");
                 dino.SetUp(true);}
            if (pos.x< cam.getWidth()/2){
                if (!dino.IsUp() && (!dino.IsDown()) && dino.isAlive()) {dino.setAnim("chrome dino duck run");
                dino.setBent(true);}
               }

            Log.d("TAP",cam.getWidth()+ " "+ inputManager.getCursorPosition());
        }
        else if( name=="TAP"&& !isPressed){
            if (pos.x<cam.getWidth()){
            if (dino.isBent()){
                dino.setBent(false);
                rootNode.detachChild(player_model);
                player_model = (Node) player_model2.deepClone();
                rootNode.attachChild(player_model);
                dino_composer = player_model.getChild("_31").getControl(AnimComposer.class);
                if (dino.isAlive()){

                dino_composer.setCurrentAction("chrome dino run");
                    dino.update_model(player_model,dino_composer);}

                else {dino.update_model(player_model,dino_composer);dino.setAnim("chrome dino death");}





            }}

        }

    }

    public void setNew_game_flag(boolean flag) {
        new_game_flag = flag;
    }

    public int getMaxScore() {
        return max_score;
    }

    public int getScore() {
        return (int) score;
    }

    public void GenerateCactus() {
        float last_coords = 10;
        Random random = new Random();
        for (int i = 0; i < 2; i++) {

            float coords = last_coords + 4f + random.nextFloat() + 8;


            cactus_arr[i] = new Cactus(cactus_spat.clone(), coords);
            rootNode.attachChild(cactus_arr[i].getSpatial());
            last_coords = coords;
        }
    }

    public void GenerateClouds() {
        float last_coords = -2;
        Random random = new Random();
        for (int i = 0; i < cloud_arr.length; i++) {

            float coords_x = last_coords + random.nextFloat();
            float coords_y = random.nextFloat() + 0.8f;
            cloud_arr[i] = new Cloud(cloud_spat.clone(), coords_x, coords_y);
            rootNode.attachChild(cloud_arr[i].getSpatial());
            last_coords = coords_x + 6;
        }
    }

    public boolean IsGameOver() {
        return this.game_over;
    }

    public void update_score() {
        score += 0.01;

    }

    public void new_game() {
        read_score();
        score = 0;


        for (Cactus cact : cactus_arr) {
            rootNode.detachChild(cact.getNode());
        }
        GenerateCactus();

        game_over = false;
        dino.setAnim("chrome dino run");
        dino.setAlive(true);
        pterod.setActive(false);
        rootNode.detachChild(pterod.getSpatial());
        pterod_is_ready=false;

        prevTime = System.currentTimeMillis();//Время на начало потока

    }


    public void read_score() {//чтение рекорда из файла
        File folder = JmeSystem.getStorageFolder();
        if (folder != null && folder.exists()) {
            try {
                file = new File(folder.getAbsolutePath() + File.separator + fileName);
                if (file.exists()) {
                    FileInputStream fileIn = new FileInputStream(file);
                    max_score = fileIn.read();
                    Log.d("READ", max_score + "");


                } else {
                    file.createNewFile();
                    max_score = 0;
                    Log.d("WRITE", "" + "NORMALNO");
                    save();
                }

            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    public void save() {//сохаренение рекорда в файл
        File folder = JmeSystem.getStorageFolder();

        if (folder != null && folder.exists()) {
            if (file != null) {
                FileOutputStream fileOut = null;
                try {
                    fileOut = new FileOutputStream(file);
                    fileOut.write(max_score);
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if (fileOut != null) {
                        try {
                            fileOut.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
    }


    @Override
    public void onAnimCycleDone(AnimControl control, AnimChannel channel, String animName) {

    }

    @Override
    public void onAnimChange(AnimControl control, AnimChannel channel, String animName) {

    }


}


