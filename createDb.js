const { Connection, Request } = require('tedious');
const dotenv = require('dotenv');
dotenv.config();

const config = {
  server: process.env.MSSQL_HOST,
  authentication: {
    type: 'default',
    options: {
      userName: 'sa',
      password: process.argv[2]
    },
  },
};

const connection = new Connection(config);

new Promise((resolve, reject) => {
  connection.on('connect', function(err) {
    connection.execSql(
      new Request('CREATE DATABASE awetestdb', err => {
        if (err) return reject(err);
        resolve();
      })
    )
  })
})
.then(() => {
  console.log('Successfully created SQLServer database on ' + process.env.MSSQL_HOST);
  process.exit()
})
.catch(console.error);