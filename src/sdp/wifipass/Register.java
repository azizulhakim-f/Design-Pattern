/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sdp.wifipass;

import java.util.HashMap;
import java.util.Map;
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
    Map<String, Integer> Sheet = new HashMap<>();
    Map<String, Integer> uniquePass = new HashMap<>();
    
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
            String pass = UUID.randomUUID().toString().substring(25);
            
            if(uniquePass.containsKey(pass)){
                i--;
                continue;
            }
            
            password[i] = pass;
            uniquePass.put(pass, 1);
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
                
                Sheet.put(stu.id,1);  
               
            } catch (InterruptedException ex) {}
        }
        
    }

    boolean passReady(Student stu) {
        boolean ret = Sheet.containsKey(stu.id);
        if(!ret)System.out.println("Password not ready yet for: " + stu.name );
        return ret;
    }

    void givePass(Student stu) {
        stu.setPassword( this.getPassword() );
        stu.setEndTime(System.nanoTime());
    }
    
}
