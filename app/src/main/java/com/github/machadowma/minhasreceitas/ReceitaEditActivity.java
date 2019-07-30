package com.github.machadowma.minhasreceitas;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class ReceitaEditActivity extends AppCompatActivity {
    public SQLiteDatabase bancoDados;
    public EditText editTextNomeReceita,editTextPreparoReceita;
    public ListView listViewIngredientes;
    public Button buttonAddIngrediente,buttonEditReceita;
    Integer id_receita;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receita_edit);

        editTextNomeReceita = (EditText) findViewById(R.id.editTextNomeReceita);
        editTextPreparoReceita = (EditText) findViewById(R.id.editTextPreparoReceita);
        listViewIngredientes = (ListView) findViewById(R.id.listViewIngredientes);
        buttonAddIngrediente = (Button) findViewById(R.id.buttonAddIngrediente);
        buttonEditReceita = (Button) findViewById(R.id.buttonEditReceita);
        Intent intent = getIntent();
        id_receita = intent.getIntExtra("id_receita",0);
        carregarDados();


        buttonAddIngrediente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                adicionarIngrediente();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        carregarDados();
    }

    public void carregarDados(){

        try {
            bancoDados = openOrCreateDatabase("minhasreceitas", MODE_PRIVATE, null);

            Cursor cursor = bancoDados.rawQuery("SELECT id,nome,modo_preparo " +
                    "FROM receita " +
                    "WHERE id = "+id_receita.toString(),null);
            cursor.moveToFirst();
            editTextNomeReceita.setText(cursor.getString(cursor.getColumnIndex("nome")));
            editTextPreparoReceita.setText(cursor.getString(cursor.getColumnIndex("modo_preparo")));
            bancoDados.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        listarIngredientes();
    }

    public void listarIngredientes(){
        try {
            bancoDados = openOrCreateDatabase("minhasreceitas", MODE_PRIVATE, null);
            Cursor cursor = bancoDados.rawQuery("SELECT id,descricao " +
                    "FROM ingrediente " +
                    "WHERE id_receita = "+id_receita.toString(),null);
            ArrayList<String> linhas = new ArrayList<String>();
            ArrayAdapter adapter = new ArrayAdapter<String>(
                    this,
                    android.R.layout.simple_list_item_1,
                    android.R.id.text1,
                    linhas
            );
            listViewIngredientes.setAdapter(adapter);
            cursor.moveToFirst();
            while(cursor!=null){
                linhas.add(cursor.getString(1));
                cursor.moveToNext();
            }
            bancoDados.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void adicionarIngrediente(){
        Intent intent = new Intent(this,IngredienteAddActivity.class);
        intent.putExtra("id_receita",id_receita);
        startActivity(intent);
    }
}
