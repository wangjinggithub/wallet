pragma solidity ^0.4.24;
contract Coin {
//关键字“public”使变量能从合约外部访问。
    address public minter;
    mapping (address => uint) public balances;

//事件让轻客户端能高效的对变化做出反应。
   // event Sent(address from, address to, uint amount);

//这个构造函数的代码仅仅只在合约创建的时候被运行。
    constructor() {
        minter = msg.sender;
    }
    function mint(address receiver, uint amount) {
        //if (msg.sender != minter) return;
        balances[receiver] += amount;
    }
    function send(address sender,address receiver, uint amount) {
        if (balances[sender] < amount) return;
        balances[sender] -= amount;
        balances[receiver] += amount;
        //Sent(msg.sender, receiver, amount);
    }
}