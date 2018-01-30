package com.tp.ad.authentication.util;

import java.io.UnsupportedEncodingException;
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

import com.tp.ad.model.User;

/**
 * 
 * @author SGSRIVASTAVM
 *
 */
public class Utility {
	private static Cipher cipher;

	static {
		try {
			cipher = Cipher.getInstance("RSA");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * @return
	 * @throws Exception
	 */
	public static User getUser(String userId, String password, String privateKey) throws Exception {
		User user = new User();
		String decrypted = decryptText(password, getPrivateKey(privateKey));
		user.setUserName(userId);
		user.setPassword(decrypted);
		return user;
	}

	/**
	 * 
	 * @param filename
	 * @return
	 * @throws Exception
	 */
	private static PrivateKey getPrivateKey(/* String filename */String privateKey) throws Exception {
		privateKey = privateKey.trim();
		KeyFactory kf = KeyFactory.getInstance("RSA");
		PKCS8EncodedKeySpec keySpecPKCS8 = new PKCS8EncodedKeySpec(Base64.getDecoder().decode(privateKey));
		PrivateKey privKey = kf.generatePrivate(keySpecPKCS8);
		return privKey;
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
	public static String decryptText(String msg, PrivateKey key)
			throws InvalidKeyException, UnsupportedEncodingException, IllegalBlockSizeException, BadPaddingException {
		cipher.init(Cipher.DECRYPT_MODE, key);
		return new String(cipher.doFinal(Base64.getDecoder().decode(msg)), "UTF-8");
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
	public static String encryptText(String msg, PublicKey key) throws NoSuchAlgorithmException, NoSuchPaddingException,
			UnsupportedEncodingException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException {
		cipher.init(Cipher.ENCRYPT_MODE, key);
		return Base64.getEncoder().encodeToString(cipher.doFinal(msg.getBytes("UTF-8")));
	}

	public static void main(String[] args) throws Exception {
		String credentials = "Q2n7J0/+7A/eac4NKbF5ELH7dlqxXoSYP79lZbtjK9+zsmk2PrIJHbJuE6MkpjJ7jW76wVoYHFYuzMS6W1WLXBhEoIoLp3prJdg6aXqu/4v6IDnCGLY2XbZ32jTDjPqwlunTS4ZENX1hlywHFKj7ipa8IazJQmmFmuFJ4FHehhDeegqP9N5tV9DJ3pv6f+sarv6JhE/FtQf/swWQ9ZmiuhA+iutL7aAAha89WuN1PkVO0oIy387TeUUzZnKDhbDuzOInPyPxUICi4quUHMHO5PkHgc6XGsdjbks04+o/hFNS385el1LXRlqiAnQFi97OuYg45yh6qxKzoLG/0FzX6w==";
		String privateKeyContent = "MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQCdFEKreCjAawphpeFtRvL/ASmKqq7mtEJ30/uEyHk8smN1yLzpmL5d4sA2cETE5brYtVwIiCCr+KS8RxXaHHzrGz9qK8OKhL6/NYE5MWoOiU3+KO1ctnRaat54BBMD0GGmw9Yk+2WdNefWHeZ4EdX9j2LdsRuB4zUSB5w8Vnek7aFoS59h+eYjsvt1iWOLvCy4Nz57Yw8H3qzlupXwhx/umMj55uw+u2sjW09gNAkqsxWcvYAuM1uLDZZTZYgRZwCp4uCKjCoOjt1n03NsD8VpOMrk1Gppx7ZBTOcqsK+EHtrGUePZ87pA1amBQNyDGsIWU7W/lUm6jIX2ZFSUehlTAgMBAAECggEAVrYif6mbRW1Rk8oQgVSTO7YWipfiaTTFJIT+6bQJSTposVKZBpLtM8LCvMqJC/CNjqe/IG6VKpndxpmvNHJZeyfVf/Scw1C4Q7FznnDqzQ964H0TWwTVgjxQhwwouwhDf93Hzy6AT6K8UnS7Dzk5WfnXZ+4m8zLLzyIoWx6YeZBe1fzd+uvTxi2TyjvVr2rxcFq67IPQtvkFk/jg1cTActsuIlOjnUp/qN3Xxi8dGMPev4G22cqLC34dJnnjPukq16hThrQcdr6kbOmG+lh0STKtZlYBh1/TWW61to5goHhL+wkoiXKpl/fmgAjswHmaUgFdzoZjFgZtkL5milRQIQKBgQDRollouiSRK1WK0VfzkklhRe6rNJ7v1jD08YB3f0V29+Xps7kK+JbecocR1S68SLKomiRbN9hPqHNfLO6Ol/RnsYK+Pwns5sWn2we55EoZ/0Y9HKbq7qNtT3Y+PueGqh8HX97CDPYpQJvtpzO9/kLB3v3SIK/fe/oA8SX2rPBg2wKBgQC/0jQ/hDMHbhLmfQz3tsIyaLODyPnHSfOZNVL6jDgtLT59wSZStjpPGspbi634ziAEX3Kl0nEIeWaKgLhuPSuY80IfcWzYuZpXwlWGo8iQ+twTahkI51q5OD7a0Bp0s5ykxfSHcBnOkqXz5SaS04q5VI/miW0CrOR+CP6EVjd26QKBgQC1Zv5lkIYqFMyeJk8sV48mHITOI+fQ9JFCtGSNg1WSACw00i+y3bB357UX6ljZ1zK6kUAaqzWEoOjNGC2KuNVmkYXoqNYm6P9rJC8IXhL9chfOtCeArmY/Jf7VGEvzDO1yzTQOyXaN9fm4afJtW0NbTmJn3Z8zDa4l+Ib4cWhE9wKBgCR7JdSun8XZ+V48iirSZzyPDRUTM+3i8qFAsvdTk5uD706bXV59qblBrxqaBgw7p3sta/B9hWHHNOlCcdEFcTU1jm3MdJcDNYdbPkjM/WgVTaWjl/9JgMeZGtsimOV5A2GTBF59w2q2GTm5awyhjtCQdI2ztTe9CfeuXjls7D+BAoGBAKYh/O+CiExG9d/VaA2RsW36kDPEkIBXO2j/BU59LzPrbCypwDirfUm6TkxTv1nzS6duEQV4u3cmUJPXuSVGgO4IFr/BJFNM3zz94cRoz/axq53dc8ab7bHAfRmcmjxzjepoJO9uwt8P11a/pZLEd5P+i2iiXM1E4baM5WAExsCE";
		String publicKeyContent = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAnRRCq3gowGsKYaXhbUby/wEpiqqu5rRCd9P7hMh5PLJjdci86Zi+XeLANnBExOW62LVcCIggq/ikvEcV2hx86xs/aivDioS+vzWBOTFqDolN/ijtXLZ0WmreeAQTA9BhpsPWJPtlnTXn1h3meBHV/Y9i3bEbgeM1EgecPFZ3pO2haEufYfnmI7L7dYlji7wsuDc+e2MPB96s5bqV8Icf7pjI+ebsPrtrI1tPYDQJKrMVnL2ALjNbiw2WU2WIEWcAqeLgiowqDo7dZ9NzbA/FaTjK5NRqace2QUznKrCvhB7axlHj2fO6QNWpgUDcgxrCFlO1v5VJuoyF9mRUlHoZUwIDAQAB";
		KeyFactory kf = KeyFactory.getInstance("RSA");
		User user = getUser("", credentials, privateKeyContent);
		System.out.println(user.getPassword());
		X509EncodedKeySpec keySpecX509 = new X509EncodedKeySpec(Base64.getDecoder().decode(publicKeyContent));
		RSAPublicKey pubKey = (RSAPublicKey) kf.generatePublic(keySpecX509);
		// This will be used at mobile side
		// USERID:PWD
		String msg = "Tetra2018";
		// String msg = "seepimasupp:Tetra2018";
		String encrypted_msg = encryptText(msg, pubKey);
		System.out.println(encrypted_msg);
		// This will be used at Apigee
		// String decrypted_msg =u.decryptText(encrypted_msg, privKey);
		// System.out.println("Original Message: " + msg + "\nEncrypted Message:
		// " + encrypted_msg
		// + "\nDecrypted Message: " + decrypted_msg);
		User user1 = getUser("seepimasupp", encrypted_msg, privateKeyContent);
		System.out.println(user1.getUserName());
	}

}
