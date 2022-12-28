const fs=require('fs')

const Web3=require('web3')
const provider=new Web3.providers.WebsocketProvider('ws://127.0.0.1:8345')
const web3=new Web3(provider)

const deployInfo=require('./MainDeploy.json')
const contractNames=Object.keys(deployInfo.contracts)
const contractName=contractNames[0]
const abi=deployInfo.contracts[contractName].abi
const bin=deployInfo.contracts[contractName].bin

const deploy=new web3.eth.Contract(abi).deploy({
    data:'0x'+bin
})

const send=async ()=>{
    const accounts=await web3.eth.getAccounts()
    const esGas=await deploy.estimateGas()

    deploy.send({
        from:accounts[0],
        gas:esGas
    }
    ,(err, transactionHash)=>{
        if(err) throw err
        })
        .on('receipt',receipt => {
            fs.writeFile('./MainContract.json',JSON.stringify(receipt),'utf-8',err => {
                if(err) throw err
                else process.exit(1)
            })
        })
}

send()