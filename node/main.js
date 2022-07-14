const fs=require('fs')

// 웹서버 모듈 nodejs express사용
const express = require('express')
const app = express()

// 웹서버 포트: 3000
const port = 3000

// 이더리움 네트워크 연결
const Web3=require('web3');
const web3=new Web3(new Web3.providers.HttpProvider('http://13.58.48.132:8445'));

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

// 쿠키 저장 모듈
const cookieParser = require('cookie-parser');

const cookieConfig={
    // 쿠키 만료일은 현재시각+하루
    expires: new Date(Date.now() + 86400000),
    // 웹 서버에서만 접근 가능
    httpOnly: true,
    // 쿠키 경로
    path: '/'
}

// 쿠키 모듈 미들웨어 등록
app.use(cookieParser());

// post를 받기 위한 미들웨어 등록
app.use(express.json());
// post받으면서 인코딩 안 깨지도록 하기 위함
app.use(express.urlencoded({ extended: true }));
/** 이 윗 부분은 기본 설정-어려우면 대충 그렇구나 하고 넘어가먄 된다- **/

app.get('/',async (req,res)=>{
    res.send(`server is correctly listening on port ${port}`)
})

/**라우팅**/
app.get('/loading',async (req,res)=>{
    let dayCookie=await getDayCookie()
    let wordJsonObjects=new Object()
    if (req.cookies.dayCookie) {
        if(req.cookies.dayCookie!=dayCookie)
        {
            await res.clearCookie('dayCookie',{path:'/loading'})
            res.cookie('dayCookie', dayCookie, {
                // 쿠키 만료일은 현재시각+하루
                expires: new Date(Date.now() + 86400000),
                // 웹 서버에서만 접근 가능
                httpOnly: true,
                // 쿠키 경로
                path: '/loading'
            })
            wordJsonObjects.MainWord=await getWordJson()
            wordJsonObjects.TestWord1=await getWordJson()
            wordJsonObjects.TestWord2=await getWordJson()
            wordJsonObjects.TestWord3=await getWordJson()
            await fs.writeFile('./dailyWords.json',JSON.stringify(wordJsonObjects),'utf-8',err => {
                if(err) throw err
            })
            res.send(wordJsonObjects)
        }
        else
        {
            res.sendFile(__dirname+'/dailyWords.json')
        }
    }
    /**dayCookie가 없다면 오늘 날짜로 등록**/
    else
    {
        res.cookie('dayCookie',dayCookie,{
            // 쿠키 만료일은 현재시각+하루
            expires: new Date(Date.now() + 86400000),
            // 웹 서버에서만 접근 가능
            httpOnly: true,
            // 쿠키 경로
            path: '/loading'
        })
        wordJsonObjects.MainWord=await getWordJson()
        wordJsonObjects.TestWord1=await getWordJson()
        wordJsonObjects.TestWord2=await getWordJson()
        wordJsonObjects.TestWord3=await getWordJson()
        await fs.writeFile('./dailyWords.json',JSON.stringify(wordJsonObjects),'utf-8',err => {
            if(err) throw err
        })
        res.send(wordJsonObjects)
    }

})

app.post('/signin', async (req,res)=> {
    const cookieConfig={
        // 쿠키 만료일은 현재시각+하루
        expires: new Date(Date.now() + 3600000),
        // 웹 서버에서만 접근 가능
        httpOnly: true,
        // 쿠키 경로
        path: '/signin'
    }

    let returnObj=new Object()
    let userInfo=new Object()
    const userid=req.body.userid;
    const password=req.body.password;

    try {
        let query = `select nickname as res from userinfo where userid=? and password=?`
        const v=await connection.query(query,[req.body.userid,req.body.password])
        if(v[0].length==0)
            returnObj.returnValue='fail'
        else
        {
            returnObj.returnValue='success'
            returnObj.returnValue=v[0][0].nickname
            if(!req.cookies.userInfo) {
                userInfo.userid=req.body.userid
                userInfo.password=req.body.password
                userInfo.nickname=v[0][0].nickname
                res.cookie('userInfo', userInfo, cookieConfig);
            }
        }
    }
    catch (e)
    {
        returnObj.success='fail'
    }
    finally {
        res.send(returnObj)
    }

})

// 회원가입 페이지에 대한 리퀘스트 처리
app.post('/signup',async (req,res)=>{
    const userid=req.body.userid;
    const password=req.body.password;
    const nickname=req.body.nickname;
    let query = `insert into userinfo(userid,password,nickname) values('${userid}','${password}',${nickname});`
    const v=await connection.query(query)
        .on('error',err => {
            if(err)
                returnObj.success='success'
            else
                returnObj.success='fail'
        })
    res.send(returnObj)
})

// 영단어 10개가 제공되는 메인화면
app.get('/main',async (req,res)=>{
})

// 서버 실행
app.listen(port, async ()=>{
    connection = await mysql_dbc.init();
    console.log(`application is listening on port ${port}...`)
})