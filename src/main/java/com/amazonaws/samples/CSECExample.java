package com.amazonaws.samples;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3EncryptionClientV2Builder;
import com.amazonaws.services.s3.AmazonS3EncryptionV2;
import com.amazonaws.services.s3.model.CryptoConfigurationV2;
import com.amazonaws.services.s3.model.CryptoMode;
import com.amazonaws.services.s3.model.EncryptionMaterials;
import com.amazonaws.services.s3.model.StaticEncryptionMaterialsProvider;

public class CSECExample {
	
	public static void main(String[] args) throws IOException, NoSuchAlgorithmException {
		encryptUsingCSEC();
	}
	
	public static void encryptUsingCSEC() throws NoSuchAlgorithmException
	{
		KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
        keyGenerator.init(256);
        
        SecretKey secretKey = keyGenerator.generateKey();
        
        
        
        String s3ObjectKey = "EncryptedContent2.txt";
        String s3ObjectContent = "This is the 2nd content to encrypt";
        
        String bucket_name = "demo3105";
        
        AmazonS3EncryptionV2 s3Encryption = AmazonS3EncryptionClientV2Builder.standard()
                .withRegion(Regions.AP_SOUTH_1)
                .withClientConfiguration(new ClientConfiguration())
                .withCryptoConfiguration(new CryptoConfigurationV2().withCryptoMode(CryptoMode.AuthenticatedEncryption))
                .withEncryptionMaterialsProvider(new StaticEncryptionMaterialsProvider(new EncryptionMaterials(secretKey)))
                .build();

        
        s3Encryption.putObject(bucket_name, s3ObjectKey, s3ObjectContent);
        //System.out.println(s3Encryption.getObjectAsString(bucket_name, s3ObjectKey));
        s3Encryption.shutdown();
        
        decryptUsingCSEC(secretKey);
	}

	
	public static void decryptUsingCSEC(SecretKey secretKey ) throws NoSuchAlgorithmException
	{
		KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
        keyGenerator.init(256);
        
        //SecretKey secretKey = keyGenerator.generateKey();
        
        String s3ObjectKey = "EncryptedContent2.txt";
        
        String bucket_name = "demo3105";
        
        
        
        AmazonS3EncryptionV2 s3Encryption = AmazonS3EncryptionClientV2Builder.standard()
                .withRegion(Regions.AP_SOUTH_1)
                .withClientConfiguration(new ClientConfiguration())
                .withCryptoConfiguration(new CryptoConfigurationV2().withCryptoMode(CryptoMode.AuthenticatedEncryption))
                .withEncryptionMaterialsProvider(new StaticEncryptionMaterialsProvider(new EncryptionMaterials(secretKey)))
                .build();

        
        s3Encryption.getObject(bucket_name, s3ObjectKey);
        System.out.println(s3Encryption.getObjectAsString(bucket_name, s3ObjectKey));
        s3Encryption.shutdown();
	}
}
