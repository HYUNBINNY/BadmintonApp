package com.app.badmintonapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class ShoppingAdapter extends RecyclerView.Adapter<ShoppingAdapter.ViewHolder> {

    //상품 리스트
    private ArrayList<Product> productList;

    public ShoppingAdapter(ArrayList<Product> productList) {
        this.productList = productList;
    }

    // 카테고리 데이터 갱신시
    public void updateList(ArrayList<Product> newList) {
        this.productList = newList;
        notifyDataSetChanged(); // 리스트 새로고침
    }

    //빈화면 툴
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // 아까 만든 item_product.xml을 가져옵니다.
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.shoppingitemlist, parent, false);
        return new ViewHolder(view);
    }

    //해당 데이터 입력
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Product item = productList.get(position);

        holder.tvName.setText(item.getName());
        holder.tvPrice.setText(item.getPrice());
        holder.tvMall.setText(item.getShopName());

        holder.ivImage.setImageResource(item.getImageProduct());
    }

    //개수
    @Override
    public int getItemCount() {
        return productList.size();
    }

    // 뷰홀더: xml의 컴포넌트들을 기억
    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivImage;
        TextView tvName, tvPrice, tvMall;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivImage = itemView.findViewById(R.id.iv_product_image);
            tvName = itemView.findViewById(R.id.tv_product_name);
            tvPrice = itemView.findViewById(R.id.tv_product_price);
            tvMall = itemView.findViewById(R.id.tv_mall_name);
        }
    }
}