package sdp.wifipass;

import java.util.ArrayList;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @author AZIZUL
 */

public class Proffessor implements Runnable{
    public String rank;
    public String name;
    static Office office;
    private static BlockingQueue queue = new ArrayBlockingQueue(1024);
    private ArrayList<Proffessor> subordinates = new ArrayList<>();
    private int childcnt;
    
    Proffessor(String rank, String name){
        this.rank = rank;
        this.name = name;
        this.childcnt = 0;
    }
    
    void addChild(Proffessor prof){
        subordinates.add(prof);
        childcnt++;
    }
    
    void sendStudent(Student stu){
        queue.offer(stu);
    }
    
    void setOffice(Office office){
        this.office = office;
    }
        
    @Override
    public void run() {
        
        while(true){
            try {
                
                Student stu = (Student) queue.take();
                System.out.println(this.name + " accepted " + stu.name);
                stu.signRequest(name);
                
                office.sendStudent(stu);
                      
                //int wait = ThreadLocalRandom.current().nextInt(1, 500+1);
                //Thread.sleep(wait);
            } catch (InterruptedException ex) {}  
        }
    }
    
    void print(int space){
        for(int j=0; j<space; j++)System.out.print(" ");
        System.out.println(this.rank + " " + this.name);
        for(int i=0; i<childcnt; i++)
        {
            subordinates.get(i).print(space+4);
        }
    }
}

class AssistantProffessor extends Proffessor {

    public AssistantProffessor(String rank, String name) {
        super(rank, name);
    }
}

class AssociateProffessor extends Proffessor {

    public AssociateProffessor(String rank, String name) {
        super(rank, name);
    }
}



