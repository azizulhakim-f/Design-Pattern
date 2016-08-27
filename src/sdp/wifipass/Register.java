/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sdp.wifipass;

import static java.lang.Math.random;
import java.util.ArrayList;
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
        PasswordFactory PF = new PasswordFactory();
        
        passCount=0;
        int i;
        for(i=0 ; i<1024; i++)
        {
            String pass = PF.getPass("CHARACTER", 5) + PF.getPass("NUMERIC", 3);
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
        if(Sheet.containsKey(stu.id)==false){
            System.out.println("Password not ready yet for: " + stu.name );
            return false;
        }  
        
        int ret = Sheet.get(stu.id);     
        if( ret==1 )return true;
        return false;
    }

    void givePass(Student stu) {
        stu.setPassword( this.getPassword() );
        stu.setEndTime(System.nanoTime());
        Sheet.put(stu.id, 2);
    }
    
}

class PasswordFactory{
     String getPass(String type, int length){
         NumPassword  Npass = new NumPassword();
         NumPassword  Np1 = new NumPassword();
         NumPassword  Np2 = new NumPassword();
         Npass.addChild(Np1);
         Npass.addChild(Np2);
         
         CharPassword Cpass = new CharPassword();
         CharPassword Cp1 = new CharPassword();
         CharPassword Cp2 = new CharPassword();
         Cpass.addChild(Cp1);
         Cpass.addChild(Cp2);
         
         
         if(type.equals("NUMERIC")){
             return Npass.getPass(length);
         }
         else { //default 
             return Cpass.getPass(length);
         }
     }
}

interface Password{
    String getPass(int len);
     
}

class NumPassword implements Password{

    private ArrayList<NumPassword> childs = new ArrayList<>();
    int childcnt=0;
    
    void addChild(NumPassword NP){
        childs.add(NP);
        childcnt++;
    }
    
    @Override
    public String getPass(int len) {
        String ret = "";
        
        for(int i=0; i<len-childcnt; i++){
            int rand = ThreadLocalRandom.current().nextInt(0, 9 + 1);
            ret += (char) ( rand + '0' );
        }
        
        for(int i=0; i<childcnt&&i<len; i++){
            ret += childs.get(i).getPass(1);
        }
        
        return ret;
    }
    
}

class CharPassword implements Password{

    private ArrayList<CharPassword> childs = new ArrayList<>();
    int childcnt=0;
    
    void addChild(CharPassword CP){
        childs.add(CP);
        childcnt++;
    }
    
    @Override
    public String getPass(int len) {
        String ret = "";
        
        for(int i=0; i<len-childcnt; i++){
            int rand = ThreadLocalRandom.current().nextInt(0, 25 + 1);
            ret += (char) ( rand + 'A' );
        }
        
        for(int i=0; i<childcnt&&i<len; i++){
            ret += childs.get(i).getPass(1);
        }
        
        return ret;
    }
    
}