package com.example.dinorush3d;




import android.util.Log;
import android.widget.Button;


import com.jme3.anim.AnimComposer;
import com.jme3.animation.AnimChannel;
import com.jme3.animation.AnimControl;
import com.jme3.animation.AnimEventListener;
import com.jme3.app.AndroidHarnessFragment;
import com.jme3.app.SimpleApplication;
import com.jme3.collision.CollisionResults;
import com.jme3.font.BitmapFont;
import com.jme3.font.BitmapText;
import com.jme3.font.Rectangle;
import com.jme3.input.TouchInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.MouseButtonTrigger;
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

public class Game extends SimpleApplication implements ActionListener, AnimEventListener{
    Dino dino;
    Cactus cactus;
    Material m;
    BitmapText score_label,max_score_label;
    Node player_model;
    AnimComposer composer;
    private File file;
    private Geometry restart_btn;
    private boolean game_over= false;
    private int max_score=0;
    private String fileName = "max_score.save";
    private boolean new_game_flag=false;
    Texture green_texture;
    private double score;
    Spatial cactus_spat;
    Geometry background;
    Cactus[] cactus_arr = new Cactus[2];
//    ArrayList<Cactus> cactus_arr = new ArrayList<Cactus>();

    @Override
    public void simpleInitApp() {
//        viewPort.setBackgroundColor(ColorRGBA.fromRGBA255(219,239,255,0));

        viewPort.setBackgroundColor(ColorRGBA.Gray);

        cam.setRotation(new Quaternion().fromAngleAxis(2.9151156f,new Vector3f(0,1,0)));
        cam.setLocation(cam.getLocation().add( new Vector3f(0,0,1.5f)));
        read_score();

//        BitmapFont guiFont = assetManager.loadFont("Interface/Fonts/Console.fnt");
//        score_label= new BitmapText(guiFont, false);//добавление полей для вывода счета и рекорда
//        score_label.setBox(new Rectangle(0, 0, 10, 1));
//        score_label.setLocalTranslation(-5.5f,3.8f,0);
//        score_label.rotate(0,-0.2f,0);
//        score_label.setText("0");
//        score_label.setSize(0.5f);

//        rootNode.attachChild(score_label);

//        max_score_label= new BitmapText(guiFont, false);
//        max_score_label.setBox(new Rectangle(0, 0, 10, 1));
//        max_score_label.setLocalTranslation(-5.5f,3f,0);
//        max_score_label.rotate(0,-0.2f,0);
//        max_score_label.setText("max: "+max_score);
//        max_score_label.setSize(0.5f);
//        rootNode.attachChild( max_score_label);


        Vector3f v = new Vector3f(3, -3f, 3);
        Box b1 = new Box(v, 14, 1f, 2.5f);
        background = new Geometry("Floor", b1);

        Material m1 = new Material(assetManager, "Common/MatDefs/Light/Lighting.j3md");
//        Texture sand_texture = assetManager.loadTexture("drawable/sand1.jpg");
//        m1.setTexture("DiffuseMap",sand_texture);
//        m1.setBoolean("UseMaterialColors", true);
//        m1.setColor("Ambient",ColorRGBA.fromRGBA255(255,255,173,0));
//
//        m1.setColor("Diffuse",ColorRGBA.fromRGBA255(255,255,173,0));
        background.setMaterial(m1);
        background.setShadowMode(RenderQueue.ShadowMode.CastAndReceive);
        rootNode.attachChild(background);


        m = new Material(assetManager, "Common/MatDefs/Light/Lighting.j3md");
        green_texture = assetManager.loadTexture("drawable/green2.jpg");
//        green_texture.setWrap(Texture.WrapMode.Repeat);
        m.setTexture("DiffuseMap",green_texture);

        player_model= (Node) assetManager.loadModel("assets/dino2.j3o");
        Spatial player_spatial = assetManager.loadModel("assets/dino2.j3o");
        player_model.rotate(0,-1.5f,0);
        player_model.setLocalScale(0.6f);
        player_model.setLocalTranslation(-2f, -2f, 3.5f);

        rootNode.attachChild(player_model);
        composer = player_model.getChild("_31").getControl(AnimComposer.class);

        dino = new Dino(player_model,composer,player_spatial);


        cactus_spat = assetManager.loadModel("assets/cactus2.j3o");//расположение кактуса
        cactus_spat.setMaterial(m);
        cactus_spat.setLocalScale(0.8f);
        cactus_spat.setLocalTranslation(0,-2f, 3.5f);
        cactus_spat.rotate(0, 0f, 0f);
        cactus_spat.setShadowMode(RenderQueue.ShadowMode.CastAndReceive);
        GenerateCactus();


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



        if (dino.isAlive()){
            update_score();
        Random random = new Random();
        for (int i = 0; i < 2; i++) {if (!cactus_arr[i].isActive()){
            rootNode.detachChild(cactus_arr[i].getSpatial());
            float last_coords = cactus_arr[(i+1)%cactus_arr.length].getSpatial().getLocalTranslation().getX();
            float coords = last_coords+4f+ random.nextFloat()+8;
            cactus_arr[i] = new Cactus(cactus_spat.clone(),coords);
            rootNode.attachChild(cactus_arr[i].getSpatial());}}


        for (Cactus cact: cactus_arr){
            cact.update();
            if ((dino.intersect(cact.getSpatial())>0)){dino.setAlive(false);dino.setAnim("chrome dino death");}

            Log.d("COLLIDE",(dino.intersect(cact.getSpatial())>0)+"");}
        Log.d("CAMERA", "" + cam.getRotation().toAngleAxis(new Vector3f()));

        }
        else { if (!game_over) {game_over=true;
            if (score>max_score) max_score=(int) score;
            save();}}
        dino.update();
        if (new_game_flag){new_game();
        new_game_flag=false;}






    }


