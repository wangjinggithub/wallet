package com.trywang.baibeiwallet;

import android.util.Log;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.trywang.baibeiwallet.utils.MnemonicUtils;

import org.bitcoinj.core.NetworkParameters;
import org.bitcoinj.crypto.ChildNumber;
import org.bitcoinj.crypto.DeterministicHierarchy;
import org.bitcoinj.crypto.DeterministicKey;
import org.bitcoinj.crypto.HDKeyDerivation;
import org.bitcoinj.crypto.HDUtils;
import org.bitcoinj.params.MainNetParams;
import org.web3j.crypto.Bip39Wallet;
import org.web3j.crypto.CipherException;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.ECKeyPair;
import org.web3j.crypto.Keys;
import org.web3j.crypto.Wallet;
import org.web3j.crypto.WalletFile;
import org.web3j.utils.Numeric;

import java.io.File;
import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static org.web3j.crypto.Hash.sha256;
import static org.web3j.crypto.Keys.ADDRESS_LENGTH_IN_HEX;
import static org.web3j.crypto.Keys.PRIVATE_KEY_LENGTH_IN_HEX;

/**
 * Utility functions for working with Wallet files.
 */
public class WalletUtils {

    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final SecureRandom secureRandom = SecureRandomUtils.secureRandom();

