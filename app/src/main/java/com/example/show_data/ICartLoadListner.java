package com.example.show_data;

import java.util.List;

public interface ICartLoadListner {

    void onCartLoadSuccess(List <CartModel> cartlist);
    void onCartLoadFailed(String message);
}
