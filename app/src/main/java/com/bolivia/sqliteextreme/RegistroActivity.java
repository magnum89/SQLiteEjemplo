package com.bolivia.sqliteextreme;

import java.util.ArrayList;
import com.bolivia.sqliteextreme.db.SQLite;
//
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class RegistroActivity extends Activity implements OnClickListener {
	//
	private TextView textView;
	private Button btnNuevo;
	private Button btnDelete;
	private Button btnLista;
	private SQLite sqlite;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_registro);
		//
		textView = (TextView) findViewById( R.id.txtResultado );
		btnNuevo = (Button) findViewById( R.id.btnNuevo );
		btnDelete = (Button) findViewById( R.id.btnDelete );
		btnLista = (Button) findViewById( R.id.btnLista );
		btnNuevo.setOnClickListener( this );
		btnLista.setOnClickListener( this );
		btnDelete.setOnClickListener( this );
		textView.setTextSize(28);
		//Recupera parametro ID de registro
		textView.setText( ""  );
		Intent i = getIntent();
		Bundle bundle = i.getExtras();
		if ( bundle != null ) {
			 int id = bundle.getInt("id");
			 //base de datos
			 sqlite = new SQLite( this );		
			 sqlite.abrir();
			 Cursor cursor = sqlite.getRegistro(id);
			 ArrayList<String> reg = sqlite.getFormatListUniv(cursor);
			 textView.setText( reg.get(0)  );
		 }		
		//
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.registro, menu);
		return true;
	}

	@Override
	public void onClick(View v ) {
		switch ( v.getId() ) 
		{
			case R.id.btnNuevo:
				Intent iMain = new Intent( RegistroActivity.this, MainActivity.class );				
				startActivity( iMain );
				break;
			case R.id.btnDelete:
				//Muestra una ventana de dialo para confirmar eliminacion de registro
				new AlertDialog.Builder(this)
				.setIcon(android.R.drawable.ic_dialog_alert)
		        .setTitle("Eliminar")
		        .setMessage("En verdad desea eliminar este registro?")
		        .setPositiveButton("Si", new DialogInterface.OnClickListener() {

		            @Override
		            public void onClick(DialogInterface dialog, int which) {		
						//Se extrae el ID = [X]		            	
						int posicionInicial = textView.getText().toString().indexOf("[") + 1; 
						int posicionFinal = textView.getText().toString().indexOf("]",posicionInicial); 
						String resultado =  textView.getText().toString().substring(posicionInicial, posicionFinal);		            	
		            	//Elimina registro y abre activity RegistrosActivity.class
		            	if(sqlite.borrar_registro( Integer.valueOf(resultado) ))
		            	{
		            		goRegistrosActivity();	
		            	}		            	
		            }

		        })
		        .setNegativeButton("No", null)
		        .show();
				
				break;
			case R.id.btnLista:
				goRegistrosActivity();
				break;
		}
	}

	/**
	 * Abre nueva Acivity = RegistrosActivity
	 * */
	public void goRegistrosActivity()
	{
		Intent iRegs = new Intent( RegistroActivity.this, RegistrosActivity.class );				
		startActivity( iRegs );
	}
	
}
