/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sdp.wifipass;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.ThreadLocalRandom;

/**
 *
 * @author AZIZUL
 */
public class SDPWifipass {
    
    public static void main(String[] args) throws FileNotFoundException, IOException, InterruptedException {
        
        
        
        String studentName[] = new String[1024];
        FileReader fr = new FileReader("input.txt");
        BufferedReader br = new BufferedReader(fr);
        int i=1, n;
        String line;
        
        while((line = br.readLine()) != null)
        {
            studentName[i++] = line;
        }
        n = i-1;
        
        
        /************************START THREADS*********************************/
        Proffessor A1 = new Proffessor("Proffessor", "A1");
        Proffessor A2 = new Proffessor("Proffessor", "A2");
        AssistantProffessor C1 = new AssistantProffessor("AssistantProffessor", "C1");
        AssistantProffessor C2 = new AssistantProffessor("AssistantProffessor", "C2");
        AssociateProffessor E1 = new AssociateProffessor("AssociateProffessor", "E1");
        AssociateProffessor E2 = new AssociateProffessor("AssociateProffessor", "E2");
        
        ///childss
        A1.addChild(C1);
        A2.addChild(C2);
        C1.addChild(E1);
        C1.addChild(E2);
        C2.addChild(E1);
        C2.addChild(E2);
        
        
        A1.print(0);
        A2.print(0);
        
        
        Office office = new Office("B");
        Register register = new Register("D");
        
        office.setRegister(register);
        A1.setOffice(office); 
        
        new Thread(A1).start();
        new Thread(A2).start();
        new Thread(C1).start();
        new Thread(C2).start();
        new Thread(E1).start();
        new Thread(E2).start();
        new Thread(office).start();
        new Thread(register).start();
        /**********************************************************************/
        
        int rand, fraud, wait, createnow, j, totalWait=0;
        
        Queue<Student> Q = new LinkedList<>();
        
        
        for( i=0; i<=n; )
        {
            // create minimum one and up to three student at a time...
            createnow = ThreadLocalRandom.current().nextInt(1, 3 + 1);
            for( j=1; j<=createnow && i+j<=n; j++)
            {
                Student stu = new Student(studentName[j+i], j+i);
                stu.setStartTime(System.nanoTime());
                System.out.println(stu.name + " applied");
                
                Q.add(stu);

                // randomly provide a Proffessor type
                rand = ThreadLocalRandom.current().nextInt(1, 3 + 1);
                if(rand==1)A1.sendStudent(stu);
                if(rand==2)C1.sendStudent(stu);
                if(rand==3)E1.sendStudent(stu);
                                
                fraud = ThreadLocalRandom.current().nextInt(1, 5 + 1);
                if(fraud==1){ //20 percent time...apply again.
                    j--; 
                }
            }
            i+=(j-1);
            
            
            
            wait = ThreadLocalRandom.current().nextInt(1, 10+1);
            totalWait += wait;
            Thread.sleep(wait);
            
            if(totalWait>20){
                totalWait=0;
                int size = Q.size();
                for(int k=0; k<size; k++){
                    Student stud = Q.poll();
                    if(register.passReady(stud)){
                        register.givePass(stud);
                        System.out.println(stud.name + " got pass: " +stud.getPassword() + " time-took: "+stud.totalTime());
                        
                    }
                    else {
                        Q.add(stud);
                    }
                }
            }
            
        }
        
        /*
        A1.endNow();
        A2.endNow();
        C1.endNow();
        C2.endNow();
        E1.endNow();
        E2.endNow();
        office.endNow();
        register.endNow();
                */
        
    }
    
}