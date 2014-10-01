package SSLsocket;

import java.io.*;
import java.net.*;

import javax.net.ssl.*;

import java.util.Scanner;
import java.security.*;
import java.util.StringTokenizer;

public class Lab3{
	public static void main( String[] args ) {
		System.out.println("Welcome to the secure sockets!");
		System.out.println("Would you like to upload a file? Press 1");
		System.out.println("Would you like to download a file? Press 2");
		System.out.println("Would you like to delete a file? Press 3");
	
		Console console = System.console();
		int answer = Integer.parseInt(console.readLine("Answer: "));
		
		switch(answer){
			case 1: SecureAdditionClient.upload();
					break;
			case 2: SecureAdditionClient.download();
					break;
			case 3: SecureAdditionClient.delete();
					break;
			default: System.out.println("Goodbye!"); 
					 break;
		}
	}
}
