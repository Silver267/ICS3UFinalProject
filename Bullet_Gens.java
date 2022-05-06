import greenfoot.*;
/**
 * Bullet generator which generates bullets / lasers.
 * 
 * @author Xuanxi Jiang
 * @version 1.0
 */
public class Bullet_Gens extends Actor{
    private final int l = 60, w = 20;//Length and width for the bullet generators.
    private int cnt, facing, life, delay, interval;
    private boolean friendly, removing;
    private int type;
    // 0 -> Bullet_Undirected
    // 1 -> laserLine
    
    public Bullet_Gens (int facing, boolean friendly, int type, int life, int delay, int interval){
        setImage(draw());
        this.facing = facing;
        this.friendly = friendly;
        setRotation(facing);
        this.life = life;
        this.type = type;
        this.delay = delay;
        this.interval = interval;
        removing = false;
        switch(type){
            case 0:
                cnt = 30;
                break;
            case 1:
                cnt = 1;
        }
    }
    
    public void act(){
        if(STGStage_1.clearBullets == true){//If in clear bullets mode, remove itself.
            getWorld().removeObject(this);
            return;
        }
        if(!friendly){//Case for unfriendly variant (which is the only variant for now
            if(removing){//If in removing animation, decrease transparency value.
                this.getImage().setTransparency(this.getImage().getTransparency()-9);
                if(this.getImage().getTransparency()<=9)//If transparency <= 9, remove it.
                    getWorld().removeObject(this);
            }else{//Normal mode
                if(delay>0){//If still in delay, decrease the delay value.
                    delay--;
                }else{//Normal generating mode
                    life--;//Life (time) remaining to shoot the bullet.
                    if(type==0){//Case for bullet generation mode
                        if(cnt<=0){
                            cnt = interval;
                            int xpos = l/2;
                            int ypos = w/4;
                            getWorld().addObject(new Bullet_Undirected(0, 4, facing, 8, 17, this.getX(), this.getY(), false), xpos, ypos);
                        }else{
                            cnt--;
                        }
                    }else if(type==1){//case for laser generation mode
                        if(cnt<=0){
                            cnt = interval;
                            int xpos = this.getX();
                            int ypos = this.getY();
                            getWorld().addObject(new laserLine(Engine.worldHeight-l/4, 10, facing, 30, 60, false), xpos, Engine.worldHeight/2-l/2);
                        }else{
                            cnt--;
                        }
                    }
                    if(life<=0)//If life <= 0, set removing animation to true.
                        removing = true;
                }
            }
        }
    }
    
    private GreenfootImage draw(){//The picture for bullet generators.
        GreenfootImage image = new GreenfootImage(l, w);
        image.setColor(Color.BLACK);
        image.fillOval(0, 0, l, w);
        return image;                
    }
}
