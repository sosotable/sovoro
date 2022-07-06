_dir=$SOVORO/node/Ethereum/eth
_log=$SOVORO/node/Ethereum/eth/my.log
geth --identity "SoVoRoNode" \
--allow-insecure-unlock \
--http --http.addr "127.0.0.1" --http.port "8445" --http.corsdomain "*" \
--datadir $_dir \
--port "38445" \
--http.api "admin,eth,debug,miner,net,txpool,personal,web3" \
--networkid 33