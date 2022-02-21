package com.titoufu.apptanquinho;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.net.URI;

import util.Comando;
import util.ModoComunica;
import util.ModoOperacao;
import util.Nivel;
import util.Modo;

public class MainActivity extends AppCompatActivity {
    // Esta linha busca a raiz do Banco de Dados no Firebase !!!!
    private DatabaseReference referencia = FirebaseDatabase.getInstance().getReference();
    // RadioButton
    private RadioGroup opcaoPrograma;
    private RadioGroup opcaoNivel;
    private Button botaoLigar;
    ModoOperacao modoOperacao = new ModoOperacao();
    ModoComunica modoComunica = new ModoComunica();
    private AlertDialog alerta;
    final Boolean[] flag = {false};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //  referencia.child("pontos").setValue("100");  // Adiciona  à raiz um filho chamado "pontos" e atribui a ele um valor de 100.
        //referencia.child("usuarios").child("tito").setValue("O bom do ano 2000");  // outro jeito

        opcaoPrograma = findViewById(R.id.radioGroupPrograma);
        opcaoNivel = findViewById(R.id.radioGroupNivel);
        botaoLigar = findViewById(R.id.idBtnLigar);
        AlertDialog.Builder builder;
        radioButton();

        referencia.child("Comunica").addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ModoComunica resposta = dataSnapshot.getValue(ModoComunica.class);
                String str1 = resposta.getResposta();
                String str2 = "LIGAR: SIM ou NÃO ?";
                if (str1.compareTo(str2) == 0) {
                    alerta();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void radioButton() {
        opcaoPrograma.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.idCicloLOngo) {
                    modoOperacao.setPrograma(Modo.CICLO_LONGO.toString());
                }
                if (checkedId == R.id.idCicloMedio) {
                    modoOperacao.setPrograma(Modo.CICLO_MEDIO.toString());
                }
                if (checkedId == R.id.idCicloCurto) {
                    modoOperacao.setPrograma(Modo.CICLO_CURTO.toString());
                }
                if (checkedId == R.id.idCicloEncher) {
                    modoOperacao.setPrograma(Modo.CICLO_ENCHER.toString());
                }
                if (checkedId == R.id.idCicloEsvaziar) {
                    modoOperacao.setPrograma(Modo.CICLO_ESVAZIAR.toString());
                }

            }
        });
        opcaoNivel.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedNivel) {
                if (checkedNivel == R.id.idNivelAlto) {
                    modoOperacao.setNivel((Nivel.ALTO.toString()));
                }
                if (checkedNivel == R.id.idNivelMedio) {
                    modoOperacao.setNivel((Nivel.MEDIO.toString()));
                }
                if (checkedNivel == R.id.IdNivelBaixo) {
                    modoOperacao.setNivel((Nivel.BAIXO.toString()));
                }
            }
        });
    }

    public void Ligar(View view) {
        modoComunica.setComando(Comando.LIGAR.toString());
        modoComunica.setResposta(Comando.SIM.toString());
        referencia.child("Modo").setValue(modoOperacao);
        referencia.child("Comunica").setValue(modoComunica);
    }


    private void alerta() {
        //Cria o gerador do AlertDialog

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        //define o titulo
        builder.setTitle("Sistema de Comando do Tanquinho");
        //define a mensagem
        builder.setMessage("DESEJA REALMENTE LIGAR ?");
        //define um botão como positivo
        builder.setPositiveButton("SIM", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
                Toast.makeText(MainActivity.this, "Ligando ...", Toast.LENGTH_LONG).show();
                modoComunica.setComando(Comando.SIM.toString());
                modoComunica.setResposta(Comando.SIM.toString());
                referencia.child("Comunica").setValue(modoComunica);
            }
        });
        //define um botão como negativo.
        builder.setNegativeButton("NÃO", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
                Toast.makeText(MainActivity.this, "Desligando ...", Toast.LENGTH_LONG).show();
                modoComunica.setComando(Comando.DESLIGAR.toString());
                modoComunica.setResposta(Comando.AGUARDANDO.toString());
                referencia.child("Comunica").setValue(modoComunica);
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}


