package com.trywang.baibeiwallet.solidity;

import org.web3j.abi.EventEncoder;
import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Bool;
import org.web3j.abi.datatypes.DynamicArray;
import org.web3j.abi.datatypes.Event;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.Utf8String;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.RemoteCall;
import org.web3j.protocol.core.methods.request.EthFilter;
import org.web3j.protocol.core.methods.response.Log;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tx.Contract;
import org.web3j.tx.TransactionManager;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;

import rx.Observable;
import rx.functions.Func1;

/**
 * <p>Auto generated code.
 * <p><strong>Do not modify!</strong>
 * <p>Please use the <a href="https://docs.web3j.io/command_line.html">web3j command line tools</a>,
 * or the org.web3j.codegen.SolidityFunctionWrapperGenerator in the 
 * <a href="https://github.com/web3j/web3j/tree/master/codegen">codegen module</a> to update.
 *
 * <p>Generated with web3j version 3.5.0.
 */
public class CloudTreeToken extends Contract {
    private static final String BINARY = "60806040526000805460a060020a60ff021916815560038190556004553480156200002957600080fd5b50604051620019b5380380620019b58339810160409081528151602080840151928401516060850151608086015160008054600160a060020a03191633178155600186905595870180519597909693810195929491019291829182916200009791600991908a0190620001af565b508551620000ad90600a906020890190620001af565b50600b8590558351620000c890600c90602087019062000234565b50600c549250620000e88884640100000000620016406200019782021704565b9150600090505b8281101562000189578160026000600c848154811015156200010d57fe5b6000918252602080832090910154600160a060020a03168352820192909252604001812091909155600c80546001926007929091859081106200014c57fe5b600091825260208083209190910154600160a060020a031683528201929092526040019020805460ff1916911515919091179055600101620000ef565b5050505050505050620002e1565b6000808284811515620001a657fe5b04949350505050565b828054600181600116156101000203166002900490600052602060002090601f016020900481019282601f10620001f257805160ff191683800117855562000222565b8280016001018555821562000222579182015b828111156200022257825182559160200191906001019062000205565b50620002309291506200029a565b5090565b8280548282559060005260206000209081019282156200028c579160200282015b828111156200028c5782518254600160a060020a031916600160a060020a0390911617825560209092019160019091019062000255565b5062000230929150620002ba565b620002b791905b80821115620002305760008155600101620002a1565b90565b620002b791905b8082111562000230578054600160a060020a0319168155600101620002c1565b6116c480620002f16000396000f3006080604052600436106101e25763ffffffff7c010000000000000000000000000000000000000000000000000000000060003504166306fdde0381146101e7578063095ea7b3146102715780630ecb93c0146102a957806318160ddd146102cc57806323b872dd146102f357806327e235e31461031d578063282d3fdf1461033e5780632eb17e5e14610362578063313ce5671461038357806335390714146103985780633eaaf86b146103ad5780633f4ba83a146103c25780635543bce7146103d75780635816ba9d1461043c57806359bf1abe1461045d5780635c6581651461047e5780635c975abb146104a5578063649128b8146104ba57806370a08231146104db57806376e6d2b4146104fc5780638456cb591461051d578063893d20e8146105325780638da5cb5b1461056357806395d89b4114610578578063a9059cbb1461058d578063aa31aee8146105b1578063c0324c77146105d2578063c0459a22146105ed578063cc872b6614610605578063d05166501461061d578063db006a751461063e578063dd62ed3e14610656578063dd644f721461067d578063e47d606014610692578063e4997dc5146106b3578063e5b5019a146106d4578063f2fde38b146106e9578063f3bdc2281461070a578063fc96f7d81461072b575b600080fd5b3480156101f357600080fd5b506101fc61074c565b6040805160208082528351818301528351919283929083019185019080838360005b8381101561023657818101518382015260200161021e565b50505050905090810190601f1680156102635780820380516001836020036101000a031916815260200191505b509250505060405180910390f35b34801561027d57600080fd5b50610295600160a060020a03600435166024356107da565b604080519115158252519081900360200190f35b3480156102b557600080fd5b506102ca600160a060020a0360043516610800565b005b3480156102d857600080fd5b506102e1610872565b60408051918252519081900360200190f35b3480156102ff57600080fd5b50610295600160a060020a0360043581169060243516604435610878565b34801561032957600080fd5b506102e1600160a060020a036004351661090b565b34801561034a57600080fd5b506102ca600160a060020a036004351660243561091d565b34801561036e57600080fd5b506102e1600160a060020a0360043516610994565b34801561038f57600080fd5b506102e16109a6565b3480156103a457600080fd5b506102e16109ac565b3480156103b957600080fd5b506102e16109b2565b3480156103ce57600080fd5b506102ca6109b8565b3480156103e357600080fd5b506103ec610a2e565b60408051602080825283518183015283519192839290830191858101910280838360005b83811015610428578181015183820152602001610410565b505050509050019250505060405180910390f35b34801561044857600080fd5b506102ca600160a060020a0360043516610a90565b34801561046957600080fd5b50610295600160a060020a0360043516610aff565b34801561048a57600080fd5b506102e1600160a060020a0360043581169060243516610b1d565b3480156104b157600080fd5b50610295610b3a565b3480156104c657600080fd5b50610295600160a060020a0360043516610b4a565b3480156104e757600080fd5b506102e1600160a060020a0360043516610b5f565b34801561050857600080fd5b506102ca600160a060020a0360043516610b70565b34801561052957600080fd5b506102ca610bda565b34801561053e57600080fd5b50610547610c55565b60408051600160a060020a039092168252519081900360200190f35b34801561056f57600080fd5b50610547610c64565b34801561058457600080fd5b506101fc610c73565b34801561059957600080fd5b50610295600160a060020a0360043516602435610cce565b3480156105bd57600080fd5b506102e1600160a060020a0360043516610d4d565b3480156105de57600080fd5b506102ca600435602435610d68565b3480156105f957600080fd5b50610547600435610dfd565b34801561061157600080fd5b506102ca600435610e25565b34801561062957600080fd5b506102ca600160a060020a0360043516610ed0565b34801561064a57600080fd5b506102ca600435610f42565b34801561066257600080fd5b506102e1600160a060020a0360043581169060243516610fed565b34801561068957600080fd5b506102e1610ff9565b34801561069e57600080fd5b50610295600160a060020a0360043516610fff565b3480156106bf57600080fd5b506102ca600160a060020a0360043516611014565b3480156106e057600080fd5b506102e1611083565b3480156106f557600080fd5b506102ca600160a060020a0360043516611089565b34801561071657600080fd5b506102ca600160a060020a03600435166110db565b34801561073757600080fd5b50610295600160a060020a0360043516611187565b6009805460408051602060026001851615610100026000190190941693909304601f810184900484028201840190925281815292918301828280156107d25780601f106107a7576101008083540402835291602001916107d2565b820191906000526020600020905b8154815290600101906020018083116107b557829003601f168201915b505050505081565b6000604060443610156107ec57600080fd5b6107f684846111a5565b91505b5092915050565b600054600160a060020a0316331461081757600080fd5b600160a060020a038116600081815260066020908152604091829020805460ff19166001179055815192835290517f42e160154868087d6bfdc0ca23d96a1c1cfa32f1b72ba9ba27b69b98a0d819dc9281900390910190a150565b60015490565b6000805460a060020a900460ff161561089057600080fd5b336000908152600860205260409020544210156108ac57600080fd5b600160a060020a03841660009081526006602052604090205460ff16156108d257600080fd5b600160a060020a03841660009081526007602052604090205460ff16156108f857600080fd5b61090384848461125a565b949350505050565b60026020526000908152604090205481565b600054600160a060020a0316331461093457600080fd5b42811161094057600080fd5b600160a060020a038216600081815260086020908152604091829020849055815192835290517fc1b5f12cea7c200ad495a43bf2d4c7ba1a753343c06c339093937849de84d9139281900390910190a15050565b60086020526000908152604090205481565b600b5481565b60045481565b60015481565b600054600160a060020a031633146109cf57600080fd5b60005460a060020a900460ff1615156109e757600080fd5b6000805474ff0000000000000000000000000000000000000000191681556040517f7805862f689e2f13df9f062ff482ad3ad112aca9e0847911ed832e158c525b339190a1565b6060600c805480602002602001604051908101604052809291908181526020018280548015610a8657602002820191906000526020600020905b8154600160a060020a03168152600190910190602001808311610a68575b5050505050905090565b600054600160a060020a03163314610aa757600080fd5b600160a060020a038116600081815260076020908152604091829020805460ff19169055815192835290517ff5a4804bd25caebf95f97bb64db16c93060d1e55fb7e75acc09653c4cccc40d79281900390910190a150565b600160a060020a031660009081526006602052604090205460ff1690565b600560209081526000928352604080842090915290825290205481565b60005460a060020a900460ff1681565b60076020526000908152604090205460ff1681565b6000610b6a8261145c565b92915050565b600054600160a060020a03163314610b8757600080fd5b600160a060020a038116600081815260086020908152604080832092909255815192835290517f9e4b5873dcdfeaf6bf534d422fe7d4748b91bc3fc2ea0e5e4c67f74dd8a13c549281900390910190a150565b600054600160a060020a03163314610bf157600080fd5b60005460a060020a900460ff1615610c0857600080fd5b6000805474ff0000000000000000000000000000000000000000191660a060020a1781556040517f6985a02210a168e66602d3235cb6db0e70f92b3ba4d376a33c0f3d9434bff6259190a1565b600054600160a060020a031690565b600054600160a060020a031681565b600a805460408051602060026001851615610100026000190190941693909304601f810184900484028201840190925281815292918301828280156107d25780601f106107a7576101008083540402835291602001916107d2565b6000805460a060020a900460ff1615610ce657600080fd5b33600090815260086020526040902054421015610d0257600080fd5b3360009081526006602052604090205460ff1615610d1f57600080fd5b3360009081526007602052604090205460ff1615610d3c57600080fd5b610d468383611477565b9392505050565b600160a060020a031660009081526008602052604090205490565b600054600160a060020a03163314610d7f57600080fd5b60148210610d8c57600080fd5b60328110610d9957600080fd5b6003829055600b54610db5908290600a0a63ffffffff6115ea16565b600481905560035460408051918252602082019290925281517fb044a1e409eac5c48e5af22d4af52670dd1a99059537a78b31b48c6500a6354e929181900390910190a15050565b600c805482908110610e0b57fe5b600091825260209091200154600160a060020a0316905081565b600054600160a060020a03163314610e3c57600080fd5b60015481810111610e4c57600080fd5b60008054600160a060020a031681526002602052604090205481810111610e7257600080fd5b60008054600160a060020a03168152600260209081526040918290208054840190556001805484019055815183815291517fcb8241adb0c3fdb35b70c24ce35c5eb0c17af7431c99f827d44a445ca624176a9281900390910190a150565b600054600160a060020a03163314610ee757600080fd5b600160a060020a038116600081815260076020908152604091829020805460ff19166001179055815192835290517f8a5c4736a33c7b7f29a2c34ea9ff9608afc5718d56f6fd6dcbd2d3711a1a49139281900390910190a150565b600054600160a060020a03163314610f5957600080fd5b600154811115610f6857600080fd5b60008054600160a060020a0316815260026020526040902054811115610f8d57600080fd5b60018054829003905560008054600160a060020a031681526002602090815260409182902080548490039055815183815291517f702d5967f45f6513a38ffc42d6ba9bf230bd40e8f53b16363c7eb4fd2deb9a449281900390910190a150565b6000610d468383611615565b60035481565b60066020526000908152604090205460ff1681565b600054600160a060020a0316331461102b57600080fd5b600160a060020a038116600081815260066020908152604091829020805460ff19169055815192835290517fd7e9ec6e6ecd65492dce6bf513cd6867560d49544421d0783ddf06e76c24470c9281900390910190a150565b60001981565b600054600160a060020a031633146110a057600080fd5b600160a060020a038116156110d8576000805473ffffffffffffffffffffffffffffffffffffffff1916600160a060020a0383161790555b50565b60008054600160a060020a031633146110f357600080fd5b600160a060020a03821660009081526006602052604090205460ff16151561111a57600080fd5b61112382610b5f565b600160a060020a0383166000818152600260209081526040808320929092556001805485900390558151928352820183905280519293507f61e6e66b0d6339b2980aecc6ccc0039736791f0ccde9ed512e789a7fbdd698c692918290030190a15050565b600160a060020a031660009081526007602052604090205460ff1690565b6000604060443610156111b757600080fd5b82158015906111e85750336000908152600560209081526040808320600160a060020a038816845290915290205415155b156111f257600080fd5b336000818152600560209081526040808320600160a060020a03891680855290835292819020879055805187815290519293927f8c5be1e5ebec7d5bd14f71427d1e84f3dd0314c0f7b2291e5b200ac8c7c3b925929181900390910190a35060019392505050565b60008080806060606436101561126f57600080fd5b600160a060020a03881660009081526005602090815260408083203384529091529020546003549094506112be90612710906112b290899063ffffffff6115ea16565b9063ffffffff61164016565b92506004548311156112d05760045492505b60001984101561130f576112ea848763ffffffff61165716565b600160a060020a03891660009081526005602090815260408083203384529091529020555b61131f868463ffffffff61165716565b600160a060020a03891660009081526002602052604090205490925061134b908763ffffffff61165716565b600160a060020a03808a166000908152600260205260408082209390935590891681522054611380908363ffffffff61166916565b600160a060020a0388166000908152600260205260408120919091558311156114155760008054600160a060020a03168152600260205260409020546113cc908463ffffffff61166916565b60008054600160a060020a0390811682526002602090815260408084209490945591548351878152935190821693918c1692600080516020611679833981519152928290030190a35b86600160a060020a031688600160a060020a0316600080516020611679833981519152846040518082815260200191505060405180910390a3506001979650505050505050565b600160a060020a031660009081526002602052604090205490565b600080806040604436101561148b57600080fd5b6114a66127106112b2600354886115ea90919063ffffffff16565b92506004548311156114b85760045492505b6114c8858463ffffffff61165716565b336000908152600260205260409020549092506114eb908663ffffffff61165716565b3360009081526002602052604080822092909255600160a060020a0388168152205461151d908363ffffffff61166916565b600160a060020a0387166000908152600260205260408120919091558311156115b05760008054600160a060020a0316815260026020526040902054611569908463ffffffff61166916565b60008054600160a060020a03908116825260026020908152604080842094909455915483518781529351911692339260008051602061167983398151915292918290030190a35b604080518381529051600160a060020a0388169133916000805160206116798339815191529181900360200190a350600195945050505050565b6000808315156115fd57600091506107f9565b5082820282848281151561160d57fe5b0414610d4657fe5b600160a060020a03918216600090815260056020908152604080832093909416825291909152205490565b600080828481151561164e57fe5b04949350505050565b60008282111561166357fe5b50900390565b600082820183811015610d4657fe00ddf252ad1be2c89b69c2b068fc378daa952ba7f163c4a11628f55a4df523b3efa165627a7a7230582069459cfaeaaf1176ee3732fa5624e7b3e47d03136b688420b2e5cf4b4d7143150029";

