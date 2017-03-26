package org.ia.transporter.events;

/**
 * Created by Administrator on 2017/2/11 0011.
 */

public class ToastEvent {

    private String toast;

    public ToastEvent(String toast) {
        this.toast = toast;
    }

    public String getToast() {
        return toast;
    }

    public void setToast(String toast) {
        this.toast = toast;
    }
}
