import greenfoot.*;
/**
 * Player hit box - moves with player; if enemy bullet hits this, the player is considered hit.
 * 
 * @author Xuanxi Jiang
 * @version 1.0
 */
public class STGPlayer_htBox extends Actor{
    public STGPlayer_htBox(){//Initializer
        setImage(ht(false));
    }

    public void act(){
        if(Greenfoot.isKeyDown("shift"))//If shift is pressed, set image to show. Else hide the image.
            setImage(ht(true));
        else
            setImage(ht(false));
    }
    
    private GreenfootImage ht(boolean show){//Drawing the hit box (a square)
        GreenfootImage image = new GreenfootImage(STGStage_1.PlayerHT, STGStage_1.PlayerHT);
        if(show){
            image.setColor(new Color(39, 174, 96));
            image.fillRect(0, 0, STGStage_1.PlayerHT, STGStage_1.PlayerHT);
        }
        return image;
    }
}
