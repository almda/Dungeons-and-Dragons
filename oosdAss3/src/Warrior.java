import View.cmdPrinter;

import java.util.ArrayList;

public class Warrior extends Player {
    int abilityCoolDown; //The number of game ticks required to pass before the warrior can cast the ability again.
    int remainingCoolDown;  //Represents the number of ticks remained until the warrior can cast its special ability

    public Warrior(Position pos, int healthPool, int healthAmount, int attackPoints, int defencePoints, int abilityCoolDown, String name) {
        super(pos, healthPool, healthAmount, attackPoints, defencePoints);
        this.abilityCoolDown = abilityCoolDown;
        this.remainingCoolDown = 0;
        this.name=name;
    }

    public void levelUp(){
        super.levelUp();
        remainingCoolDown = 0;
        setHealthPool(getHealthPool()+getLevel()*5);
        setAttackPoints(getAttackPoints()+getLevel()*2);
        setDefencePoints(getDefencePoints()+getLevel());
    }

    @Override
    protected void specialAbility(ArrayList<Enemy> enemies) {
        if (remainingCoolDown > 0)
            Main.getUI().sendMessage("Cannot use warrior special ability yet");
        else {
            remainingCoolDown = abilityCoolDown;
            for (int i = 0; i < enemies.size(); i++) {
                if (getPos().distance(enemies.get(i).getPos()) < 3) {
                    Double damage = getHealthPool() * 0.1;
                    cmdPrinter.sendMessage("Special ability activated, hitting enemy with force of " + damage + " points");
                    enemies.get(i).getHit(damage.intValue());
                    setHealthAmount(getHealthAmount() + getDefencePoints() * 10);
                    break;
                }
            }
        }
    }

    @Override
    protected void onTick() {
        remainingCoolDown--;
        if(remainingCoolDown<0) remainingCoolDown=0;
    }
    
}
