package com.github.machadowma.minhasreceitas;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class IngredienteAddActivity extends AppCompatActivity {
    private SQLiteDatabase bancoDados;
    EditText editTextDescIngrediente;
    Button buttonAddIngrediente;
    Integer id_receita;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ingrediente_add);

        editTextDescIngrediente = (EditText) findViewById(R.id.editTextDescIngrediente);
        buttonAddIngrediente = (Button) findViewById(R.id.buttonAddIngrediente);
        Intent intent = getIntent();
        id_receita = intent.getIntExtra("id_receita",0);

        buttonAddIngrediente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cadastrar();
            }
        });
    }

    public void cadastrar(){
        String valueDescricao = null;
        if(TextUtils.isEmpty(editTextDescIngrediente.getText().toString())){
            editTextDescIngrediente.setError("Este campo é obrigatório");
            return;
        } else {
            valueDescricao = editTextDescIngrediente.getText().toString();
        }

        try {
            bancoDados = openOrCreateDatabase("minhasreceitas", MODE_PRIVATE, null);
            String sql = "INSERT INTO ingrediente (descricao,id_receita) VALUES (?,?)";
            SQLiteStatement stmt = bancoDados.compileStatement(sql);
            stmt.bindString(1, valueDescricao);
            stmt.bindLong(2, id_receita);
            stmt.executeInsert();
            bancoDados.close();
            finish();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
