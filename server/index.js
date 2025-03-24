var express = require("express");
var app = express();
var port = process.env.PORT || 3000;

//Json
var bodyParser = require("body-parser");
app.use(bodyParser.json());
app.use(bodyParser.urlencoded({ extended: true }));

//Banco de Dados
var sqlite3 = require("sqlite3").verbose();
var CAMINHO_DB = "banco.db";
var db = new sqlite3.Database(CAMINHO_DB);

app.get("/tudo", function (req, res) {
  db.all(`SELECT * FROM users`, [], (err, rows) => {
    if (err) {
      res.send(err);
    }
    res.send(rows);
  });
});

app.post("/criarUsuario", function (req, res) {
    res.header("Access-Control-Allow-Origin", "*");
    var nome = req.body.nome;
    var senha = req.body.senha;
  
    // Consultar se o usuário já existe
    sqlBusca = `SELECT * FROM users WHERE nome = ?`;
    db.all(sqlBusca, [nome], (err, rows) => {
      if (err) {
        res.status(500).json({ status: "error", message: "Erro na busca" });
      } else {
        if (rows.length > 0) {
          // Usuário já existe
          res.status(409).json({ status: "exists", message: "Usuário já existe!" });
        } else {
          // Inserir novo usuário
          sql = `INSERT INTO users (nome, senha) VALUES (?, ?)`;
          db.all(sql, [nome, senha], (err, rows) => {
            if (err) {
              res.status(500).json({ status: "error", message: "Erro na gravação" });
            } else {
              res.status(200).json({ status: "success", message: "Usuário cadastrado!" });
            }
          });
        }
      }
    });
  });
  

app.get("/", function (req, res) {
  res.send("Olá! Do servidor!");
});

app.post("/user", function (req, res) {
  console.log(req.body);
  var nome = req.body.nome;
  var senha = req.body.senha;
  console.log(nome + " " + senha);
  res.send("Dados recebidos!");
});

app.listen(port, () => {
  console.log(`Servidor rodando na porta ${port}`);
});
