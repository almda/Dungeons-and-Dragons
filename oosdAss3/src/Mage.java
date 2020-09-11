import View.cmdPrinter;

import java.util.ArrayList;
import java.util.Random;

public class Mage extends Player {

    private int manaPool;
    private int currentMana;
    private int manaCost;
    private int spellPower;
    private int hitsCount;
    private int abilityRange;

    public Mage(Position pos, int healthPool, int healthAmount, int attackPoints, int defencePoints, int manaPool, int manaCost, int spellPower, int hitsCount, int abilityRange,String name) {
        super(pos, healthPool, healthAmount, attackPoints, defencePoints);
        this.manaPool = manaPool;
        this.currentMana = manaPool/4;
        this.manaCost = manaCost;
        this.spellPower = spellPower;
        this.hitsCount = hitsCount;
        this.abilityRange = abilityRange;
        this.name=name;
    }

    public void levelUp(){
        super.levelUp();
        manaPool += getLevel()*25;
        currentMana = Math.min(currentMana+manaPool/4, manaPool);
        spellPower += getLevel()*10;
    }

    @Override
    protected void specialAbility(ArrayList<Enemy> enemies) {
        if (currentMana < manaCost)
            cmdPrinter.sendMessage("Cannot use mage special ability yet");
        else {
            currentMana -= manaCost;
            int hits = 0;
            for(Enemy e : enemies){
                if(hits<hitsCount && isClose(e) && getRandom()==1)
                cmdPrinter.sendMessage("Special ability activated, hitting enemy with force of " + spellPower + " points");
                e.getHit(spellPower);
                hits++;
            }
        }
    }

    public boolean isClose(Enemy e){
        return getPos().distance((e.getPos())) < abilityRange;
    }

    @Override
    protected void onTick() {
        currentMana = Math.min(manaPool, currentMana + getLevel());
    }

    public void combat(Tile attacker) { }

    private int getRandom (){
        Random rand = new Random();
        return rand.nextInt( 2);
    }
}
