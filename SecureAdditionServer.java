package SSLsocket;


// An example class that uses the secure server socket class
// SSL/TLS: Secure Sockets Layer, predecessor to Transport Layer Security, 
// a protocol for encypting information over the internet

import java.io.*;
import java.net.*;
import javax.net.ssl.*;
import java.security.*;
import java.util.StringTokenizer;


public class SecureAdditionServer {
	private int port;
	// This is not a reserved port number
	static final int DEFAULT_PORT = 8189;
	
	//Keystore: provide credentials (private and public keys)
	static final String KEYSTORE = "jpatkeystore.ks";
	
	//Truststore: verify credentials (public keys)
	static final String TRUSTSTORE = "jpattruststore.ks";
	static final String STOREPASSWD = "changeit";
	static final String ALIASPASSWD = "changeit";
	
	public final static String FILE_TO_SEND = "hej.txt";  // file to be sent
	
	/** Constructor
	 * @param port The port where the server
	 *    will listen for requests
	 */
	SecureAdditionServer( int port ) {
		this.port = port;
	}
	
	/** The method that does the work for the class */
	public void run() {
		FileInputStream fis = null;
	    BufferedInputStream bis = null;
	    OutputStream os = null;
	    //ServerSocket servsock = null;		- sss
	    //Socket sock = null;				- incoming
	    
		try {
			KeyStore ks = KeyStore.getInstance( "JCEKS" );
			ks.load( new FileInputStream( KEYSTORE ), STOREPASSWD.toCharArray() );
			
			KeyStore ts = KeyStore.getInstance( "JCEKS" );
			ts.load( new FileInputStream( TRUSTSTORE ), STOREPASSWD.toCharArray() );
			
			//Key manager interface (X.509 specific key manager)
			KeyManagerFactory kmf = KeyManagerFactory.getInstance( "SunX509" );
			kmf.init( ks, ALIASPASSWD.toCharArray() );
			
			//Trust manager interface (X.509 specific trust manager)
			TrustManagerFactory tmf = TrustManagerFactory.getInstance( "SunX509" );
			tmf.init( ts );
			
			SSLContext sslContext = SSLContext.getInstance( "TLS" );
			sslContext.init( kmf.getKeyManagers(), tmf.getTrustManagers(), null );
			SSLServerSocketFactory sslServerFactory = sslContext.getServerSocketFactory();
			
			//Secure server socket sss
			SSLServerSocket sss = (SSLServerSocket) sslServerFactory.createServerSocket( port );
			sss.setEnabledCipherSuites( sss.getSupportedCipherSuites() );
			
			System.out.println("\n>>>> SecureAdditionServer: active ");
			SSLSocket incoming = (SSLSocket)sss.accept();
			
			
		  // send file (http://www.rgagnon.com/javadetails/java-0542.html)
			
          //Create a new file to send
		  File myFile = new File (FILE_TO_SEND);
		  
          //File size
          byte [] mybytearray  = new byte [(int)myFile.length()];
          
          //Input bytes from a file, read stream of raw bytes 
          fis = new FileInputStream(myFile);
          
          //Buffer: memory used to temporarily store data while it's being moved from one place to another.
          //Add functionality to stream fis
          bis = new BufferedInputStream(fis);
          
          //Reads bytes from bis into mybytearray starting at 0
          bis.read(mybytearray,0,mybytearray.length);	//Want to read from bis the array from first position to the last.
          
          
          //Set output stream to the incoming stream
          //incoming = incoming ssl socket
          os = incoming.getOutputStream();
          
          System.out.println("Sending " + FILE_TO_SEND + "(" + mybytearray.length + " bytes)");
          
          //Write to outputstream os from mybytearray, starting from position 0
          os.write(mybytearray,0,mybytearray.length);
          //Flushes this output stream and forces any buffered output bytes to be written out. (print everything in os)
          os.flush();
          System.out.println("Done.");
          
          //Close all streams and release any system resources assouciated with the streams
          bis.close();
          os.close();
          //Close all sockets
          incoming.close();
          sss.close();
			
			//Get file from client
			
			/*File file = new File("bye.txt");
			long length = file.length();
			if(length > Integer.MAX_VALUE){
				System.out.println("File too large!");
			}
			byte[] bytes = new byte[(int) length];
			FileInputStream fis = new FileInputStream(file);
			BufferedInputStream bais = new BufferedInputStream(fis);
			BufferedOutputStream bout = new BufferedOutputStream(incoming.getOutputStream());
			
			int count;
			
			while((count = bais.read(bytes)) > 0){
				bout.write(bytes, 0, count);
			}

			bout.flush();
			bout.close();
			fis.close();
			bais.close();
			incoming.close();*/
			

      /*ORIGINAL
       * BufferedReader in = new BufferedReader( new InputStreamReader( incoming.getInputStream() ) );
			PrintWriter out = new PrintWriter( incoming.getOutputStream(), true );			
			
			String str;
			while ( !(str = in.readLine()).equals("") ) {
				double result = 0;
				StringTokenizer st = new StringTokenizer( str );
				try {
					while( st.hasMoreTokens() ) {
						Double d = new Double( st.nextToken() );
						result += d.doubleValue();
					}
					out.println( "The result is " + result );
				}
				catch( NumberFormatException nfe ) {
					out.println( "Sorry, your list contains an invalid number" );
				}
			}
			incoming.close();*/
			
			
			
			
		}
		catch( Exception x ) {
			System.out.println( x );
			x.printStackTrace();
		}
	}
	
	
	/** The test method for the class
	 * @param args[0] Optional port number in place of
	 *        the default
	 */
	public static void main( String[] args ) {
		int port = DEFAULT_PORT;
		if (args.length > 0 ) {
			port = Integer.parseInt( args[0] );
		}
		SecureAdditionServer addServe = new SecureAdditionServer( port );
		addServe.run();
	}
}

