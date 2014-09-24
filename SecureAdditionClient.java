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
			SSLSocket client =  (SSLSocket)sslFact.createSocket(host, port);
			client.setEnabledCipherSuites( client.getSupportedCipherSuites() );
			System.out.println("\n>>>> SSL/TLS handshake completed");

			
			//BufferedReader socketIn;
			//socketIn = new BufferedReader( new InputStreamReader( client.getInputStream() ) );
			
			//PrintWriter socketOut = new PrintWriter( client.getOutputStream(), true );
			
			/* Send file from client to server */
			
			String file = "hej.txt";
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
			bouts.close();
			
			System.out.println( ">>>> Sending the file " + file + " to SecureAdditionServer" );
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
}
