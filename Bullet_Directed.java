import greenfoot.*;
/**
 * Directed bullet which aims to target. Currently only avaliable to player
 * 
 * @author Xuanxi Jiang
 * @version 1.0
 */
public class Bullet_Directed extends Actor{
    private int power, size, initFacing;
    private double speed;
    private double subPixPosX, subPixPosY;
    
    public Bullet_Directed(double speed, int power, int size, int initFacing, int x, int y){
        this.speed = speed; this.size = size; this.power = power; this.initFacing = initFacing;
        this.setImage(temp());
        this.setLocation(x, y);
        this.setRotation(initFacing);
        subPixPosX = x; subPixPosY = y;
    }
    
    public void act(){
        STGEnemy_Normal e = (STGEnemy_Normal)getOneIntersectingObject(STGEnemy_Normal.class);
        if(e!=null && !e.isReborn()){//If hit enemy in its normal state, decrease its hp by the power which this bullet has.
            getWorld().removeObject(this);
            e.chHP(-power);
            return;
        }
        if(!STGStage_1.clearBullets){//If not in clearBullets (which means an enmey exsists), set to face the enemy.
            int xpos = (this.getX()), ypos = (this.getY());
            int dx = STGStage_1.en.getX()-xpos, dy = STGStage_1.en.getY()-ypos;
            int facing = (int)(Math.toDegrees(Math.atan2(dy, dx)));
            this.setRotation(facing);
        }
        //Also sub-pixel movement, but updates real-time.
        double subPixIncX = Math.cos((this.getRotation())*(Math.PI/180))*speed;
        double subPixIncY = Math.sin((this.getRotation())*(Math.PI/180))*speed;
        subPixPosX += subPixIncX; subPixPosY += subPixIncY;
        this.setLocation((int)subPixPosX, (int)subPixPosY);
        if(isAtEdge() || this.getX()>1200)//If touching edge or touching information panel, remove itself.
            getWorld().removeObject(this);
    }
    
    private GreenfootImage temp(){//Image for player's directed bullet
        GreenfootImage image = new GreenfootImage("Star.png");
        return image;
    }
}
