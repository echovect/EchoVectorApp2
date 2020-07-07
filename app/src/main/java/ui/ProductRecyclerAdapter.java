package ui;

import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.content.Context;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.echovectorapp2.R;
import com.squareup.picasso.Picasso;


import java.util.List;

import model.Product;

public class ProductRecyclerAdapter extends RecyclerView.Adapter<ProductRecyclerAdapter.ViewHolder> {

    private Context context;
    private List<Product> productList;

    public ProductRecyclerAdapter(Context context, List<Product> productList) {
        this.context = context;
        this.productList = productList;
    }

    @NonNull
    @Override
    public ProductRecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.product_row, viewGroup, false);

        return new ViewHolder(view, context);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductRecyclerAdapter.ViewHolder viewHolder, int position) {

        Product product = productList.get(position);
        String imageUrl;

        viewHolder.title.setText(product.getTitle());
        viewHolder.description.setText(product.getDescription());
//        viewHolder.name.setText(product.getUserName());
        imageUrl = product.getImageUrl();
        //1 hour ago..
        //Source: https://medium.com/@shaktisinh/time-a-go-in-android-8bad8b171f87
        String timeAgo = (String) DateUtils.getRelativeTimeSpanString(product
                .getTimeAdded()
                .getSeconds() * 1000);
        viewHolder.dateAdded.setText(timeAgo);

        boolean isExpanded = productList.get(position).isExpanded();
        viewHolder.expandableLayout.setVisibility(isExpanded ? View.VISIBLE : View.GONE);


        /*
         Use Picasso library to download and show image
         */
        Picasso.get()
                .load(imageUrl)
                .placeholder(R.drawable.logo2)
                .fit()
                .into(viewHolder.image);

    }


    @Override
    public int getItemCount() {
        return productList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView  title, description, dateAdded;
        public ImageView image;
        ConstraintLayout expandableLayout;

        public ViewHolder(@NonNull View itemView, Context ctx) {
            super(itemView);
            context = ctx;

            title = itemView.findViewById(R.id.product_title_list);
            description = itemView.findViewById(R.id.product_description_list);
            dateAdded = itemView.findViewById(R.id.product_timestamp_list);
            image = itemView.findViewById(R.id.product_image_list);
            expandableLayout=itemView.findViewById(R.id.expandableLayout);


            //Expand Collapse the Product
            title.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Product product= productList.get(getAdapterPosition());
                    product.setExpanded(!product.isExpanded());
                    notifyItemChanged(getAdapterPosition());

                }
            });
//            name = itemView.findViewById(R.id.product_row_username);
//            shareButton = itemView.findViewById(R.id.product_row_share_button);
//            shareButton.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    //context.startActivity();
//                }
//            });

        }
    }

}
