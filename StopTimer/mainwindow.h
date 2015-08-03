#ifndef MAINWINDOW_H
#define MAINWINDOW_H

#include <QMainWindow>
#include <QTime>
#include <QTimer>
#include <QList>
#include <QStandardItemModel>
#include <QKeyEvent>

namespace Ui {
class MainWindow;
}

class MainWindow : public QMainWindow
{
    Q_OBJECT

public:
    explicit MainWindow(QWidget *parent = 0);
    ~MainWindow();

private slots:
    void on_recordButton_clicked();

    void on_stopButton_clicked();

    void saveToFile();

    void updateLabel();

signals:
    void keyCaught(QKeyEvent *e);

protected:

    virtual void keyPressEvent(QKeyEvent *event);

private:
    void resetTime();
    void startAndRecord();
    void buttonStop();
    void buttonRecord();
    void updateListView();
    void checkBoxKeepTop(int status);
    QString formatMills(long mills);
    Ui::MainWindow *ui;
    QTime *time;
    QTimer *ui_timer;
    QList<long> *list;
    QStandardItemModel *listModel;
    long tmpTime;
};

#endif // MAINWINDOW_H
