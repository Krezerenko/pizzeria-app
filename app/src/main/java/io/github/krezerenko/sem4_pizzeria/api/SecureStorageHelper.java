package io.github.krezerenko.sem4_pizzeria.api;

import android.content.Context;
import android.content.SharedPreferences;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyProperties;
import android.util.Base64;

import androidx.annotation.NonNull;

import java.io.IOException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;

public class SecureStorageHelper
{
    private static final String KEY_ALIAS = "secure_key_alias";
    private static final String PREFS_NAME = "secure_shared_prefs";
    private static final String DATA_KEY = "encrypted_data";
    private static final String IV_KEY = "iv";
    private final Context context;

    public SecureStorageHelper(Context context)
    {
        this.context = context.getApplicationContext();
    }

    // Initialize KeyStore and generate key if needed
    private KeyStore getKeyStore() throws KeyStoreException, CertificateException,
            IOException, NoSuchAlgorithmException
    {
        KeyStore keyStore = KeyStore.getInstance("AndroidKeyStore");
        keyStore.load(null);
        return keyStore;
    }

    private SecretKey getSecretKey() throws Exception
    {
        KeyStore keyStore = getKeyStore();

        if (!keyStore.containsAlias(KEY_ALIAS))
        {
            KeyGenParameterSpec keySpec = new KeyGenParameterSpec.Builder(
                    KEY_ALIAS,
                    KeyProperties.PURPOSE_ENCRYPT | KeyProperties.PURPOSE_DECRYPT
            )
                    .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
                    .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
                    .setKeySize(256)
                    .build();

            KeyGenerator keyGenerator = KeyGenerator.getInstance(
                    KeyProperties.KEY_ALGORITHM_AES,
                    "AndroidKeyStore"
            );
            keyGenerator.init(keySpec);
            keyGenerator.generateKey();
        }

        return (SecretKey) keyStore.getKey(KEY_ALIAS, null);
    }

    public void saveEncryptedData(@NonNull String data) throws Exception
    {
        SecretKey secretKey = getSecretKey();
        Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);

        byte[] iv = cipher.getIV();
        byte[] encryptedBytes = cipher.doFinal(data.getBytes());

        // Save IV and encrypted data
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
                .edit()
                .putString(IV_KEY, Base64.encodeToString(iv, Base64.DEFAULT))
                .putString(DATA_KEY, Base64.encodeToString(encryptedBytes, Base64.DEFAULT))
                .apply();
    }

    public String getDecryptedData() throws Exception
    {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        String ivString = prefs.getString(IV_KEY, null);
        String encryptedDataString = prefs.getString(DATA_KEY, null);

        if (ivString == null || encryptedDataString == null) return null;

        byte[] iv = Base64.decode(ivString, Base64.DEFAULT);
        byte[] encryptedData = Base64.decode(encryptedDataString, Base64.DEFAULT);

        SecretKey secretKey = getSecretKey();
        Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
        GCMParameterSpec spec = new GCMParameterSpec(128, iv);
        cipher.init(Cipher.DECRYPT_MODE, secretKey, spec);

        return new String(cipher.doFinal(encryptedData));
    }

    public void clearData()
    {
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
                .edit()
                .clear()
                .apply();
    }
}
