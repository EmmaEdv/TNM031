/*
*   TNM031 - Lab 2
*   Veronica Börjesson (verbo911)and Emma Edvardsson (emmed608)
*/


//Write an implementation of the RSA algorithm in java.
//Your program must have the following functionalities. It must be able to generate public and private keys.
//encrypt a plain text message given one key. decrypt the ciphertext message given the other key.

import java.lang.Math;	//For rand
import java.lang.Object;	//For rand
import java.math.BigInteger;
import java.util.Random;
import java.math.*;



public class lab1
    long myInteger = 1;
    BigInteger bi = BigInteger.valueOf(myInteger);

	/*
	* Method to get the GCD
	* GCD = Greatest Common Deviser
	*/
	private static BigInteger getGCD(BigInteger p, BigInteger q) {

		BigInteger val = (p.subtract(bi)).multiply(q.subtract(bi));

		BigInteger e = null;
		if (e.gcd(val) == bi){
			// If we find gcd --> send n & e to alice
			return e;
		}

		return null;
	}

    /*
    * Method to get a random big prime number
    */
	private static BigInteger getPrime() {
		int bitLength = 100;
		Random rnd = new Random();

		BigInteger n = BigInteger.probablePrime(bitLength, rnd);

		return n;
	}
	public static void main(String[] args) {

		/* Generate public and private keys. */

		// n = modulus
		// p, q = Bob's random large primes.
		// e = Bob's encryption exponent

		BigInteger p, q, e;
        //Convert int 1 to bigInteger
        long myInteger = 1;
        BigInteger bi = BigInteger.valueOf(myInteger);


		// We assume that p and q are primes.
		do{
			p = getPrime();
			q = getPrime();
			e = getGCD(p,q);

		}while((getGCD(p,q)) == null);


		BigInteger n = p.multiply(q);   // n = pq

		// Bob sends n & e to Alice --> n & e public


		String m = ""; // m = Alice messages

		//OBS: Verbo byter ut:
		//int msgLength = m.length(); // some value
		//BigInteger mLength = new BigInteger(String.valueOf(msgLength));
		//OBS: mot(from conversion.txt)
		//Convert a string into a BigInteger
		BigInteger mLength = New BigInteger(m.getBytes());

		int comparison = mLength.compareTo(n);

		Boolean multMsgs = false;

		//returns -1 if smaller
		//returns 1 if larger
		if(comparison == 1){
			//Divide into blocks smaller or equal to n

            multMsgs = true;
			//Divide m by n
			BigInteger div = mLength.divide(n);
			BigInteger[] msgs;

			msgs = new BigInteger[div.add(bi)];	//Store messages in array
		}

		//OBS: om vi har en array av meddelanden -> gå igenom alla och enkrypta?

		if(!multMsgs)   //Om vi endast har ett meddelande
        {


            // Alice sends m ( vart undrar verbo????)
            // m < n (remember!! )

            /* Encrypt a plain text message given one key. */


            /* Alice enctypts m */
            // Alice computes c = m^e * (mod(n))
            BigInteger c = (m.pow(e)).mod(n);
            // And sends c to Bob

            /* Decrypt the ciphertext message given the other key. */

            // Bob knows p and q --> can compute (p-1)(q-1) and can therefore
            // find the decryption exponent d with
            // d = decryption expnent

            // d*e = 1 * mod( (p-1)(q-1) )

            // (p-1)(q-1)
            BigInteger val = (p.subtract(bi)).multiply(q.subtract(bi));
            BigInteger d = (bi.mod(val)).divide(e)

            //  m = c^d * mod(n)

            //To obtain the original message, Bob computes
            //c^d = (m^e)^d = m^(1+k(theta)(n) = .... /mod (n)
            BigInteger m_de = (c.pow(d)).mod(n);



            System.out.println("Message: " + m);
            System.out.println("Encrypted message: " + c);
            System.out.println("Decrypted message: " + m_de);

		}
	}


}
