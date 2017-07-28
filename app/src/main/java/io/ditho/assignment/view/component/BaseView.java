package io.ditho.assignment.view.component;

public interface BaseView<T> {

    void startLoad();
    void finishLoad();

    void updateModel(T model);

    void goBack();
    void displayMessage(String title, String message);

}
