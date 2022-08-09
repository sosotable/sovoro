
// sql연결 객체 생성
const mysql = require('mysql2/promise');
const mysql_dbc = require('./db_connection')();
let connection=null;
(async ()=>{
    connection = await mysql_dbc.init();
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
        console.log('hello?')
        console.log(returnObj);
        connection.end()
    }
})()