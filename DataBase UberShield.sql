CREATE DATABASE UberShield;
USE UberShield;

-- Tabela de Usuários
CREATE TABLE Usuarios (
    id_usuario INT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    telefone VARCHAR(20) NOT NULL UNIQUE,
    email VARCHAR(100) NOT NULL UNIQUE,
    tipo_usuario ENUM('passageiro', 'condutor') NOT NULL,
    localizacao VARCHAR(255)
);

# tabela de condutores
CREATE TABLE Condutores (
    id_condutor INT PRIMARY KEY,
    carro_modelo VARCHAR(50) NOT NULL,
    placa_veiculo VARCHAR(15) NOT NULL UNIQUE,
    localizacao VARCHAR(255),
    nota_avaliacao FLOAT DEFAULT 0,
    FOREIGN KEY (id_condutor) REFERENCES Usuarios(id_usuario) ON DELETE CASCADE
    /* aqui eu usei CASCADE pq oque o cascade faz: o CASCADE vai deletar todas informaçoes dessa tabela
    caso o usuario(id_usuario) seja deletado, garantindo que nao mantenhamos informaçoes de um usuario
    que ja excluiu sua conta*/
);

# tabela de viagens
CREATE TABLE Viagens (
    id_viagem INT AUTO_INCREMENT PRIMARY KEY,
    id_usuario INT,
    id_condutor INT,
    origem VARCHAR(255) NOT NULL,
    destino VARCHAR(255) NOT NULL,
    data_hora DATETIME NOT NULL,
    status_viagem ENUM('solicitada', 'em andamento', 'concluída', 'cancelada') NOT NULL,
    FOREIGN KEY (id_usuario) REFERENCES Usuarios(id_usuario) ON DELETE SET NULL,
    FOREIGN KEY (id_condutor) REFERENCES Condutores(id_condutor) ON DELETE SET NULL
	/*aqui e na linha de cima usei set Null pra caso o usuario ou condutor delete, nao apareça 
    seu nome*/
);

# avaliações
CREATE TABLE Avaliacoes (
    id_avaliacao INT AUTO_INCREMENT PRIMARY KEY,
    id_viagem INT,
    id_usuario INT,
    nota_avaliacao FLOAT CHECK (nota_avaliacao BETWEEN 1 AND 5),
    comentario_avaliacao TEXT,
    FOREIGN KEY (id_viagem) REFERENCES Viagens(id_viagem) ON DELETE CASCADE,
    /* mesma coisa de antes, CASCADE pq caso a viagem seja cancelada, nao tenha avaliaçao 
    ( exclui tudo da tabela referente a viagem )*/
    FOREIGN KEY (id_usuario) REFERENCES Usuarios(id_usuario) ON DELETE SET NULL
);

# tabela das areas de risco
CREATE TABLE Areas_Risco (
    id_risco INT AUTO_INCREMENT PRIMARY KEY,
    alerta_latitude DECIMAL(10,8) NOT NULL,
    /* aqui eu usei decimal (10,8) porque: 
    Latitude varia de -90 a 90 graus (-90.00000000 a 90.00000000.). Com DECIMAL(10,8), temos espaço suficiente 
    para armazenar valores com até 8 casas decimais, garantindo precisão até frações de metro.
    o 10,8 é pra garantir precisao de 10 digitos, e 8 digitos depois do ponto (10,8).*/
    
    
    alerta_longitude DECIMAL(11,8) NOT NULL,
    /* o mesmo pensamento de cima, mas com 11,8 porque a 
    longitude vai de -180 a 180 (-180.00000000 a 180.00000000.)*/
    
    alerta_descricao TEXT NOT NULL,
    nivel_risco ENUM('baixo', 'médio', 'alto', 'crítico') NOT NULL
);

