import greenfoot.*;
/**
 * Enemy in STG mode battle - normal state
 * 
 * @author Xuanxi Jiang
 * @version 1.0
 */
public class STGEnemy_Normal extends Actor{
    private int phase, cnt; //phases of the boss, each represent a new attack pattern.
    private int hp, timer; //hp of each phase and timer for each phase
    private int state; //state of the boss.
    // 0 -> normal state; 1 -> rebirth first state; 2 -> rebirth second state;
    private double prevAng, x; //previous angle of attack and previous x in generation function.
    //for bullet generation
    private boolean endAnimation, goodEnd;
    private int posX, posY, cnt2, del, offset; //variables for phase 2 attack.
    private boolean rev; //also variable for phase 2 attack.
    private int gotoPos; //varioable for phase 3 attack.
    private boolean moving, goingLeft, isAtY; //also variables for phase 3 attack.
    private final GreenfootSound l = new GreenfootSound("laser.wav");
    private final GreenfootSound timeout = new GreenfootSound("timeout.wav");
    private final GreenfootSound laserse = new GreenfootSound("alert.wav");
    private final int[] phaseHPs = {1500, 3200}; //HP array to store hp for each phase
    
    public STGEnemy_Normal(){//Initializer
        this.setImage(new GreenfootImage("Enemy.png"));
        phase = 0; cnt = 1;
        hp = phaseHPs[0]; timer = 2400;
        state = 0;
        prevAng = 0; x = 0;
        endAnimation = false;
    }
    
    public void chHP(int val){//Change hp (currently only decrease hp is implememted as the game currently does not require to add HP.
        GreenfootSound st = new GreenfootSound("enemy_damaged.wav");
        st.setVolume(STGStage_1.seVolume);
        st.play();
        hp += val;
        if(hp<=0)
            die();
    }
    
    //Getter methods.
    public int getHP(){
        return hp;
    }
    
    public int getTimer(){
        return timer;
    }
    
    public int getPhase(){
        return phase;
    }
    
    public boolean isReborn(){
        return state!=0;
    }
    
    public void End(boolean good){//End animation -- Enemy changes to display the pre-ending informations.
        if(!endAnimation){
            endAnimation = true;
            goodEnd = good;
            state = 1;
            this.setImage(displayPoints(good));
            this.setLocation(Engine.worldWidth/2, Engine.worldHeight/2);
            STGStage_1.clearBullets = true;
        }
        if(Greenfoot.isKeyDown("enter")){
            Engine.WorldState = 3;
            if(good)
                Greenfoot.setWorld(new GoodEnd());
            else
                Greenfoot.setWorld(new BadEnd());
        }
    }
    
    private void die(){//die
        GreenfootSound d = new GreenfootSound("enemy_dead.wav");
        d.setVolume(STGStage_1.seVolume);
        d.play();
        state = 1;
    }
    
    private void reBorn(){
        if(state==1){//fade out
            if(laserse.isPlaying())//if laser alert is on, turn it off
                laserse.stop();
            STGStage_1.clearBullets = true;//clear the bullets
            if(this.getImage().getTransparency()>2){//fade out
                this.getImage().setTransparency(getImage().getTransparency()-2);
            }else{//image is almost transparent, revieve, state -> fade in
                GreenfootSound d = new GreenfootSound("revive.wav");
                d.setVolume(STGStage_1.seVolume);
                d.play();
                state = 2;
            }
        }else if(state==2){//fade in
            this.setLocation(Engine.midW, Engine.worldHeight/4);//set location to middle
            if(this.getImage().getTransparency()<255-3){//fade in
                this.getImage().setTransparency(getImage().getTransparency()+3);
            }else{
                //add phase
                phase ++;
                //set initial HP value by phase
                if(phase==3){
                    hp = phaseHPs[1];
                    timer = 4800;
                }else{
                    hp = phaseHPs[0];
                    timer = 2400;
                }
                //initialize variables, not all necessarily used in each phase, but just for generalize code
                x = 0; prevAng = 0; cnt = 0; posX = 0; posY = 50; rev = true; cnt2 = -1; gotoPos = this.getX();
                moving = false; isAtY = false;
                del = 18;
                //force transparency to 255
                this.getImage().setTransparency(255);
                STGStage_1.clearBullets = false; //set clearBullets state to false
                state = 0;//Normal state
            }
        }
    }
    
    private double phase1Method1(double dx, double ang){//method to generate the bullets of phase 1
        double fx = ((-1/10.0)*dx + 28/3.0); //generation (angle) function
        ang = ang+fx;
        int xpos = this.getX(), ypos = this.getY();
        for(int i=0; i<4; i++){//function to generate bullet for first spell card.
            double theta = 180-140/3 + i*90;//angle offset
            //set speed to 3.46 + x/584.0 so that the speed of bullets are increasing.
            getWorld().addObject(new Bullet_Undirected(0, (3.46+x/584.0), (int)(ang+theta), 8, 17, xpos, ypos, false), xpos, ypos);
        }
        return ang;
    }
    
