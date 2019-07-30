package com.github.machadowma.minhasreceitas;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private SQLiteDatabase bancoDados;
    public ListView listViewReceitas;
    public ArrayList<Integer> arrayIds;
    public Integer idSelecionado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                abrirCadastro();
            }
        });

        listViewReceitas = (ListView) findViewById(R.id.listViewReceitas);

        listViewReceitas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                alterarReceita(i);
            }
        });

        criarBancoDados();
        listarReceitas();
    }

    @Override
    protected void onResume() {
        super.onResume();
        listarReceitas();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void criarBancoDados(){
        try {
            bancoDados = openOrCreateDatabase("minhasreceitas", MODE_PRIVATE, null);
            //bancoDados.execSQL("DROP TABLE ingrediente");
            //bancoDados.execSQL("DROP TABLE receita");
            bancoDados.execSQL("CREATE TABLE IF NOT EXISTS receita(" +
                    " id INTEGER PRIMARY KEY AUTOINCREMENT" +
                    " , nome VARCHAR" +
                    " , modo_preparo VARCHAR)");
            bancoDados.execSQL("CREATE TABLE IF NOT EXISTS ingrediente(" +
                    " id INTEGER PRIMARY KEY AUTOINCREMENT" +
                    " , descricao VARCHAR" +
                    " , id_receita INTEGER" +
                    " , FOREIGN KEY(id_receita) REFERENCES receita(id))");
            bancoDados.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void listarReceitas(){
        try {
            bancoDados = openOrCreateDatabase("minhasreceitas", MODE_PRIVATE, null);
            Cursor cursor = bancoDados.rawQuery("SELECT id,nome FROM receita",null);
            ArrayList<String> linhas = new ArrayList<String>();
            ArrayAdapter adapter = new ArrayAdapter<String>(
                    this,
                    android.R.layout.simple_list_item_1,
                    android.R.id.text1,
                    linhas
            );
            listViewReceitas.setAdapter(adapter);
            cursor.moveToFirst();
            arrayIds = new ArrayList<Integer>();
            while(cursor!=null){
                arrayIds.add(cursor.getInt(cursor.getColumnIndex("id")));
                linhas.add(cursor.getString(1));
                cursor.moveToNext();
            }
            bancoDados.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void abrirCadastro(){
        Intent intent = new Intent(this,ReceitaAddActivity.class);
        startActivity(intent);
    }

    public void alterarReceita(Integer i){
        idSelecionado = arrayIds.get(i);
        Intent intent = new Intent(this,ReceitaEditActivity.class);
        intent.putExtra("id_receita",idSelecionado);
        startActivity(intent);
    }
}
