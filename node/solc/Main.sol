// SPDX-License-Identifier: UNLICENSED
pragma solidity ^0.8.0;

contract Main {
    address owner;
    address receiver;
    uint ownerBalance;
    uint contractBalance;
    event ReceiveEvent(address _address,uint _value,string _string);
    event FallbackEvent(address _address,uint _value,string _string);
    receive() external payable {
        emit ReceiveEvent(msg.sender,msg.value,"receive event called");
    }
    fallback() external payable {
        emit FallbackEvent(msg.sender,msg.value,"fallback event called");
    }
    constructor() {
        owner=msg.sender;
    }
    function deposit(uint value) public payable isCorrectValue(value) {
        contractBalance+=value;
    }
    function withdraw(address _receiver,uint value) public payable {
        payable(_receiver).transfer(value);
        contractBalance-=value;
    }
    function getContractbalance() public view returns(uint,uint) {
        return (address(this).balance,contractBalance);
    }
    function getAddressBalance(address _address) public view returns(uint) {
        return _address.balance;
    }
    function makeRandNumFromOneToN(uint n,uint nounce_1,uint nounce_2) internal view returns(uint) {
        return uint(keccak256(abi.encodePacked(block.timestamp+nounce_1+nounce_2,block.difficulty)))%n+1;
    }
    function makeRandArray(uint nounce) external view returns(uint[] memory) {
        uint[] memory randArray=new uint[](10);
        for(uint i=0;i<10;i++) {
            randArray[i]=makeRandNumFromOneToN(4500,i,nounce);
        }
        return randArray;
    }
    modifier isCorrectValue(uint value) {
        require(value==msg.value,"incorrect value");
        _;
    }
    modifier isOwner() {
        require(msg.sender==owner,"message sender is not owner");
        _;
    }
}