    private void phase2Method1(int pos, int delay, int off){//method to generate the side bulletGens for phase2
        //use delay to control the timing of the bullet generators, so they could shoot bullets synchronously
        getWorld().addObject(new Bullet_Gens(0, false, 0, 90, delay*5, 7), 16, pos+off);
        getWorld().addObject(new Bullet_Gens(180, false, 0, 90, delay*5, 7), Engine.midW*2-16, pos+off);
    }

    private double phase2Method2(){//method to generate the bullets comming from the enemy.
        int xpos = (this.getX()), ypos = (this.getY());
        int dx = STGStage_1.ht.getX()-xpos, dy = STGStage_1.ht.getY()-ypos; //preprocess dx and dy
        int ang = (int)(Math.toDegrees(Math.atan2(dy, dx))); //calculate the angle of bullets to make them aim to player
        for(int i=0; i<6; i++){
            int theta = ang+i*60; //angle offset
            getWorld().addObject(new Bullet_Undirected(0, 2, theta, 8, 17, xpos, ypos, false), xpos, ypos);
        }
        return 0;
    }
    
    private void phase2Method3(int delta){//method to generate laser bulletGens for phase2
        //mirror the laser bulletGens
        getWorld().addObject(new Bullet_Gens(270, false, 1, 100, 0, 120), delta, Engine.worldHeight-16);
        getWorld().addObject(new Bullet_Gens(270, false, 1, 100, 0, 120), Engine.midW*2-delta, Engine.worldHeight-16);
    }
    
    private void phase3Method1(int targetX){ //Method to generate the large laser and move the Enemy for phase3
        if(!moving && this.getX()!=targetX){//If not moving and targetX does not equal to the targetX, set moving to true.
            cnt2 = -1;
            l.stop();
            moving = true;
            if(this.getX()-targetX<0)//Check if going left
                goingLeft = false;
            else
                goingLeft = true;
        }else if(!moving){//If everything is in place, add a laser warning line.
            cnt = 480;
            cnt2 = 260;
            laserse.setVolume(STGStage_1.seVolume);
            laserse.play();
            gotoPos = (int)(Math.random()*Engine.midW)+Engine.midW/2;
            int xpos = (this.getX());
            int ypos = (this.getY());
            getWorld().addObject(new laserLine(900-ypos/2, Engine.midW*2-256, 90, 120, 240, true), xpos, Engine.midW-ypos);
        }else{
            if(goingLeft){
                this.setLocation(this.getX()-5, this.getY());
                if(this.getX()<=targetX){
                    this.setLocation(targetX, this.getY());
                    moving = false;
                }
            }else{
                this.setLocation(this.getX()+5, this.getY());
                if(this.getX()>=targetX){
                    this.setLocation(targetX, this.getY());
                    moving = false;
                }
            }
        }
    }
    
    private double phase3Method2(double dx, double ang){//Method to generate the bullets for phase3
        double fx = (10*Math.sin(0.5*x));
        ang = ang+fx;
        int xpos = this.getX(), ypos = this.getY();
        for(int i=0; i<20; i++){
            double theta = i*18;
            getWorld().addObject(new Bullet_Undirected(0, 6, (int)(ang+theta), 8, 17, xpos, ypos, false), xpos, ypos);
        }
        return ang;
    }
    
    private double phase4Method1(double prev){//Method to generate the bullets for phase4
        x++;
        if(this.getY()>Engine.worldHeight/8){
            setLocation(this.getX(), this.getY()-2);
            return 0;
        }else{
            if(prev<-7){//swing the "lines" of bullets, reverse swing if necessary.
                prev = -7;
                rev = true;
            }else if(prev>7){
                prev = 7;
                rev = false;
            }
            prev += (rev ? 1 : -1);
            int xpos = Engine.midW, ypos = this.getY();
            for(int i=0; i<3; i++){//generating the 4 "lines" of bullets on the side
                int theta = (i-1)*80;
                getWorld().addObject(new Bullet_Undirected(0, 11, (int)(90+prev)+theta, 8, 17, xpos-xpos/3, ypos-50, false), xpos-xpos/3, ypos-50);
                getWorld().addObject(new Bullet_Undirected(0, 11, (int)(90-prev)+theta, 8, 17, xpos+xpos/3, ypos-50, false), xpos+xpos/3, ypos-50);
                getWorld().addObject(new Bullet_Undirected(0, 11, (int)(90+prev)+theta, 8, 17, xpos-3*(xpos/4), ypos+150, false), xpos-3*(xpos/4), ypos+150);
                getWorld().addObject(new Bullet_Undirected(0, 11, (int)(90-prev)+theta, 8, 17, xpos+3*(xpos/4), ypos+150, false), xpos+3*(xpos/4), ypos+150);
            }
            if(prev%7==0 && prev!=0){//The other bullets
                int tmp = (int)(Math.random()*18);
                if(timer<4500){//The two "rings" of bullets
                    for(int i=0; i<20; i++){
                        getWorld().addObject(new Bullet_Undirected(2, (2.13+x/867.0), i*18+tmp, 8, 17, xpos-3*(xpos/4), ypos+150, false), xpos-3*(xpos/4), ypos+150);
                        getWorld().addObject(new Bullet_Undirected(2, (2.13+x/867.0), i*18-tmp, 8, 17, xpos+3*(xpos/4), ypos+150, false), xpos+3*(xpos/4), ypos+150);
                    }
                }
                if(prev>0){//The bullets which aims to player
                    int dx = STGStage_1.ht.getX()-this.getX(), dy = STGStage_1.ht.getY()-this.getY();
                    getWorld().addObject(new Bullet_Undirected(3, 1, (int)(Math.toDegrees(Math.atan2(dy, dx))), 8, 23, this.getX(), this.getY(), false), this.getY(), this.getY());
                }
            }
            return prev;
        }
    }
    
