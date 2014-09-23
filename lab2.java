/*
*   TNM031 - Lab 2
*   Veronica Börjesson (verbo911)and Emma Edvardsson (emmed608)
*/


//Write an implementation of the RSA algorithm in java.
//Your program must have the following functionalities. It must be able to generate public and private keys.
//encrypt a plain text message given one key. decrypt the ciphertext message given the other key.

import java.lang.Math;	//For rand
import java.lang.Object;	//For rand
import java.util.Arrays;
import java.math.BigInteger;
import java.util.Random;
import java.math.*;



public class lab1{
    static long myInteger = 1;
    static BigInteger bi = BigInteger.valueOf(myInteger);

    /*
    * Method to get a random big prime number
    */
	private static BigInteger getPrime() {
		int bitLength = 1024;
		Random rnd = new Random();

		BigInteger n = BigInteger.probablePrime(bitLength, rnd);

		return n;
	}
	
	public static void main(String[] args) {
		/* Generate public and private keys. */
		// p, q = random large primes.
		BigInteger p, q, e;
		
        //Convert int 1 to bigInteger
        long myInteger = 1;
        BigInteger bi = BigInteger.valueOf(myInteger);

        p = getPrime();
		q = getPrime();
		e = getPrime(); // e = encryption exponent
		//System.out.println("p: " + p);
		//System.out.println("q: " + q);
		//System.out.println("e: " + e);
		
		BigInteger n = p.multiply(q);   // n = pq

		String m = "TND031 - Network programming and securitylllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkklllllllllllllllllll"; // m = messages
		BigInteger mLength = new BigInteger(m.getBytes());

        /* Encrypt a plain text message given one key. */
        BigInteger c = mLength.modPow(e,n);

        /* Decrypt the ciphertext message given the other key. */
        BigInteger val = (p.subtract(bi)).multiply(q.subtract(bi));
        BigInteger d = e.modInverse(val);
        //System.out.println("d: " + d);

        BigInteger m_decr = c.modPow(d, n);
        //System.out.println("m_decr: " + m_decr);
        
        String mesg = new String(m_decr.toByteArray());

        System.out.println("Message: " + m);
        System.out.println("Encrypted message: " + c);
        System.out.println("Decrypted message: " + mesg);
	
	}
}
