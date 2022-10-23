package co.edu.paseo_viernes;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    EditText etcodigo, etnombre, etciudad, etcantidad;
    CheckBox cbactivo;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    String codigo, nombre, ciudad, cantidad;

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
    }

    public void Adicionar(View view){
        codigo=etcodigo.getText().toString();
        nombre=etnombre.getText().toString();
        ciudad=etciudad.getText().toString();
        cantidad=etcantidad.getText().toString();
        if (codigo.isEmpty() || nombre.isEmpty() || ciudad.isEmpty() || cantidad.isEmpty()){
            Toast.makeText(this, "Todos los datos son requeridos", Toast.LENGTH_SHORT).show();
            etcodigo.requestFocus();
        }
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

    private void Limpiar_campos(){
        etcodigo.setText("");
        etciudad.setText("");
        etnombre.setText("");
        etcantidad.setText("");
        etcodigo.requestFocus();
    }
}