package com.bolivia.sqliteextreme.db;
//
import java.util.ArrayList;
//
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class SQLite {

	private SQLiteHelper sqliteHelper;
	private SQLiteDatabase db;	
	
	/** Constructor de clase */
	public SQLite(Context context)
	{
		sqliteHelper = new SQLiteHelper( context );
	}

	/** Abre conexion a base de datos */
	public void abrir(){
		Log.i("SQLite", "Se abre conexion a la base de datos " + sqliteHelper.getDatabaseName() );
		db = sqliteHelper.getWritableDatabase(); 		
	}
	
	/** Cierra conexion a la base de datos */
	public void cerrar()
	{
		Log.i("SQLite", "Se cierra conexion a la base de datos " + sqliteHelper.getDatabaseName() );
		sqliteHelper.close();		
	}
	
	/**
	 * Metodo para agregar un nuevo registro
	 * @param String nombre Nombre completo
	 * @param String fecha fecha de nacimiento de la forma 12/05/1900
	 * @param String pais
	 * @param String sexo
	 * @param String ingles si habla ingles
	 * @return BOOLEAN TRUE si tuvo exito FALSE caso contrario
	 * */
	public boolean addRegistro( String nombre, String fecha, String pais, String sexo, String ingles )
	{		
		if( nombre.length()> 0 )
		{
			ContentValues contentValues = new ContentValues();
			contentValues.put( sqliteHelper.__campo_nombre , nombre);
			contentValues.put( sqliteHelper.__campo_fechanac , fecha  );
			contentValues.put( sqliteHelper.__campo_pais , pais);
			contentValues.put( sqliteHelper.__campo_sexo , sexo);
			contentValues.put( sqliteHelper.__campo_ingles , ingles);
			Log.i("SQLite", "Nuevo registro " );
			return ( db.insert( sqliteHelper.__tabla__ , null, contentValues ) != -1 )?true:false;			
		}
		else
			return false; 
	}
	
	
	/**
	 * Metodo que retorna el ID del ultimo universitario registrado
	 * @return integer ID -1 si no existen registros
	 * */
	public int getUltimoID()
	{
		int id = -1;
		//query(String table, 
		//String[] columns, 
		//String selection, String[] selectionArgs, String groupBy, String having, 
		//String orderBy, String limit)
		Cursor cursor = db.query( sqliteHelper.__tabla__ , 
				new String[]{ sqliteHelper.__campo_id },
				null, null, null,null,
				sqliteHelper.__campo_id + " DESC ", "1");
		if( cursor.moveToFirst() )
		{
			do
			{
				id = cursor.getInt(0);
			} while ( cursor.moveToNext() );
		}
		return id;
	}

	/**
	 * @param INT ID del registro a eliminar
	 * @return BOOLEAN
	 * */
	public boolean borrar_registro( int id )
	{
		//table , whereClause, whereArgs
		return  (db.delete( sqliteHelper.__tabla__ , sqliteHelper.__campo_id + " = " + id ,  null) > 0) ? true:false;
		
	}
	/**
	 * Obtiene todos los registros de la unica tabla de la base de datos
	 * @return Cursor
	 * */
	public Cursor getRegistros()
	{
		return db.query( sqliteHelper.__tabla__ ,				
					new String[]{
							sqliteHelper.__campo_id ,
							sqliteHelper.__campo_nombre,
							sqliteHelper.__campo_fechanac,
							sqliteHelper.__campo_pais,
						    sqliteHelper.__campo_sexo,
							sqliteHelper.__campo_ingles
								  }, 
				null, null, null, null, null);
	}


	/**
	 * Obtiene un registro
	 * */
	public Cursor getRegistro( int id )
	{
		return db.query( sqliteHelper.__tabla__ ,				
					new String[]{
							sqliteHelper.__campo_id ,
							sqliteHelper.__campo_nombre,
							sqliteHelper.__campo_fechanac,
							sqliteHelper.__campo_pais,
						    sqliteHelper.__campo_sexo,
							sqliteHelper.__campo_ingles
								  }, 
                sqliteHelper.__campo_id + " = " + id , 
				null, null, null, null);
	}
	
	/**
	 * Dado un Cursor con los registros de la base de datos, da formato y retorna resultado
	 * @return ArrayList<String>
	 * */
	public ArrayList<String> getFormatListUniv( Cursor cursor )
	{
		ArrayList<String> listData = new ArrayList<String>();
		String item = "";
			if( cursor.moveToFirst() )
			{
				do
				{
					item += "ID: [" + cursor.getInt(0) + "]\r\n";
					item += "Nombre: " + cursor.getString(1) + "\r\n";
					item += "Fecha de Nacimiento: " + cursor.getString(2) + "\r\n";
					item += "Pais: " + cursor.getString(3) + "\r\n";
					item += "Sexo: " + cursor.getString(4) + "\r\n";
					item += "Habla ingles: " + cursor.getString(5) + "";
					listData.add( item );
					item="";
		            
				} while ( cursor.moveToNext() );
			}		
		return listData;		
	}
	
}
