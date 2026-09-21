package com.example.recyclerview;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    Button btLoad;
    
    // Đổi từ static sang biến thông thường
    private List<Article> articleList = new ArrayList<>();
    private ArticleAdapter articleAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), new androidx.core.view.OnApplyWindowInsetsListener() {
            @Override
            public WindowInsetsCompat onApplyWindowInsets(android.view.View v, WindowInsetsCompat insets) {
                Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
                v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
                return insets;
            }
        });

        if (articleList.isEmpty()) {
            articleList.add(new Article("IPhone 18", "iPhone 18 Pro và Pro Max sử dụng thiết kế nhôm nguyên khối, mặt trước Ceramic Shield 2 và mặt sau Ceramic Shield. Bảng màu gồm Đen, Bạc, Băng Thanh và Đỏ Burgundy.", R.drawable.img_1_2));
            articleList.add(new Article("Honda SH Mode 2027", "Honda SH Mode 2027 vừa được Honda Việt Nam giới thiệu với nhiều thay đổi tập trung vào trang bị, diện mạo và khả năng kiểm soát khí thải. Mẫu xe tay ga này dự kiến được đưa ra thị trường từ ngày 2/10/2026, đồng thời đáp ứng tiêu chuẩn khí thải EURO 4.", R.drawable.img_1_3));
            articleList.add(new Article("Tinh Hà Say Hi", "Tinh Hà “Say Hi” chính thức ra mắt tại TP. Hồ Chí Minh, dàn cast chính gồm 24 Anh Trai. Sự kiện thu hút hàng nghìn người hâm mộ. Chương trình hứa hẹn mang đến hành trình âm nhạc mới với quy mô đầu tư lớn.", R.drawable.img_1_4));
        }

        recyclerView = findViewById(R.id.recyclerView);
        btLoad = findViewById(R.id.btLoad);
        
        // Cấu hình adapter và bắt sự kiện click
        articleAdapter = new ArticleAdapter(this, articleList, new ArticleAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Article article, int position) {
                // Đóng gói dữ liệu gửi qua DetailActivity
                Intent intent = new Intent(MainActivity.this, ArticleDetailActivity.class);
                intent.putExtra("ARTICLE", article);
                intent.putExtra("POSITION", position);
                // Mở DetailActivity và chờ kết quả trả về
                startActivityForResult(intent, 1);
            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        
        // Gán adapter ngay lập tức để hiện data luôn không cần chờ bấm nút
        recyclerView.setAdapter(articleAdapter);
        
        btLoad.setOnClickListener(new android.view.View.OnClickListener() {
            @Override
            public void onClick(android.view.View v) {
                recyclerView.setAdapter(articleAdapter);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data != null) {
            Article updatedArticle = (Article) data.getSerializableExtra("UPDATED_ARTICLE");
            int position = data.getIntExtra("POSITION", -1);

            if (position != -1 && updatedArticle != null) {
                // Cập nhật vào list
                articleList.set(position, updatedArticle);
                // Báo cho Adapter biết vị trí này đã thay đổi
                articleAdapter.notifyItemChanged(position);
            }
        }
    }
}