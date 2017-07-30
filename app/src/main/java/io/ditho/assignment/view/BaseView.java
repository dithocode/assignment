package io.ditho.assignment.view;

public interface BaseView<T> {

    void startLoad();
    void finishLoad();

    void updateModel(T model);

    boolean goBack();
    void displayMessage(String title, String message);

}
