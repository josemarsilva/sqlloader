# SqlLoader - SQL file Loader

## 1. Introdução ##

Este repositório contém o código fonte do componente **SqlLoader**. Este componente distribuído como um arquivo (.jar) pode ser executado em linha de comando (tanto Windows quanto Linux). O **SqlLoader** recebe como argumentos: a) o tipo do arquivo de entrada (Ex: 'csv'); b) o nome do arquivo  de entrada.


### 1.1. Help Online linha

* O componente **SqlLoader** funciona com argumentos de linha de comando (tanto Windows quanto Linux). Com o argumento '-h' mostra o help da a aplicação.

```bat
C:\My Git\workspace-github\csvintoexcel\dist>java -jar sqlloader.jar
usage: sqlloader [<args-options-list>] - v.2019.11.27
 -c,--table-columns <arg>      Nome das colunas da tabela SQL (separadas
                               por '-') onde sera feita a carga. Ex: -c
                               col_1-col_2-col_3-col_4-col_5
 -d,--database-type <arg>      Database type. List of values
                               ('sqlserver'). Ex: -d sqlserver
 -h,--help                     shows usage help message. See more
                               https://github.com/josemarsilva/sqlloader
 -i,--input-file-name <arg>    Nome do arquivo de entrada da carga na
                               tabela SQL. Ex: -i input.csv
 -n,--table-name <arg>         Nome da tabela SQL onde sera feita a carga.
                               Ex: -n input_table
 -o,--commit-interval <arg>    Intervalo de linhas em que se deve fazer
                               commit (default=0 toda linha). Ex: -o 0
 -s,--skip-header-rows <arg>   Numero de linhas de cabecalhos a saltar.
                               Ex: -s 1
 -t,--input-file-type <arg>    Tipo do arquivo de entrada da carga na
                               tabela SQL. Lista de valores: ('csv' ). Ex:
                               -t csv
 -u,--database-url <arg>       Database URL. Ex: -u
                               jdbc:sqlserver://localhost:1433;user=sa;pas
                               sword=secret123;databaseName=Northwind```




### 2. Documentação ###

### 2.1. Diagrama de Caso de Uso (Use Case Diagram) ###

#### Diagrama de Caso de Uso

![UseCaseDiagram](doc/UseCaseDiagram%20-%20Context%20-%20SqlLoader.png)


### 2.5. Requisitos ###

* n/a


## 3. Projeto ##

### 3.1. Pré-requisitos ###

* Linguagem de programação: Java
* IDE: Eclipse (recomendado Oxigen 2)
* JDK: 1.8
* Maven Repository dependence: 
  * `pom.xml`: Apache CLI
  * `mvn install:install-file -Dfile=sqljdbc4.jar -DgroupId=com.microsoft.sqlserver -DartifactId=sqljdbc4 -Dversion=4.0 -Dpackaging=jar`


### 3.2. Guia para Desenvolvimento ###

* Obtenha o código fonte através de um "git clone". Utilize a branch "master" se a branch "develop" não estiver disponível.
* Faça suas alterações, commit e push na branch "develop".


### 3.3. Guia para Configuração ###

* n/a


### 3.4. Guia para Teste ###

* n/a


### 3.5. Guia para Implantação ###

* Obtenha o último pacote (.war) estável gerado disponível na sub-pasta './dist'.



### 3.6. Guia para Demonstração ###

* **Step-1**: Suponha o arquivo `input.csv` abaixo:

```csv
col-1;col-2;col-3;col-4;col-5
1;impar;cachorro;Brasil;branco
2;par;gato;Argentina;preto
3;impar;pato;Chile;azul
4;par;leao;Uruguai;amarelo
5;impar;onca;Estados Unidos;verde
6;par;tigre;Mexico;branco
7;impar;jaguatirica;Canada;preto
8;par;lobo;Portugal;azul
9;impar;vaca;Espanha;amarelo
10;par;cavalo;Franca;verde
11;impar;galinha;Alemanha;branco
12;par;cisne;Belgica;preto
13;impar;papagaio;Marrocos;azul
14;par;arara;Argelia;amarelo
15;impar;sabia;Libia;verde
16;par;bode;Tunisia;branco
17;impar;burro;Brasil;preto
18;par;elefante;Egito;azul
19;impar;macaco;Mauritania;amarelo
20;par;aguia;Senegal;verde
```

* **Step-2**: Suponha que você deseje carregar este arquivo em uma tabela do banco de dados chamada `dbo.input_table`:

```sql
	CREATE TABLE [dbo].[input_table]
	(
		[id]    [int] IDENTITY(1,1)  NOT NULL,
		[col_1] [int]                NOT NULL,
		[col_2] [nvarchar] (30)      NOT NULL,
		[col_3] [nvarchar] (30)      NOT NULL,
		[col_4] [nvarchar] (30)      NOT NULL,
		[col_5] [nvarchar] (30)      NOT NULL,
	PRIMARY KEY CLUSTERED 
		(
		  [id] ASC
		) 
	);
```


* **Step-3**: Executamos a ferramenta **SqlLoader** em linha de comando com os seguintes argumentos:

```cmd
java -jar sqlloader.jar 
Reading 'input.csv' ...
```

* **Step-3**: Observe o resultado da carga do arquivo `input.csv` executando uma query no banco de dados:

```sql
SELECT * FROM dbo.input_table
```




### 3.7. Guia para Execução ###

* n/a


## Referências ##

* n/a
