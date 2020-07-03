package com.hungts.internetbanking.util;

import name.neuhalfen.projects.crypto.bouncycastle.openpgp.BouncyGPG;
import name.neuhalfen.projects.crypto.bouncycastle.openpgp.keys.callbacks.KeyringConfigCallbacks;
import name.neuhalfen.projects.crypto.bouncycastle.openpgp.keys.keyrings.InMemoryKeyring;
import name.neuhalfen.projects.crypto.bouncycastle.openpgp.keys.keyrings.KeyringConfigs;
import org.bouncycastle.openpgp.PGPException;
import org.bouncycastle.util.io.Streams;

import java.io.*;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SignatureException;

import static java.nio.charset.StandardCharsets.UTF_8;

public class PGPSecurity {

    public PGPSecurity() {

    }

    public static class ArmoredKeyPair {

        private final String privateKey;
        private final String publicKey;

        private ArmoredKeyPair(String privateKey, String publicKey) {
            this.privateKey = privateKey;
            this.publicKey = publicKey;
        }

        public String privateKey() {
            return privateKey;
        }

        public String publicKey() {
            return publicKey;
        }

        public static ArmoredKeyPair of(String privateKey, String publicKey) {
            return new ArmoredKeyPair(privateKey, publicKey);
        }
    }

    private InMemoryKeyring keyring(String passphrase,
                                    ArmoredKeyPair armoredKeyPair,
                                    String... recipientsArmoredPublicKey)
            throws IOException, PGPException {
        InMemoryKeyring keyring = KeyringConfigs.forGpgExportedKeys(KeyringConfigCallbacks.withPassword(passphrase));
        keyring.addSecretKey(armoredKeyPair.privateKey().getBytes(UTF_8));
        keyring.addPublicKey(armoredKeyPair.publicKey().getBytes(UTF_8));
        for (String recipientArmoredPublicKey : recipientsArmoredPublicKey) {
            keyring.addPublicKey(recipientArmoredPublicKey.getBytes(UTF_8));
        }
        return keyring;
    }

    public String encryptAndSign(String unencryptedMessage,
                                 String senderUserIdEmail,
                                 String senderPassphrase,
                                 ArmoredKeyPair senderArmoredKeyPair,
                                 String receiverUserId,
                                 String receiverArmoredPublicKey)
            throws IOException, PGPException, NoSuchAlgorithmException, SignatureException, NoSuchProviderException {

        InMemoryKeyring keyring = keyring(senderPassphrase, senderArmoredKeyPair, receiverArmoredPublicKey);

        ByteArrayOutputStream encryptedOutputStream = new ByteArrayOutputStream();
        try (
                BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(encryptedOutputStream);
                OutputStream bouncyGPGOutputStream = BouncyGPG.encryptToStream()
                        .withConfig(keyring)
                        .withStrongAlgorithms()
                        .toRecipient(receiverUserId)
                        .andSignWith(senderUserIdEmail)
                        .armorAsciiOutput()
                        .andWriteTo(bufferedOutputStream)
        ) {
            Streams.pipeAll(new ByteArrayInputStream(unencryptedMessage.getBytes()), bouncyGPGOutputStream);
        }

        return encryptedOutputStream.toString(UTF_8.name());
    }

    public String decryptAndVerify(String encryptedMessage,
                                   String receiverPassphrase,
                                   ArmoredKeyPair receiverArmoredKeyPair,
                                   String senderUserIdEmail,
                                   String senderArmoredPublicKey)
            throws IOException, PGPException, NoSuchProviderException {

        InMemoryKeyring keyring = keyring(receiverPassphrase, receiverArmoredKeyPair, senderArmoredPublicKey);

        ByteArrayOutputStream unencryptedOutputStream = new ByteArrayOutputStream();
        try (
                BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(unencryptedOutputStream);
                InputStream bouncyGPGInputStream = BouncyGPG
                        .decryptAndVerifyStream()
                        .withConfig(keyring)
                        .andRequireSignatureFromAllKeys(senderUserIdEmail)
                        .fromEncryptedInputStream(new ByteArrayInputStream(encryptedMessage.getBytes(UTF_8)))
        ) {
            Streams.pipeAll(bouncyGPGInputStream, bufferedOutputStream);
        }

        return unencryptedOutputStream.toString(UTF_8.name());
    }
}