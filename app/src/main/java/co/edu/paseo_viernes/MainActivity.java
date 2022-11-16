package co.edu.paseo_viernes;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    EditText etcodigo, etnombre, etciudad, etcantidad;
    CheckBox cbactivo;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    String codigo, nombre, ciudad, cantidad, codigo_id;
    byte sw;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Ocultar la barra de titulo y asociar objetos xml
        etcodigo=findViewById(R.id.etcodigo);
        etnombre=findViewById(R.id.etnombre);
        etciudad=findViewById(R.id.etciudad);
        etcantidad=findViewById(R.id.etcantidad);
        cbactivo=findViewById(R.id.cbactivo);
        sw=0;
    }

    public void Adicionar(View view){
        codigo=etcodigo.getText().toString();
        nombre=etnombre.getText().toString();
        ciudad=etciudad.getText().toString();
        cantidad=etcantidad.getText().toString();
        if (codigo.isEmpty() || nombre.isEmpty() || ciudad.isEmpty() || cantidad.isEmpty()) {
            Toast.makeText(this, "Todos los datos son requeridos", Toast.LENGTH_SHORT).show();
            etcodigo.requestFocus();
        }else{
            // Create a new user with a first and last name
            Map<String, Object> user = new HashMap<>();
            user.put("Codigo", codigo);
            user.put("Nombre", nombre);
            user.put("Ciudad", ciudad);
            user.put("Cantidad", cantidad);
            user.put("Activo", "si");

// Add a new document with a generated ID
            db.collection("factura")
                    .add(user)
                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            Toast.makeText(MainActivity.this, "Datos Guardados", Toast.LENGTH_SHORT).show();
                            Limpiar_campos();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(MainActivity.this, "Error guardando datos", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

    public void Consultar (View view){
        codigo=etcodigo.getText().toString();
        if (codigo.isEmpty()){
            Toast.makeText(this, "Codigo es requerido", Toast.LENGTH_SHORT).show();
            etcodigo.requestFocus();
        }
        else{
            db.collection("factura")
                    .whereEqualTo("Codigo",codigo)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                sw=1;
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    if (document.getString("Activo").equals("No")){
                                        Toast.makeText(MainActivity.this, "Documento existe pero esta anulado", Toast.LENGTH_SHORT).show();
                                        Limpiar_campos();
                                    }
                                    else{
                                        codigo_id=document.getId();
                                        etnombre.setText(document.getString("Nombre"));
                                        etciudad.setText(document.getString("Ciudad"));
                                        etcantidad.setText(document.getString("Cantidad"));
                                        cbactivo.setChecked(true);
                                    }
                                }
                            } else {
                                Toast.makeText(MainActivity.this, "Documento no existe", Toast.LENGTH_SHORT).show();
                                //Log.w(TAG, "Error getting documents.", task.getException());
                            }
                        }
                    });
        }
    }

    public void Modificar(View view) {
        if (sw == 0) {
            Toast.makeText(this, "Para modificar debe primero consultar", Toast.LENGTH_SHORT).show();
            etcodigo.requestFocus();
        } else {
            codigo = etcodigo.getText().toString();
            nombre = etnombre.getText().toString();
            ciudad = etciudad.getText().toString();
            cantidad = etcantidad.getText().toString();
            if (codigo.isEmpty() || nombre.isEmpty() || ciudad.isEmpty() || cantidad.isEmpty()) {
                Toast.makeText(this, "Todos los datos son requeridos", Toast.LENGTH_SHORT).show();
                etcodigo.requestFocus();
            } else {
                // Create a new user with a first and last name
                Map<String, Object> user = new HashMap<>();
                user.put("Codigo", codigo);
                user.put("Nombre", nombre);
                user.put("Ciudad", ciudad);
                user.put("Cantidad", cantidad);
                user.put("Activo", "si");

// Add a new document with a generated ID
                db.collection("factura").document(codigo_id)
                        .set(user)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(MainActivity.this, "datos actualizado correctamente", Toast.LENGTH_SHORT).show();
                                Limpiar_campos();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(MainActivity.this, "Error al actualizar datos correctamente", Toast.LENGTH_SHORT).show();
                            }
                        });

            }
        }
    }

    public void Eliminar(View view) {
        if (sw == 0) {
            Toast.makeText(this, "Para modificar debe primero consultar", Toast.LENGTH_SHORT).show();
            etcodigo.requestFocus();
        }
        else {

                db.collection("factura").document(codigo_id)
                        .delete()
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(MainActivity.this, "Documento eliminado", Toast.LENGTH_SHORT).show();
                                Limpiar_campos();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(MainActivity.this, "Error eliminando documento", Toast.LENGTH_SHORT).show();
                            }
                        });

            }
        }

    public void Anular(View view) {
        if (sw == 0) {
            Toast.makeText(this, "Para anular debe primero consultar", Toast.LENGTH_SHORT).show();
            etcodigo.requestFocus();
        } else {
            codigo = etcodigo.getText().toString();
            nombre = etnombre.getText().toString();
            ciudad = etciudad.getText().toString();
            cantidad = etcantidad.getText().toString();
            if (codigo.isEmpty() || nombre.isEmpty() || ciudad.isEmpty() || cantidad.isEmpty()) {
                Toast.makeText(this, "Todos los datos son requeridos", Toast.LENGTH_SHORT).show();
                etcodigo.requestFocus();
            } else {
                // Create a new user with a first and last name
                Map<String, Object> user = new HashMap<>();
                user.put("Codigo", codigo);
                user.put("Nombre", nombre);
                user.put("Ciudad", ciudad);
                user.put("Cantidad", cantidad);
                user.put("Activo", "No");

// Add a new document with a generated ID
                db.collection("factura").document(codigo_id)
                        .set(user)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(MainActivity.this, "datos anulados correctamente", Toast.LENGTH_SHORT).show();
                                Limpiar_campos();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(MainActivity.this, "Error al anular datos correctamente", Toast.LENGTH_SHORT).show();
                            }
                        });

            }
        }
    }

    public void Cancelar(View view){

        Limpiar_campos();
    }

    public void Listar(View view){
        Intent intlistar=new Intent(this,ListarActivity2.class);
        startActivity(intlistar);
    }

    private void Limpiar_campos(){
        etcodigo.setText("");
        etciudad.setText("");
        etnombre.setText("");
        etcantidad.setText("");
        etcodigo.requestFocus();
        cbactivo.setChecked(false);
        sw=0;
    }
}