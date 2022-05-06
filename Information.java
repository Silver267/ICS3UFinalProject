import greenfoot.*;
/**
 * Essentially a panel which displays nececarry information such as player shield remaining.
 * 
 * @author Xuanxi Jiang
 * @version 1.0
 */
public class Information extends Actor{
    private int cnt;

    public Information(){//Initializer for information object.
        cnt = 0;
        switch (Engine.WorldState){//Currently only implemented state for battle stage -- other cases are currently all unnececarry.
            case 2:
                if(STGStage_1.en.getPhase()!=3)
                    setImage(drawInformation_STG(STGStage_1.pl.getShield(), 1500, STGStage_1.en.getHP()));
                else
                    setImage(drawInformation_STG(STGStage_1.pl.getShield(), 3200, STGStage_1.en.getHP()));
        }
    }
    
    public void act(){
        switch (Engine.WorldState){//actively update the information panel
            case 2:
                if(STGStage_1.en.getPhase()!=3)
                    setImage(drawInformation_STG(STGStage_1.pl.getShield(), 1500, STGStage_1.en.getHP()));
                else
                    setImage(drawInformation_STG(STGStage_1.pl.getShield(), 3200, STGStage_1.en.getHP()));
        }
    }
    
    private GreenfootImage drawInformation_STG(int shield, int TotalEnemyHP, int EnemyHP){//Drawing the information panel.
        GreenfootImage image = new GreenfootImage(400, Engine.worldHeight);
        image.setColor(new Color(194, 149, 145));
        image.fillRect(0, 0, 400, Engine.worldHeight);
        image.setColor(new Color(102, 255, 204));
        image.setFont(new Font("Helvetica", 24));
        image.drawString("Shield:", 20, Engine.worldHeight/6);
        for(int i=0; i<shield; i++)//Shield images are drawn as pictures to visualize the game to the player.
            image.drawImage(shieldImage(), 20 + i*24, Engine.worldHeight/6+4);
        image.drawString("Enemy:", 20, Engine.worldHeight/6+48);
        if(STGStage_1.en.isReborn() && shield>0){//If enemy is in reborn state, draw the "hp regain" animation.
            image.drawImage(drawHPBar(90, cnt), 20, Engine.worldHeight/6+52);
            cnt++;
            for(int i=0; i<(4-STGStage_1.en.getPhase()-1); i++)
                image.drawImage(phaseBar(255), 20+i*80, Engine.worldHeight/6+146);
            if(90-cnt>0)
                image.drawImage(phaseBar((int)((90-cnt)*2.8333)), 20+(4-STGStage_1.en.getPhase()-1)*80, Engine.worldHeight/6+146);
        }else{//If not, update the hp bar accordingly with the current hp of enemy.
            cnt = 0;
            image.drawImage(drawHPBar(TotalEnemyHP, EnemyHP), 20, Engine.worldHeight/6+52);
            for(int i=0; i<(4-STGStage_1.en.getPhase()); i++)
                image.drawImage(phaseBar(255), 20+i*80, Engine.worldHeight/6+146);
        }
        image.drawString("Time Remaining:", 20, Engine.worldHeight/6+96);
        image.drawString((int)Math.ceil(STGStage_1.en.getTimer()/60.0)+"", 20, Engine.worldHeight/6+120);
        image.drawString("Phase:", 20, Engine.worldHeight/6+144);
        image.drawString("CorruptedWorld - the Final Battle", 20, Engine.worldHeight*3/4);
        return image;
    }
    
    private GreenfootImage drawHPBar(int tHP, int HPRemaining){//Draw Hp bar
        GreenfootImage i = new GreenfootImage(300, 24);
        i.setColor(new Color(228, 63, 46));
        i.fillRect(0, 0, 300, 24);
        i.setColor(new Color(18, 217, 234));
        double hpPerc = 1 - ((double)HPRemaining/tHP);
        i.fillRect(0, 0, (int)(hpPerc*300), 24);
        return i;
    }
    
    private GreenfootImage shieldImage(){//Draw shield
        GreenfootImage tmp = new GreenfootImage(24, 24);
        int[] xPos = {12, 24, 12, 0, 12};
        int[] yPos = {0, 6, 24, 6, 0};
        tmp.setColor(new Color(192, 192, 192));
        tmp.fillPolygon(xPos, yPos, 5);
        return tmp;
    }
    
    private GreenfootImage phaseBar(int transParency){//Draw phase bar
        GreenfootImage i = new GreenfootImage(75, 24);
        i.setColor(new Color(18, 217, 234));
        i.fillRect(0, 0, 75, 24);
        i.setTransparency(transParency);//Transparency is for animating the phase change.
        return i;
    }
}
