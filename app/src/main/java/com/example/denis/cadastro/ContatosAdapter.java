package com.example.denis.cadastro;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class ContatosAdapter extends BaseAdapter{

    private Context context;
    private List<Contato> lista;

    //construtor pra carregar a lista
    public ContatosAdapter(Context context2, List<Contato> lista2){
        context = context2;
        lista = lista2;
    }

    @Override
    public int getCount() {
        return lista.size();
    }

    @Override
    public Contato getItem(int position) {
        return lista.get(position);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v;

        if(convertView == null){
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            v = inflater.inflate(R.layout.item_lista, null);
        }else{
            v = convertView;
        }

        Contato c = getItem(position);

        //declarando os itens da lista
        TextView itemNome = v.findViewById(R.id.itemNome);
        TextView itemTelefone = v.findViewById(R.id.itemTelefone);
        TextView itemEmail = v.findViewById(R.id.itemEmail);

        //passando os valores para os itens da lista
        itemNome.setText(c.getNome());
        itemTelefone.setText(c.getTelefone());
        itemEmail.setText(c.getEmail());

        return v;
    }
}
