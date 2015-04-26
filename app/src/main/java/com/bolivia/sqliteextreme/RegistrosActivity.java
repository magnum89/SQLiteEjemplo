package com.bolivia.sqliteextreme;

import java.util.ArrayList;
import com.bolivia.sqliteextreme.db.SQLite;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class RegistrosActivity extends Activity implements OnItemClickListener{

	//
	private ListView listView;
	private ArrayAdapter<String> adaptador ;	
	private SQLite sqlite;	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_registros);
		//		
		listView = (ListView) findViewById( R.id.lstRegistros );
		//Abre conexion a sqlite
		sqlite = new SQLite( this );
		sqlite.abrir();
		//obtiene registros e imprimir en el listview
		Cursor cursor = sqlite.getRegistros();
		ArrayList<String> listData = sqlite.getFormatListUniv( cursor );		
		adaptador = new ArrayAdapter<String>( this ,android.R.layout.simple_list_item_1  , listData );
		listView.setAdapter( adaptador );		
		listView.setOnItemClickListener( this );
		
		if( listData.size()== 0 )
		{
			Toast.makeText(getBaseContext(), "No existen registros"  ,Toast.LENGTH_SHORT).show();
		}
		//
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.registros, menu);
		return true;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		//
		Object object = listView.getItemAtPosition( position );
		//Se extrae el ID = [X] 
		int posicionInicial = object.toString().indexOf("[") + 1; 
		int posicionFinal = object.toString().indexOf("]",posicionInicial); 
		String resultado =  object.toString().substring(posicionInicial, posicionFinal);		
		//ejecuta nueva actividad
		Bundle b = new Bundle();
		b.putInt("id", Integer.valueOf(resultado) );
		Intent iRegs = new Intent( RegistrosActivity.this, RegistroActivity.class );	
		iRegs.putExtras(b);
		startActivity( iRegs );	
	}

	
}
