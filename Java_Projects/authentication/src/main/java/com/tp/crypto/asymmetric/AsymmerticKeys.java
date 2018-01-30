package com.tp.crypto.asymmetric;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;

/**
 * 
 * @author SGSRIVASTAVM
 * This class generate public and private keys for asymmetric encryption and descryption
 */
public class AsymmerticKeys {
	private KeyPairGenerator keyPairGen;
	private KeyPair keyPair;
	private PrivateKey privateKey;
	private PublicKey publicKey;
	
	/**
	 * 
	 * @param keylength
	 * @throws NoSuchAlgorithmException
	 * @throws NoSuchProviderException
	 */
	public AsymmerticKeys(int keylength) throws NoSuchAlgorithmException, NoSuchProviderException {
		this.keyPairGen = KeyPairGenerator.getInstance("RSA");
		this.keyPairGen.initialize(keylength);
	}

	/**
	 * For creating public and private keys
	 */
	public void createAsymmerticKeys() {
		this.keyPair = this.keyPairGen.generateKeyPair();
		this.privateKey = keyPair.getPrivate();
		this.publicKey = keyPair.getPublic();
	}

	/**
	 * 
	 * @return private key
	 */
	public PrivateKey getPrivateKey() {
		return this.privateKey;
	}

	/**
	 * 
	 * @return public key
	 */
	public PublicKey getPublicKey() {
		return this.publicKey;
	}

	/**
	 * 
	 * @param path
	 * @param key
	 * @throws IOException
	 * This method write key files in a specified path
	 */
	public void writeToFile(String path, byte[] key) throws IOException {
		File f = new File(path);
		f.getParentFile().mkdirs();
		FileOutputStream fos = new FileOutputStream(f);
		fos.write(key);
		fos.flush();
		fos.close();
	}

	/**
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		AsymmerticKeys gk;
		try {
			gk = new AsymmerticKeys(Integer.parseInt(args[0]));
			gk.createAsymmerticKeys();
			gk.writeToFile(args[1]+"/publicKey", gk.getPublicKey().getEncoded());
			gk.writeToFile(args[1]+"/privateKey", gk.getPrivateKey().getEncoded());
			System.out.println("Keys generated....at path:"+args[1]);
		} catch (Exception e) {
			e.printStackTrace();
		} 

	}
	
	
}
