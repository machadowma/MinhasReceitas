package com.github.machadowma.minhasreceitas;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class ReceitaAddActivity extends AppCompatActivity {
    public EditText editTextNomeReceita,editTextPreparoReceita;
    public Button buttonAddReceita;
    public SQLiteDatabase bancoDados;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receita_add);

        editTextNomeReceita = (EditText) findViewById(R.id.editTextNomeReceita);
        editTextPreparoReceita = (EditText) findViewById(R.id.editTextPreparoReceita);
        buttonAddReceita = (Button) findViewById(R.id.buttonAddReceita);

        buttonAddReceita.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cadastrar();
            }
        });
    }

    public void cadastrar(){
        String valueNome = null;
        String valuePreparo = null;
        if(TextUtils.isEmpty(editTextNomeReceita.getText().toString())){
            editTextNomeReceita.setError("Este campo é obrigatório");
            return;
        } else {
            valueNome = editTextNomeReceita.getText().toString();
        }

        if(!TextUtils.isEmpty(editTextPreparoReceita.getText().toString())){
            valuePreparo = editTextPreparoReceita.getText().toString();
        }

        try {
            bancoDados = openOrCreateDatabase("minhasreceitas", MODE_PRIVATE, null);
            String sql = "INSERT INTO receita (nome,modo_preparo) VALUES (?,?)";
            SQLiteStatement stmt = bancoDados.compileStatement(sql);

            stmt.bindString(1, valueNome);

            if(valuePreparo==null){
                stmt.bindNull(2);
            } else {
                stmt.bindString(2, valuePreparo);
            }

            stmt.executeInsert();
            bancoDados.close();
            finish();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
