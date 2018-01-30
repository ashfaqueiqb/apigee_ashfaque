package com.tp.crypto.asymmetric.test;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

/**
 * 
 * @author SGSRIVASTAVM
 *
 */
public class Main {
	private Cipher cipher;

	public Main() throws NoSuchAlgorithmException, NoSuchPaddingException {
		this.cipher = Cipher.getInstance("RSA");
	}

	/**
	 * 
	 * @param filename
	 * @return
	 * @throws Exception
	 */
	public PrivateKey getPrivateKey(String filename) throws Exception {
		//int x = Main.class.getResourceAsStream(filename).available();
		//byte[] keyBytes = new byte[x];
		//Main.class.getResourceAsStream(filename).read(keyBytes);
		byte[] keyBytes = Files.readAllBytes(new File(filename).toPath());
		PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(keyBytes);
		KeyFactory kf = KeyFactory.getInstance("RSA");
		return kf.generatePrivate(spec);
	}

	/**
	 * 
	 * @param filename
	 * @return
	 * @throws Exception
	 */
	public PublicKey getPublicKey(String filename) throws Exception {
		byte[] keyBytes = Files.readAllBytes(new File(filename).toPath());
		String test = keyBytes.toString();
		byte[] keyBytes1 = test.getBytes();
		X509EncodedKeySpec spec = new X509EncodedKeySpec(keyBytes);
		KeyFactory kf = KeyFactory.getInstance("RSA");
		return kf.generatePublic(spec);
	}

	/**
	 * 
	 * @param msg
	 * @param key
	 * @return
	 * @throws NoSuchAlgorithmException
	 * @throws NoSuchPaddingException
	 * @throws UnsupportedEncodingException
	 * @throws IllegalBlockSizeException
	 * @throws BadPaddingException
	 * @throws InvalidKeyException
	 */
	public String encryptText(String msg, PublicKey key) throws NoSuchAlgorithmException, NoSuchPaddingException,
			UnsupportedEncodingException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException {
		this.cipher.init(Cipher.ENCRYPT_MODE, key);
		return Base64.getEncoder().encodeToString(cipher.doFinal(msg.getBytes("UTF-8")));
	}

	/**
	 * 
	 * @param msg
	 * @param key
	 * @return
	 * @throws InvalidKeyException
	 * @throws UnsupportedEncodingException
	 * @throws IllegalBlockSizeException
	 * @throws BadPaddingException
	 */
	public String decryptText(String msg, PrivateKey key)
			throws InvalidKeyException, UnsupportedEncodingException, IllegalBlockSizeException, BadPaddingException {
		this.cipher.init(Cipher.DECRYPT_MODE, key);
		return new String(cipher.doFinal(Base64.getDecoder().decode(msg)), "UTF-8");
	}

	/**
	 * 
	 * @param args
	 * @throws Exception
	 */
	public static void main1(String[] args) throws Exception {
		Main ac = new Main();
		PrivateKey privateKey = ac.getPrivateKey("c:/mansih/privateKey.pem");
		PublicKey publicKey = ac.getPublicKey("c:/mansih/publicKey.pem");

		
		
		
		
		//This will be used at mobile side
		//USERID:PWD
		String msg = "seepimasupp:Tetra2018";
		String encrypted_msg = ac.encryptText(msg, publicKey);
		
		//This will be used at Apigee
		String decrypted_msg = ac.decryptText(encrypted_msg, privateKey);
		System.out.println("Original Message: " + msg + "\nEncrypted Message: " + encrypted_msg
				+ "\nDecrypted Message: " + decrypted_msg);

	}
	
