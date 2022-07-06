module.exports = (()=>{
    return {
        local: { // localhost
            host: 'localhost',
            port: '3306',
            user: 'root',
            password: '0000',
            database: '0000'
        },
        real: { // real server db info
            host: '13.58.48.132',
            port: '3306',
            user: 'DWORD',
            password: 'ssp',
            database: 'DWORD'
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