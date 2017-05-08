/**
 * Project Name: itee
 * File Name:	 BaseEditFragment.java
 * Package Name: cn.situne.itee.fragment
 * Date:		 2015-03-26
 * Copyright (c) 2015, itee@kenes.com.cn All Rights Reserved.
 *
 */

package cn.situne.itee.fragment;

/**
 * ClassName:BaseEditFragment <br/>
 * Function: Editable of fragment . <br/>
 * Date: 2015-03-26 <br/>
 *
 * @author dengjunzeng
 * @version 1.0
 * @see
 */
public abstract class BaseEditFragment extends BaseFragment {


    public enum FragmentMode {
        FragmentModeEdit(1),
        FragmentModeAdd(2),
        FragmentModeBrowse(3);

        private int value = 0;

        FragmentMode(int value) {
            this.value = value;
        }

        public static FragmentMode valueOf(int value) {
            switch (value) {
                case 1:
                    return FragmentModeEdit;
                case 2:
                    return FragmentModeAdd;
                case 3:
                default:
                    return FragmentModeBrowse;
            }
        }

        public int value() {
            return this.value;
        }
    }

    private FragmentMode fragmentMode;

    public FragmentMode getFragmentMode() {
        return fragmentMode;
    }

    public void setFragmentMode(FragmentMode fragmentMode) {
        this.fragmentMode = fragmentMode;
    }
}