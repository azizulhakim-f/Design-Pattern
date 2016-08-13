/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sdp.wifipass;

import java.util.UUID;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadLocalRandom;

/**
 *
 * @author AZIZUL
 */
public class Register implements Runnable{

    String name;
    private int passCount;
    private int passLimit;
    String[] password = new String[1030];
    private static BlockingQueue queue = new ArrayBlockingQueue(1024);
    
    
    Register(String name){
        this.name = name;
        generatePassword();
    }
    
    void sendStudent(Student stu){
        queue.offer(stu);
    }
    
    void generatePassword()
    {
        passCount=0;
        int i;
        for(i=0 ; i<1024; i++)
        {
            password[i] = UUID.randomUUID().toString();
        }
        passLimit = i;
    }
    
    String getPassword()
    {
        if(passCount==passLimit){
            System.out.println("No more password available.");
        }
        return password[passCount++];
    }
    
    
    @Override
    public void run() {
        
        while(true){
            try {
                
                Student stu = (Student) queue.take();
                System.out.println(this.name + " accepted " + stu.name);
                
                String pass = getPassword();
                pass = pass.substring(0, 7);
                
                stu.setPassword(pass);
                stu.setEndTime(System.nanoTime());
                
                System.out.println(stu.name + " pass: " + pass + " time took: "+stu.totalTime());
                
                
                //int wait = ThreadLocalRandom.current().nextInt(1, 500+1);
                //Thread.sleep(wait);
            } catch (InterruptedException ex) {}
        }
        
    }
    
}
