# 📝 To-Do List API REST

![Java](https://img.shields.io/badge/Java-17-orange?style=for-the-badge&logo=openjdk)
![Spring Boot](https://img.shields.io/badge/Spring%2520Boot-2.7+-green?style=for-the-badge&logo=springboot)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-15-blue?style=for-the-badge&logo=postgresql)
![Bootstrap](https://img.shields.io/badge/Bootstrap-5.3-purple?style=for-the-badge&logo=bootstrap)

> Uma aplicação web moderna e responsiva para **gerenciamento de tarefas**, perfeita para organização pessoal ou em equipe.

---

## 🖼️ Demonstração

> Substitua os links abaixo pelos screenshots ou GIFs do seu app:

![Dashboard](C:\Projects\Java Spring\To-Do-List-project\frontend\public\assets\dashboard.png)  
![Criar Tarefa](https://via.placeholder.com/800x400.png?text=Criar+Tarefa)  
![Lista de Tarefas](https://via.placeholder.com/800x400.png?text=Lista+de+Tarefas)

---

## ✨ Funcionalidades Principais

| Funcionalidade       | Descrição                                           |
|---------------------|---------------------------------------------------|
| ✅ **Cadastro de Tarefas** | Adicione tarefas com título, descrição, prioridade e categoria |
| 📋 **Listagem de Tarefas com Paginação** | Visualize todas as tarefas em uma tabela organizada |
| ✏️ **Edição de Tarefas** | Atualize informações das tarefas existentes       |
| 🗑️ **Exclusão de Tarefas** | Remova tarefas completadas ou desnecessárias      |
| 🏷️ **Categorização**      | Organize tarefas por categorias (Trabalho, Pessoal, Estudos) |
| ⚡ **Sistema de Prioridades** | Classifique tarefas como Alta, Média ou Baixa prioridade |
| 📱 **Design Responsivo**  | Interface adaptável para desktop e mobile |
| 🎨 **Interface Moderna**  | Design clean e profissional com Bootstrap 5 |

---

## 🛠️ Tecnologias Utilizadas

**Backend**
- ☕ Java 17  
- 🌱 Spring Boot 2.7+ 
- 🗄️ PostgreSQL  
- 🔗 Spring Data JPA  
- 📦 Maven  

**Frontend**
- 🌐 HTML5  
- 🎨 CSS3  
- ⚡ JavaScript
- ⚛️ React  
- 💎 Bootstrap 5.3  
- 🎭 Bootstrap Icons  

---

## 🚀 Como Executar o Projeto

### Pré-requisitos
- Java 17+  
- Maven 3.6+  
- PostgreSQL 12+  
- Git  

### Configuração do Banco de Dados
```sql
CREATE DATABASE todolist;

spring.datasource.url=jdbc:postgresql://localhost:5432/seu_banco
spring.datasource.username=seu_usuario
spring.datasource.password=sua_senha
spring.jpa.hibernate.ddl-auto=update

Executando a Aplicação
# Clonar o repositório
git clone https://github.com/JhonataLuis/To-Do-List-project.git
cd To-Do-List-project

# Compilar e executar
mvn clean install
mvn spring-boot:run


Acesse: http://localhost:8080

📁 Estrutura do Projeto
To-Do-List-project/
├── src/
│   └── main/
│       ├── java/br/com/toDoList/
│       │   ├── model/Tarefas.java
│       │   ├── repository/TarefasRepository.java
│       │   ├── controller/TarefasController.java
│       │   └── ToDoListApplication.java
│       └── resources/
│           ├── static/ 
│           └── application.properties
├── pom.xml
└── README.md

🎯 Endpoints da API
Método	Endpoint	Descrição	Parâmetros
GET	/listatodos	Lista todas as tarefas	-
GET	/buscartarefaid	Busca tarefa por ID	idTarefa
POST	/salvar	Cria uma nova tarefa	JSON da tarefa
PUT	/atualizar	Atualiza uma tarefa	JSON da tarefa
DELETE	/delete	Remove uma tarefa	idTarefa
Modelo de Dados da Tarefa
{
  "id": 1,
  "titulo": "Reunião com a equipe",
  "descricao": "Preparar apresentação de resultados",
  "concluido": false,
  "prioridade": "Alta",
  "categoria": "Trabalho",
  "dataCriacao": "2024-10-01T10:00:00"
}

🔧 Configuração de Desenvolvimento
# Hot reload
spring.devtools.restart.enabled=true

# Log detalhado
logging.level.br.com.toDoList=DEBUG

Variáveis de Ambiente
export DB_URL=jdbc:postgresql://localhost:5432/todolist
export DB_USERNAME=usuario
export DB_PASSWORD=senha

🤝 Contribuindo

Faça um fork do projeto

Crie uma branch: git checkout -b feature/AmazingFeature

Commit suas alterações: git commit -m 'Add some AmazingFeature'

Push: git push origin feature/AmazingFeature

Abra um Pull Request 🚀

Guidelines

Siga o padrão de código existente

Adicione testes para novas funcionalidades

Atualize a documentação quando necessário

Use mensagens de commit claras

📄 Licença

MIT License © 2024 Jhonata Luis

👨‍💻 Autor

Jhonata Luis

GitHub:[https://github.com/JhonataLuis]

LinkedIn: linkedin

Email: email@example.com

🔄 Status do Projeto


Versão: 1.0.0 | Última atualização: Outubro 2025


⭐️ Se este projeto foi útil, deixe uma estrela no repositório!
