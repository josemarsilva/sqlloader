package org.gnu.sqlloader;

import java.io.File;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.log4j.Logger;


/*
 * CliArgsParser class is responsible for extract, compile and check consistency
 * for command line arguments passed as parameters in command line
 * 
 * @author josemarsilva@yahoo.com.br
 * @date   2019-03-05
 * 
 */

public class CliArgsParser {

	// Logger ...
	final static Logger logger = Logger.getLogger(CliArgsParser.class);


	// Constants ...
	public final static String APP_NAME = new String("sqlloader");
	public final static String APP_VERSION = new String("v.2020.04.16");
	public final static String APP_USAGE = new String(APP_NAME + " [<args-options-list>] - "+ APP_VERSION);
	public final static int ERROR_LEVEL_SQLLOADER = 1;

	// Constants defaults ...

	// Constants Error Messages ...
	public final static String MSG_ERROR_FILE_TYPE_INVALID = new String("Erro: tipo de arquivo '%s' inválido !");
	public final static String MSG_ERROR_FILE_NOT_FOUND = new String("Erro: arquivo '%s' não existe !");
	public final static String MSG_ERROR_SQL_TYPE_INVALID = new String("Erro: database type '%s' inválido!");
	
	// Properties ...
    private String inputFileType = new String("");
    private String inputFileName = new String("");
    private Integer skipHeaderRows = new Integer(0);
    private String databaseType = new String("sqlserver");
    private String databaseUrl = new String("");
    private String tableName = new String("");
    private String tableColumns = new String("");
    private String tableColumnFormats = new String("");
    private Integer commitInterval = new Integer(0);
    private String loadMode = new String("append");


    /*
     * CliArgsParser(args) - Constructor ...
     */
	public CliArgsParser( String[] args ) {


		// Options creating ...
		logger.info("Command line Options");
		Options options = new Options();
		
		
		// Options configuring  ...
        Option helpOption = Option.builder("h") 
        		.longOpt("help") 
        		.required(false) 
        		.desc("shows usage help message. See more https://github.com/josemarsilva/sqlloader") 
        		.build(); 
        Option inputFileTypeOption = Option.builder("t")
        		.longOpt("input-file-type") 
        		.required(true) 
        		.desc("Tipo do arquivo de entrada da carga na tabela SQL. Lista de valores: ('csv' ). Ex: -t csv")
        		.hasArg()
        		.build();
        Option inputFileNameOption = Option.builder("i")
        		.longOpt("input-file-name") 
        		.required(true) 
        		.desc("Nome do arquivo de entrada da carga na tabela SQL. Ex: -i input.csv")
        		.hasArg()
        		.build();
        Option skipHeaderRowsOption = Option.builder("s")
        		.longOpt("skip-header-rows") 
        		.required(false)
        		.desc("Numero de linhas de cabecalhos a saltar. Ex: -s 1")
        		.hasArg()
        		.build();
        Option databaseTypeOption = Option.builder("d")
        		.longOpt("database-type") 
        		.required(false) 
        		.desc("Database type. List of values ('sqlserver'). Ex: -d sqlserver")
        		.hasArg()
        		.build();
        Option databaseUrlOption = Option.builder("u")
        		.longOpt("database-url") 
        		.required(true) 
        		.desc("Database URL. Ex: -u jdbc:sqlserver://localhost:1433;user=sa;password=secret123;databaseName=Northwind")
        		.hasArg()
        		.build();
        Option tableNameOption = Option.builder("n")
        		.longOpt("table-name") 
        		.required(true) 
        		.desc("Nome da tabela SQL onde sera feita a carga. Ex: -n input_table")
        		.hasArg()
        		.build();
        Option tableColumnsOption = Option.builder("c")
        		.longOpt("table-columns") 
        		.required(true) 
        		.desc("Nome das colunas da tabela SQL (separadas por '-') onde sera feita a carga. Ex: -c col_1-col_2-col_3-col_4-col_5")
        		.hasArg()
        		.build();
        Option tableColumnFormatsOption = Option.builder("f")
        		.longOpt("table-column-formats") 
        		.required(false) 
        		.desc("Formatos das colunas da tabela SQL (separadas por '-'). Lista de valores: ('s': String, 'd':Inteiro, 'f':Decimal). Ex: -c d-s-s-s-s")
        		.hasArg()
        		.build();
        Option commitIntervalOption = Option.builder("o")
        		.longOpt("commit-interval") 
        		.required(false) 
        		.desc("Intervalo de linhas em que se deve fazer commit (default=0 toda linha). Ex: -o 0")
        		.hasArg()
        		.build();
        Option loadModeOption = Option.builder("m")
        		.longOpt("load-mode") 
        		.required(false) 
        		.desc("Modo de carga. Lista de valores: ('append', 'replace') (default='append'). Ex: -m replace")
        		.hasArg()
        		.build();
        
		// Options adding configuration ...
        options.addOption(helpOption);
        options.addOption(inputFileTypeOption);
        options.addOption(inputFileNameOption);
        options.addOption(skipHeaderRowsOption);
        options.addOption(databaseTypeOption);
        options.addOption(databaseUrlOption);
        options.addOption(tableNameOption);
        options.addOption(tableColumnsOption);
        options.addOption(tableColumnFormatsOption);
        options.addOption(commitIntervalOption);
        options.addOption(loadModeOption);
        
        
        // CommandLineParser ...
        CommandLineParser parser = new DefaultParser();
		try {
			CommandLine cmdLine = parser.parse(options, args);
			
	        if (cmdLine.hasOption("help")) { 
	            HelpFormatter formatter = new HelpFormatter();
	            formatter.printHelp(APP_USAGE, options);
	            System.exit(0);
	        } else {
	        	
	        	// Set properties from Options ...
	        	this.setInputFileType( cmdLine.getOptionValue("input-file-type", "") );
	        	this.setInputFileName( cmdLine.getOptionValue("input-file-name", "") );
	        	this.setSkipHeaderRows( cmdLine.getOptionValue("skip-header-rows", "") );
	        	this.setDatabaseType( cmdLine.getOptionValue("database-type", "")  );
	        	this.setDatabaseUrl( cmdLine.getOptionValue("database-url", "") );
	        	this.setTableName( cmdLine.getOptionValue("table-name", "") );
	        	this.setTableColumns( cmdLine.getOptionValue("table-columns", "") );
	        	this.setTableColumnFormats( cmdLine.getOptionValue("table-column-formats", "") );
	        	this.setCommitInterval( cmdLine.getOptionValue("commit-interval", "") );
	        	this.setLoadMode( cmdLine.getOptionValue("load-mode", "") );
	        	
	        	// Logger
	        	logger.info("input-file-type: " + this.getInputFileType());
	        	logger.info("input-file-name: " + this.getInputFileName());
	        	logger.info("skip-header-rows: " + this.getSkipHeaderRows());
	        	logger.info("database-type: " + this.getDatabaseType());
	        	logger.info("database-url: " + this.getDatabaseUrl());
	        	logger.info("table-name: " + this.getTableName());
	        	logger.info("table-columns: " + this.getTableColumns());
	        	logger.info("table-column-formats: " + this.getTableColumnFormats());
	        	logger.info("commit-interval: " + this.getCommitInterval());
	        	logger.info("load-mode: " + this.getLoadMode());

	        	
	        	// Check arguments Options ...
	        	try {
	        		checkArgumentOptions();
	        	} catch (Exception e) {
	    			System.err.println(e.getMessage());
	    			System.exit(ERROR_LEVEL_SQLLOADER);
	        	}
	        	
	        	System.out.println(APP_NAME + " - "+ APP_VERSION);
	        	
	        }
			
		} catch (ParseException e) {
			System.err.println(e.getMessage());
            HelpFormatter formatter = new HelpFormatter(); 
            formatter.printHelp(APP_USAGE, options); 
			System.exit(ERROR_LEVEL_SQLLOADER);
		} 
        
	}