    public static final String FUNC_NAME = "name";

    public static final String FUNC_APPROVE = "approve";

    public static final String FUNC_ADDBLACKLIST = "addBlackList";

    public static final String FUNC_TOTALSUPPLY = "totalSupply";

    public static final String FUNC_TRANSFERFROM = "transferFrom";

    public static final String FUNC_BALANCES = "balances";

    public static final String FUNC_LOCK = "lock";

    public static final String FUNC_ISLOCKTIMELIST = "isLockTimeList";

    public static final String FUNC_DECIMALS = "decimals";

    public static final String FUNC_MAXIMUMFEE = "maximumFee";

    public static final String FUNC__TOTALSUPPLY = "_totalSupply";

    public static final String FUNC_UNPAUSE = "unpause";

    public static final String FUNC_GETINITADDRESSES = "getInitAddresses";

    public static final String FUNC_UNFROZEN = "unFrozen";

    public static final String FUNC_GETBLACKLISTSTATUS = "getBlackListStatus";

    public static final String FUNC_ALLOWED = "allowed";

    public static final String FUNC_PAUSED = "paused";

    public static final String FUNC_ISFROZENLIST = "isFrozenList";

    public static final String FUNC_BALANCEOF = "balanceOf";

    public static final String FUNC_UNLOCK = "unLock";

