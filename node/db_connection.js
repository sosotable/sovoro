const mysql = require('mysql2/promise');
const config = require('./db_info').real;

module.exports = ()=>{
    return {
        init: async ()=>{
            return mysql.createPool({
                host: config.host,
                port: config.port,
                user: config.user,
                password: config.password,
                database: config.database
            });
        },

        open: async (con)=>{
            con.connect((err)=>{
                if (err) {
                    console.error('mysql connection error :' + err);
                } else {
                    console.info('mysql is connected successfully.');
                }
            })
        }
    }
};