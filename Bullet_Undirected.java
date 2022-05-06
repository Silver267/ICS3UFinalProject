import greenfoot.*;
/**
 * Undirected bullet (goes in a straight line and will not automatically aim to target)
 * 
 * @author Xuanxi Jiang
 * @version 1.0
 */
public class Bullet_Undirected extends Actor{
    //Borrowed the concept of speed, friendly from Mr. Cohen
    private int pict, power, size, facing;
    //pict = 0 -> Enemy normal bullet
    //pict = 1 -> Player normal bullet
    private double speed;
    //facing is the side which the bullet is facing
    //for bullets which heads a straight line but initially facing the player.
    //power is the power of the bullet, depends how many hp (or shield) the target will take
    //if touching this bullet.
    //size is the size of bullet.
    //Note that this bullet does not have a "hit zone", so size should be < 15.
    private boolean friendly;
    //variables for sub-pixel movement
    private double subPixPosX, subPixPosY, subPixIncX, subPixIncY;
    
    public Bullet_Undirected(int pict, double speed, int facing, int power, int size, int x, int y, boolean friendly){
        this.pict = pict; this.speed = speed; this.facing = facing; this.power = power; this.size = size; this.friendly = friendly;
        setRotation(facing);
        setImage(temp());
        setLocation(x, y);
        //initialization of sub-pixel movement - Stores the increase value in a double.
        subPixPosX = x; subPixPosY = y;
        subPixIncX = Math.cos((facing)*(Math.PI/180))*speed;
        subPixIncY = Math.sin((facing)*(Math.PI/180))*speed;
    }

    public void act(){
        //custom sub-pixel movement in replacemenent of move() which features accurate movement at angle.
        subPixPosX+=subPixIncX; subPixPosY+=subPixIncY;
        setLocation((int)subPixPosX, (int)subPixPosY);
        if(friendly){//If friendly bullet, decrease enemy hp when hitted the enemy.
            if(this.isTouching(STGEnemy_Normal.class) && !STGStage_1.en.isReborn()){
                getWorld().removeObject(this);
                STGStage_1.en.chHP(-power);
                return;
            }
        }else{//else, decrease player hp when hitted the player.
            if(STGStage_1.clearBullets == true){
                getWorld().removeObject(this);
                return;
            }
            if(this.isTouching(STGPlayer_htBox.class) && !STGStage_1.pl.isInv()){
                getWorld().removeObject(this);
                STGStage_1.pl.chShield(-1);
                return;
            }
        }
        if(isAtEdge() || this.getX()>1200)//If touching edge or touching information panel, remove itself.
            getWorld().removeObject(this);
    }
    
    private GreenfootImage temp(){
        if(pict==0){
            //Enemy bullet
            GreenfootImage image = new GreenfootImage(size*3/2, size*2/3);
            image.setColor(new Color(231, 76, 60));
            image.fillOval(0, 0, size*3/2, size*2/3);
            image.setColor(Color.WHITE);
            image.drawOval(0, 0, size*3/2, size*2/3);
            return image;
        }
        if(pict==1){
            //Player's bullet
            GreenfootImage image = new GreenfootImage("Star.png");
            return image;
        }
        if(pict==2){
            //Enemy stage 4 bullet (1)
            GreenfootImage image = new GreenfootImage(size*3/2, size*2/3);
            image.setColor(new Color(40, 55, 71));
            image.fillOval(0, 0, size*3/2, size*2/3);
            image.setColor(Color.WHITE);
            image.drawOval(0, 0, size*3/2, size*2/3);
            return image;
        }
        if(pict==3){
            //Enemy stage 4 bullet (2)
            GreenfootImage image = new GreenfootImage(size*3/2, size*2/3);
            image.setColor(new Color(21, 67, 96));
            image.fillOval(0, 0, size*3/2, size*2/3);
            image.setColor(Color.WHITE);
            image.drawOval(0, 0, size*3/2, size*2/3);
            return image;
        }
        return null;
    }
}