    static {
        objectMapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    public static String generateFullNewWalletFile(String password, File destinationDirectory)
            throws NoSuchAlgorithmException, NoSuchProviderException,
            InvalidAlgorithmParameterException, CipherException, IOException {

        return generateNewWalletFile(password, destinationDirectory, true);
    }

    public static String generateLightNewWalletFile(String password, File destinationDirectory)
            throws NoSuchAlgorithmException, NoSuchProviderException,
            InvalidAlgorithmParameterException, CipherException, IOException {

        return generateNewWalletFile(password, destinationDirectory, false);
    }

    public static String generateNewWalletFile(
            String password, File destinationDirectory, boolean useFullScrypt)
            throws CipherException, IOException, InvalidAlgorithmParameterException,
            NoSuchAlgorithmException, NoSuchProviderException {

        ECKeyPair ecKeyPair = Keys.createEcKeyPair();
        return generateWalletFile(password, ecKeyPair, destinationDirectory, useFullScrypt);
    }

    public static String generateWalletFile(
            String password, ECKeyPair ecKeyPair, File destinationDirectory, boolean useFullScrypt)
            throws CipherException, IOException {

        WalletFile walletFile;
        if (useFullScrypt) {
            walletFile = Wallet.createStandard(password, ecKeyPair);
        } else {
            walletFile = Wallet.createLight(password, ecKeyPair);
        }

        String fileName = getWalletFileName(walletFile);
        File destination = new File(destinationDirectory, fileName);
        Log.i("钱包地址 = ", "generateWalletFile: 钱包地址 = " + walletFile.getAddress());

        objectMapper.writeValue(destination, walletFile);

        return fileName;
    }


    /**
     * 生成bip39 钱包
     * @param password 生成keystore用的密码，也是用于助记词生成种子所加的盐。
     * @param destinationDirectory keystore保存的目录
     * @return
     * @throws CipherException
     * @throws IOException
     */
    public static Bip39Wallet generateBip39Wallet(String password, File destinationDirectory)
            throws CipherException, IOException {
        Log.i("TAG", "generateBip39Wallet:钱包39生成的地址如下 ");
        byte[] initialEntropy = new byte[16];
        secureRandom.nextBytes(initialEntropy);

        String mnemonic = MnemonicUtils.generateMnemonic(initialEntropy);
//        byte[] seed = MnemonicUtils.generateSeed(mnemonic, password);
        mnemonic = "again fossil all tired ticket hurry gown modify submit minimum plate mountain";
        byte[] seed = MnemonicUtils.generateSeed(mnemonic, null);
        ECKeyPair privateKey = ECKeyPair.create(sha256(seed));
        Log.i("Bip39Wallet", "generateBip39Wallet: getPrivateKey = " + privateKey.getPrivateKey()
                + "\ngetPublicKey = " + privateKey.getPublicKey());

        String walletFile = generateWalletFile(password, privateKey, destinationDirectory, false);

        return new Bip39Wallet(walletFile, mnemonic);
    }


    public static Bip39Wallet generateBip44Wallet(String password, File destinationDirectory)
            throws CipherException, IOException {
        Log.i("TAG", "generateBip44Wallet:钱包44生成的地址如下 ");
        byte[] initialEntropy = new byte[16];
        secureRandom.nextBytes(initialEntropy);

        String mnemonic = MnemonicUtils.generateMnemonic(initialEntropy);
//        byte[] seed = MnemonicUtils.generateSeed(mnemonic, password);
        mnemonic = "again fossil all tired ticket hurry gown modify submit minimum plate mountain";
        //生成种子
        byte[] seed = MnemonicUtils.generateSeed(mnemonic, null);
        NetworkParameters params = MainNetParams.get();
        // 3. 生成根私钥 root private key

        DeterministicKey rootPrivateKey = HDKeyDerivation.createMasterPrivateKey(seed);
        // 4. 根私钥进行 priB58编码,得到测试网站上显示的数据
        String priv = rootPrivateKey.serializePrivB58(params);
        Log.i("TAG","BIP32 Extended Private Key:" + priv);
        // 5. 生成 HD 钱包 , 由根私钥
        DeterministicHierarchy dh = new DeterministicHierarchy(rootPrivateKey);
        // 6. 定义父路径
        List<ChildNumber> parentPath = HDUtils.parsePath("M/44H/60H/0H/0");
        // 7. 由父路径,派生出第一个子私钥 "new ChildNumber(0)" 表示第一个
        DeterministicKey child = dh.deriveChild(parentPath, true, true, new ChildNumber(0));
        Log.w("TAG"," BIP32 Extended private Key:" + child.serializePrivB58(params));
        String childPrivateKey = child.getPrivateKeyAsHex();
        String childPublicKey = child.getPublicKeyAsHex();
        ECKeyPair childEcKeyPair = ECKeyPair.create(child.getPrivKeyBytes());
        String childAddress = Keys.getAddress(childEcKeyPair);
        String fullAddress = "0x" + childAddress;
        Log.w("TAG","child privateKey:" + childPrivateKey + "\n" + "child publicKey:" + childPublicKey + "\n" + "address:" + fullAddress);
//        WalletFile walletFile = org.web3j.crypto.Wallet.createLight(password, childEcKeyPair);
//        String keystore = Singleton.get().gson.toJson(walletFile);
//        Log.w("TAG","keystore:\n" + keystore);
//        WalletManager.shared().saveWallet(context, walletName, walletFile, mnemonics);
//        return new HLWallet(mnemonics, walletName, childAddress);




//        AddressIndex addressIndex = BIP44
//                .m()
//                .purpose44()
//                .coinType(60)
//                .account(0)
//                .external()
//                .address(5);
//
//        String path = BIP44.m().purpose44()
//                .coinType(60)
//                .account(0)
//                .external()
//                .address(0)
//                .toString();
//
//        ExtendedPrivateKey key = ExtendedPrivateKey.fromSeed(seed, Bitcoin.MAIN_NET);
//        ExtendedPrivateKey child = key.derive(path);
//        ExtendedPublicKey childPub = child.neuter();
//
//        ExtendedPrivateKey addressKey = key
//                .derive(addressIndex, AddressIndex.DERIVATION);
//
//        byte[] privateKeyByte = child.extendedKeyByteArray();



//        byte[] seed = new SeedCalculator()
//                .withWordsFromWordList(English.INSTANCE)
//                .calculateSeed(mnemonicWordsInAList(助记词List), passphrase);


        byte[] privateKeyByte = child.getPrivKeyBytes();

//        ECKeyPair privateKey = ECKeyPair.create(sha256(seed));
        ECKeyPair privateKey = ECKeyPair.create(privateKeyByte);
        Log.i("Bip44Wallet", "generateBip44Wallet: getPrivateKey = " + privateKey.getPrivateKey()
                + "\ngetPublicKey = " + privateKey.getPublicKey());

        String walletFile = generateWalletFile(password, privateKey, destinationDirectory, false);

        return new Bip39Wallet(walletFile, mnemonic);
    }





    public static BaibeiWallet generateBip39WalletV2(String password)
            throws CipherException, IOException {
        byte[] initialEntropy = new byte[16];
        secureRandom.nextBytes(initialEntropy);

        String mnemonic = MnemonicUtils.generateMnemonic(initialEntropy);
//        byte[] seed = MnemonicUtils.generateSeed(mnemonic, password);
        byte[] seed = MnemonicUtils.generateSeed(mnemonic, null);
        ECKeyPair privateKey = ECKeyPair.create(sha256(seed));
        Log.i("Bip39Wallet", "generateBip39Wallet: getPrivateKey = " + privateKey.getPrivateKey()
                + "\ngetPublicKey = " + privateKey.getPublicKey());
        WalletFile walletFile;
        walletFile = Wallet.createLight(password, privateKey);
//        String walletFile = generateWalletFile(password, privateKey, destinationDirectory, false);

        return new BaibeiWallet(walletFile, mnemonic);
    }

    public static BaibeiWallet generateBip39WalletV2(String password,String mnemonic)
            throws CipherException, IOException {
//        byte[] seed = MnemonicUtils.generateSeed(mnemonic, password);
        byte[] seed = MnemonicUtils.generateSeed(mnemonic, null);
        ECKeyPair privateKey = ECKeyPair.create(sha256(seed));
        Log.i("Bip39Wallet", "generateBip39Wallet: getPrivateKey = " + privateKey.getPrivateKey()
                + "\ngetPublicKey = " + privateKey.getPublicKey());
        WalletFile walletFile;
        walletFile = Wallet.createLight(password, privateKey);
//        String walletFile = generateWalletFile(password, privateKey, destinationDirectory, false);

        return new BaibeiWallet(walletFile, mnemonic);
    }



    public static Credentials loadCredentials(String password, String source)
            throws IOException, CipherException {
        return loadCredentials(password, new File(source));
    }

    public static Credentials loadCredentials(String password, File source)
            throws IOException, CipherException {
        WalletFile walletFile = objectMapper.readValue(source, WalletFile.class);
        return Credentials.create(Wallet.decrypt(password, walletFile));
    }
    public static Credentials loadCredentials(String password, WalletFile walletFile )
            throws IOException, CipherException {
        return Credentials.create(Wallet.decrypt(password, walletFile));
    }



    public static boolean checkPwd(String password, WalletFile walletFile) {
        ECKeyPair ep = null;
        try {
//             ep = Wallet.decrypt(password, walletFile);
             ep = com.trywang.baibeiwallet.Wallet.decrypt(password, walletFile);
        } catch (CipherException e) {
            e.printStackTrace();
        }
        return ep != null;
    }

    public static Credentials loadBip39Credentials(String password, String mnemonic) {
//        byte[] seed = MnemonicUtils.generateSeed(mnemonic, password);
        byte[] seed = MnemonicUtils.generateSeed(mnemonic, null);
        return Credentials.create(ECKeyPair.create(sha256(seed)));
    }
    public static Credentials loadBip39Credentials2(String password, String mnemonic) {
        byte[] seed = MnemonicUtils.generateSeed(mnemonic, password);
        return Credentials.create(ECKeyPair.create(sha256(seed)));
    }

    private static String getWalletFileName(WalletFile walletFile) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("'UTC--'yyyy-MM-dd'T'HH-mm-ss.SSS'--'");
        return dateFormat.format(new Date()) + walletFile.getAddress() + ".json";
    }

