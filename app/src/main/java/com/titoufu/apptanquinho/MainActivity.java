package com.titoufu.apptanquinho;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import util.Modo;
import util.ModoOperacao;
import util.Nivel;
import util.Tanque;

public class MainActivity extends AppCompatActivity {
    // Esta linha busca a raiz do Banco de Dados no Firebase !!!!
    private final DatabaseReference referencia = FirebaseDatabase.getInstance().getReference("Tanque");
    private final DatabaseReference resposta = referencia.child("resposta");
    /* Initialize Firebase Auth
    private final FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseAuth.AuthStateListener mAuthListener;
     */

    private RadioGroup opcaoPrograma;
    private RadioGroup opcaoNivel;
    private Button botao;
    String str4 = "?";
    boolean flagInicializa = true;
    ModoOperacao modoOperacao = new ModoOperacao();
    private AlertDialog alerta;
    private Tanque tanque = new Tanque();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        opcaoPrograma = findViewById(R.id.radioGroupPrograma);
        opcaoNivel = findViewById(R.id.radioGroupNivel);
        botao = findViewById(R.id.idBtnLigar);
        if (flagInicializa) radioButtonInitialize();
        flagInicializa = false;

        //////////////////// -  Ouvindo mudança na child "resposta" //////////////////////
        resposta.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                /*
                 para ler um objeto inteiro (Tanque, poer exemplo:)
                Tanque tanque=dataSnapshot.getValue(Tanque.class);
                */
                String str1 = dataSnapshot.getValue(String.class);
                String str2 = "LIGAR: SIM ou NÃO ?";
                String str3 = "LIGADO";
                String str4 = "CICLO ENCERRADO";
                if (str1.compareTo(str2) == 0) {
                    botao.setBackgroundColor(R.color.botao_liberado);
                    alerta();
                } else if (str1.compareTo(str3) == 0) {
                    botao.setBackgroundColor(getColor(R.color.botao_travado));
                    botao.setText("Tanquinho Ligado");
                } else if (str1.compareTo(str4) == 0) {
                    botao.setBackgroundColor(getColor(R.color.botao_liberado));
                    botao.setText("Ligar Tanquinho");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        //////////////////


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

        //////////

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


    public void radioButtonInitialize() {
        modoOperacao.setNivel((Nivel.BAIXO.toString()));
        modoOperacao.setPrograma(Modo.CICLO_CURTO.toString());
        tanque.setComando("DESLIGAR");
        tanque.setPrograma(modoOperacao.getPrograma());
        tanque.setNivel(modoOperacao.getNivel());
        tanque.setResposta("App => comandou DESLIGAR");
        tanque.setStatus("App Conectado");
        referencia.setValue(tanque);
    }

    public void Ligar(View view) {
        tanque.setComando("LIGAR");
        tanque.setPrograma(modoOperacao.getPrograma());
        tanque.setNivel(modoOperacao.getNivel());
        tanque.setResposta("App => comandou LIGAR");
        tanque.setStatus("App Conectado");
        referencia.setValue(tanque);
    }

    public void alerta() {
        //Cria o gerador do AlertDialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        //define o titulo
        builder.setTitle("Sistema de Comando do Tanquinho");
        //define a mensagem
        builder.setMessage("DESEJA REALMENTE LIGAR ?");
        //define um botão como positivo
        builder.setPositiveButton("SIM", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
                tanque.setComando("SIM");
                tanque.setPrograma(modoOperacao.getPrograma());
                tanque.setNivel(modoOperacao.getNivel());
                tanque.setResposta("App => comandou SIM");
                tanque.setStatus("App Conectado");
                referencia.setValue(tanque);
            }
        });
        //define um botão como negativo.
        builder.setNegativeButton("NÃO", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
                tanque.setComando("NÃO");
                tanque.setPrograma(modoOperacao.getPrograma());
                tanque.setNivel(modoOperacao.getNivel());
                tanque.setResposta("App => comandou NÃO");
                tanque.setStatus("App Conectado");
                referencia.setValue(tanque);
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}

