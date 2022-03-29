package edu.neu.covidcareapp.activities.ui.home;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import edu.neu.covidcareapp.R;
import edu.neu.covidcareapp.activities.dailyCheck.CheckActivity;
import edu.neu.covidcareapp.activities.news.NewsActivity;

public class HomeFragment extends Fragment {

//    private HomeViewModel homeViewModel;
//    private FragmentHomeBinding binding;

    Activity communityActivity;
    Activity newsActivity;
    Activity checkActivity;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

//        homeViewModel =
//                new ViewModelProvider(this).get(HomeViewModel.class);
//
//        binding = FragmentHomeBinding.inflate(inflater, container, false);
//        View root = binding.getRoot();
//
//        final TextView textView = binding.textHome;
//        homeViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
//            @Override
//            public void onChanged(@Nullable String s) {
//                textView.setText(s);
//            }
//        });

        communityActivity = getActivity();
        newsActivity = getActivity();
        checkActivity = getActivity();

        View root = inflater.inflate(R.layout.fragment_home,container,false);
        return root;
    }


    public void onStart(){
        super.onStart();
        Button CommBtn = (Button) communityActivity.findViewById(R.id.communityBtn);
        Button NewsBtn = (Button) newsActivity.findViewById(R.id.newsABtn);
        Button CheckBtn = (Button) checkActivity.findViewById(R.id.checkBtn);

        CommBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(communityActivity, edu.neu.covidcareapp.activities.community.communityActivity.class);
                startActivity(intent);
            }
        });


        NewsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(newsActivity, NewsActivity.class);
                startActivity(intent);
            }
        });



        CheckBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(checkActivity, CheckActivity.class);
                startActivity(intent);
            }
        });

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
       // binding = null;
    }
}