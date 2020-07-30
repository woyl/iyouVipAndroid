package com.jfkj.im.TIM.base;



import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

public class BaseTimFragment extends Fragment {

    public void forward(Fragment fragment, boolean hide) {
        forward(getId(), fragment, null, hide);
    }

    public void forward(int viewId, Fragment fragment, String name, boolean hide) {
        FragmentTransaction trans = getActivity().getSupportFragmentManager().beginTransaction();
        if (hide) {
            trans.hide(this);
            trans.add(viewId, fragment);
        } else {
            trans.replace(viewId, fragment);
        }

        trans.addToBackStack(name);
        trans.commitAllowingStateLoss();
    }

    public void backward() {
        getFragmentManager().popBackStack();
    }
}
