// 파일 입출력 모듈 fs
const fs=require('fs')

// 웹서버 모듈 nodejs express사용
const express = require('express')
const app = express()

// 웹서버 포트: 3000
const port = 3000

/**이더리움 네트워크 관련 구**/
// 이더리움 네트워크 연결
const Web3=require('web3')
const wsProvider=new Web3.providers.WebsocketProvider('ws://13.58.48.132:8445')
const httpProvider=new Web3.providers.HttpProvider('http://13.58.48.132:8445')

// ws로 네트워크 연결
const web3=new Web3(wsProvider)

// deploy 정보
const deployInfo=require('./MainDeploy.json')
const contractNames=Object.keys(deployInfo.contracts)
const contractName=contractNames[0]
const abi=deployInfo.contracts[contractName].abi
const bin=deployInfo.contracts[contractName].bin

// contract정보
const contractInfo=require('./MainContract.json')
const contractAddress=contractInfo.contractAddress

const deploy=new web3.eth.Contract(abi).deploy({data:'0x'+bin})
const contract=new web3.eth.Contract(abi,contractAddress)

// 10개의 수를 가져오는 랜덤 1차원 배열 생성
const getRandArray=async ()=>{
    const nounce=Math.floor(Math.random()*5000)
    return await contract.methods.makeRandArray(nounce).call();
}

// [4][10]의 랜덤 2차원 배열 생성
const makeRandIdxArray=async ()=>{
    let idxArray=[];
    idxArray.push(await getRandArray())
    idxArray.push(await getRandArray())
    idxArray.push(await getRandArray())
    idxArray.push(await getRandArray())
    return idxArray
}

// sql연결 객체 생성
const mysql_dbc = require('./db_connection')();
const connection = mysql_dbc.init();

const year = new Date().getFullYear(); // 년도
const month = new Date().getMonth() + 1;  // 월
const date = new Date().getDate(); // 일
const myDay=String(year)+String(month)+String(date);

// 랜덤 단어 인덱스 배열
let randArray=new Object()

// 쿠키 저장 모듈
const cookieParser = require('cookie-parser');

// 쿠키 모듈 미들웨어 등록
app.use(cookieParser());
// sql연결
mysql_dbc.open(connection);

// post를 받기 위한 미들웨어 등록
app.use(express.json());
// post받으면서 인코딩 안 깨지도록 하기 위함
app.use(express.urlencoded({ extended: false }));
/** 이 윗 부분은 기본 설정-어려우면 대충 그렇구나 하고 넘어가먄 된다- **/

// 라우팅

// 기본 경로에서의 리스폰스
app.get('/', async (req, res)=>{
    res.send('Hello World!')
})
// signin 경로에서의 get요청이 들어올 경우 리스폰스
// 없어도 된다
app.get('/signin',async (req,res)=>{
    res.send(req.body);
})
// signin 경로에서의 post요청이 들어올 경우 리스폰스
// 해당 경로 path를 받고 request와 response를 처리할 콜백 함수를 등록한다(순서 바뀌면 에러난다 -(res, req)가 아니다!-
app.post('/signin', (req,res)=>{
    // req.body는 post로 들어온 정보
    // map형식으로 보내졌다
    // JsonObject형식으로 사용 가능하다
    // id는 해당 리퀘스트(req)의 몸체(body)의 아이디(userid) 값
    const userid=req.body.userid;
    const password=req.body.password;
    // 데이터베이스 조회 퀴리
    // 해당 아이디와 비밀번호와 맞는 row가 존재하는지 확인하는 쿼리
    let query = `select * from userinfo where userid='${userid}' and password='${password}'`
    // 쿼리 실행
    connection.query(query, (err, result)=>{
        if(err) throw err
        // 결과의 길이가 0인 경우
        // 결과가 없는 경우를 뜻한다
        // -1을 리스폰스
        if(result.length==0) res.send('-1')
        else {
            // DB에 해당 정보가 존재하는 경우
            // 쿠키를 등록한다
            res.cookie('userid', result[0].userid, cookieConfig);
            res.cookie('password', result[0].password, cookieConfig);
            res.cookie('nickname', result[0].nickname, cookieConfig);
            // main화면으로 넘어가기 위해 0을 리스폰스
            res.send('0')
        }

    })
})

// 회원가입 페이지에 대한 리퀘스트 처리
app.post('/signup',async (req,res)=>{
    // 이더리움 계정 생성
    // 생성된 계정에 대한 비밀번호 설정
    // const newAccoount=await web3.eth.personal.newAccount(req.body.password);
})

// 영단어 10개가 제공되는 메인화면
app.get('/main',async (req,res)=>{

    // 쿠키 읽기
    // 만약 dayCookie가 지정되어 있다면
    if (req.cookies.dayCookie) {
        // 만약 dayCookie가 오늘 날짜와 다르다면
        // 하루가 지났음
        // 새로운 단어를 할당해야 함
        if(req.cookies.dayCookie!=myDay) {
            // 기존 날짜 쿠키 삭제
            res.clearCookie('dayCookie', req.cookies.dayCookie, {
                httpOnly: true,
                path: '/main'
            });
            // 오늘 날짜로 쿠키 재등록
            res.cookie('dayCookie', myDay, {
                // 쿠키 만료일은 현재시각+하루
                expires: new Date(Date.now() + 86400000),
                // 웹 서버에서만 접근 가능
                httpOnly: true,
                // 쿠키 경로
                path: '/main'
            })
            randArray["dailyWordsIndex"]=await getRandArray()
            randArray["wordTestIdx1"]=await getRandArray()
            randArray["wordTestIdx2"]=await getRandArray()
            randArray["wordTestIdx3"]=await getRandArray()
            /*
            여기에 sql과 연동하여 오늘의 새로운 단어 지정하는 코드
            */
        }
        /*
        날짜가 같을 경우 새로 지정하지 않음
         */
        console.log(req.cookies)
    }
    // dayCookie가 없다면 오늘 날짜로 등록
    else
    {
        res.cookie('dayCookie', myDay, {
            expires: new Date(Date.now() + 86400000),
            httpOnly: true,
            path: '/main'
        })
    }
})

// 서버 실행
app.listen(port, ()=>{
    console.log(`application is listening on port ${port}...`)
})
