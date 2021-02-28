package dev.lazze.basics.antibot;

import java.util.concurrent.*;

public class BotAttack
{
    private boolean pinging;
    private boolean nevv;
    private String nicktype;
    private int length;
    private int bots;
    private int legit;
    private long name;
    
    public BotAttack(final long name, final boolean pinging, final boolean nevv, final String nicktype, final int length) {
        this.length = 0;
        this.bots = 0;
        this.legit = 0;
        this.pinging = pinging;
        this.nevv = nevv;
        this.nicktype = nicktype;
        this.name = name;
        this.length = length;
        final ExecutorService exe = Executors.newCachedThreadPool();
        exe.submit(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        Thread.sleep(1000L);
                    }
                    catch (InterruptedException ex) {}
                    if (BotAttack.this.bots == 0) {
                        final BotAttack this$0 = BotAttack.this;
                        BotAttack.access1(this$0, this$0.legit + 1);
                        if (BotAttack.this.legit > BotBoth.timeout) {
                            break;
                        }
                        continue;
                    }
                    else {
                        BotAttack.access2(BotAttack.this, 0);
                        BotAttack.access1(BotAttack.this, 0);
                    }
                }
                BotBoth.cancelAttack(name);
            }
        });
    }
    
    private String getNickType(final String name) {
        if (name.length() == this.length) {
            return "length";
        }
        if (BotBoth.nicks.contains(name)) {
            return "nicks";
        }
        return "null";
    }
    
    public long getName() {
        return this.name;
    }
    
    public boolean handleLogin(final boolean b, final String name, final String IP) {
        if (this.pinging) {
            if (this.nevv == BotBoth.isNew(name) && this.nicktype.equals(this.getNickType(name))) {
                if (b) {
                    BotBoth.log(name, IP);
                    ++this.bots;
                }
                return true;
            }
        }
        else if (this.pinging == BotBoth.pingedServer(IP) && this.nevv == BotBoth.isNew(name) && this.nicktype.equals(this.getNickType(name))) {
            if (b) {
                BotBoth.log(name, IP);
                ++this.bots;
            }
            return true;
        }
        return false;
    }
    
    public static void access1(final BotAttack attack, final int legit) {
        attack.legit = legit;
    }
    
    public static void access2(final BotAttack attack, final int bots) {
        attack.bots = bots;
    }
}
