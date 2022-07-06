const fs=require('fs')

const Web3=require('web3')
const provider=new Web3.providers.WebsocketProvider('ws://127.0.0.1:8345')
const web3=new Web3(provider)

const deployInfo=require('./MainDeploy.json')
const contractNames=Object.keys(deployInfo.contracts)
const contractName=contractNames[0]
const abi=deployInfo.contracts[contractName].abi
const bin=deployInfo.contracts[contractName].bin

const contractInfo=require('./MainContract.json')
const contractAddress=contractInfo.contractAddress

const deploy=new web3.eth.Contract(abi).deploy({
    data:'0x'+bin
})
const contract=new web3.eth.Contract(abi,contractAddress)

const getRandArray=async ()=>{
    const nounce=Math.floor(Math.random()*5000)
    return await contract.methods.makeRandArray(nounce).call();
}

const makeRandIdxArray=async ()=>{
    let idxArray=[];
    idxArray.push(await getRandArray())
    idxArray.push(await getRandArray())
    idxArray.push(await getRandArray())
    idxArray.push(await getRandArray())
    return idxArray
}

makeRandIdxArray()