import greenfoot.*;
/**
 * Player in STG mode battle - normal state
 * 
 * @author Xuanxi Jiang
 * @version 1.0
 */
public class STGPlayer_Normal extends Actor{
    private int speed, coolDown = 0; //coolDown is fire coolDown
    private int shield, state, inCntDown; 
    // 0 -> normal state; 1 -> rebirth first state; 2 -> rebirth second state; 3 -> invincible state
    //inCntDown == invincible count down, typically set to 2s (120 frames).
    public STGPlayer_Normal(){//Initializer for player
        this.setImage(new GreenfootImage("Player.png"));
        shield = 3;
        state = 0;
        inCntDown = 0;
        this.getImage().setTransparency(255);
    }
    
    //returns the shield count of player
    public int getShield(){
        return shield;
    }
    
    //returns if the player is in invincible state
    public boolean isInv(){
        return inCntDown!=0;
    }
    
    //Changes the shield value for player. Currently only have case for decrease value.
    public void chShield(int val){
        if(val<0){
            if(inCntDown<=0){//If not in invincible state, perform shield decrease.
                GreenfootSound st = new GreenfootSound("biu.wav"); //Sound effect for shield-1
                st.setVolume(STGStage_1.seVolume);
                st.play();
                shield += val;
                state = 1;//Invincible state
                inCntDown = 120; //Invincible countdown.
                if(shield<0){
                    STGStage_1.en.End(false);
                    state = 3;
                }
            }
        }
    }
    
    public void act(){
        if(state==0) //If in normal state
            move();
        else if(state==1) //If in rebirth state
            reBirthFirst();
        else if(state==2) //If in second rebirth state
            reBirthSecond();
        else if(state==3) //If all shields are lost, remove itself.
            getWorld().removeObject(this);
    }
    
    private void reBirthFirst(){ //Animation for rebirth (phase1)
        if(this.getImage().getWidth()>5 && this.getImage().getHeight()>5 && this.getImage().getTransparency()>5){
            this.getImage().scale(this.getImage().getWidth()-5, this.getImage().getHeight()-5);
            this.getImage().setTransparency(getImage().getTransparency()-5);
        }else{
            state = 2;
        }
    }
    
    private void reBirthSecond(){ //Rebirth (phase2)
        setLocation(Engine.midW, Engine.worldHeight*3/4);
        this.setImage(new GreenfootImage("Player.png"));
        this.getImage().setTransparency(160); 
        state = 0;
    }
    
    private void move(){ //detects player input for moving or fireing
        if(Greenfoot.isKeyDown("shift")) //slow moving speed / high moving speed
            speed = 4;
        else
            speed = 8;
        int dirX = 0, dirY = 0; //direction for X and Y.
        if (Greenfoot.isKeyDown("up"))
            dirY -= speed;
        if (Greenfoot.isKeyDown("down"))
            dirY += speed;
        if (Greenfoot.isKeyDown("left"))
            dirX -= speed;
        if (Greenfoot.isKeyDown("right"))
            dirX += speed;
        if(getX()+dirX > Engine.worldWidth-400){//case for player touching the "Information" object
            if (dirX != 0 && dirY != 0){//If both X and Y is pressed, divide both increment by 2.
                setLocation(Engine.worldWidth-400, getY()+dirY/2);
                STGStage_1.ht.setLocation(Engine.worldWidth-400, getY()+dirY/2);
                //Set the location of the hit box along with player.
            }else{
                setLocation(Engine.worldWidth-400, getY()+dirY);
                STGStage_1.ht.setLocation(Engine.worldWidth-400, getY()+dirY);
            }
        }else{//Not touching "Information" object 
            if (dirX != 0 && dirY != 0){ 
                setLocation(getX()+dirX/2, getY()+dirY/2);
                STGStage_1.ht.setLocation(getX()+dirX/2, getY()+dirY/2);
            }else{
                setLocation(getX()+dirX, getY()+dirY);
                STGStage_1.ht.setLocation(getX()+dirX, getY()+dirY);
            }
        }
        if (Greenfoot.isKeyDown("z")) //call fire method
            fire();
        if(inCntDown<=0){ //if invincible count down <= 0, the player is no longer invincible.
            this.getImage().setTransparency(255); 
            inCntDown = 0;
        }else{//else continue to count down invincible time.
            inCntDown--; 
        }
    }
    
    private void fire(){//method for fireing
        if(coolDown<=0){//If not in cooldown, shoot the bullet. Else don't shoot.
            coolDown = 5; //shoot interval is set to 5 frames / bullet
            GreenfootSound st = new GreenfootSound("player_Shoot.wav");
            st.setVolume(STGStage_1.seVolume-5);
            st.play();
            if(Greenfoot.isKeyDown("shift"))//If shift is pressed, undirected bullet with higher damage is used.
                getWorld().addObject(new Bullet_Undirected(1, 20, 270, 8, 12, getX(), getY()-50, true), getX(), getY()-50);
            else//Otherwise, directed bullet which automatically aims to target and has lower damage is used.
                getWorld().addObject(new Bullet_Directed(20, 3, 12, 270, getX(), getY()-50), getX(), getY()-50);
        }else{
            coolDown--;
        }
    }
}
