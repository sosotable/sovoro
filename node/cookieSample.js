const bodyParser = require("body-parser");
const fs=require('node:fs/promises')

// 웹서버 모듈 nodejs express사용
const express = require('express')
const app = express()
const server = require('http').createServer(app);

// 웹서버 포트: *
const port = *
// sql연결 객체 생성
const mysql = require('mysql2/promise');
const mysql_dbc = require('./db_connection')();
let connection=null;

const getRandWordId=async (range)=>{
    return Math.floor(Math.random()*range)+1
}

const getDataLength=async ()=>{
    const v=await connection.query('select count(*) as cnt from wordinfo')
    return v[0][0].cnt
}

const getWordJson=async ()=>{
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

const getDayCookie=async ()=>{
    let year = new Date().getFullYear(); // 년도
    let month = new Date().getMonth() + 1;  // 월
    let date = new Date().getDate(); // 일
    return String(year)+String(month)+String(date);
}

app.get('/',async (req, res) => {
    let dayCookie = await getDayCookie()
    let wordJsonObjects = new Object()

    const v=await connection.query(`select * from dailywords where daycookie='${dayCookie}'`);
    if(v[0].length==0) {
        wordJsonObjects.MainWord=await getWordJson()
        wordJsonObjects.TestWord1=await getWordJson()
        wordJsonObjects.TestWord2=await getWordJson()
        wordJsonObjects.TestWord3=await getWordJson()
        let query='insert into dailywords values(?,?,?,?,?)'
        await connection.query(query,[
            dayCookie,
            JSON.stringify(wordJsonObjects.MainWord),
            JSON.stringify(wordJsonObjects.TestWord1),
            JSON.stringify(wordJsonObjects.TestWord2),
            JSON.stringify(wordJsonObjects.TestWord3)
        ])
    } else {
        let mainWord=await connection.query(`select mainword from dailywords where daycookie='${dayCookie}';`)
        let testWord1=await connection.query(`select testword1 from dailywords where daycookie='${dayCookie}';`)
        let testWord2=await connection.query(`select testword2 from dailywords where daycookie='${dayCookie}';`)
        let testWord3=await connection.query(`select testword3 from dailywords where daycookie='${dayCookie}';`)
        wordJsonObjects.MainWord=JSON.parse(mainWord[0][0].mainword)
        wordJsonObjects.TestWord1=JSON.parse(testWord1[0][0].testword1)
        wordJsonObjects.TestWord2=JSON.parse(testWord2[0][0].testword2)
        wordJsonObjects.TestWord3=JSON.parse(testWord3[0][0].testword3)
    }
    res.send(wordJsonObjects)
})

server.listen(port, async ()=>{
    connection = await mysql_dbc.init();
    console.log(`application is listening on port ${port}...`)
})