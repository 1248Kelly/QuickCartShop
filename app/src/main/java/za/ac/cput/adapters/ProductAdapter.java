package za.ac.cput.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import za.ac.cput.R;
import za.ac.cput.model.CartItem;
import za.ac.cput.model.Product;
import za.ac.cput.ui.product.ProductDetailsActivity;
import za.ac.cput.util.LocalCart;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {

    private List<Product> products;


    public void setProducts(List<Product> products) {
        this.products = products;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_product, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Product product = products.get(position);
        holder.bind(product);
    }

    @Override
    public int getItemCount() {
        return products != null ? products.size() : 0;
    }

    static class ProductViewHolder extends RecyclerView.ViewHolder {
        private ImageView productImage;
        private TextView productName, productPrice, productDescription;

        private Button btnAddToCart;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            productImage = itemView.findViewById(R.id.productImage);
            productName = itemView.findViewById(R.id.productName);
            productPrice = itemView.findViewById(R.id.productPrice);
            productDescription = itemView.findViewById(R.id.productDescription);
            btnAddToCart = itemView.findViewById(R.id.btnAddToCart);
        }

        public void bind(Product product) {
            productName.setText(product.getProductName());
            productPrice.setText(String.format("R%.2f", product.getProductPrice()));
            productDescription.setText(product.getProductDescription());

            loadProductImage(product.getProductId());


            itemView.setOnClickListener(v -> {
                Context context = v.getContext();
                Intent intent = new Intent(context, ProductDetailsActivity.class);
                intent.putExtra("PRODUCT_ID", product.getProductId());
                intent.putExtra("PRODUCT_NAME", product.getProductName());
                intent.putExtra("PRODUCT_PRICE", product.getProductPrice());
                intent.putExtra("PRODUCT_DESCRIPTION", product.getProductDescription());
                intent.putExtra("PRODUCT_STOCK", product.getStockAvailability());
                context.startActivity(intent);
            });


            btnAddToCart.setOnClickListener(v -> {
                int quantity = 1;

//                CartItem cartItem = new CartItem.Builder()
//                        .setProductId(product.getProductId())
//                        .setQuantity(quantity)
//                        .setPrice(product.getProductPrice())
//                        .setTotalPrice(product.getProductPrice() * quantity)
//                        .build();


                CartItem cartItem = new CartItem.Builder()
                        .setProductId(product.getProductId())
                        .setProductName(product.getProductName()) // new
                        .setProductImageUrl("http://10.0.2.2:8080/mobileApp/Product/image/" + product.getProductId()) // new
                        .setQuantity(quantity)
                        .setPrice(product.getProductPrice())
                        .setTotalPrice(product.getProductPrice() * quantity)
                        .build();


                LocalCart.addItem(cartItem);

                Toast.makeText(v.getContext(),
                        product.getProductName() + " added to cart",
                        Toast.LENGTH_SHORT).show();
            });
        }

        private void loadProductImage(Long productId) {
            if (productId == null) {
                Glide.with(itemView.getContext())
                        .load(R.drawable.placeholder_image)
                        .into(productImage);
                return;
            }

            String imageUrl = "http://10.0.2.2:8080/mobileApp/Product/image/" + productId;

            Glide.with(itemView.getContext())
                    .load(imageUrl)
                    .placeholder(R.drawable.placeholder_image)
                    .error(R.drawable.placeholder_image)
                    .skipMemoryCache(true)
                    .diskCacheStrategy(com.bumptech.glide.load.engine.DiskCacheStrategy.NONE)
                    .into(productImage);
        }
    }
    }
