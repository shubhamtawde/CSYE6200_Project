package database;

public class SqlQueries {
	
	public static final String LOGIN_QUERY = "SELECT * FROM user WHERE username = ? AND password = ? AND type = ?;";
	
	public static final String GET_NAME_QUERY = "SELECT fullname FROM user WHERE username = ?";
	
	public static final String CREATE_APPOINTMENT_TABLE = "CREATE TABLE IF NOT EXISTS \"appointment\" (\n"
			+ "	\"id\"	INTEGER NOT NULL UNIQUE,\n"
			+ "	\"username\"	TEXT,\n"
			+ "	\"fullname\"	TEXT,\n"
			+ "	\"phone\"	TEXT,\n"
			+ "	\"email\"	TEXT,\n"
			+ "	\"gender\"	TEXT,\n"
			+ "	\"DOB\"	TEXT,\n"
			+ "	\"address\"	TEXT,\n"
			+ "	\"AD\"	TEXT,\n"
			+ "	\"issue\"	TEXT,\n"
			+ "	\"status\"	TEXT,\n"
			+ "	\"prescription\"	TEXT,\n"
			+ "	PRIMARY KEY(\"id\" AUTOINCREMENT),\n"
			+ "	FOREIGN KEY(\"username\") REFERENCES \"user\"(\"username\")\n"
			+ ");";
	
	public static final String CREATE_USER_TABLE = "CREATE TABLE IF NOT EXISTS \"user\" (\n"
			+ "	\"id\"	INTEGER NOT NULL UNIQUE,\n"
			+ "	\"username\"	TEXT NOT NULL UNIQUE,\n"
			+ "	\"fullname\"	TEXT NOT NULL,\n"
			+ "	\"password\"	TEXT,\n"
			+ "	\"email\"	TEXT,\n"
			+ "	\"phone\"	TEXT,\n"
			+ "	\"address\"	TEXT,\n"
			+ "	\"dob\"	TEXT,\n"
			+ "	\"type\"	TEXT,\n"
			+ "	\"enroll\"	TEXT,\n"
			+ "	\"qualification\"	TEXT,\n"
			+ "	\"expertise\"	TEXT,\n"
			+ "	\"gender\"	TEXT,\n"
			+ "	PRIMARY KEY(\"id\" AUTOINCREMENT)\n"
			+ ");";
}
