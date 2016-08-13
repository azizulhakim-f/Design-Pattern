package sdp.wifipass;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @author AZIZUL
 */
public class Office implements Runnable{

    String name;
    Register register;
    
    Map<String, Integer> Sheet = new HashMap<>();
    private static BlockingQueue queue = new ArrayBlockingQueue(1024);

    
    Office (String name){
        this.name = name;
    }
    void setRegister(Register register){
        this.register = register;
    }
    void sendStudent(Student stu){
        queue.offer(stu);
    }
    
    @Override
    public  void run() {

        
        while(true){
            try {
                
                int totalWait=0;
                while(queue.size()<9){
                    int wait = ThreadLocalRandom.current().nextInt(1, 10+1);
                    totalWait+=wait;
                    Thread.sleep(wait);
                    
                    if(totalWait>20)break;
                    
                }
                
                for(int i=0; i<10; i++){
                    Student stu = (Student) queue.take();
                    System.out.println(this.name + " accepted " + stu.name);

                    if(Sheet.containsKey(stu.id)){
                        System.out.println(stu.name + " tried to cheat");
                        continue;
                    }

                    Sheet.put(stu.id,1);
                    register.sendStudent(stu);
                }
                
                //int wait = ThreadLocalRandom.current().nextInt(1, 500+1);
                //Thread.sleep(wait);
            } catch (InterruptedException ex) {}
            
        }
    
    }
}
