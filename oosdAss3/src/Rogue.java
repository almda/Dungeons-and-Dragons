import View.cmdPrinter;

import java.util.ArrayList;

public class Rogue extends Player {
    int cost;
    int currentEnergy;

    public Rogue(Position pos, int healthPool, int healthAmount, int attackPoints, int defencePoints, int cost,String name) {
        super(pos, healthPool, healthAmount, attackPoints, defencePoints);
        this.cost = cost;
        this.currentEnergy = 100;
        this.name=name;
    }

    public void levelUp(){
        super.levelUp();
        currentEnergy = 100;
        setAttackPoints(getAttackPoints()+getLevel()*3);
    }

    @Override
    protected void specialAbility(ArrayList<Enemy> enemies) {
        if (currentEnergy < cost)
            cmdPrinter.sendMessage("Cannot use rogue special ability yet");
        else {
            currentEnergy -= cost;
            for (Enemy enemy : enemies){
                if(getPos().distance(enemy.getPos())<2){
                    //enemy.setHealthAmount(enemy.getHealthAmount()-getAttackPoints());
                    cmdPrinter.sendMessage("Special ability activated, hitting enemy with force of " + getAttackPoints() + " points");
                    enemy.getHit(getAttackPoints());
                }
            }
        }
    }

    @Override
    protected void onTick() {
        currentEnergy = Math.max(currentEnergy+10, 100);
    }

    public void combat(Tile attacker)
    {

    }

}
