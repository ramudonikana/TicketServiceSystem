package com.wm.ts;

import java.io.IOException;
import java.util.Scanner;


public class Balcony2LevelSeatArrangement {
	static Scanner input = new Scanner(System.in);
    static String arrS[][] = new String[10][10];
    static String cName[] = {"A","B","C","D","E","F","G", "H","I","J",
    		"k","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z"};

    static int i, j, k;            // Loop Control Variables

    static void dispData() {    // Method that will display the array content
        for (i=0; i<10; ++i) {
            for (j=0; j<10; ++j) {
                System.out.print(arrS[i][j] + "\t");
            }
            System.out.println();
        }
        System.out.println();
    }

    static boolean chkData(String vData) {  // Method that will check for reservation availability
        for (i=0; i<10; ++i) {
            for (j=0; j<10; ++j) {
                if ((arrS[i][j]).equalsIgnoreCase(vData)) {
                    arrS[i][j]="Booked";
                    return true;
                }
            }
        }
        return false;
    }

    static boolean chkFull() {  // Method that will check if all reservations were occupied
        for (i=0; i<10; ++i) {
            for (j=0; j<10; ++j) {
                if (!(arrS[i][j]).equals("Booked")) {
                    return false;
                }
            }
        }
        return true;
    }

    public static void seatReserve() throws IOException {  // the MAIN method program
       System.out.println("Seats Available in Balcony2 Level by Seat Number ");
		String inData = new String("");
        for (i=0; i<10; ++i) {                                   // Initialized array with constant data
            for (j=0; j<10; ++j) {
                arrS[i][j] = new String((i+1) + cName[j]);}
        }

        do {                                                    // Loop until user press X to exit
            dispData();
           
            if (chkFull())
            {
                System.out.println("Reservation is FULL");
                inData="Booked";
            }
            else 
            {     
            	System.out.println("Enter email id to make a payment to reserve Seat");
            	String customeremail=input.next();
                System.out.print("Enter Seat number to Reserve Seat: ");
                inData = input.next();
                if (chkData(inData))
                    System.out.println("Reservation Successful for coustomer:"+customeremail);
                
                else
                    System.out.println("Occupied Seat!");
            }       
        } while (!inData.equalsIgnoreCase("Booked"));

    }   

}

