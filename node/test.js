// sql연결 객체 생성
//const mysql_dbc = require('./db_connection')();
//const connection = mysql_dbc.init();

// mysql2 모듈을 사용해야 비동기 처리가 가능하다
const mysql = require('mysql2/promise');
const mysql_dbc = require('./db_connection')();
let connection=null;

const dbInit=async ()=>{
    if(connection==null)
        connection = await mysql_dbc.init();
}

const getRandWordId=async (range)=>{
    return Math.floor(Math.random()*range)+1
}

const getDataLength=async ()=>{
    await dbInit()
    const v=await connection.query('select count(*) as cnt from wordinfo')
    return v[0][0].cnt
}

const getWordJson=async ()=>{
    await dbInit()
    let wordJson=new Object()
    let v,engword,korword;
    const length=await getDataLength()
    for(let i=0;i<10;i++) {
        const value=await getRandWordId(length)
        let query = `select * from wordinfo where wordid = '${value}';`
        v=await connection.query(query)
        engword=v[0][0].engword
        korword=v[0][0].korword
        wordJson[engword]=korword
    }
    return wordJson
}
const fun1=async ()=>{
    let wordJsonObjects=new Object()
    wordJsonObjects.MainWord=await getWordJson()
    wordJsonObjects.TestWord1=await getWordJson()
    wordJsonObjects.TestWord2=await getWordJson()
    wordJsonObjects.TestWord3=await getWordJson()
    return wordJsonObjects
    connection.end()
}

const fun2=async ()=>{
    await dbInit()
    let query = `select count(*) as cnt from userinfo where userid='test1'and password='test1'`
    let query1 = `select count(*) as cnt from userinfo where userid='test5'and password='test5'`
    const v=await connection.query(query1)
    console.log(v[0][0].cnt)
}

fun2()