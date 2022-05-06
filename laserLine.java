import greenfoot.*;
/**
 * Laser attack warning line
 * 
 * @author Xuanxi Jiang
 * @version 1.0
 */
public class laserLine extends Actor{
    private int length, width, facing, duration, lasduration;
    private boolean sound;
    
    public laserLine(int length, int width, int facing, int duration, int lasduration, boolean sound){//Initializer for laser warning line
        this.length = length;
        this.width = width;
        this.facing = facing;
        this.duration = duration;
        this.lasduration = lasduration;
        this.sound = sound;
        setRotation(facing);
        setImage(draw());
    }
    
    public void act(){
        if(STGStage_1.clearBullets == true){//If in clear bullets mode, cancle the laser generation.
            getWorld().removeObject(this);
            return;
        }
        duration--;
        if(duration <= 0){//If not in clear bullets mode and the count down <= 0, generate a laser and remove this warning line.
            int xpos = (this.getX());
            int ypos = (this.getY());
            getWorld().addObject(new laser(length, width, facing, lasduration, sound), xpos, ypos);
            getWorld().removeObject(this);
            return;
        }
        if(duration%10<5)//Flashing
            this.getImage().setTransparency(160);
        else
            this.getImage().setTransparency(255);
    }
    
    private GreenfootImage draw(){//Warning line image
        GreenfootImage image = new GreenfootImage(length, 2);
        image.setColor(new Color(175, 122, 197));
        image.fillRect(0, 0, length, 2);
        return image;                
    }
}
