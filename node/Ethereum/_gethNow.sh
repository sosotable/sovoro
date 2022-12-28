_dir=$SOVORO/node/Ethereum/eth
_log=$SOVORO/node/Ethereum/eth/my.log
geth --identity "*" \
--allow-insecure-unlock \
--http --http.addr "*" --http.port "*" --http.corsdomain "*" \
--datadir $_dir \
--port "*" \
--http.api "*" \
--networkid 33