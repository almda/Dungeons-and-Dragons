import View.cmdPrinter;

import java.util.ArrayList;
import java.util.Random;

public abstract class Player extends Unit {
    private int experience;
    private int level; //of character, not game level

    public Player(Position pos, int healthPool, int healthAmount, int attackPoints, int defencePoints) {
        super('@', pos, healthPool, healthAmount, attackPoints, defencePoints);
        experience = 0;
        level = 1;
        Clock.signToClock(this);
    }

    public void updateExperience(int points){
        experience+=points;
        if (experience > level*50)
            levelUp();
    }

    public void setExperience(int exp){
        this.experience = exp;
    }

    public void setLevel(int lvl){
        this.level = lvl;
    }

    public void levelUp(){
        cmdPrinter.sendMessage(getName()+ "leveled up!\n");
        experience = experience-level*50;
        level++;
        setHealthPool(getHealthPool()+level*10);
        setHealthAmount(getHealthPool());
        setAttackPoints(getAttackPoints()+level*4);
        setDefencePoints(getDefencePoints()+level);
    }

    public int getLevel(){return level;}
    public int getExperience(){return experience;}

    protected abstract void onTick();

    void getHit(int damage){
        setHealthAmount(getHealthAmount()-damage);
        if(getHealthAmount() < 0) {
            setHealthAmount(0);
        }
    }

    protected abstract void specialAbility(ArrayList <Enemy> enemies);

    public void combat(Tile attacker)
    {
        attacker.engage(this);
    }
    public void engage (Empty defender){
        Position p = new Position(this.getPos().getX(),this.getPos().getY());
        this.getPos().setLocation(defender.getPos().getX(), defender.getPos().getY());
        defender.getPos().setLocation(p.getX(),p.getY());
    }

    public void engage ( Wall defender){ }

    public void engage ( Player defender){ }

    public void engage ( Enemy defender){
        cmdPrinter.sendMessage(this.getName() + " engaged in combat with "+ defender.getName());
        int attackRoll = getRandom(this.getAttackPoints());
        cmdPrinter.sendMessage(this.getName() + " rolled up "+ attackRoll+ " attack points");
        int defenceRoll = getRandom(defender.getDefencePoints());
        cmdPrinter.sendMessage(defender.getName() + " rolled up "+ defenceRoll+ " defence points");
        if(attackRoll-defenceRoll>0) {
            defender.getHit(attackRoll);
            cmdPrinter.sendMessage(this.getName() + " dealt "+(attackRoll-defenceRoll) +" damage points to "+ defender.getName());
            if(defender.getHealthAmount()==0){ //Player killed enemy
                this.updateExperience(defender.getExperience());
                cmdPrinter.sendMessage(defender.getName() +" has been killed. "+ this.getName()+ " earned "+ defender.getExperience()+" experience points");
            }
        }
    }


    private static int getRandom (int upperValue){
        Random rand = new Random();
        return rand.nextInt(upperValue + 1);
    }


    @Override
    public void combat(Player  attacker) {

        attacker.engage(this);
    }

    @Override
    public void combat(Enemy  attacker) {

        attacker.engage(this);
    }

    @Override
    public void combat(Wall  attacker) {

    }

    @Override
    public void combat(Empty  attacker) {

    }

}
