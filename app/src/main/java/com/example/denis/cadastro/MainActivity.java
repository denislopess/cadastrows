package com.example.denis.cadastro;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    EditText editNome, editTelefone, editEmail, editId;
    Button btnNovo, btnSalvar, btnExcluir;
    ListView listViewContatos;

    ContatosAdapter contatosAdapter;
    List<Contato> lista;

    private String HOST = "https://denislopesdbcom.000webhostapp.com/webservice";

    private int itemClicado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editNome = findViewById(R.id.editNome);
        editTelefone = findViewById(R.id.editTelefone);
        editEmail = findViewById(R.id.editEmail);
        editId = findViewById(R.id.editId);

        btnNovo = findViewById(R.id.btnNovo);
        btnSalvar = findViewById(R.id.btnSalvar);
        btnExcluir = findViewById(R.id.btnExcluir);

        listViewContatos = findViewById(R.id.listViewContatos);

        lista = new ArrayList<Contato>();
        contatosAdapter = new ContatosAdapter(MainActivity.this, lista);

        listViewContatos.setAdapter(contatosAdapter);


        listaContatos();



        btnNovo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                limpaCampos();
            }
        });

        //criando evento pra salvar contato
        btnSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //variaveis pra pegar os valores dos campos
                final String id = editId.getText().toString();
                final String nome = editNome.getText().toString();
                final String telefone = editTelefone.getText().toString();
                final String email = editEmail.getText().toString();



                if(nome.isEmpty()){
                    editNome.setError("Campo obrigatório");
                }else if(id.isEmpty()){
                    //fazer CREATE
                    String url = HOST + "/create.php";

                    Ion.with(MainActivity.this)
                            .load(url)
                            .setBodyParameter("nome", nome)
                            .setBodyParameter("telefone", telefone)
                            .setBodyParameter("email", email)
                            .asJsonObject()
                            .setCallback(new FutureCallback<JsonObject>() {
                                @Override
                                public void onCompleted(Exception e, JsonObject result) {

                                    //retorno da aplicação - JSON
                                    if(result.get("CREATE").getAsString().equals("OK")){

                                        int idRetornado = Integer.parseInt(result.get("ID").getAsString());

                                        Contato c = new Contato();
                                        c.setId(idRetornado);
                                        c.setNome(nome);
                                        c.setTelefone(telefone);
                                        c.setEmail(email);

                                        lista.add(c);

                                        contatosAdapter.notifyDataSetChanged();

                                        limpaCampos();

                                        //Toast.makeText(MainActivity.this,"Salvo com sucesso, Id " + idRetornado,Toast.LENGTH_LONG).show();

                                        Toast.makeText(MainActivity.this,"Salvo com sucesso, Id " + idRetornado,Toast.LENGTH_LONG).show();
                                    }else{
                                        Toast.makeText(MainActivity.this,"Ocorreu algum erro!",Toast.LENGTH_LONG).show();
                                    }
                                }
                            });

                }else{
                    //fazer UPDATE caso o ID existir

                    String url = HOST + "/update.php";

                    Ion.with(MainActivity.this)
                            .load(url)
                            .setBodyParameter("id", id)
                            .setBodyParameter("nome", nome)
                            .setBodyParameter("telefone", telefone)
                            .setBodyParameter("email", email)
                            .asJsonObject()
                            .setCallback(new FutureCallback<JsonObject>() {
                                @Override
                                public void onCompleted(Exception e, JsonObject result) {

                                    //retorno da aplicação - JSON
                                    if(result.get("UPDATE").getAsString().equals("OK")){

                                        //instanciando a classe Contato
                                        Contato c = new Contato();
                                        c.setId(Integer.parseInt(id));
                                        c.setNome(nome);
                                        c.setTelefone(telefone);
                                        c.setEmail(email);

                                        //função quando clicar no item da lista enviar o texto para os campos
                                        lista.set(itemClicado, c);

                                        contatosAdapter.notifyDataSetChanged();

                                        limpaCampos();

                                        Toast.makeText(MainActivity.this,"Atualizado com sucesso",Toast.LENGTH_LONG).show();
                                    }else{
                                        Toast.makeText(MainActivity.this,"Ocorreu algum erro ao atualizar!",Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                }
            }


        });

        btnExcluir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String id = editId.getText().toString();

                if(id.isEmpty()){
                    Toast.makeText(MainActivity.this,"Nenhum contato está selecionado!",Toast.LENGTH_LONG).show();
                }else{
                    //Tentar Apagar o Contato
                    String url = HOST + "/delete.php";

                    Ion.with(MainActivity.this)
                            .load(url)
                            .setBodyParameter("id", id)
                            .asJsonObject()
                            .setCallback(new FutureCallback<JsonObject>() {
                                @Override
                                public void onCompleted(Exception e, JsonObject result) {

                                    //retorno da aplicação - JSON
                                    if(result.get("DELETE").getAsString().equals("OK")){

                                        lista.remove(itemClicado);

                                        contatosAdapter.notifyDataSetChanged();

                                        limpaCampos();

                                        Toast.makeText(MainActivity.this,"Excluido com sucesso",Toast.LENGTH_LONG).show();
                                    }else{
                                        Toast.makeText(MainActivity.this,"Ocorreu algum erro ao excluir!",Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                }
            }
        });

        listViewContatos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {

                Contato c = (Contato) adapterView.getAdapter().getItem(position);

                editId.setText(String.valueOf(c.getId()));
                editNome.setText(c.getNome());
                editTelefone.setText(c.getTelefone());
                editEmail.setText(c.getEmail());

                itemClicado = position;
            }
        });

    }

    private void limpaCampos() {
        editId.setText("");
        editNome.setText("");
        editTelefone.setText("");
        editEmail.setText("");

        editNome.requestFocus();
    }

    private void listaContatos(){

        String url = HOST + "/read.php";

        Ion.with(getBaseContext())
                .load(url)
                .asJsonArray()
                .setCallback(new FutureCallback<JsonArray>() {
                    @Override
                    public void onCompleted(Exception e, JsonArray result) {

                        for (int i = 0; i < result.size(); i++){

                            JsonObject obj = result.get(i).getAsJsonObject();

                            Contato c = new Contato();

                            c.setId(obj.get("id").getAsInt());
                            c.setNome(obj.get("nome").getAsString());
                            c.setTelefone(obj.get("telefone").getAsString());
                            c.setEmail(obj.get("email").getAsString());

                            lista.add(c);
                        }


                    }
                });
    }
}
