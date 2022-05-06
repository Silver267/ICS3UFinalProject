import greenfoot.*;
/**
 * STG Mode Stage 1.
 * 
 * @author Xuanxi Jiang
 * @version 1.0
 */
public class STGStage_1 extends World{
    //Enemy radius, Player width, Player height, Player hitbox side length, volume for sound effects
    public static final int EnemyR = 128, PlayerW = 64, PlayerH = 128, PlayerHT = 8, seVolume = 90;
    
    //Initialize static variables
    public static STGPlayer_htBox ht;
    public static STGPlayer_Normal pl;
    public static STGEnemy_Normal en;
    public static GreenfootSound STG_StageOne_BGM;
    public static boolean clearBullets;
    
    //Private variables for dynamic background
    private int curY;//Current Y offset
    private GreenfootImage background;
    
    //Method to set the paint orders.
    private void setPaintOrders(){
        this.setPaintOrder(Information.class, STGEnemy_Normal.class, Bullet_Gens.class, Bullet_Undirected.class, Bullet_Directed.class, laser.class, STGPlayer_htBox.class, STGPlayer_Normal.class);
    }
    
    public STGStage_1(){    
        // Create a new world with 1600x900 cells with a cell size of 1x1 pixels.
        super(Engine.worldWidth, Engine.worldHeight, 1);
        clearBullets = false; 
        ht = new STGPlayer_htBox();
        pl = new STGPlayer_Normal();
        en = new STGEnemy_Normal();
        setPaintOrders(); curY = 0;
        background = new GreenfootImage("ice.jpg");
        //STG_StageOne_BGM = new GreenfootSound("Nuclear_Fusion.wav");
        //STG_StageOne_BGM.playLoop();
        addObject(pl, Engine.midW, Engine.worldHeight*3/4);
        addObject(ht, Engine.midW, Engine.worldHeight*3/4);
        addObject(en, Engine.midW, Engine.worldHeight/4);
        addObject(new Information(), Engine.worldWidth-200, Engine.worldHeight/2);
    }
    
    public void act(){
        dynamicBG();
        //case for enemy is in "reborn" state and current phase of enemy is not the last phase
        if(en.isReborn() && en.getPhase()!=3){
            curY--;
        }else{
            //case for different phases of the enemy.
            switch(en.getPhase()){
            case 2:
                curY-=2;
                break;
            case 3:
                curY++;
                break;
            default:
                curY--;
                break;
            }
        }
    }
    
    //Method of dynamic moving background
    private void dynamicBG(){
        //calculate the y offset.
        int dy = curY%background.getHeight()-background.getHeight();
        //if current y position divides the background height, reset the current Y to 0 to prevent overflow.
        if(curY%background.getHeight()==0)
            curY = 0;
        //clear background
        this.getBackground().clear();
        //draw texture
        for(int i=0; i<Engine.worldWidth; i+=background.getWidth())
            for(int j=dy; j<Engine.worldHeight; j+=background.getHeight())
                this.getBackground().drawImage(background, i, j);
    }
}