	public static void main(String[] args) throws Exception{
		Main ac = new Main();
		    //String privateKeyContent = new String(Files.readAllBytes(new File("c:/mansih/privateKey").toPath()));
	    String privateKeyContent="MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQCdFEKreCjAawphpeFtRvL/ASmKqq7mtEJ30/uEyHk8smN1yLzpmL5d4sA2cETE5brYtVwIiCCr+KS8RxXaHHzrGz9qK8OKhL6/NYE5MWoOiU3+KO1ctnRaat54BBMD0GGmw9Yk+2WdNefWHeZ4EdX9j2LdsRuB4zUSB5w8Vnek7aFoS59h+eYjsvt1iWOLvCy4Nz57Yw8H3qzlupXwhx/umMj55uw+u2sjW09gNAkqsxWcvYAuM1uLDZZTZYgRZwCp4uCKjCoOjt1n03NsD8VpOMrk1Gppx7ZBTOcqsK+EHtrGUePZ87pA1amBQNyDGsIWU7W/lUm6jIX2ZFSUehlTAgMBAAECggEAVrYif6mbRW1Rk8oQgVSTO7YWipfiaTTFJIT+6bQJSTposVKZBpLtM8LCvMqJC/CNjqe/IG6VKpndxpmvNHJZeyfVf/Scw1C4Q7FznnDqzQ964H0TWwTVgjxQhwwouwhDf93Hzy6AT6K8UnS7Dzk5WfnXZ+4m8zLLzyIoWx6YeZBe1fzd+uvTxi2TyjvVr2rxcFq67IPQtvkFk/jg1cTActsuIlOjnUp/qN3Xxi8dGMPev4G22cqLC34dJnnjPukq16hThrQcdr6kbOmG+lh0STKtZlYBh1/TWW61to5goHhL+wkoiXKpl/fmgAjswHmaUgFdzoZjFgZtkL5milRQIQKBgQDRollouiSRK1WK0VfzkklhRe6rNJ7v1jD08YB3f0V29+Xps7kK+JbecocR1S68SLKomiRbN9hPqHNfLO6Ol/RnsYK+Pwns5sWn2we55EoZ/0Y9HKbq7qNtT3Y+PueGqh8HX97CDPYpQJvtpzO9/kLB3v3SIK/fe/oA8SX2rPBg2wKBgQC/0jQ/hDMHbhLmfQz3tsIyaLODyPnHSfOZNVL6jDgtLT59wSZStjpPGspbi634ziAEX3Kl0nEIeWaKgLhuPSuY80IfcWzYuZpXwlWGo8iQ+twTahkI51q5OD7a0Bp0s5ykxfSHcBnOkqXz5SaS04q5VI/miW0CrOR+CP6EVjd26QKBgQC1Zv5lkIYqFMyeJk8sV48mHITOI+fQ9JFCtGSNg1WSACw00i+y3bB357UX6ljZ1zK6kUAaqzWEoOjNGC2KuNVmkYXoqNYm6P9rJC8IXhL9chfOtCeArmY/Jf7VGEvzDO1yzTQOyXaN9fm4afJtW0NbTmJn3Z8zDa4l+Ib4cWhE9wKBgCR7JdSun8XZ+V48iirSZzyPDRUTM+3i8qFAsvdTk5uD706bXV59qblBrxqaBgw7p3sta/B9hWHHNOlCcdEFcTU1jm3MdJcDNYdbPkjM/WgVTaWjl/9JgMeZGtsimOV5A2GTBF59w2q2GTm5awyhjtCQdI2ztTe9CfeuXjls7D+BAoGBAKYh/O+CiExG9d/VaA2RsW36kDPEkIBXO2j/BU59LzPrbCypwDirfUm6TkxTv1nzS6duEQV4u3cmUJPXuSVGgO4IFr/BJFNM3zz94cRoz/axq53dc8ab7bHAfRmcmjxzjepoJO9uwt8P11a/pZLEd5P+i2iiXM1E4baM5WAExsCE";    
		//String publicKeyContent = new String(Files.readAllBytes(new File("c:/mansih/publicKey").toPath()));
	    String publicKeyContent   ="MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAnRRCq3gowGsKYaXhbUby/wEpiqqu5rRCd9P7hMh5PLJjdci86Zi+XeLANnBExOW62LVcCIggq/ikvEcV2hx86xs/aivDioS+vzWBOTFqDolN/ijtXLZ0WmreeAQTA9BhpsPWJPtlnTXn1h3meBHV/Y9i3bEbgeM1EgecPFZ3pO2haEufYfnmI7L7dYlji7wsuDc+e2MPB96s5bqV8Icf7pjI+ebsPrtrI1tPYDQJKrMVnL2ALjNbiw2WU2WIEWcAqeLgiowqDo7dZ9NzbA/FaTjK5NRqace2QUznKrCvhB7axlHj2fO6QNWpgUDcgxrCFlO1v5VJuoyF9mRUlHoZUwIDAQAB";

	       // privateKeyContent = privateKeyContent.replaceAll("\\n", "").replace("-----BEGIN PRIVATE KEY-----", "").replace("-----END PRIVATE KEY-----", "");
	     //   publicKeyContent = publicKeyContent.replaceAll("\\n", "").replace("-----BEGIN PUBLIC KEY-----", "").replace("-----END PUBLIC KEY-----", "");;

	        KeyFactory kf = KeyFactory.getInstance("RSA");

	        PKCS8EncodedKeySpec keySpecPKCS8 = new PKCS8EncodedKeySpec(Base64.getDecoder().decode(privateKeyContent));
	        PrivateKey privKey = kf.generatePrivate(keySpecPKCS8);

	        X509EncodedKeySpec keySpecX509 = new X509EncodedKeySpec(Base64.getDecoder().decode(publicKeyContent));
	        RSAPublicKey pubKey = (RSAPublicKey) kf.generatePublic(keySpecX509);

	       // System.out.println(privKey);
	       // System.out.println(pubKey);
	        
	      //This will be used at mobile side
			//USERID:PWD
			String msg = "seepimasupp:Tetra2018";
			String encrypted_msg = ac.encryptText(msg, pubKey);
			
			//This will be used at Apigee
			String decrypted_msg = ac.decryptText(encrypted_msg, privKey);
			System.out.println("Original Message: " + msg + "\nEncrypted Message: " + encrypted_msg
					+ "\nDecrypted Message: " + decrypted_msg);

	}
}
