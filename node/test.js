// sql연결 객체 생성
//const mysql_dbc = require('./db_connection')();
//const connection = mysql_dbc.init();
// mysql2 모듈을 사용해야 비동기 처리가 가능하다
const mysql = require('mysql2/promise');

const getRandWordId=async (range)=>{
    return Math.floor(Math.random()*range)+1
}

const getDataLength=async ()=>{
    const connection = await mysql.createConnection({
        host: '13.58.48.132',
        port: '3306',
        user: 'DWORD',
        password: 'ssp',
        database: 'DWORD'
    });
    const v=await connection.query('select * from userinfo')
    console.log(v)
    // let query = `select count(*) as cnt from wordinfo`;
    // let length=0
    // const v=await (await connection).query('select * from userinfo')
    // console.log(v.values)
    // connection.end()
}

const getWordArray=async ()=>{
    let wordArray=new Object()
    for(let i=0;i<10;i++) {
        const value=await getRandWordId(2000)
        let query = `select * from wordinfo where wordid = '${value}';`
        connection.query(query, (err, result)=>{
            if(err) throw err
            else {
                const key=Object.keys(result)[0]
                wordArray.result[key].engword=result[key].korword
            }
        })
    }
    return wordArray
}

getDataLength()