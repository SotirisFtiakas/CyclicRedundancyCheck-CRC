import java.util.Random;                                            /* Name : Sotiris Ftiakas */
import java.lang.Math;                                              /* AEM  :      3076       */
import java.util.Scanner;

public class Main{

    public static void main(String args[])
    {
        int TRIES = 0,error=0,noerror=0;

        while(TRIES<10000){                   //Declaration of how many times we want to send a message
            Random random = new Random();
            final int K = 10;                 /*Declaration of K bits of M message*/
            Scanner o = new Scanner(System.in);
            int[] M;                          //Array for M message bits
            int message = 0;
            int[] P_Arr;                      //Array for P divisor bits
            int[] Zeros;                      //Array used to append extra 0s in M message
            int[] FCS;                        //Array used to create the F binary number
            int[] CRC;                        //Array used to create the T binary number
            int totalBits;

            /*----------------------------------------------------------------------------------*/
            /*                                      TASK 1                                      */
            /*         Generate randomly chosen binary messages of K bits (e.g. K = 10)         */
            /*----------------------------------------------------------------------------------*/

            M = new int[K];                     //Declaration of "M-Array"
            System.out.println("K bits = " + K);

            for (int i = 0; i < K; i++) {       //Initialisation of "M-Array"
                if (i == 0) {
                    M[i] = 1;
                } else {
                    M[i] = random.nextInt(2);
                }
            }
            int temp = 0;
            for (int i = K - 1; i >= 0; i--) {
                message = message + M[i] * (int) (Math.pow(10, temp));         //Calculation of M message
                temp++;
            }
            System.out.println("M message = " + message);

            /*----------------------------------------------------------------------------------*/
            /*                                      TASK 2                                      */
            /*  CRC calculation for the previous M message. User must give the P binary number  */
            /*----------------------------------------------------------------------------------*/

            int P;//=110101;                    //Used for the exercise given. (Comment next 6 lines)
            System.out.println("Enter P binary number ( last bit must be 1 ): ");
            P = o.nextInt();
            while (P % 10 != 1) {
                System.out.println("Enter P binary number again ( last bit must be 1 ): ");
                P = o.nextInt();
            }

            boolean condition = false;
            temp = 1;
            int bits = 0;
            while (!condition) {                //Calculation of how many bits the given P has
                if (P <= temp) {
                    condition = true;
                }
                bits++;
                temp = 1 + temp * 10;
            }
            P_Arr = new int[bits];              //Declaration of "P-Array"

            temp = P;
            for (int i = bits - 1; i >= 0; i--) {
                P_Arr[i] = temp % 10;           //Initialisation of "P-Array"
                temp = temp / 10;
            }

            System.out.println("Binary number P = " + P);
            System.out.println("Number of P bits = " + bits);

            totalBits = K + bits - 1;

            Zeros = new int[totalBits];         //Declaration of "extra 0s-Array"
            FCS = new int[totalBits];           //Declaration of "F-Array"
            CRC = new int[totalBits];           //Declaration of "T-Array"

            System.arraycopy(M, 0, Zeros, 0, M.length);       //Initialisation of "extra 0s-Array"


            System.arraycopy(Zeros, 0, FCS, 0, Zeros.length);


            FCS = divide(P_Arr, FCS);           //Creation of "F-Array" (CRC) binary number (remainder of division)

            /*----------------------------------------------------------------------------------*/
            /*                                      TASK 3                                      */
            /*            Sending CRC message with a Bit-Error-Rate E (e.g. E=10^(-3)           */
            /*----------------------------------------------------------------------------------*/

            for (int i = 0; i < Zeros.length; i++) {
                CRC[i] = (Zeros[i] ^ FCS[i]);     //Creation of "T-Array" binary number

                if (random.nextInt(1000) == 0) {        //Bit Error Rate E=10^(-3)
                    if (CRC[i] == 0) {
                        CRC[i] = 1;
                    } else {
                        CRC[i] = 0;
                    }
                }
            }



            /*----------------------------------------------------------------------------------*/
            /*                                      TASK 4                                      */
            /*                         Checking for errors at receiver                          */
            /*----------------------------------------------------------------------------------*/

            FCS = divide(P_Arr, CRC);           //Using FCS to store the 0s, if there is no error

            for (int i = 0; i < FCS.length; i++) {
                if (FCS[i] != 0)                 //Checking for errors
                {
                    System.out.println("Error\n");
                    error++;
                    break;
                }
                if (i == FCS.length - 1) {
                    System.out.println("No Error\n");
                    noerror++;
                }
            }
            TRIES++;
        }
        System.out.println("\nErrors: " + error + " No Errors: " + noerror);
    }

    //Function for binary divisions

    private static int[] divide(int P[], int Message[])
    {
        int curr=0;
        boolean condition=false;
        while(!condition)
        {
            for(int i=0;i<P.length;i++)
                Message[curr+i]=(Message[curr+i]^P[i]);

            while(Message[curr]==0 && curr!=Message.length-1)
                curr++;

            if((Message.length-curr)<P.length)
                condition=true;
        }
        return Message;
    }
}