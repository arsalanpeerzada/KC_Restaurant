package com.example.smartrestaurant.handler;

import android.os.Bundle;
import android.support.annotation.AnimRes;
import android.support.annotation.IdRes;
import android.support.annotation.IntRange;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.transition.TransitionInflater;
import android.util.Pair;
import android.view.View;

import com.example.smartrestaurant.R;
import com.example.smartrestaurant.util.PermissionsUtil;

import java.util.List;

/**
 * Created by Dell 5521 on 11/28/2016.
 */

public class FragmentHandler {

    FragmentManager fragmentManager;
    @IdRes
    int containerId;
    FragmentActivity context;

    public FragmentHandler(@IdRes int containerId, FragmentActivity context) {
        this.containerId = containerId;
        this.context = context;
        fragmentManager = context.getSupportFragmentManager();
    }

    public FragmentHandler(@IdRes int containerId, FragmentManager fragmentManager) {
        this.containerId = containerId;
        this.fragmentManager = fragmentManager;
    }


    /**
     * @param fragment          Fragment
     * @param addToStack        adding to stack or not
     * @param replaceFragment   replace fragment or not
     * @param fragmentAnimationConstant Range 1 - 4
     *                          1 = animate only on Pre-Lollipop
     *                          2 = animate only on Lollipop or Higher
     *                          3 = animation in all android version
     *                          4 = do not animate in all android version
     * @param viewAndString     Transition ViewString Pair for Lollipop or higher
     * @param animRes           anim res for fragment animation
     */
    public void addReplaceFragment(Fragment fragment,
                                   boolean addToStack,
                                   boolean replaceFragment,
                                   @IntRange(from = 1, to = 4) int fragmentAnimationConstant,
                                   @Nullable List<Pair<View, String>> viewAndString,
                                   @AnimRes Integer... animRes) {

        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        if (PermissionsUtil.isLollipopOrHigher() && viewAndString != null) {
            fragment.setSharedElementEnterTransition(TransitionInflater.from(context)
                    .inflateTransition(R.transition.shared_transition));
            fragment.setEnterTransition(TransitionInflater.from(context)
                    .inflateTransition(android.R.transition.fade));
            fragment.setAllowEnterTransitionOverlap(true);
            fragment.setAllowReturnTransitionOverlap(true);

            /*This bundle is added for adding transition animation from list item
                cux transition name must be different for each item
             */
            Bundle bundle = new Bundle();
            for (int i = 0; i < viewAndString.size() ; i++){
                bundle.putString("Transition"+i,viewAndString.get(i).second);
            }
            fragment.setArguments(bundle);
            /*Bundle end*/


            for (Pair<View, String> i : viewAndString)
                fragmentTransaction.addSharedElement(i.first, i.second);
        }

        switch (fragmentAnimationConstant) {
            case 1:
                if (!PermissionsUtil.isLollipopOrHigher())
                    addCustomAnimation(fragmentTransaction, animRes);
                break;
            case 2:
                if (PermissionsUtil.isLollipopOrHigher())
                    addCustomAnimation(fragmentTransaction, animRes);
                break;
            case 3:
                addCustomAnimation(fragmentTransaction, animRes);
                break;
            case 4:
                addCustomAnimation(null, animRes);
                break;
        }

        if (replaceFragment) {
            fragmentTransaction.replace(containerId, fragment, fragment.getClass().getName());
        } else {
            fragmentTransaction.add(containerId, fragment, fragment.getClass().getName());
        }
        if (addToStack) {
            fragmentTransaction.addToBackStack(fragment.getClass().getName());
        }
        fragmentTransaction.commitAllowingStateLoss();
    }

    private void addCustomAnimation(FragmentTransaction fragmentTransaction, @AnimRes Integer... animRes) {
        try {
            if (animRes != null) {
                if (animRes.length == 2)
                    fragmentTransaction.setCustomAnimations(animRes[0], animRes[1]);
                else if (animRes.length == 4)
                    fragmentTransaction.setCustomAnimations(animRes[0], animRes[1], animRes[2], animRes[3]);
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