    public static final String FUNC_PAUSE = "pause";

    public static final String FUNC_GETOWNER = "getOwner";

    public static final String FUNC_OWNER = "owner";

    public static final String FUNC_SYMBOL = "symbol";

    public static final String FUNC_TRANSFER = "transfer";

    public static final String FUNC_GETLOCKTIME = "getLockTime";

    public static final String FUNC_SETPARAMS = "setParams";

    public static final String FUNC_INITADDRESSES = "initAddresses";

    public static final String FUNC_ISSUE = "issue";

    public static final String FUNC_FROZEN = "frozen";

    public static final String FUNC_REDEEM = "redeem";

    public static final String FUNC_ALLOWANCE = "allowance";

    public static final String FUNC_BASISPOINTSRATE = "basisPointsRate";

    public static final String FUNC_ISBLACKLISTED = "isBlackListed";

    public static final String FUNC_REMOVEBLACKLIST = "removeBlackList";

    public static final String FUNC_MAX_UINT = "MAX_UINT";

    public static final String FUNC_TRANSFEROWNERSHIP = "transferOwnership";

    public static final String FUNC_DESTROYBLACKFUNDS = "destroyBlackFunds";

    public static final String FUNC_GETFROZENSTATUS = "getFrozenStatus";

    public static final Event ISSUE_EVENT = new Event("Issue", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}),
            Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
    ;

    public static final Event REDEEM_EVENT = new Event("Redeem", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}),
            Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
    ;

    public static final Event PARAMS_EVENT = new Event("Params", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}),
            Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}));
    ;

    public static final Event LOCK_EVENT = new Event("Lock", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}),
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
    ;

    public static final Event UNLOCK_EVENT = new Event("UnLock", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}),
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
    ;

    public static final Event FROZEN_EVENT = new Event("Frozen",
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}),
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
    ;

    public static final Event UNFROZEN_EVENT = new Event("UnFrozen", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}),
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
    ;

    public static final Event DESTROYEDBLACKFUNDS_EVENT = new Event("DestroyedBlackFunds", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}, new TypeReference<Uint256>() {}),
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}, new TypeReference<Uint256>() {}));
    ;

    public static final Event ADDEDBLACKLIST_EVENT = new Event("AddedBlackList", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}),
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
    ;

    public static final Event REMOVEDBLACKLIST_EVENT = new Event("RemovedBlackList", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}),
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
    ;

    public static final Event APPROVAL_EVENT = new Event("Approval", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}, new TypeReference<Address>() {}, new TypeReference<Uint256>() {}),
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}, new TypeReference<Address>() {}, new TypeReference<Uint256>() {}));
    ;

    public static final Event TRANSFER_EVENT = new Event("Transfer", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}, new TypeReference<Address>() {}, new TypeReference<Uint256>() {}),
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}, new TypeReference<Address>() {}, new TypeReference<Uint256>() {}));
    ;

    public static final Event PAUSE_EVENT = new Event("Pause", 
            Arrays.<TypeReference<?>>asList(),
            Arrays.<TypeReference<?>>asList());
    ;

    public static final Event UNPAUSE_EVENT = new Event("Unpause", 
            Arrays.<TypeReference<?>>asList(),
            Arrays.<TypeReference<?>>asList());
    ;

    protected CloudTreeToken(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    protected CloudTreeToken(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public RemoteCall<String> name() {
        final Function function = new Function(FUNC_NAME, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteCall<TransactionReceipt> approve(String _spender, BigInteger _value) {
        final Function function = new Function(
                FUNC_APPROVE, 
                Arrays.<Type>asList(new Address(_spender),
                new Uint256(_value)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<TransactionReceipt> addBlackList(String _evilUser) {
        final Function function = new Function(
                FUNC_ADDBLACKLIST, 
                Arrays.<Type>asList(new Address(_evilUser)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<BigInteger> totalSupply() {
        final Function function = new Function(FUNC_TOTALSUPPLY, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteCall<TransactionReceipt> transferFrom(String _from, String _to, BigInteger _value) {
        final Function function = new Function(
                FUNC_TRANSFERFROM, 
                Arrays.<Type>asList(new Address(_from),
                new Address(_to),
                new Uint256(_value)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<BigInteger> balances(String param0) {
        final Function function = new Function(FUNC_BALANCES, 
                Arrays.<Type>asList(new Address(param0)),
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteCall<TransactionReceipt> lock(String lockAddress, BigInteger lockTime) {
        final Function function = new Function(
                FUNC_LOCK, 
                Arrays.<Type>asList(new Address(lockAddress),
                new Uint256(lockTime)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<BigInteger> isLockTimeList(String param0) {
        final Function function = new Function(FUNC_ISLOCKTIMELIST, 
                Arrays.<Type>asList(new Address(param0)),
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteCall<BigInteger> decimals() {
        final Function function = new Function(FUNC_DECIMALS, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteCall<BigInteger> maximumFee() {
        final Function function = new Function(FUNC_MAXIMUMFEE, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteCall<BigInteger> _totalSupply() {
        final Function function = new Function(FUNC__TOTALSUPPLY, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteCall<TransactionReceipt> unpause() {
        final Function function = new Function(
                FUNC_UNPAUSE, 
                Arrays.<Type>asList(), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<List> getInitAddresses() {
        final Function function = new Function(FUNC_GETINITADDRESSES, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<DynamicArray<Address>>() {}));
        return new RemoteCall<List>(
                new Callable<List>() {
                    @Override
                    @SuppressWarnings("unchecked")
                    public List call() throws Exception {
                        List<Type> result = (List<Type>) executeCallSingleValueReturn(function, List.class);
                        return convertToNative(result);
                    }
                });
    }

    public RemoteCall<TransactionReceipt> unFrozen(String _clearedUser) {
        final Function function = new Function(
                FUNC_UNFROZEN, 
                Arrays.<Type>asList(new Address(_clearedUser)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<Boolean> getBlackListStatus(String _maker) {
        final Function function = new Function(FUNC_GETBLACKLISTSTATUS, 
                Arrays.<Type>asList(new Address(_maker)),
                Arrays.<TypeReference<?>>asList(new TypeReference<Bool>() {}));
        return executeRemoteCallSingleValueReturn(function, Boolean.class);
    }

    public RemoteCall<BigInteger> allowed(String param0, String param1) {
        final Function function = new Function(FUNC_ALLOWED, 
                Arrays.<Type>asList(new Address(param0),
                new Address(param1)),
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteCall<Boolean> paused() {
        final Function function = new Function(FUNC_PAUSED, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Bool>() {}));
        return executeRemoteCallSingleValueReturn(function, Boolean.class);
    }

    public RemoteCall<Boolean> isFrozenList(String param0) {
        final Function function = new Function(FUNC_ISFROZENLIST, 
                Arrays.<Type>asList(new Address(param0)),
                Arrays.<TypeReference<?>>asList(new TypeReference<Bool>() {}));
        return executeRemoteCallSingleValueReturn(function, Boolean.class);
    }

    public RemoteCall<BigInteger> balanceOf(String who) {
        final Function function = new Function(FUNC_BALANCEOF, 
                Arrays.<Type>asList(new Address(who)),
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteCall<TransactionReceipt> unLock(String unLockAddress) {
        final Function function = new Function(
                FUNC_UNLOCK, 
                Arrays.<Type>asList(new Address(unLockAddress)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<TransactionReceipt> pause() {
        final Function function = new Function(
                FUNC_PAUSE, 
                Arrays.<Type>asList(), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<String> getOwner() {
        final Function function = new Function(FUNC_GETOWNER, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteCall<String> owner() {
        final Function function = new Function(FUNC_OWNER, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteCall<String> symbol() {
        final Function function = new Function(FUNC_SYMBOL, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteCall<TransactionReceipt> transfer(String _to, BigInteger _value) {
        final Function function = new Function(
                FUNC_TRANSFER, 
                Arrays.<Type>asList(new Address(_to),
                new Uint256(_value)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<BigInteger> getLockTime(String _maker) {
        final Function function = new Function(FUNC_GETLOCKTIME, 
                Arrays.<Type>asList(new Address(_maker)),
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteCall<TransactionReceipt> setParams(BigInteger newBasisPoints, BigInteger newMaxFee) {
        final Function function = new Function(
                FUNC_SETPARAMS, 
                Arrays.<Type>asList(new Uint256(newBasisPoints),
                new Uint256(newMaxFee)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<String> initAddresses(BigInteger param0) {
        final Function function = new Function(FUNC_INITADDRESSES, 
                Arrays.<Type>asList(new Uint256(param0)),
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteCall<TransactionReceipt> issue(BigInteger amount) {
        final Function function = new Function(
                FUNC_ISSUE, 
                Arrays.<Type>asList(new Uint256(amount)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<TransactionReceipt> frozen(String _evilUser) {
        final Function function = new Function(
                FUNC_FROZEN, 
                Arrays.<Type>asList(new Address(_evilUser)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<TransactionReceipt> redeem(BigInteger amount) {
        final Function function = new Function(
                FUNC_REDEEM, 
                Arrays.<Type>asList(new Uint256(amount)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<BigInteger> allowance(String _owner, String _spender) {
        final Function function = new Function(FUNC_ALLOWANCE, 
                Arrays.<Type>asList(new Address(_owner),
                new Address(_spender)),
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteCall<BigInteger> basisPointsRate() {
        final Function function = new Function(FUNC_BASISPOINTSRATE, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteCall<Boolean> isBlackListed(String param0) {
        final Function function = new Function(FUNC_ISBLACKLISTED, 
                Arrays.<Type>asList(new Address(param0)),
                Arrays.<TypeReference<?>>asList(new TypeReference<Bool>() {}));
        return executeRemoteCallSingleValueReturn(function, Boolean.class);
    }

    public RemoteCall<TransactionReceipt> removeBlackList(String _clearedUser) {
        final Function function = new Function(
                FUNC_REMOVEBLACKLIST, 
                Arrays.<Type>asList(new Address(_clearedUser)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<BigInteger> MAX_UINT() {
        final Function function = new Function(FUNC_MAX_UINT, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteCall<TransactionReceipt> transferOwnership(String newOwner) {
        final Function function = new Function(
                FUNC_TRANSFEROWNERSHIP, 
                Arrays.<Type>asList(new Address(newOwner)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<TransactionReceipt> destroyBlackFunds(String _blackListedUser) {
        final Function function = new Function(
                FUNC_DESTROYBLACKFUNDS, 
                Arrays.<Type>asList(new Address(_blackListedUser)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<Boolean> getFrozenStatus(String _maker) {
        final Function function = new Function(FUNC_GETFROZENSTATUS, 
                Arrays.<Type>asList(new Address(_maker)),
                Arrays.<TypeReference<?>>asList(new TypeReference<Bool>() {}));
        return executeRemoteCallSingleValueReturn(function, Boolean.class);
    }

    public static RemoteCall<CloudTreeToken> deploy(Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit, BigInteger _initialSupply, String _name, String _symbol, BigInteger _decimals, List<String> _initAddresses) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new Uint256(_initialSupply),
                new Utf8String(_name),
                new Utf8String(_symbol),
                new Uint256(_decimals),
                new DynamicArray<Address>(
                        org.web3j.abi.Utils.typeMap(_initAddresses, Address.class))));
        return deployRemoteCall(CloudTreeToken.class, web3j, credentials, gasPrice, gasLimit, BINARY, encodedConstructor);
    }

    public static RemoteCall<CloudTreeToken> deploy(Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit, BigInteger _initialSupply, String _name, String _symbol, BigInteger _decimals, List<String> _initAddresses) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new Uint256(_initialSupply),
                new Utf8String(_name),
                new Utf8String(_symbol),
                new Uint256(_decimals),
                new DynamicArray<Address>(
                        org.web3j.abi.Utils.typeMap(_initAddresses, Address.class))));
        return deployRemoteCall(CloudTreeToken.class, web3j, transactionManager, gasPrice, gasLimit, BINARY, encodedConstructor);
    }

    public List<IssueEventResponse> getIssueEvents(TransactionReceipt transactionReceipt) {
        List<EventValuesWithLog> valueList = extractEventParametersWithLog(ISSUE_EVENT, transactionReceipt);
        ArrayList<IssueEventResponse> responses = new ArrayList<IssueEventResponse>(valueList.size());
        for (EventValuesWithLog eventValues : valueList) {
            IssueEventResponse typedResponse = new IssueEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.amount = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Observable<IssueEventResponse> issueEventObservable(EthFilter filter) {
        return web3j.ethLogObservable(filter).map(new Func1<Log, IssueEventResponse>() {
            @Override
            public IssueEventResponse call(Log log) {
                EventValuesWithLog eventValues = extractEventParametersWithLog(ISSUE_EVENT, log);
                IssueEventResponse typedResponse = new IssueEventResponse();
                typedResponse.log = log;
                typedResponse.amount = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
                return typedResponse;
            }
        });
    }

    public Observable<IssueEventResponse> issueEventObservable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(ISSUE_EVENT));
        return issueEventObservable(filter);
    }

    public List<RedeemEventResponse> getRedeemEvents(TransactionReceipt transactionReceipt) {
        List<EventValuesWithLog> valueList = extractEventParametersWithLog(REDEEM_EVENT, transactionReceipt);
        ArrayList<RedeemEventResponse> responses = new ArrayList<RedeemEventResponse>(valueList.size());
        for (EventValuesWithLog eventValues : valueList) {
            RedeemEventResponse typedResponse = new RedeemEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.amount = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Observable<RedeemEventResponse> redeemEventObservable(EthFilter filter) {
        return web3j.ethLogObservable(filter).map(new Func1<Log, RedeemEventResponse>() {
            @Override
            public RedeemEventResponse call(Log log) {
                EventValuesWithLog eventValues = extractEventParametersWithLog(REDEEM_EVENT, log);
                RedeemEventResponse typedResponse = new RedeemEventResponse();
                typedResponse.log = log;
                typedResponse.amount = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
                return typedResponse;
            }
        });
    }

    public Observable<RedeemEventResponse> redeemEventObservable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(REDEEM_EVENT));
        return redeemEventObservable(filter);
    }

    public List<ParamsEventResponse> getParamsEvents(TransactionReceipt transactionReceipt) {
        List<EventValuesWithLog> valueList = extractEventParametersWithLog(PARAMS_EVENT, transactionReceipt);
        ArrayList<ParamsEventResponse> responses = new ArrayList<ParamsEventResponse>(valueList.size());
        for (EventValuesWithLog eventValues : valueList) {
            ParamsEventResponse typedResponse = new ParamsEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.feeBasisPoints = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse.maxFee = (BigInteger) eventValues.getNonIndexedValues().get(1).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Observable<ParamsEventResponse> paramsEventObservable(EthFilter filter) {
        return web3j.ethLogObservable(filter).map(new Func1<Log, ParamsEventResponse>() {
            @Override
            public ParamsEventResponse call(Log log) {
                EventValuesWithLog eventValues = extractEventParametersWithLog(PARAMS_EVENT, log);
                ParamsEventResponse typedResponse = new ParamsEventResponse();
                typedResponse.log = log;
                typedResponse.feeBasisPoints = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
                typedResponse.maxFee = (BigInteger) eventValues.getNonIndexedValues().get(1).getValue();
                return typedResponse;
            }
        });
    }

    public Observable<ParamsEventResponse> paramsEventObservable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(PARAMS_EVENT));
        return paramsEventObservable(filter);
    }

    public List<LockEventResponse> getLockEvents(TransactionReceipt transactionReceipt) {
        List<EventValuesWithLog> valueList = extractEventParametersWithLog(LOCK_EVENT, transactionReceipt);
        ArrayList<LockEventResponse> responses = new ArrayList<LockEventResponse>(valueList.size());
        for (EventValuesWithLog eventValues : valueList) {
            LockEventResponse typedResponse = new LockEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.lockAddress = (String) eventValues.getNonIndexedValues().get(0).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Observable<LockEventResponse> lockEventObservable(EthFilter filter) {
        return web3j.ethLogObservable(filter).map(new Func1<Log, LockEventResponse>() {
            @Override
            public LockEventResponse call(Log log) {
                EventValuesWithLog eventValues = extractEventParametersWithLog(LOCK_EVENT, log);
                LockEventResponse typedResponse = new LockEventResponse();
                typedResponse.log = log;
                typedResponse.lockAddress = (String) eventValues.getNonIndexedValues().get(0).getValue();
                return typedResponse;
            }
        });
    }

    public Observable<LockEventResponse> lockEventObservable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(LOCK_EVENT));
        return lockEventObservable(filter);
    }

    public List<UnLockEventResponse> getUnLockEvents(TransactionReceipt transactionReceipt) {
        List<EventValuesWithLog> valueList = extractEventParametersWithLog(UNLOCK_EVENT, transactionReceipt);
        ArrayList<UnLockEventResponse> responses = new ArrayList<UnLockEventResponse>(valueList.size());
        for (EventValuesWithLog eventValues : valueList) {
            UnLockEventResponse typedResponse = new UnLockEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.unLockAddress = (String) eventValues.getNonIndexedValues().get(0).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Observable<UnLockEventResponse> unLockEventObservable(EthFilter filter) {
        return web3j.ethLogObservable(filter).map(new Func1<Log, UnLockEventResponse>() {
            @Override
            public UnLockEventResponse call(Log log) {
                EventValuesWithLog eventValues = extractEventParametersWithLog(UNLOCK_EVENT, log);
                UnLockEventResponse typedResponse = new UnLockEventResponse();
                typedResponse.log = log;
                typedResponse.unLockAddress = (String) eventValues.getNonIndexedValues().get(0).getValue();
                return typedResponse;
            }
        });
    }

    public Observable<UnLockEventResponse> unLockEventObservable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(UNLOCK_EVENT));
        return unLockEventObservable(filter);
    }

    public List<FrozenEventResponse> getFrozenEvents(TransactionReceipt transactionReceipt) {
        List<EventValuesWithLog> valueList = extractEventParametersWithLog(FROZEN_EVENT, transactionReceipt);
        ArrayList<FrozenEventResponse> responses = new ArrayList<FrozenEventResponse>(valueList.size());
        for (EventValuesWithLog eventValues : valueList) {
            FrozenEventResponse typedResponse = new FrozenEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse._user = (String) eventValues.getNonIndexedValues().get(0).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Observable<FrozenEventResponse> frozenEventObservable(EthFilter filter) {
        return web3j.ethLogObservable(filter).map(new Func1<Log, FrozenEventResponse>() {
            @Override
            public FrozenEventResponse call(Log log) {
                EventValuesWithLog eventValues = extractEventParametersWithLog(FROZEN_EVENT, log);
                FrozenEventResponse typedResponse = new FrozenEventResponse();
                typedResponse.log = log;
                typedResponse._user = (String) eventValues.getNonIndexedValues().get(0).getValue();
                return typedResponse;
            }
        });
    }

    public Observable<FrozenEventResponse> frozenEventObservable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(FROZEN_EVENT));
        return frozenEventObservable(filter);
    }

    public List<UnFrozenEventResponse> getUnFrozenEvents(TransactionReceipt transactionReceipt) {
        List<EventValuesWithLog> valueList = extractEventParametersWithLog(UNFROZEN_EVENT, transactionReceipt);
        ArrayList<UnFrozenEventResponse> responses = new ArrayList<UnFrozenEventResponse>(valueList.size());
        for (EventValuesWithLog eventValues : valueList) {
            UnFrozenEventResponse typedResponse = new UnFrozenEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse._user = (String) eventValues.getNonIndexedValues().get(0).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Observable<UnFrozenEventResponse> unFrozenEventObservable(EthFilter filter) {
        return web3j.ethLogObservable(filter).map(new Func1<Log, UnFrozenEventResponse>() {
            @Override
            public UnFrozenEventResponse call(Log log) {
                EventValuesWithLog eventValues = extractEventParametersWithLog(UNFROZEN_EVENT, log);
                UnFrozenEventResponse typedResponse = new UnFrozenEventResponse();
                typedResponse.log = log;
                typedResponse._user = (String) eventValues.getNonIndexedValues().get(0).getValue();
                return typedResponse;
            }
        });
    }

    public Observable<UnFrozenEventResponse> unFrozenEventObservable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(UNFROZEN_EVENT));
        return unFrozenEventObservable(filter);
    }

    public List<DestroyedBlackFundsEventResponse> getDestroyedBlackFundsEvents(TransactionReceipt transactionReceipt) {
        List<EventValuesWithLog> valueList = extractEventParametersWithLog(DESTROYEDBLACKFUNDS_EVENT, transactionReceipt);
        ArrayList<DestroyedBlackFundsEventResponse> responses = new ArrayList<DestroyedBlackFundsEventResponse>(valueList.size());
        for (EventValuesWithLog eventValues : valueList) {
            DestroyedBlackFundsEventResponse typedResponse = new DestroyedBlackFundsEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse._blackListedUser = (String) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse._balance = (BigInteger) eventValues.getNonIndexedValues().get(1).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Observable<DestroyedBlackFundsEventResponse> destroyedBlackFundsEventObservable(EthFilter filter) {
        return web3j.ethLogObservable(filter).map(new Func1<Log, DestroyedBlackFundsEventResponse>() {
            @Override
            public DestroyedBlackFundsEventResponse call(Log log) {
                EventValuesWithLog eventValues = extractEventParametersWithLog(DESTROYEDBLACKFUNDS_EVENT, log);
                DestroyedBlackFundsEventResponse typedResponse = new DestroyedBlackFundsEventResponse();
                typedResponse.log = log;
                typedResponse._blackListedUser = (String) eventValues.getNonIndexedValues().get(0).getValue();
                typedResponse._balance = (BigInteger) eventValues.getNonIndexedValues().get(1).getValue();
                return typedResponse;
            }
        });
    }

    public Observable<DestroyedBlackFundsEventResponse> destroyedBlackFundsEventObservable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(DESTROYEDBLACKFUNDS_EVENT));
        return destroyedBlackFundsEventObservable(filter);
    }

    public List<AddedBlackListEventResponse> getAddedBlackListEvents(TransactionReceipt transactionReceipt) {
        List<EventValuesWithLog> valueList = extractEventParametersWithLog(ADDEDBLACKLIST_EVENT, transactionReceipt);
        ArrayList<AddedBlackListEventResponse> responses = new ArrayList<AddedBlackListEventResponse>(valueList.size());
        for (EventValuesWithLog eventValues : valueList) {
            AddedBlackListEventResponse typedResponse = new AddedBlackListEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse._user = (String) eventValues.getNonIndexedValues().get(0).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Observable<AddedBlackListEventResponse> addedBlackListEventObservable(EthFilter filter) {
        return web3j.ethLogObservable(filter).map(new Func1<Log, AddedBlackListEventResponse>() {
            @Override
            public AddedBlackListEventResponse call(Log log) {
                EventValuesWithLog eventValues = extractEventParametersWithLog(ADDEDBLACKLIST_EVENT, log);
                AddedBlackListEventResponse typedResponse = new AddedBlackListEventResponse();
                typedResponse.log = log;
                typedResponse._user = (String) eventValues.getNonIndexedValues().get(0).getValue();
                return typedResponse;
            }
        });
    }

    public Observable<AddedBlackListEventResponse> addedBlackListEventObservable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(ADDEDBLACKLIST_EVENT));
        return addedBlackListEventObservable(filter);
    }

    public List<RemovedBlackListEventResponse> getRemovedBlackListEvents(TransactionReceipt transactionReceipt) {
        List<EventValuesWithLog> valueList = extractEventParametersWithLog(REMOVEDBLACKLIST_EVENT, transactionReceipt);
        ArrayList<RemovedBlackListEventResponse> responses = new ArrayList<RemovedBlackListEventResponse>(valueList.size());
        for (EventValuesWithLog eventValues : valueList) {
            RemovedBlackListEventResponse typedResponse = new RemovedBlackListEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse._user = (String) eventValues.getNonIndexedValues().get(0).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Observable<RemovedBlackListEventResponse> removedBlackListEventObservable(EthFilter filter) {
        return web3j.ethLogObservable(filter).map(new Func1<Log, RemovedBlackListEventResponse>() {
            @Override
            public RemovedBlackListEventResponse call(Log log) {
                EventValuesWithLog eventValues = extractEventParametersWithLog(REMOVEDBLACKLIST_EVENT, log);
                RemovedBlackListEventResponse typedResponse = new RemovedBlackListEventResponse();
                typedResponse.log = log;
                typedResponse._user = (String) eventValues.getNonIndexedValues().get(0).getValue();
                return typedResponse;
            }
        });
    }

    public Observable<RemovedBlackListEventResponse> removedBlackListEventObservable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(REMOVEDBLACKLIST_EVENT));
        return removedBlackListEventObservable(filter);
    }

    public List<ApprovalEventResponse> getApprovalEvents(TransactionReceipt transactionReceipt) {
        List<EventValuesWithLog> valueList = extractEventParametersWithLog(APPROVAL_EVENT, transactionReceipt);
        ArrayList<ApprovalEventResponse> responses = new ArrayList<ApprovalEventResponse>(valueList.size());
        for (EventValuesWithLog eventValues : valueList) {
            ApprovalEventResponse typedResponse = new ApprovalEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.owner = (String) eventValues.getIndexedValues().get(0).getValue();
            typedResponse.spender = (String) eventValues.getIndexedValues().get(1).getValue();
            typedResponse.value = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Observable<ApprovalEventResponse> approvalEventObservable(EthFilter filter) {
        return web3j.ethLogObservable(filter).map(new Func1<Log, ApprovalEventResponse>() {
            @Override
            public ApprovalEventResponse call(Log log) {
                EventValuesWithLog eventValues = extractEventParametersWithLog(APPROVAL_EVENT, log);
                ApprovalEventResponse typedResponse = new ApprovalEventResponse();
                typedResponse.log = log;
                typedResponse.owner = (String) eventValues.getIndexedValues().get(0).getValue();
                typedResponse.spender = (String) eventValues.getIndexedValues().get(1).getValue();
                typedResponse.value = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
                return typedResponse;
            }
        });
    }

    public Observable<ApprovalEventResponse> approvalEventObservable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(APPROVAL_EVENT));
        return approvalEventObservable(filter);
    }

    public List<TransferEventResponse> getTransferEvents(TransactionReceipt transactionReceipt) {
        List<EventValuesWithLog> valueList = extractEventParametersWithLog(TRANSFER_EVENT, transactionReceipt);
        ArrayList<TransferEventResponse> responses = new ArrayList<TransferEventResponse>(valueList.size());
        for (EventValuesWithLog eventValues : valueList) {
            TransferEventResponse typedResponse = new TransferEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.from = (String) eventValues.getIndexedValues().get(0).getValue();
            typedResponse.to = (String) eventValues.getIndexedValues().get(1).getValue();
            typedResponse.value = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Observable<TransferEventResponse> transferEventObservable(EthFilter filter) {
        return web3j.ethLogObservable(filter).map(new Func1<Log, TransferEventResponse>() {
            @Override
            public TransferEventResponse call(Log log) {
                EventValuesWithLog eventValues = extractEventParametersWithLog(TRANSFER_EVENT, log);
                TransferEventResponse typedResponse = new TransferEventResponse();
                typedResponse.log = log;
                typedResponse.from = (String) eventValues.getIndexedValues().get(0).getValue();
                typedResponse.to = (String) eventValues.getIndexedValues().get(1).getValue();
                typedResponse.value = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
                return typedResponse;
            }
        });
    }

    public Observable<TransferEventResponse> transferEventObservable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(TRANSFER_EVENT));
        return transferEventObservable(filter);
    }

    public List<PauseEventResponse> getPauseEvents(TransactionReceipt transactionReceipt) {
        List<EventValuesWithLog> valueList = extractEventParametersWithLog(PAUSE_EVENT, transactionReceipt);
        ArrayList<PauseEventResponse> responses = new ArrayList<PauseEventResponse>(valueList.size());
        for (EventValuesWithLog eventValues : valueList) {
            PauseEventResponse typedResponse = new PauseEventResponse();
            typedResponse.log = eventValues.getLog();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Observable<PauseEventResponse> pauseEventObservable(EthFilter filter) {
        return web3j.ethLogObservable(filter).map(new Func1<Log, PauseEventResponse>() {
            @Override
            public PauseEventResponse call(Log log) {
                EventValuesWithLog eventValues = extractEventParametersWithLog(PAUSE_EVENT, log);
                PauseEventResponse typedResponse = new PauseEventResponse();
                typedResponse.log = log;
                return typedResponse;
            }
        });
    }

    public Observable<PauseEventResponse> pauseEventObservable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(PAUSE_EVENT));
        return pauseEventObservable(filter);
    }

    public List<UnpauseEventResponse> getUnpauseEvents(TransactionReceipt transactionReceipt) {
        List<EventValuesWithLog> valueList = extractEventParametersWithLog(UNPAUSE_EVENT, transactionReceipt);
        ArrayList<UnpauseEventResponse> responses = new ArrayList<UnpauseEventResponse>(valueList.size());
        for (EventValuesWithLog eventValues : valueList) {
            UnpauseEventResponse typedResponse = new UnpauseEventResponse();
            typedResponse.log = eventValues.getLog();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Observable<UnpauseEventResponse> unpauseEventObservable(EthFilter filter) {
        return web3j.ethLogObservable(filter).map(new Func1<Log, UnpauseEventResponse>() {
            @Override
            public UnpauseEventResponse call(Log log) {
                EventValuesWithLog eventValues = extractEventParametersWithLog(UNPAUSE_EVENT, log);
                UnpauseEventResponse typedResponse = new UnpauseEventResponse();
                typedResponse.log = log;
                return typedResponse;
            }
        });
    }

    public Observable<UnpauseEventResponse> unpauseEventObservable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(UNPAUSE_EVENT));
        return unpauseEventObservable(filter);
    }

    public static CloudTreeToken load(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return new CloudTreeToken(contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    public static CloudTreeToken load(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return new CloudTreeToken(contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public static class IssueEventResponse {
        public Log log;

        public BigInteger amount;
    }

    public static class RedeemEventResponse {
        public Log log;

        public BigInteger amount;
    }

    public static class ParamsEventResponse {
        public Log log;

        public BigInteger feeBasisPoints;

        public BigInteger maxFee;
    }

    public static class LockEventResponse {
        public Log log;

        public String lockAddress;
    }

    public static class UnLockEventResponse {
        public Log log;

        public String unLockAddress;
    }

    public static class FrozenEventResponse {
        public Log log;

        public String _user;
    }

    public static class UnFrozenEventResponse {
        public Log log;

        public String _user;
    }

    public static class DestroyedBlackFundsEventResponse {
        public Log log;

        public String _blackListedUser;

        public BigInteger _balance;
    }

    public static class AddedBlackListEventResponse {
        public Log log;

        public String _user;
    }

    public static class RemovedBlackListEventResponse {
        public Log log;

        public String _user;
    }

    public static class ApprovalEventResponse {
        public Log log;

        public String owner;

        public String spender;

        public BigInteger value;
    }

    public static class TransferEventResponse {
        public Log log;

        public String from;

        public String to;

        public BigInteger value;
    }

    public static class PauseEventResponse {
        public Log log;
    }

    public static class UnpauseEventResponse {
        public Log log;
    }
}
