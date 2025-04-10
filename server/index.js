const express = require("express");
const app = express();
const port = process.env.PORT || 3000;
const crypto = require('crypto');
const sqlite3 = require("sqlite3").verbose();
const CAMINHO_DB = "banco.db";
const db = new sqlite3.Database(CAMINHO_DB);
const crypto = require('crypto');

// Health Check simplificado
app.get('/health', (req, res) => {
  res.status(200).json({ status: 'online', timestamp: Date.now() });
});

// Tratamento de erros global
app.use((err, req, res, next) => {
  console.error(err.stack);
  res.status(500).json({ status: 'error', message: 'Erro interno no servidor' });
});

// Middleware
app.use(express.json());
app.use(express.urlencoded({ extended: true }));
app.use((req, res, next) => {
    res.header("Access-Control-Allow-Origin", "*");
    next();
});

// Nosso endpoint, aqui busca o salt
app.post('/buscarSalt', (req, res) => {
    const { nome } = req.body;
    
    db.get(
        'SELECT salt FROM users WHERE nome = ?',
        [nome],
        (err, row) => {
            if (err) {
                return res.status(500).json({ 
                    status: 'error', 
                    message: 'Erro no servidor' 
                });
            }
            
            if (!row) {
                return res.status(404).json({ 
                    status: 'not_found', 
                    message: 'Usuário não encontrado' 
                });
            }
            
            res.json({ 
                status: 'success', 
                salt: row.salt 
            });
        }
    );
});

// Inicialização assíncrona do banco
db.serialize(() => {
    db.run(`CREATE TABLE IF NOT EXISTS users (
      id INTEGER PRIMARY KEY AUTOINCREMENT,
      nome TEXT NOT NULL UNIQUE,
      email TEXT NOT NULL UNIQUE,
      senha_hash TEXT NOT NULL,
      salt TEXT NOT NULL
    )`);
    
    console.log('Banco de dados inicializado');
  });
  

// Login do usuario
app.post("/user", (req, res) => {
    const { nome, senha_hash } = req.body;
    
    db.get(
        'SELECT senha_hash FROM users WHERE nome = ?',
        [nome],
        (err, row) => {
            if (err) {
                return res.status(500).json({ 
                    status: 'error', 
                    message: 'Erro no servidor' 
                });
            }
            
            if (!row) {
                return res.status(401).json({ 
                    status: 'unauthorized', 
                    message: 'Credenciais inválidas' 
                });
            }
            
            if (senha_hash === row.senha_hash) {
                res.json({ 
                    status: 'success', 
                    message: 'Login bem-sucedido' 
                });
            } else {
                res.status(401).json({ 
                    status: 'unauthorized', 
                    message: 'Credenciais inválidas' 
                });
            }
        }
    );
});

// Inicialização assíncrona do banco
db.serialize(() => {
  db.run(`CREATE TABLE IF NOT EXISTS users (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    nome TEXT NOT NULL UNIQUE,
    email TEXT NOT NULL UNIQUE,
    senha_hash TEXT NOT NULL,
    salt TEXT NOT NULL
  )`);
  
  console.log('Banco de dados inicializado');
});

// Mantenha essa linha no final
app.listen(port, '0.0.0.0', () => {
  console.log(`Servidor rodando na porta ${port}`);
});

// Registro de usuarios é feito aqui, o backend todo ta no app ( endpoint criar usuario )
app.post("/criarUsuario", function (req, res) {
    const { 
        nome, 
        email,
        senha_hash, 
        salt
    } = req.body;

    db.get(
        'SELECT id FROM users WHERE nome = ? OR email = ?',
        [nome, email],
        (err, row) => {
            if (err) {
                return res.status(500).json({ 
                    status: 'error', 
                    message: 'Erro no servidor' 
                });
            }
            
            if (row) {
                return res.status(409).json({ 
                    status: 'conflict', 
                    message: 'Usuário ou email já existem' 
                });
            }
            
            db.run(
                `INSERT INTO users (
                    nome, 
                    email,
                    senha_hash, 
                    salt
                ) VALUES (?, ?, ?, ?)`,
                [nome, email, senha_hash, salt],
                function(err) {
                    if (err) {
                        return res.status(500).json({ 
                            status: 'error', 
                            message: 'Erro ao criar usuário' 
                        });
                    }
                    
                    res.json({ 
                        status: 'success', 
                        message: 'Usuário criado com sucesso' 
                    });
                }
            );
        }
    );
});

app.listen(port, () => {
    console.log(`Servidor rodando na porta ${port}`);
});