    private void phase1ATK(){ //Attack method for phase1 (each progression is calculated by frames)
        if(cnt<=0){
            cnt = 1;
            prevAng = phase1Method1(x, prevAng);
            x++;
        }else{
            cnt--;
        }
    }
    
    private void phase2ATK(){ //Attack method for phase1 (each progression is calculated by frames)
        if(cnt<=0){
            cnt = 4;
            if(posY>Engine.worldHeight-50){
                rev = true;
                cnt = 160;
                del = 18;
                offset = (int)(Math.random()*25);
                posY = Engine.worldHeight-50;
            }else if(posY<50){
                rev = false;
                cnt = 160;
                del = 18;
                offset = (int)(Math.random()*25);
                offset = -offset;
                posY = 50;
            }else{
                if(!rev)
                    posY += 50;
                else
                    posY -= 50;
                phase2Method1(posY, del, offset);
                del--;
            }
        }else{
            cnt --;
        }
        if(cnt2<=0){
            cnt2 = 45;
            phase2Method2();
            phase2Method3((int)(Math.random()*Engine.midW));
            x++;
        }else{
            cnt2--;
        }
    }
    
    private void phase3ATK(){//Attack method for phase3 (each progression is calculated by frames)
        if(cnt<=0){//If a phase of laser attack is completed, generate another phase.
            cnt = 2; 
            if(!isAtY){
                setLocation(getX(), getY()-5);
                if(getY()<=50){
                    setLocation(getX(), 50);
                    isAtY = true;
                }
            }else{
                phase3Method1(gotoPos);
            }
        }else{
            cnt--;
        }
        if(cnt2!=-1){//If currently in laser attack, generate the bullets which comes with the laser.
            cnt2--;
            if(cnt2%8==0 && cnt2<=140){
                prevAng = phase3Method2(x, prevAng);
                x++;
            }        
        }
    }
    
    private void phase4ATK(){//Attack method for phase4 (each progression is calculated by frames)
        if(cnt<=0){  
            cnt = 2;
            prevAng = phase4Method1(prevAng);
        }else{
            cnt--;
        }
    }
    
    public void act(){
        if(state==0){
            timer--;
            if(timer <= 0)//If time limit is exceeded, die and go to next phase.
                die();
            else if(timer <= 360 && timer%60==0)//if time remaining is smaller than 6s (60frames), play timeout once per second.
                timeout.play();
            switch(phase){
                case 0://first attack
                    phase1ATK();
                    break;
                case 1://second attack (or phase 2)
                    phase2ATK();
                    break;
                case 2://third attack (phase 3)
                    phase3ATK();
                    break;
                case 3://fourth attack (phase 4)
                    phase4ATK();
                    break;
            }
        }else{
            switch(phase){
                case 3:
                    if(!endAnimation)//If not in end animation, go to end animation and set goodEnd to true
                        End(true);
                    else//Else, go to end animation method.
                        End(goodEnd);
                    break;
                default:
                    if(endAnimation){//If in end animation (called by player), go to end animation method.
                        End(goodEnd);
                        return;
                    }
                    reBorn();//else reborn
                    break;
            }
        }
    }
    
    private GreenfootImage displayPoints(boolean Good){//Display the pre-ending (end animatino) information.
        GreenfootImage image = new GreenfootImage(Engine.worldWidth, Engine.worldHeight);
        image.setColor(Color.BLACK);
        image.fillRect(0, 0, Engine.worldWidth, Engine.worldHeight);
        image.setTransparency(100);
        image.setColor(Color.RED);
        image.setFont(new Font("Helvetica", 36));
        image.drawString("Press Enter to continue", Engine.midW-300, 450);
        return image;
    }
}
