package org.gnu.sqlloader;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.apache.log4j.Logger;

public class SqlLoader {

	// Logger ...
	final static Logger logger = Logger.getLogger(SqlLoader.class);

	/*
	 * Constants ...
	 */
	private final static String SQLSERVER_JDBC_DRIVER = new String("com.microsoft.sqlserver.jdbc.SQLServerDriver");
	private static final String SQLSERVER_TRUNCATE_TABLE = new String("TRUNCATE TABLE %tablename");
	private static final String SQLSERVER_REBUILD_INDEX = new String( "ALTER INDEX ALL ON %tablename REBUILD");
	private static final String SQLSERVER_DBCC_CHECKIDENT = new String("DBCC CHECKIDENT (\"%tablename\", RESEED, 1)");
	private static final String SQLSERVER_INSERT_INTO_SQLSERVERLOADTABLE = new String("INSERT INTO %tablename ( %tablecolumns ) VALUES ( %tablevalues );");
	
	public final static String LOAD_MODE_REPLACE = new String("replace");

	
	/*
	 * Properties ...
	 */
	CliArgsParser cliArgsParser = null;
	

	/*
	 * Constructor 
	 */
	public SqlLoader(CliArgsParser cliArgsParser) throws Exception {
		super();
		
		// Setter cliArgsParser ...
		logger.info("SqlLoader Constructor()");
		this.cliArgsParser = cliArgsParser;
		
	}
	
	
	/*
	 * executeSqlLoader() 
	 */
	public void executeSqlLoader() throws Exception {
				
		// Connect database
		logger.info("doConnectSqlServer( " + cliArgsParser.getDatabaseUrl() + "," + SQLSERVER_JDBC_DRIVER );
		Connection conn = doConnectSqlServer( cliArgsParser.getDatabaseUrl(), SQLSERVER_JDBC_DRIVER );

		// Read, Split, Load Input File and Commit ...
		readSplitLoad(cliArgsParser, conn);
		conn.commit();
		
		// Disconnect database
		logger.info("doDisconnectSqlServer()" );
		doDisconnectSqlServer( conn );

		
	}
	
	
	/*
	 * readSplitLoad(inputFile) - Read file to count number of rows ...
	 */
	public void readSplitLoad(CliArgsParser cliArgsParser, Connection conn) throws Exception {
		
		// LoadMode = 'replace' ?
		if (cliArgsParser.getLoadMode().contentEquals(LOAD_MODE_REPLACE)) {
			logger.info("doTruncateTable(): " + cliArgsParser.getTableName() );
			doTruncateTable(conn, cliArgsParser.getTableName());
		}
		
		String line = new String("");
		long nrows = (long) 0;
		logger.info("InputStreamReader(" + cliArgsParser.getInputFileName() + ")" );
	    BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(cliArgsParser.getInputFileName()), "UTF-8"));
		System.out.println("Loading '%s' ...".replaceFirst("%s", cliArgsParser.getInputFileName()));
		
		while ((line = br.readLine()) != null) {
			nrows++;
			// Skip Headers rows?
			if (nrows > cliArgsParser.getSkipHeaderRows()) {
				// eachRowSplitInsertIntoDatabase ...
				try {
					eachRowSplitInsertIntoDatabase(nrows, line, conn, cliArgsParser);
				} catch (Exception e) {
					System.out.println("");
					System.out.println("Loading EXCEPTION ERROR on row: %s - Original line is:".replaceFirst("%s", String.valueOf(nrows)));
					System.out.println(line);
					e.printStackTrace();
				}
			} else {
				logger.info("row: skipping" );
			}
			// Show progress nrow ?
			if ( (nrows < 10) ||
				(nrows >= 10 && nrows < 100 && nrows % 10 == 0) ||
				(nrows >= 100 && nrows < 1000 && nrows % 100 == 0) ||
				(nrows >= 1000 && nrows < 10000 && nrows % 1000 == 0) ||
				(nrows >= 10000 && nrows < 100000 && nrows % 5000 == 0) ||
				(nrows >= 100000 && nrows % 10000 == 0)
				) {
					System.out.print("\rLoading - row: %s".replaceFirst("%s", String.valueOf(nrows) ));
			}
			// Commit interval ?
			if (cliArgsParser.getCommitInterval() == 0) {
				logger.info("conn.commit()");
				conn.commit();
			} else if (nrows % cliArgsParser.getCommitInterval() == 0 ) {
				logger.info("conn.commit()");
				conn.commit();
			}
		}
		br.close();
		System.out.println("");
		System.out.println("Loading - row: %s".replaceFirst("%s", String.valueOf(nrows)));
		System.out.println("Loading finished successfully!");
	}	
	
	
	/*
	 * doConnectSqlServer()
	 */
	private static Connection doConnectSqlServer(String databaseUrl, String jdbcDriver) throws ClassNotFoundException, SQLException {
		
		// Class.forName
		Class.forName(jdbcDriver);
		
		//
		// DriverManager.getConnection ...
		//
		Connection conn = DriverManager.getConnection(databaseUrl);
		
		//
		// Return
		//
		return conn;
		
	}
	

	/*
	 * doTruncateTable()
	 */
	private static void doTruncateTable(Connection conn, String sTableName) throws SQLException {

		String strSql = new String("");
		PreparedStatement prepStmt = null;
		
		// Prepared Statement and Execute ...
		System.out.println("Truncate table '%s' ...".replaceFirst("%s", sTableName));
		strSql = new String(SQLSERVER_TRUNCATE_TABLE.replaceAll("%tablename", sTableName));
		prepStmt = conn.prepareStatement(strSql);
		prepStmt.execute();
        
		// Prepared Statement and Execute ...
		System.out.println("Rebuild indexes '%s' ...".replaceFirst("%s", sTableName));
		strSql = new String(SQLSERVER_REBUILD_INDEX.replaceAll("%tablename", sTableName));
		prepStmt = conn.prepareStatement(strSql);
		prepStmt.execute();
        
		// Prepared Statement and Execute ...
		System.out.println("Initialize sequence '%s' ...".replaceFirst("%s", sTableName));
		strSql = new String(SQLSERVER_DBCC_CHECKIDENT.replaceAll("%tablename", sTableName));
		prepStmt = conn.prepareStatement(strSql);
		prepStmt.execute();
        
        // Close ...
        prepStmt.close();
	}

	
	/*
	 * doDisconnectSqlServer()
	 */
	private static void doDisconnectSqlServer(Connection conn) throws SQLException {

		//
		// Force last commit
		//
		conn.commit();
		
		//
		// DriverManager.getConnection ...
		//
		conn.close();
	}

	
	/*
	 * eachRowSplitCsvInsertIntoDatabase()
	 */
	public void eachRowSplitInsertIntoDatabase(long nrows, String line, Connection conn, CliArgsParser cliArgsParser) throws SQLException {
		String strTableColumns = new String("");
		String strTableValues = new String("");
		String[] aTableColumnsArgs = cliArgsParser.getTableColumns().split("-");
		String[] aTableValuesLine = line.split(";");
		String[] aTableColumnFormats = cliArgsParser.getTableColumnFormats().split("-");
		for(int i=0; i<aTableColumnsArgs.length; i++) {
			if (aTableColumnsArgs[i]!=null) {
				if (!aTableColumnsArgs[i].contentEquals("") && i < aTableValuesLine.length) {
					if (!strTableColumns.contentEquals("")) {
						strTableColumns = new String(strTableColumns.concat(", "));
						strTableValues = new String(strTableValues.concat(", "));
					}
					String strEnquoteString = new String("'");
					if(i < aTableColumnFormats.length) {
						if (aTableColumnFormats[i].contentEquals("d") || aTableColumnFormats[i].contentEquals("f")) {
							strEnquoteString = new String("");
						}
					}
					strTableColumns = new String(strTableColumns.concat(aTableColumnsArgs[i]));
					// NULL value not enquoted?
					if (strEnquoteString.contentEquals("") && aTableValuesLine[i].contentEquals("") ) {
						strTableValues = new String(strTableValues.concat("NULL"));
					} else {
						strTableValues = new String(strTableValues.concat(strEnquoteString).concat(aTableValuesLine[i].replaceAll("'", "''")).concat(strEnquoteString));
					}
				}
			}
		}
		// Prepared Statement and ...
		String strSql = new String(SQLSERVER_INSERT_INTO_SQLSERVERLOADTABLE.replace("%tablename", cliArgsParser.getTableName()).replace("%tablecolumns", strTableColumns).replace("%tablevalues", strTableValues));
		PreparedStatement prepStmt = conn.prepareStatement(strSql);
		
        // Execute prepared statement ...
		logger.info("row " + nrows + ": " + strSql);
        prepStmt.execute();
        
        // Close ...
        prepStmt.close();

	}

		
}