    public static String getDefaultKeyDirectory() {
        return getDefaultKeyDirectory(System.getProperty("os.name"));
    }

    static String getDefaultKeyDirectory(String osName1) {
        String osName = osName1.toLowerCase();

        if (osName.startsWith("mac")) {
            return String.format(
                    "%s%sLibrary%sEthereum", System.getProperty("user.home"), File.separator,
                    File.separator);
        } else if (osName.startsWith("win")) {
            return String.format("%s%sEthereum", System.getenv("APPDATA"), File.separator);
        } else {
            return String.format("%s%s.ethereum", System.getProperty("user.home"), File.separator);
        }
    }

    public static String getTestnetKeyDirectory() {
        return String.format(
                "%s%stestnet%skeystore", getDefaultKeyDirectory(), File.separator, File.separator);
    }

    public static String getMainnetKeyDirectory() {
        return String.format("%s%skeystore", getDefaultKeyDirectory(), File.separator);
    }

    public static boolean isValidPrivateKey(String privateKey) {
        String cleanPrivateKey = Numeric.cleanHexPrefix(privateKey);
        return cleanPrivateKey.length() == PRIVATE_KEY_LENGTH_IN_HEX;
    }

    public static boolean isValidAddress(String input) {
        String cleanInput = Numeric.cleanHexPrefix(input);

        try {
            Numeric.toBigIntNoPrefix(cleanInput);
        } catch (NumberFormatException e) {
            return false;
        }

        return cleanInput.length() == ADDRESS_LENGTH_IN_HEX;
    }
}
