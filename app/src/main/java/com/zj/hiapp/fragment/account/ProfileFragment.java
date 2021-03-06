package com.zj.hiapp.fragment.account;

import android.os.Bundle;
import android.view.View;

import com.zj.common.ui.component.HiBaseFragment;
import com.zj.hiapp.R;
import com.zj.hiapp.databinding.FragmentProfilePageBinding;
import com.zj.hiapp.router.HiRoute;

public class ProfileFragment extends HiBaseFragment<FragmentProfilePageBinding> {
    @Override
    public int getLayoutId() {
        return R.layout.fragment_profile_page;
    }

    @Override
    protected void initEvent() {
        binding.tvLogin.setOnClickListener(v -> {
                    if (AccountManager.INSTANCE.getUserModel() != null) {
                        return;
                    }

                    HiRoute.INSTANCE.startActivity(requireContext(),
                            null,
                            HiRoute.Destination.ACCOUNT_LOGIN,
                            -1);
                }
        );

        AccountManager.INSTANCE.observeLogin(this, userModel -> {
            if (userModel != null) {
                binding.setUser(userModel);
                AccountManager.INSTANCE.getCoinInfo(this, coinInfoModel -> {
                    if (coinInfoModel != null) {
                        binding.setCoinInfo(coinInfoModel);
                    }
                });
            }
        });

        binding.itemCollection.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putString("goodsSign", "Y9z2luxNyt1Uv1IxwfDZeoUikQeR7J48_JQ9SyNG6Fj");
            HiRoute.INSTANCE.startActivity(requireContext(), bundle, HiRoute.Destination.DETAIL_MAIN, 0);
        });
    }

}
