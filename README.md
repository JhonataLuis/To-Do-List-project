📝 To-Do List Application

https://img.shields.io/badge/Java-17-orange?style=for-the-badge&logo=openjdk
https://img.shields.io/badge/Spring%2520Boot-2.7+-green?style=for-the-badge&logo=springboot
https://img.shields.io/badge/PostgreSQL-15-blue?style=for-the-badge&logo=postgresql
https://img.shields.io/badge/Bootstrap-5.3-purple?style=for-the-badge&logo=bootstrap

Uma aplicação web moderna e responsiva para gerenciamento de tarefas

https://img.shields.io/github/issues/JhonataLuis/To-Do-List-project
https://img.shields.io/github/forks/JhonataLuis/To-Do-List-project
https://img.shields.io/github/stars/JhonataLuis/To-Do-List-project
https://img.shields.io/github/license/JhonataLuis/To-Do-List-project


✨ Funcionalidades
Funcionalidade	Descrição
✅ Cadastro de Tarefas	Adicione novas tarefas com título, descrição, prioridade e categoria
📋 Listagem de Tarefas	Visualize todas as tarefas em uma tabela organizada
✏️ Edição de Tarefas	Atualize informações das tarefas existentes
🗑️ Exclusão de Tarefas	Remova tarefas completadas ou desnecessárias
🏷️ Categorização	Organize tarefas por categorias (Trabalho, Pessoal, Estudos)
⚡ Sistema de Prioridades	Classifique tarefas como Alta, Média ou Baixa prioridade
📱 Design Responsivo	Interface adaptável para desktop e dispositivos móveis
🎨 Interface Moderna	Design clean e profissional com Bootstrap 5
🛠️ Tecnologias Utilizadas
Backend
Java 17 - Linguagem de programação

Spring Boot 2.7+ - Framework para aplicação web

Spring Data JPA - Persistência de dados

PostgreSQL - Banco de dados relacional

Maven - Gerenciamento de dependências

Frontend
HTML5 - Estrutura da aplicação

CSS3 - Estilização e design responsivo

JavaScript - Interatividade e chamadas AJAX

Bootstrap 5.3 - Framework CSS

jQuery 3.7 - Manipulação DOM e AJAX

Font Awesome - Ícones

🚀 Como Executar o Projeto
Pré-requisitos
Java 17 ou superior

Maven 3.6+

PostgreSQL 12+

Git

📋 Configuração do Banco de Dados
Crie um banco de dados no PostgreSQL:

sql
CREATE DATABASE todolist;
Configure as credenciais no application.properties:

properties
spring.datasource.url=jdbc:postgresql://localhost:5432/todolist
spring.datasource.username=seu_usuario
spring.datasource.password=sua_senha
spring.jpa.hibernate.ddl-auto=update
🏃‍♂️ Executando a Aplicação
Clone o repositório:

bash
git clone https://github.com/JhonataLuis/To-Do-List-project.git
cd To-Do-List-project
Compile e execute:

bash
mvn clean install
mvn spring-boot:run
Acesse a aplicação:

text
http://localhost:8080
📁 Estrutura do Projeto
text
To-Do-List-project/
├── src/
│   └── main/
│       ├── java/
│       │   └── br/com/toDoList/
│       │       ├── model/
│       │       │   └── Tarefas.java          # Entidade JPA
│       │       ├── repository/
│       │       │   └── TarefasRepository.java # Interface de repositório
│       │       ├── controller/
│       │       │   └── TarefasController.java # Controlador REST
│       │       └── ToDoListApplication.java  # Classe principal
│       └── resources/
│           ├── static/                       # Arquivos estáticos (HTML, CSS, JS)
│           └── application.properties        # Configurações
├── pom.xml                                   # Dependências Maven
└── README.md
🎯 Endpoints da API
Método	Endpoint	Descrição	Parâmetros
GET	/listatodos	Lista todas as tarefas	-
GET	/buscartarefaid	Busca tarefa por ID	idTarefa
POST	/salvar	Cria uma nova tarefa	JSON da tarefa
PUT	/atualizar	Atualiza uma tarefa existente	JSON da tarefa
DELETE	/delete	Remove uma tarefa	idTarefa

📝 Modelo de Dados da Tarefa

json
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
🔥 Executando em Ambiente de Desenvolvimento
Habilite o hot reload:

properties
spring.devtools.restart.enabled=true
Configure o log:

properties
logging.level.br.com.toDoList=DEBUG
🌍 Variáveis de Ambiente
bash
export DB_URL=jdbc:postgresql://localhost:5432/todolist
export DB_USERNAME=usuario
export DB_PASSWORD=senha
🤝 Contribuindo
Contribuições são sempre bem-vindas! Para contribuir:

Faça um fork do projeto

Crie uma branch para sua feature (git checkout -b feature/AmazingFeature)

Commit suas mudanças (git commit -m 'Add some AmazingFeature')

Push para a branch (git push origin feature/AmazingFeature)

Abra um Pull Request

📋 Guidelines para Contribuição
Siga o padrão de código existente

Adicione testes para novas funcionalidades

Atualize a documentação quando necessário

Use mensagens de commit claras e descritivas

📄 Licença
Este projeto está sob a licença MIT. Veja o arquivo LICENSE para mais detalhes.

text
MIT License

Copyright (c) 2024 Jhonata Luis

Permissão é concedida, gratuitamente, a qualquer pessoa que obtenha uma cópia
deste software e arquivos de documentação associados (o "Software"), para lidar
no Software sem restrição, incluindo, sem limitação, os direitos de usar, copiar,
modificar, fundir, publicar, distribuir, sublicenciar e/ou vender cópias do Software,
e para permitir que as pessoas a quem o Software é fornecido o façam, sujeito às
seguintes condições:
👨‍💻 Autor
Jhonata Luis

GitHub: @JhonataLuis

LinkedIn: Jhonata Luis

📞 Suporte
Se você encontrar algum problema ou tiver alguma dúvida:

Verifique a documentação e issues existentes

Abra uma issue descrevendo o problema

Entre em contato: jhonata.luis@example.com

🔄 Status do Projeto
https://img.shields.io/badge/Status-Em%2520Desenvolvimento-yellow?style=for-the-badge

Versão: 1.0.0
Última atualização: Outubro 2024

🎉 Agradecimentos
Equipe do Spring Boot pelo excelente framework

Comunidade Bootstrap pelos componentes UI

Todos os contribuidores e testadores


⭐️ Se este projeto foi útil para você, deixe uma estrela no repositório!
https://api.star-history.com/svg?repos=JhonataLuis/To-Do-List-project&type=Date



