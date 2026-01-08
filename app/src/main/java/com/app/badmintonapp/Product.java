package com.app.badmintonapp; // 패키지명은 본인 프로젝트에 맞게 수정

public class Product {
    // 상품명
    String name;

    // 해당 가격
    String price;

    // 쇼핑창 이름
    String ShopName;

    // 상품 이미지
    int imageProduct;

    // 카테고리
    String category;

    public Product(String name, String price, String mallName, int imageResId, String category) {
        this.name = name;
        this.price = price;
        this.ShopName = mallName;
        this.imageProduct = imageResId;
        this.category = category;
    }

    public String getName() { return name; }
    public String getPrice() { return price; }
    public String getShopName() { return ShopName; }
    public int getImageProduct() { return imageProduct; }
    public String getCategory() { return category; }
}