	//
	private void checkArgumentOptions() throws Exception {
		
		// Check input file type argument ...
		if (!inputFileType.equals("csv")) {
			throw new Exception(MSG_ERROR_FILE_TYPE_INVALID.replaceFirst("%s", inputFileType));
		}
		if (!databaseType.equals("") && !databaseType.equals("sqlserver")) {
			throw new Exception(MSG_ERROR_SQL_TYPE_INVALID.replaceFirst("%s", databaseType));
		}
		// Check input file name argument  ...
		if (!(new File(inputFileName).exists())) {
			throw new Exception(MSG_ERROR_FILE_NOT_FOUND.replaceFirst("%s", inputFileName));
		}
		
	}

	
	// Generate Getters and Setters - Begin ...

	public String getInputFileType() {
		return inputFileType;
	}

	public void setInputFileType(String inputFileType) {
		this.inputFileType = inputFileType;
	}

	public String getInputFileName() {
		return inputFileName;
	}

	public void setInputFileName(String inputFileName) {
		this.inputFileName = inputFileName;
	}

	public void setSkipHeaderRows(String optionValue) {
		if(!optionValue.contentEquals(""))
			this.skipHeaderRows = Integer.parseInt( optionValue );
		else
			this.skipHeaderRows = 0;
	}

	public Integer getSkipHeaderRows() {
		return skipHeaderRows;
	}

	public String getDatabaseUrl() {
		return databaseUrl;
	}

	public void setDatabaseUrl(String databaseUrl) {
		this.databaseUrl = databaseUrl;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public String getDatabaseType() {
		return databaseType;
	}

	public void setDatabaseType(String databaseType) {
		this.databaseType = databaseType;
	}

	public Integer getCommitInterval() {
		return commitInterval;
	}

	public void setCommitInterval(String optionValue) {
		if(!optionValue.contentEquals(""))
			this.commitInterval= Integer.parseInt( optionValue );
		else
			this.commitInterval = 0;
	}

	public String getTableColumns() {
		return tableColumns;
	}

	public void setTableColumns(String tableColumns) {
		this.tableColumns = tableColumns;
	}

	public String getTableColumnFormats() {
		return tableColumnFormats;
	}

	public void setTableColumnFormats(String tableColumnFormats) {
		this.tableColumnFormats = tableColumnFormats;
	}

	/**
	 * @return the loadMode
	 */
	public String getLoadMode() {
		return loadMode;
	}

	/**
	 * @param loadMode the loadMode to set
	 */
	public void setLoadMode(String loadMode) {
		this.loadMode = loadMode;
	}
	
	// Generate Getters and Setters - End.


}
