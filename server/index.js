const express = require("express");
const app = express();
const port = process.env.PORT || 3000;
const sqlite3 = require("sqlite3").verbose();
const CAMINHO_DB = "banco.db";

// configuracao do banco de dados, se falhar vai dar erro
const db = new sqlite3.Database(CAMINHO_DB, (err) => {
  if (err) {
    console.error("Erro ao conectar ao banco:", err.message);
    process.exit(1);
  }
  
  console.log("Conectado ao banco SQLite");
  
  // criacao do DB do ap
  db.run(`CREATE TABLE IF NOT EXISTS users (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    nome TEXT NOT NULL UNIQUE,
    email TEXT NOT NULL UNIQUE,
    senha_hash TEXT NOT NULL,
    salt TEXT NOT NULL
  )`, (err) => {
    if (err) {
      console.error("Erro ao criar tabela:", err.message);
    } else {
      console.log("Tabela 'users' verificada/criada com sucesso");
    }
  });
});

// nossos middlewares pra post e get
app.use(express.json());
app.use(express.urlencoded({ extended: true }));
app.use((req, res, next) => {
  res.header("Access-Control-Allow-Origin", "*");
  res.header("Access-Control-Allow-Methods", "GET, POST");
  res.header("Access-Control-Allow-Headers", "Content-Type");
  next();
});

// rotas do servidor
app.get('/', (req, res) => res.send('Servidor ta mil grau, rodando tudo OK'));
app.get('/favicon.ico', (req, res) => res.status(204).end());

app.post('/buscarSalt', (req, res) => {
  const { nome } = req.body;
  console.log(`[BUSCAR SALT] Solicitado para: ${nome}`);
//eeeeh, aq é a verificacao de dados na loginacitivty
  db.get(
    'SELECT salt FROM users WHERE LOWER(nome) = LOWER(?)',
    [nome.trim()],
    (err, row) => {
      if (err) {
        console.error('[ERRO DB]', err.message);
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
      
      res.json({ status: 'success', salt: row.salt });
    }
  );
});
// rota do login na loginactivity
app.post("/user", (req, res) => {
  const { nome, senha_hash } = req.body;
  console.log(`[LOGIN] Tentativa de: ${nome}`);

  db.get(
    'SELECT senha_hash FROM users WHERE LOWER(nome) = LOWER(?)',
    [nome.trim()],
    (err, row) => {
      if (err) {
        console.error('[ERRO DB]', err.message);
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
        res.json({ status: 'success', message: 'Login bem-sucedido' });
      } else {
        res.status(401).json({ 
          status: 'unauthorized', 
          message: 'Credenciais inválidas' 
        });
      }
    }
  );
});
//rota da criacao de usuario
app.post("/criarUsuario", (req, res) => {
  const { nome, email, senha_hash, salt } = req.body;
  const nomeNormalizado = nome.toLowerCase().trim();
  console.log(`[REGISTRO] Tentativa: ${nomeNormalizado} (${email})`);

  db.get(
    'SELECT id FROM users WHERE LOWER(nome) = ? OR email = ?',
    [nomeNormalizado, email],
    (err, row) => {
      if (err) {
        console.error('[ERRO DB]', err.message);
        return res.status(500).json({ 
          status: 'error', 
          message: 'Erro no servidor' 
        });
      }
      
      if (row) {
        console.log('[CONFLITO] Usuário/email já existe');
        return res.status(409).json({ 
          status: 'conflict', 
          message: 'Usuário ou email já existem' 
        });
      }
      
      db.run(
        `INSERT INTO users (nome, email, senha_hash, salt) VALUES (?, ?, ?, ?)`,
        [nomeNormalizado, email, senha_hash, salt],
        function(err) {
          if (err) {
            console.error('[ERRO DB]', err.message);
            return res.status(500).json({ 
              status: 'error', 
              message: 'Erro ao criar usuário' 
            });
          }
          
          console.log(`[REGISTRO] Sucesso - ID: ${this.lastID}`);
          res.json({ 
            status: 'success', 
            message: 'Usuário criado com sucesso' 
          });
        }
      );
    }
  );
});

// aqui os tratamentos de erro
app.use((err, req, res, next) => {
  console.error('[ERRO GLOBAL]', err.stack);
  res.status(500).json({ 
    status: 'error', 
    message: 'Erro interno no servidor' 
  });
});

//log do serverrr
app.listen(port, () => {
  console.log(`Servidor rodando na porta ${port}`);
});