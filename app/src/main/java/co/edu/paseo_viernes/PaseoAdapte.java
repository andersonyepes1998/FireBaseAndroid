package co.edu.paseo_viernes;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class PaseoAdapte extends RecyclerView.Adapter <PaseoAdapte.paseoViewHolder> {

    ArrayList<ClsPaseo> objpaseo;

    public PaseoAdapte(ArrayList<ClsPaseo> objpaseo) {
        this.objpaseo = objpaseo;
    }

    @NonNull
    @Override
    public paseoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.paseorecursos,null,false);
        return new PaseoAdapte.paseoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull paseoViewHolder holder, int position) {
        holder.codigo.setText(objpaseo.get(position).getCodigo().toString());
        holder.nombre.setText(objpaseo.get(position).getNombre().toString());
        holder.ciudad.setText(objpaseo.get(position).getCiudad().toString());
        holder.cantidad.setText(objpaseo.get(position).getCantidad().toString());
    }

    @Override
    public int getItemCount() {
        return objpaseo.size();
    }

    public static class paseoViewHolder extends RecyclerView.ViewHolder {
        TextView codigo,nombre,ciudad,cantidad;
        public paseoViewHolder(@NonNull View itemView) {
            super(itemView);
            codigo = itemView.findViewById(R.id.etcodigo);
            nombre = itemView.findViewById(R.id.etnombre);
            ciudad = itemView.findViewById(R.id.etciudad);
            cantidad = itemView.findViewById(R.id.etcantidad);
        }
    }
}
