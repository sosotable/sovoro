const bodyParser = require("body-parser");
const fs=require('node:fs/promises')

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
const web3=new Web3(new Web3.providers.HttpProvider('*'));

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

app.use("/userImages", express.static(__dirname + '/userImages'));

const engPattern = /[a-zA-Z]/; //영어
const korPattern = /[ㄱ-ㅎ|ㅏ-ㅣ|가-힣]/; //한글

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
            io.emit('nickname check',returnObj)
        }
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
        let query1='insert into commentinfo(userid,nickname,commentcontent) values(?,?,?)'
        let query2='select * from commentinfo order by commentid desc limit 1'
        let returnObj=new Object()
        try
        {
            await connection.query(query1,[msg.userid,msg.nickname,msg.commentcontent])
            const v=await connection.query(query2)
            const infos=v[0]
            for(let i=0;i<infos.length;i++){
                const k=await connection.query(`select userimage from userinfo where userid='${infos[i].userid}'`)
                let commentId=infos[i].commentid
                returnObj[commentId]=new Object()
                returnObj[commentId].userid=infos[i].userid
                returnObj[commentId].userimage=k[0][0].userimage
                returnObj[commentId].commentcontent=infos[i].commentcontent
                returnObj[commentId].commentlikes=infos[i].commentlikes
                returnObj[commentId].nickname=infos[i].nickname
            }
        }
        catch (e)
        {
            console.log(e)
        }
        finally {
            socket.emit('read message',returnObj)
        }
    })
    socket.on('read message', async ()=>{
        let query='select * from commentinfo limit 50'
        let returnObj=new Object()
        try
        {
            const v=await connection.query(query)
            const infos=v[0]
            for(let i=0;i<infos.length;i++){
                const k=await connection.query(`select userimage from userinfo where userid='${infos[i].userid}'`)
                let commentId=infos[i].commentid
                returnObj[commentId]=new Object()
                returnObj[commentId].userid=infos[i].userid
                returnObj[commentId].userimage=k[0][0].userimage
                returnObj[commentId].commentcontent=infos[i].commentcontent
                returnObj[commentId].commentlikes=infos[i].commentlikes
                returnObj[commentId].nickname=infos[i].nickname
            }
        }
        catch (e)
        {
            console.log(e)
        }
        finally {
            socket.emit('read message',returnObj)
        }
    })

    socket.on('add likes',async (msg)=>{
        let returnObj={}
        returnObj.position=msg.position
        try {
            console.log(msg)
            const query1=`
        UPDATE commentinfo
        SET commentlikes = commentlikes+1
        WHERE commentid = ${msg.commentId};
        `
            const query2=`
        insert into commentcookie(commentid,commentowner,commenteduser)
        values(${msg.commentId},'${msg.commentOwner}','${msg.commentedUser}');
        `
            const query3=`
            select * from commentcookie
            where 
            commentid=${msg.commentId} and
            commentowner='${msg.commentOwner}' and
            commenteduser='${msg.commentedUser}'
            `
            const query4=`
            insert into commentnotification(commentid,commentowner,commenteduser)
        values(${msg.commentId},'${msg.commentOwner}','${msg.commentedUser}');
            `
            const v=await connection.query(query3)
            if(v[0].length==0) {
                await connection.query(query1)
                await connection.query(query2)
                await connection.query(query4)
                returnObj.click=false
            } else {
                returnObj.click=true
            }
        } catch (e) {
            console.log(e)
        }
        finally {
            console.log(returnObj)
            io.emit('add likes',returnObj)
        }

    })
    socket.on('upload image', async (msg) => {
        console.log(msg)
        let query= `
            UPDATE userinfo
            SET userimage=?
            WHERE userid=?`
        await connection.query(query
            ,[`*/${msg.userid}.png`,`${msg.userid}`])
        await fs.writeFile(`./userImages/${msg.userid}.png`,msg.userImage,'utf-8')
    });
    socket.on('word search', async msg=>{
        if(engPattern.test(msg)) {
            const query=`select * from wordinfo where engword like '%${msg}%'`
            const r=await connection.query(query);
            const v=r[0]
            io.emit('search result', v)
        } else if(korPattern.test(msg)) {
            const query=`select * from wordinfo where korword like '%${msg}%'`
            const r=await connection.query(query);
            const v=r[0]
            io.emit('search result', v)
        }
    })
    socket.on('read notification',async msg=>{
        let returnObj={}
        const query=`
        select * from commentnotification
        where commenteduser='${msg.userId}'
        `
        const v=await connection.query(query)
        for(let i=0;i<v[0].length;i++) {
            if(v[0][i].watched==0) {
                let id=v[0][i].commentid
                returnObj[id]={}
                returnObj[id].commentOwner=v[0][i].commentowner
                returnObj[id].commentedUser=v[0][i].commenteduser
            }
        }
        console.log(returnObj)
        io.emit('read notification',returnObj)
    })
})

app.get('/',async (req,res)=>{
    res.send(`server is correctly listening on port ${port}`)
})

/**라우팅**/
app.get('/loading',async (req,res)=>{
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
        let query = `select * from userinfo where userid=? and password=?`
        const v=await connection.query(query,[req.body.userid,req.body.password])
        // console.log(v[0])
        if(v[0].length==0) {
            returnObj.returnValue = false
        }
        else
        {
            returnJson=v[0][0]
            returnObj.success=true
            returnObj.userNickname=returnJson.nickname
            returnObj.userImage=returnJson.userimage
        }
    }
    catch (e)
    {
        console.log(e)
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