package SSLsocket;

// A client-side class that uses a secure TCP/IP socket

import java.io.*;
import java.net.*;
import java.security.KeyStore;
import javax.net.ssl.*;

public class SecureAdditionClient {
	private InetAddress host;
	private int port;
	// This is not a reserved port number 
	static final int DEFAULT_PORT = 8189;
	static final String KEYSTORE = "jpatkeystore.ks";
	static final String TRUSTSTORE = "jpattruststore.ks";
	static final String STOREPASSWD = "changeit";
	static final String ALIASPASSWD = "changeit";
	

	 public final static String FILE_TO_RECEIVE = "downloaded-file.txt";	//File to be received/downloaded
  
	 public final static int FILE_SIZE = 6022386; // file size temporary hard coded
     											// should bigger than the file to be downloaded
	
	// Constructor @param host Internet address of the host where the server is located
	// @param port Port number on the host where the server is listening
	public SecureAdditionClient( InetAddress host, int port ) {
		this.host = host;
		this.port = port;
	}
	
  // The method used to start a client object
	public void run() {
		try {
			KeyStore ks = KeyStore.getInstance( "JCEKS" );
			ks.load( new FileInputStream( KEYSTORE ), STOREPASSWD.toCharArray() );
			
			KeyStore ts = KeyStore.getInstance( "JCEKS" );
			ts.load( new FileInputStream( TRUSTSTORE ), STOREPASSWD.toCharArray() );
			
			KeyManagerFactory kmf = KeyManagerFactory.getInstance( "SunX509" );
			kmf.init( ks, ALIASPASSWD.toCharArray() );
			
			TrustManagerFactory tmf = TrustManagerFactory.getInstance( "SunX509" );
			tmf.init( ts );
			
			SSLContext sslContext = SSLContext.getInstance( "TLS" );
			sslContext.init( kmf.getKeyManagers(), tmf.getTrustManagers(), null );
			SSLSocketFactory sslFact = sslContext.getSocketFactory();    
			
			//Secure Socket for client, with server as host
			SSLSocket client =  (SSLSocket)sslFact.createSocket(host, port);
			
			//Encryption (lab2)
			client.setEnabledCipherSuites( client.getSupportedCipherSuites() );
			System.out.println("\n>>>> SSL/TLS handshake completed");
			
			//Set nr of bytes to read
			int bytesRead;
			
			//Nr of bytes that is currently read
			int current = 0;
			
			//Output stream for writing data to a file
			FileOutputStream fos = null;
			
			//Store data temporarily while sending file
			BufferedOutputStream bos = null;

			//Array with bytes (where we store everything)
			byte [] mybytearray = new byte [FILE_SIZE];
			
			//Store input stream for client socket in is
			//getinputstream: returns an input stream for this socket (client)
			InputStream is = client.getInputStream();
			
			//Writing file FILE_TO_RECEIVE to the outputstream fos
			fos = new FileOutputStream(FILE_TO_RECEIVE);
			
			// Buffer fos - store in bos temporarily
			bos = new BufferedOutputStream(fos);
			
			//Read number of bytes from is to mybytearray, starting at 0
			bytesRead = is.read(mybytearray, 0, mybytearray.length);
			
			//Set current to bytesRead
			current = bytesRead;
			
			do{
				//Read from inputstream is to mybytesarray.
				//Start at current and read to last position minus current (OBS: kolla sedan)
				bytesRead = is.read(mybytearray, current, (mybytearray.length-current));
				
				//bytesRead is bigger than 0 --> add bytesRead to current
				//If it contains something (bytesRead is not zero), then add to current
				if(bytesRead >= 0) 
					current += bytesRead;
			} while(bytesRead > -1);	//Do while there is still data to read (OBS: testa sedan när det fungerar)
			
			//Write to bos from mybytearray starting from position 0
			bos.write(mybytearray, 0, current);
			//Flushes this output stream and forces any buffered output bytes to be written out. (print everything in os)
			bos.flush();
			System.out.println("File " + FILE_TO_RECEIVE + " downloaded (" + current + " bytes read)");
			
			
			//Close all the streams and sockets
			fos.close();
			bos.close();
			//Close client socket
			client.close();
		
	
			
			//BufferedReader socketIn;
			//socketIn = new BufferedReader( new InputStreamReader( client.getInputStream() ) );
			
			//PrintWriter socketOut = new PrintWriter( client.getOutputStream(), true );
			
			/* Send file from client to server */
			
			
			// Old
			/*String file = "hej.txt";
			FileOutputStream fouts = null;
			BufferedOutputStream bouts = null;
			
			OutputStream outs = client.getOutputStream();
			PrintStream socketOut = new PrintStream(outs);
			
			InputStream ins = client.getInputStream();
			BufferedReader socketIn = new BufferedReader(new InputStreamReader(ins));
			
			int bufferSize = client.getReceiveBufferSize();
			
			//socketOut.println();
			fouts = new FileOutputStream(file);
			bouts = new BufferedOutputStream(fouts);
			
			byte[] bytes = new byte[bufferSize];
			
			int count;
			while((count = ins.read()) != -1){
				bouts.write(bytes, 0, count);
			}
			socketIn.close();
			fouts.close();
			bouts.close();*/
			
			//System.out.println( ">>>> Sending the file " + file + " to SecureAdditionServer" );
			
			//socketOut.println( file );
			//System.out.println( socketIn.readLine() );

			//socketOut.println ( "" );
		}
		catch( Exception x ) {
			System.out.println( x );
			x.printStackTrace();
		}
	}
	
	
	// The test method for the class @param args Optional port number and host name
	public static void main( String[] args ) {
		try {
			InetAddress host = InetAddress.getLocalHost();
			int port = DEFAULT_PORT;
			if ( args.length > 0 ) {
				port = Integer.parseInt( args[0] );
			}
			if ( args.length > 1 ) {
				host = InetAddress.getByName( args[1] );
			}
			SecureAdditionClient addClient = new SecureAdditionClient( host, port );
			addClient.run();
		}
		catch ( UnknownHostException uhx ) {
			System.out.println( uhx );
			uhx.printStackTrace();
		}
	}
	
	public static void upload(){
		
	}
	public static void download(){
		
	}
	public static void delete(){
		
	}
}