    @Override
    public void onAction(String name, boolean isPressed, float tpf) {
        if  (!dino.IsUp()&&(!dino.IsDown())&&dino.isAlive())dino.setAnim("chrome dino jump");
        if (name =="TAP"&&isPressed){
             dino.SetUp(true);


        }

    }
    public void setNew_game_flag(boolean flag){
        new_game_flag=flag;
    }
    public int getMaxScore(){
        return max_score;
    }
    public int getScore(){return (int) score;}

    public void GenerateCactus(){
        float last_coords = 10;
        Random random = new Random();
        for (int i = 0; i < 2; i++) {

            float coords = last_coords+8f+ random.nextFloat();

            cactus_arr[i]=new Cactus(cactus_spat.clone(),coords);
            rootNode.attachChild(cactus_arr[i].getSpatial());
            last_coords=coords;
        }
    }
    public boolean IsGameOver(){
        return this.game_over;
    }

    public void update_score(){
        score+=0.01;

    }
    public void new_game(){

        read_score();
        score=0;
        for (Cactus cact: cactus_arr){
            rootNode.detachChild(cact.getNode());}
        GenerateCactus();
        game_over = false;
        dino.setAnim("chrome dino run");
        dino.setAlive(true);

    }


    public void read_score(){//чтение рекорда из файла
        File folder = JmeSystem.getStorageFolder();
        if (folder != null && folder.exists()) {
            try {
                file = new File(folder.getAbsolutePath() + File.separator + fileName);
                if (file.exists()) {
                    FileInputStream fileIn = new FileInputStream(file);
                    max_score =  fileIn.read();
                    Log.d("READ",max_score+"");


                } else {
                    file.createNewFile();
                    max_score=0;
                    Log.d("WRITE",""+"NORMALNO");
                    save();
                }

            } catch (IOException ex) {
                ex.printStackTrace();
            }}}

    public void save() {//сохаренение рекорда в файл
        File folder = JmeSystem.getStorageFolder();

        if (folder != null && folder.exists()) {
            if (file != null) {
                FileOutputStream fileOut=null;
                try {
                    fileOut = new FileOutputStream(file);
                    fileOut.write(max_score);} catch (IOException e) {
                    e.printStackTrace();
                }finally {
                    if (fileOut!=null){try {fileOut.close();} catch (IOException e) {
                        e.printStackTrace();
                    }
                    }
                }
            }}}



    @Override
    public void onAnimCycleDone(AnimControl control, AnimChannel channel, String animName) {

    }

    @Override
    public void onAnimChange(AnimControl control, AnimChannel channel, String animName) {

    }


}


