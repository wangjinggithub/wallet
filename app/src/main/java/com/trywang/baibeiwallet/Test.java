package com.trywang.baibeiwallet;

import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.Uint;
import org.web3j.crypto.CipherException;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.Web3jFactory;
import org.web3j.protocol.http.HttpService;

import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.util.Collections;

/**
 * TODO 写清楚此类的作用
 *
 * @author Try
 * @date 2018/9/21 14:14
 */
public class Test {
    public void test(){
        Web3j web3j = Web3jFactory.build(new HttpService());
//        web3j.ethSendTransaction()
        new Function("",
                Collections.singletonList(new Uint(BigInteger.valueOf(1))),
                Collections.singletonList(new TypeReference<Type>() {}));

//        byte[] initialEntropy = new byte[16];
//        new SecureRandom().nextBytes(initialEntropy);
//        org.web3j.crypto.MnemonicUtils.generateMnemonic(initialEntropy);
        try {
            org.web3j.crypto.WalletUtils.generateBip39Wallet("1",new File(""));
        } catch (CipherException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
