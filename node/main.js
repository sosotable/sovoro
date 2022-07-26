const bodyParser = require("body-parser");
const fs=require('fs')

// 웹서버 모듈 nodejs express사용
const express = require('express')
const app = express()
const server = require('http').createServer(app);

// 소켓
const io = require('socket.io')(server);

// 웹서버 포트: 3000
const port = 3000

// 이더리움 네트워크 연결
const Web3=require('web3');
const web3=new Web3(new Web3.providers.HttpProvider('http://13.58.48.132:8445'));

// sql연결 객체 생성
const mysql = require('mysql2/promise');
const mysql_dbc = require('./db_connection')();
let connection=null;

// 메일 모듈
const nodeoutlook = require('nodejs-nodemailer-outlook')
let second=0;
let interval

function timerCallback() {
    second++
    if(second<=180) {
        io.emit('timer start',second)
    } else {
        second=0;
        io.emit('timer stop')
        clearTimeout(interval)
    }
}

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

// app.use(bodyParser.json());//json타입
// app.use(bodyParser.urlencoded({extended:true}));//post방식의 encoding

// post를 받기 위한 미들웨어 등록
app.use(express.json());
// post받으면서 인코딩 안 깨지도록 하기 위함
app.use(express.urlencoded({ extended: true }));
/** 이 윗 부분은 기본 설정-어려우면 대충 그렇구나 하고 넘어가먄 된다- **/

io.sockets.on('connection', async (socket) => {
    console.log(`Socket connected : ${socket.id}`)
    socket.on('disconnect', () => {
        console.log(`Socket disconnected : ${socket.id}`)
        second=0
        clearTimeout(interval)
    })
    socket.on('new message', (msg) => {
        console.log('message: ' + msg);
    });
    socket.on('id check', async (msg)=>{
        const query=`select * from userinfo where userid='${msg}'`
        let returnObj=new Object()
        returnObj.returnValue=false
        try {
            const v = await connection.query(query)
            if(v[0].length!=0)
                returnObj.returnValue=true
        } catch (e) {
            console.log(e)
        } finally {
            console.log(returnObj)
            io.emit('id check',returnObj)
        }
    })
    socket.on('nickname check',async (msg)=>{
        const query=`select * from userinfo where nickname='${msg}'`
        let returnObj=new Object()
        returnObj.returnValue=false
        try {
            const v = await connection.query(query)
            if(v[0].length!=0)
                returnObj.returnValue=true
        } catch (e) {
            console.log(e)
        } finally {
            console.log(returnObj)
            io.emit('nickname check',returnObj)
        }
        console.log(msg)
    })
    socket.on('mail check', async (msg)=>{
        const randNum=Math.floor(Math.random() * (999999 - 100000) + 100000)
        let returnObj=new Object()
        returnObj.mailAuth=String(randNum)
        await nodeoutlook.sendEmail({
                auth: {
                    "user": "ssossotable@outlook.com",
                    "pass": "kiter7968!"
                },
                from: "ssossotable@outlook.com",
                to: msg,
                subject: '소소식탁 소보로 인증 번호입니다',
                text: "인증 번호를 입력해 주세요: "+String(randNum),
                replyTo: 'ssossotable@outlook.com',
                onError: (e) => console.log(e),
                onSuccess: (i) => console.log(i)
            }
        )
        io.emit('mail check',returnObj)
    })
    socket.on('timer start', ()=>{
        interval=setInterval(timerCallback, 1000);
    })
    socket.on('add comment', async (msg)=>{
        let query='insert into commentinfo(userid,nickname,commentcontent) values(?,?,?)'
        try
        {
            await connection.query(query,[msg.userid,msg.nickname,msg.commentcontent])
        }
        catch (e)
        {
            console.log(e)
        }
    })
    socket.on('read message', async ()=>{
        let query='select * from commentinfo limit 50'
        let returnObj=new Object()
        try
        {
            const v=await connection.query(query)
            const infos=v[0]
            infos.forEach((element,index,array)=>{
                let commentNumber=element.commentnumber
                returnObj[commentNumber]=new Object()
                returnObj[commentNumber].userid=element.userid
                returnObj[commentNumber].commentcontent=element.commentcontent
                returnObj[commentNumber].commentlikes=element.commentlikes
                returnObj[commentNumber].nickname=element.nickname
            })
            socket.emit('read message',returnObj)
        }
        catch (e)
        {
            console.log(e)
        }
    })
    socket.on('add likes',async (msg)=>{
        let execute;
        const getCommentLikes=`select * from commentinfo where commentnumber=${msg}`
        execute=await connection.query(getCommentLikes)
        const commentLikesCount=execute[0][0].commentlikes+1
        const updateCommentLikes= `
                UPDATE commentinfo
                SET
                    commentlikes=${commentLikesCount}
                WHERE
                    commentnumber=${msg};`
        execute=await connection.query(updateCommentLikes)
    })
})

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
    console.log(req.body)
    try {
        let query = `select nickname as res from userinfo where userid=? and password=?`
        const v=await connection.query(query,[req.body.userid,req.body.password])
        // console.log(v[0])
        if(v[0].length==0) {
            returnObj.returnValue = false
        }
        else
        {
            returnObj.returnValue=true
            returnObj.userNickname=v[0][0].res
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
        console.log('one')
        returnObj.success=false
    }
    finally {
        res.send(returnObj)
    }

})

// 회원가입 페이지에 대한 리퀘스트 처리
app.post('/signup',async (req,res)=>{
    const userid=req.body.userId;
    const password=req.body.password;
    const nickname=req.body.userNickname;

    let returnObj=new Object()
    returnObj['success']=false;
    let query = `insert into userinfo(userid,password,nickname) values('${userid}','${password}',${nickname});`
    try {
        const v = await connection.query(query)
        returnObj['success']=true;
    } catch (e) {
        console.log(e)
    } finally {
        console.log(req.body)
        console.log(returnObj)
        res.send(returnObj)
    }

})

// 영단어 10개가 제공되는 메인화면
app.get('/main',async (req,res)=>{
})

// 서버 실행
server.listen(port, async ()=>{
    connection = await mysql_dbc.init();
    console.log(`application is listening on port ${port}...`)
})