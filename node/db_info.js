/**비동기 처리**/
module.exports = (()=>{
    return {
        local: { // localhost
            host: '*',
            port: '*',
            user: '*',
            password: '*',
            database: '*'
        },
        real: { // real server db info
            host: '*',
            port: '*',
            user: '*',
            password: '*',
            database: '*'
        },
        dev: { // dev server db info
            host: '',
            port: '',
            user: '',
            password: '',
            database: ''
        }
    }
})();