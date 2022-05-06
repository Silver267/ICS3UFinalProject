import greenfoot.*;
/**
 * Laser attack
 * 
 * @author Xuanxi Jiang
 * @version 1.0
 */
public class laser extends Actor{
    private int length, width, facing, duration; 
    private int curL;
    boolean generating, removing;
    private final GreenfootSound laserSound = new GreenfootSound("laser.wav");
    
    public laser(int length, int width, int facing, int duration, boolean sound){
        this.length = length;
        this.width = width;
        this.facing = facing;
        this.duration = duration;
        setRotation(facing);
        curL = 1;
        generating = true;
        setImage(draw());
        if(sound){//If sound = true, play the sound effect for laser.
            laserSound.setVolume(STGStage_1.seVolume-20);
            laserSound.playLoop();
        }
    }
    
    public void act(){
        if(STGStage_1.clearBullets == true){//If in clearbullets mode, remove this object and stop the sound effect if playing.
            if(laserSound!=null && laserSound.isPlaying())
                laserSound.stop();
            getWorld().removeObject(this);
            return;
        }
        if(!removing){//Normal mode
            duration--;
            if(duration<=30){//If less than 1/2 second remaining, fade out.
                removing = true;
                return;
            }
            if(generating){//generating animation (if neccecarry)
                if(curL == 1)
                    curL = 0;
                curL += length/10; //Increase current length to length/10 
                if(curL>=length){//If current length >= length, set it = length and stop generating animation.
                    curL = length;
                    generating = false;
                }
                setImage(draw());
            }
            STGPlayer_htBox ht = (STGPlayer_htBox)getOneIntersectingObject(STGPlayer_htBox.class);
            STGPlayer_Normal p = (STGPlayer_Normal)getOneIntersectingObject(STGPlayer_Normal.class);
            if(ht!=null && p!=null && !p.isInv())//If intersect with player, remove one shield.
                p.chShield(-1);
        }else{//Removing animation (fade out)
            if(laserSound!=null)//If sound effect is playing, stop it.
                laserSound.stop();
            this.getImage().setTransparency(this.getImage().getTransparency()-9);
            if(this.getImage().getTransparency()<=9)//If the image transparency <= 9, remove it.
                getWorld().removeObject(this);
        }
    }
    
    private GreenfootImage draw(){//laser image
        GreenfootImage image = new GreenfootImage(length, width);
        image.setColor(new Color(142, 68, 173));
        image.fillRect(0, 0, curL, width);
        image.setColor(new Color(128, 0, 128));
        image.fillOval(0, width/3, curL*2, width/3);
        return image;                
    }
}
